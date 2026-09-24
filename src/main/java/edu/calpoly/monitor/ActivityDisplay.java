// TASK #75: Create Java Swing ActivityDisplay
package edu.calpoly.monitor;
import javax.swing.JFrame; // we need the window
import javax.swing.JPanel; // we need the container 
import javax.swing.JLabel; // we need make it visible
import java.awt.GridLayout; // we also need to place some layout using libraries

/**
 * DisplayDataActivity -> Must receives the messages adn also has the tracker included
 * ActivityDisplay must owns the swing window and also the count labels, in order to display them correctly
 * Example: Show ROBOT: 0 Messages, Last 60s
 * Therefore we will use JFrame Library, JPanel Library, JLabel,to display (GUI) 
 * 
 */



 // we do not need to create the main Constructor bc its gonna be a helper class


 /**
  * ActivityDisplay creates and manages the Swing window 
  * that shows the current 60-second message counts for 
  * - Robot, 
  * -Gaze, 
  * -Affect, and 
  * -LiDAR, and 
  * updates those labels when new counts are provided.
  * @author Diego Martinez Parra (sp4msvwnz)
  * @version version 1.0 (2026-09-23)
  * ActivityDisplay
  */

 
public class ActivityDisplay {
    private final JFrame frame;
    
    // four label fields (objects)
    private final JLabel robotCountLabel = new JLabel("Robot: 0 Messages"); // ROBOT
    
    private final JLabel gazeCountLabel = new JLabel("Gaze: 0 Messages"); // GAZE

    private final JLabel affectCountLabel = new JLabel("Affect: 0 Messages"); // AFFECT

    private final JLabel lidarCountLabel = new JLabel("LiDAR: 0 Messages"); // LIDAR

    // EXTRA

    private final JLabel timeWindowLabel = new JLabel("Last 60 Seconds"); // to display more UI


    // CONSTRUCTOR
    public ActivityDisplay() {
        frame = new JFrame("Recent Data Activity");

        int rows = 5;
        int columns = 1;
        
        // Create a panel

        JPanel panel = new JPanel(new GridLayout(rows, columns));
        panel.add(timeWindowLabel);
        panel.add(robotCountLabel);
        panel.add(gazeCountLabel);
        panel.add(affectCountLabel);
        panel.add(lidarCountLabel);
        frame.add(panel);
        // we have to tell them to close the application
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when the user closes the window
        // set the size
        frame.pack();

        // center the frame.

        frame.setLocationRelativeTo(null);

        // set it visible
        frame.setVisible(true);
    }

    // FOR #76 I NEED TO ADD A METHOD THAT WILL BE PUBLIC TO COUNT OR UPDATE COUNTS
    public void updateCounts(int robotCount, int gazeCount, int affectCount, int lidarCount) {
        robotCountLabel.setText("Robot: " + robotCount + " messages");
        gazeCountLabel.setText("Gaze: " + gazeCount + " messages");
        affectCountLabel.setText("Affect: " + affectCount + " messages");
        lidarCountLabel.setText("LiDAR: " + lidarCount + " messages");
        
    }

    
}