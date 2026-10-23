# CSC 3100 — MQTT Student Starter

Implement the TODOs in `src/main/java/edu/calpoly/csc3100/mqtt/StudentApp.java`. Requires Java 17+ and Maven.

## Protocol

Broker: `tcp://broker.hivemq.com:1883` (free public HiveMQ broker).

- Subscribe first: `csc3100/lab/SESSION/challenge`
- Publish READY: `csc3100/lab/SESSION/ready` with `YOUR_ID:READY`
- Receive challenge: `YOUR_ID:RANDOM_KEY`
- Publish ACK: `csc3100/lab/SESSION/ack` with `YOUR_ID:ACK:RANDOM_KEY`

Use UTF-8, QoS 1, and non-retained messages. Ignore challenges for other students. Print useful console diagnostics. Use a unique MQTT client ID per running process. The student ID in the message is **not** the MQTT connection client ID.

Do not publish passwords or private information to a public broker.
