package com.example.data

enum class ScenarioCategory(val id: String, val displayName: String) {
    FAKE_JOB("fake_job", "Recruitment & Job Offer"),
    BANK_IMPERSONATION("bank_impersonation", "Bank & Security Impersonation"),
    UPI_FRAUD("upi_fraud", "UPI & Payment Traps")
}

enum class ScenarioDifficulty(val label: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate")
}

data class RedFlagItem(
    val title: String,
    val description: String,
    val detectedInStep: Int
)

data class ScenarioOption(
    val id: String,
    val text: String,
    val isSafe: Boolean,
    val pointsDelta: Int,
    val feedbackText: String,
    val nextStepId: String?,
    val explanation: String,
    val mistakeTag: String? = null
)

enum class SpeakerType {
    SCAMMER,
    USER,
    SYSTEM_NOTE
}

data class ChatMessage(
    val speaker: SpeakerType,
    val speakerName: String,
    val text: String,
    val timestampText: String = "Just now",
    val isSuspiciousFlagged: Boolean = false
)

data class ScenarioStep(
    val id: String,
    val stepNumber: Int,
    val totalSteps: Int,
    val promptMessage: ChatMessage,
    val redFlagsAtThisStep: List<RedFlagItem> = emptyList(),
    val options: List<ScenarioOption>
)

data class Scenario(
    val id: String,
    val title: String,
    val category: ScenarioCategory,
    val difficulty: ScenarioDifficulty,
    val estimatedMinutes: Int,
    val shortDescription: String,
    val contextDescription: String,
    val initialStepId: String,
    val steps: Map<String, ScenarioStep>,
    val relatedLessonId: String,
    val learningTakeaway: String,
    val safetyChecklist: List<String>,
    val commonMistakeToAvoid: String,
    val safestAction: String
)

data class SimulationDecisionRecord(
    val stepNumber: Int,
    val promptText: String,
    val selectedOption: ScenarioOption
)

data class SimulationResult(
    val scenarioId: String,
    val scenarioTitle: String,
    val score: Int, // 0 - 100
    val isCompleted: Boolean,
    val passed: Boolean,
    val badgeEarned: String?,
    val decisionsHistory: List<SimulationDecisionRecord>,
    val correctDecisions: List<SimulationDecisionRecord>,
    val mistakes: List<SimulationDecisionRecord>,
    val allEncounteredRedFlags: List<RedFlagItem>,
    val safestAction: String,
    val commonMistakeToAvoid: String,
    val safetyChecklist: List<String>,
    val relatedLessonId: String,
    val disclaimer: String = "This educational score is based on your simulated choices in a fictional environment and is not a validated real-world cyber assessment."
)

object SimulatorEngine {
    fun calculateResult(
        scenario: Scenario,
        decisions: List<SimulationDecisionRecord>
    ): SimulationResult {
        var baseScore = 50
        val correctDecisions = mutableListOf<SimulationDecisionRecord>()
        val mistakes = mutableListOf<SimulationDecisionRecord>()
        val encounteredRedFlags = mutableListOf<RedFlagItem>()

        decisions.forEach { record ->
            baseScore += record.selectedOption.pointsDelta
            if (record.selectedOption.isSafe) {
                correctDecisions.add(record)
            } else {
                mistakes.add(record)
            }
            val step = scenario.steps.values.find { it.stepNumber == record.stepNumber }
            step?.redFlagsAtThisStep?.let { encounteredRedFlags.addAll(it) }
        }

        // Clamp between 0 and 100
        val finalScore = baseScore.coerceIn(0, 100)
        val passed = finalScore >= 60
        val badge = if (passed) "Scam-Aware Learner" else null

        return SimulationResult(
            scenarioId = scenario.id,
            scenarioTitle = scenario.title,
            score = finalScore,
            isCompleted = decisions.size >= scenario.steps.size,
            passed = passed,
            badgeEarned = badge,
            decisionsHistory = decisions,
            correctDecisions = correctDecisions,
            mistakes = mistakes,
            allEncounteredRedFlags = encounteredRedFlags.distinctBy { it.title },
            safestAction = scenario.safestAction,
            commonMistakeToAvoid = scenario.commonMistakeToAvoid,
            safetyChecklist = scenario.safetyChecklist,
            relatedLessonId = scenario.relatedLessonId
        )
    }
}
