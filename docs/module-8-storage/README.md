# Module 8: Storage

## Overview

`StoreMessages` reads a datastream of message records from a `Broker` and sends them to any given destination with a CSV storage file.

## Implementation

The class is located at:

```text
src/main/java/edu/calpoly/provided/StoreMessages.java
```

`StoreMessages` is configured with:

- A `Path` identifying the storage file.
- a host (`string`) to send messages to.
- A port (`int`) to send messages to.

The default configuration when main is run is:

```text
Storage file: data/messages.csv
Host: localhost
Port: 5000
```

By default, the class will be configured to run on localhost 5000 to match the broker. Can be constructed to run with any host.

## Storage Behavior

When the program starts, it:

1. Opens data/messages.csv using UTF-8, creating the file and parent directory if necessary.
2. Receives messages through Broker.receive().
3. Associates an ISO-8601 timestamp with each received message.
4. Appends each timestamp and unchanged message to the file in receive order.
5. Flushes each record immediately and stops when the sender closes the connection.

Each record is sent as a single line and in the same order that they were stored in. The stored CSV content is not parsed or modified, so commas and the original record format are preserved.

## Running the Program

Compile the project with Maven:

```bash
mvn compile
```

Start the provided storage tester first. From the project root, run:

```bash
java -cp target/classes edu.calpoly.test.sprint1.TestStoreMessages
```

In a second terminal, run the storage program:

```bash
java -cp target/classes edu.calpoly.StoreMessages
```

The tester listens on `localhost:5000`, receives the records, and compares them with `data/messages.csv`.

## Expected Result

For the supplied `data/messages.csv`, the tester should write five records in this order:

```text
2026-09-14T10:00:01,Hello from CSC 3100
2026-09-14T10:00:05,Robot data received
2026-09-14T10:00:10,Gaze sample accepted
2026-09-14T10:00:15,System is operating normally
2026-09-14T10:00:20,End of sample messages
```

And a successful run reports:

```text
PASS: Retrieved records match the provided storage file exactly and in order.
```
