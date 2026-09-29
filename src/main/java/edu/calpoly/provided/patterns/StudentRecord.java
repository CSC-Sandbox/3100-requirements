package edu.calpoly.provided.patterns;

public class StudentRecord {

    private final int id;
    private final String name;
    private final String major;
    private final int graduationYear;
    private final String email;

    private StudentRecord(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.major = builder.major;
        this.graduationYear = builder.graduationYear;
        this.email = builder.email;
    }

    @Override
    public String toString() {
        return id + " - " + name + " - " + major + " - " + graduationYear + " - " + email;
    }
}
