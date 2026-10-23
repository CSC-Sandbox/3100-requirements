package edu.calpoly.csc3100.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** Receives instructor challenges and acknowledges those for this student. */
public final class Subscriber implements MqttCallback {
    private static final String BROKER = "tcp://broker.hivemq.com:1883";
    private static final int QOS = 1;
    private final String prefix;
    private final String studentId;
    private final Publisher publisher;
    private MqttClient client;

    public Subscriber(String prefix, String studentId, Publisher publisher) {
        this.prefix = prefix;
        this.studentId = studentId;
        this.publisher = publisher;
    }

    public void connect() throws MqttException {
        // TODO 5: Create an MqttClient with its OWN unique ID and MemoryPersistence.
        // TODO 6: Register this instance as callback: client.setCallback(this).
        // TODO 7: Connect with a clean session.
        throw new UnsupportedOperationException("TODO: Subscriber.connect");
    }

    public void subscribe() throws MqttException {
        // TODO 8: Subscribe to prefix + "challenge" with QoS 1.
        throw new UnsupportedOperationException("TODO: Subscriber.subscribe");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
        System.out.println("Received: " + payload);
        // TODO 9: Parse the payload in the form "id:key".
        // TODO 10: Ignore challenges addressed to other students.
        // TODO 11: Call publisher.sendAck(key) for this student's challenge.
        // Remember: all students share the same challenge topic.
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("MQTT connection lost: " + cause);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Subscriber does not publish messages.
    }
}
