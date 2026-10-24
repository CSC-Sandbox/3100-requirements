# CSC 3100 — MQTT Instructor Dashboard

A standalone Java 17 / Swing application that receives student READY and ACK messages and automatically sends each student's MQTT challenge.

## Important — Testing after class

**If you did not finish the lab during class, start this instructor dashboard first and KEEP IT RUNNING while you test your student application.** The dashboard acts as the instructor and sends the challenge; without it, your student application may wait indefinitely for a challenge.

1. Open a terminal in `lab/mqtt/instructor` and run:
   ```sh
   mvn compile exec:java
   ```
2. In the dashboard, leave the session as **fall2026** and click **Connect**. Wait for **Connected — fall2026**.
3. **Keep the dashboard open and connected.**
4. Open a **second terminal** in `lab/mqtt/student` and run your completed student code:
   ```sh
   mvn compile exec:java -Dexec.args="YOUR_ID fall2026"
   ```
5. Watch the dashboard for **READY → Challenge sent → Success**. Capture your student application's console output showing READY, the received challenge, and ACK.
6. To test another round, click **New Round** in the dashboard, then **restart the student application** so it subscribes and publishes READY again.

The dashboard uses the roster in `src/main/resources/students.csv`. Your `YOUR_ID` must match a roster ID (Cal Poly email username without the domain). The instructor code is **provided for local testing**; your graded work is in the student `Publisher` and `Subscriber` classes.

**Public broker warning:** All instances using `fall2026` share the same public MQTT topics. If the classroom instructor dashboard or another student's test dashboard is also active, you may receive duplicate challenges. For isolated testing, choose a unique session string in the dashboard and pass the **same** session to the student application. Never publish secrets or sensitive data.

## Protocol

- `csc3100/lab/SESSION/ready`: `ID:READY`
- `csc3100/lab/SESSION/challenge`: `ID:KEY`
- `csc3100/lab/SESSION/ack`: `ID:ACK:KEY`

Uses HiveMQ `tcp://broker.hivemq.com:1883`, UTF-8, QoS 1, and non-retained messages.
