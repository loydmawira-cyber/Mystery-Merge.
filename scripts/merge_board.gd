extends Control
const UI = preload("res://scripts/ui_theme.gd")
const TILE_SCRIPT = preload("res://scripts/evidence_tile.gd")
var board_tiles: Array = []
var inventory_tiles: Array = []
var energy_label: Label
var coin_label: Label
var gem_label: Label
var item_count_label: Label
var inventory_count_label: Label
var feedback_label: Label
var task_label: Label
var board_grid: GridContainer
var inventory_grid: GridContainer
var selected_source: Dictionary = {}
func _ready() -> void:
	GameState.initialize_board()
	_build_ui()
	GameState.economy_changed.connect(_refresh_economy)
	GameState.board_changed.connect(_refresh_board)
	_refresh_economy()
	_refresh_board()
	_refresh_task()
	GameState.progress_changed.connect(_refresh_task)
	GameState.task_completed.connect(_on_task_completed)
func _build_ui() -> void:
	var background := ColorRect.new()
	background.color = UI.NAVY
	background.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(background)
	var margin := MarginContainer.new()
	margin.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	margin.add_theme_constant_override("margin_left", 13)
	margin.add_theme_constant_override("margin_right", 13)
	margin.add_theme_constant_override("margin_top", 12)
	margin.add_theme_constant_override("margin_bottom", 12)
	add_child(margin)
	var root := VBoxContainer.new()
	root.add_theme_constant_override("separation", 8)
	margin.add_child(root)
	var header := HBoxContainer.new()
	header.add_theme_constant_override("separation", 8)
	root.add_child(header)
	var back := _button("‹", UI.NAVY_LIGHT, UI.CREAM, 48)
	back.pressed.connect(func(): NavigationManager.show_screen("home"))
	header.add_child(back)
	var title_col := VBoxContainer.new()
	title_col.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	title_col.add_theme_constant_override("separation", 0)
	header.add_child(title_col)
	var title := _label("THE EVIDENCE BOARD", 19, UI.GOLD)
	title_col.add_child(title)
	var subtitle := _label("CASE 01 · THE MISSING LOCKET", 10, UI.MUTED)
	title_col.add_child(subtitle)
	energy_label = _label("⚡ 72", 14, UI.CREAM)
	header.add_child(energy_label)
	var currency_row := HBoxContainer.new()
	currency_row.alignment = BoxContainer.ALIGNMENT_END
	currency_row.add_theme_constant_override("separation", 12)
	root.add_child(currency_row)
	coin_label = _label("◉ 12,450", 12, UI.GOLD)
	currency_row.add_child(coin_label)
	gem_label = _label("◆ 38", 12, Color("d7a5ff"))
	currency_row.add_child(gem_label)
	task_label = _label(GameState.get_task_progress_text(), 12, UI.GOLD)
	task_label.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	root.add_child(task_label)
	feedback_label = _label("Drag matching clues together to restore them.", 13, UI.PAPER)
	feedback_label.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	feedback_label.custom_minimum_size.y = 24
	root.add_child(feedback_label)
	var generator_panel := PanelContainer.new()
	generator_panel.add_theme_stylebox_override("panel", UI.panel(Color("172e3a"), UI.GOLD_DARK, 14))
	root.add_child(generator_panel)
	var generator_col := VBoxContainer.new()
	generator_col.add_theme_constant_override("separation", 5)
	generator_panel.add_child(generator_col)
	var generator_title := _label("SEARCH THE MANSION  ·  5 ENERGY EACH", 11, UI.GOLD)
	generator_col.add_child(generator_title)
	var generator_row := HBoxContainer.new()
	generator_row.add_theme_constant_override("separation", 6)
	generator_col.add_child(generator_row)
	_add_generator_button(generator_row, "study", "STUDY", "Desk & drawers")
	_add_generator_button(generator_row, "garden_room", "GARDEN", "Paths & flowers")
	_add_generator_button(generator_row, "locked_attic", "ATTIC", "Old keepsakes")
	var board_header := HBoxContainer.new()
	root.add_child(board_header)
	var board_title := _label("MERGE BOARD  ·  7 × 9", 13, UI.CREAM)
	board_title.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	board_header.add_child(board_title)
	item_count_label = _label("12 / 63 clues", 11, UI.MUTED)
	board_header.add_child(item_count_label)
	var board_panel := PanelContainer.new()
	board_panel.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	board_panel.add_theme_stylebox_override("panel", UI.panel(Color("0c202c"), Color("31515b"), 13))
	root.add_child(board_panel)
	board_grid = GridContainer.new()
	board_grid.columns = GameState.BOARD_COLUMNS
	board_grid.add_theme_constant_override("h_separation", 4)
	board_grid.add_theme_constant_override("v_separation", 4)
	board_panel.add_child(board_grid)
	for index in range(GameState.BOARD_SIZE):
		var tile = TILE_SCRIPT.new()
		tile.configure(self, "board", index)
		board_grid.add_child(tile)
		board_tiles.append(tile)
	var inventory_header := HBoxContainer.new()
	root.add_child(inventory_header)
	var inventory_title := _label("POCKET INVENTORY", 12, UI.GOLD)
	inventory_title.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	inventory_header.add_child(inventory_title)
	inventory_count_label = _label("0 / 8", 11, UI.MUTED)
	inventory_header.add_child(inventory_count_label)
	var inventory_panel := PanelContainer.new()
	inventory_panel.add_theme_stylebox_override("panel", UI.panel(Color("172e3a"), UI.GOLD_DARK, 12))
	root.add_child(inventory_panel)
	inventory_grid = GridContainer.new()
	inventory_grid.columns = GameState.INVENTORY_SIZE
	inventory_grid.add_theme_constant_override("h_separation", 4)
	inventory_panel.add_child(inventory_grid)
	for index in range(GameState.INVENTORY_SIZE):
		var tile = TILE_SCRIPT.new()
		tile.configure(self, "inventory", index)
		inventory_grid.add_child(tile)
		inventory_tiles.append(tile)
	var hint := _label("Tap a clue, then tap a space — or drag it. Store extras below.", 10, UI.MUTED)
	hint.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	root.add_child(hint)
	var footer := HBoxContainer.new()
	footer.add_theme_constant_override("separation", 7)
	root.add_child(footer)
	var home_button := _button("HOME", UI.NAVY_LIGHT, UI.CREAM, 0)
	home_button.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	home_button.pressed.connect(func(): NavigationManager.show_screen("home"))
	footer.add_child(home_button)
	var shop_button := _button("SHOP", UI.NAVY_LIGHT, UI.CREAM, 0)
	shop_button.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	shop_button.pressed.connect(func(): NavigationManager.show_screen("shop"))
	footer.add_child(shop_button)
	var settings_button := _button("SETTINGS", UI.NAVY_LIGHT, UI.CREAM, 0)
	settings_button.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	settings_button.pressed.connect(func(): NavigationManager.show_screen("settings"))
	footer.add_child(settings_button)
func _add_generator_button(parent: HBoxContainer, generator_id: String, title: String, detail: String) -> void:
	var button := Button.new()
	button.text = "%s\n%s" % [title, detail]
	button.custom_minimum_size.y = 48
	button.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	button.add_theme_font_size_override("font_size", 11)
	button.add_theme_color_override("font_color", UI.CREAM)
	button.add_theme_color_override("font_hover_color", UI.CREAM)
	button.add_theme_stylebox_override("normal", UI.button_style(Color("20505a"), UI.GOLD_DARK))
	button.add_theme_stylebox_override("hover", UI.button_style(Color("2a6670"), UI.GOLD))
	button.add_theme_stylebox_override("pressed", UI.button_style(Color("153e47"), UI.GOLD))
	button.pressed.connect(_on_generator_pressed.bind(generator_id))
	parent.add_child(button)
func _label(value: String, size: int, color: Color) -> Label:
	var label := Label.new()
	label.text = value
	label.add_theme_font_size_override("font_size", size)
	label.add_theme_color_override("font_color", color)
	label.vertical_alignment = VERTICAL_ALIGNMENT_CENTER
	return label
func _button(value: String, bg: Color, fg: Color, min_width: float) -> Button:
	var button := Button.new()
	button.text = value
	button.custom_minimum_size = Vector2(min_width, 42)
	button.add_theme_font_size_override("font_size", 13)
	button.add_theme_color_override("font_color", fg)
	button.add_theme_color_override("font_hover_color", fg)
	button.add_theme_stylebox_override("normal", UI.button_style(bg, UI.GOLD_DARK))
	button.add_theme_stylebox_override("hover", UI.button_style(bg.lightened(0.08), UI.GOLD))
	button.add_theme_stylebox_override("pressed", UI.button_style(bg.darkened(0.12), UI.GOLD))
	button.add_theme_stylebox_override("focus", StyleBoxEmpty.new())
	return button
func _on_generator_pressed(generator_id: String) -> void:
	var result := GameState.generate_from_generator(generator_id)
	if not bool(result.get("ok", false)):
		_show_feedback(str(result.get("message", "Search unavailable.")), false)
		return
	selected_source.clear()
	_refresh_selection()
	_show_feedback(str(result.get("message", "A new clue was found.")), true)
	_animate_tile(board_tiles[int(result.get("slot", 0))], true)
func on_tile_tapped(zone: String, index: int) -> void:
	var tapped_item := GameState.get_slot(zone, index)
	if selected_source.is_empty():
		if tapped_item.is_empty():
			return
		selected_source = {"from_zone": zone, "from_index": index}
		_refresh_selection()
		_show_feedback("Clue selected. Choose a destination or matching clue.", true)
		return
	var source_zone := str(selected_source.get("from_zone", ""))
	var source_index := int(selected_source.get("from_index", -1))
	if source_zone == zone and source_index == index:
		selected_source.clear()
		_refresh_selection()
		_show_feedback("Selection cleared.", true)
		return
	perform_drop(source_zone, source_index, zone, index)
func perform_drop(from_zone: String, from_index: int, to_zone: String, to_index: int) -> void:
	var result := GameState.transfer_item(from_zone, from_index, to_zone, to_index)
	selected_source.clear()
	_refresh_selection()
	if not bool(result.get("ok", false)):
		_show_feedback(str(result.get("message", "That move is not possible.")), false)
		return
	var is_merge := str(result.get("kind", "")) == "merge"
	_show_feedback(str(result.get("message", "Clue moved.")), true)
	var target_tile := _tile_for(to_zone, to_index)
	if target_tile != null:
		_animate_tile(target_tile, is_merge)
func _refresh_board() -> void:
	for index in range(board_tiles.size()):
		board_tiles[index].render_item(GameState.get_slot("board", index))
	for index in range(inventory_tiles.size()):
		inventory_tiles[index].render_item(GameState.get_slot("inventory", index))
	if is_instance_valid(item_count_label):
		item_count_label.text = "%d / %d clues" % [GameState.board_item_count(), GameState.BOARD_SIZE]
	if is_instance_valid(inventory_count_label):
		inventory_count_label.text = "%d / %d" % [GameState.inventory_item_count(), GameState.INVENTORY_SIZE]
	_refresh_selection()
func _refresh_economy() -> void:
	if is_instance_valid(energy_label):
		energy_label.text = "⚡ %d/%d" % [GameState.energy, GameState.max_energy]
	if is_instance_valid(coin_label):
		coin_label.text = "◉ %s" % _commas(GameState.coins)
	if is_instance_valid(gem_label):
		gem_label.text = "◆ %d" % GameState.gems
func _refresh_selection() -> void:
	for index in range(board_tiles.size()):
		var selected := not selected_source.is_empty() and str(selected_source.get("from_zone", "")) == "board" and int(selected_source.get("from_index", -1)) == index
		board_tiles[index].set_selected(selected)
	for index in range(inventory_tiles.size()):
		var selected := not selected_source.is_empty() and str(selected_source.get("from_zone", "")) == "inventory" and int(selected_source.get("from_index", -1)) == index
		inventory_tiles[index].set_selected(selected)
func _tile_for(zone: String, index: int) -> Control:
	if zone == "board" and index >= 0 and index < board_tiles.size():
		return board_tiles[index]
	if zone == "inventory" and index >= 0 and index < inventory_tiles.size():
		return inventory_tiles[index]
	return null
func _animate_tile(tile: Control, merged: bool) -> void:
	if not is_instance_valid(tile):
		return
	tile.pivot_offset = tile.size * 0.5
	tile.scale = Vector2(0.82, 0.82) if merged else Vector2(0.94, 0.94)
	var tween := create_tween()
	tween.tween_property(tile, "scale", Vector2(1.10, 1.10), 0.11).set_trans(Tween.TRANS_BACK).set_ease(Tween.EASE_OUT)
	tween.tween_property(tile, "scale", Vector2.ONE, 0.12).set_trans(Tween.TRANS_BACK).set_ease(Tween.EASE_IN_OUT)
func _show_feedback(message: String, success: bool) -> void:
	feedback_label.text = message
	feedback_label.add_theme_color_override("font_color", UI.GOLD if success else Color("f08b80"))
	feedback_label.pivot_offset = feedback_label.size * 0.5
	feedback_label.scale = Vector2(0.96, 0.96)
	var tween := create_tween()
	tween.tween_property(feedback_label, "scale", Vector2.ONE, 0.12)
func _commas(value: int) -> String:
	var digits := str(value)
	var output := ""
	while digits.length() > 3:
		output = "," + digits.substr(digits.length() - 3) + output
		digits = digits.substr(0, digits.length() - 3)
	return digits + output

func _refresh_task() -> void:
	if is_instance_valid(task_label):
		task_label.text = GameState.get_task_progress_text()

func _on_task_completed(task_id: String, story_text: String) -> void:
	_refresh_task()
	var dialog := AcceptDialog.new()
	dialog.title = "Evidence Objective Complete"
	dialog.dialog_text = "%s\n\n%s" % [task_id.replace("_", " ").capitalize(), story_text]
	add_child(dialog)
	dialog.popup_centered()
