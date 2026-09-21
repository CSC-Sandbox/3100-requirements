// TASK #74: 60 Second-ActivityTracker
// For this particular task, I wanted to have a tracker of the timestap, time and related to the messages

/**
 * Source Type must be received (condition) -> TASK #74(a).
 * Store its arrival time() -> TASK #74(b).
 * -Rm times > 60s() ->> TASK #74(c).
 * Return the currentCount from that specific Source  -> #74(d) 
 *  - ROBOT
 *  - GAZE
 *  - AFFECT
 *  - LIDAR
 *  - 
 * 
 */

package edu.calpoly;

import java.util.ArrayDeque;
import java.util.Deque;


public class ActivityTracker {

    // Need to create the constant -> Must be private as well
    
    private static final long WINDOW_MIL = 60_000L;

    // TimeStamp queues
    //Source Type must be received (condition) -> TASK #74(a).

    // Robot
    private final Deque<Long> robotTimestamp = new ArrayDeque<>();

    //Gaze
    private final Deque<Long> gazeTimestamp = new ArrayDeque<>();

    // Affect
    private final Deque<Long> affectTimestamp = new ArrayDeque<>();

    // Lidar 
    private final Deque<Long> lidarTimestamp = new ArrayDeque<>();


    // a method that will then reiceved the source type -> current time recorded -> timestamp added to the source's queue

    //Store its arrival time() -> TASK #74(b).
    public void recordTimestamp(String sourceType) {
        long now = System.currentTimeMillis();
        
        switch (sourceType) {
            case "ROBOT":
                robotTimestamp.addLast(now);
    
                break;
            
            case "GAZE":
                gazeTimestamp.addLast(now);
                break;

            case "AFFECT":
                affectTimestamp.addLast(now);
                break;

            case "LIDAR":
                lidarTimestamp.addLast(now);
                break;

            default:
                return;
        }
        
    }
    //-Rm times > 60s() ->> TASK #74(c).
    // method
    private void removeExpiredTimestamps(Deque<Long> timestamps, long currentTime) {
        while (!timestamps.isEmpty() && currentTime - timestamps.peekFirst() > WINDOW_MIL) {
                
            timestamps.removeFirst();
        
            }  
    }
    
    //  Return the currentCount from that specific Source  -> #74(d) (COMBINE ALL THE ABOVE)
    // another method
    public int getRecentCount(String sourceType) {

        long now = System.currentTimeMillis();

        switch (sourceType) {
            case "ROBOT":
                removeExpiredTimestamps(robotTimestamp, now);
                return robotTimestamp.size();
            
            case "GAZE":
                removeExpiredTimestamps(gazeTimestamp, now);
                return gazeTimestamp.size();

            case "AFFECT":
                removeExpiredTimestamps(affectTimestamp, now);
                return affectTimestamp.size();

            case "LIDAR":
                removeExpiredTimestamps(lidarTimestamp, now);
                return lidarTimestamp.size();
        
            default:
                return 0;
        }
    }


}



