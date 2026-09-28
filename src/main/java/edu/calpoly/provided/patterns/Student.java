package edu.calpoly.provided.patterns;

public class Student {

    public static void main(String[] args) {

        // Factory Pattern:
        // The student requests a computer without creating a concrete class directly.
        Computer computer = ComputerFactory.createComputer("laptop");
        computer.start();

        // Builder Pattern:
        // The student constructs a registrar record step by step.
        StudentRecord record = new StudentRecord.Builder(12345, "Alex Smith")
                .major("Computer Science")
                .graduationYear(2028)
                .email("asmith@university.edu")
                .build();

        System.out.println("Registrar record:");
        System.out.println(record);
    }
}
