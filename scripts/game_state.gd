extends Node
const BOARD_COLUMNS: int = 7
const BOARD_ROWS: int = 9
const BOARD_SIZE: int = BOARD_COLUMNS * BOARD_ROWS
const INVENTORY_SIZE: int = 8
const GENERATOR_COST: int = 5
var player_level: int = 1
var energy: int = 72
var max_energy: int = 100
var coins: int = 12450
var gems: int = 38
var current_case: String = "case_01"
var current_location: String = "study"
var case_progress: float = 0.0
var sound_enabled: bool = true
var music_enabled: bool = true
var notifications_enabled: bool = true
var item_catalog: Dictionary = {}
var generator_catalog: Dictionary = {}
var task_catalog: Array = []
var dialogue_catalog: Array = []
var completed_tasks: Array[String] = []
var current_task_index: int = 0
var daily_claimed_day: int = -1
var last_energy_timestamp: int = 0
var energy_timer: Timer
var save_path: String = "user://mystery_merge_save.json"
var board_items: Array = []
var inventory_items: Array = []
var board_initialized: bool = false
var rng := RandomNumberGenerator.new()
signal economy_changed
signal progress_changed
signal board_changed
signal task_completed(task_id: String, story_text: String)
signal save_loaded
func _ready() -> void:
	rng.randomize()
	_load_content_data()
	_load_game()
	_apply_offline_energy()
	initialize_board()
	_save_game()
	energy_timer = Timer.new()
	energy_timer.wait_time = 10.0
	energy_timer.timeout.connect(_on_energy_tick)
	add_child(energy_timer)
	energy_timer.start()
func _load_content_data() -> void:
	var evidence_data := _read_json("res://data/evidence_items.json")
	for item in evidence_data.get("items", []):
		if item is Dictionary and item.has("id"):
			item_catalog[str(item["id"])] = item
	var task_data := _read_json("res://data/tasks.json")
	task_catalog = task_data.get("tasks", [])
	var dialogue_data := _read_json("res://data/dialogue.json")
	dialogue_catalog = dialogue_data.get("scenes", [])
	var generator_data := _read_json("res://data/generators.json")
	for generator in generator_data.get("generators", []):
		if generator is Dictionary and generator.has("id"):
			generator_catalog[str(generator["id"])] = generator
func _read_json(path: String) -> Dictionary:
	if not FileAccess.file_exists(path):
		push_error("Missing game data file: " + path)
		return {}
	var parsed: Variant = JSON.parse_string(FileAccess.get_file_as_string(path))
	if parsed is Dictionary:
		return parsed
	push_error("Invalid JSON object in: " + path)
	return {}
func initialize_board() -> void:
	if board_initialized:
		return
	if board_items.size() != BOARD_SIZE:
		board_items.resize(BOARD_SIZE)
		for index in range(BOARD_SIZE):
			board_items[index] = ""
	if inventory_items.size() != INVENTORY_SIZE:
		inventory_items.resize(INVENTORY_SIZE)
		for index in range(INVENTORY_SIZE):
			inventory_items[index] = ""
	if _count_items(board_items) == 0:
		var starter_items := [
			{"slot": 8, "id": "torn_photo"}, {"slot": 9, "id": "torn_photo"},
			{"slot": 15, "id": "old_key"}, {"slot": 16, "id": "old_key"},
			{"slot": 24, "id": "folded_note"}, {"slot": 25, "id": "folded_note"},
			{"slot": 33, "id": "footprint_trace"}, {"slot": 34, "id": "footprint_trace"},
			{"slot": 42, "id": "wax_fragment"}, {"slot": 43, "id": "wax_fragment"},
			{"slot": 50, "id": "dried_bloom"}, {"slot": 51, "id": "dried_bloom"}
		]
		for entry in starter_items:
			board_items[int(entry["slot"])] = str(entry["id"])
	board_initialized = true
	board_changed.emit()
func get_item(item_id: String) -> Dictionary:
	if item_catalog.has(item_id):
		return item_catalog[item_id]
	return {}
func get_slot(zone: String, index: int) -> String:
	if zone == "board":
		if index >= 0 and index < board_items.size():
			return str(board_items[index])
	elif zone == "inventory":
		if index >= 0 and index < inventory_items.size():
			return str(inventory_items[index])
	return ""
func transfer_item(from_zone: String, from_index: int, to_zone: String, to_index: int) -> Dictionary:
	if from_zone == to_zone and from_index == to_index:
		return {"ok": false, "message": "Choose a different space."}
	var source_item := get_slot(from_zone, from_index)
	if source_item.is_empty():
		return {"ok": false, "message": "That space is empty."}
	var target_item := get_slot(to_zone, to_index)
	if not _valid_slot(from_zone, from_index) or not _valid_slot(to_zone, to_index):
		return {"ok": false, "message": "That space is unavailable."}
	if target_item.is_empty():
		_set_slot(to_zone, to_index, source_item)
		_set_slot(from_zone, from_index, "")
		board_changed.emit()
		_save_game()
		return {"ok": true, "kind": "move", "item_id": source_item, "message": "Clue moved."}
	if source_item == target_item:
		var item_data := get_item(source_item)
		var result_id := str(item_data.get("merge_result_id", ""))
		if result_id.is_empty() or not item_catalog.has(result_id):
			return {"ok": false, "message": "This clue is already fully restored."}
		_set_slot(to_zone, to_index, result_id)
		_set_slot(from_zone, from_index, "")
		board_changed.emit()
		_register_task_result(result_id)
		_save_game()
		return {"ok": true, "kind": "merge", "item_id": result_id, "message": "Merged: %s" % str(get_item(result_id).get("display_name", result_id))}
	if from_zone == "inventory" and to_zone == "inventory":
		_set_slot(from_zone, from_index, target_item)
		_set_slot(to_zone, to_index, source_item)
		board_changed.emit()
		_save_game()
		return {"ok": true, "kind": "swap", "item_id": source_item, "message": "Inventory rearranged."}
	return {"ok": false, "message": "Those clues do not fit together."}
func generate_from_generator(generator_id: String) -> Dictionary:
	if generator_id == "garden_room" and current_task_index < 2:
		return {"ok": false, "message": "Garden Room unlocks after the first two evidence objectives."}
	if generator_id == "locked_attic" and current_task_index < 4:
		return {"ok": false, "message": "The attic key is still missing. Complete four evidence objectives first."}
	if not generator_catalog.has(generator_id):
		return {"ok": false, "message": "That search location is unavailable."}
	var empty_slots: Array[int] = []
	for index in range(board_items.size()):
		if str(board_items[index]).is_empty():
			empty_slots.append(index)
	if empty_slots.is_empty():
		return {"ok": false, "message": "The board is full. Move clues to inventory first."}
	if energy < GENERATOR_COST:
		return {"ok": false, "message": "Not enough energy. Each search costs 5."}
	var generator: Dictionary = generator_catalog[generator_id]
	var pool: Array = generator.get("pool", [])
	var total_weight := 0
	for entry in pool:
		if entry is Dictionary:
			total_weight += maxi(1, int(entry.get("weight", 1)))
	if total_weight <= 0:
		return {"ok": false, "message": "This search location has no clues configured."}
	if not spend_energy(GENERATOR_COST):
		return {"ok": false, "message": "Not enough energy. Each search costs 5."}
	var roll := rng.randi_range(1, total_weight)
	var selected_item := ""
	for entry in pool:
		if not (entry is Dictionary):
			continue
		roll -= maxi(1, int(entry.get("weight", 1)))
		if roll <= 0:
			selected_item = str(entry.get("item_id", ""))
			break
	if selected_item.is_empty() or not item_catalog.has(selected_item):
		energy = mini(max_energy, energy + GENERATOR_COST)
		economy_changed.emit()
		return {"ok": false, "message": "The search table is missing an evidence item."}
	var destination := empty_slots[rng.randi_range(0, empty_slots.size() - 1)]
	board_items[destination] = selected_item
	current_location = generator_id
	board_changed.emit()
	_save_game()
	return {"ok": true, "slot": destination, "item_id": selected_item, "message": "Found: %s" % str(get_item(selected_item).get("display_name", selected_item))}
func spend_energy(amount: int) -> bool:
	if amount < 0 or energy < amount:
		return false
	energy -= amount
	economy_changed.emit()
	_save_game()
	return true
func add_energy(amount: int) -> void:
	energy = clampi(energy + amount, 0, max_energy)
	economy_changed.emit()
	_save_game()
func add_coins(amount: int) -> void:
	coins = maxi(0, coins + amount)
	economy_changed.emit()
	_save_game()
func add_gems(amount: int) -> void:
	gems = maxi(0, gems + amount)
	economy_changed.emit()
	_save_game()
func set_progress(value: float) -> void:
	case_progress = clampf(value, 0.0, 1.0)
	progress_changed.emit()
func board_item_count() -> int:
	return _count_items(board_items)
func inventory_item_count() -> int:
	return _count_items(inventory_items)
func _count_items(slots: Array) -> int:
	var count := 0
	for item_id in slots:
		if not str(item_id).is_empty():
			count += 1
	return count
func _valid_slot(zone: String, index: int) -> bool:
	if zone == "board":
		return index >= 0 and index < BOARD_SIZE
	if zone == "inventory":
		return index >= 0 and index < INVENTORY_SIZE
	return false
func _set_slot(zone: String, index: int, item_id: String) -> void:
	if zone == "board":
		board_items[index] = item_id
	elif zone == "inventory":
		inventory_items[index] = item_id

func get_current_task() -> Dictionary:
	if current_task_index < 0 or current_task_index >= task_catalog.size():
		return {}
	return task_catalog[current_task_index]

func get_task_progress_text() -> String:
	if current_task_index >= task_catalog.size():
		return "CASE 01 COMPLETE"
	var task: Dictionary = get_current_task()
	return "TASK %d/%d · %s" % [current_task_index + 1, task_catalog.size(), str(task.get("title", "Investigate"))]

func _register_task_result(item_id: String) -> void:
	if current_task_index >= task_catalog.size():
		return
	var task: Dictionary = get_current_task()
	var required: Array = task.get("required_items", [])
	if not required.has(item_id):
		return
	var task_id := str(task.get("id", ""))
	if completed_tasks.has(task_id):
		return
	completed_tasks.append(task_id)
	add_coins(int(task.get("coin_reward", 0)))
	add_gems(int(task.get("gem_reward", 0)))
	var story_id := str(task.get("story_reward", ""))
	var story_text := "A new detail emerges from the evidence."
	for scene in dialogue_catalog:
		if str(scene.get("id", "")) == story_id:
			story_text = str(scene.get("text", story_text))
			break
	current_task_index += 1
	case_progress = float(current_task_index) / float(maxi(1, task_catalog.size()))
	if current_task_index >= 4:
		current_location = "locked_attic"
	elif current_task_index >= 2:
		current_location = "garden_room"
	task_completed.emit(task_id, story_text)
	progress_changed.emit()
	_save_game()

func claim_daily_reward() -> Dictionary:
	var day := _today_key()
	if daily_claimed_day == day:
		return {"ok": false, "message": "Today's detective gift has already been claimed."}
	daily_claimed_day = day
	add_coins(100)
	add_energy(10)
	_save_game()
	return {"ok": true, "message": "Daily gift: 100 coins and 10 energy."}

func purchase_energy() -> Dictionary:
	if coins < 500:
		return {"ok": false, "message": "You need 500 coins."}
	if energy >= max_energy:
		return {"ok": false, "message": "Your energy is already full."}
	coins -= 500
	add_energy(25)
	economy_changed.emit()
	_save_game()
	return {"ok": true, "message": "Purchased 25 energy for 500 coins."}

func _apply_offline_energy() -> void:
	var now := int(Time.get_unix_time_from_system())
	if last_energy_timestamp <= 0:
		last_energy_timestamp = now
		return
	var gained := mini(max_energy - energy, int(maxi(0, now - last_energy_timestamp) / 120))
	if gained > 0:
		energy += gained
		last_energy_timestamp += gained * 120
		economy_changed.emit()

func _save_game() -> void:
	if last_energy_timestamp <= 0:
		last_energy_timestamp = int(Time.get_unix_time_from_system())
	var data := {
		"version": 1, "energy": energy, "coins": coins, "gems": gems,
		"current_case": current_case, "current_location": current_location,
		"case_progress": case_progress, "sound_enabled": sound_enabled,
		"music_enabled": music_enabled, "notifications_enabled": notifications_enabled,
		"board_items": board_items, "inventory_items": inventory_items,
		"completed_tasks": completed_tasks, "current_task_index": current_task_index,
		"daily_claimed_day": daily_claimed_day, "last_energy_timestamp": last_energy_timestamp
	}
	var file := FileAccess.open(save_path, FileAccess.WRITE)
	if file:
		file.store_string(JSON.stringify(data))
	else:
		push_error("Unable to save Mystery Merge progress.")

func _load_game() -> void:
	if not FileAccess.file_exists(save_path):
		return
	var parsed: Variant = JSON.parse_string(FileAccess.get_file_as_string(save_path))
	if not (parsed is Dictionary):
		push_warning("Save file is invalid; starting a new case.")
		return
	energy = clampi(int(parsed.get("energy", energy)), 0, max_energy)
	coins = maxi(0, int(parsed.get("coins", coins)))
	gems = maxi(0, int(parsed.get("gems", gems)))
	current_case = str(parsed.get("current_case", current_case))
	current_location = str(parsed.get("current_location", current_location))
	case_progress = clampf(float(parsed.get("case_progress", case_progress)), 0.0, 1.0)
	sound_enabled = bool(parsed.get("sound_enabled", sound_enabled))
	music_enabled = bool(parsed.get("music_enabled", music_enabled))
	notifications_enabled = bool(parsed.get("notifications_enabled", notifications_enabled))
	current_task_index = clampi(int(parsed.get("current_task_index", 0)), 0, task_catalog.size())
	daily_claimed_day = int(parsed.get("daily_claimed_day", -1))
	last_energy_timestamp = int(parsed.get("last_energy_timestamp", 0))
	completed_tasks.clear()
	for task_id in parsed.get("completed_tasks", []):
		completed_tasks.append(str(task_id))
	var saved_board: Array = parsed.get("board_items", [])
	if saved_board.size() == BOARD_SIZE:
		board_items = saved_board.duplicate()
	var saved_inventory: Array = parsed.get("inventory_items", [])
	if saved_inventory.size() == INVENTORY_SIZE:
		inventory_items = saved_inventory.duplicate()
	save_loaded.emit()

func reset_local_save() -> void:
	if FileAccess.file_exists(save_path):
		DirAccess.remove_absolute(ProjectSettings.globalize_path(save_path))
	energy = 72
	coins = 12450
	gems = 38
	board_items.clear()
	inventory_items.clear()
	completed_tasks.clear()
	current_task_index = 0
	current_case = "case_01"
	current_location = "study"
	case_progress = 0.0
	sound_enabled = true
	music_enabled = true
	notifications_enabled = true
	daily_claimed_day = -1
	last_energy_timestamp = int(Time.get_unix_time_from_system())
	initialize_board()
	_save_game()
	economy_changed.emit()
	progress_changed.emit()

func _on_energy_tick() -> void:
	if energy >= max_energy:
		last_energy_timestamp = int(Time.get_unix_time_from_system())
		return
	var now := int(Time.get_unix_time_from_system())
	var gained := mini(max_energy - energy, int(maxi(0, now - last_energy_timestamp) / 120))
	if gained > 0:
		energy += gained
		last_energy_timestamp += gained * 120
		economy_changed.emit()
		_save_game()

func _today_key() -> int:
	var date := Time.get_datetime_dict_from_system()
	return int(date.year) * 1000 + int(date.month) * 32 + int(date.day)
