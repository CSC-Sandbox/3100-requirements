// package edu.calpoly;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * User Story #35 -- Create Map from LiDAR Data.
 *
 * Receives LIDAR,X,Y,Z messages through Broker, accumulates into a
 * 2D occupancy grid, and displays the grid in window.
 *
 * Test: start edu.calpoly.provided.TestDisplayLidar first, then run this.
 * Run with --ascii to print the map in the terminal instead of a window.
 */
public class DisplayLidar extends JPanel {

    private static final long serialVersionUID = 1L;

    // Broker
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    // Map geometry: simulator room is 8x8 m (walls at +/-4), 1 m margin
    private static final double RESOLUTION = 0.10;   // meters per cell
    private static final double ORIGIN_X = -5.0;     // world x of the left edge
    private static final double ORIGIN_Y = -5.0;     // world y of the bottom edge
    private static final int COLS = 100;             // 10 m / 0.1 m
    private static final int ROWS = 100;

    // Height band that counts as obstacle
    private static final double Z_MIN = 0.0;
    private static final double Z_MAX = 2.0;

    // Java Math fix: (-0.9 + 5.0) / 0.1 == 40.999 in Java, not 41 -> nudge up
    private static final double EPSILON = 1e-9;

    // Display
    private static final int CELL_SIZE_PX = 6;

    // Occupied[row][col]; row 0 is the top of the map
    private final boolean[][] occupied = new boolean[ROWS][COLS];

    public DisplayLidar() {
        setPreferredSize(new Dimension(COLS * CELL_SIZE_PX, ROWS * CELL_SIZE_PX));
        setBackground(Color.WHITE);
    }

    // Parsing: returns {x, y, z}, or null if malformed
    static double[] parseLidar(String message) {
        if (message == null) { return null; }

        String[] fields = message.trim().split(",");
        if (fields.length != 4 || !fields[0].trim().equals("LIDAR")) {
            return null;
        }
        try {
            double x = Double.parseDouble(fields[1].trim());
            double y = Double.parseDouble(fields[2].trim());
            double z = Double.parseDouble(fields[3].trim());
            // parseDouble accepts "NaN"/"Infinity" -- reject them
            if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(z)) {
                return null;
            }
            return new double[] {x, y, z};
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Grid
    static int worldToCol(double x) {
        return (int) Math.floor((x - ORIGIN_X) / RESOLUTION + EPSILON);
    }

    // Flips y: larger world y -> smaller (higher on screen) row
    static int worldToRow(double y) {
        int yIndex = (int) Math.floor((y - ORIGIN_Y) / RESOLUTION + EPSILON);
        return ROWS - 1 - yIndex;
    }

    static boolean inBounds(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    //Marks the cell containing (x, y) as occupied
    synchronized boolean markOccupied(double x, double y) {
        int row = worldToRow(y);
        int col = worldToCol(x);
        if (!inBounds(row, col)) {
            return false;
        }
        occupied[row][col] = true;
        return true;
    }
    // {can delete} For terminal debugging
    synchronized int countOccupied() {
        int count = 0;
        for (boolean[] row : occupied) {
            for (boolean cell : row) {
                if (cell) count++;
            }
        }
        return count;
    }

    // {can delete} For terminal debugging: '#' = occupied, '.' = free/unknown
    synchronized String toAscii() {
        StringBuilder sb = new StringBuilder((COLS + 1) * ROWS);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                sb.append(occupied[r][c] ? '#' : '.');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // Drawing
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   // clears the background
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.BLACK);
        synchronized (this) {
            for(int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (occupied[row][col]) {
                        g2.fillRect(col * CELL_SIZE_PX, row * CELL_SIZE_PX, 
                            CELL_SIZE_PX,CELL_SIZE_PX);

                    }
                }
            }
        }
        int dotSize = CELL_SIZE_PX * 2; 
        int cx = worldToCol(0.0) * CELL_SIZE_PX + CELL_SIZE_PX / 2;
        int cy = worldToRow(0.0) * CELL_SIZE_PX + CELL_SIZE_PX / 2;
        g2.setColor(Color.RED);
        g2.fillRect(cx - dotSize / 2, cy - dotSize / 2, dotSize,
             dotSize);
    }

    // Receive loop + main
    void runReceiveLoop(Broker broker, boolean asciiMode) {
        long received = 0;
        long rejected = 0;

        while (true) {
            String message;
            try {
                message = broker.receive();
            } catch (IllegalStateException e) {
                System.err.println("Broker unavailable (" + e.getMessage() +
                     "). Retrying in 1 s...");
                sleepQuietly(1000);
                continue;
            }

            double[] p = parseLidar(message);
            if (p == null) {
                rejected++;
                System.err.println("Skipping malformed message: " + message);
                continue;
            }
            if (p[2] < Z_MIN || p[2] > Z_MAX) {
                continue;
            }

            markOccupied(p[0], p[1]);
            received++;

            if (asciiMode) { // {can delete} For terminal debugging
                if (received % 100 == 0) {
                    System.out.println("\n--- " + received + " points, "
                            + countOccupied() + " occupied cells, "
                            + rejected + " rejected ---");
                    System.out.print(toAscii());
                }
            } else {
                repaint();
            }
        }
    }

    private static void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        boolean asciiMode = args.length > 0 && args[0].equals("--ascii");
        Broker broker = new Broker(HOST, PORT);
        DisplayLidar map = new DisplayLidar();

        if (asciiMode) { // {can delete} For terminal debugging
            map.runReceiveLoop(broker, true);
            return;
        }

        // Swing components created on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("LiDAR Map");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(map);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        // receive() blocks -> running it on the EDT would freeze I think
        Thread receiver = new Thread(() -> map.runReceiveLoop(broker, false), "lidar-receiver");
        receiver.setDaemon(true);
        receiver.start();
    }
}
