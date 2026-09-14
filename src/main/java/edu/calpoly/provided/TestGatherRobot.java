package edu.calpoly.provided;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Course-provided test receiver for the Gather Robot Pose assignment.
 *
 * <p>Run this program before running the student's GatherRobot program.
 * The program listens on localhost port 5000 and prints each message
 * it receives.</p>
 */
public class TestGatherRobot {

    private static final int PORT = 5000;

    public static void main(String[] args) {

        System.out.println("Waiting for robot data on localhost:" + PORT + "...");

        try (ServerSocket server = new ServerSocket(PORT)) {

            while (true) {
                receiveMessage(server);
            }

        } catch (IOException e) {
            System.err.println("Test server stopped: " + e.getMessage());
        }
    }

    private static void receiveMessage(ServerSocket server) throws IOException {

        try (
            Socket socket = server.accept();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            )
        ) {
            String message = in.readLine();

            System.out.println();
            System.out.println("Robot Pose Received");
            System.out.println(message);
        }
    }
}
