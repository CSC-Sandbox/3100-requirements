package edu.calpoly.provided;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Course-provided communication abstraction.
 *
 * <p>For the first programming assignment, Broker sends text messages
 * through a TCP socket. Student implementations should use this class
 * rather than implement the socket communication directly.</p>
 */
public class Broker {

    private final String host;
    private final int port;

    /**
     * Creates a Broker configured to send data to the given host and port.
     *
     * @param host destination host
     * @param port destination port
     */
    public Broker(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Sends one text message to the configured destination.
     *
     * @param message message to send
     */
    public void send(String message) {
        try (
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println(message);
        } catch (IOException e) {
            throw new IllegalStateException(
                "Unable to send message to " + host + ":" + port, e
            );
        }
    }
}
