package edu.calpoly.messages;

import org.apache.commons.lang3.StringUtils;

public final class MessageInput {

    private MessageInput() {
    }

    public static boolean isValid(String message) {
        return !StringUtils.isBlank(message);
    }

    public static String normalize(String message) {
        return StringUtils.trim(message);
    }
}