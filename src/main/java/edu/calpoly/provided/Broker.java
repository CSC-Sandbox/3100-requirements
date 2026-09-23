package edu.calpoly.provided;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Course-provided communication abstraction for CSC 3100.
 * This class is used to send and receive messages to/from a communication service.
 *
 * @author Javier Gonzalez-Sanchez (javiergs)
 * @version 1.0 (2026-09-01)
 */
public class Broker {
  private final String host;
  private final int port;
  private Socket receiveSocket;
  private BufferedReader receiveReader;

  public Broker(String host, int port) {
    this.host = host;
    this.port = port;
  }

  /**
   * Sends one line to the communication service.
   */
  public void send(String message) {
    try (Socket socket = new Socket(host, port);
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
      out.println(message);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to send message to " + host + ":" + port, e);
    }
  }

  /**
   * Receives the next message from a course-provided display tester.
   */
  public String receive() {
    try {
      if (receiveSocket == null || receiveSocket.isClosed()) {
        receiveSocket = new Socket(host, port);
        PrintWriter out = new PrintWriter(receiveSocket.getOutputStream(), true);
        out.println("RECEIVE");
        receiveReader = new BufferedReader(new InputStreamReader(receiveSocket.getInputStream()));
      }
      String message = receiveReader.readLine();
      if (message == null) throw new IOException("Connection closed by server");
      return message;
    } catch (IOException e) {
      closeReceiver();
      throw new IllegalStateException("Unable to receive message from " + host + ":" + port, e);
    }
  }

  private void closeReceiver() {
    try {
      if (receiveReader != null) receiveReader.close();
    } catch (IOException ignored) {
    }
    try {
      if (receiveSocket != null) receiveSocket.close();
    } catch (IOException ignored) {
    }
    receiveReader = null;
    receiveSocket = null;
  }
}
