package edu.calpoly.provided;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

/**
 * Course-provided gaze simulator used to test DisplayEye.java.
 *
 * @author Javier Gonzalez-Sanchez (javiergs)
 * @version 1.0 (2026-09-01)
 */
public class TestDisplayEye {
  private static final int PORT = 5000;
  private static final int STEPS_X = 20;
  private static final int ROWS = 6;
  private static final long DELAY_MS = 150;

  public static void main(String[] args) {
    System.out.println("TestDisplayEye running on localhost:" + PORT);
    System.out.println("Run DisplayEye.java and observe the gaze point.");

    try (ServerSocket server = new ServerSocket(PORT)) {
      while (true) {
        try (Socket socket = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

          String mode = in.readLine();
          if (!"RECEIVE".equals(mode)) continue;

          System.out.println("DisplayEye connected. Sending test sequence...");
          sendSnake(out);
        } catch (IOException e) {
          System.out.println("DisplayEye disconnected. Waiting for another connection...");
        }
      }
    } catch (IOException e) {
      System.err.println("Test server stopped: " + e.getMessage());
    }
  }

  private static void sendSnake(PrintWriter out) throws IOException {
    while (!out.checkError()) {
      for (int row = 0; row < ROWS; row++) {
        double y = (double) row / (ROWS - 1);
        boolean leftToRight = row % 2 == 0;

        for (int step = 0; step <= STEPS_X; step++) {
          double x = (double) step / STEPS_X;
          if (!leftToRight) x = 1.0 - x;

          out.printf(Locale.US, "GAZE,%.2f,%.2f%n", x, y);
          out.flush();
          sleep();
        }
      }
    }
  }

  private static void sleep() throws IOException {
    try {
      Thread.sleep(DELAY_MS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IOException("Test sequence interrupted", e);
    }
  }
}
