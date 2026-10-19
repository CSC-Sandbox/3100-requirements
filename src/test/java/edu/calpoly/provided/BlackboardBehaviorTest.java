package edu.calpoly.provided;

import static org.junit.jupiter.api.Assertions.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** Behavioral contract for the instructor-provided event repository. */
class BlackboardBehaviorTest {
    private final List<PropertyChangeListener> listeners = new ArrayList<>();

    @AfterEach
    void cleanup() {
        for (PropertyChangeListener listener : listeners) {
            Blackboard.unsubscribe(Blackboard.GAZE_POSITION, listener);
        }
        Blackboard.clearValues();
    }

    @Test
    void generatorPublishesThreeMeasurementsAndRetainsLatestValue() {
        List<GazePosition> received = new ArrayList<>();
        PropertyChangeListener observer = event -> received.add((GazePosition) event.getNewValue());
        listeners.add(observer);
        Blackboard.subscribe(Blackboard.GAZE_POSITION, observer);
        A03DataGenerator.publishGazeSequence();
        assertEquals(List.of(new GazePosition(0.10, 0.20),
                new GazePosition(0.50, 0.75),
                new GazePosition(0.90, 0.40)), received);
        assertEquals(new GazePosition(0.90, 0.40), Blackboard.get(Blackboard.GAZE_POSITION));
    }

    @Test
    void removingObserverStopsFurtherNotifications() {
        List<Object> received = new ArrayList<>();
        PropertyChangeListener observer = event -> received.add(event.getNewValue());
        Blackboard.subscribe(Blackboard.GAZE_POSITION, observer);
        Blackboard.put(Blackboard.GAZE_POSITION, new GazePosition(0.10, 0.20));
        Blackboard.unsubscribe(Blackboard.GAZE_POSITION, observer);
        Blackboard.put(Blackboard.GAZE_POSITION, new GazePosition(0.50, 0.75));
        assertEquals(1, received.size());
    }

    @Test
    void unrelatedPropertyDoesNotNotifyGazeObserver() {
        List<Object> received = new ArrayList<>();
        PropertyChangeListener observer = event -> received.add(event.getNewValue());
        listeners.add(observer);
        Blackboard.subscribe(Blackboard.GAZE_POSITION, observer);
        Blackboard.put(Blackboard.ACTIVITY_STATE, "ACTIVE");
        assertTrue(received.isEmpty());
    }
}
