package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SurakshaBlue
import com.example.ui.theme.SurakshaNavy
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AiConsentModal(
    onEnableAi: () -> Unit,
    onUseBasicCheck: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        icon = {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = SurakshaBlue,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "About AI-Assisted Analysis",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SurakshaNavy,
                modifier = Modifier.testTag("ai_consent_title")
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "To provide detailed explanations, SURAKSHA AI can send your submitted text to an AI service. This text leaves your device. Do not include passwords, PINs, or other sensitive personal information. You can still use basic rule-based checking without enabling AI.",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Security,
                            contentDescription = null,
                            tint = SurakshaBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Privacy first: Rule-based analysis works 100% locally on your phone.",
                            fontSize = 12.sp,
                            color = SurakshaBlue,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onEnableAi,
                colors = ButtonDefaults.buttonColors(containerColor = SurakshaBlue),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("btn_enable_ai")
            ) {
                Text("Enable AI Analysis")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onUseBasicCheck,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("btn_basic_check_only")
            ) {
                Text("Use Basic Check Only", color = SurakshaNavy)
            }
        }
    )
}
