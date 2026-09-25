/**
 * Handles the gathering of robot pose data.
 *
 * @author Tobin Discoe (sipactivism)
 * @version 1.0 (2026-09-23)
 */

package edu.calpoly.visualization;

import edu.calpoly.provided.Broker;

public class GatherRobot {
    public void sendData(Broker broker, RobotPose pose) {
        double[] vals = pose.getValues();

        System.out.println("Generated Robot Pose");
        System.out.println();
        System.out.println("J1: " + (vals[0]<0 ? "" : " ") + vals[0]); // using a ? operator,
        System.out.println("J2: " + (vals[1]<0 ? "" : " ") + vals[1]); // i only add an extra space
        System.out.println("J3: " + (vals[2]<0 ? "" : " ") + vals[2]); // if the number isn't negative.
        System.out.println("J4: " + (vals[3]<0 ? "" : " ") + vals[3]); // which makes it look nicer.
        System.out.println("J5: " + (vals[4]<0 ? "" : " ") + vals[4]);
        System.out.println("J6: " + (vals[5]<0 ? "" : " ") + vals[5]);
        System.out.println("X:  " + (vals[6]<0 ? "" : " ") + vals[6]);
        System.out.println("Y:  " + (vals[7]<0 ? "" : " ") + vals[7]);
        System.out.println("Z:  " + (vals[8]<0 ? "" : " ") + vals[8]);
        System.out.println();
        System.out.println("Sending:");
        System.out.println(pose);

        broker.send(pose.toString());
    }

    void main() {
        RobotPose pose = new RobotPose(-1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0);
        Broker broker = new Broker("localhost",5000);
        GatherRobot robot = new GatherRobot();
        robot.sendData(broker, pose);
    }
}
