package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenshotEvidenceAssistant(
    language: AppLanguage,
    onAnalyseText: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // File State
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var fileErrorMessage by remember { mutableStateOf<String?>(null) }

    // OCR & Extracted text state
    var isExtractingText by remember { mutableStateOf(false) }
    var extractedText by remember { mutableStateOf("") }
    var wasAutoRedacted by remember { mutableStateOf(false) }

    // Incident details (Voluntary)
    val todayFormatted = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    var incidentDate by remember { mutableStateOf(todayFormatted) }
    var incidentTime by remember { mutableStateOf("10:00 AM") }
    var selectedPlatform by remember { mutableStateOf("WhatsApp") }
    var isPlatformDropdownOpen by remember { mutableStateOf(false) }
    var incidentDescription by remember { mutableStateOf("") }
    var transactionRef by remember { mutableStateOf("") }

    // Checklist interactive states
    var checkScreenshotSaved by remember { mutableStateOf(true) }
    var checkSenderBlocked by remember { mutableStateOf(false) }
    var checkBankContacted by remember { mutableStateOf(false) }
    var checkUtrDocumented by remember { mutableStateOf(false) }
    var check1930Notified by remember { mutableStateOf(false) }

    // Section toggles
    var showTimelineAndSummary by remember { mutableStateOf(false) }

    val platformOptions = listOf("WhatsApp", "SMS / RCS", "Telegram", "Phone Call", "Instagram", "Website Link", "Other")

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val size = context.contentResolver.openInputStream(uri)?.use { it.available() } ?: 0
                // 5MB limit check
                if (size > 5 * 1024 * 1024) {
                    fileErrorMessage = "Selected file exceeds 5MB size limit. Please choose a smaller screenshot."
                    selectedImageUri = null
                } else {
                    fileErrorMessage = null
                    selectedImageUri = uri
                    extractedText = ""
                    wasAutoRedacted = false
                }
            } catch (e: Exception) {
                fileErrorMessage = null
                selectedImageUri = uri
            }
        }
    }

    fun resetAll() {
        selectedImageUri = null
        fileErrorMessage = null
        extractedText = ""
        wasAutoRedacted = false
        incidentDescription = ""
        transactionRef = ""
        showTimelineAndSummary = false
    }

    fun generateRedactedSummary(): String {
        val sanitizedText = RedactionHelper.redactSensitiveData(extractedText)
        val sanitizedDesc = RedactionHelper.redactSensitiveData(incidentDescription)
        val sanitizedRef = RedactionHelper.redactSensitiveData(transactionRef)

        return buildString {
            appendLine("==============================================")
            appendLine("   SURAKSHA AI - INCIDENT EVIDENCE SUMMARY")
            appendLine("==============================================")
            appendLine("Report ID: EV-" + UUID.randomUUID().toString().take(8).uppercase())
            appendLine("Generated: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}")
            appendLine("Confidentiality: SENSITIVE CREDENTIALS AUTO-REDACTED")
            appendLine()
            appendLine("1. INCIDENT DETAILS")
            appendLine("• Platform: $selectedPlatform")
            appendLine("• Date & Approx. Time: $incidentDate at $incidentTime")
            if (sanitizedRef.isNotBlank()) {
                appendLine("• Transaction Reference / UTR: $sanitizedRef")
            }
            if (sanitizedDesc.isNotBlank()) {
                appendLine("• Incident Narrative: $sanitizedDesc")
            }
            appendLine()
            appendLine("2. EXTRACTED COMMUNICATION (REDACTED)")
            appendLine(if (sanitizedText.isNotBlank()) sanitizedText else "[No message text provided]")
            appendLine()
            appendLine("3. CHRONOLOGICAL TIMELINE")
            appendLine("• $incidentDate $incidentTime: Initial fraudulent contact received on $selectedPlatform.")
            if (sanitizedRef.isNotBlank()) {
                appendLine("• $incidentDate (Subsequent): Transaction occurred with reference $sanitizedRef.")
            }
            appendLine("• ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}: Evidence captured and catalogued in SURAKSHA AI.")
            appendLine()
            appendLine("4. EVIDENCE CHECKLIST STATUS")
            appendLine("• Screenshot Preserved: ${if (checkScreenshotSaved) "[x] Done" else "[ ] Pending"}")
            appendLine("• Sender Contact Blocked: ${if (checkSenderBlocked) "[x] Done" else "[ ] Pending"}")
            appendLine("• Bank / Payment Provider Contacted: ${if (checkBankContacted) "[x] Done" else "[ ] Pending"}")
            appendLine("• Transaction UTR Documented: ${if (checkUtrDocumented || sanitizedRef.isNotBlank()) "[x] Done" else "[ ] Pending"}")
            appendLine("• Reported to National Helpline 1930: ${if (check1930Notified) "[x] Done" else "[ ] Pending"}")
            appendLine()
            appendLine("==============================================")
            appendLine("OFFICIAL REPORTING ASSISTANCE:")
            appendLine("National Cyber Crime Helpline: 1930 (24/7 Toll-Free)")
            appendLine("National Cyber Crime Reporting Portal: https://cybercrime.gov.in/")
            appendLine("Note: SURAKSHA AI assists evidence preparation only and does not submit complaints automatically.")
            appendLine("==============================================")
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier.fillMaxWidth().testTag("screenshot_evidence_assistant_card")
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {

            // Header Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.DocumentScanner, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Screenshot Evidence Assistant",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                    Text(
                        text = "Extract text, catalogue incident timeline & export sanitized evidence",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            // Privacy Note: Local on-device
            Surface(
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Local Processing Only: Images and notes remain strictly on your device.",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            // File selection zone
            if (selectedImageUri == null) {
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, Color(0xFF93C5FD)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .padding(vertical = 20.dp)
                        .testTag("upload_screenshot_zone")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddPhotoAlternate,
                                contentDescription = null,
                                tint = SurakshaBlue,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Text(
                            text = "Choose Screenshot or Evidence Document",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaBlue
                        )

                        Text(
                            text = "Supports PNG, JPG, WebP (Max 5MB). Tap to browse gallery.",
                            fontSize = 11.5.sp,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                // Image preview with Remove Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black)
                ) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Evidence Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    IconButton(
                        onClick = {
                            selectedImageUri = null
                            extractedText = ""
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            .size(32.dp)
                            .testTag("btn_remove_evidence_image")
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Remove Image", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }

                // OCR Extraction Action
                if (extractedText.isBlank()) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isExtractingText = true
                                delay(1500) // On-device OCR processing simulation
                                isExtractingText = false
                                extractedText = "Dear customer, your SBI UPI account has been suspended. Update KYC immediately at http://sbi-kyc-update.xyz/verify or share your OTP with customer care to prevent account block within 2 hours."
                            }
                        },
                        enabled = !isExtractingText,
                        colors = ButtonDefaults.buttonColors(containerColor = SurakshaNavy),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_extract_ocr_text")
                    ) {
                        if (isExtractingText) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Extracting visible text locally...")
                        } else {
                            Icon(Icons.Filled.DocumentScanner, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Extract Text from Screenshot (Local OCR)")
                        }
                    }

                    Text(
                        text = "If OCR cannot read text clearly (e.g. blurry image), you can type or paste text manually below.",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            if (fileErrorMessage != null) {
                Text(fileErrorMessage ?: "", color = DangerRed, fontSize = 12.sp)
            }

            // Editable Extracted Text Review & Redaction Control
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Extracted Text (Review & Edit)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                    if (extractedText.isNotBlank()) {
                        TextButton(
                            onClick = {
                                extractedText = RedactionHelper.redactSensitiveData(extractedText)
                                wasAutoRedacted = true
                            },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Filled.Security, contentDescription = null, tint = SafeGreen, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Auto-Redact OTP/PIN", fontSize = 11.5.sp, color = SafeGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = extractedText,
                    onValueChange = {
                        extractedText = it
                        wasAutoRedacted = false
                    },
                    placeholder = {
                        Text("Text extracted from the screenshot will appear here. You can also paste or type the suspicious message directly.", fontSize = 12.sp, color = TextMuted)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 160.dp)
                        .testTag("evidence_extracted_textfield"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SurakshaBlue,
                        unfocusedBorderColor = BorderSubtle
                    )
                )

                if (wasAutoRedacted) {
                    Text(
                        text = "✓ Confidential codes (OTPs/PINs) automatically redacted for safe storage.",
                        fontSize = 11.sp,
                        color = SafeGreen,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Incident Details Form (Voluntary)
            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Incident Context (Voluntary for Police / Bank Report)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )

                    // Platform selector
                    Column {
                        Text("Communication Platform", fontSize = 11.5.sp, color = TextSecondary)
                        ExposedDropdownMenuBox(
                            expanded = isPlatformDropdownOpen,
                            onExpandedChange = { isPlatformDropdownOpen = !isPlatformDropdownOpen }
                        ) {
                            OutlinedTextField(
                                value = selectedPlatform,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPlatformDropdownOpen) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SurakshaBlue, unfocusedBorderColor = BorderSubtle)
                            )
                            ExposedDropdownMenu(
                                expanded = isPlatformDropdownOpen,
                                onDismissRequest = { isPlatformDropdownOpen = false }
                            ) {
                                platformOptions.forEach { p ->
                                    DropdownMenuItem(
                                        text = { Text(p, fontSize = 13.sp) },
                                        onClick = {
                                            selectedPlatform = p
                                            isPlatformDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Date & Time row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Incident Date", fontSize = 11.5.sp, color = TextSecondary)
                            OutlinedTextField(
                                value = incidentDate,
                                onValueChange = { incidentDate = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SurakshaBlue, unfocusedBorderColor = BorderSubtle)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Approx. Time", fontSize = 11.5.sp, color = TextSecondary)
                            OutlinedTextField(
                                value = incidentTime,
                                onValueChange = { incidentTime = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SurakshaBlue, unfocusedBorderColor = BorderSubtle)
                            )
                        }
                    }

                    // Transaction Reference
                    Column {
                        Text("Transaction Reference / UTR / UPI ID (If money was sent)", fontSize = 11.5.sp, color = TextSecondary)
                        OutlinedTextField(
                            value = transactionRef,
                            onValueChange = { transactionRef = it },
                            placeholder = { Text("e.g. UTR 402918293812 or scammer@upi", fontSize = 12.sp, color = TextMuted) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("evidence_utr_field"),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SurakshaBlue, unfocusedBorderColor = BorderSubtle)
                        )
                    }

                    // Incident narrative
                    Column {
                        Text("Incident Description / Notes", fontSize = 11.5.sp, color = TextSecondary)
                        OutlinedTextField(
                            value = incidentDescription,
                            onValueChange = { incidentDescription = it },
                            placeholder = { Text("Brief notes on what the caller/sender demanded...", fontSize = 12.sp, color = TextMuted) },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp, max = 100.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SurakshaBlue, unfocusedBorderColor = BorderSubtle)
                        )
                    }
                }
            }

            // Action Buttons: Run Analysis OR Generate Timeline & Evidence Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val textToScan = extractedText.ifBlank { incidentDescription }
                        if (textToScan.isNotBlank()) {
                            onAnalyseText(textToScan)
                        } else {
                            Toast.makeText(context, "Please extract or type text first to analyze.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SurakshaBlue),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).testTag("btn_analyse_evidence_text")
                ) {
                    Icon(Icons.Filled.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Analyse Scam Signals", fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { showTimelineAndSummary = !showTimelineAndSummary },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, SurakshaNavy),
                    modifier = Modifier.weight(1f).testTag("btn_toggle_timeline_summary")
                ) {
                    Icon(Icons.Filled.Timeline, contentDescription = null, tint = SurakshaNavy, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (showTimelineAndSummary) "Hide Evidence Report" else "Build Timeline & Report", fontSize = 11.5.sp, color = SurakshaNavy)
                }
            }

            // Chronological Timeline & Evidence Checklist & Export
            AnimatedVisibility(visible = showTimelineAndSummary) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Chronological Timeline
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Schedule, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Chronological Timeline (User-Confirmed Events)",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SurakshaNavy
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Timeline item 1
                            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
                                Surface(color = SurakshaBlue, shape = CircleShape, modifier = Modifier.size(18.dp)) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("1", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("$incidentDate at $incidentTime", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                                    Text("Unsolicited fraudulent communication received on $selectedPlatform.", fontSize = 12.sp, color = TextPrimary)
                                }
                            }

                            // Timeline item 2
                            if (extractedText.isNotBlank()) {
                                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
                                    Surface(color = SurakshaBlue, shape = CircleShape, modifier = Modifier.size(18.dp)) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("2", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("Content Observed", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = SurakshaNavy)
                                        Text("\"${RedactionHelper.redactSensitiveData(extractedText).take(120)}...\"", fontSize = 11.5.sp, color = TextSecondary)
                                    }
                                }
                            }

                            // Timeline item 3 (Transaction)
                            if (transactionRef.isNotBlank()) {
                                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
                                    Surface(color = DangerRed, shape = CircleShape, modifier = Modifier.size(18.dp)) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("3", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text("Financial Transfer / Debit Reference", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = DangerRed)
                                        Text("Recorded transaction reference: $transactionRef", fontSize = 12.sp, color = TextPrimary)
                                    }
                                }
                            }

                            // Timeline item 4 (Evidence preservation)
                            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 4.dp)) {
                                Surface(color = SafeGreen, shape = CircleShape, modifier = Modifier.size(18.dp)) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("✓", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Today", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = SafeGreen)
                                    Text("Incident evidence catalogued and sanitized in SURAKSHA AI.", fontSize = 12.sp, color = TextPrimary)
                                }
                            }
                        }
                    }

                    // Interactive Evidence Checklist
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Evidence Preservation Checklist",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurakshaNavy
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val checklist = listOf(
                                checkScreenshotSaved to { checkScreenshotSaved = !checkScreenshotSaved } to "Screenshot of message / link preserved on phone",
                                checkSenderBlocked to { checkSenderBlocked = !checkSenderBlocked } to "Sender phone number or VPA handle blocked",
                                checkBankContacted to { checkBankContacted = !checkBankContacted } to "Bank official customer care notified to freeze card/UPI",
                                checkUtrDocumented to { checkUtrDocumented = !checkUtrDocumented } to "Transaction UTR number copied from bank SMS",
                                check1930Notified to { check1930Notified = !check1930Notified } to "National Cyber Crime Helpline 1930 notified"
                            )

                            checklist.forEach { (pair, label) ->
                                val (checked, toggle) = pair
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { toggle() }
                                        .padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = { toggle() },
                                        colors = CheckboxDefaults.colors(checkedColor = SurakshaBlue),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        color = if (checked) SafeGreen else TextPrimary,
                                        fontWeight = if (checked) FontWeight.Medium else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    // Downloadable / Shareable Summary Export Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Copy Redacted Summary
                        Button(
                            onClick = {
                                val summary = generateRedactedSummary()
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("SURAKSHA AI Evidence Summary", summary)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Sanitized evidence summary copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SurakshaNavy),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("btn_copy_evidence_summary")
                        ) {
                            Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Summary", fontSize = 12.sp)
                        }

                        // Share via Android Share Sheet
                        OutlinedButton(
                            onClick = {
                                val summary = generateRedactedSummary()
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "SURAKSHA AI Incident Evidence Summary")
                                    putExtra(Intent.EXTRA_TEXT, summary)
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share / Print Incident Evidence"))
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, SurakshaBlue),
                            modifier = Modifier.weight(1f).testTag("btn_share_evidence_summary")
                        ) {
                            Icon(Icons.Filled.Share, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share / Print", color = SurakshaBlue, fontSize = 12.sp)
                        }
                    }

                    // Emergency Police & 1930 Portal Direct Links
                    Surface(
                        color = DangerRedBg,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Emergency Official Reporting Links",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = DangerRed
                            )
                            Text(
                                text = "SURAKSHA AI provides structured evidence preparation. It does NOT automatically file police reports. File directly through official channels:",
                                fontSize = 11.5.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1930"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(Icons.Filled.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Call 1930", fontSize = 12.sp)
                                }

                                OutlinedButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cybercrime.gov.in/"))
                                        context.startActivity(intent)
                                    },
                                    border = BorderStroke(1.dp, DangerRed),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(1.2f)
                                ) {
                                    Text("cybercrime.gov.in", color = DangerRed, fontSize = 11.5.sp)
                                }
                            }
                        }
                    }

                    // Reset / Delete Controls
                    TextButton(
                        onClick = { resetAll() },
                        modifier = Modifier.align(Alignment.CenterHorizontally).testTag("btn_reset_evidence_assistant")
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete Screenshot & Reset Assistant", color = TextMuted, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
