# CSC 3100 — MQTT Student Starter

Complete the MQTT TODOs in **Publisher.java** and **Subscriber.java**. The provided **Main.java** coordinates both clients; do not change it.

## Structure

- `Main`: creates the publisher and subscriber; connects, subscribes, then sends READY.
- `Publisher`: publishes READY and ACK messages (TODOs 1–4).
- `Subscriber implements MqttCallback`: receives challenges, filters by student ID, and invokes `publisher.sendAck(key)` (TODOs 5–11).

The publisher and subscriber use **separate MQTT connections** and must have **different unique client IDs**. Use `MemoryPersistence` so Paho does not create local MQTT persistence directories.

## Protocol

Broker: `tcp://broker.hivemq.com:1883` (public HiveMQ broker).

- Subscribe to `csc3100/lab/SESSION/challenge` **before** sending READY.
- Publish `YOUR_ID:READY` to `csc3100/lab/SESSION/ready`.
- Receive `YOUR_ID:RANDOM_KEY` on the shared challenge topic.
- Ignore challenges addressed to other students.
- Publish `YOUR_ID:ACK:RANDOM_KEY` to `csc3100/lab/SESSION/ack`.

Use UTF-8, QoS 1, non-retained messages and a clean session. Use your Cal Poly email username **without** `@calpoly.edu` as YOUR_ID. The student ID in the payload is not the MQTT client ID.

## Run

Requires Java 17+ and Maven. From `lab/mqtt/student`:

```sh
mvn compile exec:java -Dexec.args="YOUR_ID fall2026"
```

The starter compiles, but it intentionally throws `UnsupportedOperationException` until the TODOs are implemented. After connecting and subscribing, it publishes READY once; restart it to request another challenge.

This is a shared public broker: do not publish passwords, private information, or real secrets.
