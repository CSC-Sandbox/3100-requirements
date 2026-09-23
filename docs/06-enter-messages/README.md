# EnterMessage

`EnterMessage` is a Swing-based Java desktop application that provides a graphical user interface for entering and sending text messages across a network socket connection.

## Overview

The application presents a lightweight text editor window. Messages typed into the text area are trimmed and transmitted via a network `Broker` instance connected to `localhost:5000` when the user clicks the **Send** button.

## Features

* **Graphical User Interface:** Built using standard Java Swing (`JFrame`, `JTextArea`, `JButton`, `JScrollPane`).
* **Network Integration:** Communicates with a local server or broker instance running on port `5000`.
* **Input Handling:** Ignores empty messages and automatically clears the text box after transmission.

## Setup & Usage

### Prerequisites
* Java Development Kit (JDK) 8 or higher.
* A running instance of `Broker` on `localhost:5000` listening for incoming socket transmissions.

### Execution

1. Compile the source files:
   ```bash
   javac edu/calpoly/provided/EnterMessage.java
