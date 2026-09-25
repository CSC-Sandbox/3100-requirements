package edu.calpoly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Course-provided tester for Issue #8 — Store Messages.
 *
 * @author Javier Gonzalez-Sanchez (javiergs)
 * @version 1.0 (2026-09-01)
 */
public class TestStoreMessages {

  private static final int PORT = 5000;
  private static final Path INPUT_FILE = Path.of("data", "store-input.txt");

  public static void main(String[] args) throws Exception {
    if (!Files.exists(INPUT_FILE)) {
      System.err.println("Missing test data: " + INPUT_FILE.toAbsolutePath());
      return;
    }

    List<String> messages = Files.readAllLines(INPUT_FILE, StandardCharsets.UTF_8)
        .stream()
        .filter(line -> !line.isBlank())
        .toList();

    System.out.println("TestStoreMessages");
    System.out.println("Input: " + INPUT_FILE);
    System.out.println("Messages to send: " + messages.size());
    System.out.println("Waiting for StoreMessages on localhost:" + PORT + " ...");

    try (ServerSocket server = new ServerSocket(PORT);
         Socket socket = server.accept();
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

      String request = in.readLine();
      if (!"RECEIVE".equals(request)) {
        System.err.println("Expected RECEIVE handshake but got: " + request);
        return;
      }

      System.out.println("StoreMessages connected. Sending test data...");
      for (String message : messages) {
        out.println(message);
        System.out.println("  SENT: " + message);
        Thread.sleep(200);
      }
    }

    System.out.println("Finished sending messages.");

    if (args.length > 0) {
      Path storedFile = Path.of(args[0]);
      Thread.sleep(500);
      verifyStoredFile(storedFile, messages);
    } else {
      System.out.println();
      System.out.println("Optional verification:");
      System.out.println("Run this tester with the path to the student's storage file:");
      System.out.println("  TestStoreMessages <stored-file>");
      System.out.println("The verifier checks that all input messages appear in order.");
    }
  }

  private static void verifyStoredFile(Path storedFile, List<String> expectedMessages) throws IOException {
    System.out.println();
    System.out.println("Verifying stored file: " + storedFile);

    if (!Files.exists(storedFile)) {
      System.err.println("FAIL: Stored file was not found.");
      return;
    }

    String content = Files.readString(storedFile, StandardCharsets.UTF_8);
    int position = 0;

    for (String expected : expectedMessages) {
      int found = content.indexOf(expected, position);
      if (found < 0) {
        System.err.println("FAIL: Missing or out-of-order message: " + expected);
        return;
      }
      position = found + expected.length();
    }

    System.out.println("PASS: All input messages were found in the stored file in the expected order.");
    System.out.println("Note: Verify separately that each stored message has a timestamp.");
  }

}
