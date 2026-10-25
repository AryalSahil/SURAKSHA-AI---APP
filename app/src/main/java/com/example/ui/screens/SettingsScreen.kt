package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.data.AppLanguage
import com.example.data.Translations
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    language: AppLanguage,
    onLanguageClick: () -> Unit,
    aiConsentEnabled: Boolean,
    onChangeAiConsent: () -> Unit,
    onOpenPrivacy: () -> Unit,
    onOpenFeedback: () -> Unit,
    onClearData: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showClearDataDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurakshaBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column {
            Text(
                text = Translations.get("settings_title", language),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SurakshaNavy
            )
            Text(
                text = "Preferences, Privacy, and Diagnostics",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        // Section: Language Preference
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("settings_lang_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLanguageClick() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFFEFF6FF),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Language, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = Translations.get("settings_language_title", language),
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurakshaNavy
                            )
                            Text(
                                text = "${language.displayName} (${language.nativeName})",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Change",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SurakshaBlue
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // Section: AI-Assisted Analysis
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("settings_ai_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFFEFF6FF),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = Translations.get("settings_ai_title", language),
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurakshaNavy
                            )
                            Text(
                                text = if (aiConsentEnabled) "Enabled (AI explanations active)" else "Disabled (Strictly device-local checks)",
                                fontSize = 12.sp,
                                color = if (aiConsentEnabled) SafeGreen else TextSecondary
                            )
                        }
                    }

                    TextButton(onClick = onChangeAiConsent, modifier = Modifier.testTag("btn_change_ai_consent")) {
                        Text("Change", color = SurakshaBlue, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "When enabled, analysis details can leverage the Gemini AI model. Text never includes personal passwords or PINs.",
                    fontSize = 11.5.sp,
                    color = TextMuted,
                    lineHeight = 16.sp
                )
            }
        }

        // Section: Privacy & Feedback Navigation Items
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Privacy Item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenPrivacy() }
                        .padding(16.dp)
                        .testTag("settings_privacy_item"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.PrivacyTip, contentDescription = null, tint = SurakshaNavy, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = Translations.get("settings_privacy_btn", language),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                }

                Divider(color = BorderSubtle, thickness = 0.8.dp)

                // Feedback Item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenFeedback() }
                        .padding(16.dp)
                        .testTag("settings_feedback_item"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Feedback, contentDescription = null, tint = SurakshaNavy, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = Translations.get("settings_feedback_btn", language),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextPrimary
                        )
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                }
            }
        }

        // Section: Clear Local Data
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("settings_clear_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.DeleteSweep, contentDescription = null, tint = DangerRed, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = Translations.get("settings_clear_data_btn", language),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DangerRed
                            )
                            Text(
                                text = "Resets quiz progress, language, & local cache",
                                fontSize = 11.5.sp,
                                color = TextMuted
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { showClearDataDialog = true },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, DangerRed),
                        modifier = Modifier.testTag("btn_clear_data")
                    ) {
                        Text("Reset", color = DangerRed, fontSize = 12.sp)
                    }
                }
            }
        }

        // Section: About App
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("settings_about_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_app_logo),
                        contentDescription = "Suraksha AI Logo",
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = Translations.get("settings_about_title", language),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaNavy
                        )
                        Text(
                            text = Translations.get("settings_version", language),
                            fontSize = 11.5.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = Translations.get("settings_tagline", language),
                    fontSize = 12.5.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "National Emergency: 1930 | cybercrime.gov.in\nOfficial civic safety project for cyber fraud prevention.",
                        fontSize = 11.sp,
                        color = TextMuted,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Confirmation Dialog for Clearing Data
    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            icon = { Icon(Icons.Filled.Warning, contentDescription = null, tint = DangerRed) },
            title = { Text("Clear All Local Data?") },
            text = {
                Text(
                    "This will reset your quiz completion status, saved feedback items, and language preference back to default. This cannot be undone.",
                    fontSize = 13.5.sp,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearData()
                        showClearDataDialog = false
                        Toast.makeText(context, "Local data cleared successfully.", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) {
                    Text("Clear Everything")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
