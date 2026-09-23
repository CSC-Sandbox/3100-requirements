package edu.calpoly.provided;

import java.util.HashMap;
import java.util.Map;

/**
 * MessageValidator - Core pipeline for inspecting incoming sensor/telemetry payloads.
 * Handles both plain CSV streams and JSON payloads, checking structural validity and range limits.
 */
public class MessageValidator {

    public String validate(String rawInput) {
        if (rawInput == null) return "INVALID (Format)";

        String input = rawInput.trim();
        if (input.isEmpty()) return "INVALID (Format)";

        if (input.startsWith("{") && input.endsWith("}")) {
            return validateJson(input);
        } else {
            return validateCsv(input);
        }
    }

    // CSV VALIDATION PIPELINE
    private String validateCsv(String input) {
        String[] tokens = input.split(",");
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }

        if (tokens.length == 0 || tokens[0].isEmpty()) return "INVALID (Format)";

        String type = tokens[0].toUpperCase();

        try {
            switch (type) {
                case "GAZE":
                    // Format: GAZE, X, Y -> 3 tokens total
                    if (tokens.length < 3) return "INVALID (Missing value)";
                    if (tokens.length > 3) return "INVALID (Altering value)";

                    double gazeX = Double.parseDouble(tokens[1]);
                    double gazeY = Double.parseDouble(tokens[2]);

                    // Both X and Y must be in the range [0.0, 1.0]
                    if (gazeX < 0.0 || gazeX > 1.0 || gazeY < 0.0 || gazeY > 1.0) {
                        return "INVALID (Range)";
                    }
                    return "VALID";

                case "ROBOT":
                    // Format: ROBOT, J1, J2, J3, J4, J5, J6, X, Y, Z -> 10 tokens total (header + 9 values)
                    if (tokens.length < 10) return "INVALID (Missing value)";
                    if (tokens.length > 10) return "INVALID (Altering value)";

                    for (int i = 1; i <= 9; i++) {
                        Double.parseDouble(tokens[i]);
                    }
                    return "VALID";

                case "AFFECT":
                    // Format: AFFECT, focus, excitement, engagement, interest, stress -> 6 tokens total
                    if (tokens.length < 6) return "INVALID (Missing value)";
                    if (tokens.length > 6) return "INVALID (Altering value)";

                    for (int i = 1; i <= 5; i++) {
                        double val = Double.parseDouble(tokens[i]);
                        if (val < 0.0 || val > 1.0) {
                            return "INVALID (Range)";
                        }
                    }
                    return "VALID";

                case "LIDAR":
                    // Format: LIDAR, X, Y, Z -> 4 tokens total
                    if (tokens.length < 4) return "INVALID (Missing value)";
                    if (tokens.length > 4) return "INVALID (Altering value)";

                    Double.parseDouble(tokens[1]);
                    Double.parseDouble(tokens[2]);
                    Double.parseDouble(tokens[3]);
                    return "VALID";

                default:
                    return "INVALID (Unknown Type)";
            }
        } catch (NumberFormatException e) {
            return "INVALID (Format)";
        }
    }

    // JSON VALIDATION PIPELINE
    private String validateJson(String input) {
        String unescaped = input.replace("\\\"", "\"");

        Map<String, String> jsonMap = parseSimpleJson(unescaped);
        if (jsonMap == null || !jsonMap.containsKey("type")) {
            return "INVALID (Format)";
        }

        String type = jsonMap.get("type").toUpperCase();

        try {
            switch (type) {
                case "GAZE":
                    if (!jsonMap.containsKey("x") || !jsonMap.containsKey("y")) {
                        return "INVALID (Missing value)";
                    }
                    double gazeX = Double.parseDouble(jsonMap.get("x"));
                    double gazeY = Double.parseDouble(jsonMap.get("y"));

                    if (gazeX < 0.0 || gazeX > 1.0 || gazeY < 0.0 || gazeY > 1.0) {
                        return "INVALID (Range)";
                    }
                    return "VALID";

                case "ROBOT":
                    String[] robotKeys = {"j1", "j2", "j3", "j4", "j5", "j6", "x", "y", "z"};
                    for (String key : robotKeys) {
                        if (!jsonMap.containsKey(key)) {
                            return "INVALID (Missing value)";
                        }
                        Double.parseDouble(jsonMap.get(key));
                    }
                    return "VALID";

                case "AFFECT":
                    String[] affectKeys = {"focus", "excitement", "engagement", "interest", "stress"};
                    for (String key : affectKeys) {
                        if (!jsonMap.containsKey(key)) {
                            return "INVALID (Missing value)";
                        }
                        double val = Double.parseDouble(jsonMap.get(key));
                        if (val < 0.0 || val > 1.0) {
                            return "INVALID (Range)";
                        }
                    }
                    return "VALID";

                case "LIDAR":
                    if (!jsonMap.containsKey("x") || !jsonMap.containsKey("y") || !jsonMap.containsKey("z")) {
                        return "INVALID (Missing value)";
                    }
                    Double.parseDouble(jsonMap.get("x"));
                    Double.parseDouble(jsonMap.get("y"));
                    Double.parseDouble(jsonMap.get("z"));
                    return "VALID";

                default:
                    return "INVALID (Unknown Type)";
            }
        } catch (NumberFormatException e) {
            return "INVALID (Format)";
        }
    }

    /**
     * Lightweight JSON string extractor. Splits key-value pairs without external dependencies.
     */
    private Map<String, String> parseSimpleJson(String json) {
        Map<String, String> map = new HashMap<>();

        String clean = json.replace("{", "").replace("}", "").replace("\"", "").trim();
        String[] pairs = clean.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
            }
        }
        return map;
    }
}