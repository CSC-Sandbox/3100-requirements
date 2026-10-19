package edu.calpoly.visualization;

import edu.calpoly.provided.Broker;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Graphics;

public class DisplayRobot extends JFrame {

    private final RobotPanel robotPanel = new RobotPanel();

    public DisplayRobot() {
        setTitle("Robot Pose Display");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        add(robotPanel);

    }

    private RobotPose parseRobotMessage(String message) {
        String[] parts = message.split(",");

        if (parts.length != 10 || !parts[0].equals("ROBOT")) {
            throw new IllegalArgumentException("Invalid robot message: " + message);
        }

        double[] values = new double[9];

        for (int i = 0; i < 9; i++) {
            values[i] = Double.parseDouble(parts[i + 1]);
        }

        return new RobotPose(
            values[0],
            values[1],
            values[2],
            values[3],
            values[4],
            values[5],
            values[6],
            values[7],
            values[8]
        );
    }

    private void receiveRobotData() {
        Broker broker = new Broker("localhost", 5000);

        Thread receiverThread = new Thread(() -> {
            while (true) {
                try {
                    String message = broker.receive();
                    RobotPose pose = parseRobotMessage(message);

                    SwingUtilities.invokeLater(() -> {
                        robotPanel.setPose(pose);
                    });

                } catch (Exception e) {
                    System.err.println("Error receiving robot data: " + e.getMessage());
                }
            }
        });

        receiverThread.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DisplayRobot display = new DisplayRobot();
            display.setVisible(true);
            display.receiveRobotData();
        });
    }

    private static class RobotPanel extends JPanel {

    private RobotPose pose;

    public RobotPanel() {
        setPreferredSize(new Dimension(600, 600));
    }

    public void setPose(RobotPose pose) {
        this.pose = pose;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("Robot Pose", 20, 30);

        if (pose == null) {
            g.drawString("Waiting for robot data...", 20, 50);
            return;
        }

        double[] values = pose.getValues();

        int x = getWidth() / 2;
        int y = getHeight() / 2;

        int segmentLength = 50;
        double totalAngle = 0;

        for (int i = 0; i < 6; i++) {
            totalAngle += values[i];

            int nextX = x + (int) (segmentLength * Math.cos(totalAngle));
            int nextY = y + (int) (segmentLength * Math.sin(totalAngle));

            g.drawLine(x, y, nextX, nextY);
            g.fillOval(x - 5, y - 5, 10, 10);

            x = nextX;
            y = nextY;
        }

        g.fillOval(x - 5, y - 5, 10, 10);
    }
}

}