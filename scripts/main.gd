extends Control

const COLS := 7
const ROWS := 9
const SAVE_PATH := "user://mystery_merge_save.json"
const ITEMS := [
	{"name":"Paper Clip","icon":"📎"},
	{"name":"Case Note","icon":"🗒️"},
	{"name":"Fingerprint","icon":"🖐️"},
	{"name":"Magnifier","icon":"🔎"},
	{"name":"Photo","icon":"📷"},
	{"name":"Key","icon":"🗝️"},
	{"name":"Locket","icon":"📿"},
	{"name":"Final Clue","icon":"💎"}
]
const BG := Color("#101d2b")
const PANEL := Color("#1b3040")
const GOLD := Color("#e7b45b")
const CREAM := Color("#f4e8ce")

var board: Array[int] = []
var selected := -1
var energy := 72
var coins := 12450
var gems := 38
var task_level := 2
var task_goal := 3
var task_done := 0
var case_complete := false
var sound_on := true
var last_energy_time := 0
var daily_claimed_day := -1
var board_grid: GridContainer
var energy_label: Label
var coins_label: Label
var gems_label: Label
var task_label: Label
var story_label: Label
var status_label: Label
var tile_buttons: Array[Button] = []
var shop_panel: PanelContainer
var timer: Timer

func _ready() -> void:
	_load_game()
	_apply_energy_regen()
	_build_ui()
	_refresh()
	timer = Timer.new()
	timer.wait_time = 10.0
	timer.timeout.connect(_on_regen_tick)
	add_child(timer)
	timer.start()
	get_viewport().size_changed.connect(_on_viewport_changed)

func _build_ui() -> void:
	for child in get_children():
		remove_child(child)
		child.queue_free()
	var bg := ColorRect.new()
	bg.color = BG
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var root := VBoxContainer.new()
	root.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	root.add_theme_constant_override("separation", 10)
	root.add_theme_color_override("font_color", CREAM)
	root.add_theme_constant_override("margin_left", 18)
	root.add_theme_constant_override("margin_right", 18)
	root.add_theme_constant_override("margin_top", 16)
	root.add_theme_constant_override("margin_bottom", 12)
	add_child(root)

	var header := HBoxContainer.new()
	root.add_child(header)
	var title := Label.new()
	title.text = "MYSTERY MERGE"
	title.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	title.add_theme_font_size_override("font_size", 27)
	title.add_theme_color_override("font_color", GOLD)
	header.add_child(title)
	var case_badge := Label.new()
	case_badge.text = "CASE 01"
	case_badge.add_theme_color_override("font_color", CREAM)
	case_badge.add_theme_font_size_override("font_size", 17)
	header.add_child(case_badge)

	var subtitle := Label.new()
	subtitle.text = "THE MISSING LOCKET  •  THE HALE HOUSE"
	subtitle.add_theme_font_size_override("font_size", 13)
	subtitle.add_theme_color_override("font_color", Color("#a9bac4"))
	root.add_child(subtitle)

	var wallet := HBoxContainer.new()
	root.add_child(wallet)
	energy_label = _stat_label("⚡  %d/100" % energy)
	coins_label = _stat_label("🪙  %d" % coins)
	gems_label = _stat_label("💎  %d" % gems)
	wallet.add_child(energy_label)
	wallet.add_child(coins_label)
	wallet.add_child(gems_label)

	var task_card := PanelContainer.new()
	task_card.add_theme_stylebox_override("panel", _panel_style(Color("#263d4c")))
	root.add_child(task_card)
	var task_box := VBoxContainer.new()
	task_card.add_child(task_box)
	task_label = Label.new()
	task_label.add_theme_font_size_override("font_size", 16)
	task_label.add_theme_color_override("font_color", GOLD)
	task_box.add_child(task_label)
	story_label = Label.new()
	story_label.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	story_label.add_theme_font_size_override("font_size", 13)
	story_label.add_theme_color_override("font_color", CREAM)
	story_label.text = "Nora found a strange trail of clues in the study. Merge evidence to uncover what happened."
	task_box.add_child(story_label)

	var board_title := HBoxContainer.new()
	root.add_child(board_title)
	var board_caption := Label.new()
	board_caption.text = "EVIDENCE BOARD"
	board_caption.add_theme_color_override("font_color", GOLD)
	board_caption.add_theme_font_size_override("font_size", 16)
	board_caption.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	board_title.add_child(board_caption)
	var board_hint := Label.new()
	board_hint.text = "Tap two matching pieces"
	board_hint.add_theme_font_size_override("font_size", 12)
	board_hint.add_theme_color_override("font_color", Color("#a9bac4"))
	board_title.add_child(board_hint)

	board_grid = GridContainer.new()
	board_grid.columns = COLS
	board_grid.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	board_grid.size_flags_vertical = Control.SIZE_EXPAND_FILL
	board_grid.add_theme_constant_override("h_separation", 4)
	board_grid.add_theme_constant_override("v_separation", 4)
	root.add_child(board_grid)
	tile_buttons.clear()
	for i in range(ROWS * COLS):
		var b := Button.new()
		b.custom_minimum_size = Vector2(0, 66)
		b.size_flags_horizontal = Control.SIZE_EXPAND_FILL
		b.size_flags_vertical = Control.SIZE_EXPAND_FILL
		b.add_theme_font_size_override("font_size", 19)
		b.add_theme_color_override("font_color", CREAM)
		b.add_theme_stylebox_override("normal", _tile_style(false))
		b.add_theme_stylebox_override("hover", _tile_style(true))
		b.add_theme_stylebox_override("pressed", _tile_style(true))
		b.pressed.connect(_on_tile_pressed.bind(i))
		board_grid.add_child(b)
		tile_buttons.append(b)

	status_label = Label.new()
	status_label.text = "Select a piece, then tap a matching neighbour to merge."
	status_label.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	status_label.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	status_label.add_theme_font_size_override("font_size", 12)
	status_label.add_theme_color_override("font_color", Color("#b6c8d0"))
	root.add_child(status_label)

	var actions := GridContainer.new()
	actions.columns = 3
	root.add_child(actions)
	_add_action(actions, "⚙ GENERATE  •  5⚡", _on_generate)
	_add_action(actions, "🛍 SHOP", _open_shop)
	_add_action(actions, "🎁 DAILY", _claim_daily)
	_add_action(actions, "📖 CASE FILE", _show_case_file)
	_add_action(actions, "▶ REWARD VIDEO", _reward_ad)
	_add_action(actions, "💾 SAVE", _save_game)
	shop_panel = PanelContainer.new()
	shop_panel.visible = false
	shop_panel.add_theme_stylebox_override("panel", _panel_style(Color("#314757")))
	root.add_child(shop_panel)
	_build_shop()

func _stat_label(value: String) -> Label:
	var l := Label.new()
	l.text = value
	l.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	l.add_theme_font_size_override("font_size", 16)
	l.add_theme_color_override("font_color", CREAM)
	return l

func _panel_style(color: Color) -> StyleBoxFlat:
	var s := StyleBoxFlat.new()
	s.bg_color = color
	s.corner_radius_top_left = 12
	s.corner_radius_top_right = 12
	s.corner_radius_bottom_left = 12
	s.corner_radius_bottom_right = 12
	s.content_margin_left = 12
	s.content_margin_right = 12
	s.content_margin_top = 9
	s.content_margin_bottom = 9
	return s

func _tile_style(highlight: bool) -> StyleBoxFlat:
	var s := StyleBoxFlat.new()
	s.bg_color = Color("#496071") if highlight else Color("#263b4b")
	s.border_color = GOLD if highlight else Color("#3a5261")
	s.set_border_width_all(2 if highlight else 1)
	s.corner_radius_top_left = 7
	s.corner_radius_top_right = 7
	s.corner_radius_bottom_left = 7
	s.corner_radius_bottom_right = 7
	return s

func _add_action(parent: GridContainer, caption: String, callback: Callable) -> void:
	var b := Button.new()
	b.text = caption
	b.custom_minimum_size = Vector2(0, 42)
	b.add_theme_font_size_override("font_size", 12)
	b.add_theme_color_override("font_color", CREAM)
	b.add_theme_stylebox_override("normal", _panel_style(Color("#294454")))
	b.pressed.connect(callback)
	parent.add_child(b)

func _build_shop() -> void:
	var box := VBoxContainer.new()
	shop_panel.add_child(box)
	var heading := Label.new()
	heading.text = "FIELD SUPPLIES"
	heading.add_theme_color_override("font_color", GOLD)
	heading.add_theme_font_size_override("font_size", 17)
	box.add_child(heading)
	var energy_buy := Button.new()
	energy_buy.text = "Buy 25 Energy  —  500 Coins"
	energy_buy.pressed.connect(_buy_energy)
	box.add_child(energy_buy)
	var close := Button.new()
	close.text = "Close"
	close.pressed.connect(func(): shop_panel.visible = false)
	box.add_child(close)

func _refresh() -> void:
	if energy_label == null:
		return
	energy_label.text = "⚡  %d/100" % energy
	coins_label.text = "🪙  %d" % coins
	gems_label.text = "💎  %d" % gems
	if case_complete:
		task_label.text = "CASE COMPLETE — THE MISSING LOCKET"
		story_label.text = "The locket has been recovered. The evidence points to a hidden compartment behind the study clock. More cases are coming."
	else:
		task_label.text = "TASK %d  •  Merge %d more %s" % [_task_number(), maxi(0, task_goal - task_done), ITEMS[task_level - 1].name]
	for i in range(tile_buttons.size()):
		var level: int = board[i]
		var b: Button = tile_buttons[i]
		if level <= 0:
			b.text = ""
			b.disabled = false
		else:
			b.text = ITEMS[mini(level - 1, ITEMS.size() - 1)].icon
			b.tooltip_text = ITEMS[mini(level - 1, ITEMS.size() - 1)].name
			b.disabled = false
		b.add_theme_stylebox_override("normal", _tile_style(i == selected))

func _task_number() -> int:
	if task_level == 2: return 1
	if task_level == 3: return 2
	return 3

func _on_tile_pressed(index: int) -> void:
	if case_complete:
		status_label.text = "Case complete. Open the case file for the conclusion."
		return
	if selected == -1:
		if board[index] <= 0:
			status_label.text = "That space is empty. Generate evidence first."
			return
		selected = index
		status_label.text = "Selected %s. Choose an adjacent matching piece." % ITEMS[board[index] - 1].name
		_refresh()
		return
	if selected == index:
		selected = -1
		_refresh()
		return
	var from_level: int = board[selected]
	var to_level: int = board[index]
	if from_level > 0 and from_level == to_level and _are_adjacent(selected, index):
		board[index] = mini(to_level + 1, ITEMS.size())
		board[selected] = 0
		selected = -1
		_on_merge(board[index])
		status_label.text = "Merged into %s!" % ITEMS[board[index] - 1].name
		_save_game()
	elif to_level == 0 and from_level > 0 and _are_adjacent(selected, index):
		board[index] = from_level
		board[selected] = 0
		selected = -1
		status_label.text = "Evidence moved."
		_save_game()
	else:
		if to_level > 0:
			selected = index
			status_label.text = "Piece selected. Match it with an adjacent identical piece."
		else:
			status_label.text = "Move evidence only to an adjacent empty space."
	_refresh()

func _are_adjacent(a: int, b: int) -> bool:
	var ar := int(a / COLS)
	var ac := a % COLS
	var br := int(b / COLS)
	var bc := b % COLS
	return abs(ar - br) + abs(ac - bc) == 1

func _on_merge(level: int) -> void:
	if level >= task_level:
		task_done += 1
		if task_done >= task_goal:
			coins += 250
			if task_level == 2:
				task_level = 3
				task_goal = 2
			elif task_level == 3:
				task_level = 4
				task_goal = 1
			else:
				case_complete = true
				gems += 10
			task_done = 0
			story_label.text = "New evidence unlocked. Nora follows the clues deeper into the house."
			status_label.text = "Task completed! +250 coins."
	_refresh()

func _on_generate() -> void:
	if not _spend_energy(5):
		status_label.text = "Not enough energy. Wait for regeneration or visit the shop."
		return
	var empty: Array[int] = []
	for i in range(board.size()):
		if board[i] == 0:
			empty.append(i)
	if empty.is_empty():
		energy = mini(100, energy + 5)
		status_label.text = "Board full. Move pieces or merge them to make room."
	else:
		var spot: int = empty[randi() % empty.size()]
		board[spot] = 1
		status_label.text = "Fresh evidence generated in the study."
	_save_game()
	_refresh()

func _spend_energy(amount: int) -> bool:
	if energy < amount:
		return false
	energy -= amount
	return true

func _buy_energy() -> void:
	if coins < 500:
		status_label.text = "You need 500 coins for energy."
	else:
		coins -= 500
		energy = mini(100, energy + 25)
		status_label.text = "Purchased 25 energy."
		_save_game()
	_refresh()

func _open_shop() -> void:
	shop_panel.visible = not shop_panel.visible
	_refresh()

func _claim_daily() -> void:
	var day := int(Time.get_unix_time_from_system() / 86400)
	if daily_claimed_day == day:
		status_label.text = "Daily reward already collected. Come back tomorrow."
	else:
		daily_claimed_day = day
		coins += 500
		energy = mini(100, energy + 10)
		status_label.text = "Daily reward collected: 500 coins and 10 energy!"
		_save_game()
	_refresh()

func _reward_ad() -> void:
	energy = mini(100, energy + 10)
	status_label.text = "Reward video demo: +10 energy. Connect an ad network before release."
	_save_game()
	_refresh()

func _show_case_file() -> void:
	if case_complete:
		story_label.text = "CASE CLOSED: The Missing Locket. Nora found the locket hidden behind the study clock. Case 02 — The Silent Portrait — is coming soon."
	else:
		story_label.text = "CASE FILE: Claire reported the locket missing after the family dinner. Search the study, merge evidence and follow the trail."
	status_label.text = "Case notes updated."

func _on_regen_tick() -> void:
	var now := int(Time.get_unix_time_from_system())
	var elapsed := now - last_energy_time
	if elapsed >= 120 and energy < 100:
		var gained := mini(100 - energy, int(elapsed / 120))
		energy += gained
		last_energy_time += gained * 120
		_save_game()
		_refresh()

func _apply_energy_regen() -> void:
	var now := int(Time.get_unix_time_from_system())
	if last_energy_time <= 0:
		last_energy_time = now
	var gained := mini(100 - energy, int(maxi(0, now - last_energy_time) / 120))
	energy += gained
	last_energy_time += gained * 120

func _save_game() -> void:
	last_energy_time = int(Time.get_unix_time_from_system())
	var data := {
		"board": board, "energy": energy, "coins": coins, "gems": gems,
		"task_level": task_level, "task_goal": task_goal, "task_done": task_done,
		"case_complete": case_complete, "last_energy_time": last_energy_time,
		"daily_claimed_day": daily_claimed_day, "sound_on": sound_on
	}
	var file := FileAccess.open(SAVE_PATH, FileAccess.WRITE)
	if file:
		file.store_string(JSON.stringify(data))
		status_label.text = "Progress saved on this device."
	_refresh()

func _load_game() -> void:
	var file := FileAccess.open(SAVE_PATH, FileAccess.READ)
	if file:
		var parsed = JSON.parse_string(file.get_as_text())
		if parsed is Dictionary:
			energy = int(parsed.get("energy", energy))
			coins = int(parsed.get("coins", coins))
			gems = int(parsed.get("gems", gems))
			task_level = int(parsed.get("task_level", task_level))
			task_goal = int(parsed.get("task_goal", task_goal))
			task_done = int(parsed.get("task_done", task_done))
			case_complete = bool(parsed.get("case_complete", false))
			last_energy_time = int(parsed.get("last_energy_time", 0))
			daily_claimed_day = int(parsed.get("daily_claimed_day", -1))
			sound_on = bool(parsed.get("sound_on", true))
			var saved_board = parsed.get("board", [])
			if saved_board is Array and saved_board.size() == ROWS * COLS:
				board.clear()
				for value in saved_board:
					board.append(int(value))
	if board.is_empty():
		board.resize(ROWS * COLS)
		board.fill(0)
		for i in range(24):
			var spot := randi() % board.size()
			board[spot] = 1
		for i in range(5):
			var spot := randi() % board.size()
			board[spot] = 2
		last_energy_time = int(Time.get_unix_time_from_system())

func _on_viewport_changed() -> void:
	for b in tile_buttons:
		b.custom_minimum_size.y = clampf(get_viewport_rect().size.y / 19.0, 48.0, 76.0)
