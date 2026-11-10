package com.example.mysterymerge.data

import android.content.Context
import com.example.mysterymerge.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.Calendar
import kotlin.random.Random

data class StoryEvent(
    val title: String,
    val storyText: String,
    val coinReward: Int,
    val gemReward: Int,
    val isCaseComplete: Boolean = false,
    val completedCaseTitle: String = ""
)

data class ActionResult(
    val success: Boolean,
    val message: String,
    val kind: String = "",
    val itemId: String = "",
    val slot: Int = -1
)

class GameStateRepository(private val context: Context) {

    private companion object {
        const val ENERGY_INTERVAL_SECONDS = 120
    }

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    private val saveFile: File by lazy {
        File(context.filesDir, "mystery_merge_save.json")
    }

    // Main dispatcher: the ticker and every player action now run on one thread, so a stale
    // copy of the state can never overwrite a merge made a moment earlier.
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    private val _state = MutableStateFlow(createInitialState())
    val state: StateFlow<GameSaveState> = _state.asStateFlow()

    private val _storyEvents = MutableSharedFlow<StoryEvent>(extraBufferCapacity = 16)
    val storyEvents: SharedFlow<StoryEvent> = _storyEvents.asSharedFlow()

    // Live countdown timer in seconds until next energy point (0..120)
    private val _secondsUntilNextEnergy = MutableStateFlow(ENERGY_INTERVAL_SECONDS)
    val secondsUntilNextEnergy: StateFlow<Int> = _secondsUntilNextEnergy.asStateFlow()

    private val _selection = MutableStateFlow<Pair<String, Int>?>(null)
    val selection: StateFlow<Pair<String, Int>?> = _selection.asStateFlow()

    init {
        loadGame()
        syncEnergy()
        startLiveEnergyTicker()
    }

    private fun createInitialState(): GameSaveState {
        val board = MutableList(GameContent.BOARD_SIZE) { "" }
        // Level-1 clues only: pairs and quads that teach merging without skipping the cases.
        val starterItems = listOf(
            "torn_photo", "torn_photo", "torn_photo",
            "old_key", "old_key", "old_key",
            "folded_note", "folded_note", "folded_note", "folded_note",
            "dust_sample", "dust_sample",
            "footprint_trace", "footprint_trace",
            "empty_chain", "empty_chain",
            "blue_ribbon", "blue_ribbon"
        )
        // Spread them over the top rows, leaving gaps so the board feels like a workspace.
        val starterSlots = listOf(0, 1, 2, 4, 5, 7, 8, 9, 10, 14, 15, 17, 21, 22, 24, 28, 29, 31)
        for ((k, id) in starterItems.withIndex()) {
            board[starterSlots[k]] = id
        }
        val inventory = MutableList(GameContent.INVENTORY_SIZE) { "" }
        inventory[0] = "empty_chain"
        inventory[1] = "footprint_trace"

        return GameSaveState(
            version = 2,
            energy = 100,
            maxEnergy = 100,
            coins = 500,
            gems = 10,
            currentCaseId = "case_01",
            currentLocation = "study",
            caseProgress = 0.0f,
            boardItems = board,
            inventoryItems = inventory,
            completedTasks = emptyList(),
            completedCases = emptyList(),
            caseTaskIndices = mapOf("case_01" to 0),
            currentTaskIndex = 0,
            dailyClaimedDay = -1,
            lastEnergyTimestamp = System.currentTimeMillis() / 1000L
        )
    }

    private fun loadGame() {
        if (!saveFile.exists()) {
            val initial = createInitialState()
            _state.value = initial
            saveGame(initial)
            return
        }
        try {
            val content = saveFile.readText()
            val loaded = json.decodeFromString<GameSaveState>(content)
            if (loaded.version < 2) {
                // Old format only: keep wallet, start the board over.
                val fresh = createInitialState().copy(
                    coins = loaded.coins,
                    gems = loaded.gems
                )
                _state.value = fresh
                saveGame(fresh)
                return
            }
            val knownItem: (String) -> String = { id -> if (id.isEmpty() || GameContent.itemMap.containsKey(id)) id else "" }
            val board = if (loaded.boardItems.size == GameContent.BOARD_SIZE) {
                loaded.boardItems.map(knownItem)
            } else {
                // Resize instead of discarding: keep what fits.
                List(GameContent.BOARD_SIZE) { loaded.boardItems.getOrElse(it) { "" } }.map(knownItem)
            }
            val inventory = if (loaded.inventoryItems.size == GameContent.INVENTORY_SIZE) {
                loaded.inventoryItems.map(knownItem)
            } else {
                List(GameContent.INVENTORY_SIZE) { loaded.inventoryItems.getOrElse(it) { "" } }.map(knownItem)
            }
            val caseId = if (GameContent.caseMap.containsKey(loaded.currentCaseId)) loaded.currentCaseId else "case_01"
            _state.value = loaded.copy(
                boardItems = board,
                inventoryItems = inventory,
                currentCaseId = caseId,
                lastEnergyTimestamp = if (loaded.lastEnergyTimestamp <= 0L) {
                    System.currentTimeMillis() / 1000L
                } else {
                    loaded.lastEnergyTimestamp
                }
            )
        } catch (e: Exception) {
            val fallback = createInitialState()
            _state.value = fallback
            saveGame(fallback)
        }
    }

    private fun saveGame(currState: GameSaveState = _state.value) {
        try {
            val content = json.encodeToString(currState)
            saveFile.writeText(content)
        } catch (_: Exception) {}
    }

    private fun syncEnergy() {
        val curr = _state.value
        if (curr.energy >= curr.maxEnergy) {
            _secondsUntilNextEnergy.value = ENERGY_INTERVAL_SECONDS
            return
        }
        val now = System.currentTimeMillis() / 1000L
        val elapsed = (now - curr.lastEnergyTimestamp).coerceAtLeast(0L)
        val gained = (elapsed / ENERGY_INTERVAL_SECONDS).toInt().coerceAtMost(curr.maxEnergy - curr.energy)
        if (gained > 0) {
            val newEnergy = curr.energy + gained
            val updated = curr.copy(
                energy = newEnergy,
                lastEnergyTimestamp = if (newEnergy >= curr.maxEnergy) now else curr.lastEnergyTimestamp + gained * ENERGY_INTERVAL_SECONDS
            )
            _state.value = updated
            saveGame(updated)
        }
        val st = _state.value
        val remainder = ((now - st.lastEnergyTimestamp).coerceAtLeast(0L) % ENERGY_INTERVAL_SECONDS).toInt()
        _secondsUntilNextEnergy.value = (ENERGY_INTERVAL_SECONDS - remainder).coerceIn(1, ENERGY_INTERVAL_SECONDS)
    }

    private fun startLiveEnergyTicker() {
        coroutineScope.launch {
            while (isActive) {
                syncEnergy()
                delay(1000L)
            }
        }
    }

    fun getSelection(): Pair<String, Int>? = _selection.value

    fun clearSelection() {
        _selection.value = null
    }

    fun handleTileClick(zone: String, index: Int): ActionResult {
        val curr = _state.value
        val itemsList = if (zone == "board") curr.boardItems else curr.inventoryItems
        if (index !in itemsList.indices) {
            return ActionResult(false, "Space unavailable")
        }
        val targetItem = itemsList[index]

        val prev = _selection.value
        val prevZone = prev?.first
        val prevIndex = prev?.second ?: -1

        if (prevZone == null) {
            if (targetItem.isEmpty()) {
                return ActionResult(false, "That space is empty.")
            }
            _selection.value = zone to index
            val item = GameContent.itemMap[targetItem]
            val name = item?.displayName ?: targetItem
            return ActionResult(true, "Selected $name. Choose another to merge or move.", "select", targetItem, index)
        }

        if (prevZone == zone && prevIndex == index) {
            clearSelection()
            return ActionResult(true, "Selection cleared.", "deselect")
        }

        val result = transferItem(prevZone, prevIndex, zone, index)
        clearSelection()
        return result
    }

    fun transferItem(fromZone: String, fromIndex: Int, toZone: String, toIndex: Int): ActionResult {
        val curr = _state.value
        val fromList = if (fromZone == "board") curr.boardItems.toMutableList() else curr.inventoryItems.toMutableList()
        val toList = if (toZone == "board") curr.boardItems.toMutableList() else curr.inventoryItems.toMutableList()

        if (fromIndex !in fromList.indices || toIndex !in toList.indices) {
            return ActionResult(false, "That space is unavailable.")
        }
        val sourceItem = fromList[fromIndex]
        if (sourceItem.isEmpty()) {
            return ActionResult(false, "That space is empty.")
        }
        val targetItem = if (fromZone == toZone) fromList[toIndex] else toList[toIndex]

        if (targetItem.isEmpty()) {
            // Move item
            if (fromZone == toZone) {
                fromList[toIndex] = sourceItem
                fromList[fromIndex] = ""
                val updated = if (fromZone == "board") curr.copy(boardItems = fromList) else curr.copy(inventoryItems = fromList)
                _state.value = updated
                saveGame(updated)
            } else {
                fromList[fromIndex] = ""
                toList[toIndex] = sourceItem
                val updated = curr.copy(
                    boardItems = if (fromZone == "board") fromList else toList,
                    inventoryItems = if (fromZone == "inventory") fromList else toList
                )
                _state.value = updated
                saveGame(updated)
            }
            return ActionResult(true, "Clue moved.", "move", sourceItem, toIndex)
        }

        if (sourceItem == targetItem) {
            // Merge
            val itemData = GameContent.itemMap[sourceItem]
            val resultId = itemData?.mergeResultId.orEmpty()
            if (resultId.isEmpty() || !GameContent.itemMap.containsKey(resultId)) {
                return ActionResult(false, "This clue is already fully restored (Level 3 Max).")
            }

            if (fromZone == toZone) {
                fromList[toIndex] = resultId
                fromList[fromIndex] = ""
                val updated = if (fromZone == "board") curr.copy(boardItems = fromList) else curr.copy(inventoryItems = fromList)
                _state.value = updated
                saveGame(updated)
            } else {
                fromList[fromIndex] = ""
                toList[toIndex] = resultId
                val updated = curr.copy(
                    boardItems = if (fromZone == "board") fromList else toList,
                    inventoryItems = if (fromZone == "inventory") fromList else toList
                )
                _state.value = updated
                saveGame(updated)
            }

            val mergedName = GameContent.itemMap[resultId]?.displayName ?: resultId
            val delivered = settleTasks()
            val msg = if (delivered > 0) "Merged: $mergedName. Objective complete!" else "Merged: $mergedName"
            return ActionResult(true, msg, "merge", resultId, toIndex)
        }

        if (fromZone == "inventory" && toZone == "inventory") {
            // Swap in inventory
            fromList[fromIndex] = targetItem
            fromList[toIndex] = sourceItem
            val updated = curr.copy(inventoryItems = fromList)
            _state.value = updated
            saveGame(updated)
            return ActionResult(true, "Inventory rearranged.", "swap", sourceItem, toIndex)
        }

        return ActionResult(false, "Those clues do not fit together.")
    }

    fun sellItem(zone: String, index: Int): ActionResult {
        val curr = _state.value
        val list = if (zone == "board") curr.boardItems.toMutableList() else curr.inventoryItems.toMutableList()
        if (index !in list.indices) return ActionResult(false, "Item not found")
        val itemId = list[index]
        if (itemId.isEmpty()) return ActionResult(false, "Slot is empty")

        val item = GameContent.itemMap[itemId] ?: return ActionResult(false, "Unknown item")
        val coinsEarned = item.sellValue
        list[index] = ""

        val updated = if (zone == "board") {
            curr.copy(boardItems = list, coins = curr.coins + coinsEarned)
        } else {
            curr.copy(inventoryItems = list, coins = curr.coins + coinsEarned)
        }
        _state.value = updated
        saveGame(updated)
        clearSelection()
        return ActionResult(true, "Sold ${item.displayName} for $coinsEarned coins.", "sell")
    }

    fun stashToInventory(boardIndex: Int): ActionResult {
        val curr = _state.value
        if (boardIndex !in curr.boardItems.indices) return ActionResult(false, "Invalid board slot")
        val itemId = curr.boardItems[boardIndex]
        if (itemId.isEmpty()) return ActionResult(false, "Slot is empty")

        val emptyInv = curr.inventoryItems.indexOfFirst { it.isEmpty() }
        if (emptyInv == -1) return ActionResult(false, "Field bag is full (8/8).")

        val result = transferItem("board", boardIndex, "inventory", emptyInv)
        clearSelection()
        return result
    }

    fun retrieveToBoard(inventoryIndex: Int): ActionResult {
        val curr = _state.value
        if (inventoryIndex !in curr.inventoryItems.indices) return ActionResult(false, "Invalid inventory slot")
        val itemId = curr.inventoryItems[inventoryIndex]
        if (itemId.isEmpty()) return ActionResult(false, "Slot is empty")

        val emptyBoard = curr.boardItems.indexOfFirst { it.isEmpty() }
        if (emptyBoard == -1) return ActionResult(false, "Evidence board is full.")

        val result = transferItem("inventory", inventoryIndex, "board", emptyBoard)
        clearSelection()
        return result
    }

    fun generateFromLocation(generatorId: String): ActionResult {
        val curr = _state.value
        if (!GameContent.isGeneratorUnlocked(generatorId, curr)) {
            val needed = if (generatorId == "locked_attic") "four" else "two"
            return ActionResult(false, "This location unlocks after $needed evidence objectives.")
        }
        val gen = GameContent.generators.find { it.id == generatorId }
            ?: return ActionResult(false, "Search location is unavailable.")

        val emptySlots = curr.boardItems.indices.filter { curr.boardItems[it].isEmpty() }
        if (emptySlots.isEmpty()) {
            return ActionResult(false, "The board is full. Move clues to inventory or merge matches first.")
        }
        if (curr.energy < GameContent.GENERATOR_COST) {
            return ActionResult(false, "Not enough energy. Each search costs ${GameContent.GENERATOR_COST} ⚡.")
        }

        val totalWeight = gen.pool.sumOf { it.weight.coerceAtLeast(1) }
        var roll = Random.nextInt(1, totalWeight + 1)
        var selectedItem = ""
        for (item in gen.pool) {
            roll -= item.weight.coerceAtLeast(1)
            if (roll <= 0) {
                selectedItem = item.itemId
                break
            }
        }
        if (selectedItem.isEmpty()) {
            selectedItem = gen.pool.firstOrNull()?.itemId.orEmpty()
        }

        val dest = emptySlots.random()
        val newBoard = curr.boardItems.toMutableList()
        newBoard[dest] = selectedItem

        // Leaving "full" starts the regeneration clock from now, not from some old timestamp.
        val clockStart = if (curr.energy >= curr.maxEnergy) System.currentTimeMillis() / 1000L else curr.lastEnergyTimestamp
        val updated = curr.copy(
            energy = (curr.energy - GameContent.GENERATOR_COST).coerceAtLeast(0),
            boardItems = newBoard,
            currentLocation = generatorId,
            lastEnergyTimestamp = clockStart
        )
        _state.value = updated
        saveGame(updated)
        syncEnergy()

        val name = GameContent.itemMap[selectedItem]?.displayName ?: selectedItem
        settleTasks()
        return ActionResult(true, "Found: $name!", "generate", selectedItem, dest)
    }

    fun switchCase(caseId: String): ActionResult {
        val targetCase = GameContent.caseMap[caseId] ?: return ActionResult(false, "Case not found")
        val curr = _state.value
        if (!GameContent.isCaseUnlocked(targetCase, curr.completedCases) && curr.currentCaseId != caseId) {
            val prev = GameContent.previousCase(targetCase)
            return ActionResult(false, "Complete ${prev?.let { GameContent.caseLabel(it.number) } ?: "the previous case"} first to unlock this dossier.")
        }

        val taskIdx = curr.caseTaskIndices[caseId] ?: 0
        val progress = taskIdx.toFloat() / targetCase.tasks.size.toFloat()

        val newIndices = curr.caseTaskIndices.toMutableMap()
        newIndices[caseId] = taskIdx
        val updated = curr.copy(
            currentCaseId = caseId,
            currentTaskIndex = taskIdx,
            caseTaskIndices = newIndices,
            caseProgress = progress.coerceIn(0.0f, 1.0f)
        )
        _state.value = updated
        saveGame(updated)
        // Clues you already hold may satisfy the first objective of the case you just opened.
        settleTasks()
        return ActionResult(true, "Opened ${GameContent.caseLabel(targetCase.number)}: ${targetCase.title}")
    }

    /**
     * Completes every objective that can be completed right now. A clue needed by the active
     * objective is handed in from the board (or the field bag) and consumed. Solving a case
     * moves the investigation on to the next unlocked case automatically.
     * Returns how many objectives were completed.
     */
    private fun settleTasks(): Int {
        var completed = 0
        var guard = 0
        while (guard++ < 60) {
            val curr = _state.value
            val activeCase = GameContent.caseMap[curr.currentCaseId] ?: return completed
            val currentIdx = curr.caseTaskIndices[curr.currentCaseId] ?: curr.currentTaskIndex
            if (currentIdx !in activeCase.tasks.indices) return completed
            val task = activeCase.tasks[currentIdx]

            val boardIdx = curr.boardItems.indexOfFirst { it.isNotEmpty() && task.requiredItems.contains(it) }
            val invIdx = if (boardIdx == -1) {
                curr.inventoryItems.indexOfFirst { it.isNotEmpty() && task.requiredItems.contains(it) }
            } else -1
            if (boardIdx == -1 && invIdx == -1) return completed

            val newBoard = curr.boardItems.toMutableList()
            val newInv = curr.inventoryItems.toMutableList()
            if (boardIdx != -1) newBoard[boardIdx] = "" else newInv[invIdx] = ""
            val sel = _selection.value
            if (sel != null && ((sel.first == "board" && sel.second == boardIdx && boardIdx != -1) ||
                        (sel.first == "inventory" && sel.second == invIdx && invIdx != -1))) {
                _selection.value = null
            }

            val alreadyDone = curr.completedTasks.contains(task.id)
            val nextIdx = currentIdx + 1
            val isCaseComplete = nextIdx >= activeCase.tasks.size
            val firstTimeCase = isCaseComplete && !curr.completedCases.contains(curr.currentCaseId)

            val newIndices = curr.caseTaskIndices.toMutableMap()
            newIndices[curr.currentCaseId] = nextIdx
            val newCompletedCases = if (firstTimeCase) curr.completedCases + curr.currentCaseId else curr.completedCases

            val taskCoins = if (alreadyDone) 0 else task.coinReward
            val taskGems = if (alreadyDone) 0 else task.gemReward
            val extraCoins = if (firstTimeCase) activeCase.completionCoins else 0
            val extraGems = if (firstTimeCase) activeCase.completionGems else 0

            // Move on to the next case automatically once this one is solved.
            val nextCase = if (isCaseComplete) GameContent.nextCase(activeCase) else null
            val nextCaseId = nextCase?.id ?: curr.currentCaseId
            val nextCaseIdx = if (nextCase != null) (newIndices[nextCase.id] ?: 0) else nextIdx
            if (nextCase != null) newIndices[nextCase.id] = nextCaseIdx
            val activeAfter = nextCase ?: activeCase

            val updated = curr.copy(
                boardItems = newBoard,
                inventoryItems = newInv,
                coins = curr.coins + taskCoins + extraCoins,
                gems = curr.gems + taskGems + extraGems,
                completedTasks = if (alreadyDone) curr.completedTasks else curr.completedTasks + task.id,
                completedCases = newCompletedCases,
                caseTaskIndices = newIndices,
                currentCaseId = nextCaseId,
                currentTaskIndex = nextCaseIdx,
                caseProgress = (nextCaseIdx.toFloat() / activeAfter.tasks.size.toFloat()).coerceIn(0.0f, 1.0f)
            )
            _state.value = updated
            saveGame(updated)
            completed++

            val storyText = GameContent.dialogueMap[task.storyReward]
                ?: "A crucial new piece of evidence falls into place."
            _storyEvents.tryEmit(
                StoryEvent(
                    title = task.title,
                    storyText = storyText,
                    coinReward = taskCoins + extraCoins,
                    gemReward = taskGems + extraGems,
                    isCaseComplete = isCaseComplete,
                    completedCaseTitle = activeCase.title
                )
            )
        }
        return completed
    }

    fun isDailyRewardClaimed(): Boolean = _state.value.dailyClaimedDay == getTodayKey()

    fun claimDailyReward(): ActionResult {
        val curr = _state.value
        val todayKey = getTodayKey()
        if (curr.dailyClaimedDay == todayKey) {
            return ActionResult(false, "Today's detective gift has already been claimed.")
        }
        val updated = curr.copy(
            coins = curr.coins + 100,
            energy = (curr.energy + 10).coerceAtMost(curr.maxEnergy),
            dailyClaimedDay = todayKey
        )
        _state.value = updated
        saveGame(updated)
        syncEnergy()
        return ActionResult(true, "Daily gift claimed: +100 coins and +10 energy!")
    }

    fun purchaseEnergyPack(coinCost: Int, energyAmount: Int): ActionResult {
        val curr = _state.value
        if (curr.energy >= curr.maxEnergy) {
            return ActionResult(false, "Your energy is already full.")
        }
        val gained = energyAmount.coerceAtMost(curr.maxEnergy - curr.energy)
        // Pay only for the energy that actually fits.
        val cost = if (gained >= energyAmount) coinCost else
            kotlin.math.ceil(coinCost.toDouble() * gained / energyAmount).toInt()
        if (curr.coins < cost) {
            return ActionResult(false, "Not enough coins for this supply pack.")
        }
        val updated = curr.copy(
            coins = curr.coins - cost,
            energy = curr.energy + gained
        )
        _state.value = updated
        saveGame(updated)
        syncEnergy()
        return ActionResult(true, "Purchased $gained energy for $cost coins.")
    }

    fun claimTestAdReward(): ActionResult {
        val curr = _state.value
        val updated = curr.copy(
            energy = (curr.energy + 10).coerceAtMost(curr.maxEnergy)
        )
        _state.value = updated
        saveGame(updated)
        syncEnergy()
        return ActionResult(true, "+10 Test Energy added!")
    }

    fun toggleSound() {
        val updated = _state.value.copy(soundEnabled = !_state.value.soundEnabled)
        _state.value = updated
        saveGame(updated)
    }

    fun toggleMusic() {
        val updated = _state.value.copy(musicEnabled = !_state.value.musicEnabled)
        _state.value = updated
        saveGame(updated)
    }

    fun toggleHaptics() {
        val updated = _state.value.copy(hapticsEnabled = !_state.value.hapticsEnabled)
        _state.value = updated
        saveGame(updated)
    }

    fun toggleNotifications() {
        val updated = _state.value.copy(notificationsEnabled = !_state.value.notificationsEnabled)
        _state.value = updated
        saveGame(updated)
    }

    fun resetLocalSave() {
        if (saveFile.exists()) {
            saveFile.delete()
        }
        val fresh = createInitialState()
        _state.value = fresh
        saveGame(fresh)
        clearSelection()
        syncEnergy()
    }

    fun todayKey(): Int = getTodayKey()

    private fun getTodayKey(): Int {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return year * 1000 + month * 32 + day
    }
}
