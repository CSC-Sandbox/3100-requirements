package edu.calpoly.provided;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RetrieveMessages {
    private static final int PORT = 5000;
    private static final Path DATA_FILE = Path.of("data", "messages.csv");

    private static List<String> messages = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        RetrieveMessages retriever = new RetrieveMessages();
        retriever.readFile(DATA_FILE);

        if (messages.isEmpty()) {
            System.err.println("No messages to retrieve.");
            return;
        }

        Broker broker = new Broker("localhost", PORT);

        for (String message : messages) {
            broker.send(message);
        }
    }

    private void readFile(Path filePath) {
        if (!Files.exists(DATA_FILE)) {
            System.err.println("Missing Storage File: " + DATA_FILE.toAbsolutePath());
            return;
        }

        try {
            messages = Files.readAllLines(filePath, StandardCharsets.UTF_8)
                    .stream()
                    .filter(line -> !line.isBlank())
                    .toList();
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
