# GatherRobot
### RobotPose
A robot can have six joint angles and a Cartesian position.
This is all defined in a `RobotPose`, which is initialized with all of these values.
These values can be set and retrieved with the `setValues` and `getValues` methods respectively.
A `toString` method also exists which returns a `String` in the appropriate pose format.
<br><br>
### GatherRobot
Inside of this class is one method, `main`, which first instantiates a `RobotPose`, and uses the mentioned methods to display the data inside of that pose.
Next, it uses a `Broker` to send the string representation to localhost:5000.