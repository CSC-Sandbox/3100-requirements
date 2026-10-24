package edu.calpoly.csc3100.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Publishes READY and ACK messages to the instructor.
 *
 * @author Marcus Hauen-Limkilde
 * @version 1.0
 */
public final class Publisher {
    private static final String BROKER = "tcp://broker.hivemq.com:1883";
    private static final int QOS = 1;
    private final String prefix;
    private final String studentId;
    private MqttClient client;

    public Publisher(String prefix, String studentId) {
        this.prefix = prefix;
        this.studentId = studentId;
    }

    public void connect() throws MqttException {
        client = new MqttClient(BROKER, "pub-" + studentId + "-" + UUID.randomUUID(), new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);
    }

    public void sendReady() throws MqttException {
        String message = studentId + ":READY";
        MqttMessage msg = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
        msg.setQos(QOS);
        msg.setRetained(false);
        client.publish(prefix + "ready", msg);
        System.out.println("Published READY: " + message);
    }

    public void sendAck(String key) throws MqttException {
        String message = studentId + ":ACK:" + key;
        MqttMessage msg = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
        msg.setQos(QOS);
        msg.setRetained(false);
        client.publish(prefix + "ack", msg);
        System.out.println("Published ACK: " + message);
    }
}
