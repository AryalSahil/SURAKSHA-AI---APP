package com.example.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.Translations
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.SurakshaBlue
import com.example.ui.theme.TextMuted

enum class NavTab(val id: String, val translationKey: String) {
    HOME("home", "nav_home"),
    SCAM_CHECKER("scam_checker", "nav_scam_checker"),
    SIMULATOR("simulator", "nav_simulator"),
    LEARN("learn", "nav_learn"),
    REPORT_HELP("report_help", "nav_report_help"),
    SETTINGS("settings", "nav_settings")
}

@Composable
fun BottomNavBar(
    currentTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .height(80.dp)
            .testTag("bottom_nav_bar"),
        containerColor = Color.White,
        tonalElevation = 6.dp
    ) {
        val items = listOf(
            NavTab.HOME to Icons.Filled.Home,
            NavTab.SCAM_CHECKER to Icons.Filled.Security,
            NavTab.SIMULATOR to Icons.Filled.Forum,
            NavTab.LEARN to Icons.Filled.MenuBook,
            NavTab.REPORT_HELP to Icons.Filled.PhoneInTalk,
            NavTab.SETTINGS to Icons.Filled.Settings
        )

        items.forEach { (tab, icon) ->
            val isSelected = currentTab == tab
            val label = Translations.get(tab.translationKey, language)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SurakshaBlue,
                    selectedTextColor = SurakshaBlue,
                    indicatorColor = Color(0xFFEFF6FF),
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("nav_tab_${tab.id}")
            )
        }
    }
}
