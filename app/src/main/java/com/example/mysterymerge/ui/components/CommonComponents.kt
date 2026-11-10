package com.example.mysterymerge.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mysterymerge.data.StoryEvent
import com.example.mysterymerge.model.Screen
import com.example.mysterymerge.ui.theme.*

@Composable
fun TopBarWallet(
    energy: Int,
    maxEnergy: Int,
    coins: Int,
    gems: Int,
    energyTimerText: String = "02:16",
    onSettingsClick: () -> Unit,
    onEnergyClick: () -> Unit = {},
    onCoinsClick: () -> Unit = {},
    onGemsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Tagline at the very top right
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Small clues. Bigger truths.",
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic,
                color = MysteryGoldDark,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Transparent,
                    border = BorderStroke(2.dp, MysteryGold),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "🔍",
                            fontSize = 14.sp
                        )
                    }
                }
                Column {
                    Text(
                        text = "MYSTERY",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = MysteryGold,
                        letterSpacing = 1.sp,
                        lineHeight = 15.sp
                    )
                    Text(
                        text = "MERGE",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = MysteryGold,
                        letterSpacing = 1.5.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            // Resource Counters
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Energy with timer
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ResourcePill(
                        symbol = "⚡",
                        symbolColor = Color(0xFF38B6FF),
                        value = "$energy/$maxEnergy",
                        onClick = onEnergyClick,
                        testTag = "wallet_energy"
                    )
                    Text(
                        text = "⏱ $energyTimerText",
                        fontSize = 8.sp,
                        color = MysteryMuted,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Coins
                ResourcePill(
                    symbol = "🪙",
                    symbolColor = MysteryGold,
                    value = formatCommas(coins),
                    onClick = onCoinsClick,
                    testTag = "wallet_coins"
                )

                // Gems
                ResourcePill(
                    symbol = "💎",
                    symbolColor = MysteryGemPurple,
                    value = "$gems",
                    onClick = onGemsClick,
                    testTag = "wallet_gems"
                )

                // Settings Gear
                Surface(
                    onClick = onSettingsClick,
                    shape = CircleShape,
                    color = Color(0xFF0D2530),
                    border = BorderStroke(1.dp, MysteryTealBright.copy(alpha = 0.7f)),
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("top_settings_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "⚙", fontSize = 16.sp, color = MysteryCream)
                    }
                }
            }
        }
    }
}

@Composable
fun ResourcePill(
    symbol: String,
    symbolColor: Color,
    value: String,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF09202A),
        border = BorderStroke(1.2.dp, Color(0xFF225B6A)),
        modifier = modifier.testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(start = 5.dp, end = 2.dp, top = 2.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(text = symbol, fontSize = 11.sp, color = symbolColor)
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MysteryCream
            )
            // Plus icon inside pill
            Surface(
                shape = CircleShape,
                color = Color(0xFF144755),
                border = BorderStroke(0.8.dp, Color(0xFF3DA3B8)),
                modifier = Modifier.size(16.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "+", fontSize = 10.sp, fontWeight = FontWeight.Black, color = MysteryCream)
                }
            }
        }
    }
}

@Composable
fun ResourceChip(
    symbol: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MysteryNavyLight,
        border = BorderStroke(1.dp, MysteryGoldDark.copy(alpha = 0.6f)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = symbol,
                fontSize = 11.sp,
                color = color
            )
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MysteryCream
            )
        }
    }
}

@Composable
fun BottomNavBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    hasCaseNotifications: Boolean = true,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF081820),
        border = BorderStroke(1.dp, Color(0xFF1A3B49)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavButton(
                icon = "▦",
                label = "Board",
                isSelected = currentScreen == Screen.BOARD,
                onClick = { onNavigate(Screen.BOARD) },
                testTag = "nav_board_button"
            )
            NavButton(
                icon = "📁",
                label = "Cases",
                isSelected = currentScreen == Screen.CASES,
                hasNotification = hasCaseNotifications,
                onClick = { onNavigate(Screen.CASES) },
                testTag = "nav_cases_button"
            )
            NavButton(
                icon = "🏪",
                label = "Shop",
                isSelected = currentScreen == Screen.SHOP,
                onClick = { onNavigate(Screen.SHOP) },
                testTag = "nav_shop_button"
            )
        }
    }
}

@Composable
fun NavButton(
    icon: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    hasNotification: Boolean = false
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) Color(0xFF102F3A) else Color.Transparent,
        border = if (isSelected) BorderStroke(1.5.dp, MysteryGold) else null,
        modifier = Modifier
            .testTag(testTag)
            .width(100.dp)
            .height(52.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 18.sp,
                    color = if (isSelected) MysteryGold else MysteryMuted
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MysteryCream else MysteryMuted
                )
            }

            if (hasNotification && !isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 6.dp, end = 26.dp)
                        .size(8.dp)
                        .background(Color(0xFFE53935), CircleShape)
                )
            }
        }
    }
}

@Composable
fun StoryRevealDialog(
    event: StoryEvent,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MysteryPaper,
            border = BorderStroke(3.dp, MysteryGoldDark),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = if (event.isCaseComplete) "✦ CASE SOLVED ✦" else "✦ EVIDENCE DISCOVERED ✦",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (event.isCaseComplete) MysteryGold else MysteryGoldDark,
                    letterSpacing = 1.sp
                )
                Text(
                    text = if (event.isCaseComplete) event.completedCaseTitle.uppercase() else event.title.uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = MysteryInk,
                    textAlign = TextAlign.Center
                )

                HorizontalDivider(color = MysteryGoldDark.copy(alpha = 0.5f), thickness = 1.dp)

                Text(
                    text = event.storyText,
                    fontSize = 13.sp,
                    color = MysteryInk,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center
                )

                if (event.isCaseComplete) {
                    Text(
                        text = "Next Case Dossier is now Unlocked in the Archives!",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF144D5B),
                        textAlign = TextAlign.Center
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (event.coinReward > 0) {
                        Text(
                            text = "+${event.coinReward} 🪙",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7E5525)
                        )
                    }
                    if (event.gemReward > 0) {
                        Text(
                            text = "+${event.gemReward} 💎",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MysteryTeal
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MysteryTeal,
                        contentColor = MysteryCream
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MysteryGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dismiss_story_dialog_button")
                ) {
                    Text(
                        text = "CONTINUE INVESTIGATION",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

fun formatCommas(value: Int): String {
    return "%,d".format(value)
}
