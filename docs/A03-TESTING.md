# A03 — Design Pattern Refactoring and Behavioral Tests

Each student must improve an **existing feature**, not merely add a pattern-shaped class.
The assigned Story identifies the required pattern and existing component.

## Instructor-provided Blackboard

`edu.calpoly.provided.Blackboard` extends `PropertyChangeSupport`.
Its **static** `put`, `get`, `subscribe`, and `unsubscribe` methods
operate on one shared observable repository. Do not modify instructor-provided code.

Properties: `gaze.position`, `robot.pose`, `lidar.scan`,
`affect.state`, and `activity.state`. The instructor fixture
`A03DataGenerator.publishGazeSequence()` publishes three immutable
`GazePosition` records with known values. Other data contracts and
component-level acceptance tests will be specified in the corresponding Stories.

## Required evidence

1. Identify the actual design problem in the existing implementation.
2. Implement the assigned pattern **in the application's execution path**.
3. Demonstrate observable behavior with controlled inputs and expected outputs.
4. Verify that the original feature still works and that the refactoring
   has not introduced regressions.
5. Submit updated UML (PNG or JPEG), JavaDoc, tests, and a Story/Task-linked PR.

For Swing observers, update components on the Event Dispatch Thread using
`SwingUtilities.invokeLater`; tests should wait for the EDT to finish.

## Run

```bash
mvn clean verify
```

`BlackboardBehaviorTest` verifies the instructor infrastructure:
notification, latest-value storage, property filtering, and unsubscribe.
**It does not certify a student's Observer implementation.**
Each student's feature-specific behavioral acceptance test must additionally
verify the actual modified application component.

For Strategy, Factory, Builder, Decorator, and Composite, tests must check
the changed behavior and integration with the existing client, not only
the existence of pattern-related class names.
