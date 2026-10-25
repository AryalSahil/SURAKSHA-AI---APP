package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun ReportHelpScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var step1Expanded by remember { mutableStateOf(true) }
    var step2Expanded by remember { mutableStateOf(true) }
    var step3Expanded by remember { mutableStateOf(true) }
    var step4Expanded by remember { mutableStateOf(true) }

    // Interactive checklist states
    var checkScreenshot by remember { mutableStateOf(false) }
    var checkUtr by remember { mutableStateOf(false) }
    var checkDateTime by remember { mutableStateOf(false) }
    var checkPhoneUpi by remember { mutableStateOf(false) }
    var checkAppName by remember { mutableStateOf(false) }
    var checkCallRecording by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurakshaBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Column {
            Text(
                text = Translations.get("help_title", language),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SurakshaNavy
            )
            Text(
                text = "Emergency 4-Step Incident Response Protocol",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        // Emergency Banner (top, always visible)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DangerRedBg),
            border = BorderStroke(1.5.dp, DangerRedBorder),
            modifier = Modifier.fillMaxWidth().testTag("report_emergency_banner")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DangerRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Surface(
                            color = DangerRed,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = Translations.get("help_emergency_badge", language),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = Translations.get("help_emergency_title", language),
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = DangerRed
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, DangerRedBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "NATIONAL CYBER CRIME HELPLINE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextMuted,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "1930",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DangerRed,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "Available 24/7 across India for financial cyber fraud complaints",
                            fontSize = 11.5.sp,
                            color = TextSecondary
                        )
                    }
                }

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
                        modifier = Modifier.weight(1f).testTag("btn_call_1930")
                    ) {
                        Icon(Icons.Filled.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(Translations.get("help_emergency_btn_call", language), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cybercrime.gov.in/"))
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, DangerRed),
                        modifier = Modifier.weight(1.2f).testTag("btn_open_portal")
                    ) {
                        Text(Translations.get("help_emergency_btn_portal", language), color = DangerRed, fontSize = 12.sp)
                    }
                }
            }
        }

        // STEP 1: Secure Yourself First (Expandable)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("step_1_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { step1Expanded = !step1Expanded },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = SurakshaNavy,
                            shape = CircleShape,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("1", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Translations.get("help_step1_title", language),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaNavy
                        )
                    }
                    Icon(
                        imageVector = if (step1Expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = TextMuted
                    )
                }

                AnimatedVisibility(visible = step1Expanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        val items = listOf(
                            "Do NOT share any more OTPs, UPI PINs, or passwords with anyone.",
                            "Do NOT install any screen-sharing apps (AnyDesk, TeamViewer) at anyone's instruction.",
                            "Do NOT transfer any additional money for 'tax clearance', 'release fees', or 'file charges'.",
                            "Immediately change your banking, email, and UPI passwords from a safe, unaffected device."
                        )
                        items.forEach { item ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(Icons.Filled.Security, contentDescription = null, tint = DangerRed, modifier = Modifier.size(16.dp).padding(top = 2.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(item, fontSize = 13.sp, color = TextPrimary, lineHeight = 18.sp)
                            }
                        }
                    }
                }
            }
        }

        // STEP 2: Contact Your Bank or Payment Provider (Expandable)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("step_2_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { step2Expanded = !step2Expanded },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = SurakshaBlue,
                            shape = CircleShape,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("2", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Translations.get("help_step2_title", language),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaNavy
                        )
                    }
                    Icon(
                        imageVector = if (step2Expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = TextMuted
                    )
                }

                AnimatedVisibility(visible = step2Expanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = "• Call the official customer helpline printed directly on the back of your physical bank debit or credit card.\n• Open your official banking application or website directly (never from a chat link) and block the compromised card or account.\n• Inform the banking representative that a fraudulent transaction took place and request to freeze the transaction or place a debit freeze on the beneficiary account.\n• Request a formal complaint reference number and note the representative's name.",
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 19.sp
                        )
                    }
                }
            }
        }

        // STEP 3: Preserve Evidence (Interactive Checklist)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("step_3_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { step3Expanded = !step3Expanded },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = SurakshaNavy,
                            shape = CircleShape,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("3", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Translations.get("help_step3_title", language),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaNavy
                        )
                    }
                    Icon(
                        imageVector = if (step3Expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = TextMuted
                    )
                }

                AnimatedVisibility(visible = step3Expanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = "Check off each piece of evidence as you secure it before deleting any records:",
                            fontSize = 12.5.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        val checklistItems = listOf(
                            checkScreenshot to { checkScreenshot = !checkScreenshot } to "Screenshot of the suspicious message / chat / post",
                            checkUtr to { checkUtr = !checkUtr } to "Transaction ID or UTR number from bank SMS/statement",
                            checkDateTime to { checkDateTime = !checkDateTime } to "Exact date, time, and timezone of the transaction",
                            checkPhoneUpi to { checkPhoneUpi = !checkPhoneUpi } to "Phone number, email, or UPI ID used by the fraudster",
                            checkAppName to { checkAppName = !checkAppName } to "Name of any application, APK, or website involved",
                            checkCallRecording to { checkCallRecording = !checkCallRecording } to "Call recording or call history log if available"
                        )

                        checklistItems.forEach { (pair, label) ->
                            val (isChecked, onToggle) = pair
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onToggle() }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { onToggle() },
                                    colors = CheckboxDefaults.colors(checkedColor = SurakshaBlue),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    color = if (isChecked) SafeGreen else TextPrimary,
                                    fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }

        // STEP 4: File a Cybercrime Complaint (Expandable)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("step_4_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { step4Expanded = !step4Expanded },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = SurakshaBlue,
                            shape = CircleShape,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("4", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = Translations.get("help_step4_title", language),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaNavy
                        )
                    }
                    Icon(
                        imageVector = if (step4Expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = null,
                        tint = TextMuted
                    )
                }

                AnimatedVisibility(visible = step4Expanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = "1. Visit the National Cyber Crime Portal at https://cybercrime.gov.in/.\n2. Click on 'Report Financial Fraud' or 'Report Other Cyber Crime'.\n3. Register with your active Indian mobile number to generate a tracking token.\n4. Upload the evidence gathered in Step 3 (screenshots, UTR numbers, chat logs).\n5. You can also visit your nearest local police station or district Cyber Crime Police Station with printed copies of your bank statement and evidence checklist.",
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cybercrime.gov.in/"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SurakshaNavy),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("btn_file_complaint")
                        ) {
                            Icon(Icons.Filled.OpenInBrowser, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Open cybercrime.gov.in to File Complaint")
                        }
                    }
                }
            }
        }

        // Disclaimer Card at bottom
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("report_disclaimer_card")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Info, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Official Protocol Notice",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = Translations.get("help_disclaimer", language),
                    fontSize = 11.5.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
