# Module 7: Storage and Message Retrieval

## Overview

`RetrieveMessages` reads stored message records from a csv storage file and sends them to any given destination with a `Broker`.

## Implementation

The class is located at:

```text
src/main/java/edu/calpoly/provided/RetrieveMessages.java
```

`RetrieveMessages` is configured with:

- A `Path` identifying the storage file.
- a host (`string`) to send messages to.
- A port (`int`) to send messages to.

The default configuration when main is run is:

```text
Storage file: data/messages.csv
Host: localhost
Port: 5000
```

The constructor makes the storage path, destination host, and port configurable for other callers and tests.

## Retrieval Behavior

When the program starts, it:

1. Reads `data/messages.csv` using UTF-8.
2. Filters out blank lines.
3. Reports an error if the storage file is missing or cannot be read.
4. Reports `No messages to retrieve.` when no records are available.
5. Sends every remaining record through `Broker.send()` in file order.

Each record is sent as a single line and in the same order that they were stored in. The stored CSV content is not parsed or modified, so commas and the original record format are preserved.

## Running the Program

Compile the project with Maven:

```bash
mvn compile
```

Start the provided retrieval tester first. From the project root, run:

```bash
java -cp target/classes edu.calpoly.test.sprint1.TestRetrieveMessages
```

In a second terminal, run the retrieval program:

```bash
java -cp target/classes edu.calpoly.RetrieveMessages
```

The tester listens on `localhost:5000`, receives the records, and compares them with `data/messages.csv`.

## Expected Result

For the supplied `data/messages.csv`, the tester should receive five records in this order:

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
