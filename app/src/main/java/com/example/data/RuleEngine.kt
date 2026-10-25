package com.example.data

import java.util.Locale
import java.util.UUID

object RuleEngine {

    fun analyzeText(
        text: String,
        selectedCategory: ScamCategory? = null,
        language: AppLanguage = AppLanguage.EN
    ): AnalysisResult {
        val trimmed = text.trim()
        if (trimmed.length < 10) {
            val inconclusiveFingerprint = PatternEngine.detectFingerprint(text)
            return AnalysisResult(
                id = "AN-" + UUID.randomUUID().toString().take(8).uppercase(),
                riskLevel = RiskLevel.UNABLE_TO_ASSESS,
                summary = "The provided message is too brief to detect meaningful safety or fraud indicators.",
                warningSigns = emptyList(),
                whyItMatters = "Fraud assessment requires sufficient message context, instructions, links, or contact details.",
                recommendedActions = listOf(
                    "Paste the full SMS, email, or message body including any links or contact numbers.",
                    "Verify the sender's identity through official public channels before responding."
                ),
                safeVerificationSteps = listOf(
                    "Check official bank numbers printed on the back of your debit/credit card.",
                    "Visit official company websites directly by typing their web address in a browser."
                ),
                isUrlAnalysis = false,
                rawInput = text,
                patternFingerprint = inconclusiveFingerprint
            )
        }

        val lower = trimmed.lowercase(Locale.ROOT)
        val findings = mutableListOf<RuleFinding>()
        var severityScore = 0

        // 1. Requests for OTP, UPI PIN, password, recovery code
        val otpKeywords = listOf("otp", "one time password", "upi pin", "mpin", "atm pin", "password", "recovery code", "secret code", "verification code", "ओटीपी", "पिन")
        val matchedOtp = otpKeywords.firstOrNull { lower.contains(it) }
        if (matchedOtp != null) {
            findings.add(
                RuleFinding(
                    ruleName = "Sensitive Credential Request",
                    explanation = "Legitimate institutions and banks NEVER ask you to disclose OTPs, UPI PINs, or passwords. PINs are only entered by you on official payment screens to send money, never to receive refunds or verify accounts.",
                    matchedText = matchedOtp
                )
            )
            severityScore += 3
        }

        // 2. Advance fee, registration fee, security deposit
        val feeKeywords = listOf("registration fee", "processing fee", "security deposit", "advance fee", "pay rs", "send rs", "transfer rs", "clearance fee", "booking amount", "tax fee", "पंजीकरण शुल्क", "फीस")
        val matchedFee = feeKeywords.firstOrNull { lower.contains(it) }
        if (matchedFee != null) {
            findings.add(
                RuleFinding(
                    ruleName = "Advance Fee / Upfront Payment Demand",
                    explanation = "Demanding upfront fees for job offers, loan releases, or lottery prizes is a hallmark of advance-fee fraud. Genuine employers and lenders never charge registration or release fees.",
                    matchedText = matchedFee
                )
            )
            severityScore += 3
        }

        // 3. Threats of account suspension or arrest
        val threatKeywords = listOf("suspended", "blocked", "deactivated", "permanently closed", "police", "arrest", "cbi", "cyber cell", "electricity disconnected", "power cut tonight", "legal action", "बंद हो जाएगा", "ब्लॉक")
        val matchedThreat = threatKeywords.firstOrNull { lower.contains(it) }
        if (matchedThreat != null) {
            findings.add(
                RuleFinding(
                    ruleName = "Intimidation & Threat of Penalty",
                    explanation = "Scammers use fabricated threats of account deactivation, utility disconnection, or legal prosecution to create panic and disable rational judgment.",
                    matchedText = matchedThreat
                )
            )
            severityScore += 2
        }

        // 4. Urgency pressure words
        val urgencyKeywords = listOf("immediately", "urgent", "last chance", "expires today", "within 2 hours", "within 24 hours", "hurry", "right now", "limited time", "offer expires", "तुरंत")
        val matchedUrgency = urgencyKeywords.firstOrNull { lower.contains(it) }
        if (matchedUrgency != null) {
            findings.add(
                RuleFinding(
                    ruleName = "Artificial Urgency & Time Pressure",
                    explanation = "Imposing tight deadlines forces victims to act impulsively without verifying details with family, colleagues, or official helplines.",
                    matchedText = matchedUrgency
                )
            )
            severityScore += 1
        }

        // 5. Prize, reward, cashback, lottery claims
        val prizeKeywords = listOf("won rs", "lucky draw", "lottery", "cashback", "reward", "guaranteed return", "free gift", "selected for prize", "विजेता", "इनाम")
        val matchedPrize = prizeKeywords.firstOrNull { lower.contains(it) }
        if (matchedPrize != null) {
            findings.add(
                RuleFinding(
                    ruleName = "Unsolicited Prize or Reward Claim",
                    explanation = "Unexpected lottery winnings or astronomical investment returns are used to lure targets into paying fake 'processing' or 'customs clearance' charges.",
                    matchedText = matchedPrize
                )
            )
            severityScore += 2
        }

        // 6. Requests to install remote access apps
        val remoteKeywords = listOf("anydesk", "teamviewer", "quicksupport", "rustdesk", "airdroid", "screen share", "remote access", "support app")
        val matchedRemote = remoteKeywords.firstOrNull { lower.contains(it) }
        if (matchedRemote != null) {
            findings.add(
                RuleFinding(
                    ruleName = "Remote Screen-Access Application Request",
                    explanation = "Scammers trick victims into installing screen-sharing software like AnyDesk or TeamViewer to observe OTPs, copy credentials, and directly drain bank accounts.",
                    matchedText = matchedRemote
                )
            )
            severityScore += 3
        }

        // 7. Suspicious payment instruction keywords
        val paymentKeywords = listOf("send rs.1", "send rs 1", "upi://pay", "@fakebank", "scan qr", "collect request", "refund process", "receive money click")
        val matchedPayment = paymentKeywords.firstOrNull { lower.contains(it) }
        if (matchedPayment != null) {
            findings.add(
                RuleFinding(
                    ruleName = "Suspicious Payment Manipulation",
                    explanation = "Instructions to send nominal amounts (e.g. ₹1) or scan QR codes to 'receive' refunds actually trigger debit authorizations or expose UPI handles to automated exploit payloads.",
                    matchedText = matchedPayment
                )
            )
            severityScore += 3
        }

        // 8. Requests for sensitive identity or banking information
        val sensitiveDataKeywords = listOf("aadhaar", "pan card", "net banking", "cvv", "card number", "bank account number", "debit card", "आधार", "पैन")
        val matchedData = sensitiveDataKeywords.firstOrNull { lower.contains(it) }
        if (matchedData != null) {
            findings.add(
                RuleFinding(
                    ruleName = "Collection of Identity / Banking Credentials",
                    explanation = "Asking for Aadhaar, PAN, and card numbers via text channels enables identity theft, unauthorized SIM swaps, and fraudulent loan applications.",
                    matchedText = matchedData
                )
            )
            severityScore += 2
        }

        // Determine Risk Level
        val riskLevel = when {
            severityScore >= 3 || findings.size >= 2 -> RiskLevel.HIGH_CONCERN
            severityScore >= 1 || findings.isNotEmpty() -> RiskLevel.CAUTION
            else -> RiskLevel.LOW_CONCERN
        }

        val summary = when (riskLevel) {
            RiskLevel.HIGH_CONCERN -> "High likelihood of malicious intent detected. Strong warning signs match known digital financial fraud patterns."
            RiskLevel.CAUTION -> "Suspicious elements detected. Exercise caution and do not disclose sensitive details or transfer money."
            RiskLevel.LOW_CONCERN -> "No prominent scam patterns detected by the automated rule checks. However, remain vigilant."
            RiskLevel.UNABLE_TO_ASSESS -> "Insufficient content to assess risk."
        }

        val whyItMatters = when {
            findings.any { it.ruleName.contains("Remote Screen-Access") } ->
                "Remote access applications allow bad actors to view your phone screen in real time, capturing 2FA codes and bank passwords as you type them."
            findings.any { it.ruleName.contains("Advance Fee") } ->
                "Advance fee recruitment and fake loan schemes profit from thousands of small payments. Once money is sent, the contact disappears immediately."
            findings.any { it.ruleName.contains("Intimidation") } ->
                "Cyber fraudsters systematically simulate official authority to bypass your natural skepticism through fear and stress."
            else ->
                "Digital financial frauds in India frequently exploit urgency, unsolicited rewards, and deceptive payment steps to misdirect funds."
        }

        val recommendedActions = mutableListOf<String>()
        if (riskLevel == RiskLevel.HIGH_CONCERN) {
            recommendedActions.add("Do NOT share your OTP, UPI PIN, password, or grant remote access to anyone.")
        }
        recommendedActions.add("Do not click any embedded links or dial unverified telephone numbers provided in the message.")
        recommendedActions.add("Do not make any upfront advance transfers, registration deposits, or test transactions.")
        recommendedActions.add("Block the sender and report the contact on your messaging platform (e.g. WhatsApp / SMS).")
        if (riskLevel == RiskLevel.HIGH_CONCERN || riskLevel == RiskLevel.CAUTION) {
            recommendedActions.add("If you have already transferred money or entered credentials, call helpline 1930 immediately.")
        }

        val safeVerificationSteps = listOf(
            "Call the official bank phone number printed directly on the back of your physical debit or credit card.",
            "Log in to your account only through the official banking app downloaded from Google Play or by typing the exact web address yourself.",
            "Verify employment opportunities on the company's verified LinkedIn or official careers website.",
            "Check consumer alerts on the Indian Cyber Crime Reporting Portal (cybercrime.gov.in)."
        )

        val fingerprint = PatternEngine.detectFingerprint(trimmed)
        val finalRiskLevel = if (fingerprint.riskLevel == RiskLevel.HIGH_CONCERN && riskLevel != RiskLevel.HIGH_CONCERN) {
            RiskLevel.HIGH_CONCERN
        } else {
            riskLevel
        }

        return AnalysisResult(
            id = "AN-" + UUID.randomUUID().toString().take(8).uppercase(),
            riskLevel = finalRiskLevel,
            summary = summary,
            warningSigns = findings,
            whyItMatters = whyItMatters,
            recommendedActions = recommendedActions,
            safeVerificationSteps = safeVerificationSteps,
            isUrlAnalysis = false,
            rawInput = text,
            patternFingerprint = fingerprint
        )
    }

    fun analyzeUrl(url: String): AnalysisResult {
        val trimmed = url.trim()
        val findings = mutableListOf<RuleFinding>()
        var severityScore = 0

        val lower = trimmed.lowercase(Locale.ROOT)
        // Clean protocol for domain checking
        val noProtocol = lower.removePrefix("https://").removePrefix("http://")
        val domain = noProtocol.substringBefore("/").substringBefore("?").substringBefore(":")

        // 1. URL shorteners
        val shorteners = listOf("bit.ly", "tinyurl.com", "t.co", "goo.gl", "ow.ly", "is.gd", "buff.ly", "rb.gy", "cutt.ly")
        val isShortener = shorteners.any { domain == it || domain.endsWith(".$it") }
        if (isShortener) {
            findings.add(
                RuleFinding(
                    ruleName = "Masked Link / URL Shortener",
                    explanation = "Shortened URLs obscure the real destination server. Fraudsters frequently disguise malicious phishing forms behind shortened links.",
                    matchedText = domain
                )
            )
            severityScore += 2
        }

        // 2. IP address used instead of domain
        val ipPattern = Regex("""^(\d{1,3}\.){3}\d{1,3}""")
        if (ipPattern.containsMatchIn(domain)) {
            findings.add(
                RuleFinding(
                    ruleName = "Raw IP Address Host",
                    explanation = "Legitimate consumer websites use established brand domain names. Direct numeric IP addresses are frequently linked to illicit hosting and temporary phishing pages.",
                    matchedText = domain
                )
            )
            severityScore += 3
        }

        // 3. Suspicious high-risk TLDs
        val suspiciousTlds = listOf(".xyz", ".tk", ".ml", ".ga", ".cf", ".gq", ".top", ".buzz", ".loan", ".cc", ".icu", ".work", ".click")
        val matchedTld = suspiciousTlds.firstOrNull { domain.endsWith(it) }
        if (matchedTld != null) {
            findings.add(
                RuleFinding(
                    ruleName = "High-Risk Top-Level Domain (TLD)",
                    explanation = "The domain uses an extension ('$matchedTld') statistically favored in throwaway phishing attacks due to minimal identity verification and near-zero cost.",
                    matchedText = matchedTld
                )
            )
            severityScore += 2
        }

        // 4. Misleading brand impersonation & hyphenated brand patterns
        val brandNames = listOf("sbi", "hdfc", "icici", "pnb", "axis", "paytm", "phonepe", "gpay", "amazon", "flipkart", "uidai", "aadhaar", "incometax", "gov")
        val brandModifiers = listOf("loan", "offer", "reward", "cashback", "update", "kyc", "verify", "support", "secure", "login", "online", "portal", "refund")

        var matchedBrandCombo: String? = null
        for (b in brandNames) {
            for (m in brandModifiers) {
                if (domain.contains("$b-$m") || domain.contains("$m-$b") || domain.contains("$b$m") || domain.contains("$m$b")) {
                    matchedBrandCombo = "$b-$m"
                    break
                }
            }
            if (matchedBrandCombo != null) break
        }

        if (matchedBrandCombo != null) {
            findings.add(
                RuleFinding(
                    ruleName = "Brand Name Impersonation / Typo-squatting",
                    explanation = "The domain combines a trusted brand with security or incentive keywords (e.g. '$matchedBrandCombo'). Official banks never host customer portals on compound generic domains.",
                    matchedText = matchedBrandCombo
                )
            )
            severityScore += 3
        }

        // 5. Excessive subdomains (>3 levels)
        val subdomainCount = domain.count { it == '.' }
        if (subdomainCount >= 3) {
            findings.add(
                RuleFinding(
                    ruleName = "Excessive Subdomain Stacking",
                    explanation = "Deep subdomain levels (more than 3 levels) are often configured to push deceptive brand tokens into mobile browser address bars while the root domain belongs to an unrelated entity.",
                    matchedText = "$subdomainCount subdomain levels"
                )
            )
            severityScore += 2
        }

        // 6. Suspicious path keywords
        val suspiciousPaths = listOf("/verify", "/confirm", "/otp", "/login", "/secure", "/update-kyc", "/claim-prize", "/free-recharge", "/refund", "/netbanking")
        val path = "/" + noProtocol.substringAfter("/", "")
        val matchedPath = suspiciousPaths.firstOrNull { path.contains(it) }
        if (matchedPath != null) {
            findings.add(
                RuleFinding(
                    ruleName = "Sensitive Action Path Keyword",
                    explanation = "The URL path targets credential harvesting keywords ('$matchedPath') designed to mimic official verification and authentication endpoints.",
                    matchedText = matchedPath
                )
            )
            severityScore += 2
        }

        // 7. Hyphenated security words in domain
        val securityWords = listOf("bank", "gov", "official", "secure", "login", "verify")
        val matchedHyphenated = securityWords.firstOrNull { domain.contains("-$it") || domain.contains("$it-") }
        if (matchedHyphenated != null && matchedBrandCombo == null) {
            findings.add(
                RuleFinding(
                    ruleName = "Deceptive Security Keyword in Domain",
                    explanation = "The domain uses hyphenated security buzzwords ('$matchedHyphenated') to give an illusion of official legitimacy.",
                    matchedText = matchedHyphenated
                )
            )
            severityScore += 2
        }

        // Evaluate Risk Level
        val riskLevel = when {
            severityScore >= 3 || findings.size >= 2 -> RiskLevel.HIGH_CONCERN
            severityScore >= 1 || findings.isNotEmpty() -> RiskLevel.CAUTION
            else -> RiskLevel.LOW_CONCERN
        }

        val summary = when (riskLevel) {
            RiskLevel.HIGH_CONCERN -> "Significant phishing indicators detected in the link structure. Extreme caution advised."
            RiskLevel.CAUTION -> "Potentially deceptive or obfuscated link characteristics detected."
            RiskLevel.LOW_CONCERN -> "No prominent phishing patterns detected in this URL string."
            RiskLevel.UNABLE_TO_ASSESS -> "Unable to assess link format."
        }

        val whyItMatters = "Phishing websites replicate the appearance of legitimate banking, e-commerce, and government interfaces. Entering your netbanking password, OTP, or card number on a fake domain gives criminals immediate access to your accounts."

        val recommendedActions = mutableListOf<String>()
        if (riskLevel == RiskLevel.HIGH_CONCERN) {
            recommendedActions.add("Do NOT share your OTP, UPI PIN, password, or grant remote access to anyone.")
        }
        recommendedActions.add("Do not enter banking credentials, Aadhaar, PAN, or phone numbers on this webpage.")
        recommendedActions.add("Never download or install application files (.apk) offered by unverified web links.")
        recommendedActions.add("To access official services, search for the verified organization on Google or type the known domain directly.")

        val safeVerificationSteps = listOf(
            "Verify domain suffixes: Official Indian government portals strictly end in '.gov.in' or '.nic.in'.",
            "Official banking portals utilize verified root domains registered directly to the bank (e.g. sbi.co.in, hdfcbank.com, icicibank.com).",
            "Look for domain ownership credentials on the National Internet Exchange of India (NIXI / registry.in)."
        )

        return AnalysisResult(
            id = "URL-" + UUID.randomUUID().toString().take(8).uppercase(),
            riskLevel = riskLevel,
            summary = summary,
            warningSigns = findings,
            whyItMatters = whyItMatters,
            recommendedActions = recommendedActions,
            safeVerificationSteps = safeVerificationSteps,
            isUrlAnalysis = true,
            rawInput = url
        )
    }
}
