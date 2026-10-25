package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SurakshaColorScheme = lightColorScheme(
    primary = SurakshaNavy,
    onPrimary = SurakshaSurface,
    primaryContainer = SurakshaNavy,
    onPrimaryContainer = SurakshaSurface,
    secondary = SurakshaBlue,
    onSecondary = SurakshaSurface,
    secondaryContainer = InfoBlueBg,
    onSecondaryContainer = InfoBlueText,
    tertiary = SafeGreen,
    onTertiary = SurakshaSurface,
    background = SurakshaBackground,
    onBackground = TextPrimary,
    surface = SurakshaSurface,
    onSurface = TextPrimary,
    surfaceVariant = SurakshaSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BorderMedium,
    outlineVariant = BorderSubtle,
    error = DangerRed,
    onError = SurakshaSurface,
    errorContainer = DangerRedBg,
    onErrorContainer = DangerRed
)

@Composable
fun SurakshaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = SurakshaNavy.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = SurakshaColorScheme,
        typography = Typography,
        content = content
    )
}
