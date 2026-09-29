package edu.calpoly.test.sprint2;

import edu.calpoly.messages.MessageInput;

/**
 * Sprint 2 test program for Apache Commons Lang integration.
 *
 * Students should not modify this file to make their implementation pass.
 * Modify the implementation instead.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class TestMessageInput {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("Sprint 2 - Commons Lang Test");

        testBlankInput();
        testNormalizeInput();
        testNormalMessage();

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Message input tests failed.");
        }
    }

    private static void testBlankInput() {
        check(!MessageInput.isValid(null), "null input is invalid");
        check(!MessageInput.isValid("   "), "blank input is invalid");
    }

    private static void testNormalizeInput() {
        check("Hello".equals(MessageInput.normalize("  Hello  ")),
                "leading and trailing whitespace is removed");
        check(MessageInput.normalize(null) == null,
                "null normalization remains null");
    }

    private static void testNormalMessage() {
        String message = "GAZE,0.42,0.71";
        check(MessageInput.isValid(message), "normal message is valid");
        check(message.equals(MessageInput.normalize(message)),
                "normal message remains unchanged");
    }

    private static void check(boolean condition, String message) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + message);
        } else {
            failed++;
            System.out.println("[FAIL] " + message);
        }
    }
}
