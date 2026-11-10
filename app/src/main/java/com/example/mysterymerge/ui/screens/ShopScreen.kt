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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mysterymerge.data.GameContent
import com.example.mysterymerge.model.GameSaveState
import com.example.mysterymerge.model.Screen
import com.example.mysterymerge.ui.components.TopBarWallet
import com.example.mysterymerge.ui.components.formatCommas
import com.example.mysterymerge.ui.theme.*

@Composable
fun ShopScreen(
    state: GameSaveState,
    feedbackText: String,
    onPurchaseEnergyPack: (cost: Int, amount: Int) -> Unit,
    onClaimTestAdReward: () -> Unit,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

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
            onSettingsClick = { onNavigate(Screen.SETTINGS) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .widthIn(max = 500.dp)
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "DETECTIVE SHOP",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = MysteryGold
                    )
                    Text(
                        text = "SUPPORT THE MYSTERY · KEEP THE STORY GOING",
                        fontSize = 9.sp,
                        color = MysteryMuted,
                        letterSpacing = 0.5.sp
                    )
                }

                Surface(
                    onClick = { onNavigate(Screen.BOARD) },
                    shape = RoundedCornerShape(12.dp),
                    color = MysteryNavyLight,
                    border = BorderStroke(1.dp, MysteryGoldDark),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "‹", fontSize = 22.sp, color = MysteryCream)
                    }
                }
            }

            // Featured Hero: Starter Kit
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = MysteryPaper,
                border = BorderStroke(2.dp, MysteryGold),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "✦  FIRST CASE SUPPLY KIT  ✦",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7E5525)
                    )
                    Text(
                        text = "A BRIGHTER BEGINNING",
                        fontSize = 10.sp,
                        color = Color(0xFF80643C)
                    )
                    Text(
                        text = "300 ◆  ·  500 ⚡  ·  +1 BAG SLOT",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF302319)
                    )
                    Text(
                        text = "A generous starter bundle for a new detective.",
                        fontSize = 11.sp,
                        color = Color(0xFF4C3926)
                    )
                    Button(
                        onClick = {},
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = MysteryGold.copy(alpha = 0.4f),
                            disabledContentColor = MysteryInk.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "STARTER PACK · COMING SOON",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Section: Field Energy Supply Packs
            Text(
                text = "FIELD ENERGY SUPPLIES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MysteryGold,
                letterSpacing = 1.sp
            )

            // Grid of 4 packs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                EnergyPackCard(
                    pack = GameContent.energyPacks[0],
                    canAfford = state.coins >= GameContent.energyPacks[0].coinCost && state.energy < state.maxEnergy,
                    onBuy = { onPurchaseEnergyPack(GameContent.energyPacks[0].coinCost, GameContent.energyPacks[0].amount) },
                    modifier = Modifier.weight(1f),
                    testTag = "buy_energy_pack_10"
                )
                EnergyPackCard(
                    pack = GameContent.energyPacks[1],
                    canAfford = state.coins >= GameContent.energyPacks[1].coinCost && state.energy < state.maxEnergy,
                    onBuy = { onPurchaseEnergyPack(GameContent.energyPacks[1].coinCost, GameContent.energyPacks[1].amount) },
                    modifier = Modifier.weight(1f),
                    testTag = "buy_energy_pack_25"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                EnergyPackCard(
                    pack = GameContent.energyPacks[2],
                    canAfford = state.coins >= GameContent.energyPacks[2].coinCost && state.energy < state.maxEnergy,
                    onBuy = { onPurchaseEnergyPack(GameContent.energyPacks[2].coinCost, GameContent.energyPacks[2].amount) },
                    modifier = Modifier.weight(1f),
                    testTag = "buy_energy_pack_50"
                )
                EnergyPackCard(
                    pack = GameContent.energyPacks[3],
                    canAfford = state.coins >= GameContent.energyPacks[3].coinCost && state.energy < state.maxEnergy,
                    onBuy = { onPurchaseEnergyPack(GameContent.energyPacks[3].coinCost, GameContent.energyPacks[3].amount) },
                    modifier = Modifier.weight(1f),
                    testTag = "buy_energy_pack_75"
                )
            }

            // Development Rewarded Ad Simulation Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF123B45),
                border = BorderStroke(1.5.dp, MysteryTealBright),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "OPTIONAL REWARDED VIDEO",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MysteryGold
                    )
                    Text(
                        text = "Development test simulation — no real ad network is connected.",
                        fontSize = 11.sp,
                        color = MysteryCream.copy(alpha = 0.8f)
                    )
                    Button(
                        onClick = onClaimTestAdReward,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MysteryTeal,
                            contentColor = MysteryCream
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MysteryTealBright),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("shop_claim_test_ad_button")
                    ) {
                        Text(
                            text = "CLAIM TEST REWARD · +10 ENERGY",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (feedbackText.isNotEmpty()) {
                Text(
                    text = feedbackText,
                    fontSize = 12.sp,
                    color = MysteryGold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onNavigate(Screen.BOARD) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MysteryNavyLight,
                    contentColor = MysteryCream
                ),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, MysteryGoldDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "‹  BACK TO INVESTIGATION",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EnergyPackCard(
    pack: com.example.mysterymerge.model.EnergyPack,
    canAfford: Boolean,
    onBuy: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MysteryNavyLight,
        border = BorderStroke(1.5.dp, MysteryGoldDark),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "⚡", fontSize = 28.sp, color = MysteryGold)
            Text(
                text = "+${pack.amount}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = MysteryCream
            )
            Text(
                text = pack.name,
                fontSize = 10.sp,
                color = MysteryMuted,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${formatCommas(pack.coinCost)} coins",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MysteryPaper
            )
            Button(
                onClick = onBuy,
                enabled = canAfford,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MysteryGold,
                    contentColor = MysteryInk
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(testTag)
            ) {
                Text(
                    text = "BUY",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
