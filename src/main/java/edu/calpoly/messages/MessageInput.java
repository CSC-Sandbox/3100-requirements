package edu.calpoly.messages;

import org.apache.commons.lang3.StringUtils;

/**
 * Provides utility methods for validating and normalizing message input.
 *
 * @author Matthew Davi
 * @version 1.0
 */
public final class MessageInput {

    private MessageInput() {
    }

    /**
     * Checks whether a message contains at least one non-whitespace character.
     *
     * @param message the message to check, which may be null
     * @return true if the message is not null, empty, or only whitespace;
     *         false otherwise
     */
    public static boolean isValid(String message) {
        return !StringUtils.isBlank(message);
    }

    /**
     * Removes leading and trailing characters with values of 32 or less
     * from a message, including spaces, tabs, and newlines.
     *
     * @param message the message to normalize, which may be null
     * @return the trimmed message, or null if the message is null
     */
    public static String normalize(String message) {
        return StringUtils.trim(message);
    }
}