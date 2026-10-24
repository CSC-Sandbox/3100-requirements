package edu.calpoly.messages;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies message construction, compatibility, and submission behavior.
 *
 * @author Matthew Davi
 * @version 1.0
 */
public class MessageInputBehaviorTest {

    /** Checks that the original static utilities preserve their behavior. */
    @Test
    void preservesOriginalUtilities() {
        assertFalse(MessageInput.isValid(null));
        assertFalse(MessageInput.isValid(""));
        assertFalse(MessageInput.isValid(" \t\n "));
        assertTrue(MessageInput.isValid("Hello"));
        assertTrue(MessageInput.isValid(" Hello "));

        assertNull(MessageInput.normalize(null));
        assertEquals("", MessageInput.normalize("   "));
        assertEquals("Hello", MessageInput.normalize(" \tHello\n "));
        assertEquals("Hello", MessageInput.normalize("Hello"));
        assertEquals("Hello\nworld",
                MessageInput.normalize("Hello\nworld"));
    }

    /** Checks text-only construction and omitted metadata. */
    @Test
    void buildsTextOnlyMessage() {
        MessageInput message = new MessageInputBuilder()
                .text("  Hello  ")
                .build();

        assertEquals("Hello", message.getText());
        assertNull(message.getSender());
        assertNull(message.getSubject());
        assertEquals("Hello", message.toMessageLine());
    }

    /** Checks normalization and formatting of optional metadata. */
    @Test
    void buildsMessageWithMetadata() {
        MessageInput message = new MessageInputBuilder()
                .text(" Hello ")
                .sender(" Matthew ")
                .subject(" Study group ")
                .build();

        assertEquals("Hello", message.getText());
        assertEquals("Matthew", message.getSender());
        assertEquals("Study group", message.getSubject());
        assertEquals("Matthew [Study group]: Hello",
                message.toMessageLine());
    }

    /** Checks required-text validation. */
    @Test
    void rejectsMissingOrBlankText() {
        for (String text : new String[]{null, "", "   ", "\t"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> new MessageInputBuilder().text(text).build());
        }
    }

    /** Checks that explicitly supplied blank metadata is invalid. */
    @Test
    void rejectsBlankMetadata() {
        for (String value : new String[]{"", "   ", "\t"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> new MessageInputBuilder()
                            .text("Hello").sender(value).build());

            assertThrows(IllegalArgumentException.class,
                    () -> new MessageInputBuilder()
                            .text("Hello").sender("Matthew")
                            .subject(value).build());
        }
    }

    /** Checks the cross-field rule regardless of setter order. */
    @Test
    void requiresSenderForSubject() {
        assertThrows(IllegalArgumentException.class,
                () -> new MessageInputBuilder()
                        .text("Hello").subject("Study group").build());

        MessageInput message = new MessageInputBuilder()
                .subject("Study group")
                .text("Hello")
                .sender("Matthew")
                .build();

        assertEquals("Matthew [Study group]: Hello",
                message.toMessageLine());
    }

    /** Checks line breaks before trimming in every field. */
    @Test
    void rejectsLineBreaks() {
        for (String value : new String[]{
                "Hello\nworld", "Hello\rworld", "\nHello", "Hello\r"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> new MessageInputBuilder().text(value).build());

            assertThrows(IllegalArgumentException.class,
                    () -> new MessageInputBuilder()
                            .text("Hello").sender(value).build());

            assertThrows(IllegalArgumentException.class,
                    () -> new MessageInputBuilder()
                            .text("Hello").sender("Matthew")
                            .subject(value).build());
        }
    }

    /** Checks that reusing a builder does not change an earlier message. */
    @Test
    void builtMessagesRemainUnchanged() {
        MessageInputBuilder builder = new MessageInputBuilder()
                .text("First").sender("Matthew");

        MessageInput first = builder.build();
        MessageInput second = builder.text("Second")
                .sender(null).build();

        assertEquals("Matthew: First", first.toMessageLine());
        assertEquals("Second", second.toMessageLine());
    }

    /** Checks exact output through the submission service used by the GUI. */
    @Test
    void submitsExpectedMessagesInOrder() {
        List<String> sent = new ArrayList<>();
        MessageSubmission submission = new MessageSubmission(sent::add);

        submission.submit("  Hello  ", null, null);
        submission.submit("Hello", " Matthew ", null);
        submission.submit("Hello", "Matthew", " Study group ");

        assertEquals(List.of(
                "Hello",
                "Matthew: Hello",
                "Matthew [Study group]: Hello"), sent);
    }

    /** Checks that empty optional UI controls mean omitted metadata. */
    @Test
    void treatsBlankOptionalControlsAsOmitted() {
        List<String> sent = new ArrayList<>();
        MessageSubmission submission = new MessageSubmission(sent::add);

        submission.submit("Hello", "   ", "");

        assertEquals(List.of("Hello"), sent);
    }

    /** Checks that invalid submissions never reach the destination. */
    @Test
    void blocksInvalidSubmissions() {
        List<String> sent = new ArrayList<>();
        MessageSubmission submission = new MessageSubmission(sent::add);

        for (String text : new String[]{null, "", "   ", "Hello\nworld"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> submission.submit(text, null, null));
        }

        assertThrows(IllegalArgumentException.class,
                () -> submission.submit("Hello", null, "Study group"));
        assertThrows(IllegalArgumentException.class,
                () -> submission.submit("Hello", "   ", "Study group"));
        assertThrows(IllegalArgumentException.class,
                () -> submission.submit("Hello", "\n", null));
        assertThrows(IllegalArgumentException.class,
                () -> submission.submit("Hello", "Matthew", "\r"));

        assertTrue(sent.isEmpty());
    }
}