package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.SampleData
import com.example.data.ScamCategory
import com.example.data.Translations
import com.example.ui.components.NavTab
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    language: AppLanguage,
    onNavigateToScamChecker: (subTab: Int, category: ScamCategory?) -> Unit,
    onNavigateToTab: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var tipIndex by remember { mutableIntStateOf(0) }
    val currentTip = SampleData.safetyTips[tipIndex % SampleData.safetyTips.size]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurakshaBackground)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Hero Header: Logo & Tagline
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SurakshaNavy),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("home_hero_card")
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
                                Image(
                                    painter = painterResource(R.drawable.ic_app_logo),
                                    contentDescription = "Suraksha AI Logo",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = Translations.get("app_name", language),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Citizen Cyber Defense Utility",
                                fontSize = 11.5.sp,
                                color = Color(0xFFBFDBFE)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "\"${Translations.get("tagline", language)}\"",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        lineHeight = 22.sp
                    )
                }
            }
        }

        // Emergency Card: Call 1930 immediately
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
            border = BorderStroke(1.2.dp, Color(0xFFFECACA)),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_emergency_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(DangerRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PhoneInTalk,
                            contentDescription = "Emergency Phone",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = Translations.get("home_emergency_title", language),
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = DangerRed,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = Translations.get("home_emergency_sub", language),
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1930"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).testTag("btn_dial_1930")
                    ) {
                        Icon(Icons.Filled.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call 1930", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cybercrime.gov.in/"))
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, DangerRed),
                        modifier = Modifier.weight(1.3f).testTag("btn_visit_cybercrime")
                    ) {
                        Text("cybercrime.gov.in", color = DangerRed, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }

        // Two Prominent Action Cards
        Text(
            text = "Instant Scam Scanners",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SurakshaNavy
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Check a Suspicious Message Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToScamChecker(0, null) }
                    .testTag("action_check_message")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Chat,
                            contentDescription = null,
                            tint = SurakshaBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = Translations.get("home_action_message", language),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        lineHeight = 18.sp
                    )

                    Text(
                        text = Translations.get("home_action_message_sub", language),
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Check SMS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SurakshaBlue
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = SurakshaBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Check a Website Link Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BorderSubtle),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToScamChecker(1, null) }
                    .testTag("action_check_link")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Link,
                            contentDescription = null,
                            tint = SurakshaBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = Translations.get("home_action_link", language),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        lineHeight = 18.sp
                    )

                    Text(
                        text = Translations.get("home_action_link_sub", language),
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "Check URL",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SurakshaBlue
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = SurakshaBlue,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Scam Conversation Simulator Banner Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.2.dp, Color(0xFFBBF7D0)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToTab(NavTab.SIMULATOR) }
                .testTag("home_action_simulator")
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFDCFCE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Forum,
                        contentDescription = null,
                        tint = SafeGreen,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Scam Simulator",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaNavy
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = SafeGreen,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "INTERACTIVE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Practice responding to fake recruiters, bank OTP scams & UPI traps in realistic fictional chats.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = SafeGreen,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Scam Category Chips (Tapping navigates to Scam Checker with that category pre-selected)
        Column {
            Text(
                text = Translations.get("home_category_title", language),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SurakshaNavy
            )
            Spacer(modifier = Modifier.height(10.dp))

            val chips = listOf(
                ScamCategory.FAKE_JOB to "Fake Jobs",
                ScamCategory.LOAN_OFFER to "Loan Offers",
                ScamCategory.UPI_FRAUD to "UPI Fraud",
                ScamCategory.PHISHING to "Phishing",
                ScamCategory.FAKE_CUSTOMER_CARE to "Fake Customer Care",
                ScamCategory.OTP_SECURITY to "OTP Scams"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                chips.forEach { (cat, label) ->
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .clickable { onNavigateToScamChecker(0, cat) }
                            .testTag("home_category_chip_${cat.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = null,
                                tint = SurakshaBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = label,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Digital Safety Tip of the Day Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BorderSubtle),
            modifier = Modifier.fillMaxWidth().testTag("safety_tip_card")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Lightbulb,
                            contentDescription = null,
                            tint = CautionAmber,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = Translations.get("home_tip_title", language),
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurakshaNavy
                        )
                    }

                    IconButton(
                        onClick = { tipIndex++ },
                        modifier = Modifier.size(28.dp).testTag("btn_next_tip")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Next Tip",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentTip,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 19.sp
                )
            }
        }

        // Quick Link Buttons to Learn and Report & Help
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { onNavigateToTab(NavTab.LEARN) },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SurakshaNavy),
                modifier = Modifier.weight(1f).testTag("quick_link_learn")
            ) {
                Icon(Icons.Filled.School, contentDescription = null, modifier = Modifier.size(16.dp), tint = SurakshaNavy)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = Translations.get("home_quick_learn", language),
                    color = SurakshaNavy,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }

            OutlinedButton(
                onClick = { onNavigateToTab(NavTab.REPORT_HELP) },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SurakshaBlue),
                modifier = Modifier.weight(1f).testTag("quick_link_report")
            ) {
                Icon(Icons.Filled.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp), tint = SurakshaBlue)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = Translations.get("home_quick_report", language),
                    color = SurakshaBlue,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
