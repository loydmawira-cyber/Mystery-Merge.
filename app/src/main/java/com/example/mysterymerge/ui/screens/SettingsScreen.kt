package com.example.mysterymerge.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mysterymerge.model.GameSaveState
import com.example.mysterymerge.model.Screen
import com.example.mysterymerge.ui.components.TopBarWallet
import com.example.mysterymerge.ui.theme.*

@Composable
fun SettingsScreen(
    state: GameSaveState,
    onToggleSound: () -> Unit,
    onToggleMusic: () -> Unit,
    onToggleNotifications: () -> Unit,
    onResetData: () -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var showResetDialog by remember { mutableStateOf(false) }
    var showHowToPlay by remember { mutableStateOf(false) }

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
            onSettingsClick = {}
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .widthIn(max = 500.dp)
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "SETTINGS",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                color = MysteryGold
            )

            // Audio & Notification Toggles
            ToggleOptionCard(
                label = "Sound Effects",
                enabled = state.soundEnabled,
                onToggle = onToggleSound,
                testTag = "toggle_sound"
            )

            ToggleOptionCard(
                label = "Music",
                enabled = state.musicEnabled,
                onToggle = onToggleMusic,
                testTag = "toggle_music"
            )

            ToggleOptionCard(
                label = "Notifications",
                enabled = state.notificationsEnabled,
                onToggle = onToggleNotifications,
                testTag = "toggle_notifications"
            )

            Text(
                text = "Settings and evidence progress are saved automatically on this device.",
                fontSize = 12.sp,
                color = MysteryMuted
            )

            Spacer(modifier = Modifier.weight(1f, fill = false).height(40.dp))

            // How to Play / Detective Handbook
            Button(
                onClick = { showHowToPlay = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MysteryTeal,
                    contentColor = MysteryCream
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, MysteryGold),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("settings_how_to_play_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "📖", fontSize = 18.sp)
                    Text(
                        text = "HOW TO PLAY · DETECTIVE HANDBOOK",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = MysteryGold
                    )
                }
            }

            // Reset Game Data
            Button(
                onClick = { showResetDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF653D46),
                    contentColor = Color(0xFFFFD9D3)
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF8B4D58)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("reset_game_data_button")
            ) {
                Text(
                    text = "RESET LOCAL GAME DATA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // Back to Board
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
                    .height(50.dp)
                    .testTag("settings_back_board_button")
            ) {
                Text(
                    text = "BACK TO EVIDENCE BOARD",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = "Reset Mystery Merge?",
                    color = MysteryGold,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This permanently clears your local evidence board, currencies, and case progress on this device.",
                    color = MysteryCream
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        onResetData()
                        onNavigate(Screen.BOARD)
                    },
                    modifier = Modifier.testTag("confirm_reset_button")
                ) {
                    Text("RESET", color = Color(0xFFFF8B80), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showResetDialog = false }
                ) {
                    Text("CANCEL", color = MysteryCream)
                }
            },
            containerColor = MysteryNavyLight,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showHowToPlay) {
        HowToPlayDialog(onDismiss = { showHowToPlay = false })
    }
}

@Composable
private fun ToggleOptionCard(
    label: String,
    enabled: Boolean,
    onToggle: () -> Unit,
    testTag: String
) {
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1C3C49),
        border = BorderStroke(1.dp, MysteryGoldDark.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MysteryCream
            )
            Switch(
                checked = enabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MysteryGold,
                    checkedTrackColor = MysteryTeal,
                    uncheckedThumbColor = MysteryMuted,
                    uncheckedTrackColor = MysteryNavy
                )
            )
        }
    }
}
