package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AnalysisResult
import com.example.data.AppLanguage
import com.example.data.PatternConfidence
import com.example.data.PatternFingerprint
import com.example.data.RiskLevel
import com.example.data.Translations
import com.example.ui.theme.*

@Composable
fun ResultCardsView(
    result: AnalysisResult,
    language: AppLanguage,
    onAnalyseAnother: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("analysis_results_container"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Risk Assessment Card (Top, Prominent)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = result.riskLevel.containerColor),
            border = BorderStroke(1.5.dp, result.riskLevel.borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("risk_assessment_card")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = result.riskLevel.icon,
                        contentDescription = result.riskLevel.title,
                        tint = result.riskLevel.contentColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        val riskTitle = when (result.riskLevel) {
                            RiskLevel.LOW_CONCERN -> Translations.get("results_risk_low", language)
                            RiskLevel.CAUTION -> Translations.get("results_risk_caution", language)
                            RiskLevel.HIGH_CONCERN -> Translations.get("results_risk_high", language)
                            RiskLevel.UNABLE_TO_ASSESS -> Translations.get("results_risk_unknown", language)
                        }
                        Text(
                            text = riskTitle,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = result.riskLevel.contentColor
                        )
                        Text(
                            text = "Assessment ID: ${result.id}",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = result.summary,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (result.isUrlAnalysis) {
                        Translations.get("link_disclaimer", language)
                    } else {
                        Translations.get("checker_disclaimer", language)
                    },
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }

        // 2. High Concern Advisory Banner (Prominent Red Banner)
        if (result.riskLevel == RiskLevel.HIGH_CONCERN) {
            Surface(
                color = DangerRed,
                shape = RoundedCornerShape(14.dp),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("high_concern_advisory_banner")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Alert",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = Translations.get("checker_high_advisory", language),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // 2.5 Scam Pattern Fingerprint Card
        result.patternFingerprint?.let { fingerprint ->
            var isSignalsPanelExpanded by remember { mutableStateOf(false) }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.2.dp, when (fingerprint.confidence) {
                    PatternConfidence.DEFINITIVE_MATCH -> DangerRedBorder
                    PatternConfidence.HIGH_CONFIDENCE -> CautionAmberBorder
                    PatternConfidence.MODERATE_CONFIDENCE -> BorderMedium
                    PatternConfidence.INCONCLUSIVE -> InfoBlueBorder
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pattern_fingerprint_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Fingerprint,
                                contentDescription = "Fingerprint",
                                tint = when (fingerprint.confidence) {
                                    PatternConfidence.DEFINITIVE_MATCH -> DangerRed
                                    PatternConfidence.HIGH_CONFIDENCE -> CautionAmberText
                                    PatternConfidence.MODERATE_CONFIDENCE -> SurakshaBlue
                                    PatternConfidence.INCONCLUSIVE -> TextMuted
                                },
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = Translations.get("results_heading_fingerprint", language),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurakshaNavy
                            )
                        }

                        Surface(
                            color = when (fingerprint.confidence) {
                                PatternConfidence.DEFINITIVE_MATCH -> DangerRedBg
                                PatternConfidence.HIGH_CONFIDENCE -> CautionAmberBg
                                PatternConfidence.MODERATE_CONFIDENCE -> Color(0xFFEFF6FF)
                                PatternConfidence.INCONCLUSIVE -> Color(0xFFF1F5F9)
                            },
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = fingerprint.confidence.label,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (fingerprint.confidence) {
                                    PatternConfidence.DEFINITIVE_MATCH -> DangerRed
                                    PatternConfidence.HIGH_CONFIDENCE -> CautionAmberText
                                    PatternConfidence.MODERATE_CONFIDENCE -> SurakshaBlue
                                    PatternConfidence.INCONCLUSIVE -> TextMuted
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = fingerprint.patternName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = fingerprint.summaryExplanation,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )

                    // Directly Observed Evidence
                    if (fingerprint.observedEvidence.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = Translations.get("results_observed_evidence", language),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaNavy
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        fingerprint.observedEvidence.forEach { obs ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.FactCheck, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = obs.signalName,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                }
                                Text(
                                    text = "\"${obs.quote}\"",
                                    fontSize = 12.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                    color = DangerRed,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                                Text(
                                    text = obs.description,
                                    fontSize = 11.5.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    // Inferred Possibilities (Clearly separated from observed facts)
                    if (fingerprint.inferredPossibilities.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = Translations.get("results_inferred_possibilities", language),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaNavy
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        fingerprint.inferredPossibilities.forEach { inf ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Psychology,
                                    contentDescription = null,
                                    tint = CautionAmberText,
                                    modifier = Modifier.size(16.dp).padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = inf,
                                    fontSize = 12.5.sp,
                                    color = TextSecondary,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }

                    // What You Can Safely Do Next
                    if (fingerprint.safeNextSteps.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = Translations.get("results_safe_next_steps", language),
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafeGreen
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        fingerprint.safeNextSteps.forEach { step ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = null,
                                    tint = SafeGreen,
                                    modifier = Modifier.size(15.dp).padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = step,
                                    fontSize = 12.5.sp,
                                    color = TextPrimary,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }

                    // Expandable Details Panel for Detected Signals
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isSignalsPanelExpanded = !isSignalsPanelExpanded }
                            .testTag("btn_inspect_signals_panel")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Tune, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${Translations.get("results_inspect_signals", language)} (${fingerprint.allDetectedSignals.size})",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SurakshaBlue
                                )
                            }
                            Icon(
                                imageVector = if (isSignalsPanelExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = null,
                                tint = SurakshaBlue
                            )
                        }
                    }

                    AnimatedVisibility(visible = isSignalsPanelExpanded) {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            fingerprint.allDetectedSignals.forEach { sig ->
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, BorderSubtle),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = sig.name,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SurakshaNavy
                                            )
                                            Surface(
                                                color = if (sig.role == "Primary Trigger") DangerRedBg else Color(0xFFF1F5F9),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = sig.role,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = if (sig.role == "Primary Trigger") DangerRed else TextMuted,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        sig.detectedText?.let { snippet ->
                                            Text(
                                                text = "Pattern matched: \"$snippet\"",
                                                fontSize = 11.5.sp,
                                                color = DangerRed,
                                                modifier = Modifier.padding(vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = sig.explanation,
                                            fontSize = 11.5.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Rule Signature: ${fingerprint.matchedPatternRule} • No validated probability scores used",
                        fontSize = 10.5.sp,
                        color = TextMuted,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // 3. Warning Signs Found Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("warning_signs_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Flag,
                        contentDescription = null,
                        tint = SurakshaNavy,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = Translations.get("results_heading_warning_signs", language),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (result.warningSigns.isEmpty()) {
                    Text(
                        text = Translations.get("results_no_warning_signs", language),
                        fontSize = 14.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                } else {
                    result.warningSigns.forEachIndexed { index, finding ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "•",
                                    color = DangerRed,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Column {
                                    Text(
                                        text = finding.ruleName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = finding.explanation,
                                        fontSize = 13.sp,
                                        color = TextSecondary,
                                        lineHeight = 18.sp
                                    )
                                    if (!finding.matchedText.isNullOrBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Surface(
                                            color = Color(0xFFF1F5F9),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "${Translations.get("results_pattern_matched", language)} \"${finding.matchedText}\"",
                                                fontSize = 11.sp,
                                                color = TextMuted,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            if (index < result.warningSigns.size - 1) {
                                Divider(
                                    color = BorderSubtle,
                                    thickness = 0.8.dp,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Why This Matters Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("why_matters_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = SurakshaBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = Translations.get("results_heading_why_matters", language),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = result.whyItMatters,
                    fontSize = 13.5.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            }
        }

        // 5. Recommended Actions Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("recommended_actions_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircleOutline,
                        contentDescription = null,
                        tint = SafeGreen,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = Translations.get("results_heading_recommended", language),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                result.recommendedActions.forEachIndexed { i, action ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            color = if (i == 0 && result.riskLevel == RiskLevel.HIGH_CONCERN) DangerRedBg else Color(0xFFEFF6FF),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.size(22.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${i + 1}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (i == 0 && result.riskLevel == RiskLevel.HIGH_CONCERN) DangerRed else SurakshaBlue
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = action,
                            fontSize = 13.sp,
                            color = if (i == 0 && result.riskLevel == RiskLevel.HIGH_CONCERN) DangerRed else TextPrimary,
                            fontWeight = if (i == 0 && result.riskLevel == RiskLevel.HIGH_CONCERN) FontWeight.Bold else FontWeight.Normal,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // 6. Safe Verification Steps Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("verification_steps_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.VerifiedUser,
                        contentDescription = null,
                        tint = SurakshaBlue,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = Translations.get("results_heading_verification", language),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                result.safeVerificationSteps.forEach { step ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = SafeGreen,
                            modifier = Modifier.size(16.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = step,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // 7. Reporting Guidance Card (Shown for Caution or High)
        if (result.riskLevel == RiskLevel.CAUTION || result.riskLevel == RiskLevel.HIGH_CONCERN) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                border = BorderStroke(1.dp, Color(0xFFFECACA)),
                modifier = Modifier.fillMaxWidth().testTag("reporting_guidance_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.PhoneInTalk,
                            contentDescription = null,
                            tint = DangerRed,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Translations.get("results_heading_reporting", language),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DangerRed
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "If you have already lost money: Call 1930 immediately (National Cyber Crime Helpline).",
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "• Contact your bank or payment provider through their official verified helpline to freeze transactions.\n• File an official complaint on the National Cyber Crime Portal.\n• Preserve all SMS messages, UTR numbers, and screenshots as evidence.",
                        fontSize = 12.5.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1930"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("btn_call_1930_results")
                        ) {
                            Icon(Icons.Filled.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call 1930", fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cybercrime.gov.in/"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, DangerRed),
                            modifier = Modifier.weight(1.3f).testTag("btn_cybercrime_portal_results")
                        ) {
                            Text("cybercrime.gov.in", color = DangerRed, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 8. Action Buttons Row at the bottom
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Copy Safety Checklist
            OutlinedButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val content = buildString {
                        appendLine("--- SURAKSHA AI Safety Checklist ---")
                        appendLine("Assessment: ${result.riskLevel.title}")
                        appendLine("Summary: ${result.summary}")
                        if (result.warningSigns.isNotEmpty()) {
                            appendLine("\nWarning Signs:")
                            result.warningSigns.forEach { appendLine("• ${it.ruleName}: ${it.explanation}") }
                        }
                        appendLine("\nRecommended Actions:")
                        result.recommendedActions.forEachIndexed { i, a -> appendLine("${i + 1}. $a") }
                        appendLine("\nNational Cyber Crime Helpline: 1930 | https://cybercrime.gov.in/")
                    }
                    val clip = ClipData.newPlainText("Suraksha Safety Checklist", content)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, Translations.get("results_toast_copied", language), Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, SurakshaNavy),
                modifier = Modifier.weight(1f).testTag("btn_copy_checklist")
            ) {
                Text(
                    text = Translations.get("results_btn_copy_checklist", language),
                    color = SurakshaNavy,
                    fontSize = 11.5.sp,
                    maxLines = 1
                )
            }

            // Share Concern
            OutlinedButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val safeSummary = buildString {
                        appendLine("⚠️ Cyber Safety Alert from SURAKSHA AI")
                        appendLine("Risk Assessment: ${result.riskLevel.title}")
                        appendLine(result.summary)
                        if (result.riskLevel == RiskLevel.HIGH_CONCERN) {
                            appendLine("\n🚨 ADVISORY: Do NOT share your OTP, UPI PIN, password, or grant remote access to anyone.")
                        }
                        appendLine("\nCheck suspicious messages with SURAKSHA AI: Think Before You Click. Check Before You Pay.")
                        appendLine("National Cyber Helpline: 1930")
                    }
                    val clip = ClipData.newPlainText("Suraksha Alert", safeSummary)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, Translations.get("results_toast_concern_copied", language), Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, SurakshaBlue),
                modifier = Modifier.weight(1f).testTag("btn_share_concern")
            ) {
                Text(
                    text = Translations.get("results_btn_share_concern", language),
                    color = SurakshaBlue,
                    fontSize = 11.5.sp,
                    maxLines = 1
                )
            }
        }

        // Analyse Another button
        Button(
            onClick = onAnalyseAnother,
            colors = ButtonDefaults.buttonColors(containerColor = SurakshaNavy),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_analyse_another")
        ) {
            Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = Translations.get("results_btn_analyse_another", language),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
