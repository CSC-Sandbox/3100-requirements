package edu.calpoly.provided;

/** Immutable normalized gaze coordinates for A03 fixtures.
 * @author CSC 3100 course staff
 * @version 1.0
 */
public record GazePosition(double x, double y) {
    public GazePosition {
        if (!Double.isFinite(x) || !Double.isFinite(y)
                || x < 0 || x > 1 || y < 0 || y > 1) {
            throw new IllegalArgumentException("Coordinates must be finite and in [0,1]");
        }
    }
}
