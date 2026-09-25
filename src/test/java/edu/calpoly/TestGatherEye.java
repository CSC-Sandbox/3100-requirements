package edu.calpoly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Course-provided receiver used to test GatherEye.java.
 *
 * @author Javier Gonzalez-Sanchez (javiergs)
 * @version 1.0 (2026-09-01)
 */
public class TestGatherEye {

  private static final int PORT = 5000;

  public static void main(String[] args) {
    System.out.println("Waiting for gaze data on localhost:" + PORT + "...");
    System.out.println("Expected format: GAZE,X,Y");

    try (ServerSocket server = new ServerSocket(PORT)) {
      while (true) {
        try (Socket socket = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
          String message = in.readLine();
          if (message != null) {
            System.out.println("Gaze Position Received");
            System.out.println(message);
            if (!message.startsWith("GAZE,")) {
              System.out.println("WARNING: message should begin with GAZE,");
            }
          }
        }
      }
    } catch (IOException e) {
      System.err.println("Test server stopped: " + e.getMessage());
    }
  }

}
