package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class UserPreferencesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("suraksha_prefs", Context.MODE_PRIVATE)

    private val _language = MutableStateFlow(AppLanguage.fromCode(prefs.getString("pref_language", "en") ?: "en"))
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _completedLessons = MutableStateFlow(prefs.getStringSet("pref_completed_lessons", emptySet())?.toSet() ?: emptySet())
    val completedLessons: StateFlow<Set<String>> = _completedLessons.asStateFlow()

    private val _completedScenarios = MutableStateFlow(prefs.getStringSet("pref_completed_scenarios", emptySet())?.toSet() ?: emptySet())
    val completedScenarios: StateFlow<Set<String>> = _completedScenarios.asStateFlow()

    private val _aiConsentEnabled = MutableStateFlow(prefs.getBoolean("pref_ai_consent", false))
    val aiConsentEnabled: StateFlow<Boolean> = _aiConsentEnabled.asStateFlow()

    private val _hasSeenAiModal = MutableStateFlow(prefs.getBoolean("pref_has_seen_ai_modal", false))
    val hasSeenAiModal: StateFlow<Boolean> = _hasSeenAiModal.asStateFlow()

    private val _feedbackItems = MutableStateFlow(loadFeedback())
    val feedbackItems: StateFlow<List<FeedbackItem>> = _feedbackItems.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        prefs.edit().putString("pref_language", lang.code).apply()
        _language.value = lang
    }

    fun markLessonCompleted(lessonId: String) {
        val current = _completedLessons.value.toMutableSet()
        current.add(lessonId)
        prefs.edit().putStringSet("pref_completed_lessons", current).apply()
        _completedLessons.value = current
    }

    fun markScenarioCompleted(scenarioId: String) {
        val current = _completedScenarios.value.toMutableSet()
        current.add(scenarioId)
        prefs.edit().putStringSet("pref_completed_scenarios", current).apply()
        _completedScenarios.value = current
    }

    fun setAiConsent(enabled: Boolean) {
        prefs.edit()
            .putBoolean("pref_ai_consent", enabled)
            .putBoolean("pref_has_seen_ai_modal", true)
            .apply()
        _aiConsentEnabled.value = enabled
        _hasSeenAiModal.value = true
    }

    fun saveFeedback(item: FeedbackItem) {
        val current = _feedbackItems.value.toMutableList()
        current.add(0, item)
        saveFeedbackList(current)
        _feedbackItems.value = current
    }

    private fun loadFeedback(): List<FeedbackItem> {
        val jsonStr = prefs.getString("pref_feedback_json", null) ?: return emptyList()
        return try {
            val array = JSONArray(jsonStr)
            val list = mutableListOf<FeedbackItem>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    FeedbackItem(
                        id = obj.optString("id"),
                        category = obj.optString("category"),
                        description = obj.optString("description"),
                        contactEmail = obj.optString("email").takeIf { it.isNotEmpty() },
                        relatedAnalysisId = obj.optString("analysisId").takeIf { it.isNotEmpty() },
                        timestamp = obj.optLong("timestamp")
                    )
                )
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveFeedbackList(list: List<FeedbackItem>) {
        try {
            val array = JSONArray()
            for (item in list) {
                val obj = JSONObject()
                obj.put("id", item.id)
                obj.put("category", item.category)
                obj.put("description", item.description)
                obj.put("email", item.contactEmail ?: "")
                obj.put("analysisId", item.relatedAnalysisId ?: "")
                obj.put("timestamp", item.timestamp)
                array.put(obj)
            }
            prefs.edit().putString("pref_feedback_json", array.toString()).apply()
        } catch (_: Exception) {
        }
    }

    fun clearAllData() {
        prefs.edit().clear().apply()
        _language.value = AppLanguage.EN
        _completedLessons.value = emptySet()
        _completedScenarios.value = emptySet()
        _aiConsentEnabled.value = false
        _hasSeenAiModal.value = false
        _feedbackItems.value = emptyList()
    }
}
