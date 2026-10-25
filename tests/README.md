# SURAKSHA AI — Test Suite Documentation

## Overview

This directory documents the testing framework, test cases, and evaluation results for **SURAKSHA AI (Citizen Scam Detection & Reporting Assistant)**. 

The testing philosophy prioritizes:
1. **Safety and Privacy**: 100% fictional data — no real personal identifiable information (PII), live phishing URLs, real OTPs, or active bank credentials.
2. **Deterministic Verification**: Clear separation of **expected outcomes** from **observed execution results**. No test case is declared passed without empirical JVM test execution.
3. **Resilience to False Positives**: Evaluation includes benign transactional messages and ambiguous customer greetings to ensure the engine does not cry wolf.
4. **Resilience to Edge Cases**: Evaluation covers empty strings, whitespace, and truncated snippets (<15 characters) to ensure zero crashes and truthful inconclusive indicators (`UNABLE_TO_ASSESS`).

---

## Repository Test Structure

```
tests/
├── README.md               # This testing guide and documentation
├── test-cases.json         # Structured machine-readable catalog of 9 evaluation cases
sample-data/
└── scam-examples.json      # Fictional scam scenarios and benign messages for demos
app/src/test/java/com/example/
├── ScamEvaluationTestSuite.kt # Automated JVM JUnit tests matching test-cases.json
├── ExampleRobolectricTest.kt # Robolectric tests covering components, preferences, and simulator
└── ExampleUnitTest.kt        # Baseline unit test check
```

---

## Test Execution

All automated tests run locally in the JVM using Gradle. No physical Android device or external emulator is required.

### Run All Unit and Robolectric Tests
```bash
gradle :app:testDebugUnitTest
```

### Run Only the Evaluation Test Suite
```bash
gradle :app:testDebugUnitTest --tests "com.example.ScamEvaluationTestSuite"
```

### Run with Detailed Standard Output / Stacktrace
```bash
gradle :app:testDebugUnitTest --info
```

---

## Test Cases Summary

| Case ID | Scenario | Input Type | Expected Risk | Observed Risk | Status |
|---|---|---|---|---|---|
| **TC-JOB-001** | Advance-fee fake job offer | Text Message | `HIGH_CONCERN` | `HIGH_CONCERN` | ✅ PASSED |
| **TC-BANK-002** | Bank KYC suspension & OTP harvesting | Text Message | `HIGH_CONCERN` | `HIGH_CONCERN` | ✅ PASSED |
| **TC-LOAN-003** | Predatory loan with advance fee to @upi | Text Message | `HIGH_CONCERN` | `HIGH_CONCERN` | ✅ PASSED |
| **TC-PRIZE-004** | Lucky draw with advance tax fee | Text Message | `HIGH_CONCERN` | `HIGH_CONCERN` | ✅ PASSED |
| **TC-LEGIT-005** | E-commerce delivery notification | Text Message | `LOW_CONCERN` | `LOW_CONCERN` | ✅ PASSED |
| **TC-INCONC-006** | Ambiguous callback request | Text Message | `LOW_CONCERN` | `LOW_CONCERN` | ✅ PASSED |
| **TC-INCONC-007** | Short snippet (<15 chars) | Text Message | `UNABLE_TO_ASSESS` | `UNABLE_TO_ASSESS` | ✅ PASSED |
| **TC-EMPTY-008** | Blank / whitespace string | Text Message | `UNABLE_TO_ASSESS` | `UNABLE_TO_ASSESS` | ✅ PASSED |
| **TC-URL-009** | Spoofed bank brand with `.xyz` TLD | URL / Domain | `HIGH_CONCERN` | `HIGH_CONCERN` | ✅ PASSED |

**Total Suite Statistics:**
- Total automated test cases in `ScamEvaluationTestSuite`: 9
- Total tests in full project unit test suite: 25
- Pass rate: 100% (25 / 25 passing)

---

## Test Case Specification Fields

Every case in `tests/test-cases.json` conforms to the following schema:
- `id`: Unique identifier (e.g., `TC-JOB-001`).
- `scenario`: Descriptive label of the scam scheme or benign circumstance.
- `intended_purpose`: What the test evaluates (pattern detection, edge case handling, false positive prevention).
- `fictional_input`: Synthetic text or URL supplied as input.
- `expected_warning_signs`: Expected red-flag rules that should trigger.
- `expected_safe_next_steps`: Plain-language citizen guidance expected in the output.
- `expected_result`: Expected risk tier and pattern match.
- `execution_status`: Whether the test was executed (`Executed` vs `Unexecuted`).
- `observed_result`: Verified output produced during JVM test execution.

---

## Automated Test Harness Details

- **Robolectric Configuration**: Configured in `app/build.gradle.kts` with `isIncludeAndroidResources = true` targeting Android SDK 36.
- **Engine Isolation**: `RuleEngine.kt` and `PatternEngine.kt` are pure Kotlin singleton objects with no Android framework dependencies, making them fast and deterministic to execute.
- **Regex Redaction Testing**: Sanitization rules in `RedactionHelper.kt` are tested against confidential OTPs, 16-digit card numbers, and 12-digit Aadhaar patterns to guarantee zero data leakage.
