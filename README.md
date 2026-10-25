# SURAKSHA AI — Citizen Scam Detection and Reporting Assistant

> **"Think Before You Click. Check Before You Pay."**  
> *A mobile-first cyber safety utility empowering Indian citizens to identify, simulate, and defend against digital fraud.*

[![Platform](https://img.shields.io/badge/Platform-Android%20(API%2024%2B)-3DDC84.svg?style=flat&logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202024.09.00-4285F4.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Tests](https://img.shields.io/badge/Tests-25%20Passing-brightgreen.svg)](tests/)

---

## 1. Project Overview & Hackathon Theme

**SURAKSHA AI** is a digital safety companion developed for the **Cyber Safety & Citizen Empowerment** hackathon theme. It addresses the escalating wave of financial cybercrimes, UPI payment traps, loan app extortion, fake job recruitment schemes, and digital arrest intimidation targeting citizens across India.

### The Problem
Millions of first-time digital users and vulnerable smartphone owners across India face sophisticated cyber manipulation daily. Victims often act under panic, artificial deadlines, or the illusion of quick wealth. Critical barriers include:
- Inability to recognize deceptive patterns (e.g., entering a UPI PIN to "receive" money).
- Reluctance to consult family due to shame or fear.
- Missing the critical **Golden Hour** (the first 2 to 3 hours after a fraudulent transaction) to dial `1930` and freeze illicit fund transfers.
- Lack of simple, local language explanations.

### Intended Beneficiaries
- **Everyday Citizens & Senior Citizens**: Straightforward, jargon-free threat explanations with explicit safe actions in English, Hindi (हिन्दी), and Assamese (অসমীয়া).
- **Students & Job Seekers**: Protection against advance-fee recruitment scams and fake work-from-home offers.
- **Micro-Merchants & Small Business Owners**: Defense against fraudulent UPI collect requests and QR code traps.

---

## 2. Feature Matrix: Completed vs. Planned

| Capability | Status | Implementation Details |
|---|---|---|
| **Heuristic Text Scam Checker** | ✅ **Completed** | Multi-signal detection analyzing credentials, advance fees, threats, urgency, and prizes. |
| **Scam Pattern Fingerprinting** | ✅ **Completed** | Identifies 5 compound scam archetypes with separated observed quotes and inferred possibilities. |
| **Phishing URL Inspector** | ✅ **Completed** | Structural analysis for suspicious TLDs (`.xyz`, `.ga`), IP addresses, and brand impersonation (`sbi`, `hdfc`, `paytm`). |
| **Scam Conversation Simulator** | ✅ **Completed** | Interactive branching chat engine with 3 scenarios (Fake Job, Bank OTP, UPI Trap), scoring (0–100), and badges. |
| **Screenshot Evidence Assistant** | ✅ **Completed** | Zero-permission photo picker, local text extraction review, chronological timeline builder, and checklist. |
| **Sensitive Data Redaction** | ✅ **Completed** | Local regex sanitizer masking OTPs, 16-digit payment card numbers, and Aadhaar numbers. |
| **Citizen Learning Center** | ✅ **Completed** | 8 structured cyber safety lessons with interactive quizzes and local progress tracking. |
| **Emergency Reporting Guide** | ✅ **Completed** | One-touch 1930 dialing guidance, Golden Hour recovery instructions, and direct links to `cybercrime.gov.in`. |
| **Multilingual Support** | ✅ **Completed** | Trilingual interface (English, Hindi, Assamese) for core navigation, lessons, and scam verdicts. |
| **Local Offline Persistence** | ✅ **Completed** | Device-private `SharedPreferences` storing completed lessons, simulation achievements, and language choice. |
| **SURAKSHA INTEL Dashboard** | ⚠️ **Simulated Demo** | Standalone administrative demonstration dashboard running on simulated mock data. **Not connected to the live mobile app.** |
| **Server-Side AI Inference** | ⏳ **Planned** | Opt-in backend Gemini AI analysis for dialectal idioms (architecture ready via Secrets plugin). |
| **Automated FIR / Police Filing** | ❌ **Out of Scope** | By design: citizens are guided to official portals; the app never claims to file police reports automatically. |

---

## 3. Screenshots

*(Screenshots can be captured directly via the Android Streaming Emulator or local Studio device runner)*

| Home Safety Radar | Scam Pattern Fingerprint | Scam Simulator |
|:---:|:---:|:---:|
| ![Home Dashboard](docs/screenshots/home_screen.png)<br>*(Placeholder)* | ![Pattern Fingerprint](docs/screenshots/pattern_fingerprint.png)<br>*(Placeholder)* | ![Simulator](docs/screenshots/scam_simulator.png)<br>*(Placeholder)* |

| Evidence Timeline | Cyber Literacy Lessons | Emergency Reporting |
|:---:|:---:|:---:|
| ![Evidence Timeline](docs/screenshots/evidence_assistant.png)<br>*(Placeholder)* | ![Learning Center](docs/screenshots/lessons_screen.png)<br>*(Placeholder)* | ![Emergency Help](docs/screenshots/report_help.png)<br>*(Placeholder)* |

---

## 4. Technology Stack & Architecture

- **Operating System**: Android (minSdk: `24` / Android 7.0 Nougat, targetSdk: `36`)
- **Programming Language**: Kotlin `2.2.10`
- **UI Framework**: Jetpack Compose (`androidx.compose.bom:2024.09.00`) with Material 3 (`androidx.compose.material3:1.3.1`)
- **Build System**: Gradle 9.1.1 (Kotlin DSL — `build.gradle.kts`)
- **Asynchronous Flow**: Kotlin Coroutines `1.10.2` & `StateFlow`
- **Local Persistence**: Android `SharedPreferences` via `UserPreferencesRepository.kt`
- **Secrets Management**: Secrets Gradle Plugin (`com.google.android.libraries.mapsplatform.secrets-gradle-plugin:2.0.1`) with `.env` / `.env.example`
- **Testing**: JUnit 4 (`4.13.2`), Robolectric (`4.16.1`), AndroidX Test Core (`1.6.1`)

*For detailed architectural flowcharts and data flow diagrams, see [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).*

---

## 5. Verified Prerequisites & Environment

Before building locally, verify your development environment:
- **Java Development Kit (JDK)**: JDK 17 or JDK 21 (JDK 17 recommended)
- **Android SDK Platform**: API Level 36 (`compileSdk = 36`)
- **Android SDK Build-Tools**: 34.0.0 or higher
- **Gradle**: Gradle 8.11+ / AGP 9.1.1 compatible runner
- **Operating System**: Linux, macOS, or Windows with Android Studio Ladybug / Meerkat

---

## 6. Step-by-Step Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/suraksha-ai.git
cd suraksha-ai
```

### 2. Configure Environment Variables
Copy `.env.example` to `.env` if configuring optional keys:
```bash
cp .env.example .env
```
*(The core application does not require any active API keys to run; all rule matching and simulation logic execute locally on-device).*

### 3. Open in Android Studio
1. Launch Android Studio.
2. Select **Open** and choose the `suraksha-ai` root folder.
3. Allow Gradle to synchronize project dependencies automatically.

---

## 7. Verified Build & Development Commands

Use standard `gradle` commands from the repository root:

| Action | Command | Purpose |
|---|---|---|
| **Assemble Debug APK** | `gradle assembleDebug` | Compiles and builds the debug APK in `app/build/outputs/apk/debug/app-debug.apk`. |
| **Run All Unit Tests** | `gradle :app:testDebugUnitTest` | Executes the complete 25-test suite (Robolectric + Evaluation Suite). |
| **Run Specific Test Suite** | `gradle :app:testDebugUnitTest --tests "com.example.ScamEvaluationTestSuite"` | Runs only the 9 scam evaluation test cases. |
| **Build Release Bundle (AAB)** | `gradle bundleRelease` | Generates the signed Android App Bundle for Google Play distribution. |
| **Check Lint & Dependencies** | `gradle check` | Validates source code formatting and static analysis rules. |

> **Note on Gradle Wrapper**: In certain cloud environments, invoke `gradle <task>` directly. On local machines with the wrapper installed, `./gradlew <task>` can also be used.

---

## 8. Database Setup

**Database Status**: **No external database setup required.**
- The citizen application intentionally avoids requiring cloud databases, server backends, or SQL server provisioning.
- User settings (language, completed lessons, simulator badges) are stored locally in the app's sandboxed `SharedPreferences`.
- While Room database dependencies (`androidx.room:room-runtime:2.7.0`) are pre-declared in Gradle configuration for future extensions, no active SQLite database tables are currently provisioned.

---

## 9. Accessing Project Components

### 1. Main Citizen Mobile Application
- **Device / Streaming Emulator**: Open the installed app from the launcher labeled **"Suraksha AI"**.
- **First Screen**: Opens to the Home Dashboard with quick access to the Scam Checker, Simulator, and Emergency Help.

### 2. Public Landing Page
- Represents the public discovery and web onboarding layer.
- *Status*: External web presentation layer; see hackathon submission links or project repository portal for deployment status.

### 3. SURAKSHA INTEL Admin Dashboard
- Represents the law enforcement / administrative threat monitoring demo.
- *Status*: Standalone administrative demonstration dashboard operating on simulated data.
- **Important**: SURAKSHA INTEL does **not** listen to live citizen data from this Android client.

---

## 10. Testing Instructions & Test Results

Detailed test case definitions are documented in [`tests/README.md`](tests/README.md) and [`tests/test-cases.json`](tests/test-cases.json).

### Run Test Suite
```bash
gradle :app:testDebugUnitTest
```

### Observed Results
- **Full Suite Status**: **25 / 25 Tests Passing (100%)**
- **Execution Time**: ~26 seconds on host JVM.
- **Covered Scenarios**:
  - `TC-JOB-001`: Fake job + Upfront fee + Urgency (`FP_ADVANCE_FEE_JOB`) — ✅ Passed
  - `TC-BANK-002`: Bank impersonation + Link + OTP (`FP_BANK_CREDENTIAL_HARVESTING`) — ✅ Passed
  - `TC-LOAN-003`: 0% loan + Upfront fee + Personal UPI (`FP_PREDATORY_LOAN_FEE`) — ✅ Passed
  - `TC-PRIZE-004`: Lucky draw + Tax fee (`FP_INVESTMENT_PRIZE_TRAP`) — ✅ Passed
  - `TC-LEGIT-005`: Delivery notice false-positive check (`FP_BENIGN_LEGITIMATE`) — ✅ Passed
  - `TC-INCONC-006`: Ambiguous conversation check (`FP_BENIGN_LEGITIMATE`) — ✅ Passed
  - `TC-INCONC-007`: Short input threshold (`FP_INCONCLUSIVE`) — ✅ Passed
  - `TC-EMPTY-008`: Blank input boundary check — ✅ Passed
  - `TC-URL-009`: Malicious `.xyz` bank URL analysis — ✅ Passed
  - Redaction tests: Masking of OTPs, credit cards, and Aadhaar numbers — ✅ Passed
  - Simulator tests: Scoring algorithms and badges — ✅ Passed

---

## 11. Security, Privacy & Safety Considerations

1. **Zero Data Harvesting**: The application does not collect, record, or transmit user-submitted messages, SMS excerpts, or screenshots.
2. **Local Regex Redaction**: `RedactionHelper.kt` strips OTPs, 16-digit card numbers, and Aadhaar numbers locally before evidence generation.
3. **Zero-Permission Image Picker**: Uses Android's modern photo picker (`PickVisualMedia`) without requesting broad storage permissions (`READ_EXTERNAL_STORAGE`).
4. **Offline Resilience**: Scam pattern checking and simulator modules function entirely without an internet connection.
5. **No False Promises**: The application explicitly informs users that it is an educational detection utility and does not file automatic police complaints.

---

## 12. Official Indian Cybercrime Reporting Resources

If you or someone you know has fallen victim to cyber fraud:

- 📞 **National Cyber Crime Helpline Number**: **`1930`** (Toll-Free, 24/7)
- 🌐 **National Cyber Crime Reporting Portal**: [https://cybercrime.gov.in](https://cybercrime.gov.in)
- ⏳ **The Golden Hour Rule**: Report financial fraud within the first **2 to 3 hours** to maximize the chance of freezing funds through the Citizen Financial Cyber Fraud Reporting and Management System (CFCFRMS).
- 🏦 **Bank Card Helplines**: Immediately dial the customer support number printed on the back of your physical debit or credit card to freeze your card and netbanking account.

---

## 13. License & Third-Party Asset Attribution

### Software License
This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for complete terms.

### Third-Party Software & Dependency Notice
The MIT License applies to the source code and documentation created by the project author. It does **not** automatically relicense third-party dependencies, libraries, fonts, or assets used by the project:
- **AndroidX & Jetpack Libraries**: Licensed under the Apache License, Version 2.0.
- **Kotlin Standard Library & Coroutines**: Licensed under the Apache License, Version 2.0.
- **Material Symbols & Icons**: Designed by Google and distributed under the Apache License, Version 2.0.
- **Image Loading (Coil)**: Licensed under the Apache License, Version 2.0.
- **Networking (Retrofit / OkHttp / Moshi)**: Licensed under the Apache License, Version 2.0.
- **Test Frameworks (JUnit / Robolectric)**: Licensed under Eclipse Public License 1.0 and MIT License.

### Third-Party Assets Requiring Verification
- **Branding Artwork & Logos**: The Suraksha AI custom shield emblem and graphical assets located in `app/src/main/res/drawable/` and `public/assets/` are project-specific design assets; verify redistribution terms prior to commercial reuse.
