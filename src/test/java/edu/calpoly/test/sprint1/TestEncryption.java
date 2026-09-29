package edu.calpoly.test.sprint1;

import edu.calpoly.provided.Encryption;

/**
 * Course-provided demonstration and round-trip test for the Encryption utility.
 *
 * @author Javier Gonzalez-Sanchez (javiergs)
 * @version 1.0 (2026-09-01)
 */
public class TestEncryption {

  private static final String[] TEST_MESSAGES = {
      "Hello World",
      "Meet me at 3:00 PM",
      "CSC 3100 - Software Engineering!"
  };

  public static void main(String[] args) {
    int passed = 0;

    for (String original : TEST_MESSAGES) {
      String encrypted = Encryption.encrypt(original);
      String recovered = Encryption.decrypt(encrypted);

      boolean encryptedChanged = !original.equals(encrypted);
      boolean recoveredMatches = original.equals(recovered);
      boolean success = encryptedChanged && recoveredMatches;

      System.out.println("Original:  " + original);
      System.out.println("Encrypted: " + encrypted);
      System.out.println("Recovered: " + recovered);
      System.out.println(success ? "PASS" : "FAIL");
      System.out.println();

      if (success) {
        passed++;
      }
    }

    System.out.println("Result: " + passed + "/" + TEST_MESSAGES.length + " tests passed.");
  }
}
