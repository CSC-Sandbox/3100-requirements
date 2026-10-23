package edu.calpoly.messages;

import org.apache.commons.lang3.StringUtils;

/**
 * Represents immutable message text with optional sender and subject metadata.
 * Preserves the original text validation and normalization utilities.
 *
 * @author Matthew Davi
 * @version 2.0
 */
public final class MessageInput {
    private final String text;
    private final String sender;
    private final String subject;

    /**
     * Creates a message from fields validated by MessageInputBuilder.
     *
     * @param text validated message text
     * @param sender optional sender, or null
     * @param subject optional subject, or null
     */
    MessageInput(String text, String sender, String subject) {
        this.text = text;
        this.sender = sender;
        this.subject = subject;
    }

    /**
     * Checks whether text contains a non-whitespace character.
     *
     * @param message text to check, which may be null
     * @return true if the text is not null or blank
     */
    public static boolean isValid(String message) {
        return !StringUtils.isBlank(message);
    }

    /**
     * Removes leading and trailing characters with values of 32 or less.
     *
     * @param message text to normalize, which may be null
     * @return trimmed text, or null if the input is null
     */
    public static String normalize(String message) {
        return StringUtils.trim(message);
    }

    /**
     * Returns the validated message text.
     *
     * @return message text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the optional sender.
     *
     * @return sender, or null when omitted
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the optional subject.
     *
     * @return subject, or null when omitted
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Formats the message for submission through Broker.
     *
     * @return text with any sender and subject metadata
     */
    public String toMessageLine() {
        if (sender == null) {
            return text;
        }
        if (subject == null) {
            return sender + ": " + text;
        }
        return sender + " [" + subject + "]: " + text;
    }
}