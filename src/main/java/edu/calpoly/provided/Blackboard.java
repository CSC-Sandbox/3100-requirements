package edu.calpoly.provided;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shared observable repository for A03. The Blackboard intentionally extends
 * PropertyChangeSupport, following the course's inheritance-based design.
 * Static methods expose one shared instance to producers and observers.
 *
 * @author CSC 3100 course staff
 * @version 1.0
 */
public final class Blackboard extends PropertyChangeSupport {
    private static final long serialVersionUID = 1L;
    public static final String GAZE_POSITION = "gaze.position";
    public static final String ROBOT_POSE = "robot.pose";
    public static final String LIDAR_SCAN = "lidar.scan";
    public static final String AFFECT_STATE = "affect.state";
    public static final String ACTIVITY_STATE = "activity.state";

    private static final Blackboard INSTANCE = new Blackboard();
    private final Map<String, Object> values = new ConcurrentHashMap<>();

    private Blackboard() {
        super(new Object());
    }

    /** Publishes a value and notifies listeners when it changes. */
    public static void put(String property, Object value) {
        Objects.requireNonNull(property, "property");
        Objects.requireNonNull(value, "value");
        Object previous = INSTANCE.values.put(property, value);
        INSTANCE.firePropertyChange(property, previous, value);
    }

    /** Returns the latest value, or null if none has been published. */
    public static Object get(String property) {
        return INSTANCE.values.get(Objects.requireNonNull(property, "property"));
    }

    /** Adds a listener for one property. */
    public static void subscribe(String property, PropertyChangeListener listener) {
        INSTANCE.addPropertyChangeListener(property, listener);
    }

    /** Removes a previously registered property-specific listener. */
    public static void unsubscribe(String property, PropertyChangeListener listener) {
        INSTANCE.removePropertyChangeListener(property, listener);
    }

    /** Clears stored values; intended for deterministic tests. */
    public static void clearValues() {
        INSTANCE.values.clear();
    }
}
