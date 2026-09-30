# Module 1 - Robot Visualization

## DisplayRobot

`DisplayRobot` is a Java Swing application that displays the current robot pose using incoming robot data.

The application receives robot pose messages through the course-provided `Broker`. Messages use the following format:

`ROBOT,J1,J2,J3,J4,J5,J6,X,Y,Z`

The six joint values (`J1` through `J6`) are used to create a 2D visualization of the robot. The `X`, `Y`, and `Z` values are also received and parsed as part of the robot pose.

As new robot pose data is received, the visualization automatically updates to display the most recent pose.

## Running and Testing

First, run the provided `TestDisplayRobot` test program. This starts the robot pose simulator on `localhost:5000`.

Then run `DisplayRobot`.

`DisplayRobot` connects to the simulator through `Broker` and continuously receives robot pose data. The robot visualization should update as new pose messages are received.
