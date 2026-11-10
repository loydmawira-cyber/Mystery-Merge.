package com.example.mysterymerge.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.mysterymerge.data.GameContent
import com.example.mysterymerge.model.GameSaveState
import com.example.mysterymerge.model.Screen
import com.example.mysterymerge.ui.components.ResourceChip
import com.example.mysterymerge.ui.components.formatCommas
import com.example.mysterymerge.ui.theme.*

@Composable
fun HomeScreen(
    state: GameSaveState,
    onNavigate: (Screen) -> Unit,
    onClaimDailyReward: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MysteryNavy, Color(0xFF061822), MysteryNavy)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "◉ MYSTERY",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = MysteryGold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "   MERGE",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = MysteryGold,
                        letterSpacing = 2.sp
                    )
                }

                Surface(
                    onClick = { onNavigate(Screen.SETTINGS) },
                    shape = RoundedCornerShape(12.dp),
                    color = MysteryNavyLight,
                    border = BorderStroke(1.dp, MysteryGoldDark),
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("home_settings_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "⚙", fontSize = 20.sp, color = MysteryCream)
                    }
                }
            }

            Text(
                text = "SMALL CLUES. BIGGER TRUTHS.",
                fontSize = 10.sp,
                color = MysteryGoldDark,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            // Wallet Bar
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MysteryNavyLight.copy(alpha = 0.9f),
                border = BorderStroke(1.5.dp, MysteryGoldDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ResourceChip(symbol = "⚡", value = "${state.energy}/${state.maxEnergy}", color = MysteryGold)
                    ResourceChip(symbol = "◉", value = formatCommas(state.coins), color = MysteryGold)
                    ResourceChip(symbol = "◆", value = "${state.gems}", color = MysteryGemPurple)
                }
            }

            // Hero Splash / Illustration
            Surface(
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(2.dp, MysteryGoldDark),
                color = MysteryCardBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_detective_icon),
                        contentDescription = "Detective investigation banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MysteryNavy.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "HALE MANOR ARCHIVES",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MysteryGold
                        )
                        Text(
                            text = "A missing heirloom. Hidden motives.",
                            fontSize = 13.sp,
                            color = MysteryCream
                        )
                    }
                }
            }

            // Case Main Card
            val activeCase = GameContent.caseMap[state.currentCaseId] ?: GameContent.cases.first()
            val currentTask = activeCase.tasks.getOrNull(state.currentTaskIndex)

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MysteryPaper,
                border = BorderStroke(2.dp, MysteryGoldDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "ACTIVE DOSSIER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MysteryGoldDark,
                        letterSpacing = 1.sp
                    )

                    Text(
                        text = "CASE 0${activeCase.number}\n${activeCase.title.uppercase()}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = MysteryInk,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )

                    HorizontalDivider(
                        color = MysteryGoldDark.copy(alpha = 0.5f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Text(
                        text = activeCase.description,
                        fontSize = 12.sp,
                        color = MysteryInk,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )

                    val progressTitle = if (currentTask != null) {
                        "TASK ${state.currentTaskIndex + 1}/${activeCase.tasks.size} · ${currentTask.title.uppercase()}"
                    } else {
                        "CASE 0${activeCase.number} COMPLETE"
                    }

                    Text(
                        text = progressTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MysteryGoldDark
                    )

                    LinearProgressIndicator(
                        progress = { state.caseProgress },
                        color = MysteryTeal,
                        trackColor = MysteryGoldDark.copy(alpha = 0.3f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )

                    val detailText = if (currentTask != null) {
                        val names = currentTask.requiredItems.mapNotNull { GameContent.itemMap[it]?.displayName }
                        "Restore ${names.joinToString(", ")} to reveal the next detail."
                    } else {
                        "Every clue has been restored. Case solved."
                    }

                    Text(
                        text = detailText,
                        fontSize = 11.sp,
                        color = Color(0xFF5E4930),
                        textAlign = TextAlign.Center
                    )

                    Button(
                        onClick = { onNavigate(Screen.BOARD) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MysteryTeal,
                            contentColor = MysteryCream
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, MysteryGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("home_continue_investigation_button")
                    ) {
                        Text(
                            text = "CONTINUE INVESTIGATION     ›",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // Daily Detective Gift Button
            Button(
                onClick = onClaimDailyReward,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MysteryNavyLight,
                    contentColor = MysteryCream
                ),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, MysteryGoldDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("home_daily_gift_button")
            ) {
                Text(
                    text = "✦  DAILY DETECTIVE GIFT  ·  +100 COINS / +10 ENERGY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MysteryGold
                )
            }

            // Quick Nav Links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickNavCard(
                    title = "EVIDENCE\nBOARD",
                    icon = "▦",
                    onClick = { onNavigate(Screen.BOARD) },
                    modifier = Modifier.weight(1f),
                    testTag = "home_quick_board"
                )
                QuickNavCard(
                    title = "CASE\nDOSSIER",
                    icon = "▤",
                    onClick = { onNavigate(Screen.CASES) },
                    modifier = Modifier.weight(1f),
                    testTag = "home_quick_cases"
                )
                QuickNavCard(
                    title = "DETECTIVE\nSHOP",
                    icon = "♜",
                    onClick = { onNavigate(Screen.SHOP) },
                    modifier = Modifier.weight(1f),
                    testTag = "home_quick_shop"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun QuickNavCard(
    title: String,
    icon: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MysteryNavyLight,
        border = BorderStroke(1.dp, MysteryGoldDark.copy(alpha = 0.5f)),
        modifier = modifier.testTag(testTag)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = icon, fontSize = 22.sp, color = MysteryGold)
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MysteryCream,
                textAlign = TextAlign.Center
            )
        }
    }
}
