package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.Lesson
import com.example.data.LessonsData
import com.example.data.Translations
import com.example.ui.theme.*

@Composable
fun LearnScreen(
    language: AppLanguage,
    completedLessons: Set<String>,
    onLessonCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedLesson by remember { mutableStateOf<Lesson?>(null) }

    // If viewing a lesson detail, handle back to return to list
    if (selectedLesson != null) {
        BackHandler {
            selectedLesson = null
        }
        LessonDetailView(
            lesson = selectedLesson!!,
            language = language,
            isCompleted = completedLessons.contains(selectedLesson!!.id),
            onComplete = {
                onLessonCompleted(selectedLesson!!.id)
            },
            onBack = { selectedLesson = null }
        )
        return
    }

    val filteredLessons = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            LessonsData.lessons
        } else {
            val q = searchQuery.lowercase()
            LessonsData.lessons.filter {
                it.title.lowercase().contains(q) ||
                it.shortDescription.lowercase().contains(q) ||
                it.category.lowercase().contains(q)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurakshaBackground)
            .padding(16.dp)
    ) {
        // Header
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
            Text(
                text = Translations.get("learn_title", language),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SurakshaNavy
            )
            Text(
                text = "${completedLessons.size} of ${LessonsData.lessons.size} modules mastered",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(Translations.get("learn_search_placeholder", language), fontSize = 13.sp, color = TextMuted)
            },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = null, tint = TextMuted)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Filled.Close, contentDescription = "Clear", tint = TextMuted)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SurakshaBlue,
                unfocusedBorderColor = BorderSubtle,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag("learn_search_bar")
        )

        // Lessons List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize().testTag("lessons_list")
        ) {
            items(filteredLessons, key = { it.id }) { lesson ->
                val isCompleted = completedLessons.contains(lesson.id)
                LessonCardItem(
                    lesson = lesson,
                    isCompleted = isCompleted,
                    onClick = { selectedLesson = lesson }
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun LessonCardItem(
    lesson: Lesson,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, if (isCompleted) SafeGreenBorder else BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("lesson_card_${lesson.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = lesson.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SurakshaBlue,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${lesson.durationMinutes} min read",
                        fontSize = 11.5.sp,
                        color = TextMuted
                    )

                    if (isCompleted) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = SafeGreenBg,
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Quiz Completed",
                                tint = SafeGreen,
                                modifier = Modifier.size(16.dp).padding(2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = lesson.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SurakshaNavy
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = lesson.shortDescription,
                fontSize = 12.5.sp,
                color = TextSecondary,
                lineHeight = 17.sp,
                maxLines = 2
            )
        }
    }
}

@Composable
fun LessonDetailView(
    lesson: Lesson,
    language: AppLanguage,
    isCompleted: Boolean,
    onComplete: () -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Quiz State
    val userAnswers = remember { mutableStateMapOf<Int, Int>() }
    var quizSubmitted by remember { mutableStateOf(false) }

    val score = remember(quizSubmitted) {
        if (!quizSubmitted) 0
        else {
            lesson.quizQuestions.count { q ->
                userAnswers[q.id] == q.correctIndex
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurakshaBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Back Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back_lesson")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = SurakshaNavy)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Surface(
                color = Color(0xFFEFF6FF),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = lesson.category,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SurakshaBlue,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${lesson.durationMinutes} min read",
                fontSize = 12.sp,
                color = TextMuted
            )
        }

        // Lesson Title Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SurakshaNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = lesson.title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = lesson.shortDescription,
                    fontSize = 13.sp,
                    color = Color(0xFFDCE9FF),
                    lineHeight = 18.sp
                )
            }
        }

        // 1. What is this scam?
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "What is this scam?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = SurakshaNavy
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lesson.whatIsThis,
                    fontSize = 13.5.sp,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )
            }
        }

        // 2. Fictional Scenario
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7).copy(alpha = 0.5f)),
            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.AutoStories, contentDescription = null, tint = CautionAmberText, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "A Fictional Example",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CautionAmberText
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lesson.fictionalExample,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    lineHeight = 19.sp
                )
            }
        }

        // 3. Warning Signs
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Warning, contentDescription = null, tint = DangerRed, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Warning Signs",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                lesson.warningSigns.forEach { sign ->
                    Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                        Text("•", color = DangerRed, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(text = sign, fontSize = 13.sp, color = TextPrimary, lineHeight = 18.sp)
                    }
                }
            }
        }

        // 4. What to Do
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = SafeGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "What to Do",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                lesson.whatToDo.forEachIndexed { i, action ->
                    Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                        Text("${i + 1}.", color = SafeGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(text = action, fontSize = 13.sp, color = TextPrimary, lineHeight = 18.sp)
                    }
                }
            }
        }

        // 5. What NOT to Do
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Cancel, contentDescription = null, tint = DangerRed, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "What NOT to Do",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                lesson.whatNotToDo.forEach { notAction ->
                    Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                        Text("✕", color = DangerRed, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(text = notAction, fontSize = 13.sp, color = DangerRed, lineHeight = 18.sp)
                    }
                }
            }
        }

        // 6. Interactive Knowledge Quiz Section
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
            border = BorderStroke(1.2.dp, BorderMedium),
            modifier = Modifier.fillMaxWidth().testTag("quiz_section")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Quiz, contentDescription = null, tint = SurakshaBlue, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Knowledge Check Quiz (3 Questions)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurakshaNavy
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                lesson.quizQuestions.forEachIndexed { qIdx, question ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Q${qIdx + 1}: ${question.question}",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        question.options.forEachIndexed { optIdx, option ->
                            val isSelected = userAnswers[question.id] == optIdx
                            val isCorrect = optIdx == question.correctIndex

                            val backgroundColor = when {
                                !quizSubmitted && isSelected -> Color(0xFFEFF6FF)
                                quizSubmitted && isCorrect -> SafeGreenBg
                                quizSubmitted && isSelected && !isCorrect -> DangerRedBg
                                else -> Color.White
                            }

                            val borderColor = when {
                                !quizSubmitted && isSelected -> SurakshaBlue
                                quizSubmitted && isCorrect -> SafeGreen
                                quizSubmitted && isSelected && !isCorrect -> DangerRed
                                else -> BorderSubtle
                            }

                            Surface(
                                color = backgroundColor,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, borderColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable(enabled = !quizSubmitted) {
                                        userAnswers[question.id] = optIdx
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            if (!quizSubmitted) userAnswers[question.id] = optIdx
                                        },
                                        enabled = !quizSubmitted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = option,
                                        fontSize = 12.5.sp,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }

                        // Explanation after submission
                        if (quizSubmitted) {
                            Surface(
                                color = Color(0xFFF8FAFC),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            ) {
                                Text(
                                    text = question.explanation,
                                    fontSize = 11.5.sp,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (!quizSubmitted) {
                    val allAnswered = lesson.quizQuestions.all { userAnswers.containsKey(it.id) }
                    Button(
                        onClick = {
                            quizSubmitted = true
                            onComplete()
                        },
                        enabled = allAnswered,
                        colors = ButtonDefaults.buttonColors(containerColor = SurakshaBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_submit_quiz")
                    ) {
                        Text(if (allAnswered) "Submit Quiz Answers" else "Answer All 3 Questions to Submit")
                    }
                } else {
                    // Quiz Result & Score
                    Surface(
                        color = if (score >= 2) SafeGreenBg else Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Your Score: $score / ${lesson.quizQuestions.size}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (score >= 2) SafeGreen else CautionAmberText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (score == 3) "Outstanding! You have mastered this safety topic."
                                       else if (score == 2) "Good job! Review the explanations above to achieve a perfect score."
                                       else "Keep practicing! Review the warning signs and retake the quiz.",
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            userAnswers.clear()
                            quizSubmitted = false
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(Translations.get("learn_retake_quiz", language), color = SurakshaNavy)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
