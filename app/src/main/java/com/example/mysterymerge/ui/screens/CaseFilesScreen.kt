package com.example.mysterymerge.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mysterymerge.data.GameContent
import com.example.mysterymerge.model.CaseModel
import com.example.mysterymerge.model.GameSaveState
import com.example.mysterymerge.model.Screen
import com.example.mysterymerge.ui.components.TopBarWallet
import com.example.mysterymerge.ui.theme.*

@Composable
fun CaseFilesScreen(
    state: GameSaveState,
    onSelectCase: (caseId: String) -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var selectedViewCaseId by remember { mutableStateOf(state.currentCaseId) }
    val viewedCase = GameContent.caseMap[selectedViewCaseId] ?: GameContent.cases.first()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MysteryNavy)
    ) {
        TopBarWallet(
            energy = state.energy,
            maxEnergy = state.maxEnergy,
            coins = state.coins,
            gems = state.gems,
            onSettingsClick = { onNavigate(Screen.SETTINGS) },
            onEnergyClick = { onNavigate(Screen.SHOP) },
            onCoinsClick = { onNavigate(Screen.SHOP) },
            onGemsClick = { onNavigate(Screen.SHOP) }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MANOR CASE ARCHIVES",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = MysteryGold
                    )
                    Text(
                        text = "SELECT A DOSSIER TO INVESTIGATE",
                        fontSize = 10.sp,
                        color = MysteryMuted,
                        letterSpacing = 1.sp
                    )
                }

                Surface(
                    onClick = { onNavigate(Screen.BOARD) },
                    shape = RoundedCornerShape(10.dp),
                    color = MysteryNavyLight,
                    border = BorderStroke(1.dp, MysteryGoldDark),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "✕", fontSize = 16.sp, color = MysteryCream)
                    }
                }
            }

            // Case Selector Carousel / Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (c in GameContent.cases) {
                    val isSelected = selectedViewCaseId == c.id
                    val isCompleted = state.completedCases.contains(c.id)
                    val isUnlocked = c.number == 1 || state.completedCases.contains("case_0${c.number - 1}")
                    val isActive = state.currentCaseId == c.id

                    Surface(
                        onClick = { selectedViewCaseId = c.id },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFF1E4C5A) else if (isUnlocked) MysteryNavyLight else Color(0xFF131F27),
                        border = BorderStroke(
                            1.5.dp,
                            if (isSelected) MysteryGold else if (isActive) MysteryTealBright else Color.DarkGray
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                            .testTag("case_tab_${c.number}")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "CASE ${c.number}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) MysteryGold else if (isUnlocked) MysteryCream else Color.Gray
                            )
                            Text(
                                text = if (isCompleted) "SOLVED ✓" else if (isActive) "ACTIVE" else if (isUnlocked) "OPEN" else "LOCKED",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted) MysteryGold else if (isActive) MysteryTealBright else Color.Gray
                            )
                        }
                    }
                }
            }

            // Selected Case Dossier Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MysteryPaper,
                border = BorderStroke(2.dp, MysteryGoldDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFC7B18E)),
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(68.dp)
                        ) {
                            Image(
                                painter = painterResource(id = viewedCase.thumbResId),
                                contentDescription = viewedCase.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(3.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CASE 0${viewedCase.number}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7E5525)
                            )
                            Text(
                                text = viewedCase.title,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = MysteryInk
                            )
                            Text(
                                text = viewedCase.subtitle,
                                fontSize = 11.sp,
                                color = Color(0xFF5E4930),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    HorizontalDivider(color = MysteryGoldDark.copy(alpha = 0.4f), thickness = 1.dp)

                    Text(
                        text = viewedCase.description,
                        fontSize = 12.sp,
                        color = MysteryInk,
                        lineHeight = 17.sp
                    )

                    // Case Rewards Banner
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Completion Reward:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6E4924)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "+${viewedCase.completionCoins} 🪙",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7E5525)
                            )
                            Text(
                                text = "+${viewedCase.completionGems} 💎",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MysteryTeal
                            )
                        }
                    }

                    // Investigate Button
                    val isViewedUnlocked = viewedCase.number == 1 || state.completedCases.contains("case_0${viewedCase.number - 1}")
                    val isCurrentlyActive = state.currentCaseId == viewedCase.id

                    Button(
                        onClick = {
                            if (isViewedUnlocked) {
                                onSelectCase(viewedCase.id)
                                onNavigate(Screen.BOARD)
                            }
                        },
                        enabled = isViewedUnlocked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCurrentlyActive) MysteryGoldDark else MysteryTeal,
                            contentColor = MysteryCream,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.3f),
                            disabledContentColor = Color.DarkGray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("investigate_case_button")
                    ) {
                        Text(
                            text = if (isCurrentlyActive) "CONTINUE INVESTIGATION ›" else if (isViewedUnlocked) "OPEN THIS CASE ›" else "LOCKED (Complete Case 0${viewedCase.number - 1})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Case Evidence Checklist
            Text(
                text = "EVIDENCE OBJECTIVES (${viewedCase.tasks.size} STEPS)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MysteryGold,
                letterSpacing = 1.sp
            )

            val viewedTaskIndex = state.caseTaskIndices[viewedCase.id] ?: if (state.currentCaseId == viewedCase.id) state.currentTaskIndex else 0

            for ((idx, task) in viewedCase.tasks.withIndex()) {
                val isCompleted = state.completedTasks.contains(task.id) || viewedTaskIndex > idx
                val isCurrent = viewedTaskIndex == idx

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isCompleted) MysteryCardBg else if (isCurrent) MysteryNavyLight else MysteryNavyLight.copy(alpha = 0.5f),
                    border = BorderStroke(
                        1.dp,
                        if (isCurrent) MysteryGold else if (isCompleted) MysteryGoldDark.copy(alpha = 0.6f) else Color.DarkGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = if (isCompleted) "✓" else "${idx + 1}.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCompleted) MysteryGold else MysteryMuted
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.title,
                                fontSize = 12.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCompleted) MysteryCream else if (isCurrent) MysteryGold else MysteryMuted
                            )
                            val reqNames = task.requiredItems.mapNotNull { GameContent.itemMap[it]?.displayName }
                            Text(
                                text = "Requires: ${reqNames.joinToString(", ")}",
                                fontSize = 10.sp,
                                color = MysteryPaper.copy(alpha = 0.8f)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "+${task.coinReward} 🪙",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MysteryGold
                            )
                            if (task.gemReward > 0) {
                                Text(
                                    text = "+${task.gemReward} 💎",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MysteryGemPurple
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onNavigate(Screen.BOARD) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MysteryNavyLight,
                    contentColor = MysteryCream
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MysteryGoldDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("cases_back_to_board")
            ) {
                Text(
                    text = "RETURN TO EVIDENCE BOARD",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
