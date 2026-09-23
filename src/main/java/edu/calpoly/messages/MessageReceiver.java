package edu.calpoly.messages;

import edu.calpoly.provided.Broker;
import java.util.function.Consumer;

public class MessageReceiver implements Runnable {
    private final Broker broker;
    private final Consumer<String> messageHandler;

    public MessageReceiver(Broker broker, Consumer<String> messageHandler) {
        this.broker = broker;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            String message = broker.receive();
            messageHandler.accept(message);
        }
    }
}