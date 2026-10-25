package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.data.*
import com.example.ui.components.*
import com.example.ui.screens.*

enum class SubScreen {
    NONE,
    PRIVACY,
    FEEDBACK
}

@Composable
fun SurakshaApp(
    preferencesRepo: UserPreferencesRepository? = null
) {
    val context = LocalContext.current.applicationContext
    val repo = preferencesRepo ?: remember { UserPreferencesRepository(context) }
    val language by repo.language.collectAsState()
    val completedLessons by repo.completedLessons.collectAsState()
    val completedScenarios by repo.completedScenarios.collectAsState()
    val aiConsentEnabled by repo.aiConsentEnabled.collectAsState()
    val hasSeenAiModal by repo.hasSeenAiModal.collectAsState()

    var currentTab by remember { mutableStateOf(NavTab.HOME) }
    var currentSubScreen by remember { mutableStateOf(SubScreen.NONE) }

    // Pre-selection params for Scam Checker
    var checkerInitialTab by remember { mutableIntStateOf(0) }
    var checkerInitialCategory by remember { mutableStateOf<ScamCategory?>(null) }

    var showLanguageModal by remember { mutableStateOf(false) }
    var showAiConsentModal by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Back handling
    BackHandler(enabled = currentSubScreen != SubScreen.NONE || currentTab != NavTab.HOME) {
        if (currentSubScreen != SubScreen.NONE) {
            currentSubScreen = SubScreen.NONE
        } else if (currentTab != NavTab.HOME) {
            currentTab = NavTab.HOME
        }
    }

    // Trigger AI Consent modal first time Scam Checker is visited
    LaunchedEffect(currentTab) {
        if (currentTab == NavTab.SCAM_CHECKER && !hasSeenAiModal) {
            showAiConsentModal = true
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                currentLanguage = language,
                onLanguageClick = { showLanguageModal = true }
            )
        },
        bottomBar = {
            if (currentSubScreen == SubScreen.NONE) {
                BottomNavBar(
                    currentTab = currentTab,
                    onTabSelected = { tab ->
                        currentTab = tab
                        currentSubScreen = SubScreen.NONE
                    },
                    language = language
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentSubScreen) {
                SubScreen.PRIVACY -> {
                    PrivacyScreen(
                        onBack = { currentSubScreen = SubScreen.NONE },
                        onOpenFeedback = { currentSubScreen = SubScreen.FEEDBACK }
                    )
                }
                SubScreen.FEEDBACK -> {
                    FeedbackScreen(
                        onBack = { currentSubScreen = SubScreen.NONE },
                        onSubmitFeedback = { item ->
                            repo.saveFeedback(item)
                        }
                    )
                }
                SubScreen.NONE -> {
                    when (currentTab) {
                        NavTab.HOME -> {
                            HomeScreen(
                                language = language,
                                onNavigateToScamChecker = { subTab, category ->
                                    checkerInitialTab = subTab
                                    checkerInitialCategory = category
                                    currentTab = NavTab.SCAM_CHECKER
                                },
                                onNavigateToTab = { tab -> currentTab = tab }
                            )
                        }
                        NavTab.SCAM_CHECKER -> {
                            ScamCheckerScreen(
                                language = language,
                                onLanguageChange = { repo.setLanguage(it) },
                                initialTab = checkerInitialTab,
                                initialCategory = checkerInitialCategory
                            )
                        }
                        NavTab.SIMULATOR -> {
                            SimulatorScreen(
                                language = language,
                                completedScenarios = completedScenarios,
                                onScenarioCompleted = { scenarioId ->
                                    repo.markScenarioCompleted(scenarioId)
                                },
                                onNavigateToLesson = { lessonId ->
                                    currentTab = NavTab.LEARN
                                },
                                onNavigateToHome = {
                                    currentTab = NavTab.HOME
                                }
                            )
                        }
                        NavTab.LEARN -> {
                            LearnScreen(
                                language = language,
                                completedLessons = completedLessons,
                                onLessonCompleted = { lessonId ->
                                    repo.markLessonCompleted(lessonId)
                                }
                            )
                        }
                        NavTab.REPORT_HELP -> {
                            ReportHelpScreen(language = language)
                        }
                        NavTab.SETTINGS -> {
                            SettingsScreen(
                                language = language,
                                onLanguageClick = { showLanguageModal = true },
                                aiConsentEnabled = aiConsentEnabled,
                                onChangeAiConsent = { showAiConsentModal = true },
                                onOpenPrivacy = { currentSubScreen = SubScreen.PRIVACY },
                                onOpenFeedback = { currentSubScreen = SubScreen.FEEDBACK },
                                onClearData = { repo.clearAllData() }
                            )
                        }
                    }
                }
            }
        }
    }

    // Language Selector Modal Bottom Sheet
    if (showLanguageModal) {
        LanguageSelectorModal(
            currentLanguage = language,
            onLanguageSelected = { lang ->
                repo.setLanguage(lang)
            },
            onDismiss = { showLanguageModal = false }
        )
    }

    // AI-Assisted Analysis Consent Modal
    if (showAiConsentModal) {
        AiConsentModal(
            onEnableAi = {
                repo.setAiConsent(true)
                showAiConsentModal = false
            },
            onUseBasicCheck = {
                repo.setAiConsent(false)
                showAiConsentModal = false
            },
            onDismiss = {
                repo.setAiConsent(false)
                showAiConsentModal = false
            }
        )
    }
}
