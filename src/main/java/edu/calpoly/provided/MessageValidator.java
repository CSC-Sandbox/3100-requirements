package edu.calpoly.provided;

import java.util.HashMap;
import java.util.Map;

/**
 * MessageValidator - Core pipeline for inspecting incoming sensor/telemetry payloads.
 * Handles both plain CSV streams and JSON payloads, checking structural validity and range limits.
 */
public class MessageValidator {

    public String validate(String rawInput) {
        // Quick null guard
        if (rawInput == null) return "INVALID (Format)";

        // Clean up leading/trailing whitespace before parsing
        String input = rawInput.trim();
        if (input.isEmpty()) return "INVALID (Format)";

        // Format Detection: check if payload is wrapped as a JSON object
        if (input.startsWith("{") && input.endsWith("}")) {
            return validateJson(input);
        } else {
            return validateCsv(input);
        }
    }


    // CSV VALIDATION PIPELINE
    private String validateCsv(String input) {
        // Step 1: Tokenize by comma and trim each field
        String[] tokens = input.split(",");
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].trim();
        }

        if (tokens.length == 0 || tokens[0].isEmpty()) return "INVALID (Format)";

        // Extract the leading header type (e.g. GAZE, ROBOT, etc.)
        String type = tokens[0].toUpperCase();

        try {
            switch (type) {
                case "GAZE":
                    // Format: GAZE, X, Y -> Expecting exactly 3 tokens (header + 2 values)
                    if (tokens.length < 3) return "INVALID (Missing value)";
                    if (tokens.length > 3) return "INVALID (Altering value)"; // Extra/trailing parameters injected

                    double gazeX = Double.parseDouble(tokens[1]);
                    double gazeY = Double.parseDouble(tokens[2]);

                    // Whiteboard spec: 48 <= X <= 50 and 48 <= Y <= 50
                    if (gazeX < 48.0 || gazeX > 50.0 || gazeY < 48.0 || gazeY > 50.0) {
                        return "INVALID (Range)";
                    }
                    return "VALID";

                case "ROBOT":
                    // Format: ROBOT, X, Y, Z -> Expecting 4 tokens total
                    if (tokens.length < 4) return "INVALID (Missing value)";
                    if (tokens.length > 4) return "INVALID (Altering value)";

                    // Verify numeric conversions
                    Double.parseDouble(tokens[1]);
                    Double.parseDouble(tokens[2]);
                    Double.parseDouble(tokens[3]);
                    return "VALID";

                case "AFFECT":
                    // Format: AFFECT, Score -> Expecting 2 tokens total
                    if (tokens.length < 2) return "INVALID (Missing value)";
                    if (tokens.length > 2) return "INVALID (Altering value)";

                    double excitement = Double.parseDouble(tokens[1]);
                    // Emotion/excitement metric must fall within standard [0.0, 1.0] bound
                    if (excitement < 0.0 || excitement > 1.0) {
                        return "INVALID (Range)";
                    }
                    return "VALID";

                case "LIDAR":
                    // Format: LIDAR, X, Y, Z -> Expecting 4 tokens total
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
            // Fails if numeric string conversion hits unexpected non-digit characters
            return "INVALID (Format)";
        }
    }

    // JSON VALIDATION PIPELINE
    private String validateJson(String input) {
        // Strip escaped quotes (e.g. \"type\") to prevent parsing errors on raw payloads
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

                    // Boundary check for gaze orientation coordinates
                    if (gazeX < 48.0 || gazeX > 50.0 || gazeY < 48.0 || gazeY > 50.0) {
                        return "INVALID (Range)";
                    }
                    return "VALID";

                case "ROBOT":
                    if (!jsonMap.containsKey("x") || !jsonMap.containsKey("y") || !jsonMap.containsKey("z")) {
                        return "INVALID (Missing value)";
                    }
                    Double.parseDouble(jsonMap.get("x"));
                    Double.parseDouble(jsonMap.get("y"));
                    Double.parseDouble(jsonMap.get("z"));
                    return "VALID";

                case "AFFECT":
                    if (!jsonMap.containsKey("excitement")) {
                        return "INVALID (Missing value)";
                    }
                    double excitement = Double.parseDouble(jsonMap.get("excitement"));
                    if (excitement < 0.0 || excitement > 1.0) {
                        return "INVALID (Range)";
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
        
        // Strip structural syntax characters
        String clean = json.replace("{", "").replace("}", "").replace("\"", "").trim();
        String[] pairs = clean.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                map.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return map;
    }
}