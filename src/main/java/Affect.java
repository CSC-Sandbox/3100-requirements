import java.security.InvalidParameterException;

public class Affect {
    private final double focus;
    private final double excitement;
    private final double engagement;

    private final double interest;
    private final double stress;

    public Affect(double focus, double excitement, double engagement, double interest, double stress) {
        this.focus = focus;
        this.excitement = excitement;
        this.engagement = engagement;
        this.interest = interest;
        this.stress = stress;
    }

    /**
     * Parse a new `Affect` from text in the format
     * "AFFECT,[focus],[excitement],[engagement],[interest],[stress]"
     */
    public static Affect fromString(String message) {
        String[] parts = message.split(",");

        if (parts.length != 6) {
            throw new InvalidParameterException("Expected 6 comma seperated values");
        }

        if (!parts[0].equals("AFFECT")) {
            throw new InvalidParameterException("Affect messages should begin with 'AFFECT'");
        }

        return new Affect(
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Double.parseDouble(parts[4]),
                Double.parseDouble(parts[5])
        );
    }

    @Override
    public String toString() {
        return String.format(
                "AFFECT,%f,%f,%f,%f,%f",
                this.focus,
                this.excitement,
                this.engagement,
                this.interest,
                this.stress
        );
    }

    public double getFocus() {
        return focus;
    }

    public double getExcitement() {
        return excitement;
    }

    public double getEngagement() {
        return engagement;
    }

    public double getInterest() {
        return interest;
    }

    public double getStress() {
        return stress;
    }
}
