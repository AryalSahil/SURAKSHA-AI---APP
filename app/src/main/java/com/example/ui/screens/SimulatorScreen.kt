package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*

private enum class SimulatorViewMode {
    SCENARIO_LIST,
    ACTIVE_SIMULATION,
    RESULTS_REPORT
}

@Composable
fun SimulatorScreen(
    language: AppLanguage,
    completedScenarios: Set<String>,
    onScenarioCompleted: (String) -> Unit,
    onNavigateToLesson: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var viewMode by remember { mutableStateOf(SimulatorViewMode.SCENARIO_LIST) }
    var activeScenario by remember { mutableStateOf<Scenario?>(null) }
    var currentStepId by remember { mutableStateOf<String?>(null) }
    var userDecisions by remember { mutableStateOf<List<SimulationDecisionRecord>>(emptyList()) }
    var simulationResult by remember { mutableStateOf<SimulationResult?>(null) }

    // State within active step
    var selectedOptionForStep by remember { mutableStateOf<ScenarioOption?>(null) }
    var showRedFlagsExpanded by remember { mutableStateOf(true) }

    // Intercept back button when inside active simulation or results
    BackHandler(enabled = viewMode != SimulatorViewMode.SCENARIO_LIST) {
        if (viewMode == SimulatorViewMode.ACTIVE_SIMULATION || viewMode == SimulatorViewMode.RESULTS_REPORT) {
            viewMode = SimulatorViewMode.SCENARIO_LIST
            activeScenario = null
            currentStepId = null
            selectedOptionForStep = null
            userDecisions = emptyList()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurakshaBackground)
    ) {
        when (viewMode) {
            SimulatorViewMode.SCENARIO_LIST -> {
                SimulatorScenarioListView(
                    language = language,
                    completedScenarios = completedScenarios,
                    onSelectScenario = { scenario ->
                        activeScenario = scenario
                        currentStepId = scenario.initialStepId
                        userDecisions = emptyList()
                        selectedOptionForStep = null
                        showRedFlagsExpanded = true
                        viewMode = SimulatorViewMode.ACTIVE_SIMULATION
                    }
                )
            }
            SimulatorViewMode.ACTIVE_SIMULATION -> {
                val scenario = activeScenario
                val step = scenario?.steps?.get(currentStepId)

                if (scenario != null && step != null) {
                    ActiveSimulationView(
                        language = language,
                        scenario = scenario,
                        step = step,
                        selectedOption = selectedOptionForStep,
                        showRedFlags = showRedFlagsExpanded,
                        onToggleRedFlags = { showRedFlagsExpanded = !showRedFlagsExpanded },
                        onSelectOption = { option ->
                            selectedOptionForStep = option
                        },
                        onProceed = {
                            val option = selectedOptionForStep
                            if (option != null) {
                                val newDecisions = userDecisions + SimulationDecisionRecord(
                                    stepNumber = step.stepNumber,
                                    promptText = step.promptMessage.text,
                                    selectedOption = option
                                )
                                userDecisions = newDecisions

                                val nextStep = option.nextStepId
                                if (nextStep != null && scenario.steps.containsKey(nextStep)) {
                                    currentStepId = nextStep
                                    selectedOptionForStep = null
                                    showRedFlagsExpanded = true
                                } else {
                                    // Simulation ended, calculate result
                                    val result = SimulatorEngine.calculateResult(scenario, newDecisions)
                                    simulationResult = result
                                    onScenarioCompleted(scenario.id)
                                    viewMode = SimulatorViewMode.RESULTS_REPORT
                                }
                            }
                        },
                        onExit = {
                            viewMode = SimulatorViewMode.SCENARIO_LIST
                            activeScenario = null
                        }
                    )
                } else {
                    viewMode = SimulatorViewMode.SCENARIO_LIST
                }
            }
            SimulatorViewMode.RESULTS_REPORT -> {
                simulationResult?.let { result ->
                    SimulationResultsReportView(
                        language = language,
                        result = result,
                        onReplay = {
                            activeScenario?.let { sc ->
                                currentStepId = sc.initialStepId
                                userDecisions = emptyList()
                                selectedOptionForStep = null
                                showRedFlagsExpanded = true
                                viewMode = SimulatorViewMode.ACTIVE_SIMULATION
                            }
                        },
                        onLearnMore = {
                            onNavigateToLesson(result.relatedLessonId)
                        },
                        onReturnToList = {
                            viewMode = SimulatorViewMode.SCENARIO_LIST
                            activeScenario = null
                        },
                        onNavigateToHome = onNavigateToHome
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 1. SCENARIO LISTING VIEW
// -------------------------------------------------------------
@Composable
private fun SimulatorScenarioListView(
    language: AppLanguage,
    completedScenarios: Set<String>,
    onSelectScenario: (Scenario) -> Unit
) {
    val scenarios = SimulatorData.scenarios
    var selectedCategoryFilter by remember { mutableStateOf<ScenarioCategory?>(null) }

    val filteredScenarios = remember(selectedCategoryFilter) {
        if (selectedCategoryFilter == null) scenarios
        else scenarios.filter { it.category == selectedCategoryFilter }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("simulator_scenario_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurakshaNavy),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("simulator_hero_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(SurakshaNavy, Color(0xFF1E4E8C))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Forum,
                                        contentDescription = null,
                                        tint = Color(0xFF60A5FA),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = Translations.get("sim_header_title", language),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    letterSpacing = 0.3.sp
                                )
                                Text(
                                    text = "Interactive Citizen Cyber Training",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFFBFDBFE)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = Translations.get("sim_header_sub", language),
                            fontSize = 13.5.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress Stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                color = Color.White.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "${completedScenarios.size} / ${scenarios.size}",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = Translations.get("sim_stat_completed", language),
                                        fontSize = 11.sp,
                                        color = Color(0xFFBFDBFE)
                                    )
                                }
                            }

                            Surface(
                                color = Color.White.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1.3f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (completedScenarios.isNotEmpty()) Icons.Filled.Verified else Icons.Filled.Shield,
                                        contentDescription = null,
                                        tint = if (completedScenarios.isNotEmpty()) Color(0xFF34D399) else Color(0xFFFCD34D),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (completedScenarios.isNotEmpty()) "Scam-Aware" else "In Training",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Learner Badge",
                                            fontSize = 10.5.sp,
                                            color = Color(0xFFBFDBFE)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Fictional Simulation Safety Notice
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = SurakshaBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "100% fictional & safe training environment. Learn how to recognize and dismantle scam attempts through realistic chat simulations.",
                        fontSize = 12.sp,
                        color = SurakshaNavy,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedCategoryFilter == null,
                    onClick = { selectedCategoryFilter = null },
                    label = { Text("All", fontSize = 12.sp) }
                )
                FilterChip(
                    selected = selectedCategoryFilter == ScenarioCategory.FAKE_JOB,
                    onClick = { selectedCategoryFilter = ScenarioCategory.FAKE_JOB },
                    label = { Text("Jobs", fontSize = 12.sp) }
                )
                FilterChip(
                    selected = selectedCategoryFilter == ScenarioCategory.BANK_IMPERSONATION,
                    onClick = { selectedCategoryFilter = ScenarioCategory.BANK_IMPERSONATION },
                    label = { Text("Banking", fontSize = 12.sp) }
                )
                FilterChip(
                    selected = selectedCategoryFilter == ScenarioCategory.UPI_FRAUD,
                    onClick = { selectedCategoryFilter = ScenarioCategory.UPI_FRAUD },
                    label = { Text("UPI", fontSize = 12.sp) }
                )
            }
        }

        // Scenario Cards List
        items(filteredScenarios, key = { it.id }) { scenario ->
            val isCompleted = completedScenarios.contains(scenario.id)
            ScenarioCardItem(
                scenario = scenario,
                isCompleted = isCompleted,
                onStart = { onSelectScenario(scenario) }
            )
        }
    }
}

@Composable
private fun ScenarioCardItem(
    scenario: Scenario,
    isCompleted: Boolean,
    onStart: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onStart() }
            .testTag("scenario_card_${scenario.id}")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categoryIcon = when (scenario.category) {
                        ScenarioCategory.FAKE_JOB -> Icons.Filled.WorkOutline
                        ScenarioCategory.BANK_IMPERSONATION -> Icons.Filled.AccountBalance
                        ScenarioCategory.UPI_FRAUD -> Icons.Filled.QrCodeScanner
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = categoryIcon,
                            contentDescription = null,
                            tint = SurakshaBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = scenario.category.displayName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SurakshaBlue
                    )
                }

                if (isCompleted) {
                    Surface(
                        color = Color(0xFFECFDF5),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = SafeGreen,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Completed",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SafeGreen
                            )
                        }
                    }
                }
            }

            Text(
                text = scenario.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Text(
                text = scenario.shortDescription,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 18.sp
            )

            // Metadata badges (Difficulty + Time)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Difficulty: ${scenario.difficulty.label}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "~${scenario.estimatedMinutes} mins",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onStart,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SurakshaNavy),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_start_scenario_${scenario.id}")
            ) {
                Text(
                    text = if (isCompleted) "Replay Simulation" else "Start Simulation",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.5.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 2. ACTIVE CHAT SIMULATION VIEW
// -------------------------------------------------------------
@Composable
private fun ActiveSimulationView(
    language: AppLanguage,
    scenario: Scenario,
    step: ScenarioStep,
    selectedOption: ScenarioOption?,
    showRedFlags: Boolean,
    onToggleRedFlags: () -> Unit,
    onSelectOption: (ScenarioOption) -> Unit,
    onProceed: () -> Unit,
    onExit: () -> Unit
) {
    val listState = rememberLazyListState()

    // Smoothly scroll down when an option is selected to reveal feedback
    LaunchedEffect(selectedOption) {
        if (selectedOption != null) {
            listState.animateScrollToItem(listState.layoutInfo.totalItemsCount.coerceAtLeast(1) - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("active_simulation_container")
    ) {
        // Top Navigation Bar for Simulation
        Surface(
            color = Color.White,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onExit,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_exit_simulation")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Exit Simulation",
                            tint = SurakshaNavy
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = scenario.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaNavy,
                            maxLines = 1
                        )
                        Text(
                            text = "Step ${step.stepNumber} of ${step.totalSteps}",
                            fontSize = 11.5.sp,
                            color = TextSecondary
                        )
                    }

                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "${((step.stepNumber.toFloat() / step.totalSteps.toFloat()) * 100).toInt()}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaBlue,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { step.stepNumber.toFloat() / step.totalSteps.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = SurakshaBlue,
                    trackColor = Color(0xFFE2E8F0)
                )
            }
        }

        // Chat & Choice Area
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Fictional Simulation Disclaimer Note
            item {
                Surface(
                    color = Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Shield,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Fictional Simulation: Safe training exercise. Never share real credentials.",
                            fontSize = 11.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }

            // Incoming Message Bubble
            item {
                ScammerChatBubble(message = step.promptMessage)
            }

            // "Why This Is Suspicious" Red Flags Panel
            if (step.redFlagsAtThisStep.isNotEmpty()) {
                item {
                    RedFlagsPanel(
                        redFlags = step.redFlagsAtThisStep,
                        isExpanded = showRedFlags,
                        onToggle = onToggleRedFlags
                    )
                }
            }

            // Response Selection Prompt
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.QuestionAnswer,
                        contentDescription = null,
                        tint = SurakshaNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = Translations.get("sim_your_response", language),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                }
            }

            // Multiple-Choice Options
            items(step.options, key = { it.id }) { option ->
                val isSelected = selectedOption?.id == option.id
                OptionChoiceCard(
                    option = option,
                    isSelected = isSelected,
                    isEnabled = selectedOption == null || isSelected,
                    onSelect = { onSelectOption(option) }
                )
            }

            // Instant Feedback Banner (Displayed after user makes a choice)
            if (selectedOption != null) {
                item {
                    FeedbackBanner(option = selectedOption)
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Bottom Continue / Proceed Bar
        Surface(
            color = Color.White,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = onProceed,
                    enabled = selectedOption != null,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOption?.isSafe == true) SurakshaNavy else CautionAmber,
                        disabledContainerColor = Color(0xFFE2E8F0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_proceed_next_step")
                ) {
                    val isLastStep = step.stepNumber >= step.totalSteps
                    Text(
                        text = if (isLastStep) "View Safety Results & Report" else "Continue to Next Step",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (selectedOption == null) TextMuted else Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// CHAT BUBBLE & COMPONENT HELPERS
// -------------------------------------------------------------
@Composable
private fun ScammerChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("chat_bubble_scammer"),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (message.speaker == SpeakerType.SYSTEM_NOTE) SurakshaNavy else DangerRed),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (message.speaker == SpeakerType.SYSTEM_NOTE) Icons.Filled.Security else Icons.Filled.Warning,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = message.speakerName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (message.speaker == SpeakerType.SYSTEM_NOTE) SurakshaNavy else DangerRed
                )
                Text(
                    text = message.timestampText,
                    fontSize = 10.5.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = RoundedCornerShape(topStart = 2.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, if (message.isSuspiciousFlagged) Color(0xFFFECACA) else BorderSubtle),
                shadowElevation = 1.dp
            ) {
                Text(
                    text = message.text,
                    fontSize = 13.5.sp,
                    color = TextPrimary,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }
    }
}

@Composable
private fun RedFlagsPanel(
    redFlags: List<RedFlagItem>,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
        border = BorderStroke(1.dp, Color(0xFFFECACA)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("red_flags_panel")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.ErrorOutline,
                        contentDescription = null,
                        tint = DangerRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Why This Is Suspicious (${redFlags.size} Red Flags)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DangerRed
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = DangerRed,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    redFlags.forEach { flag ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                text = "• ",
                                fontWeight = FontWeight.Bold,
                                color = DangerRed,
                                fontSize = 14.sp
                            )
                            Column {
                                Text(
                                    text = flag.title,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DangerRed
                                )
                                Text(
                                    text = flag.description,
                                    fontSize = 11.5.sp,
                                    color = TextSecondary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionChoiceCard(
    option: ScenarioOption,
    isSelected: Boolean,
    isEnabled: Boolean,
    onSelect: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected && option.isSafe -> Color(0xFFF0FDF4)
                isSelected && !option.isSafe -> Color(0xFFFEF2F2)
                else -> Color.White
            }
        ),
        border = BorderStroke(
            width = if (isSelected) 1.8.dp else 1.dp,
            color = when {
                isSelected && option.isSafe -> SafeGreen
                isSelected && !option.isSafe -> DangerRed
                else -> BorderSubtle
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) { onSelect() }
            .testTag("option_card_${option.id}")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isSelected && option.isSafe -> SafeGreen
                            isSelected && !option.isSafe -> DangerRed
                            else -> Color(0xFFE2E8F0)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = if (option.isSafe) Icons.Filled.Check else Icons.Filled.Close,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = option.text,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = TextPrimary,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun FeedbackBanner(option: ScenarioOption) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (option.isSafe) Color(0xFFECFDF5) else Color(0xFFFEF2F2),
        border = BorderStroke(1.2.dp, if (option.isSafe) Color(0xFFA7F3D0) else Color(0xFFFECACA)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("feedback_banner")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (option.isSafe) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                    contentDescription = null,
                    tint = if (option.isSafe) SafeGreen else DangerRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (option.isSafe) "Safe Decision!" else "Risky Move!",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (option.isSafe) SafeGreen else DangerRed
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = option.feedbackText,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = option.explanation,
                fontSize = 11.5.sp,
                color = TextSecondary,
                lineHeight = 16.sp
            )
        }
    }
}

// -------------------------------------------------------------
// 3. FINAL RESULTS & SAFETY REPORT VIEW
// -------------------------------------------------------------
@Composable
private fun SimulationResultsReportView(
    language: AppLanguage,
    result: SimulationResult,
    onReplay: () -> Unit,
    onLearnMore: () -> Unit,
    onReturnToList: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("simulation_results_view"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Results Header Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = if (result.passed) SurakshaNavy else Color(0xFF7F1D1D)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("results_score_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Simulation Safety Report",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFBFDBFE)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${result.score} / 100",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Badge announcement
                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (result.passed) Icons.Filled.MilitaryTech else Icons.Filled.Shield,
                                contentDescription = null,
                                tint = if (result.passed) Color(0xFFFCD34D) else Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (result.passed) "Badge Earned: Scam-Aware Learner" else "Needs Review — High Vulnerability",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = result.disclaimer,
                        fontSize = 10.5.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Correct Decisions List
        if (result.correctDecisions.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth().testTag("card_correct_decisions")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = SafeGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Smart Decisions Made (${result.correctDecisions.size})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        result.correctDecisions.forEach { dec ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "✓ ", color = SafeGreen, fontWeight = FontWeight.Bold)
                                Text(
                                    text = dec.selectedOption.text,
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Mistakes & Missed Warning Signs
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth().testTag("card_mistakes")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (result.mistakes.isEmpty()) Icons.Filled.Verified else Icons.Filled.Warning,
                            contentDescription = null,
                            tint = if (result.mistakes.isEmpty()) SafeGreen else DangerRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (result.mistakes.isEmpty()) "Flawless Performance" else "Warning Signs Missed (${result.mistakes.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (result.mistakes.isEmpty()) SafeGreen else DangerRed
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (result.mistakes.isEmpty()) {
                        Text(
                            text = "You detected every warning sign, avoided artificial pressure traps, and chose the safest real-world responses.",
                            fontSize = 12.5.sp,
                            color = TextSecondary,
                            lineHeight = 17.sp
                        )
                    } else {
                        result.mistakes.forEach { mist ->
                            Surface(
                                color = Color(0xFFFEF2F2),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    mist.selectedOption.mistakeTag?.let { tag ->
                                        Text(
                                            text = tag,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DangerRed
                                        )
                                    }
                                    Text(
                                        text = mist.selectedOption.text,
                                        fontSize = 12.sp,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = mist.selectedOption.explanation,
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Recommended Safest Action & Common Trap
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth().testTag("card_safest_action")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = null,
                                tint = SurakshaBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Recommended Safest Real-World Action",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = SurakshaNavy
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = result.safestAction,
                            fontSize = 12.5.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }

                    HorizontalDivider(color = BorderSubtle)

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.HighlightOff,
                                contentDescription = null,
                                tint = DangerRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Common Mistake to Avoid",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = DangerRed
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = result.commonMistakeToAvoid,
                            fontSize = 12.5.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Real-World Safety Checklist
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth().testTag("card_safety_checklist")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Real-World Safety Checklist",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    result.safetyChecklist.forEach { item ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = SurakshaBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item,
                                fontSize = 12.sp,
                                color = TextPrimary,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }

        // Action Buttons: Replay, Learn More, Return
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onReplay,
                    colors = ButtonDefaults.buttonColors(containerColor = SurakshaNavy),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_replay_scenario")
                ) {
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Replay Scenario", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                }

                OutlinedButton(
                    onClick = onLearnMore,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SurakshaBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_learn_more_lesson")
                ) {
                    Icon(imageVector = Icons.Filled.MenuBook, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Read Full Fraud Defense Lesson", color = SurakshaBlue, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onReturnToList,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_all_scenarios")
                    ) {
                        Text("All Scenarios", fontSize = 12.5.sp)
                    }

                    OutlinedButton(
                        onClick = onNavigateToHome,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_back_to_dashboard")
                    ) {
                        Text("Dashboard", fontSize = 12.5.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
