package edu.calpoly.messages;

import edu.calpoly.provided.Broker;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Displays received messages and submits validated message input.
 *
 * @author Matthew Davi
 * @version 2.0
 */
public class DisplayMessages extends JFrame {
    private final JTextArea messageArea;
    private final JLabel statusLabel;
    private final JTextField textField;
    private final JTextField senderField;
    private final JTextField subjectField;
    private final JButton sendButton;
    private final MessageSubmission submission;

    /** Creates the message window and submission controls. */
    public DisplayMessages() {
        super("Messages");

        Broker sendingBroker = new Broker("localhost", 5000);
        submission = new MessageSubmission(sendingBroker::send);

        messageArea = new JTextArea(15, 40);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        statusLabel = new JLabel("Waiting for messages...");
        statusLabel.setBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8));

        textField = new JTextField();
        senderField = new JTextField();
        subjectField = new JTextField();
        sendButton = new JButton("Send");

        JPanel fields = new JPanel(new GridLayout(3, 2, 8, 8));
        fields.add(new JLabel("Message:"));
        fields.add(textField);
        fields.add(new JLabel("Sender (optional):"));
        fields.add(senderField);
        fields.add(new JLabel("Subject (requires sender):"));
        fields.add(subjectField);

        JPanel inputPanel = new JPanel(new BorderLayout(8, 8));
        inputPanel.setBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8));
        inputPanel.add(fields, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(8, 8, 0, 8));

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(event -> sendMessage());
        textField.addActionListener(event -> sendMessage());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    /** Submits captured input without blocking the Swing event thread. */
    private void sendMessage() {
        if (!sendButton.isEnabled()) {
            return;
        }

        String text = textField.getText();
        String sender = senderField.getText();
        String subject = subjectField.getText();
        setInputEnabled(false);
        statusLabel.setText("Sending...");

        Thread senderThread = new Thread(() -> {
            try {
                submission.submit(text, sender, subject);
                SwingUtilities.invokeLater(() -> {
                    textField.setText("");
                    statusLabel.setText("Message sent.");
                    setInputEnabled(true);
                });
            } catch (IllegalArgumentException | IllegalStateException e) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText(e.getMessage());
                    setInputEnabled(true);
                });
            }
        }, "message-sender");

        senderThread.setDaemon(true);
        senderThread.start();
    }

    /**
     * Enables or disables input while a submission is running.
     *
     * @param enabled whether input should be enabled
     */
    private void setInputEnabled(boolean enabled) {
        textField.setEnabled(enabled);
        senderField.setEnabled(enabled);
        subjectField.setEnabled(enabled);
        sendButton.setEnabled(enabled);
    }

    /** Starts receiving messages on a background thread. */
    private void startReceiving() {
        Broker broker = new Broker("localhost", 5000);
        MessageReceiver receiver =
                new MessageReceiver(broker, this::displayMessage);

        Thread receiverThread = new Thread(() -> {
            try {
                receiver.run();
            } catch (IllegalStateException e) {
                SwingUtilities.invokeLater(() ->
                        statusLabel.setText("Receiving stopped: "
                                + e.getMessage()));
            }
        }, "message-receiver");

        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    /**
     * Appends a received message on the Swing event thread.
     *
     * @param message received message
     */
    private void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            messageArea.append(message + System.lineSeparator());
            messageArea.setCaretPosition(
                    messageArea.getDocument().getLength());
            statusLabel.setText("Receiving messages...");
        });
    }

    /**
     * Opens the message window and starts receiving messages.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DisplayMessages application = new DisplayMessages();
            application.setVisible(true);
            application.startReceiving();
        });
    }
}