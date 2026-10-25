package com.example.data

enum class PatternConfidence(val label: String) {
    DEFINITIVE_MATCH("Definitive Pattern Match"),
    HIGH_CONFIDENCE("High Confidence Pattern"),
    MODERATE_CONFIDENCE("Moderate Indicators"),
    INCONCLUSIVE("Inconclusive / Insufficient Evidence")
}

data class ObservedEvidence(
    val signalName: String,
    val quote: String,
    val description: String
)

data class DetectedSignal(
    val name: String,
    val category: String,
    val detectedText: String?,
    val role: String, // "Primary Trigger", "Corroborating Signal", "Contextual Indicator"
    val explanation: String
)

data class PatternFingerprint(
    val patternId: String,
    val patternName: String,
    val summaryExplanation: String,
    val confidence: PatternConfidence,
    val riskLevel: RiskLevel,
    val observedEvidence: List<ObservedEvidence>,
    val inferredPossibilities: List<String>,
    val safeNextSteps: List<String>,
    val allDetectedSignals: List<DetectedSignal>,
    val matchedPatternRule: String
)

data class TimelineEvent(
    val timeLabel: String,
    val title: String,
    val description: String,
    val isVerifiedByUser: Boolean = true
)

data class IncidentEvidence(
    val incidentDate: String = "",
    val incidentTime: String = "",
    val platform: String = "WhatsApp",
    val description: String = "",
    val transactionRef: String = "",
    val rawExtractedText: String = "",
    val redactedText: String = "",
    val timeline: List<TimelineEvent> = emptyList(),
    val checklist: List<String> = emptyList()
)
