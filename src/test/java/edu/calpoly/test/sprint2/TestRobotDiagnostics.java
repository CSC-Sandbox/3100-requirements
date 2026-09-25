package edu.calpoly.test.sprint2;

import edu.calpoly.visualization.RobotDiagnostics;
import edu.calpoly.visualization.RobotPose;

/**
 * Sprint 2 test program for SLF4J integration.
 *
 * Students should not modify this file to make their implementation pass.
 * Modify the implementation instead.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class TestRobotDiagnostics {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("Sprint 2 - SLF4J Test");

        testSummary();
        testLoggingMethods();

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Robot diagnostics tests failed.");
        }
    }

    private static void testSummary() {
        RobotPose pose =
                new RobotPose(1, 2, 3, 4, 5, 6, 7, 8, 9);

        String summary = RobotDiagnostics.summary(pose);

        check("ROBOT,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0".equals(summary),
                "creates the expected robot-pose summary");
    }

    private static void testLoggingMethods() {
        try {
            RobotPose pose =
                    new RobotPose(-1, 2, 3, 4, 5, 6, 7, 8, 9);

            RobotDiagnostics.logPose(pose);
            RobotDiagnostics.logSend(pose);

            pass("SLF4J logging methods execute");
        } catch (Exception e) {
            fail("SLF4J logging methods execute: " + e.getMessage());
        }
    }

    private static void check(boolean condition, String message) {
        if (condition) {
            pass(message);
        } else {
            fail(message);
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
