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

    public static class Builder {
        private final int id;
        private final String name;
        private String major;
        private int graduationYear;
        private String email;

        public Builder(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Builder major(String major) {
            this.major = major;
            return this;
        }

        public Builder graduationYear(int graduationYear) {
            this.graduationYear = graduationYear;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public StudentRecord build() {
            return new StudentRecord(this);
        }
    }
}
