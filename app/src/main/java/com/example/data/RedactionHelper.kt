package com.example.data

object RedactionHelper {

    // Regex for typical 4 to 8 digit OTPs or PINs when accompanied by relevant context
    private val otpPattern = Regex("""(?i)\b(otp|code|pin|password|mpin)\s*[:=is-]*\s*([0-9]{4,8})\b""")
    private val standaloneDigitsPattern = Regex("""\b\d{6}\b""") // Common 6-digit OTPs
    // 16-digit card numbers with spaces or dashes
    private val cardNumberPattern = Regex("""\b(?:\d{4}[ -]?){3}\d{4}\b""")
    // Aadhaar 12-digit number pattern
    private val aadhaarPattern = Regex("""\b\d{4}\s\d{4}\s\d{4}\b""")

    fun redactSensitiveData(input: String): String {
        var redacted = input

        // Redact explicit OTP / PIN captures
        redacted = otpPattern.replace(redacted) { match ->
            val label = match.groupValues[1]
            "$label: [REDACTED_CONFIDENTIAL_CODE]"
        }

        // Redact full payment card numbers, leaving only last 4 digits
        redacted = cardNumberPattern.replace(redacted) { match ->
            val clean = match.value.replace(" ", "").replace("-", "")
            "XXXX-XXXX-XXXX-" + clean.takeLast(4)
        }

        // Redact Aadhaar patterns
        redacted = aadhaarPattern.replace(redacted) {
            "XXXX-XXXX-" + it.value.takeLast(4)
        }

        return redacted
    }

    fun containsSensitiveCredentials(input: String): Boolean {
        val lower = input.lowercase()
        return lower.contains("otp") ||
                lower.contains("upi pin") ||
                lower.contains("mpin") ||
                lower.contains("password") ||
                standaloneDigitsPattern.containsMatchIn(input) ||
                cardNumberPattern.containsMatchIn(input)
    }
}
