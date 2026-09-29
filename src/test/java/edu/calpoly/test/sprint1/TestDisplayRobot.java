package edu.calpoly.test.sprint1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.Random;

/**
 * Course-provided robot-pose simulator used to test DisplayRobot.java.
 *
 * @author Javier Gonzalez-Sanchez (javiergs)
 * @version 1.0 (2026-09-01)
 */
public class TestDisplayRobot {

  private static final int PORT = 5000;
  private static final long DELAY_MS = 1000;
  private static final Random RANDOM = new Random();

  public static void main(String[] args) {
    System.out.println("TestDisplayRobot running on localhost:" + PORT);
    System.out.println("Run DisplayRobot.java and observe the robot visualization.");

    try (ServerSocket server = new ServerSocket(PORT)) {
      while (true) {
        try (Socket socket = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

          String mode = in.readLine();
          if (!"RECEIVE".equals(mode)) continue;

          System.out.println("DisplayRobot connected. Sending simulated robot poses...");
          while (!out.checkError()) {
            out.println(simulatedPose());
            out.flush();
            sleep();
          }
        } catch (IOException e) {
          System.out.println("DisplayRobot disconnected. Waiting for another connection...");
        }
      }
    } catch (IOException e) {
      System.err.println("Test server stopped: " + e.getMessage());
    }
  }

  private static String simulatedPose() {
    double[] v = new double[9];
    for (int i = 0; i < 6; i++) {
      v[i] = -Math.PI + RANDOM.nextDouble() * 2 * Math.PI;
    }
    for (int i = 6; i < 9; i++) {
      v[i] = -1.0 + RANDOM.nextDouble() * 2.0;
    }
    return String.format(Locale.US,
        "ROBOT,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f",
        v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8]);
  }

  private static void sleep() throws IOException {
    try {
      Thread.sleep(DELAY_MS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IOException("Robot pose simulation interrupted", e);
    }
  }
}
