package edu.calpoly.provided;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;

public class EnterMessage {
    private Broker broker;
    private JFrame frame;
    private JTextArea textArea;
    private JButton sendButton;
    
    public EnterMessage() {
        broker = new Broker("localhost", 5000);
        frame = new JFrame("Enter Message");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());
        
        textArea = new JTextArea();
        sendButton = new JButton("Send");

        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.add(sendButton, BorderLayout.SOUTH);
 
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = textArea.getText().trim();

                if (!message.isEmpty()) {
                    // Update 'send' below to match the exact method name inside your Broker.java file
                    broker.send(message); 
                    textArea.setText(""); // Clear after send
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new EnterMessage();
    }
}
