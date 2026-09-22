package edu.calpoly.provided;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Course-provided tester for Issue #7 — Retrieve Stored Messages.
 *
 * Start this tester first, then run the student's RetrieveMessages program.
 * The student reads data/messages.csv and sends each complete stored record
 * through Broker.send(). This tester receives those records and compares them
 * with the provided file exactly and in order.
 */
public class TestRetrieveMessages {
    private static final int PORT = 5000;
    private static final Path DATA_FILE = Path.of("data", "messages.csv");
    private static final int ACCEPT_TIMEOUT_MS = 30_000;

    public static void main(String[] args) throws Exception {
        if (!Files.exists(DATA_FILE)) {
            System.err.println("Missing test data: " + DATA_FILE.toAbsolutePath());
            return;
        }

        List<String> expected = Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8)
                .stream()
                .filter(line -> !line.isBlank())
                .toList();

        System.out.println("TestRetrieveMessages");
        System.out.println("Expected records: " + expected.size());
        System.out.println("Waiting on localhost:" + PORT + " ...");

        List<String> actual = new ArrayList<>();

        try (ServerSocket server = new ServerSocket(PORT)) {
            server.setSoTimeout(ACCEPT_TIMEOUT_MS);

            while (actual.size() < expected.size()) {
                try (Socket socket = server.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
                    String message = in.readLine();
                    if (message != null) {
                        actual.add(message);
                        System.out.println("  RECEIVED: " + message);
                    }
                } catch (SocketTimeoutException e) {
                    System.err.println("FAIL: Timed out waiting for retrieved messages.");
                    printSummary(expected, actual);
                    return;
                }
            }
        }

        printSummary(expected, actual);
    }

    private static void printSummary(List<String> expected, List<String> actual) {
        System.out.println();

        if (expected.equals(actual)) {
            System.out.println("PASS: Retrieved records match the provided storage file exactly and in order.");
            return;
        }

        System.err.println("FAIL: Retrieved records do not match the expected data.");
        System.err.println("Expected " + expected.size() + " record(s), received " + actual.size() + ".");

        int count = Math.max(expected.size(), actual.size());
        for (int i = 0; i < count; i++) {
            String e = i < expected.size() ? expected.get(i) : "<none>";
            String a = i < actual.size() ? actual.get(i) : "<none>";
            if (!e.equals(a)) {
                System.err.println("Record " + (i + 1) + ":");
                System.err.println("  expected: " + e);
                System.err.println("  actual:   " + a);
            }
        }
    }
}
