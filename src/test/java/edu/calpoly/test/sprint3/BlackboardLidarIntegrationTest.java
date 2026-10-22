package edu.calpoly.test.sprint3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jzy3d.maths.Coord3d;

import edu.calpoly.provided.Blackboard;
import edu.calpoly.visualization.Lidar3DPanel;
import edu.calpoly.visualization.LidarScanListener;

/**
 * Verifies that the LiDAR display renders the latest scan published to the
 * Blackboard, updates on the Event Dispatch Thread, and stops updating once
 * its listener is unsubscribed.
 *
 * @author Marcus Hauen-Limkilde
 * @version 1.0
 */

public class BlackboardLidarIntegrationTest {
    private static final Coord3d[] SCAN_A = { new Coord3d(1, 1, 1) };
    private static final Coord3d[] SCAN_B = { new Coord3d(2 , 2, 2) };
    private static final Coord3d[] SCAN_C = { new Coord3d(3, 3, 3) };

    private Lidar3DPanel panel;
    private LidarScanListener listener;

    @BeforeEach
    void setUp() {
        Blackboard.clearValues();
        panel = new Lidar3DPanel(new Coord3d[0]);
        listener = new LidarScanListener(panel);
        Blackboard.subscribe(Blackboard.LIDAR_SCAN, listener);
    }

    private void flushEdt() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {});
    }

    @Test 
    void secondScanReplacesFirst() throws Exception { 
        Blackboard.put(Blackboard.LIDAR_SCAN, SCAN_A);
        flushEdt();
        assertArrayEquals(SCAN_A, panel.getLastPoints());

        Blackboard.put(Blackboard.LIDAR_SCAN, SCAN_B);
        flushEdt();
        assertArrayEquals(SCAN_B, panel.getLastPoints());
    }

    @Test 
    void noUpdatesAfterUnsubscribe() throws Exception { 
        Blackboard.put(Blackboard.LIDAR_SCAN, SCAN_A);
        flushEdt();
        assertArrayEquals(SCAN_A, panel.getLastPoints());

        Blackboard.unsubscribe(Blackboard.LIDAR_SCAN, listener);

        Blackboard.put(Blackboard.LIDAR_SCAN, SCAN_B);
        flushEdt();
        assertArrayEquals(SCAN_A, panel.getLastPoints());
    }

    @AfterEach
    void tearDown() {
        Blackboard.unsubscribe(Blackboard.LIDAR_SCAN, listener);
    }

    @Test
    void updatePointsRejectsCallsOffTheEdt() {
        assertThrows(IllegalStateException.class, () -> panel.updatePoints(SCAN_C));
    }
}

