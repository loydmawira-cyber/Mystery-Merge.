package com.example.mysterymerge.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.mysterymerge.data.GameContent
import com.example.mysterymerge.model.CaseModel
import com.example.mysterymerge.model.EvidenceItem
import com.example.mysterymerge.model.GameSaveState
import com.example.mysterymerge.model.Screen
import com.example.mysterymerge.ui.components.TopBarWallet
import com.example.mysterymerge.ui.theme.*

@Composable
fun MergeBoardScreen(
    state: GameSaveState,
    selectedZone: String?,
    selectedIndex: Int,
    feedbackText: String,
    secondsUntilEnergy: Int,
    onTileClick: (zone: String, index: Int) -> Unit,
    onSellItem: (zone: String, index: Int) -> Unit,
    onStashItem: (boardIndex: Int) -> Unit,
    onRetrieveItem: (invIndex: Int) -> Unit,
    onGenerate: (generatorId: String) -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    var showGeneratorDialog by remember { mutableStateOf(false) }
    var selectedGenerator by remember { mutableStateOf("study") }

    val activeCase = GameContent.caseMap[state.currentCaseId] ?: GameContent.cases.first()
    val activeTaskIndex = state.caseTaskIndices[state.currentCaseId] ?: state.currentTaskIndex
    val currentTask = if (activeTaskIndex in activeCase.tasks.indices) activeCase.tasks[activeTaskIndex] else null

    // Selected item data for details inspector
    val selectedItemId = when (selectedZone) {
        "board" -> state.boardItems.getOrNull(selectedIndex).orEmpty()
        "inventory" -> state.inventoryItems.getOrNull(selectedIndex).orEmpty()
        else -> ""
    }
    val selectedItem = GameContent.itemMap[selectedItemId]

    val timerMinutes = secondsUntilEnergy / 60
    val timerSeconds = secondsUntilEnergy % 60
    val timerFormatted = "%02d:%02d".format(timerMinutes, timerSeconds)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MysteryNavy)
    ) {
        // Pristine Atmospheric Detective Study Background
        Image(
            painter = painterResource(id = R.drawable.bg_detective_study),
            contentDescription = "Detective study backdrop",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Vignette gradient overlay for high contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC071622),
                            Color(0x880A222D),
                            Color(0xEE050E16)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Bar: Logo, Energy (Live Timer), Coins, Gems, Settings
            TopBarWallet(
                energy = state.energy,
                maxEnergy = state.maxEnergy,
                coins = state.coins,
                gems = state.gems,
                energyTimerText = timerFormatted,
                onSettingsClick = { onNavigate(Screen.SETTINGS) },
                onEnergyClick = { onNavigate(Screen.SHOP) },
                onCoinsClick = { onNavigate(Screen.SHOP) },
                onGemsClick = { onNavigate(Screen.SHOP) }
            )

            // 2. Dynamic Authentic Manila Case Folder Banner
            DynamicCaseFolderBanner(
                caseModel = activeCase,
                currentTaskTitle = currentTask?.title ?: "All Objectives Complete!",
                taskProgress = state.caseProgress,
                taskStepText = if (currentTask != null) "Objective ${activeTaskIndex + 1}/${activeCase.tasks.size}" else "Case Solved ✓",
                onClick = { onNavigate(Screen.CASES) },
                modifier = Modifier.padding(horizontal = 2.dp)
            )

            // 3. 7 × 9 Evidence Board Container (Auto-sized to available screen space)
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                val availableWidth = maxWidth - 8.dp
                val availableHeight = maxHeight - 8.dp

                val cellByWidth = availableWidth / GameContent.BOARD_COLUMNS
                val cellByHeight = availableHeight / GameContent.BOARD_ROWS
                val cellSize = minOf(cellByWidth, cellByHeight)

                val boardWidth = cellSize * GameContent.BOARD_COLUMNS
                val boardHeight = cellSize * GameContent.BOARD_ROWS

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0C2B35),
                    border = BorderStroke(2.5.dp, Color(0xFF1B4E5E)),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .size(boardWidth + 6.dp, boardHeight + 6.dp)
                        .testTag("evidence_board_container")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(3.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (row in 0 until GameContent.BOARD_ROWS) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (col in 0 until GameContent.BOARD_COLUMNS) {
                                    val slotIndex = row * GameContent.BOARD_COLUMNS + col
                                    val itemId = state.boardItems.getOrElse(slotIndex) { "" }
                                    val isSelected = selectedZone == "board" && selectedIndex == slotIndex

                                    BoardEvidenceTile(
                                        itemId = itemId,
                                        isSelected = isSelected,
                                        size = cellSize - 2.dp,
                                        onClick = { onTileClick("board", slotIndex) },
                                        testTag = "tile_${row}_${col}"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Clue Inspector Bar (when a clue is selected)
            AnimatedVisibility(
                visible = selectedItem != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                selectedItem?.let { item ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF09202A),
                        border = BorderStroke(1.dp, MysteryGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Image(
                                    painter = painterResource(id = GameContent.getItemDrawable(item.id)),
                                    contentDescription = item.displayName,
                                    modifier = Modifier.size(28.dp).clip(RoundedCornerShape(4.dp))
                                )
                                Column {
                                    Text(
                                        text = "${item.displayName} (Lvl ${item.level})",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MysteryGold
                                    )
                                    val nextDesc = if (item.mergeResultId.isNotEmpty()) {
                                        "Merges into ${GameContent.itemMap[item.mergeResultId]?.displayName ?: "Next Clue"}"
                                    } else {
                                        "Fully Restored (Level 3 Max)"
                                    }
                                    Text(
                                        text = nextDesc,
                                        fontSize = 9.sp,
                                        color = MysteryCream,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                if (selectedZone == "board") {
                                    FilledTonalButton(
                                        onClick = { onStashItem(selectedIndex) },
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = Color(0xFF133E4D),
                                            contentColor = MysteryCream
                                        ),
                                        modifier = Modifier.height(26.dp)
                                    ) {
                                        Text("To Bag", fontSize = 9.sp)
                                    }
                                } else if (selectedZone == "inventory") {
                                    FilledTonalButton(
                                        onClick = { onRetrieveItem(selectedIndex) },
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.filledTonalButtonColors(
                                            containerColor = Color(0xFF133E4D),
                                            contentColor = MysteryCream
                                        ),
                                        modifier = Modifier.height(26.dp)
                                    ) {
                                        Text("To Board", fontSize = 9.sp)
                                    }
                                }

                                Button(
                                    onClick = { onSellItem(selectedZone ?: "", selectedIndex) },
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                    shape = RoundedCornerShape(6.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF653B2A),
                                        contentColor = MysteryGold
                                    ),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Text("Sell +${item.sellValue}🪙", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // 4. Bottom Controls: Evidence Chest + Field Bag
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Evidence Chest Generator (Bottom Left)
                Surface(
                    onClick = { onGenerate(selectedGenerator) },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF091E28),
                    border = BorderStroke(1.5.dp, Color(0xFF2E6F83)),
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .width(115.dp)
                        .height(82.dp)
                        .testTag("evidence_chest_button")
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_evidence_chest),
                                contentDescription = "Evidence Chest",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(44.dp)
                            )
                            Text(
                                text = "Evidence Chest",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MysteryGold
                            )
                            Text(
                                text = "Search (5 ⚡)",
                                fontSize = 9.sp,
                                color = MysteryCream.copy(alpha = 0.9f)
                            )
                        }

                        // Info / Search Location Picker (i)
                        Surface(
                            onClick = { showGeneratorDialog = true },
                            shape = CircleShape,
                            color = Color(0xFF074554),
                            border = BorderStroke(1.dp, MysteryTealBright),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                                .size(18.dp)
                                .testTag("generator_info_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "i",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MysteryCream
                                )
                            }
                        }
                    }
                }

                // Inventory / Field Bag (Bottom Right)
                val occupiedBagCount = state.inventoryItems.count { it.isNotEmpty() }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0A222C),
                    border = BorderStroke(1.5.dp, Color(0xFF225B6A)),
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .weight(1f)
                        .height(82.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 6.dp, vertical = 3.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Inventory ($occupiedBagCount/8)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MysteryGold
                            )
                            Text(
                                text = if (feedbackText.isNotEmpty()) feedbackText else "Tap to store / retrieve",
                                fontSize = 8.sp,
                                color = MysteryPaper,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Horizontal Row of 6 visible inventory slots
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 0 until 6) {
                                val itemId = state.inventoryItems.getOrElse(i) { "" }
                                val isSelected = selectedZone == "inventory" && selectedIndex == i

                                InventorySlotTile(
                                    itemId = itemId,
                                    isSelected = isSelected,
                                    onClick = { onTileClick("inventory", i) },
                                    modifier = Modifier.weight(1f),
                                    testTag = "inventory_slot_$i"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Search Locations Selector Dialog
    if (showGeneratorDialog) {
        AlertDialog(
            onDismissRequest = { showGeneratorDialog = false },
            title = {
                Text(
                    text = "INVESTIGATION LOCATIONS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MysteryGold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Select which Manor location to search with the Evidence Chest (5 Energy per search):",
                        fontSize = 12.sp,
                        color = MysteryCream
                    )

                    for (gen in GameContent.generators) {
                        val isUnlocked = when (gen.id) {
                            "garden_room" -> activeTaskIndex >= 2 || state.completedCases.isNotEmpty()
                            "locked_attic" -> activeTaskIndex >= 4 || state.completedCases.isNotEmpty()
                            else -> true
                        }
                        val isSelected = selectedGenerator == gen.id

                        Surface(
                            onClick = {
                                if (isUnlocked) {
                                    selectedGenerator = gen.id
                                    showGeneratorDialog = false
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFF1B4E5E) else if (isUnlocked) Color(0xFF0F323D) else Color(0xFF172026),
                            border = BorderStroke(1.dp, if (isSelected) MysteryGold else Color.DarkGray),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = gen.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUnlocked) MysteryCream else Color.Gray
                                    )
                                    Text(
                                        text = gen.description,
                                        fontSize = 10.sp,
                                        color = if (isUnlocked) MysteryPaper else Color.DarkGray
                                    )
                                }
                                Text(
                                    text = if (isUnlocked) (if (isSelected) "ACTIVE" else "SELECT") else "LOCKED",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MysteryGold else if (isUnlocked) MysteryTealBright else Color.Gray
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGeneratorDialog = false }) {
                    Text("CLOSE", color = MysteryGold)
                }
            },
            containerColor = Color(0xFF091E28),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun DynamicCaseFolderBanner(
    caseModel: CaseModel,
    currentTaskTitle: String,
    taskProgress: Float,
    taskStepText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFE4CFAC),
        border = BorderStroke(1.5.dp, Color(0xFF9E7542)),
        shadowElevation = 5.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .testTag("dynamic_case_folder_banner")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Photo of Case location with photo border
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFC7B18E)),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .size(56.dp)
                    .rotate(-2f)
            ) {
                Image(
                    painter = painterResource(id = caseModel.thumbResId),
                    contentDescription = caseModel.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
            }

            // Middle: Dynamic Case Title & Objective
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "CASE 0${caseModel.number}: ${caseModel.title.uppercase()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF281C14),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currentTaskTitle,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF573E28),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                LinearProgressIndicator(
                    progress = { taskProgress },
                    color = MysteryTeal,
                    trackColor = Color(0xFFCBB792),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
            }

            // Right: Paper note with Paperclip and Magnifying Glass button
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFF7ECDA),
                border = BorderStroke(1.dp, Color(0xFFC5AB85)),
                modifier = Modifier
                    .width(105.dp)
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Merge clues\nto uncover",
                        fontSize = 8.sp,
                        color = Color(0xFF4C3625),
                        lineHeight = 10.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF144D5B),
                        border = BorderStroke(1.dp, MysteryGold),
                        modifier = Modifier.size(26.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "🔍", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoardEvidenceTile(
    itemId: String,
    isSelected: Boolean,
    size: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    testTag: String
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.12f else 1f,
        label = "tile_scale"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        color = Color(0xFF0B2630),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 0.5.dp,
            color = if (isSelected) MysteryGold else Color(0xFF1B4957)
        ),
        shadowElevation = if (isSelected) 6.dp else 1.dp,
        modifier = Modifier
            .size(size)
            .scale(scale)
            .testTag(testTag)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (itemId.isNotEmpty()) {
                val drawableRes = GameContent.getItemDrawable(itemId)
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = itemId,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(3.dp))
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.tile_empty_recessed),
                    contentDescription = "Empty slot",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(2.dp, MysteryGold, RoundedCornerShape(4.dp))
                        .background(MysteryGold.copy(alpha = 0.15f))
                )
            }
        }
    }
}

@Composable
fun InventorySlotTile(
    itemId: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFF09202A),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MysteryGold else Color(0xFF1E5260)
        ),
        modifier = modifier
            .aspectRatio(1f)
            .testTag(testTag)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (itemId.isNotEmpty()) {
                val drawableRes = GameContent.getItemDrawable(itemId)
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = itemId,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(5.dp))
                )
            } else {
                Text(
                    text = "＋",
                    fontSize = 14.sp,
                    color = Color(0xFF2F768B)
                )
            }
        }
    }
}
