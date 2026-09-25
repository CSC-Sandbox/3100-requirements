package edu.calpoly.test.sprint2;

import edu.calpoly.messages.EncryptMessage;
import edu.calpoly.provided.Encryption;

/**
 * Sprint 2 test program for Apache Commons Codec integration.
 *
 * Students should not modify this file to make their implementation pass.
 * Modify the implementation instead.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class TestMessageSecurity {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("Sprint 2 - Commons Codec Test");

        testDigest();
        testDigestIsStable();
        testEncryptionStillWorks();

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Message security tests failed.");
        }
    }

    private static void testDigest() {
        String actual = EncryptMessage.messageDigest("CSC 3100");
        String expected =
                "f1b74b4a7f3b6f38c7641d3e25ecdd1f3ac933aa0d6fd04ef6aab8f0f98e1f15";

        check(expected.equals(actual),
                "SHA-256 digest matches expected value");
    }

    private static void testDigestIsStable() {
        String first = EncryptMessage.messageDigest("hello");
        String second = EncryptMessage.messageDigest("hello");

        check(first.equals(second),
                "same message produces same digest");
        check(first.length() == 64,
                "SHA-256 hexadecimal digest has 64 characters");
    }

    private static void testEncryptionStillWorks() {
        String message = "ROBOT,1,2,3";
        String encrypted = Encryption.encrypt(message);
        String decrypted = Encryption.decrypt(encrypted);

        check(message.equals(decrypted),
                "existing AES encryption/decryption still works");
        check(!message.equals(encrypted),
                "digest integration does not replace encryption");
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
