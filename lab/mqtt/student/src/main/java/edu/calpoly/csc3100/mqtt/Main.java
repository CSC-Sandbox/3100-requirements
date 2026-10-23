package edu.calpoly.csc3100.mqtt;

/** Starts the MQTT student exercise. */
public final class Main {
    private Main() { }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: mvn exec:java -Dexec.args=\"YOUR_ID SESSION\"");
            return;
        }
        String studentId = args[0];
        String session = args[1];
        if (!studentId.matches("[a-zA-Z0-9_-]+") ||
                !session.matches("[a-zA-Z0-9_-]{3,40}")) {
            System.out.println("Invalid student ID or session.");
            return;
        }
        String prefix = "csc3100/lab/" + session + "/";
        Publisher publisher = new Publisher(prefix, studentId);
        Subscriber subscriber = new Subscriber(prefix, studentId, publisher);

        publisher.connect();
        subscriber.connect();
        subscriber.subscribe(); // Subscribe BEFORE announcing readiness.
        publisher.sendReady();

        System.out.println("Waiting for MQTT challenges. Press Ctrl+C to stop.");
        Thread.currentThread().join();
    }
}
