package edu.calpoly.provided;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/** Course-provided local robot-pose communication service. */
public class RobotPoseServer {
    private static final int PORT = 5000;
    private static final List<PrintWriter> receivers = new CopyOnWriteArrayList<>();
    private static final Random random = new Random();

    public static void main(String[] args) throws Exception {
        System.out.println("Robot Pose Server running on localhost:" + PORT);
        ScheduledExecutorService simulator = Executors.newSingleThreadScheduledExecutor();
        simulator.scheduleAtFixedRate(() -> broadcast(simulatedPose()), 1, 1, TimeUnit.SECONDS);

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                new Thread(() -> handle(socket)).start();
            }
        } finally {
            simulator.shutdownNow();
        }
    }

    private static void handle(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String mode = in.readLine();
            if ("SEND".equals(mode)) {
                String message = in.readLine();
                if (message != null) {
                    System.out.println("Robot Pose Received: " + message);
                    broadcast(message);
                }
                socket.close();
            } else if ("RECEIVE".equals(mode)) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                receivers.add(out);
                // Keep the connection alive until the client disconnects.
                while (in.readLine() != null) { }
                receivers.remove(out);
                socket.close();
            } else {
                socket.close();
            }
        } catch (IOException ignored) { }
    }

    private static void broadcast(String pose) {
        for (PrintWriter out : receivers) {
            out.println(pose);
            if (out.checkError()) receivers.remove(out);
        }
    }

    private static String simulatedPose() {
        double[] v = new double[9];
        for (int i = 0; i < 6; i++) v[i] = -Math.PI + random.nextDouble() * 2 * Math.PI;
        for (int i = 6; i < 9; i++) v[i] = -1.0 + random.nextDouble() * 2.0;
        return String.format(Locale.US,
            "%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f",
            v[0],v[1],v[2],v[3],v[4],v[5],v[6],v[7],v[8]);
    }
}
