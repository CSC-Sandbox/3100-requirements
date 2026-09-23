# 06-Enter-Message

`EnterMessage` is a Swing-based Java desktop application that provides a graphical user interface for typing and transmitting messages across a network socket connection.

## Overview

The application creates a client interface with a text area and a **Send** button. Entered messages are trimmed and sent via a network `Broker` instance connected to `localhost:5000`.

## Key Functionality

- **GUI Interface:** Built using standard Java Swing (`JFrame`, `JTextArea`, `JButton`, `JScrollPane`).
- **Network Transmission:** Sends non-empty text strings using `broker.send(message)`.
- **Input Reset:** Automatically clears the input text box immediately after a message is transmitted.

## Quick Start

```java
import edu.calpoly.provided.EnterMessage;

public class Main {
    public static void main(String[] args) {
        // Launches the EnterMessage GUI on the Event Dispatch Thread
        new EnterMessage();
    }
}
