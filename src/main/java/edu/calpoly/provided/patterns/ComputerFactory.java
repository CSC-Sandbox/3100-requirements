package edu.calpoly.provided.patterns;

public class ComputerFactory {

    public static Computer createComputer(String type) {
        if (type.equalsIgnoreCase("laptop")) {
            return new Laptop();
        }
        if (type.equalsIgnoreCase("desktop")) {
            return new Desktop();
        }
        if (type.equalsIgnoreCase("server")) {
            return new Server();
        }
        throw new IllegalArgumentException("Unknown computer type: " + type);
    }
}
