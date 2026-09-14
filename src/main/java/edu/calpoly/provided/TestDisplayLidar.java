package edu.calpoly.provided;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Course-provided LiDAR simulator used to test DisplayLidar.java. */
public class TestDisplayLidar {
    private static final int PORT = 5000;
    private static final long DELAY_MS = 35;

    private record Point(double x, double y, double z) {}

    public static void main(String[] args) {
        System.out.println("TestDisplayLidar running on localhost:" + PORT);
        System.out.println("Run DisplayLidar.java and observe the 2D LiDAR map.");

        List<Point> scan = createScan();
        System.out.println("Simulated scan contains " + scan.size() + " points.");

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try (Socket socket = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    String mode = in.readLine();
                    if (!"RECEIVE".equals(mode)) continue;

                    System.out.println("DisplayLidar connected. Sending simulated LiDAR points...");
                    sendScan(out, scan);
                } catch (IOException e) {
                    System.out.println("DisplayLidar disconnected. Waiting for another connection...");
                }
            }
        } catch (IOException e) {
            System.err.println("Test server stopped: " + e.getMessage());
        }
    }

    private static void sendScan(PrintWriter out, List<Point> scan) throws IOException {
        while (!out.checkError()) {
            for (Point point : scan) {
                out.printf(Locale.US, "LIDAR,%.2f,%.2f,%.2f%n", point.x(), point.y(), point.z());
                out.flush();
                sleep();
                if (out.checkError()) return;
            }
        }
    }

    private static List<Point> createScan() {
        List<Point> points = new ArrayList<>();

        // Outer room boundary.
        addHorizontal(points, -4.0, 4.0, 4.0);
        addVertical(points, 4.0, -4.0, 4.0);
        addHorizontal(points, 4.0, -4.0, -4.0);
        addVertical(points, -4.0, 4.0, -4.0);

        // Interior obstacle: small rectangular structure on the left.
        addHorizontal(points, -2.2, -0.7, 1.0);
        addVertical(points, -0.7, -0.9, 1.0);
        addHorizontal(points, -0.7, -2.2, -0.9);
        addVertical(points, -2.2, 1.0, -0.9);

        // Interior obstacle: partial wall near the upper center-right.
        addVertical(points, 1.1, 0.3, 2.8);

        // Interior obstacle: L-shaped structure near the lower right.
        addHorizontal(points, 1.5, 2.6, -1.9);
        addVertical(points, 2.6, -1.9, -0.9);

        return points;
    }

    private static void addHorizontal(List<Point> points, double xStart, double xEnd, double y) {
        double step = xStart <= xEnd ? 0.10 : -0.10;
        for (double x = xStart; step > 0 ? x <= xEnd + 1e-9 : x >= xEnd - 1e-9; x += step) {
            points.add(new Point(x, y, zFor(points.size())));
        }
    }

    private static void addVertical(List<Point> points, double x, double yStart, double yEnd) {
        double step = yStart <= yEnd ? 0.10 : -0.10;
        for (double y = yStart; step > 0 ? y <= yEnd + 1e-9 : y >= yEnd - 1e-9; y += step) {
            points.add(new Point(x, y, zFor(points.size())));
        }
    }

    private static double zFor(int index) {
        return 0.10 + 0.05 * Math.sin(index * 0.15);
    }

    private static void sleep() throws IOException {
        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("LiDAR simulation interrupted", e);
        }
    }
}
