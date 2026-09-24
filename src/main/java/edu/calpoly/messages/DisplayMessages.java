package edu.calpoly.messages;

import edu.calpoly.provided.Broker;
import edu.calpoly.provided.MessageReceiver;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * A simple GUI for displaying messages received from a message broker.
 * @author Omar Coleman
 */
public class TestDisplayMessages {
public class DisplayMessages extends JFrame {
    private final JTextArea messageArea;
    private final JLabel statusLabel;

    public DisplayMessages() {
        super("Messages");

        messageArea = new JTextArea(15, 40);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        statusLabel = new JLabel("Waiting for messages...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private void startReceiving() {
        Broker broker = new Broker("localhost", 5000);
        MessageReceiver receiver =
                new MessageReceiver(broker, this::displayMessage);

        Thread receiverThread = new Thread(receiver, "message-receiver");
        receiverThread.setDaemon(true);
        receiverThread.start();
    }

    private void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            messageArea.append(message + System.lineSeparator());
            messageArea.setCaretPosition(
                    messageArea.getDocument().getLength());
            statusLabel.setText("Receiving messages...");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DisplayMessages application = new DisplayMessages();
            application.setVisible(true);
            application.startReceiving();
        });
    }
}