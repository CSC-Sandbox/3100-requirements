# CSC 3100 — Software Engineering

This repository contains the product backlog, course-provided infrastructure, design documentation, and student implementations developed throughout the course.

## Requirements

GitHub Issues are the source of truth for the system's **User Stories and Acceptance Criteria**.

Each assigned Issue also contains the specific programming instructions, testing information, and documentation requirements for that assignment.

## Repository Structure

The repository evolves throughout the course as User Stories are implemented and merged.

```text
3100-requirements/
├── README.md
├── pom.xml
├── data/
│   ├── store-input.txt
│   ├── messages.csv
│   └── empty-messages.csv
├── docs/
│   ├── 13-gather-robot/
│   ├── 34-gather-eye/
│   ├── 40-display-affect/
│   ├── module-4-security/
│   ├── module-5-chat/
│   ├── module-6-monitor/
│   ├── module-7-storage/
│   └── module-8-storage/
└── src/
    └── main/
        └── java/
            ├── ...
            └── edu/
                └── calpoly/
                    ├── ...
                    ├── messages/
                    │   └── ...
                    └── provided/
                        ├── Broker.java
                        ├── Encryption.java
                        ├── TestGatherRobot.java
                        ├── TestDisplayRobot.java
                        ├── TestGatherEye.java
                        ├── TestDisplayEye.java
                        ├── TestDisplayAffect.java
                        ├── TestDisplayLidar.java
                        ├── TestEncryption.java
                        ├── TestMessageValidator.java
                        ├── TestEnterMessage.java
                        ├── TestDisplayMessages.java
                        ├── TestMonitorData.java
                        ├── TestStoreMessages.java
                        └── TestRetrieveMessages.java
```

Student implementations are added under:

```text
src/main/java/
```

As the system grows, related classes may be organized into packages under:

```text
edu.calpoly
```

Course-provided infrastructure is located under:

```text
edu.calpoly.provided
```

Students should **use the provided classes but should not modify them** unless specifically instructed.

Additional classes, packages, tests, and documentation directories will be added as the project evolves.

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
Create Tasks as sub-issues
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
Make corrections if requested
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

Once a Pull Request is approved and merged, that implementation becomes part of the shared codebase. Future work should build on the current `main` branch rather than replacing existing implementations.

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

The details of what your program must send, receive, display, validate, store, retrieve, or test are specified in your assigned GitHub Issue.

## Student Implementations

Student implementations are merged into the repository as User Stories are completed and approved.

These classes become part of the evolving system and may be used by later User Stories.

Before beginning new programming work:

1. Make sure your local `main` branch is up to date.
2. Review the existing classes and packages relevant to your User Story.
3. Reuse existing implementations when appropriate.
4. Do not duplicate functionality that already exists in the shared codebase.
5. Create a new branch for your assigned work.

The architecture and package structure may evolve as the individual User Stories are integrated into a larger system.

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

Passing the provided test demonstrates the expected behavior for the assigned User Story. It does not prevent you from creating additional tests for your implementation.

## Documentation

Design documentation belongs under:

```text
docs/
```

Depending on the User Story or module, documentation may be organized by Story:

```text
docs/13-gather-robot/
docs/34-gather-eye/
```

or by module:

```text
docs/module-5-chat/
docs/module-7-storage/
```

Follow the directory specified in your assigned GitHub Issue.

Design documentation should include the UML diagram and any additional documentation required by the User Story.

Do **not** modify this repository's main `README.md` as part of your programming assignment.
