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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FeedbackItem
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    onBack: () -> Unit,
    onSubmitFeedback: (FeedbackItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val categories = listOf(
        "Suggest a Feature",
        "Report a Bug",
        "Incorrect Analysis Result",
        "Translation Issue",
        "Other Feedback"
    )

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var isCategoryDropdownOpen by remember { mutableStateOf(false) }

    var description by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var analysisId by remember { mutableStateOf("") }

    var descriptionError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    var isSubmitting by remember { mutableStateOf(false) }
    var isSubmittedSuccess by remember { mutableStateOf(false) }

    val dynamicPlaceholder = when (selectedCategory) {
        "Report a Bug" -> "Describe what happened, what screen you were on, and what you expected to happen..."
        "Suggest a Feature" -> "Describe the safety or detection feature you would like to see in SURAKSHA AI..."
        "Incorrect Analysis Result" -> "Describe the message or link you tested and why the risk rating seemed inaccurate..."
        "Translation Issue" -> "Tell us which language and screen contained incorrect or confusing terminology..."
        else -> "Please share your suggestions or thoughts with us..."
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurakshaBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back row
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_feedback")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SurakshaNavy)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Send Feedback",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SurakshaNavy
            )
        }

        if (isSubmittedSuccess) {
            // Success State
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SafeGreenBg),
                border = BorderStroke(1.5.dp, SafeGreenBorder),
                modifier = Modifier.fillMaxWidth().testTag("feedback_success_card")
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Success",
                        tint = SafeGreen,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Thank you for your feedback.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafeGreen
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "We will review your submission carefully to strengthen SURAKSHA AI for all citizens.",
                        fontSize = 13.sp,
                        color = TextPrimary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = SafeGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Return to Settings")
                    }
                }
            }
        } else {
            // Form Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth().testTag("feedback_form_card")
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Category Selector
                    Column {
                        Text("Feedback Category *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                        Spacer(modifier = Modifier.height(6.dp))
                        ExposedDropdownMenuBox(
                            expanded = isCategoryDropdownOpen,
                            onExpandedChange = { isCategoryDropdownOpen = !isCategoryDropdownOpen }
                        ) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryDropdownOpen) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SurakshaBlue,
                                    unfocusedBorderColor = BorderSubtle
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = isCategoryDropdownOpen,
                                onDismissRequest = { isCategoryDropdownOpen = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat, fontSize = 13.sp) },
                                        onClick = {
                                            selectedCategory = cat
                                            isCategoryDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Description textarea
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Description *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                            Text("${description.length} / 1000", fontSize = 11.sp, color = if (description.length < 20 && description.isNotEmpty()) DangerRed else TextMuted)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = {
                                if (it.length <= 1000) {
                                    description = it
                                    if (it.length >= 20) descriptionError = null
                                }
                            },
                            placeholder = { Text(dynamicPlaceholder, fontSize = 12.5.sp, color = TextMuted) },
                            isError = descriptionError != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp, max = 180.dp)
                                .testTag("feedback_description_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SurakshaBlue,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )
                        if (descriptionError != null) {
                            Text(descriptionError ?: "", color = DangerRed, fontSize = 11.5.sp, modifier = Modifier.padding(top = 4.dp))
                        } else {
                            Text("Minimum 20 characters required.", fontSize = 11.sp, color = TextMuted, modifier = Modifier.padding(top = 4.dp))
                        }
                    }

                    // Contact Email (Optional)
                    Column {
                        Text("Contact Email (Optional)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                        Text("Only if you would like a direct response", fontSize = 11.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                emailError = null
                            },
                            placeholder = { Text("e.g. name@example.com", fontSize = 12.5.sp, color = TextMuted) },
                            singleLine = true,
                            isError = emailError != null,
                            modifier = Modifier.fillMaxWidth().testTag("feedback_email_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SurakshaBlue,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )
                        if (emailError != null) {
                            Text(emailError ?: "", color = DangerRed, fontSize = 11.5.sp, modifier = Modifier.padding(top = 4.dp))
                        }
                    }

                    // Related Analysis ID (Optional)
                    Column {
                        Text("Related Analysis ID (Optional)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                        Text("Found on the risk assessment card (e.g. AN-XXXXXXXX)", fontSize = 11.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = analysisId,
                            onValueChange = { analysisId = it },
                            placeholder = { Text("Paste analysis session ID if relevant", fontSize = 12.5.sp, color = TextMuted) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("feedback_analysis_id_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SurakshaBlue,
                                unfocusedBorderColor = BorderSubtle
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            var hasError = false
                            if (description.trim().length < 20) {
                                descriptionError = "Description must be at least 20 characters."
                                hasError = true
                            }
                            if (email.isNotBlank()) {
                                val emailPattern = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}${'$'}""")
                                if (!emailPattern.matches(email.trim())) {
                                    emailError = "Please enter a valid email format."
                                    hasError = true
                                }
                            }
                            if (!hasError) {
                                coroutineScope.launch {
                                    isSubmitting = true
                                    delay(800) // Brief processing delay
                                    val item = FeedbackItem(
                                        id = "FB-" + UUID.randomUUID().toString().take(8).uppercase(),
                                        category = selectedCategory,
                                        description = description.trim(),
                                        contactEmail = email.trim().takeIf { it.isNotBlank() },
                                        relatedAnalysisId = analysisId.trim().takeIf { it.isNotBlank() }
                                    )
                                    onSubmitFeedback(item)
                                    isSubmitting = false
                                    isSubmittedSuccess = true
                                }
                            }
                        },
                        enabled = !isSubmitting,
                        colors = ButtonDefaults.buttonColors(containerColor = SurakshaNavy),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("btn_submit_feedback")
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Submitting...")
                        } else {
                            Text("Submit Feedback", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(
                        text = "Privacy note: Your feedback is used only to improve SURAKSHA AI. We do not share it with third parties. Never enter OTPs or passwords.",
                        fontSize = 11.sp,
                        color = TextMuted,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
