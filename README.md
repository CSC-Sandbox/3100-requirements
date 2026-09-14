# CSC 3100 — Software Engineering

This repository contains the product backlog, course-provided infrastructure, and student implementations developed throughout the course.

## Requirements

GitHub Issues are the source of truth for the system's **User Stories and Acceptance Criteria**.

Each assigned Issue also contains the specific programming instructions, testing information, and documentation requirements for that assignment.

## Repository Structure

```text
3100-requirements/
├── README.md
├── pom.xml
├── docs/
│   └── ...
└── src/
    └── main/
        └── java/
            └── edu/
                └── calpoly/
                    ├── provided/
                    │   ├── Broker.java
                    │   ├── TestGatherRobot.java
                    │   ├── TestDisplayRobot.java
                    │   ├── TestGatherEye.java
                    │   ├── TestDisplayEye.java
                    │   ├── TestDisplayAffect.java
                    │   ├── TestDisplayLidar.java
                    │   ├── Encryption.java
                    │   ├── TestEncryption.java
                    │   ├── TestMessageValidator.java
                    │   ├── TestEnterMessage.java
                    │   ├── TestDisplayMessages.java
                    │   ├── TestMonitorData.java
                    │   ├── TestStoreMessages.java
                    │   └── TestRetrieveMessages.java
                    └── ...
├── data/
│   ├── store-input.txt
│   ├── messages.csv
│   └── empty-messages.csv
```

The package:

```text
edu.calpoly.provided
```

contains infrastructure supplied by the instructor.

Students should **use these classes but should not modify them** unless specifically instructed.

Additional provided classes may be added as the project evolves.


## Common Data Message Contract

Data messages exchanged by the Robot, Gaze, Affect, and LiDAR modules use a common text format:

```text
TYPE,data...
```

The first field identifies the data source. The remaining fields contain the data for that source.

```text
ROBOT,J1,J2,J3,J4,J5,J6,X,Y,Z
GAZE,X,Y
AFFECT,focus,excitement,engagement,interest,stress
LIDAR,X,Y,Z
```

Examples:

```text
ROBOT,0.420,-0.180,0.750,0.100,-0.320,0.570,0.250,0.100,0.420
GAZE,0.35,0.72
AFFECT,0.72,0.44,0.65,0.58,0.33
LIDAR,1.25,-0.40,0.15
```

The `Broker` transports these messages as strings. It does not interpret the message type or values.

## Programming Workflow

Programming work begins from an assigned User Story.

```text
User Story
    ↓
Create a branch
    ↓
Design with UML
    ↓
Implement in Java
    ↓
Test
    ↓
Open a Pull Request
    ↓
Instructor review
    ↓
Merge
```

Do not commit programming work directly to `main`.

Create a separate branch for your assigned User Story. For example:

```text
13-gather-robot
```

Your Pull Request should identify the User Story being implemented. For example:

```text
Implement #13 Gather Robot Pose
```

## Provided Infrastructure

Course-provided classes are located under:

```text
src/main/java/edu/calpoly/provided/
```

The provided `Broker` class supplies the communication interface used by assignments that exchange data through the course communication service.

For example:

```java
import edu.calpoly.provided.Broker;

Broker broker = new Broker("localhost", 5000);
```

Depending on the assigned User Story, an application may send or receive data through the `Broker`. Not every assignment uses the `Broker`.

The package also contains other course utilities when needed. For example, `Encryption` provides the encryption/decryption operations used by the security module.

The details of what your program must send, receive, display, validate, or test are specified in your assigned GitHub Issue.

## Testing

Course-provided testing programs follow the naming convention:

```text
Test...
```

For example:

```text
TestGatherRobot
TestDisplayRobot
TestGatherEye
TestDisplayEye
TestDisplayAffect
TestDisplayLidar
TestEncryption
TestMessageValidator
TestEnterMessage
TestDisplayMessages
TestMonitorData
TestStoreMessages
TestRetrieveMessages
```

Your assigned GitHub Issue identifies the test program to use and explains the expected behavior.

In general, start the provided test program first and leave it running, then run your implementation.

## Documentation

Design documentation for each User Story belongs under:

```text
docs/
```

Use the directory specified in your assigned GitHub Issue.

Do **not** modify this repository's main `README.md` as part of your programming assignment.
