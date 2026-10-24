package edu.calpoly.csc3100.mqtt.instructor;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Instructor dashboard for the CSC 3100 MQTT lab.
 *
 * Student -> Instructor: ID:READY
 * Instructor -> Student: ID:KEY
 * Student -> Instructor: ID:ACK:KEY
 *
 * Challenges are sent automatically when students announce readiness.
 */
public final class InstructorApp extends JFrame {
    private static final String BROKER = "tcp://broker.hivemq.com:1883";
    private static final int QOS = 1;
    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private final SecureRandom random = new SecureRandom();
    private final Map<String, Student> students = new LinkedHashMap<>();
    private final ExecutorService io = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "mqtt-instructor-io");
        thread.setDaemon(true);
        return thread;
    });

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Student", "ID", "Status", "Time"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JLabel connection = new JLabel("Disconnected");
    private final JLabel count = new JLabel("Completed: 0");
    private final JTextField session = new JTextField("fall2026", 16);
    private final JButton connectButton = new JButton("Connect");
    private final JButton resetButton = new JButton("New Round");
    private final JTextArea log = new JTextArea(7, 70);

    private volatile MqttClient client;
    private volatile String activeSession;

    /** A participant's current round state. Updated by the MQTT I/O executor. */
    private static final class Student {
        final String id;
        final String name;
        volatile String key;
        volatile String status = "Waiting";
        volatile String time = "";

        Student(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    /** Launches the Swing dashboard. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new InstructorApp().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(),
                        "Startup error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /** Creates the instructor dashboard and loads the roster. */
    private InstructorApp() throws IOException {
        super("CSC 3100 — MQTT Instructor Dashboard");
        loadRoster();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Session:"));
        top.add(session);
        top.add(connectButton);
        top.add(resetButton);
        top.add(connection);
        add(top, BorderLayout.NORTH);

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        log.setEditable(false);
        log.setLineWrap(true);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(count, BorderLayout.NORTH);
        bottom.add(new JScrollPane(log), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        connectButton.addActionListener(e -> connect());
        resetButton.addActionListener(e -> newRound());
        resetButton.setEnabled(false);

        setSize(1050, 640);
        setLocationRelativeTo(null);
        updateTable();
        append("Public MQTT broker: " + BROKER);
        append("Waiting for instructor connection.");
    }

    /** Loads students.csv from the application's resources. */
    private void loadRoster() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/students.csv")) {
            if (input == null) {
                throw new FileNotFoundException("students.csv not found");
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input, StandardCharsets.UTF_8))) {
                String line;
                boolean first = true;
                while ((line = reader.readLine()) != null) {
                    if (first) {
                        first = false;
                        continue;
                    }
                    if (line.isBlank()) continue;
                    String[] parts = line.split(",", 2);
                    if (parts.length != 2) {
                        throw new IOException("Invalid roster line: " + line);
                    }
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    students.put(id, new Student(id, name));
                }
            }
        }
    }

    /** Returns a topic under the selected lab session. */
    private String topic(String suffix) {
        return "csc3100/lab/" + activeSession + "/" + suffix;
    }

    /** Connects to HiveMQ and subscribes to READY and ACK messages. */
    private void connect() {
        String requested = session.getText().trim();
        if (!requested.matches("[a-zA-Z0-9_-]{3,40}")) {
            JOptionPane.showMessageDialog(this,
                    "Session must contain 3–40 letters, digits, underscores or hyphens.");
            return;
        }
        connectButton.setEnabled(false);
        connection.setText("Connecting...");
        activeSession = requested;

        io.execute(() -> {
            MqttClient c = null;
            try {
                c = new MqttClient(BROKER,
                        "csc3100-instructor-" + UUID.randomUUID(),
                        new MemoryPersistence());

                c.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        client = null;
                        SwingUtilities.invokeLater(() -> {
                            connection.setText("Disconnected");
                            connectButton.setEnabled(true);
                            resetButton.setEnabled(false);
                            append("Connection lost: " + cause);
                        });
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        String payload = new String(
                                message.getPayload(), StandardCharsets.UTF_8);
                        // Keep round state changes in a single ordered I/O queue.
                        if (topic.endsWith("/ready")) {
                            io.execute(() -> onReady(payload));
                        } else if (topic.endsWith("/ack")) {
                            io.execute(() -> onAck(payload));
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        // No action required.
                    }
                });

                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                options.setAutomaticReconnect(false);
                options.setConnectionTimeout(10);
                c.connect(options);
                client = c;
                c.subscribe(topic("ready"), QOS);
                c.subscribe(topic("ack"), QOS);

                SwingUtilities.invokeLater(() -> {
                    connection.setText("Connected — " + activeSession);
                    session.setEditable(false);
                    connectButton.setEnabled(false);
                    resetButton.setEnabled(true);
                    append("Connected to HiveMQ.");
                    append("Listening: " + topic("ready"));
                    append("Listening: " + topic("ack"));
                    append("Waiting for students...");
                });
            } catch (MqttException ex) {
                client = null;
                if (c != null) {
                    try {
                        c.close();
                    } catch (MqttException ignored) {
                        // Connection already failed.
                    }
                }
                SwingUtilities.invokeLater(() -> {
                    connection.setText("Connection failed");
                    connectButton.setEnabled(true);
                    append("Connection failed: " + ex.getMessage());
                });
            }
        });
    }

    /** Handles ID:READY and automatically sends a challenge. */
    private void onReady(String payload) {
        String[] p = payload.trim().split(":", -1);
        if (p.length != 2 || !"READY".equals(p[1])) return;
        Student student = students.get(p[0]);
        if (student == null) {
            SwingUtilities.invokeLater(() ->
                    append("Ignored unknown student: " + p[0]));
            return;
        }
        if ("Success".equals(student.status)) return;

        student.status = "Ready";
        SwingUtilities.invokeLater(() -> {
            updateTable();
            append("READY: " + student.name);
        });
        sendChallenge(student);
    }

    /** Validates ID:ACK:KEY and marks the student successful. */
    private void onAck(String payload) {
        String[] p = payload.trim().split(":", -1);
        if (p.length != 3 || !"ACK".equals(p[1])) return;
        Student student = students.get(p[0]);
        if (student == null) {
            SwingUtilities.invokeLater(() ->
                    append("Ignored ACK for unknown ID: " + p[0]));
            return;
        }
        if (student.key == null || !student.key.equals(p[2])) {
            SwingUtilities.invokeLater(() ->
                    append("Invalid ACK: " + student.name));
            return;
        }
        if ("Success".equals(student.status)) return;
        student.status = "Success";
        student.time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        SwingUtilities.invokeLater(() -> {
            updateTable();
            append("SUCCESS: " + student.name);
        });
    }

    /** Generates and publishes a challenge for one student. */
    private void sendChallenge(Student student) {
        try {
            if (student.key == null) student.key = randomKey();
            publish(topic("challenge"), student.id + ":" + student.key);
            if (!"Success".equals(student.status)) {
                student.status = "Challenge sent";
            }
            SwingUtilities.invokeLater(() -> {
                updateTable();
                append("Challenge sent: " + student.name);
            });
        } catch (MqttException ex) {
            SwingUtilities.invokeLater(() ->
                    append("Publish failed for " + student.id + ": " + ex.getMessage()));
        }
    }

    /** Generates a random 10-character challenge key. */
    private String randomKey() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            result.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return result.toString();
    }

    /** Publishes a non-retained MQTT message at QoS 1. */
    private void publish(String topic, String payload) throws MqttException {
        MqttClient c = client;
        if (c == null || !c.isConnected()) {
            throw new MqttException(MqttException.REASON_CODE_CLIENT_NOT_CONNECTED);
        }
        MqttMessage message = new MqttMessage(
                payload.getBytes(StandardCharsets.UTF_8));
        message.setQos(QOS);
        message.setRetained(false);
        c.publish(topic, message);
    }

    /** Resets student statuses and keys; students must publish READY again. */
    private void newRound() {
        io.execute(() -> {
            for (Student student : students.values()) {
                student.key = randomKey();
                student.status = "Waiting";
                student.time = "";
            }
            SwingUtilities.invokeLater(() -> {
                updateTable();
                append("New round started.");
                append("Student statuses reset.");
                append("Waiting for READY messages...");
            });
        });
    }

    /** Refreshes the student table and completion counter on the Swing EDT. */
    private void updateTable() {
        model.setRowCount(0);
        int completed = 0;
        for (Student student : students.values()) {
            model.addRow(new Object[]{
                    student.name, student.id, student.status, student.time
            });
            if ("Success".equals(student.status)) completed++;
        }
        count.setText("Completed: " + completed + " / " + students.size());
    }

    /** Adds a timestamped log entry on the Swing EDT. */
    private void append(String message) {
        String timestamp = LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss"));
        log.append("[" + timestamp + "] " + message + "\n");
        log.setCaretPosition(log.getDocument().getLength());
    }
}
