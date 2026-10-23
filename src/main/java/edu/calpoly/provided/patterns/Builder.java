package edu.calpoly.provided.patterns;

public class Builder {
        final int id;
        final String name;
        String major;
        int graduationYear;
        String email;

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
