package edu.calpoly.messages;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Builds and validates user input before forwarding it to a destination.
 *
 * @author Matthew Davi
 * @version 1.0
 */
public final class MessageSubmission {
    private final Consumer<String> destination;

    /**
     * Creates a submission service.
     *
     * @param destination receives formatted messages, such as Broker.send
     * @throws NullPointerException if destination is null
     */
    public MessageSubmission(Consumer<String> destination) {
        this.destination = Objects.requireNonNull(
                destination, "Destination must not be null.");
    }

    /**
     * Validates and sends a message from input controls.
     * Blank optional controls are treated as omitted.
     *
     * @param text required message text
     * @param sender optional sender input
     * @param subject optional subject input
     * @throws IllegalArgumentException if message construction fails
     */
    public void submit(String text, String sender, String subject) {
        MessageInput message = new MessageInputBuilder()
                .text(text)
                .sender(optionalInput(sender))
                .subject(optionalInput(subject))
                .build();

        destination.accept(message.toMessageLine());
    }

    /**
     * Converts blank optional controls to omitted values while preserving
     * line breaks so the Builder can reject them.
     *
     * @param value optional control value
     * @return null for blank input, or the original value for validation
     */
    private String optionalInput(String value) {
        if (value != null
                && (value.contains("\r") || value.contains("\n"))) {
            return value;
        }
        return MessageInput.isValid(value) ? value : null;
    }
}