# Module 6 - Monitor Data Availability

## Design

`MonitorAvailability` is a Java Swing application that displays the availability of Robot, Gaze, Affect, and LiDAR data at the same time. The class extends `JFrame` and owns four status labels, one for each source.

The application receives system messages through the course-provided `Broker`. It reads the message type from the text before the first comma and saves the current time for the matching source. A Swing `Timer` runs every 100 milliseconds and compares each saved time to the current time:

- A source is **AVAILABLE** when its last received message was no more than one second ago.
- A source is **UNAVAILABLE** when more than one second has passed without a message.

The timer updates each label automatically, so a stopped source becomes unavailable even while messages from the other sources are still arriving.

## How to Run

From `src/main/java`, compile the monitor and the provided classes:

```bash
javac edu/calpoly/provided/Broker.java edu/calpoly/provided/TestMonitorData.java MonitorAvailability.java
```

Open two terminals in `src/main/java`.

In the first terminal, start the provided data tester:

```bash
java edu.calpoly.provided.TestMonitorData
```

In the second terminal, start the monitor:

```bash
java MonitorAvailability
```

## Testing

The provided tester initially sends messages from all four sources. The GUI should show all four as **AVAILABLE**. During the test, the tester temporarily stops one source at a time. That source should become **UNAVAILABLE** after one second, while the other three remain available. When the tester resumes the source, its label should return to **AVAILABLE**.

## Class

```java

/**
 * A GUI window that monitors the availability of Robot, Gaze, Affect,
 * and LiDAR data sources.
 *
 * The class stores the most recent time valid data was received from
 * each source. It uses these timestamps to determine whether a source is
 * available or unavailable and updates the corresponding status labels.
 *
 * A source is considered unavailable if no valid data has been received
 * for more than one second. When valid data is received again, its status
 * returns to available.
 */

public class MonitorAvailability extends JFrame{
    private static volatile long robotLastReceived = 0;
    private static volatile long gazeLastReceived = 0;
    private static volatile long affectLastReceived = 0;
    private static volatile long lidarLastReceived = 0;

    private JLabel robotLabel;
    private JLabel gazeLabel;
    private JLabel affectLabel;
    private JLabel lidarLabel;

```