package edu.calpoly.messages;

/**
 * Builds messages with validated text and optional metadata.
 *
 * @author Matthew Davi
 * @version 1.0
 */
public final class MessageInputBuilder {
    private String text;
    private String sender;
    private String subject;

    /**
     * Sets the required message text.
     *
     * @param text message text
     * @return this builder
     */
    public MessageInputBuilder text(String text) {
        this.text = text;
        return this;
    }

    /**
     * Sets the optional sender.
     *
     * @param sender sender name, or null to omit
     * @return this builder
     */
    public MessageInputBuilder sender(String sender) {
        this.sender = sender;
        return this;
    }

    /**
     * Sets the optional subject.
     *
     * @param subject subject text, or null to omit
     * @return this builder
     */
    public MessageInputBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Validates all fields and constructs an immutable message.
     *
     * @return the validated message
     * @throws IllegalArgumentException if text is blank, supplied metadata
     *         is blank, a field contains a line break, or subject lacks sender
     */
    public MessageInput build() {
        String validatedText = validateField(text, "Text", true);
        String validatedSender = validateField(sender, "Sender", false);
        String validatedSubject = validateField(subject, "Subject", false);

        if (validatedSubject != null && validatedSender == null) {
            throw new IllegalArgumentException(
                    "A subject requires a sender.");
        }

        return new MessageInput(
                validatedText, validatedSender, validatedSubject);
    }

    /**
     * Checks a field before trimming it so line breaks cannot be hidden.
     *
     * @param value original field value
     * @param name field name used in error messages
     * @param required whether the field must be supplied
     * @return normalized value, or null for omitted optional fields
     * @throws IllegalArgumentException if the field is invalid
     */
    private String validateField(
            String value, String name, boolean required) {
        if (value == null && !required) {
            return null;
        }

        if (!MessageInput.isValid(value)) {
            throw new IllegalArgumentException(
                    name + " must not be blank.");
        }

        if (value.contains("\r") || value.contains("\n")) {
            throw new IllegalArgumentException(
                    name + " must not contain line breaks.");
        }

        return MessageInput.normalize(value);
    }
}