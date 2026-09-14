package edu.calpoly.provided;

/** Course-provided receiver used to observe robot pose messages. */
public class TestGatherRobot {
    public static void main(String[] args) {
        Broker broker = new Broker("localhost", 5000);
        System.out.println("Waiting for robot pose data...");
        while (true) {
            System.out.println("Robot Pose Received");
            System.out.println(broker.receive());
        }
    }
}
