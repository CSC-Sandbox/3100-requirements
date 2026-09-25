package edu.calpoly.test.sprint2;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Sprint 2 test program for Classify System Activity.
 *
 * Students should not modify this file to make their implementation pass.
 * Modify the implementation instead.
 *
 * Expected student interface:
 *
 *   public static String classify(double messageRate,
 *                                 double secondsSinceLastMessage)
 *
 * The classifier must use the Tribuo model described in the assigned story.
 *
 * This program intentionally uses main() rather than JUnit. JUnit will be
 * introduced in a later activity.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class TestActivityClassifier {

    private static final String IMPLEMENTATION_CLASS =
            "edu.calpoly.monitor.ActivityClassifier";

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("Sprint 2 - Activity Classifier Test");

        testValidLabel(0.0, 60.0);
        testValidLabel(2.0, 1.0);
        testValidLabel(12.0, 0.1);

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Activity classifier tests failed.");
        }
    }

    private static void testValidLabel(double rate, double idle) {
        try {
            Class<?> clazz = Class.forName(IMPLEMENTATION_CLASS);
            Method method = clazz.getMethod(
                    "classify", double.class, double.class);

            if (!Modifier.isStatic(method.getModifiers())) {
                fail("classify must be static");
                return;
            }

            Object result = method.invoke(null, rate, idle);

            if (!(result instanceof String)) {
                fail("classify must return String");
                return;
            }

            String label = (String) result;
            if ("NO_ACTIVITY".equals(label)
                    || "NORMAL".equals(label)
                    || "HIGH_ACTIVITY".equals(label)) {
                pass("(" + rate + ", " + idle + ") -> " + label);
            } else {
                fail("Unexpected classification: " + label);
            }
        } catch (ClassNotFoundException e) {
            fail("Missing class " + IMPLEMENTATION_CLASS);
        } catch (NoSuchMethodException e) {
            fail("Missing public static method: classify(double, double)");
        } catch (Exception e) {
            fail("Unexpected error: " + e.getClass().getSimpleName()
                    + " - " + e.getMessage());
        }
    }

    private static void pass(String message) {
        passed++;
        System.out.println("[PASS] " + message);
    }

    private static void fail(String message) {
        failed++;
        System.out.println("[FAIL] " + message);
    }
}
