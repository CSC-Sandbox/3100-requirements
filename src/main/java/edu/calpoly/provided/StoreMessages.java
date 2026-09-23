package edu.calpoly.provided;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;

public class StoreMessages {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5000;
    private static final Path DEFAULT_DATA_FILE = Path.of("data", "messages.csv");

    private final Broker broker;
    private final Path dataFile;

    public StoreMessages(Path dataFile, String host, int port) {
        this.dataFile = dataFile;
        this.broker = new Broker(host, port);
    }

    public static void main(String[] args) {
        Path dataFile = args.length > 0 ? Path.of(args[0]) : DEFAULT_DATA_FILE;
        StoreMessages store = new StoreMessages(dataFile, DEFAULT_HOST, DEFAULT_PORT);

        try {
            store.receiveAndStore();
        } catch (IOException e) {
            System.err.println("Unable to store messages: " + e.getMessage());
        }
    }

    /* Receives messages until the sender closes the connection. */
    public void receiveAndStore() throws IOException {
        Path parent = dataFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                dataFile,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            while (true) {
                final String message;
                try {
                    message = broker.receive();
                } catch (IllegalStateException e) {
                    /*  Broker.receive() reports the sender closing its connection as an IllegalStateException.
                    All records are already flushed. */
                    return;
                }

                writer.write(Instant.now().toString());
                writer.write(',');
                writer.write(message);
                writer.newLine();
                writer.flush();
            }
        }
    }
}
