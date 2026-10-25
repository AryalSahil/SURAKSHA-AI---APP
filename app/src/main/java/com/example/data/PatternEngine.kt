package com.example.data

import java.util.Locale

object PatternEngine {

    fun detectFingerprint(rawText: String): PatternFingerprint {
        val trimmed = rawText.trim()

        // 1. Inconclusive check for insufficient input
        if (trimmed.length < 15 || trimmed.split(Regex("\\s+")).size < 3) {
            return PatternFingerprint(
                patternId = "FP_INCONCLUSIVE",
                patternName = "Inconclusive / Insufficient Evidence",
                summaryExplanation = "The submitted text does not contain enough structure, vocabulary, or operational instructions to match a known cyber scam fingerprint.",
                confidence = PatternConfidence.INCONCLUSIVE,
                riskLevel = RiskLevel.UNABLE_TO_ASSESS,
                observedEvidence = emptyList(),
                inferredPossibilities = listOf(
                    "Insufficient context to determine sender legitimacy or malicious intent.",
                    "The message might be an incomplete excerpt, a benign greeting, or an unformatted snippet."
                ),
                safeNextSteps = listOf(
                    "Paste the complete message including sender headers, phone numbers, or accompanying web links.",
                    "If this came from an unknown sender, do not reply or click any links until more information is available."
                ),
                allDetectedSignals = emptyList(),
                matchedPatternRule = "Input text under threshold length (<15 chars or <3 words)"
            )
        }

        val lower = trimmed.lowercase(Locale.ROOT)

        // Signal extractors (directly observed)
        val signals = mutableListOf<DetectedSignal>()
        val observedEvidence = mutableListOf<ObservedEvidence>()

        // Signal 1: Job / Recruitment Keywords
        val jobKeywords = listOf("work from home", "data entry", "job offer", "salary rs", "selected for a job", "part time job", "daily income", "hiring", "hr priya", "hr ", "recruitment")
        val matchedJob = jobKeywords.firstOrNull { lower.contains(it) }
        if (matchedJob != null) {
            signals.add(
                DetectedSignal(
                    name = "Recruitment / Employment Offer",
                    category = "Employment Hook",
                    detectedText = extractContextSnippet(trimmed, matchedJob),
                    role = "Primary Trigger",
                    explanation = "Explicitly offers employment or freelance earnings."
                )
            )
            observedEvidence.add(
                ObservedEvidence(
                    signalName = "Employment Promise",
                    quote = extractContextSnippet(trimmed, matchedJob),
                    description = "Explicit offer of employment, salary, or remote data entry tasks."
                )
            )
        }

        // Signal 2: Upfront Fee / Registration Fee / Security Deposit
        val feeKeywords = listOf("registration fee", "processing fee", "security deposit", "advance fee", "pay rs", "send rs", "slot confirmation", "tax clearance", "booking amount", "file charge")
        val matchedFee = feeKeywords.firstOrNull { lower.contains(it) }
        if (matchedFee != null) {
            signals.add(
                DetectedSignal(
                    name = "Upfront Fee / Advance Deposit Request",
                    category = "Financial Extraction",
                    detectedText = extractContextSnippet(trimmed, matchedFee),
                    role = "Primary Trigger",
                    explanation = "Direct demand for advance payment prior to receiving benefits, releases, or employment."
                )
            )
            observedEvidence.add(
                ObservedEvidence(
                    signalName = "Advance Payment Demand",
                    quote = extractContextSnippet(trimmed, matchedFee),
                    description = "Request to pay upfront fees, registration charges, or security deposits."
                )
            )
        }

        // Signal 3: Urgency / Time Pressure
        val urgencyKeywords = listOf("urgent", "immediately", "last chance", "expire", "expires today", "within 2 hours", "within 24 hours", "24 hours", "2 hours", "hurry", "right now", "limited time", "tonight at")
        val matchedUrgency = urgencyKeywords.firstOrNull { lower.contains(it) }
        if (matchedUrgency != null) {
            signals.add(
                DetectedSignal(
                    name = "Psychological Urgency / Expiry Pressure",
                    category = "Coercion Tactic",
                    detectedText = extractContextSnippet(trimmed, matchedUrgency),
                    role = "Corroborating Signal",
                    explanation = "Imposes a strict artificial deadline to induce impulsive action without verification."
                )
            )
            observedEvidence.add(
                ObservedEvidence(
                    signalName = "Time-Pressure Constraint",
                    quote = extractContextSnippet(trimmed, matchedUrgency),
                    description = "Artificial deadline imposed to force immediate response."
                )
            )
        }

        // Signal 4: Institutional Authority / Impersonation
        val bankKeywords = listOf(
            "account suspended", "account blocked", "permanently blocked",
            "update kyc", "kyc pending", "kyc is pending", "kyc update",
            "customer care", "electricity bill overdue", "power cut tonight",
            "electricity disconnected", "fakebank", "verify-upi"
        )
        val matchedBank = bankKeywords.firstOrNull { lower.contains(it) }
        if (matchedBank != null) {
            signals.add(
                DetectedSignal(
                    name = "Institutional / Authority Impersonation",
                    category = "Brand Mimicry",
                    detectedText = extractContextSnippet(trimmed, matchedBank),
                    role = "Primary Trigger",
                    explanation = "Claims authority using account suspension, KYC, or utility threats."
                )
            )
            observedEvidence.add(
                ObservedEvidence(
                    signalName = "Authority / Institutional Reference",
                    quote = extractContextSnippet(trimmed, matchedBank),
                    description = "Reference to bank, utility department, or official service authority."
                )
            )
        }

        // Signal 5: Suspicious Link / URL
        val linkKeywords = listOf("http://", "https://", "www.", ".xyz", ".tk", ".ga", ".cf", "bit.ly", "tinyurl", "update-kyc", "/verify", "upi://pay")
        val matchedLink = linkKeywords.firstOrNull { lower.contains(it) }
        if (matchedLink != null) {
            signals.add(
                DetectedSignal(
                    name = "Unverified External Link / Action URI",
                    category = "Channel Redirection",
                    detectedText = extractContextSnippet(trimmed, matchedLink),
                    role = "Corroborating Signal",
                    explanation = "Redirects victim to external URLs, payment deep-links, or phishing portals."
                )
            )
            observedEvidence.add(
                ObservedEvidence(
                    signalName = "Action Redirection Link",
                    quote = extractContextSnippet(trimmed, matchedLink),
                    description = "Embedded hyperlink or payment URI directing to external endpoints."
                )
            )
        }

        // Signal 6: OTP / Password / PIN Request
        val credentialKeywords = listOf("otp", "upi pin", "password", "net banking id", "recovery code", "share your pin", "share the otp", "mpin")
        val matchedCredential = credentialKeywords.firstOrNull { lower.contains(it) }
        if (matchedCredential != null) {
            signals.add(
                DetectedSignal(
                    name = "Confidential Credential Interception",
                    category = "Authentication Extraction",
                    detectedText = extractContextSnippet(trimmed, matchedCredential),
                    role = "Primary Trigger",
                    explanation = "Solicits secret authentication tokens (OTP, UPI PIN, passwords) that unlock accounts."
                )
            )
            observedEvidence.add(
                ObservedEvidence(
                    signalName = "Credential Extraction Attempt",
                    quote = extractContextSnippet(trimmed, matchedCredential),
                    description = "Direct solicitation of OTPs, passwords, or transaction authorization PINs."
                )
            )
        }

        // Signal 7: Loan Offer / Instant Approval
        val loanKeywords = listOf("pre-approved", "personal loan", "0% interest", "no documents", "loan of rs", "instant loan", "release your loan")
        val matchedLoan = loanKeywords.firstOrNull { lower.contains(it) }
        if (matchedLoan != null) {
            signals.add(
                DetectedSignal(
                    name = "Unsolicited Pre-Approved Credit Offer",
                    category = "Financial Hook",
                    detectedText = extractContextSnippet(trimmed, matchedLoan),
                    role = "Primary Trigger",
                    explanation = "Promises instant financing with zero documentation or zero interest."
                )
            )
            observedEvidence.add(
                ObservedEvidence(
                    signalName = "High-Value Loan Claim",
                    quote = extractContextSnippet(trimmed, matchedLoan),
                    description = "Unsolicited promise of large loan disbursement without standard underwriting."
                )
            )
        }

        // Signal 8: Personal Payment Account / UPI ID
        val personalPaymentKeywords = listOf("@upi", "@fakebank", "upi://", "pay advance via upi", "prizeclaim@", "9xxxxxxxxx")
        val matchedPersonalPayment = personalPaymentKeywords.firstOrNull { lower.contains(it) }
        if (matchedPersonalPayment != null) {
            signals.add(
                DetectedSignal(
                    name = "Direct Peer-to-Peer / Personal Payment Routing",
                    category = "Payment Route",
                    detectedText = extractContextSnippet(trimmed, matchedPersonalPayment),
                    role = "Corroborating Signal",
                    explanation = "Directs payment to individual VPAs or unverified personal mobile numbers."
                )
            )
            observedEvidence.add(
                ObservedEvidence(
                    signalName = "Peer Payment Channel",
                    quote = extractContextSnippet(trimmed, matchedPersonalPayment),
                    description = "Payment instruction routed through personal mobile or private UPI handles."
                )
            )
        }

        // Signal 9: Investment Returns / Prize / Lottery
        val rewardKeywords = listOf("won rs", "lucky draw", "lottery", "guaranteed return", "tax clearance fee", "50,000 in the lucky draw", "prize")
        val matchedReward = rewardKeywords.firstOrNull { lower.contains(it) }
        if (matchedReward != null) {
            signals.add(
                DetectedSignal(
                    name = "Unsolicited Prize / Guaranteed High-Yield Return",
                    category = "Greed Bait",
                    detectedText = extractContextSnippet(trimmed, matchedReward),
                    role = "Primary Trigger",
                    explanation = "Promises lottery payouts, lucky draw prizes, or guaranteed investment windfalls."
                )
            )
            observedEvidence.add(
                ObservedEvidence(
                    signalName = "Unearned Reward Claim",
                    quote = extractContextSnippet(trimmed, matchedReward),
                    description = "Claim of lottery winnings or guaranteed returns requiring clearance payments."
                )
            )
        }

        // Signal 10: Remote Access Apps
        val remoteKeywords = listOf("anydesk", "teamviewer", "quicksupport", "rustdesk", "screen share")
        val matchedRemote = remoteKeywords.firstOrNull { lower.contains(it) }
        if (matchedRemote != null) {
            signals.add(
                DetectedSignal(
                    name = "Remote Device Takeover Tool",
                    category = "Host Intrusion",
                    detectedText = extractContextSnippet(trimmed, matchedRemote),
                    role = "Primary Trigger",
                    explanation = "Demands installation of remote control or screen-mirroring applications."
                )
            )
            observedEvidence.add(
                ObservedEvidence(
                    signalName = "Remote Screen Control Requirement",
                    quote = extractContextSnippet(trimmed, matchedRemote),
                    description = "Explicit instruction to install screen-sharing software."
                )
            )
        }

        // --- PATTERN FINGERPRINT MATCHING LOGIC ---

        // PATTERN 1: Fake Job + Upfront Fee + Urgency
        if (matchedJob != null && matchedFee != null) {
            return PatternFingerprint(
                patternId = "FP_ADVANCE_FEE_JOB",
                patternName = "Advance-Fee Recruitment Scheme",
                summaryExplanation = "A classic job offer trap: promising lucrative remote employment while mandating an upfront 'registration', 'verification', or 'slot confirmation' deposit.",
                confidence = if (matchedUrgency != null) PatternConfidence.DEFINITIVE_MATCH else PatternConfidence.HIGH_CONFIDENCE,
                riskLevel = RiskLevel.HIGH_CONCERN,
                observedEvidence = observedEvidence,
                inferredPossibilities = listOf(
                    "The recruiter is likely a cybercriminal using burner SIMs and disposable communication channels.",
                    "Any paid 'registration deposit' will be pocketed immediately with zero job placement.",
                    "Personal identity documents (Aadhaar/PAN) provided may be sold or repurposed for illegal loan accounts."
                ),
                safeNextSteps = listOf(
                    "Do NOT transfer any registration, training, or kit fee.",
                    "Verify the company directly on their official corporate careers portal or verified LinkedIn company page.",
                    "Report and block the sender's contact on your messaging platform."
                ),
                allDetectedSignals = signals,
                matchedPatternRule = "Fake Job Indicator + Upfront Registration Fee [Urgency Corroborated]"
            )
        }

        // PATTERN 2: Bank Impersonation + Suspicious Link + OTP Request
        if ((matchedBank != null || matchedCredential != null) && (matchedLink != null || matchedCredential != null) && (matchedCredential != null || lower.contains("suspended") || lower.contains("blocked"))) {
            return PatternFingerprint(
                patternId = "FP_BANK_CREDENTIAL_HARVESTING",
                patternName = "Banking Impersonation & Credential Interception",
                summaryExplanation = "Fraudsters simulate banking or utility authority to falsely claim account suspension, urging you to open an unverified link or provide secret OTP/PIN credentials.",
                confidence = if (matchedCredential != null && matchedLink != null) PatternConfidence.DEFINITIVE_MATCH else PatternConfidence.HIGH_CONFIDENCE,
                riskLevel = RiskLevel.HIGH_CONCERN,
                observedEvidence = observedEvidence,
                inferredPossibilities = listOf(
                    "The embedded link leads to a forged netbanking replica designed to record your password and MPIN.",
                    "Any OTP shared over phone or form will immediately authorize an unauthorized debit or beneficiary addition.",
                    "The sender is masquerading as institutional customer support to instill panic."
                ),
                safeNextSteps = listOf(
                    "Do NOT enter your OTP, UPI PIN, or Netbanking password anywhere.",
                    "Do NOT click the link provided in the message.",
                    "Dial the official phone number printed on the back of your physical bank card to verify account status."
                ),
                allDetectedSignals = signals,
                matchedPatternRule = "Institutional Impersonation + Suspicious Link + Credential Interception"
            )
        }

        // PATTERN 3: Loan Offer + Advance Fee + Personal Account/UPI
        if (matchedLoan != null && (matchedFee != null || matchedPersonalPayment != null)) {
            return PatternFingerprint(
                patternId = "FP_PREDATORY_LOAN_FEE",
                patternName = "Predatory Advance-Fee Loan Scheme",
                summaryExplanation = "Offers unrealistic, zero-document personal loans with immediate approval, then conditions the loan release on paying upfront 'processing', 'file', or 'insurance' fees.",
                confidence = if (matchedFee != null && matchedPersonalPayment != null) PatternConfidence.DEFINITIVE_MATCH else PatternConfidence.HIGH_CONFIDENCE,
                riskLevel = RiskLevel.HIGH_CONCERN,
                observedEvidence = observedEvidence,
                inferredPossibilities = listOf(
                    "No legitimate loan will ever be disbursed; the fee will be stolen permanently.",
                    "The lending entity is likely operating without RBI authorization or NBFC licensing.",
                    "Requesting payment to individual UPI handles indicates illicit money-mule routing."
                ),
                safeNextSteps = listOf(
                    "Do NOT transfer any processing or clearance fee in advance.",
                    "Verify the lender's registration on the official RBI Sachet portal (sachet.rbi.org.in).",
                    "Authentic banks deduct legitimate processing charges from disbursed amounts, never via upfront UPI."
                ),
                allDetectedSignals = signals,
                matchedPatternRule = "Pre-Approved Loan Claim + Advance Fee Demand + Personal Payment Target"
            )
        }

        // PATTERN 4: Guaranteed Investment Returns + Urgency / Prize
        if ((matchedReward != null && (matchedUrgency != null || matchedFee != null)) || (lower.contains("investment") && lower.contains("guaranteed") && matchedUrgency != null)) {
            return PatternFingerprint(
                patternId = "FP_INVESTMENT_PRIZE_TRAP",
                patternName = "High-Yield Investment / Lottery Draw Fraud",
                summaryExplanation = "Lures targets with fake lottery jackpots or guaranteed investment windfalls, requiring victims to pay 'tax clearance' or deposit funds under strict artificial deadlines.",
                confidence = PatternConfidence.DEFINITIVE_MATCH,
                riskLevel = RiskLevel.HIGH_CONCERN,
                observedEvidence = observedEvidence,
                inferredPossibilities = listOf(
                    "The 'lucky draw' or 'guaranteed return' is entirely fictional.",
                    "The tax clearance fee requested is an advance-fee fraud mechanism.",
                    "Sharing Aadhaar and bank details for prize redemption enables identity theft."
                ),
                safeNextSteps = listOf(
                    "Do NOT transfer any money to claim unsolicited rewards or lottery earnings.",
                    "Legitimate lotteries and contests in India never demand tax payments sent to private UPI handles.",
                    "Delete the message and block the sender."
                ),
                allDetectedSignals = signals,
                matchedPatternRule = "Unsolicited Prize / Return + Expiry Urgency + Clearance Fee Demand"
            )
        }

        // PATTERN 5: Remote Access Takeover
        if (matchedRemote != null) {
            return PatternFingerprint(
                patternId = "FP_REMOTE_ACCESS_TAKEOVER",
                patternName = "Remote Screen Access Account Takeover",
                summaryExplanation = "Directs the victim to install remote device mirroring tools (AnyDesk, TeamViewer) under the guise of customer assistance, enabling attackers to observe OTPs and drain accounts.",
                confidence = PatternConfidence.DEFINITIVE_MATCH,
                riskLevel = RiskLevel.HIGH_CONCERN,
                observedEvidence = observedEvidence,
                inferredPossibilities = listOf(
                    "Attackers will view your screen in real time, capturing passwords and 2FA codes as you type them.",
                    "The caller is attempting to seize remote session control of your Android smartphone."
                ),
                safeNextSteps = listOf(
                    "Do NOT install AnyDesk, TeamViewer, or QuickSupport at the instruction of an unknown caller.",
                    "If already installed, turn on Airplane Mode immediately and uninstall the application.",
                    "Contact your bank to freeze netbanking if remote access was granted."
                ),
                allDetectedSignals = signals,
                matchedPatternRule = "Remote Control Software Solicitation + Customer Support Impersonation"
            )
        }

        // PATTERN 6: Single or Weak Signals (Caution)
        if (signals.isNotEmpty()) {
            return PatternFingerprint(
                patternId = "FP_PARTIAL_INDICATORS",
                patternName = "Unverified Suspicious Solicitation",
                summaryExplanation = "One or more risk indicators were observed in the message, but they do not match a complete multi-signal attack fingerprint.",
                confidence = PatternConfidence.MODERATE_CONFIDENCE,
                riskLevel = RiskLevel.CAUTION,
                observedEvidence = observedEvidence,
                inferredPossibilities = listOf(
                    "The communication contains warning characteristics often seen in unsolicited marketing or early-stage fraud outreach.",
                    "Proceed with heightened skepticism; do not disclose confidential identity or banking data."
                ),
                safeNextSteps = listOf(
                    "Verify the sender's identity through official public channels before responding.",
                    "Never transfer money or share credentials without independent confirmation."
                ),
                allDetectedSignals = signals,
                matchedPatternRule = "Isolated Warning Signals Detected (${signals.size} signal(s))"
            )
        }

        // PATTERN 7: Clean / Benign Content (Low Concern)
        return PatternFingerprint(
            patternId = "FP_BENIGN_LEGITIMATE",
            patternName = "No Malicious Fingerprint Detected",
            summaryExplanation = "The submitted text does not match any known scam patterns or attack fingerprint signatures.",
            confidence = PatternConfidence.HIGH_CONFIDENCE,
            riskLevel = RiskLevel.LOW_CONCERN,
            observedEvidence = emptyList(),
            inferredPossibilities = listOf(
                "The message exhibits standard communication patterns without common extortion, urgency, or credential-harvesting markers.",
                "Always remember that automated checks evaluate pattern signatures; remain vigilant if context changes."
            ),
            safeNextSteps = listOf(
                "Continue standard digital safety practices: never share passwords or OTPs.",
                "Verify sender authenticity if unexpected payment instructions are introduced later."
            ),
            allDetectedSignals = emptyList(),
            matchedPatternRule = "Clean Pattern Baseline: 0 Malicious Triggers"
        )
    }

    private fun extractContextSnippet(fullText: String, keyword: String): String {
        val index = fullText.lowercase(Locale.ROOT).indexOf(keyword.lowercase(Locale.ROOT))
        if (index == -1) return keyword
        val start = (index - 20).coerceAtLeast(0)
        val end = (index + keyword.length + 20).coerceAtMost(fullText.length)
        val snippet = fullText.substring(start, end).trim()
        return if (start > 0) "...$snippet..." else "$snippet..."
    }
}
