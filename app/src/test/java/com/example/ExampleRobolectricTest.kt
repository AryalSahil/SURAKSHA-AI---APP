package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Suraksha AI", appName)
    }

    @Test
    fun `test scam rule engine detects high concern on upi suspension sample`() {
        val upiSample = SampleData.samples.first { it.category == ScamCategory.UPI_FRAUD }
        val result = RuleEngine.analyzeText(upiSample.text)
        assertEquals(RiskLevel.HIGH_CONCERN, result.riskLevel)
        assertTrue(result.warningSigns.isNotEmpty())
        assertNotNull(result.patternFingerprint)
    }

    @Test
    fun `test url rule engine detects suspicious tld and shortener`() {
        val result = RuleEngine.analyzeUrl("http://sbi-kyc-update.xyz/verify")
        assertEquals(RiskLevel.HIGH_CONCERN, result.riskLevel)
        assertTrue(result.warningSigns.any { it.ruleName.contains("Brand") || it.ruleName.contains("Top-Level Domain") })
    }

    // --- PHASE 1 PATTERN FINGERPRINT UNIT TESTS ---

    @Test
    fun `test pattern 1 fake job with upfront registration fee and urgency`() {
        val text = "Congratulations! You have been selected for a Work From Home data entry job. Salary Rs.35,000/month. Pay Rs.1,500 registration fee to confirm your slot within 2 hours. Contact HR Priya: 9XXXXXXXXX"
        val fingerprint = PatternEngine.detectFingerprint(text)

        assertEquals("FP_ADVANCE_FEE_JOB", fingerprint.patternId)
        assertEquals(PatternConfidence.DEFINITIVE_MATCH, fingerprint.confidence)
        assertEquals(RiskLevel.HIGH_CONCERN, fingerprint.riskLevel)

        // Verifies separation of observed evidence from inferred possibilities
        assertTrue(fingerprint.observedEvidence.isNotEmpty())
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Employment") })
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Advance Payment") })
        assertTrue(fingerprint.inferredPossibilities.isNotEmpty())
        assertTrue(fingerprint.safeNextSteps.isNotEmpty())
        assertTrue(fingerprint.allDetectedSignals.any { it.role == "Primary Trigger" })
    }

    @Test
    fun `test pattern 2 bank impersonation with suspicious link and OTP request`() {
        val text = "Your SBI bank account has been suspended. Update KYC now at http://sbi-kyc-update.xyz/verify or share your OTP with customer care within 24 hours."
        val fingerprint = PatternEngine.detectFingerprint(text)

        assertEquals("FP_BANK_CREDENTIAL_HARVESTING", fingerprint.patternId)
        assertEquals(PatternConfidence.DEFINITIVE_MATCH, fingerprint.confidence)
        assertEquals(RiskLevel.HIGH_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Credential Extraction") })
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Authority") })
    }

    @Test
    fun `test pattern 3 loan offer with advance fee and personal payment account`() {
        val text = "Pre-approved personal loan of Rs.5,00,000 at 0% interest. No documents needed. Pay Rs.2,000 processing fee now to release your loan to prizeclaim@upi. Offer expires in 24 hours!"
        val fingerprint = PatternEngine.detectFingerprint(text)

        assertEquals("FP_PREDATORY_LOAN_FEE", fingerprint.patternId)
        assertEquals(PatternConfidence.DEFINITIVE_MATCH, fingerprint.confidence)
        assertEquals(RiskLevel.HIGH_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Loan Claim") })
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Advance Payment") })
    }

    @Test
    fun `test pattern 4 guaranteed investment returns with urgency pressure`() {
        val text = "You have won Rs.50,000 in the Lucky Draw! To claim your prize, pay Rs.500 tax clearance fee to prizeclaim@upi within 2 hours."
        val fingerprint = PatternEngine.detectFingerprint(text)

        assertEquals("FP_INVESTMENT_PRIZE_TRAP", fingerprint.patternId)
        assertEquals(PatternConfidence.DEFINITIVE_MATCH, fingerprint.confidence)
        assertEquals(RiskLevel.HIGH_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.observedEvidence.any { it.signalName.contains("Reward") })
    }

    @Test
    fun `test overlapping patterns handle multiple triggers safely`() {
        // Message combining elements of both employment and loan advance fee
        val text = "Urgent work from home job offer! Pre-approved personal loan of Rs.50,000 also provided. Pay Rs.1,000 advance fee immediately."
        val fingerprint = PatternEngine.detectFingerprint(text)

        // Pattern matches advance fee recruitment hook with comprehensive signal details
        assertNotNull(fingerprint.patternId)
        assertEquals(RiskLevel.HIGH_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.allDetectedSignals.size >= 3)
        // Ensure signals are cleanly annotated with their roles
        assertTrue(fingerprint.allDetectedSignals.any { it.name.contains("Recruitment") || it.name.contains("Loan") })
    }

    @Test
    fun `test legitimate message returns low concern and benign fingerprint`() {
        val text = "Your Amazon order #402-182910 has been delivered to your doorstep. Thank you for shopping with us!"
        val fingerprint = PatternEngine.detectFingerprint(text)

        assertEquals("FP_BENIGN_LEGITIMATE", fingerprint.patternId)
        assertEquals(PatternConfidence.HIGH_CONFIDENCE, fingerprint.confidence)
        assertEquals(RiskLevel.LOW_CONCERN, fingerprint.riskLevel)
        assertTrue(fingerprint.observedEvidence.isEmpty())
        assertTrue(fingerprint.summaryExplanation.contains("does not match", ignoreCase = true))
    }

    @Test
    fun `test inconclusive inputs return unable to assess`() {
        val shortGreeting = "Hello friend"
        val fingerprint = PatternEngine.detectFingerprint(shortGreeting)

        assertEquals("FP_INCONCLUSIVE", fingerprint.patternId)
        assertEquals(PatternConfidence.INCONCLUSIVE, fingerprint.confidence)
        assertEquals(RiskLevel.UNABLE_TO_ASSESS, fingerprint.riskLevel)
        assertTrue(fingerprint.safeNextSteps.isNotEmpty())
    }

    // --- SCAM CONVERSATION SIMULATOR UNIT TESTS ---

    @Test
    fun `test simulator library contains required scenarios with valid steps`() {
        val scenarios = SimulatorData.scenarios
        assertTrue("Must contain at least 3 scenarios", scenarios.size >= 3)

        val jobScenario = SimulatorData.getScenarioById("scenario_fake_job")
        assertNotNull("Fake job scenario must exist", jobScenario)
        assertEquals(ScenarioCategory.FAKE_JOB, jobScenario?.category)
        assertTrue(jobScenario?.steps?.isNotEmpty() == true)

        val bankScenario = SimulatorData.getScenarioById("scenario_bank_otp")
        assertNotNull("Bank impersonation OTP scenario must exist", bankScenario)
        assertEquals(ScenarioCategory.BANK_IMPERSONATION, bankScenario?.category)

        val upiScenario = SimulatorData.getScenarioById("scenario_upi_fraud")
        assertNotNull("UPI payment trap scenario must exist", upiScenario)
        assertEquals(ScenarioCategory.UPI_FRAUD, upiScenario?.category)
    }

    @Test
    fun `test simulator engine awards passing score and badge for safe decisions`() {
        val scenario = SimulatorData.getScenarioById("scenario_fake_job")!!
        val safeDecisions = listOf(
            SimulationDecisionRecord(
                stepNumber = 1,
                promptText = "Shortlisted for WFH job",
                selectedOption = scenario.steps["job_step_1"]!!.options.first { it.isSafe }
            ),
            SimulationDecisionRecord(
                stepNumber = 2,
                promptText = "Deposit registration fee",
                selectedOption = scenario.steps["job_step_2"]!!.options.first { it.isSafe }
            ),
            SimulationDecisionRecord(
                stepNumber = 3,
                promptText = "Only 2 slots left urgency",
                selectedOption = scenario.steps["job_step_3"]!!.options.first { it.isSafe }
            ),
            SimulationDecisionRecord(
                stepNumber = 4,
                promptText = "Final action",
                selectedOption = scenario.steps["job_step_4"]!!.options.first { it.isSafe }
            )
        )

        val result = SimulatorEngine.calculateResult(scenario, safeDecisions)
        assertTrue("Score should be >= 80 for safe choices", result.score >= 80)
        assertTrue("Must pass scenario", result.passed)
        assertEquals("Scam-Aware Learner", result.badgeEarned)
        assertEquals(4, result.correctDecisions.size)
        assertEquals(0, result.mistakes.size)
        assertTrue(result.allEncounteredRedFlags.isNotEmpty())
    }

    @Test
    fun `test simulator engine penalizes dangerous decisions and withholds badge`() {
        val scenario = SimulatorData.getScenarioById("scenario_bank_otp")!!
        val riskyDecisions = listOf(
            SimulationDecisionRecord(
                stepNumber = 1,
                promptText = "Unauthorized transaction alert",
                selectedOption = scenario.steps["otp_step_1"]!!.options.first { !it.isSafe }
            ),
            SimulationDecisionRecord(
                stepNumber = 2,
                promptText = "Read cancellation OTP",
                selectedOption = scenario.steps["otp_step_2"]!!.options.first { !it.isSafe }
            ),
            SimulationDecisionRecord(
                stepNumber = 3,
                promptText = "Legal intimidation",
                selectedOption = scenario.steps["otp_step_3"]!!.options.first { !it.isSafe }
            ),
            SimulationDecisionRecord(
                stepNumber = 4,
                promptText = "Final action",
                selectedOption = scenario.steps["otp_step_4"]!!.options.first { !it.isSafe }
            )
        )

        val result = SimulatorEngine.calculateResult(scenario, riskyDecisions)
        assertTrue("Score should be low for risky choices", result.score < 50)
        assertFalse("Should fail scenario on dangerous choices", result.passed)
        assertNull("Badge should not be awarded", result.badgeEarned)
        assertEquals(4, result.mistakes.size)
        assertTrue(result.mistakes.any { it.selectedOption.mistakeTag != null })
    }

    @Test
    fun `test user preferences repository tracks completed scenarios`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = UserPreferencesRepository(context)
        repo.clearAllData()

        assertEquals(0, repo.completedScenarios.value.size)

        repo.markScenarioCompleted("scenario_fake_job")
        assertTrue(repo.completedScenarios.value.contains("scenario_fake_job"))

        repo.markScenarioCompleted("scenario_upi_fraud")
        assertEquals(2, repo.completedScenarios.value.size)

        repo.clearAllData()
        assertEquals(0, repo.completedScenarios.value.size)
    }

    // --- PHASE 4 REDACTION HELPER TESTS ---

    @Test
    fun `test sensitive credentials redaction sanitizes OTPs and card numbers`() {
        val message = "Your OTP: 948201 for transaction at XYZ. Card number 4111 2222 3333 4444."
        val redacted = RedactionHelper.redactSensitiveData(message)

        assertFalse("Should not contain plain 6-digit OTP", redacted.contains("948201"))
        assertTrue("Should contain redacted OTP token", redacted.contains("[REDACTED_CONFIDENTIAL_CODE]"))
        assertFalse("Should not contain full card number", redacted.contains("4111 2222 3333 4444"))
        assertTrue("Should contain masked card token", redacted.contains("XXXX-XXXX-XXXX-4444"))
    }
}

