package edu.calpoly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

/**
 * Course-provided affective-data simulator used to test DisplayAffect.java.
 * This class simulates the behavior of an affective-data source, sending periodic updates to a client.
 *
 * @author Javier Gonzalez-Sanchez (javiergs)
 * @version 1.0 (2026-09-01)
 */
public class TestDisplayAffect {
  private static final int PORT = 5000;
  private static final long DELAY_MS = 200;

  public static void main(String[] args) {
    System.out.println("TestDisplayAffect running on localhost:" + PORT);
    System.out.println("Run DisplayAffect.java and observe the affective-state chart.");

    try (ServerSocket server = new ServerSocket(PORT)) {
      while (true) {
        try (Socket socket = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

          String mode = in.readLine();
          if (!"RECEIVE".equals(mode)) continue;

          System.out.println("DisplayAffect connected. Sending simulated affective data...");
          sendAffect(out);
        } catch (IOException e) {
          System.out.println("DisplayAffect disconnected. Waiting for another connection...");
        }
      }
    } catch (IOException e) {
      System.err.println("Test server stopped: " + e.getMessage());
    }
  }

  private static void sendAffect(PrintWriter out) throws IOException {
    int sample = 0;
    while (!out.checkError()) {
      double t = sample * 0.10;

      double focus = wave(t, 0.00, 0.55, 0.35);
      double excitement = wave(t, 1.20, 0.45, 0.30);
      double engagement = wave(t * 0.70, 2.10, 0.65, 0.25);
      double interest = wave(t * 0.85, 3.00, 0.55, 0.30);
      double stress = wave(t * 0.55, 4.00, 0.35, 0.25);

      out.printf(Locale.US, "AFFECT,%.2f,%.2f,%.2f,%.2f,%.2f%n",
          focus, excitement, engagement, interest, stress);
      out.flush();

      sample++;
      sleep();
    }
  }

  private static double wave(double t, double phase, double center, double amplitude) {
    double value = center + amplitude * Math.sin(t + phase);
    return Math.max(0.0, Math.min(1.0, value));
  }

  private static void sleep() throws IOException {
    try {
      Thread.sleep(DELAY_MS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IOException("Affective-data simulation interrupted", e);
    }
  }
}
