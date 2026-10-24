package edu.calpoly.csc3100.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Receives instructor challenges and acknowledges those for this student.
 *
 * @author Marcus Hauen-Limkilde
 * @version 1.0
 */
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
        client = new MqttClient(BROKER, "sub-" + studentId + "-" + UUID.randomUUID(),   
            new MemoryPersistence());
        client.setCallback(this);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        client.connect(options);
    }

    public void subscribe() throws MqttException {
        client.subscribe(prefix + "challenge", QOS);
        System.out.println("Subscribed to " + prefix + "challenge");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
        System.out.println("Received: " + payload);

        String[] parts = payload.split(":", 2);
        if(parts.length != 2) { return;}
        String id = parts[0];
        String key = parts[1];

        if (!id.equals(studentId)) {
            return;
        }
        publisher.sendAck(key);
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
