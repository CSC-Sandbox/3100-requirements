package edu.calpoly.visualization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RobotDiagnostics {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(RobotDiagnostics.class);

    private RobotDiagnostics() {
        // Utility class
    }

    public static String summary(RobotPose pose) {
        return pose.toString();
    }

    public static void logPose(RobotPose pose) {
        LOGGER.debug("Robot pose: {}", summary(pose));
    }

    public static void logSend(RobotPose pose) {
        LOGGER.info("Sending robot pose: {}", summary(pose));
    }
}