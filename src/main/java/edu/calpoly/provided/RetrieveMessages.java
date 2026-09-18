package edu.calpoly.provided;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RetrieveMessages {
    private static final int DEFAULT_PORT = 5000;
    private static final String DEFAULT_HOST = "localhost";
    private static final Path DEFAULT_DATA_FILE = Path.of("data", "messages.csv");

    private Path dataFile;
    private String host = "localhost";
    private int port;
    private List<String> messages = new ArrayList<>();

    public RetrieveMessages(Path dataFile, String host, int port) {
        this.dataFile = dataFile;
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) {        
        RetrieveMessages retriever = new RetrieveMessages(DEFAULT_DATA_FILE, DEFAULT_HOST, DEFAULT_PORT);
        retriever.readFile();

        if (retriever.messages.isEmpty()) {
            System.err.println("No messages to retrieve.");
            return;
        }

        retriever.sendMessages();
    }

    private void readFile() {
        if (!Files.exists(dataFile)) {
            System.err.println("Missing Storage File: " + dataFile.toAbsolutePath());
            return;
        }

        try {
            messages = Files.readAllLines(dataFile, StandardCharsets.UTF_8)
                    .stream()
                    .filter(line -> !line.isBlank())
                    .toList();
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void sendMessages() {
        Broker broker = new Broker(host, port);

        for (String message : messages) {
            broker.send(message);
        }
    }
}
