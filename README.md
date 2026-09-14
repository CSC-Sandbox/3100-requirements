# CSC 3100 — Software Engineering

This repository contains the product backlog, course-provided infrastructure, and student implementations developed throughout the course.

The GitHub Issues in this repository are the source of truth for the system's User Stories and Acceptance Criteria.

## Repository Structure

```text
3100-requirements/
├── README.md
├── pom.xml
└── src/
    └── main/
        └── java/
            └── edu/
                └── calpoly/
                    └── provided/
                        ├── Broker.java
                        └── TestGatherRobot.java
```

The package `edu.calpoly.provided` contains infrastructure supplied by the instructor.

Students should **use these classes but should not modify them** unless specifically instructed.

## Programming Workflow

Programming work begins from an assigned User Story.

The expected workflow is:

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

Each programming assignment should be completed on a separate branch associated with the assigned User Story.

Example:

```text
13-gather-robot
```

A Pull Request should identify the User Story being implemented.

Example:

```text
Implement #13 Gather Robot Pose
```

## Provided Communication Infrastructure

For the first programming assignment, communication infrastructure is provided through:

```java
import edu.calpoly.provided.Broker;
```

Example usage:

```java
Broker broker = new Broker("localhost", 5000);
broker.send(message);
```

`Broker` hides the underlying socket communication from the application.

For the first assignment, communication occurs locally using TCP sockets. Future assignments may use different communication mechanisms while preserving a similar application-facing abstraction.

## Testing

`TestGatherRobot.java` is provided for testing the Gather Robot Pose User Story.

Start the test program first:

```text
TestGatherRobot
```

It waits for data on:

```text
localhost:5000
```

Then run the student's implementation.

When data is sent successfully, the test program prints the received message.

The student's program is responsible for deciding how robot pose data is represented and organized internally.
