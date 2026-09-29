package edu.calpoly.messages;

import edu.calpoly.provided.Broker;
import java.util.function.Consumer;

/**
 * Receives messages from the broker on a background thread and forwards them
 * to a supplied message handler.
 *
 * @author Matthew Davi
 * @version September 25, 2026
 */
public class MessageReceiver implements Runnable {
    private final Broker broker;
    private final Consumer<String> messageHandler;

    /**
     * Creates a receiver for a broker and a message handler.
     *
     * @param broker source of incoming messages
     * @param messageHandler handler called for each received message
     */
    public MessageReceiver(Broker broker, Consumer<String> messageHandler) {
        this.broker = broker;
        this.messageHandler = messageHandler;
    }

    /** Receives and forwards messages until the thread is interrupted. */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            String message = broker.receive();
            messageHandler.accept(message);
        }
    }
}
