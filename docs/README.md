# 09-Message-Validator

A zero-dependency Java module that inspects, parses, and validates raw CSV and JSON sensor telemetry payloads.

## Features

- **Auto-Detection:** Automatically detects CSV vs. JSON format based on payload syntax.
- **Schema Validation:** Verifies required fields and rejects unexpected parameters.
- **Range & Type Checks:** Enforces numeric types and valid value bounds.
- **Zero Dependencies:** Built entirely using standard Java utility collections (`java.util.*`).

## Supported Sensors

| Type | Fields / Keys | Validation Rules |
| :--- | :--- | :--- |
| **`GAZE`** | `x`, `y` | $48.0 \le x, y \le 50.0$ |
| **`ROBOT`** | `x`, `y`, `z` | Numeric `double` |
| **`LIDAR`** | `x`, `y`, `z` | Numeric `double` |
| **`AFFECT`** | `excitement` | $0.0 \le excitement \le 1.0$ |

## Return Messages

- **`VALID`**: Payload passed all checks.
- **`INVALID (Format)`**: Null, empty, malformed syntax, or non-numeric values.
- **`INVALID (Missing value)`**: Missing required fields or parameters.
- **`INVALID (Altering value)`**: Extra unexpected parameters detected (CSV payloads).
- **`INVALID (Range)`**: Numeric values fall outside allowed boundaries.
- **`INVALID (Unknown Type)`**: Unrecognized sensor header or JSON `"type"` value.
