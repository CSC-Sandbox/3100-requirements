package edu.calpoly.provided;

/**
 * Deterministic publisher used by A03 observers and acceptance tests.
 * @author CSC 3100 course staff
 * @version 1.0
 */
public final class A03DataGenerator {
    private A03DataGenerator() { }

    /** Publishes a known sequence of gaze samples to the shared Blackboard. */
    public static void publishGazeSequence() {
        Blackboard.put(Blackboard.GAZE_POSITION, new GazePosition(0.10, 0.20));
        Blackboard.put(Blackboard.GAZE_POSITION, new GazePosition(0.50, 0.75));
        Blackboard.put(Blackboard.GAZE_POSITION, new GazePosition(0.90, 0.40));
    }

    /** Publishes a known activity-state transition. */
    public static void publishActivitySequence() {
        Blackboard.put(Blackboard.ACTIVITY_STATE, "IDLE");
        Blackboard.put(Blackboard.ACTIVITY_STATE, "ACTIVE");
        Blackboard.put(Blackboard.ACTIVITY_STATE, "IDLE");
    }
}
