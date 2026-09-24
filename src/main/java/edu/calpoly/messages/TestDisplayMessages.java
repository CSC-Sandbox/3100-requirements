package edu.calpoly.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;

/**
 * Course-provided receiver used to test DisplayMessages.java. It simulates a message source by sending messages at random intervals to the DisplayMessages GUI.
 *
 * @author Omar Coleman
 */
public class TestDisplayMessages {

  private static final int PORT = 5000;
  private static final int MIN_DELAY_MS = 1000;
  private static final int MAX_DELAY_MS = 3000;
  private static final Random RANDOM = new Random();

  private static final List<String> MESSAGES = List.of(
      "Hello!",
      "How are you?",
      "CSC 3100",
      "Software Engineering",
      "Testing the messages...",
      "Requirements become software.",
      "One message at a time.",
      "Message received!"
  );

  public static void main(String[] args) {
    System.out.println("TestDisplayMessages running on localhost:" + PORT);
    System.out.println("Run DisplayMessages.java and observe the message list update.");

    try (ServerSocket server = new ServerSocket(PORT)) {
      while (true) {
        try (Socket socket = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

          String mode = in.readLine();
          if (!"RECEIVE".equals(mode)) {
            continue;
          }

          System.out.println("DisplayMessages connected. Sending simulated messages...");

          int index = 0;
          while (!out.checkError()) {
            String message = MESSAGES.get(index % MESSAGES.size());
            out.println(message);
            out.flush();
            System.out.println("Sent: " + message);
            index++;
            sleepRandomly();
          }
        } catch (IOException e) {
          System.out.println("DisplayMessages disconnected. Waiting for another connection...");
        }
      }
    } catch (IOException e) {
      System.err.println("Test server stopped: " + e.getMessage());
    }
  }

  private static void sleepRandomly() throws IOException {
    int delay = MIN_DELAY_MS + RANDOM.nextInt(MAX_DELAY_MS - MIN_DELAY_MS + 1);
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IOException("Message simulation interrupted", e);
    }
  }
}
