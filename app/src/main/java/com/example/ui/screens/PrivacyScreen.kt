package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PrivacyScreen(
    onBack: () -> Unit,
    onOpenFeedback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurakshaBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Back Row
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_privacy")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SurakshaNavy)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Privacy Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SurakshaNavy
            )
        }

        // Section 1: What SURAKSHA AI Processes
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("privacy_section_1")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.DataUsage, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("1. What SURAKSHA AI Processes", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                }
                Spacer(modifier = Modifier.height(10.dp))
                val items = listOf(
                    "Text you submit for analysis (SMS, WhatsApp text, email content)",
                    "URLs and domains you submit for web security checking",
                    "Images you upload for screenshot analysis (processed strictly locally on your device in this version)",
                    "Feedback you voluntarily submit through our in-app form",
                    "Your chosen language preference (stored locally on your device)"
                )
                items.forEach {
                    Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                        Text("•", color = SurakshaBlue, fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(it, fontSize = 13.sp, color = TextPrimary, lineHeight = 18.sp)
                    }
                }
            }
        }

        // Section 2: What is Sent to External Services
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("privacy_section_2")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CloudUpload, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("2. What is Sent to External Services", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "• When AI-assisted analysis is enabled, the text or URL you submit is sent to an external AI provider (Google Gemini API) for detailed explanations. This provider processes your submission according to their privacy policy.\n• We do not attach your name, phone number, device IMEI, or other identifying information to analysis requests.\n• Important safety rule: Do not submit messages containing your own sensitive information such as personal passwords, bank account numbers, or Aadhaar numbers.",
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 19.sp
                )
            }
        }

        // Section 3: What is Stored
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("privacy_section_3")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Save, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("3. What is Stored", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "• Language preference: Stored locally on your device only.\n• Quiz progress: Stored locally on your device only.\n• Feedback submissions: Stored locally in device storage (and sent securely when cloud feedback is enabled).\n• Analysis history: Not stored by default in the current version.",
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 19.sp
                )
            }
        }

        // Section 4: Your Choices
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("privacy_section_4")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Tune, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("4. Your Choices", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "• You can use rule-based analysis without AI by declining AI consent at any time.\n• You can clear all locally stored data, quiz progress, and preferences with one tap in Settings.",
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 19.sp
                )
            }
        }

        // Section 5: Contact
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("privacy_section_5")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ContactMail, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("5. Contact & Privacy Questions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "If you have questions about privacy or how data is processed by SURAKSHA AI, please use our in-app feedback form.",
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onOpenFeedback,
                    colors = ButtonDefaults.buttonColors(containerColor = SurakshaNavy),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Open Feedback Form")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
