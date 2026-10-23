package edu.calpoly.csc3100.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** Complete the TODOs to participate in the instructor's MQTT challenge. */
public final class StudentApp {
    private static final String BROKER = "tcp://broker.hivemq.com:1883";
    private final String studentId;
    private final String prefix;
    private MqttClient client;

    public StudentApp(String studentId, String session) {
        this.studentId = studentId;
        this.prefix = "csc3100/lab/" + session + "/";
    }

    public void connect() throws MqttException {
        // TODO 1: Create a MqttClient with a UNIQUE client ID (UUID is useful).
        // TODO 2: Register a MqttCallback that calls onMessage for incoming messages.
        // TODO 3: Connect to BROKER with a clean session.
        throw new UnsupportedOperationException("TODO: connect");
    }

    public void subscribe() throws MqttException {
        // TODO 4: Subscribe to prefix + "challenge" at QoS 1.
        throw new UnsupportedOperationException("TODO: subscribe");
    }

    public void sendReady() throws MqttException {
        // TODO 5: Publish studentId + ":READY" to prefix + "ready".
        // IMPORTANT: call this only AFTER the subscription succeeds.
        throw new UnsupportedOperationException("TODO: sendReady");
    }

    public void onMessage(String payload) {
        // TODO 6: Parse "id:key" and ignore messages for other students.
        // TODO 7: Extract the key and call publishAck(key).
        System.out.println("Received: " + payload);
    }

    public void publishAck(String key) throws MqttException {
        // TODO 8: Publish studentId + ":ACK:" + key to prefix + "ack" at QoS 1.
        throw new UnsupportedOperationException("TODO: publishAck");
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: mvn exec:java -Dexec.args=\"YOUR_ID SESSION\"");
            return;
        }
        StudentApp app = new StudentApp(args[0], args[1]);
        app.connect();
        app.subscribe();
        app.sendReady();
        System.out.println("Waiting for MQTT challenges. Press Ctrl+C to stop.");
        Thread.currentThread().join();
    }
}
