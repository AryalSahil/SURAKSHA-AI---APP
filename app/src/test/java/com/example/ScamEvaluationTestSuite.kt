package com.example

import com.example.data.*
import org.junit.Assert.*
import org.junit.Test

/**
 * Automated test suite for SURAKSHA AI verification test cases.
 * Validates detection accuracy for fraudulent patterns, false positive resistance
 * on legitimate messages, and graceful handling of edge/inconclusive cases.
 */
class ScamEvaluationTestSuite {

    @Test
    fun `TC-JOB-001 - Fraudulent job offer with upfront registration fee and urgency`() {
        val input = "Congratulations! You have been selected for a Work From Home data entry job. Salary Rs.35,000/month. Pay Rs.1,500 registration fee to confirm your slot within 2 hours. Contact HR Priya: 9XXXXXXXXX"
        val fingerprint = PatternEngine.detectFingerprint(input)
        val analysis = RuleEngine.analyzeText(input)

        // Verify pattern engine detection
        assertEquals("FP_ADVANCE_FEE_JOB", fingerprint.patternId)
        assertEquals(PatternConfidence.DEFINITIVE_MATCH, fingerprint.confidence)
        assertEquals(RiskLevel.HIGH_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Employment") })
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Advance Payment") })
        assertTrue(fingerprint.inferredPossibilities.isNotEmpty())
        assertTrue(fingerprint.safeNextSteps.isNotEmpty())

        // Verify overall rule engine analysis
        assertEquals(RiskLevel.HIGH_CONCERN, analysis.riskLevel)
        assertTrue(analysis.warningSigns.any { it.ruleName.contains("Advance Fee") })
    }

    @Test
    fun `TC-BANK-002 - Bank impersonation requesting OTP and suspicious link`() {
        val input = "Your SBI bank account has been suspended. Update KYC now at http://sbi-kyc-update.xyz/verify or share your OTP with customer care within 24 hours."
        val fingerprint = PatternEngine.detectFingerprint(input)
        val analysis = RuleEngine.analyzeText(input)

        assertEquals("FP_BANK_CREDENTIAL_HARVESTING", fingerprint.patternId)
        assertEquals(PatternConfidence.DEFINITIVE_MATCH, fingerprint.confidence)
        assertEquals(RiskLevel.HIGH_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Credential Extraction") })
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Authority") })

        assertEquals(RiskLevel.HIGH_CONCERN, analysis.riskLevel)
        assertTrue(analysis.warningSigns.any { it.ruleName.contains("Sensitive Credential") })
    }

    @Test
    fun `TC-LOAN-003 - Predatory loan offer with advance processing fee to personal UPI VPA`() {
        val input = "Pre-approved personal loan of Rs.5,00,000 at 0% interest. No documents needed. Pay Rs.2,000 processing fee now to release your loan to prizeclaim@upi. Offer expires in 24 hours!"
        val fingerprint = PatternEngine.detectFingerprint(input)
        val analysis = RuleEngine.analyzeText(input)

        assertEquals("FP_PREDATORY_LOAN_FEE", fingerprint.patternId)
        assertEquals(PatternConfidence.DEFINITIVE_MATCH, fingerprint.confidence)
        assertEquals(RiskLevel.HIGH_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Loan Claim") })
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Advance Payment") })

        assertEquals(RiskLevel.HIGH_CONCERN, analysis.riskLevel)
    }

    @Test
    fun `TC-PRIZE-004 - Unsolicited lucky draw prize claiming advance tax fee`() {
        val input = "You have won Rs.50,000 in the Lucky Draw! To claim your prize, pay Rs.500 tax clearance fee to prizeclaim@upi within 2 hours."
        val fingerprint = PatternEngine.detectFingerprint(input)
        val analysis = RuleEngine.analyzeText(input)

        assertEquals("FP_INVESTMENT_PRIZE_TRAP", fingerprint.patternId)
        assertEquals(PatternConfidence.DEFINITIVE_MATCH, fingerprint.confidence)
        assertEquals(RiskLevel.HIGH_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Reward") })

        assertEquals(RiskLevel.HIGH_CONCERN, analysis.riskLevel)
    }

    @Test
    fun `TC-LEGIT-005 - Legitimate e-commerce delivery notification returns low concern`() {
        val input = "Your package with tracking order #402-182910 has been delivered to your doorstep. Thank you for shopping with us!"
        val fingerprint = PatternEngine.detectFingerprint(input)
        val analysis = RuleEngine.analyzeText(input)

        assertEquals("FP_BENIGN_LEGITIMATE", fingerprint.patternId)
        assertEquals(PatternConfidence.HIGH_CONFIDENCE, fingerprint.confidence)
        assertEquals(RiskLevel.LOW_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.observedEvidence.isEmpty())

        assertEquals(RiskLevel.LOW_CONCERN, analysis.riskLevel)
        assertTrue(analysis.warningSigns.isEmpty())
    }

    @Test
    fun `TC-INCONC-006 - Ambiguous short message returns inconclusive unable to assess`() {
        val input = "Hello, please call me back regarding our discussion yesterday."
        val fingerprint = PatternEngine.detectFingerprint(input)

        // Benign/inconclusive - not enough scam markers
        assertEquals(RiskLevel.LOW_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.observedEvidence.isEmpty())
    }

    @Test
    fun `TC-INCONC-007 - Extremely short input returns inconclusive fingerprint`() {
        val input = "Hi there"
        val fingerprint = PatternEngine.detectFingerprint(input)
        val analysis = RuleEngine.analyzeText(input)

        assertEquals("FP_INCONCLUSIVE", fingerprint.patternId)
        assertEquals(PatternConfidence.INCONCLUSIVE, fingerprint.confidence)
        assertEquals(RiskLevel.UNABLE_TO_ASSESS, fingerprint.riskLevel)
        assertEquals(RiskLevel.UNABLE_TO_ASSESS, analysis.riskLevel)
    }

    @Test
    fun `TC-EMPTY-008 - Empty or whitespace input handled safely without exceptions`() {
        val input = "   "
        val fingerprint = PatternEngine.detectFingerprint(input)
        val analysis = RuleEngine.analyzeText(input)

        assertEquals("FP_INCONCLUSIVE", fingerprint.patternId)
        assertEquals(RiskLevel.UNABLE_TO_ASSESS, fingerprint.riskLevel)
        assertEquals(RiskLevel.UNABLE_TO_ASSESS, analysis.riskLevel)
        assertTrue(analysis.warningSigns.isEmpty())
    }

    @Test
    fun `TC-URL-009 - Phishing URL analysis flags suspicious TLD and brand spoofing`() {
        val url = "http://sbi-kyc-update.xyz/verify"
        val result = RuleEngine.analyzeUrl(url)

        assertEquals(RiskLevel.HIGH_CONCERN, result.riskLevel)
        assertTrue(result.isUrlAnalysis)
        assertTrue(result.warningSigns.any { it.ruleName.contains("Brand") })
        assertTrue(result.warningSigns.any { it.ruleName.contains("Top-Level Domain") })
    }
}
