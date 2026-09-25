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
 * Course-provided test server that simulates combined data streams for monitoring applications.
 *
 * @author Javier Gonzalez-Sanchez (javiergs)
 * @version 1.0 (2026-09-01)
 */
public class TestMonitorData {
  private static final int PORT = 5000;
  private static final long TICK_MS = 50;
  private static final long ROBOT_PERIOD_MS = 250;
  private static final long GAZE_PERIOD_MS = 100;
  private static final long AFFECT_PERIOD_MS = 500;
  private static final long LIDAR_PERIOD_MS = 200;

  private static final long NORMAL_DURATION_MS = 6_000;
  private static final long OUTAGE_DURATION_MS = 4_000;

  private static final Random RANDOM = new Random();

  private enum Source {ROBOT, GAZE, AFFECT, LIDAR}

  public static void main(String[] args) {
    System.out.println("TestMonitorData running on localhost:" + PORT);
    System.out.println("Run a Module 6 monitoring application and observe source status changes.");
    System.out.println("Messages use the common TYPE,data... contract.");

    try (ServerSocket server = new ServerSocket(PORT)) {
      while (true) {
        try (Socket socket = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

          String mode = in.readLine();
          if (!"RECEIVE".equals(mode)) continue;

          System.out.println("Monitoring client connected. Sending combined data streams...");
          sendCombinedStreams(out);
        } catch (IOException e) {
          System.out.println("Monitoring client disconnected. Waiting for another connection...");
        }
      }
    } catch (IOException e) {
      System.err.println("Test server stopped: " + e.getMessage());
    }
  }

  private static void sendCombinedStreams(PrintWriter out) throws IOException {
    long start = System.currentTimeMillis();
    long nextRobot = start;
    long nextGaze = start;
    long nextAffect = start;
    long nextLidar = start;
    int affectSample = 0;
    int outageIndex = 0;
    Source lastOutage = null;

    while (!out.checkError()) {
      long now = System.currentTimeMillis();
      long elapsed = now - start;
      long cycleLength = NORMAL_DURATION_MS + OUTAGE_DURATION_MS;
      long cyclePosition = elapsed % cycleLength;

      Source outage = null;
      if (cyclePosition >= NORMAL_DURATION_MS) {
        outage = Source.values()[outageIndex % Source.values().length];
      }

      if (outage != lastOutage) {
        if (lastOutage != null) {
          System.out.println(lastOutage + " resumed.");
          outageIndex++;
        }
        if (outage != null) {
          System.out.println("Simulating outage: " + outage + " will stop for "
              + (OUTAGE_DURATION_MS / 1000) + " seconds.");
        }
        lastOutage = outage;
      }

      if (now >= nextRobot) {
        if (outage != Source.ROBOT) out.println(robotMessage());
        nextRobot = now + ROBOT_PERIOD_MS;
      }
      if (now >= nextGaze) {
        if (outage != Source.GAZE) out.println(gazeMessage(now));
        nextGaze = now + GAZE_PERIOD_MS;
      }
      if (now >= nextAffect) {
        if (outage != Source.AFFECT) out.println(affectMessage(affectSample++));
        nextAffect = now + AFFECT_PERIOD_MS;
      }
      if (now >= nextLidar) {
        if (outage != Source.LIDAR) out.println(lidarMessage(now));
        nextLidar = now + LIDAR_PERIOD_MS;
      }

      out.flush();
      sleep();
    }
  }

  private static String robotMessage() {
    double[] values = new double[9];
    for (int i = 0; i < 6; i++) {
      values[i] = -Math.PI + RANDOM.nextDouble() * 2 * Math.PI;
    }
    for (int i = 6; i < 9; i++) {
      values[i] = -1.0 + RANDOM.nextDouble() * 2.0;
    }
    return String.format(Locale.US,
        "ROBOT,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,%.3f",
        values[0], values[1], values[2], values[3], values[4],
        values[5], values[6], values[7], values[8]);
  }

  private static String gazeMessage(long now) {
    double t = (now % 10_000) / 10_000.0;
    double x = 0.5 + 0.45 * Math.sin(t * Math.PI * 2);
    double y = 0.5 + 0.40 * Math.cos(t * Math.PI * 2);
    return String.format(Locale.US, "GAZE,%.2f,%.2f", x, y);
  }

  private static String affectMessage(int sample) {
    double t = sample * 0.20;
    double focus = bounded(0.55 + 0.30 * Math.sin(t));
    double excitement = bounded(0.45 + 0.25 * Math.sin(t + 1.0));
    double engagement = bounded(0.65 + 0.20 * Math.sin(t + 2.0));
    double interest = bounded(0.55 + 0.25 * Math.sin(t + 3.0));
    double stress = bounded(0.35 + 0.20 * Math.sin(t + 4.0));
    return String.format(Locale.US, "AFFECT,%.2f,%.2f,%.2f,%.2f,%.2f",
        focus, excitement, engagement, interest, stress);
  }

  private static String lidarMessage(long now) {
    double angle = (now % 8_000) / 8_000.0 * Math.PI * 2;
    double radius = 2.5 + 0.5 * Math.sin(angle * 3);
    double x = radius * Math.cos(angle);
    double y = radius * Math.sin(angle);
    double z = 0.10 + 0.05 * Math.sin(angle * 2);
    return String.format(Locale.US, "LIDAR,%.2f,%.2f,%.2f", x, y, z);
  }

  private static double bounded(double value) {
    return Math.max(0.0, Math.min(1.0, value));
  }

  private static void sleep() throws IOException {
    try {
      Thread.sleep(TICK_MS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IOException("Monitoring simulation interrupted", e);
    }
  }
}
