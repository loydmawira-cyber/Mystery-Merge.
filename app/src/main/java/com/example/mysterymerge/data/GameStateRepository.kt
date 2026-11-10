package com.example.mysterymerge.data

import android.content.Context
import com.example.mysterymerge.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    private val saveFile: File by lazy {
        File(context.filesDir, "mystery_merge_save.json")
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default + Job())

    private val _state = MutableStateFlow(createInitialState())
    val state: StateFlow<GameSaveState> = _state.asStateFlow()

    private val _storyEvents = MutableSharedFlow<StoryEvent>(extraBufferCapacity = 1)
    val storyEvents: SharedFlow<StoryEvent> = _storyEvents.asSharedFlow()

    // Live countdown timer in seconds until next energy point (0..120)
    private val _secondsUntilNextEnergy = MutableStateFlow(120)
    val secondsUntilNextEnergy: StateFlow<Int> = _secondsUntilNextEnergy.asStateFlow()

    private var selectedSourceZone: String? = null
    private var selectedSourceIndex: Int = -1

    init {
        loadGame()
        applyOfflineEnergy()
        startLiveEnergyTicker()
    }

    private fun createInitialState(): GameSaveState {
        val board = MutableList(GameContent.BOARD_SIZE) { "" }
        val starterItems = listOf(
            0 to "folded_note", 1 to "opened_note", 2 to "torn_photo", 3 to "torn_photo",
            4 to "old_key", 5 to "dust_sample", 6 to "torn_score",
            7 to "wax_fragment", 8 to "old_key", 9 to "decoded_note", 10 to "empty_chain",
            11 to "folded_note", 12 to "restored_score", 13 to "torn_photo",
            14 to "annotated_photo", 15 to "dried_bloom", 16 to "sifted_dust", 17 to "dried_bloom",
            18 to "garden_impression", 19 to "reassembled_photo", 20 to "midnight_melody",
            21 to "wax_fragment", 22 to "folded_note", 23 to "opened_note", 24 to "dust_sample",
            25 to "matching_footprint", 26 to "embroidered_ribbon", 27 to "old_key",
            28 to "attic_fiber", 29 to "restored_score", 30 to "garden_impression", 31 to "blue_ribbon",
            32 to "reassembled_photo", 33 to "cleaned_key", 34 to "locket_clasp",
            35 to "annotated_photo", 36 to "pressed_bloom", 37 to "torn_photo", 38 to "reassembled_photo",
            39 to "sifted_dust", 40 to "footprint_trace", 41 to "family_seal",
            42 to "folded_note", 43 to "cleaned_key", 44 to "cleaned_key", 45 to "matching_footprint",
            46 to "missing_locket", 47 to "decoded_note", 48 to "opened_note"
        )
        for ((slot, id) in starterItems) {
            if (slot in board.indices) {
                board[slot] = id
            }
        }
        val inventory = MutableList(GameContent.INVENTORY_SIZE) { "" }
        val starterInventory = listOf(
            0 to "old_key",
            1 to "opened_note",
            2 to "locket_clasp",
            3 to "annotated_photo",
            4 to "footprint_trace"
        )
        for ((slot, id) in starterInventory) {
            if (slot in inventory.indices) {
                inventory[slot] = id
            }
        }

        return GameSaveState(
            version = 2,
            energy = 72,
            maxEnergy = 100,
            coins = 12450,
            gems = 38,
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
            if (loaded.version < 2 || loaded.boardItems.count { it.isNotEmpty() } < 10) {
                val fresh = createInitialState().copy(
                    coins = loaded.coins.coerceAtLeast(12450),
                    gems = loaded.gems.coerceAtLeast(38),
                    energy = loaded.energy.coerceAtLeast(72)
                )
                _state.value = fresh
                saveGame(fresh)
                return
            }
            val board = if (loaded.boardItems.size == GameContent.BOARD_SIZE) {
                loaded.boardItems
            } else {
                createInitialState().boardItems
            }
            val inventory = if (loaded.inventoryItems.size == GameContent.INVENTORY_SIZE) {
                loaded.inventoryItems
            } else {
                createInitialState().inventoryItems
            }
            _state.value = loaded.copy(
                boardItems = board,
                inventoryItems = inventory,
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

    private fun applyOfflineEnergy() {
        val curr = _state.value
        val now = System.currentTimeMillis() / 1000L
        if (curr.lastEnergyTimestamp <= 0L) {
            _state.value = curr.copy(lastEnergyTimestamp = now)
            saveGame()
            return
        }
        val diff = (now - curr.lastEnergyTimestamp).coerceAtLeast(0L)
        val gained = (diff / 120L).toInt().coerceAtMost(curr.maxEnergy - curr.energy)
        val remainder = (diff % 120L).toInt()
        _secondsUntilNextEnergy.value = (120 - remainder).coerceIn(1, 120)

        if (gained > 0) {
            val updated = curr.copy(
                energy = (curr.energy + gained).coerceAtMost(curr.maxEnergy),
                lastEnergyTimestamp = curr.lastEnergyTimestamp + (gained * 120L)
            )
            _state.value = updated
            saveGame(updated)
        }
    }

    private fun startLiveEnergyTicker() {
        coroutineScope.launch {
            while (isActive) {
                delay(1000L) // Real 1-second clock tick
                val curr = _state.value
                if (curr.energy >= curr.maxEnergy) {
                    _secondsUntilNextEnergy.value = 120
                    _state.value = curr.copy(lastEnergyTimestamp = System.currentTimeMillis() / 1000L)
                    continue
                }

                val secLeft = _secondsUntilNextEnergy.value - 1
                if (secLeft <= 0) {
                    _secondsUntilNextEnergy.value = 120
                    val updated = curr.copy(
                        energy = (curr.energy + 1).coerceAtMost(curr.maxEnergy),
                        lastEnergyTimestamp = System.currentTimeMillis() / 1000L
                    )
                    _state.value = updated
                    saveGame(updated)
                } else {
                    _secondsUntilNextEnergy.value = secLeft
                }
            }
        }
    }

    fun getSelection(): Pair<String, Int>? {
        val zone = selectedSourceZone ?: return null
        return zone to selectedSourceIndex
    }

    fun clearSelection() {
        selectedSourceZone = null
        selectedSourceIndex = -1
    }

    fun handleTileClick(zone: String, index: Int): ActionResult {
        val curr = _state.value
        val itemsList = if (zone == "board") curr.boardItems else curr.inventoryItems
        if (index !in itemsList.indices) {
            return ActionResult(false, "Space unavailable")
        }
        val targetItem = itemsList[index]

        val prevZone = selectedSourceZone
        val prevIndex = selectedSourceIndex

        if (prevZone == null) {
            if (targetItem.isEmpty()) {
                return ActionResult(false, "That space is empty.")
            }
            selectedSourceZone = zone
            selectedSourceIndex = index
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
            checkTaskCompletion(resultId)
            return ActionResult(true, "Merged: $mergedName", "merge", resultId, toIndex)
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

        return transferItem("board", boardIndex, "inventory", emptyInv)
    }

    fun retrieveToBoard(inventoryIndex: Int): ActionResult {
        val curr = _state.value
        if (inventoryIndex !in curr.inventoryItems.indices) return ActionResult(false, "Invalid inventory slot")
        val itemId = curr.inventoryItems[inventoryIndex]
        if (itemId.isEmpty()) return ActionResult(false, "Slot is empty")

        val emptyBoard = curr.boardItems.indexOfFirst { it.isEmpty() }
        if (emptyBoard == -1) return ActionResult(false, "Evidence board is full.")

        return transferItem("inventory", inventoryIndex, "board", emptyBoard)
    }

    fun generateFromLocation(generatorId: String): ActionResult {
        val curr = _state.value
        if (generatorId == "garden_room" && curr.currentTaskIndex < 2 && !curr.completedCases.contains("case_01")) {
            return ActionResult(false, "Garden Room unlocks after two evidence objectives.")
        }
        if (generatorId == "locked_attic" && curr.currentTaskIndex < 4 && !curr.completedCases.contains("case_01")) {
            return ActionResult(false, "Locked Attic unlocks after four evidence objectives.")
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

        val updated = curr.copy(
            energy = (curr.energy - GameContent.GENERATOR_COST).coerceAtLeast(0),
            boardItems = newBoard,
            currentLocation = generatorId
        )
        _state.value = updated
        saveGame(updated)

        val name = GameContent.itemMap[selectedItem]?.displayName ?: selectedItem
        return ActionResult(true, "Found: $name!", "generate", selectedItem, dest)
    }

    fun switchCase(caseId: String): ActionResult {
        val targetCase = GameContent.caseMap[caseId] ?: return ActionResult(false, "Case not found")
        val curr = _state.value
        val reqPrevCase = if (targetCase.number > 1) "case_0${targetCase.number - 1}" else null
        if (reqPrevCase != null && !curr.completedCases.contains(reqPrevCase) && curr.currentCaseId != caseId) {
            return ActionResult(false, "Complete Case 0${targetCase.number - 1} first to unlock this dossier.")
        }

        val taskIdx = curr.caseTaskIndices[caseId] ?: 0
        val progress = taskIdx.toFloat() / targetCase.tasks.size.toFloat()

        val updated = curr.copy(
            currentCaseId = caseId,
            currentTaskIndex = taskIdx,
            caseProgress = progress.coerceIn(0.0f, 1.0f)
        )
        _state.value = updated
        saveGame(updated)
        return ActionResult(true, "Opened Case 0${targetCase.number}: ${targetCase.title}")
    }

    private fun checkTaskCompletion(itemId: String) {
        val curr = _state.value
        val activeCase = GameContent.caseMap[curr.currentCaseId] ?: return
        val currentIdx = curr.caseTaskIndices[curr.currentCaseId] ?: curr.currentTaskIndex
        if (currentIdx !in activeCase.tasks.indices) return

        val currentTask = activeCase.tasks[currentIdx]
        if (!currentTask.requiredItems.contains(itemId)) return
        if (curr.completedTasks.contains(currentTask.id)) return

        val newCompletedTasks = curr.completedTasks + currentTask.id
        val nextIdx = currentIdx + 1
        val newIndices = curr.caseTaskIndices.toMutableMap()
        newIndices[curr.currentCaseId] = nextIdx
        val newProgress = nextIdx.toFloat() / activeCase.tasks.size.toFloat()

        val isCaseComplete = nextIdx >= activeCase.tasks.size
        val newCompletedCases = if (isCaseComplete && !curr.completedCases.contains(curr.currentCaseId)) {
            curr.completedCases + curr.currentCaseId
        } else {
            curr.completedCases
        }

        val extraCoins = if (isCaseComplete) activeCase.completionCoins else 0
        val extraGems = if (isCaseComplete) activeCase.completionGems else 0

        val updated = curr.copy(
            coins = curr.coins + currentTask.coinReward + extraCoins,
            gems = curr.gems + currentTask.gemReward + extraGems,
            completedTasks = newCompletedTasks,
            completedCases = newCompletedCases,
            caseTaskIndices = newIndices,
            currentTaskIndex = nextIdx,
            caseProgress = newProgress.coerceIn(0.0f, 1.0f)
        )
        _state.value = updated
        saveGame(updated)

        val storyText = GameContent.dialogueMap[currentTask.storyReward]
            ?: "A crucial new piece of evidence falls into place."

        _storyEvents.tryEmit(
            StoryEvent(
                title = currentTask.title,
                storyText = storyText,
                coinReward = currentTask.coinReward + extraCoins,
                gemReward = currentTask.gemReward + extraGems,
                isCaseComplete = isCaseComplete,
                completedCaseTitle = activeCase.title
            )
        )
    }

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
        return ActionResult(true, "Daily gift claimed: +100 coins and +10 energy!")
    }

    fun purchaseEnergyPack(coinCost: Int, energyAmount: Int): ActionResult {
        val curr = _state.value
        if (curr.coins < coinCost) {
            return ActionResult(false, "Not enough coins for this supply pack.")
        }
        if (curr.energy >= curr.maxEnergy) {
            return ActionResult(false, "Your energy is already full.")
        }
        val gained = energyAmount.coerceAtMost(curr.maxEnergy - curr.energy)
        val updated = curr.copy(
            coins = curr.coins - coinCost,
            energy = curr.energy + gained
        )
        _state.value = updated
        saveGame(updated)
        return ActionResult(true, "Purchased $gained energy for $coinCost coins.")
    }

    fun claimTestAdReward(): ActionResult {
        val curr = _state.value
        val updated = curr.copy(
            energy = (curr.energy + 10).coerceAtMost(curr.maxEnergy)
        )
        _state.value = updated
        saveGame(updated)
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
    }

    private fun getTodayKey(): Int {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return year * 1000 + month * 32 + day
    }
}
