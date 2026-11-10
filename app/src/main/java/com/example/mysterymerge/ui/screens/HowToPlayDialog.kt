package com.example.mysterymerge.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.mysterymerge.data.GameContent
import com.example.mysterymerge.ui.theme.*

@Composable
fun HowToPlayDialog(
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("BASICS", "CHAINS", "SEARCH", "CASES", "ECONOMY")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MysteryPaper,
            border = BorderStroke(2.5.dp, MysteryGoldDark),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.90f)
                .padding(vertical = 12.dp)
                .testTag("how_to_play_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "✦ DETECTIVE HANDBOOK ✦",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7E5525),
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "HOW TO PLAY",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = MysteryInk
                        )
                    }

                    Surface(
                        onClick = onDismiss,
                        shape = CircleShape,
                        color = Color(0xFF144D5B),
                        border = BorderStroke(1.dp, MysteryGold),
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("close_how_to_play_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "✕", fontSize = 14.sp, color = MysteryCream, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Navigation Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFFDCC69D),
                    contentColor = MysteryInk,
                    edgePadding = 0.dp,
                    indicator = {},
                    divider = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        Tab(
                            selected = isSelected,
                            onClick = { selectedTab = index },
                            modifier = Modifier
                                .background(if (isSelected) Color(0xFF144D5B) else Color.Transparent)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                color = if (isSelected) MysteryGold else Color(0xFF5A412E),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tab Content (Scrollable)
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when (selectedTab) {
                        0 -> BasicsTab()
                        1 -> ChainsTab()
                        2 -> SearchTab()
                        3 -> CasesTab()
                        4 -> EconomyTab()
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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
                        .height(44.dp)
                        .testTag("dismiss_handbook_button")
                ) {
                    Text(
                        text = "RETURN TO INVESTIGATION",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun BasicsTab() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        InstructionCard(
            stepNumber = "1",
            title = "Tap to Select & Move",
            body = "Tap any clue on the 7×9 board to select it (a glowing golden ring appears). Then tap any empty slot to move and organize clues across your board."
        )

        InstructionCard(
            stepNumber = "2",
            title = "Merge Matching Clues",
            body = "Tap a selected clue and tap another identical clue from the same chain. They will merge into a higher-tier evidence item (Level 1 → Level 2 → Level 3 Max)!"
        )

        InstructionCard(
            stepNumber = "3",
            title = "Clue Inspector & Quick Actions",
            body = "When a clue is selected, an inspector bar appears below the board showing its name, level, lore, and next merge target. You can also tap 'To Bag' to store it or 'Sell' to liquidate excess items for coins!"
        )

        InstructionCard(
            stepNumber = "4",
            title = "Field Bag (Pocket Inventory)",
            body = "The 8-slot Field Bag at the bottom holds extra items when your board gets crowded. Tap a clue and tap an empty inventory slot to store it, or tap a stored clue and an empty board slot to retrieve it."
        )
    }
}

@Composable
private fun ChainsTab() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "There are 10 distinct evidence chains. Items only merge with their exact chain partner:",
            fontSize = 11.sp,
            color = Color(0xFF4C3625),
            fontWeight = FontWeight.Medium
        )

        val chainItems = listOf(
            Triple("The Photograph", "Study Desk", listOf("torn_photo", "reassembled_photo", "annotated_photo")),
            Triple("The Brass Key", "Study Desk", listOf("old_key", "cleaned_key", "engraved_key")),
            Triple("The Hidden Note", "Study Desk", listOf("folded_note", "opened_note", "decoded_note")),
            Triple("The Garden Footprint", "Garden Room", listOf("footprint_trace", "garden_impression", "matching_footprint")),
            Triple("The Attic Dust", "Locked Attic", listOf("dust_sample", "sifted_dust", "attic_fiber")),
            Triple("The Blue Ribbon", "Garden Room", listOf("blue_ribbon", "mended_ribbon", "embroidered_ribbon")),
            Triple("The Family Seal", "Locked Attic", listOf("wax_fragment", "melted_seal", "family_seal")),
            Triple("The Midnight Score", "Locked Attic", listOf("torn_score", "restored_score", "midnight_melody")),
            Triple("The Pressed Flower", "Garden Room", listOf("dried_bloom", "pressed_bloom", "garden_token")),
            Triple("The Missing Locket", "Locked Attic", listOf("empty_chain", "locket_clasp", "missing_locket"))
        )

        for ((title, location, itemIds) in chainItems) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF0E0C4),
                border = BorderStroke(1.dp, Color(0xFFBE9758)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2C1E14)
                        )
                        Text(
                            text = "Found in: $location",
                            fontSize = 9.sp,
                            color = MysteryTeal,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for ((lvl, id) in itemIds.withIndex()) {
                            val item = GameContent.itemMap[id]
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(76.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF0C2B35),
                                    border = BorderStroke(1.dp, MysteryGoldDark),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = GameContent.getItemDrawable(id)),
                                        contentDescription = item?.displayName ?: id,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Text(
                                    text = "Lvl ${lvl + 1}: ${item?.shortName ?: id}",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4C3625),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }

                            if (lvl < 2) {
                                Text(text = "➔", fontSize = 12.sp, color = Color(0xFF9E7542), fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTab() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        InstructionCard(
            stepNumber = "⚡",
            title = "Searching with the Evidence Chest",
            body = "Tap the 3D Evidence Chest at the bottom left to search the manor. Each search costs 5 Energy (⚡) and spawns a random level-1 clue onto an empty board slot."
        )

        InstructionCard(
            stepNumber = "i",
            title = "Switching Investigation Locations",
            body = "Tap the small (i) button on the Evidence Chest to choose which area of the manor to search:"
        )

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFF0E0C4),
            border = BorderStroke(1.dp, Color(0xFFBE9758)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "1. Study Desk (Always Available)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C1E14)
                )
                Text(
                    text = "Spawns Torn Photos, Old Keys, Folded Notes, and Attic Dust samples.",
                    fontSize = 10.sp,
                    color = Color(0xFF5A412E)
                )

                HorizontalDivider(color = Color(0xFFBE9758).copy(alpha = 0.4f), thickness = 0.5.dp)

                Text(
                    text = "2. Garden Room (Unlocks at Objective 2)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C1E14)
                )
                Text(
                    text = "Spawns Footprint Traces, Blue Ribbons, and Dried Blooms.",
                    fontSize = 10.sp,
                    color = Color(0xFF5A412E)
                )

                HorizontalDivider(color = Color(0xFFBE9758).copy(alpha = 0.4f), thickness = 0.5.dp)

                Text(
                    text = "3. Locked Attic (Unlocks at Objective 4)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C1E14)
                )
                Text(
                    text = "Spawns Wax Fragments, Torn Music Scores, and Empty Locket Chains.",
                    fontSize = 10.sp,
                    color = Color(0xFF5A412E)
                )
            }
        }
    }
}

@Composable
private fun CasesTab() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        InstructionCard(
            stepNumber = "🗃",
            title = "Active Case Objective Banner",
            body = "The manila folder banner at the top shows your current active Case and the exact evidence item required. For example, 'Reconstruct the photograph' requires a Reassembled Photograph (Level 2)."
        )

        InstructionCard(
            stepNumber = "✦",
            title = "Story Reveals & Rewards",
            body = "Whenever you merge or possess the required evidence clue, the objective completes automatically! You earn Coins, Gems, and trigger a narrative story reveal scene from detective Nora."
        )

        InstructionCard(
            stepNumber = "📁",
            title = "10 Detective Cases",
            body = "Tap the 'Cases' tab in the bottom bar to view the Manor Case Archives. Complete all 5 objectives in a case to earn bonus Coins and Gems and unlock the next mystery. The clue you hand in is used up, and the next case opens automatically:\n\n" +
                    "• Case 01: The Missing Locket\n" +
                    "• Case 02: The Clock Tower Cipher\n" +
                    "• Case 03: The Rose Garden Mystery\n" +
                    "• Case 04: The Alchemist's Secret\n" +
                    "• Case 05: The Midnight Masquerade\n" +
                    "• Case 06: The Silent Conservatory\n" +
                    "• Case 07: The Sealed Letter\n" +
                    "• Case 08: The Violinist's Last Note\n" +
                    "• Case 09: The Cellar Beneath the Chapel\n" +
                    "• Case 10: The Heir's Return"
        )
    }
}

@Composable
private fun EconomyTab() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        InstructionCard(
            stepNumber = "⚡",
            title = "Automatic Energy Regeneration",
            body = "Energy regenerates at 1 point every 2 minutes (120 seconds) up to your max capacity of 100 ⚡. Regeneration continues automatically even when the app is closed!"
        )

        InstructionCard(
            stepNumber = "🪙",
            title = "Coins & Clue Selling",
            body = "Earn coins by completing case objectives, claiming daily gifts, and selling unwanted clues using the inspector bar. Use coins in the Detective Shop to buy instant +10, +25, +50, or +75 Energy refills."
        )

        InstructionCard(
            stepNumber = "💎",
            title = "Precious Gems",
            body = "Gems are rare detective rewards earned from milestones and major case solutions. Use them in the shop for exclusive mystery supplies."
        )

        InstructionCard(
            stepNumber = "🎁",
            title = "Daily Detective Gift",
            body = "Log in each day to claim your free detective supply grant: +100 Coins and +10 Energy!"
        )
    }
}

@Composable
private fun InstructionCard(
    stepNumber: String,
    title: String,
    body: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF0E0C4),
        border = BorderStroke(1.dp, Color(0xFFBE9758)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF144D5B),
                border = BorderStroke(1.dp, MysteryGold),
                modifier = Modifier.size(26.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stepNumber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = MysteryGold
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF2C1E14)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = body,
                    fontSize = 11.sp,
                    color = Color(0xFF4C3625),
                    lineHeight = 15.sp
                )
            }
        }
    }
}
