package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.*
import com.example.ui.components.ResultCardsView
import com.example.ui.components.ScreenshotEvidenceAssistant
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScamCheckerScreen(
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    initialTab: Int = 0,
    initialCategory: ScamCategory? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var selectedTab by remember(initialTab) { mutableIntStateOf(initialTab) }
    var selectedCategory by remember(initialCategory) { mutableStateOf(initialCategory) }

    // Message input state
    var messageText by remember { mutableStateOf("") }
    var isSampleLoaded by remember { mutableStateOf(false) }
    var showSampleDialog by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }

    // Link input state
    var linkUrl by remember { mutableStateOf("") }
    var urlError by remember { mutableStateOf<String?>(null) }

    // Screenshot state
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isExtractingText by remember { mutableStateOf(false) }
    var extractedText by remember { mutableStateOf("") }
    var screenshotError by remember { mutableStateOf<String?>(null) }

    // Analysis state
    var isAnalyzing by remember { mutableStateOf(false) }
    var currentResult by remember { mutableStateOf<AnalysisResult?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            // Check size if possible via content resolver
            try {
                val size = context.contentResolver.openInputStream(uri)?.use { it.available() } ?: 0
                if (size > 5 * 1024 * 1024) {
                    screenshotError = Translations.get("screenshot_size_error", language)
                    selectedImageUri = null
                } else {
                    screenshotError = null
                    selectedImageUri = uri
                    extractedText = ""
                }
            } catch (e: Exception) {
                screenshotError = null
                selectedImageUri = uri
            }
        }
    }

    fun resetForm() {
        messageText = ""
        linkUrl = ""
        urlError = null
        selectedImageUri = null
        extractedText = ""
        isSampleLoaded = false
        currentResult = null
        screenshotError = null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurakshaBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Row: Title & Language Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = Translations.get("nav_scam_checker", language),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SurakshaNavy
                )
                Text(
                    text = "Instant Multi-Signal Threat Detection",
                    fontSize = 11.5.sp,
                    color = TextSecondary
                )
            }

            // Quick language selector inside header
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, BorderSubtle)
            ) {
                Row(modifier = Modifier.padding(2.dp)) {
                    AppLanguage.entries.forEach { lang ->
                        val isSelected = language == lang
                        Surface(
                            color = if (isSelected) SurakshaNavy else Color.Transparent,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .clickable { onLanguageChange(lang) }
                                .testTag("header_lang_${lang.code}")
                        ) {
                            Text(
                                text = lang.displayName.take(3),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else TextSecondary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Three Input Tabs at top: Message / Text, Website Link, Screenshot
        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = SurakshaBlue,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .testTag("checker_tab_row")
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0; currentResult = null },
                text = { Text(Translations.get("checker_tab_message", language), fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold) },
                icon = { Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_message")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1; currentResult = null },
                text = { Text(Translations.get("checker_tab_link", language), fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold) },
                icon = { Icon(Icons.Filled.Link, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_link")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2; currentResult = null },
                text = { Text(Translations.get("checker_tab_screenshot", language), fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold) },
                icon = { Icon(Icons.Filled.Image, contentDescription = null, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.testTag("tab_screenshot")
            )
        }

        // ================= TAB 0: MESSAGE / TEXT =================
        if (selectedTab == 0) {
            // Category selector & Try sample button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category button / dropdown trigger
                OutlinedButton(
                    onClick = { showCategoryMenu = true },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, BorderSubtle),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f).testTag("btn_select_category")
                ) {
                    Icon(Icons.Filled.Category, contentDescription = null, modifier = Modifier.size(16.dp), tint = SurakshaNavy)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = selectedCategory?.let { Translations.get("cat_${it.id}", language) }
                            ?: Translations.get("checker_category_selector_label", language),
                        fontSize = 11.5.sp,
                        color = SurakshaNavy,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Try Sample Button
                Button(
                    onClick = { showSampleDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFF6FF)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_try_sample")
                ) {
                    Icon(Icons.Filled.AutoFixHigh, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Translations.get("checker_try_sample", language),
                        fontSize = 12.sp,
                        color = SurakshaBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Fictional sample disclaimer banner
            if (isSampleLoaded) {
                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth().testTag("sample_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = null, tint = CautionAmberText, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Translations.get("checker_sample_banner", language),
                            fontSize = 11.5.sp,
                            color = CautionAmberText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Textarea Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = {
                            if (it.length <= 3000) {
                                messageText = it
                                isSampleLoaded = false
                            }
                        },
                        placeholder = {
                            Text(
                                text = Translations.get("checker_textarea_placeholder", language),
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp, max = 220.dp)
                            .testTag("checker_textarea"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SurakshaBlue,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = Translations.get("checker_disclaimer", language),
                            fontSize = 10.sp,
                            color = TextMuted,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${messageText.length} / 3000 ${Translations.get("checker_char_counter", language)}",
                            fontSize = 10.sp,
                            color = if (messageText.length > 2900) DangerRed else TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Analyse Now Button
            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        coroutineScope.launch {
                            isAnalyzing = true
                            delay(600) // Brief realistic signal processing simulation
                            currentResult = RuleEngine.analyzeText(messageText, selectedCategory, language)
                            isAnalyzing = false
                            delay(100)
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    }
                },
                enabled = messageText.isNotBlank() && !isAnalyzing,
                colors = ButtonDefaults.buttonColors(containerColor = SurakshaBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_analyse_now")
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(Translations.get("checker_analysing", language), fontSize = 14.sp)
                } else {
                    Icon(Icons.Filled.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(Translations.get("checker_analyse_now", language), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // ================= TAB 1: WEBSITE LINK =================
        if (selectedTab == 1) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Website Link or Domain",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = linkUrl,
                        onValueChange = {
                            if (it.length <= 500) {
                                linkUrl = it
                                urlError = null
                            }
                        },
                        placeholder = {
                            Text(
                                text = Translations.get("link_input_placeholder", language),
                                fontSize = 12.5.sp,
                                color = TextMuted
                            )
                        },
                        singleLine = true,
                        isError = urlError != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("link_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SurakshaBlue,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )

                    if (urlError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = urlError ?: "",
                            fontSize = 11.5.sp,
                            color = DangerRed
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mandatory Safe Guarantee Note
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Shield,
                                contentDescription = null,
                                tint = SurakshaBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = Translations.get("link_warning_safe", language),
                                fontSize = 11.5.sp,
                                color = SurakshaBlue,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Quick URL Samples to test
                    Text(
                        text = "Try sample suspicious links:",
                        fontSize = 11.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SampleData.sampleUrls.take(2).forEach { sample ->
                            OutlinedButton(
                                onClick = { linkUrl = sample; urlError = null },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = sample.removePrefix("http://").removePrefix("https://"),
                                    fontSize = 10.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // Analyse URL Button
            Button(
                onClick = {
                    val trimmed = linkUrl.trim()
                    if (trimmed.isBlank() || (!trimmed.contains(".") && !trimmed.contains("://"))) {
                        urlError = Translations.get("link_error_invalid", language)
                        return@Button
                    }
                    coroutineScope.launch {
                        isAnalyzing = true
                        delay(600)
                        currentResult = RuleEngine.analyzeUrl(trimmed)
                        isAnalyzing = false
                        delay(100)
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                },
                enabled = linkUrl.isNotBlank() && !isAnalyzing,
                colors = ButtonDefaults.buttonColors(containerColor = SurakshaBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_analyse_link")
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(Translations.get("checker_analysing", language), fontSize = 14.sp)
                } else {
                    Icon(Icons.Filled.Link, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Analyse Website Link", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // ================= TAB 2: SCREENSHOT EVIDENCE ASSISTANT =================
        if (selectedTab == 2) {
            ScreenshotEvidenceAssistant(
                language = language,
                onAnalyseText = { textToAnalyze ->
                    coroutineScope.launch {
                        isAnalyzing = true
                        delay(600)
                        currentResult = RuleEngine.analyzeText(textToAnalyze, selectedCategory, language)
                        isAnalyzing = false
                        delay(100)
                        scrollState.animateScrollTo(scrollState.maxValue)
                    }
                }
            )
        }

        // ================= STRUCTURED RESULTS DISPLAY =================
        currentResult?.let { result ->
            Spacer(modifier = Modifier.height(8.dp))
            ResultCardsView(
                result = result,
                language = language,
                onAnalyseAnother = { resetForm() }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }

    // Category Selector Dropdown Dialog
    if (showCategoryMenu) {
        AlertDialog(
            onDismissRequest = { showCategoryMenu = false },
            title = { Text("Select Scam Category", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ScamCategory.entries.forEach { cat ->
                        Surface(
                            color = if (selectedCategory == cat) Color(0xFFEFF6FF) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCategory = cat
                                    showCategoryMenu = false
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = Translations.get("cat_${cat.id}", language),
                                fontSize = 13.5.sp,
                                color = if (selectedCategory == cat) SurakshaBlue else TextPrimary,
                                fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCategoryMenu = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Try Sample Dialog with all 8 categories
    if (showSampleDialog) {
        AlertDialog(
            onDismissRequest = { showSampleDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AutoFixHigh, contentDescription = null, tint = SurakshaBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select a Fictional Scam Sample", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SampleData.samples.forEach { sample ->
                        Surface(
                            color = Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    messageText = sample.text
                                    selectedCategory = sample.category
                                    isSampleLoaded = true
                                    showSampleDialog = false
                                }
                                .testTag("sample_item_${sample.category.id}")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = Translations.get("cat_${sample.category.id}", language),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SurakshaNavy
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = sample.text,
                                    fontSize = 11.5.sp,
                                    color = TextSecondary,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSampleDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
