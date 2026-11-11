package com.example.mysterymerge.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mysterymerge.data.ActionResult
import com.example.mysterymerge.data.GameContent
import com.example.mysterymerge.data.SceneContent
import com.example.mysterymerge.model.GameSaveState
import com.example.mysterymerge.model.SceneHotspot
import com.example.mysterymerge.model.Screen
import com.example.mysterymerge.ui.components.TopBarWallet
import com.example.mysterymerge.ui.theme.*

private data class Discovery(
    val label: String,
    val text: String,
    val itemName: String,
    val count: Int,
    val coins: Int
)

private enum class SpotState { LOCKED, OPEN, FOUND }

@Composable
fun SceneScreen(
    state: GameSaveState,
    onSearch: (hotspotId: String) -> ActionResult,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val caseModel = GameContent.caseMap[state.currentCaseId] ?: GameContent.cases.first()
    val scene = SceneContent.sceneByCase[caseModel.id] ?: SceneContent.scenes.first()
    val activeIdx = state.caseTaskIndices[caseModel.id] ?: state.currentTaskIndex
    val currentTask = caseModel.tasks.getOrNull(activeIdx)

    var discovery by remember { mutableStateOf<Discovery?>(null) }
    var hint by remember { mutableStateOf("") }
    val found = state.foundHotspots
    val foundCount = scene.hotspots.count { found.contains(it.id) }

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
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .widthIn(max = 500.dp)
                .align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column {
                Text(
                    text = "${GameContent.caseLabel(caseModel.number)} · INVESTIGATION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MysteryGold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = scene.title.uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = MysteryCream
                )
            }

            // Current lead
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MysteryCardBg,
                border = BorderStroke(1.dp, MysteryGoldDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "CURRENT LEAD",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MysteryGold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = currentTask?.title ?: "Case solved. Open the Cases tab to continue.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MysteryCream
                    )
                    if (currentTask != null) {
                        val need = currentTask.requiredItems.mapNotNull { GameContent.itemMap[it]?.displayName }
                        Text(
                            text = "Evidence needed: ${need.joinToString(" or ")}",
                            fontSize = 11.sp,
                            color = MysteryMuted
                        )
                    }
                }
            }

            // The room
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.75f)
                    .clip(RoundedCornerShape(14.dp))
                    .testTag("scene_view")
            ) {
                val w = maxWidth
                val h = maxHeight
                Image(
                    painter = painterResource(id = scene.imageResId),
                    contentDescription = scene.title,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                for (spot in scene.hotspots) {
                    val req = spot.requiresId
                    val spotState = when {
                        found.contains(spot.id) -> SpotState.FOUND
                        req != null && !found.contains(req) -> SpotState.LOCKED
                        else -> SpotState.OPEN
                    }
                    HotspotMarker(
                        spot = spot,
                        spotState = spotState,
                        modifier = Modifier.offset(x = w * spot.x - 42.dp, y = h * spot.y - 30.dp),
                        onClick = {
                            val result = onSearch(spot.id)
                            if (result.success) {
                                hint = ""
                                val itemName = GameContent.itemMap[result.itemId]?.displayName.orEmpty()
                                discovery = Discovery(
                                    label = spot.label,
                                    text = result.message,
                                    itemName = itemName,
                                    count = result.slot.coerceAtLeast(0),
                                    coins = spot.coinReward
                                )
                            } else {
                                hint = result.message
                            }
                        }
                    )
                }
            }

            Text(
                text = "Spots searched: $foundCount / ${scene.hotspots.size}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MysteryMuted
            )
            if (hint.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF2B1F14),
                    border = BorderStroke(1.dp, MysteryGoldDark),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = hint,
                        fontSize = 12.sp,
                        color = MysteryCream,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            // Case notes
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MysteryPaper,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "CASE NOTES",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MysteryInk,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = scene.intro,
                        fontSize = 12.sp,
                        color = MysteryInk
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }

    discovery?.let { d ->
        AlertDialog(
            onDismissRequest = { discovery = null },
            containerColor = MysteryCardBg,
            titleContentColor = MysteryGold,
            textContentColor = MysteryCream,
            title = { Text(text = d.label.uppercase(), fontWeight = FontWeight.Black, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = d.text, fontSize = 13.sp)
                    if (d.count > 0) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MysteryNavyLight,
                            border = BorderStroke(1.dp, MysteryGoldDark)
                        ) {
                            Text(
                                text = "Evidence found: ${d.count} × ${d.itemName}\nAdded to your evidence board. Merge matching pieces to restore them.",
                                fontSize = 12.sp,
                                color = MysteryGold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                    if (d.coins > 0) {
                        Text(text = "+${d.coins} coins", fontSize = 12.sp, color = MysteryGold)
                    }
                }
            },
            confirmButton = {
                if (d.count > 0) {
                    Button(
                        onClick = {
                            discovery = null
                            onNavigate(Screen.BOARD)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MysteryGoldDark, contentColor = MysteryCream),
                        modifier = Modifier.testTag("scene_to_board_button")
                    ) { Text("RESTORE EVIDENCE") }
                } else {
                    TextButton(onClick = { discovery = null }) { Text("CONTINUE", color = MysteryGold) }
                }
            },
            dismissButton = {
                if (d.count > 0) {
                    TextButton(onClick = { discovery = null }) { Text("KEEP SEARCHING", color = MysteryMuted) }
                }
            }
        )
    }
}

@Composable
private fun HotspotMarker(
    spot: SceneHotspot,
    spotState: SpotState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pulse = rememberInfiniteTransition(label = "spot_pulse")
    val glow by pulse.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "spot_glow"
    )
    val ringColor = when (spotState) {
        SpotState.FOUND -> Color(0xFF5FBF7A)
        SpotState.LOCKED -> Color(0xFF8A8A8A)
        SpotState.OPEN -> MysteryGold
    }
    Column(
        modifier = modifier.width(84.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = Color(0xAA081820),
            border = BorderStroke(2.dp, ringColor),
            modifier = Modifier
                .size(38.dp)
                .alpha(if (spotState == SpotState.OPEN) glow else 0.9f)
                .testTag("scene_spot_${spot.id}")
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = when (spotState) {
                        SpotState.FOUND -> "✓"
                        SpotState.LOCKED -> "🔒"
                        SpotState.OPEN -> "🔍"
                    },
                    fontSize = 15.sp,
                    color = ringColor
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = spot.label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = MysteryCream,
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = Modifier
                .background(Color(0xCC081820), RoundedCornerShape(6.dp))
                .padding(horizontal = 4.dp, vertical = 1.dp)
        )
    }
}
