package edu.calpoly.provided;

import java.util.List;

/**
 * Course-provided test cases for the MessageValidator class.
 *
 * @author Javier Gonzalez-Sanchez (javiergs)
 * @version 1.0
 */
public class TestMessageValidator {
  private record TestCase(String description, String message, boolean expectedValid) {
  }

  public static void main(String[] args) {
    List<TestCase> cases = List.of(
        // CSV -- valid
        new TestCase("Valid ROBOT CSV",
            "ROBOT,0.42,-0.18,0.75,0.10,-0.32,0.57,0.25,0.10,0.42", true),
        new TestCase("Valid GAZE CSV",
            "GAZE,0.35,0.72", true),
        new TestCase("Valid AFFECT CSV",
            "AFFECT,0.72,0.44,0.65,0.58,0.33", true),
        new TestCase("Valid LIDAR CSV",
            "LIDAR,1.25,-0.40,0.15", true),

        // CSV -- invalid
        new TestCase("GAZE value out of range",
            "GAZE,1.40,0.72", false),
        new TestCase("AFFECT contains non-numeric data",
            "AFFECT,0.72,hello,0.65,0.58,0.33", false),
        new TestCase("LIDAR missing a value",
            "LIDAR,1.25,-0.40", false),
        new TestCase("ROBOT has too few values",
            "ROBOT,0.42,-0.18,0.75", false),
        new TestCase("Unknown message type",
            "TEMPERATURE,22.5", false),

        // JSON -- valid
        new TestCase("Valid ROBOT JSON",
            "{\"type\":\"ROBOT\",\"data\":[0.42,-0.18,0.75,0.10,-0.32,0.57,0.25,0.10,0.42]}", true),
        new TestCase("Valid GAZE JSON",
            "{\"type\":\"GAZE\",\"data\":[0.35,0.72]}", true),
        new TestCase("Valid AFFECT JSON",
            "{\"type\":\"AFFECT\",\"data\":[0.72,0.44,0.65,0.58,0.33]}", true),
        new TestCase("Valid LIDAR JSON",
            "{\"type\":\"LIDAR\",\"data\":[1.25,-0.40,0.15]}", true),

        // JSON -- invalid
        new TestCase("JSON missing type",
            "{\"data\":[0.35,0.72]}", false),
        new TestCase("JSON missing data",
            "{\"type\":\"GAZE\"}", false),
        new TestCase("Invalid JSON syntax",
            "{\"type\":\"LIDAR\",\"data\":[1.25,-0.40,0.15]", false),
        new TestCase("AFFECT JSON value out of range",
            "{\"type\":\"AFFECT\",\"data\":[0.72,0.44,1.20,0.58,0.33]}", false)
    );

    System.out.println("CSC 3100 - Message Validator Test Cases");
    System.out.println("Use these messages to test your MessageValidator implementation.\n");

    int number = 1;
    for (TestCase testCase : cases) {
      System.out.println("Test " + number++ + ": " + testCase.description());
      System.out.println("Message:  " + testCase.message());
      System.out.println("Expected: " + (testCase.expectedValid() ? "VALID" : "INVALID"));
      System.out.println();
    }

    System.out.println("Your validator should continue processing after every invalid message.");
  }
}
