package edu.calpoly.test.sprint2;

import edu.calpoly.eye.GazeStatistics;

/**
 * Sprint 2 test program for Apache Commons Math integration.
 *
 * Students should not modify this file to make their implementation pass.
 * Modify the implementation instead.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class TestGazeStatistics {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("Sprint 2 - Commons Math Test");

        testMean();
        testStandardDeviation();

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed > 0) {
            throw new AssertionError("Gaze statistics tests failed.");
        }
    }

    private static void testMean() {
        double[] values = {0.10, 0.20, 0.30, 0.40};
        check(close(GazeStatistics.mean(values), 0.25),
                "calculates the mean of gaze values");
    }

    private static void testStandardDeviation() {
        double[] values = {1.0, 2.0, 3.0};
        check(close(GazeStatistics.standardDeviation(values), 1.0),
                "calculates sample standard deviation");
    }

    private static boolean close(double actual, double expected) {
        return Math.abs(actual - expected) < 0.000001;
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
