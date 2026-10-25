package com.example.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ui.theme.*

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    EN("en", "English", "English"),
    HI("hi", "Hindi", "हिन्दी"),
    AS("as", "Assamese", "অসমীয়া");

    companion object {
        fun fromCode(code: String): AppLanguage = entries.firstOrNull { it.code == code } ?: EN
    }
}

enum class ScamCategory(val id: String, val titleEn: String) {
    UPI_FRAUD("upi_fraud", "UPI / Payment Fraud"),
    FAKE_JOB("fake_job", "Fake Job / Recruitment"),
    LOAN_OFFER("loan_offer", "Loan / Financial Offer"),
    PHISHING("phishing", "Phishing / Suspicious Website"),
    FAKE_CUSTOMER_CARE("fake_customer_care", "Fake Customer Care"),
    OTP_SECURITY("otp_security", "OTP / Account Security Scam"),
    INVESTMENT_REWARD("investment_reward", "Investment / Reward Offer"),
    OTHER("other", "Other / Not Sure")
}

enum class RiskLevel(
    val title: String,
    val icon: ImageVector,
    val containerColor: Color,
    val borderColor: Color,
    val contentColor: Color
) {
    LOW_CONCERN(
        title = "Low Concern",
        icon = Icons.Filled.CheckCircle,
        containerColor = SafeGreenBg,
        borderColor = SafeGreenBorder,
        contentColor = SafeGreen
    ),
    CAUTION(
        title = "Caution",
        icon = Icons.Filled.Warning,
        containerColor = CautionAmberBg,
        borderColor = CautionAmberBorder,
        contentColor = CautionAmberText
    ),
    HIGH_CONCERN(
        title = "High Concern",
        icon = Icons.Filled.Dangerous,
        containerColor = DangerRedBg,
        borderColor = DangerRedBorder,
        contentColor = DangerRed
    ),
    UNABLE_TO_ASSESS(
        title = "Unable to Assess",
        icon = Icons.Filled.HelpOutline,
        containerColor = InfoBlueBg,
        borderColor = InfoBlueBorder,
        contentColor = InfoBlueText
    )
}

data class RuleFinding(
    val ruleName: String,
    val explanation: String,
    val matchedText: String? = null
)

data class AnalysisResult(
    val id: String,
    val riskLevel: RiskLevel,
    val summary: String,
    val warningSigns: List<RuleFinding>,
    val whyItMatters: String,
    val recommendedActions: List<String>,
    val safeVerificationSteps: List<String>,
    val isUrlAnalysis: Boolean = false,
    val rawInput: String = "",
    val patternFingerprint: PatternFingerprint? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class Lesson(
    val id: String,
    val title: String,
    val category: String,
    val durationMinutes: Int,
    val shortDescription: String,
    val whatIsThis: String,
    val fictionalExample: String,
    val warningSigns: List<String>,
    val whatToDo: List<String>,
    val whatNotToDo: List<String>,
    val quizQuestions: List<QuizQuestion>
)

data class FeedbackItem(
    val id: String,
    val category: String,
    val description: String,
    val contactEmail: String? = null,
    val relatedAnalysisId: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
