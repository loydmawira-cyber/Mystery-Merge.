package com.example.mysterymerge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mysterymerge.data.GameContent
import com.example.mysterymerge.data.GameStateRepository
import com.example.mysterymerge.data.StoryEvent
import com.example.mysterymerge.model.Screen
import com.example.mysterymerge.ui.components.BottomNavBar
import com.example.mysterymerge.ui.components.StoryRevealDialog
import com.example.mysterymerge.ui.screens.*
import com.example.mysterymerge.ui.theme.MysteryMergeTheme
import com.example.mysterymerge.ui.theme.MysteryNavy

class MainActivity : ComponentActivity() {

    private lateinit var repository: GameStateRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        repository = GameStateRepository(applicationContext)

        setContent {
            MysteryMergeTheme {
                MysteryMergeApp(repository = repository)
            }
        }
    }
}

@Composable
fun MysteryMergeApp(repository: GameStateRepository) {
    val state by repository.state.collectAsStateWithLifecycle()
    val secondsUntilEnergy by repository.secondsUntilNextEnergy.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf(Screen.BOARD) }
    // Queue so that several objectives solved in one move are shown one after another.
    val storyQueue = remember { mutableStateListOf<StoryEvent>() }
    val currentStoryEvent = storyQueue.firstOrNull()
    var feedbackMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    // Listen to story reveal events
    LaunchedEffect(Unit) {
        repository.storyEvents.collect { event ->
            storyQueue.add(event)
        }
    }

    // Handle Android system back press
    BackHandler(enabled = currentScreen != Screen.BOARD) {
        currentScreen = Screen.BOARD
    }

    val selection by repository.selection.collectAsStateWithLifecycle()
    val selectedZone = selection?.first
    val selectedIndex = selection?.second ?: -1

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            BottomNavBar(
                currentScreen = currentScreen,
                onNavigate = { currentScreen = it },
                hasCaseNotifications = state.completedCases.size < GameContent.cases.size
            )
        },
        containerColor = MysteryNavy
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MysteryNavy)
        ) {
            when (currentScreen) {
                Screen.BOARD -> MergeBoardScreen(
                    state = state,
                    selectedZone = selectedZone,
                    selectedIndex = selectedIndex,
                    feedbackText = feedbackMessage,
                    secondsUntilEnergy = secondsUntilEnergy,
                    onTileClick = { zone, index ->
                        val result = repository.handleTileClick(zone, index)
                        feedbackMessage = result.message
                    },
                    onSellItem = { zone, index ->
                        val result = repository.sellItem(zone, index)
                        feedbackMessage = result.message
                    },
                    onStashItem = { boardIndex ->
                        val result = repository.stashToInventory(boardIndex)
                        feedbackMessage = result.message
                    },
                    onRetrieveItem = { invIndex ->
                        val result = repository.retrieveToBoard(invIndex)
                        feedbackMessage = result.message
                    },
                    onGenerate = { genId ->
                        val result = repository.generateFromLocation(genId)
                        feedbackMessage = result.message
                    },
                    onNavigate = { currentScreen = it }
                )
                Screen.CASES -> CaseFilesScreen(
                    state = state,
                    onSelectCase = { caseId ->
                        val result = repository.switchCase(caseId)
                        feedbackMessage = result.message
                    },
                    onNavigate = { currentScreen = it }
                )
                Screen.SHOP -> ShopScreen(
                    state = state,
                    feedbackText = feedbackMessage,
                    dailyClaimed = state.dailyClaimedDay == repository.todayKey(),
                    onClaimDaily = {
                        val result = repository.claimDailyReward()
                        feedbackMessage = result.message
                    },
                    onPurchaseEnergyPack = { cost, amount ->
                        val result = repository.purchaseEnergyPack(cost, amount)
                        feedbackMessage = result.message
                    },
                    onClaimTestAdReward = {
                        val result = repository.claimTestAdReward()
                        feedbackMessage = result.message
                    },
                    onNavigate = { currentScreen = it }
                )
                Screen.SETTINGS -> SettingsScreen(
                    state = state,
                    onToggleSound = { repository.toggleSound() },
                    onToggleMusic = { repository.toggleMusic() },
                    onToggleNotifications = { repository.toggleNotifications() },
                    onResetData = {
                        repository.resetLocalSave()
                        feedbackMessage = "Local case data reset."
                    },
                    onNavigate = { currentScreen = it }
                )
            }

            // Evidence Discovered Story Reveal Dialog
            currentStoryEvent?.let { story ->
                StoryRevealDialog(
                    event = story,
                    onDismiss = { if (storyQueue.isNotEmpty()) storyQueue.removeAt(0) }
                )
            }
        }
    }
}
