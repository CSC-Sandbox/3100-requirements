# 09-Message-Validator

A zero-dependency Java module that inspects, parses, and validates raw CSV and JSON sensor telemetry payloads.

## Features

- **Auto-Detection:** Automatically routes payloads to CSV or JSON processing logic based on input formatting.
- **Schema Validation:** Enforces required field counts and rejects malformed or unexpected parameters.
- **Range & Type Checks:** Verifies numeric conversions and validates values against specified physical bounds.
- **Zero Dependencies:** Built using standard Java collections (`java.util.*`).

## Supported Sensors

| Type | Fields / Keys | Validation Rules |
| :--- | :--- | :--- |
| **`GAZE`** | `x`, `y` | $0.0 \le x, y \le 1.0$ |
| **`ROBOT`** | `j1`, `j2`, `j3`, `j4`, `j5`, `j6`, `x`, `y`, `z` | Numeric `double` (9 values) |
| **`LIDAR`** | `x`, `y`, `z` | Numeric `double` (3 values) |
| **`AFFECT`** | `focus`, `excitement`, `engagement`, `interest`, `stress` | $0.0 \le \text{metric} \le 1.0$ (5 values) |

## Return Messages

- **`VALID`**: Payload passed all structural, type, and range checks.
- **`INVALID (Format)`**: Null, empty, syntax errors, or non-numeric values.
- **`INVALID (Missing value)`**: Mandatory fields or values are missing.
- **`INVALID (Altering value)`**: Extra unexpected parameters detected (CSV payloads).
- **`INVALID (Range)`**: Numeric values fall outside allowed domain boundaries.
- **`INVALID (Unknown Type)`**: Unrecognized sensor header or JSON `"type"` value.

## Summary

The `MessageValidator` class parses and validates raw CSV and JSON telemetry payloads for sensor types like `GAZE`, `ROBOT`, `LIDAR`, and `AFFECT`. It automatically detects input formats, enforces exact field counts, checks numeric types, and verifies range bounds like `GAZE` coordinates ($0.0 \le x, y \le 1.0$) and `AFFECT` metrics ($0.0 \le \text{metric} \le 1.0$ across focus, excitement, engagement, interest, and stress).

Input strings passed to `MessageValidator.validate` return diagnostic statuses such as `VALID`, `INVALID (Format)`, `INVALID (Missing value)`, `INVALID (Altering value)`, `INVALID (Range)`, or `INVALID (Unknown Type)`. JSON parsing is handled internally using standard Java maps without external dependencies.

## Quick Start

```java
import edu.calpoly.provided.MessageValidator;

public class Main {
    public static void main(String[] args) {
        MessageValidator validator = new MessageValidator();

        // Valid CSV AFFECT payload (header + 5 metrics)
        validator.validate("AFFECT, 0.8, 0.9, 0.7, 0.6, 0.2"); // Returns: "VALID"

        // Valid JSON ROBOT payload (9 joint & coordinate fields)
        validator.validate("{\"type\":\"ROBOT\", \"j1\":\"0\", \"j2\":\"0\", \"j3\":\"0\", \"j4\":\"0\", \"j5\":\"0\", \"j6\":\"0\", \"x\":\"10.5\", \"y\":\"5.0\", \"z\":\"2.0\"}"); // Returns: "VALID"

        // Out of Range GAZE payload
        validator.validate("GAZE, 1.5, 0.5"); // Returns: "INVALID (Range)"
    }
}
