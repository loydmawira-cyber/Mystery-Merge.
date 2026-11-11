package com.example.mysterymerge.model

import kotlinx.serialization.Serializable

@Serializable
data class EvidenceItem(
    val id: String,
    val displayName: String,
    val shortName: String,
    val description: String,
    val level: Int,
    val chainId: String,
    val sellValue: Int,
    val mergeResultId: String = "",
    val symbol: String,
    val tileColor: String
)

@Serializable
data class EvidenceChain(
    val id: String,
    val title: String,
    val location: String
)

@Serializable
data class GeneratorPoolItem(
    val itemId: String,
    val weight: Int
)

@Serializable
data class GeneratorLocation(
    val id: String,
    val title: String,
    val description: String,
    val pool: List<GeneratorPoolItem>
)

@Serializable
data class TaskItem(
    val id: String,
    val caseId: String,
    val title: String,
    val requiredItems: List<String>,
    val coinReward: Int,
    val gemReward: Int,
    val storyReward: String
)

@Serializable
data class CaseModel(
    val id: String,
    val number: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val thumbResId: Int,
    val locations: List<String>,
    val tasks: List<TaskItem>,
    val completionCoins: Int = 500,
    val completionGems: Int = 10
)

@Serializable
data class DialogueScene(
    val id: String,
    val text: String
)

@Serializable
data class EnergyPack(
    val amount: Int,
    val name: String,
    val coinCost: Int
)

/** Damaged clue pieces hidden in a spot, enough to build what one objective needs. */
data class ClueReward(
    val itemId: String,
    val count: Int,
    val taskId: String
)

data class SceneHotspot(
    val id: String,
    val label: String,
    val x: Float,
    val y: Float,
    val searchText: String,
    val clue: ClueReward? = null,
    val coinReward: Int = 0,
    val requiresId: String? = null,
    val lockedHint: String = ""
)

data class CaseScene(
    val caseId: String,
    val title: String,
    val intro: String,
    val imageResId: Int,
    val hotspots: List<SceneHotspot>
)

@Serializable
data class GameSaveState(
    val version: Int = 2,
    val energy: Int = 72,
    val maxEnergy: Int = 100,
    val coins: Int = 12450,
    val gems: Int = 38,
    val currentCaseId: String = "case_01",
    val currentLocation: String = "study",
    val caseProgress: Float = 0.0f,
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val boardItems: List<String> = emptyList(),
    val inventoryItems: List<String> = emptyList(),
    val completedTasks: List<String> = emptyList(),
    val completedCases: List<String> = emptyList(),
    val caseTaskIndices: Map<String, Int> = emptyMap(),
    val currentTaskIndex: Int = 0,
    val dailyClaimedDay: Int = -1,
    val lastEnergyTimestamp: Long = 0L,
    val foundHotspots: List<String> = emptyList()
)

enum class Screen {
    SCENE,
    BOARD,
    CASES,
    SHOP,
    SETTINGS
}
