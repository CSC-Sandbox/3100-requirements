package edu.calpoly.test.sprint3;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import edu.calpoly.messages.StoreMessages;
import edu.calpoly.messages.StoreMessagesBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Verifies validated builder configuration and the existing CSV storage
 * behavior
 * using temporary files and a controlled broker on an ephemeral TCP port.
 *
 * @author Landon McCaslin
 * @version 1.0
 */
class CsvStorageBuilderTest {
  @TempDir
  Path temporaryDirectory;

  @Test
  void buildsWithDefaults() {
    assertNotNull(assertDoesNotThrow(() -> new StoreMessagesBuilder().build()));
  }

  @Test
  void settersAreFluentAndValidationWaitsUntilBuild() {
    StoreMessagesBuilder builder = new StoreMessagesBuilder();

    assertSame(builder, builder.dataFile(null));
    assertSame(builder, builder.host(" "));
    assertSame(builder, builder.port(0));
    assertThrows(IllegalArgumentException.class, builder::build);

    assertNotNull(builder.dataFile(temporaryDirectory.resolve("messages.csv"))
        .host("localhost").port(5000).build());
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = { " ", "\t" })
  void rejectsMissingOrBlankFilePaths(String path) {
    StoreMessagesBuilder builder = new StoreMessagesBuilder()
        .dataFile(path == null ? null : Path.of(path));

    assertThrows(IllegalArgumentException.class, builder::build);
  }

  @Test
  void rejectsAnExistingDirectoryAsTheOutputFile() {
    StoreMessagesBuilder builder = new StoreMessagesBuilder()
        .dataFile(temporaryDirectory);

    assertThrows(IllegalArgumentException.class, builder::build);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = { " ", " broker", "broker ", "bad host", "broker\tname", "broker\nname" })
  void rejectsMissingBlankOrWhitespaceHosts(String host) {
    StoreMessagesBuilder builder = new StoreMessagesBuilder().host(host);

    assertThrows(IllegalArgumentException.class, builder::build);
  }

  @ParameterizedTest
  @ValueSource(ints = { Integer.MIN_VALUE, -1, 0, 65536, Integer.MAX_VALUE })
  void rejectsPortsOutsideTheTcpRange(int port) {
    StoreMessagesBuilder builder = new StoreMessagesBuilder().port(port);

    assertThrows(IllegalArgumentException.class, builder::build);
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 65535 })
  void acceptsBothValidPortBoundaries(int port) {
    assertNotNull(new StoreMessagesBuilder().port(port).build());
  }

  @Test
  void buildingDoesNotConnectOrCreateTheOutputDirectory() throws Exception {
    Path file = temporaryDirectory.resolve("not-created/messages.csv");

    try (ServerSocket server = new ServerSocket(0)) {
      server.setSoTimeout(150);
      assertNotNull(new StoreMessagesBuilder().dataFile(file)
          .host("127.0.0.1").port(server.getLocalPort()).build());

      assertFalse(Files.exists(file.getParent()));
      assertThrows(SocketTimeoutException.class, () -> {
        try (Socket unexpectedConnection = server.accept()) {
          fail("Building a store must not connect to its broker");
        }
      });
    }
  }

  @Test
  void builderPreservesConstructorStorageBehaviorAndAppendOrder() throws Exception {
    Path originalFile = temporaryDirectory.resolve("original/nested/messages.csv");
    Path builderFile = temporaryDirectory.resolve("builder/nested/messages.log");
    List<String> firstBatch = List.of("Hello", "Robot position: 1,2,3", "Status \"ready\"", "");
    List<String> secondBatch = List.of("A later message");
    IntFunction<StoreMessages> original = port -> new StoreMessages(originalFile, "localhost", port);
    // Override only the path and port to exercise the builder's default host.
    IntFunction<StoreMessages> built = port -> new StoreMessagesBuilder()
        .dataFile(builderFile).port(port).build();

    receiveFromBroker(original, firstBatch);
    receiveFromBroker(built, firstBatch);
    assertEquals(firstBatch, readMessagesAndValidateTimestamps(originalFile));
    assertEquals(firstBatch, readMessagesAndValidateTimestamps(builderFile));

    receiveFromBroker(original, secondBatch);
    receiveFromBroker(built, secondBatch);
    List<String> expected = new ArrayList<>(firstBatch);
    expected.addAll(secondBatch);
    assertEquals(expected, readMessagesAndValidateTimestamps(originalFile));
    assertEquals(expected, readMessagesAndValidateTimestamps(builderFile));
  }

  @Test
  void reusingTheBuilderDoesNotChangePreviouslyBuiltStores() throws Exception {
    Path firstFile = temporaryDirectory.resolve("first.csv");
    Path secondFile = temporaryDirectory.resolve("second.csv");

    try (ServerSocket firstBroker = new ServerSocket(0);
        ServerSocket secondBroker = new ServerSocket(0)) {
      StoreMessagesBuilder builder = new StoreMessagesBuilder().dataFile(firstFile)
          .port(firstBroker.getLocalPort());
      StoreMessages firstStore = builder.build();
      StoreMessages secondStore = builder.dataFile(secondFile).host("127.0.0.1")
          .port(secondBroker.getLocalPort()).build();

      assertNotSame(firstStore, secondStore);
      exchangeMessages(firstBroker, firstStore, List.of("First store"));
      exchangeMessages(secondBroker, secondStore, List.of("Second store"));
      assertEquals(List.of("First store"), readMessagesAndValidateTimestamps(firstFile));
      assertEquals(List.of("Second store"), readMessagesAndValidateTimestamps(secondFile));
    }
  }

  @Test
  void existingWriterPreservesMultilineUnicodeAndAppendsRecords() throws Exception {
    Path file = temporaryDirectory.resolve("writer/nested/messages.csv");
    String timestamp = "2026-10-22T08:00:00Z";
    List<String> messages = List.of("First, line\nSecond \"line\" — café", "Next record");

    for (String message : messages) {
      StoreMessages.writeRecord(file, timestamp, message);
    }

    assertEquals(messages, readMessagesAndValidateTimestamps(file));
    try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
        CSVParser parser = CSVFormat.DEFAULT.parse(reader)) {
      for (CSVRecord record : parser) {
        assertEquals(timestamp, record.get(0));
      }
    }
  }

  private void receiveFromBroker(IntFunction<StoreMessages> factory, List<String> messages)
      throws Exception {
    try (ServerSocket server = new ServerSocket(0)) {
      exchangeMessages(server, factory.apply(server.getLocalPort()), messages);
    }
  }

  private void exchangeMessages(ServerSocket server, StoreMessages store, List<String> messages)
      throws Exception {
    server.setSoTimeout(5000);
    ExecutorService receiverExecutor = Executors.newSingleThreadExecutor(task -> {
      Thread receiverThread = new Thread(task, "csv-storage-test-receiver");
      receiverThread.setDaemon(true);
      return receiverThread;
    });
    Future<?> receiver = receiverExecutor.submit(() -> {
      store.receiveAndStore();
      return null;
    });

    try {
      try (Socket peer = server.accept();
          BufferedReader input = new BufferedReader(new InputStreamReader(
              peer.getInputStream(), StandardCharsets.UTF_8));
          PrintWriter output = new PrintWriter(peer.getOutputStream(), true,
              StandardCharsets.UTF_8)) {
        peer.setSoTimeout(5000);
        assertEquals("RECEIVE", input.readLine());
        for (String message : messages) {
          output.println(message);
        }
        assertFalse(output.checkError());
      }
      // Closing the peer signals EOF, allowing the store to close its receiver.
      receiver.get(5, TimeUnit.SECONDS);
    } finally {
      receiverExecutor.shutdownNow();
      assertTrue(receiverExecutor.awaitTermination(5, TimeUnit.SECONDS),
          "The storage receiver must finish after the broker closes");
    }
  }

  private List<String> readMessagesAndValidateTimestamps(Path file) throws Exception {
    List<String> messages = new ArrayList<>();
    try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
        CSVParser parser = CSVFormat.DEFAULT.parse(reader)) {
      for (CSVRecord record : parser) {
        assertEquals(2, record.size());
        assertDoesNotThrow(() -> Instant.parse(record.get(0)));
        messages.add(record.get(1));
      }
    }
    return messages;
  }
}
