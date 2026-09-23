# Module 5 Chat — Display Messages

## Overview

The Display Messages application provides a Java Swing interface that displays messages received from the course-provided communication service. New messages appear automatically, previously received messages remain visible, and messages are displayed in their original arrival order.

## Design

The application contains two classes:

- `DisplayMessages` creates and manages the Swing graphical interface. It starts the message receiver and safely adds received messages to the text area on Swing's event dispatch thread.
- `MessageReceiver` runs on a background thread and repeatedly calls `Broker.receive()`. It passes each received message to the display through a message handler.

The background thread prevents the blocking `receive()` operation from freezing the graphical interface.

## Compile

From the repository root, run:

```bash
mkdir -p target/classes
javac -d target/classes \
src/main/java/edu/calpoly/provided/Broker.java \
src/main/java/edu/calpoly/provided/TestDisplayMessages.java \
src/main/java/edu/calpoly/chat/MessageReceiver.java \
src/main/java/edu/calpoly/chat/DisplayMessages.java