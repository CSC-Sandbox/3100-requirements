package edu.calpoly.messages;

import java.nio.file.Files;
import java.nio.file.Path;

/*
 * Builds a StoreMessages class object. Building does not connect to the broker or create files.
 * Host availability and filesystem permissions are still checked when the store is used. A builder
 * may be reused; changing it does not change stores already built.
 *
 * @author Landon McCaslin
 * @version 1.0
 */
public final class StoreMessagesBuilder {
  private Path dataFile = Path.of("data", "messages.csv");
  private String host = "localhost";
  private int port = 5000;

  public StoreMessagesBuilder dataFile(Path dataFile) {
    this.dataFile = dataFile;
    return this;
  }

  public StoreMessagesBuilder host(String host) {
    this.host = host;
    return this;
  }

  public StoreMessagesBuilder port(int port) {
    this.port = port;
    return this;
  }

  public StoreMessages build() {
    if (dataFile == null || dataFile.toString().isBlank()) {
      throw new IllegalArgumentException("dataFile must specify a file path");
    }
    if (Files.isDirectory(dataFile)) {
      throw new IllegalArgumentException("dataFile must not be a directory");
    }
    if (host == null || host.isBlank()
        || host.chars().anyMatch(Character::isWhitespace)) {
      throw new IllegalArgumentException("host must be nonblank and contain no whitespace");
    }
    if (port < 1 || port > 65535) {
      throw new IllegalArgumentException("port must be between 1 and 65535");
    }
    return new StoreMessages(dataFile, host, port);
  }
}
