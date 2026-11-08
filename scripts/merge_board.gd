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
	GameState.progress_changed.connect(_refresh_task)
	GameState.task_completed.connect(_on_task_completed)
	_refresh_economy()
	_refresh_board()
	_refresh_task()

func _build_ui() -> void:
	var bg := ColorRect.new()
	bg.color = Color("091923")
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var glow := ColorRect.new()
	glow.color = Color(0.07, 0.22, 0.27, 0.22)
	glow.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(glow)
	var outer := MarginContainer.new()
	outer.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	outer.add_theme_constant_override("margin_left", 12)
	outer.add_theme_constant_override("margin_right", 12)
	outer.add_theme_constant_override("margin_top", 10)
	outer.add_theme_constant_override("margin_bottom", 10)
	add_child(outer)
	var root := VBoxContainer.new()
	root.add_theme_constant_override("separation", 7)
	outer.add_child(root)

	var top := HBoxContainer.new()
	top.add_theme_constant_override("separation", 7)
	root.add_child(top)
	var crest := _label("✦", 25, UI.GOLD)
	crest.custom_minimum_size.x = 34
	crest.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	top.add_child(crest)
	var brand := VBoxContainer.new()
	brand.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	brand.add_child(_label("MYSTERY MERGE", 18, UI.GOLD))
	brand.add_child(_label("CASE 01  ·  THE MISSING LOCKET", 9, UI.MUTED))
	top.add_child(brand)
	energy_label = _resource_chip(top, "⚡", "72/100", UI.GOLD)
	coin_label = _resource_chip(top, "◉", "12,450", UI.GOLD)
	gem_label = _resource_chip(top, "◆", "38", Color("d9a4ff"))

	var case_card := PanelContainer.new()
	case_card.add_theme_stylebox_override("panel", UI.panel(Color("1b3945"), UI.GOLD_DARK, 13))
	root.add_child(case_card)
	var case_row := HBoxContainer.new()
	case_row.add_theme_constant_override("separation", 10)
	case_card.add_child(case_row)
	var mansion := _label("⌂", 38, UI.GOLD)
	mansion.custom_minimum_size.x = 55
	mansion.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	case_row.add_child(mansion)
	var case_copy := VBoxContainer.new()
	case_copy.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	case_copy.add_child(_label("THE MISSING LOCKET", 17, UI.CREAM))
	case_copy.add_child(_label("Merge evidence. Uncover the truth.", 10, UI.PAPER))
	case_row.add_child(case_copy)
	var cases := _small_button("CASE FILE", UI.TEAL, UI.CREAM)
	cases.pressed.connect(func(): NavigationManager.show_screen("cases"))
	case_row.add_child(cases)

	var objective := PanelContainer.new()
	objective.add_theme_stylebox_override("panel", UI.panel(Color("d9c39e"), Color("9c7440"), 10))
	root.add_child(objective)
	var obj_row := HBoxContainer.new()
	objective.add_child(obj_row)
	var obj_icon := _label("▤", 20, UI.INK)
	obj_row.add_child(obj_icon)
	task_label = _label(GameState.get_task_progress_text(), 12, UI.INK)
	task_label.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	task_label.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	obj_row.add_child(task_label)
	item_count_label = _label("0/63", 10, UI.INK)
	obj_row.add_child(item_count_label)

	var gen_head := HBoxContainer.new()
	root.add_child(gen_head)
	var gen_title := _label("SEARCH LOCATIONS", 11, UI.GOLD)
	gen_title.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	gen_head.add_child(gen_title)
	gen_head.add_child(_label("5 ENERGY PER SEARCH", 9, UI.MUTED))
	var gen_row := HBoxContainer.new()
	gen_row.add_theme_constant_override("separation", 6)
	root.add_child(gen_row)
	_add_generator_button(gen_row, "study", "STUDY", "Desk & drawers")
	_add_generator_button(gen_row, "garden_room", "GARDEN", "Footprints")
	_add_generator_button(gen_row, "locked_attic", "ATTIC", "Keepsakes")

	var board_head := HBoxContainer.new()
	root.add_child(board_head)
	var board_title := _label("EVIDENCE BOARD", 13, UI.CREAM)
	board_title.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	board_head.add_child(board_title)
	board_head.add_child(_label("7 × 9  ·  TAP OR DRAG TO MERGE", 9, UI.MUTED))
	var board_panel := PanelContainer.new()
	board_panel.size_flags_vertical = Control.SIZE_EXPAND_FILL
	board_panel.add_theme_stylebox_override("panel", UI.panel(Color("0b2732"), Color("39727a"), 12))
	root.add_child(board_panel)
	board_grid = GridContainer.new()
	board_grid.columns = GameState.BOARD_COLUMNS
	board_grid.add_theme_constant_override("h_separation", 4)
	board_grid.add_theme_constant_override("v_separation", 4)
	board_panel.add_child(board_grid)
	for i in range(GameState.BOARD_SIZE):
		var tile = TILE_SCRIPT.new()
		tile.configure(self, "board", i)
		board_grid.add_child(tile)
		board_tiles.append(tile)

	var inventory_panel := PanelContainer.new()
	inventory_panel.add_theme_stylebox_override("panel", UI.panel(Color("132f3b"), UI.GOLD_DARK, 11))
	root.add_child(inventory_panel)
	var inv_col := VBoxContainer.new()
	inv_col.add_theme_constant_override("separation", 4)
	inventory_panel.add_child(inv_col)
	var inv_head := HBoxContainer.new()
	inv_col.add_child(inv_head)
	var inv_title := _label("FIELD BAG", 10, UI.GOLD)
	inv_title.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	inv_head.add_child(inv_title)
	inventory_count_label = _label("0/8", 9, UI.MUTED)
	inv_head.add_child(inventory_count_label)
	inventory_grid = GridContainer.new()
	inventory_grid.columns = GameState.INVENTORY_SIZE
	inventory_grid.add_theme_constant_override("h_separation", 4)
	inv_col.add_child(inventory_grid)
	for i in range(GameState.INVENTORY_SIZE):
		var tile = TILE_SCRIPT.new()
		tile.configure(self, "inventory", i)
		inventory_grid.add_child(tile)
		inventory_tiles.append(tile)

	feedback_label = _label("Tap a clue, then choose a matching clue to merge.", 10, UI.PAPER)
	feedback_label.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	feedback_label.custom_minimum_size.y = 17
	root.add_child(feedback_label)
	var footer := HBoxContainer.new()
	footer.add_theme_constant_override("separation", 6)
	root.add_child(footer)
	_add_nav(footer, "⌂  HOME", "home")
	_add_nav(footer, "▤  CASES", "cases")
	_add_nav(footer, "♜  SHOP", "shop")
	_add_nav(footer, "⚙", "settings")

func _resource_chip(parent: HBoxContainer, symbol: String, value: String, color: Color) -> Label:
	var chip := Label.new()
	chip.text = symbol + " " + value
	chip.add_theme_font_size_override("font_size", 10)
	chip.add_theme_color_override("font_color", color)
	chip.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	chip.vertical_alignment = VERTICAL_ALIGNMENT_CENTER
	chip.custom_minimum_size.x = 63
	parent.add_child(chip)
	return chip

func _add_generator_button(parent: HBoxContainer, id: String, title: String, detail: String) -> void:
	var b := _small_button(title + "\n" + detail, Color("1c4b55"), UI.CREAM)
	b.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	b.custom_minimum_size.y = 43
	b.add_theme_font_size_override("font_size", 10)
	b.pressed.connect(_on_generator_pressed.bind(id))
	parent.add_child(b)

func _add_nav(parent: HBoxContainer, title: String, screen: String) -> void:
	var b := _small_button(title, Color("142d39"), UI.CREAM)
	b.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	b.custom_minimum_size.y = 42
	b.add_theme_font_size_override("font_size", 10)
	b.pressed.connect(func(): NavigationManager.show_screen(screen))
	parent.add_child(b)

func _small_button(caption: String, bg: Color, fg: Color) -> Button:
	var b := Button.new()
	b.text = caption
	b.add_theme_font_size_override("font_size", 11)
	b.add_theme_color_override("font_color", fg)
	b.add_theme_color_override("font_hover_color", fg)
	b.add_theme_stylebox_override("normal", UI.button_style(bg, UI.GOLD_DARK))
	b.add_theme_stylebox_override("hover", UI.button_style(bg.lightened(0.09), UI.GOLD))
	b.add_theme_stylebox_override("pressed", UI.button_style(bg.darkened(0.1), UI.GOLD))
	b.add_theme_stylebox_override("focus", StyleBoxEmpty.new())
	return b

func _label(value: String, size: int, color: Color) -> Label:
	var l := Label.new()
	l.text = value
	l.add_theme_font_size_override("font_size", size)
	l.add_theme_color_override("font_color", color)
	l.vertical_alignment = VERTICAL_ALIGNMENT_CENTER
	return l

func _on_generator_pressed(id: String) -> void:
	var result := GameState.generate_from_generator(id)
	if not bool(result.get("ok", false)):
		_show_feedback(str(result.get("message", "Search unavailable.")), false)
		return
	selected_source.clear()
	_refresh_selection()
	_show_feedback(str(result.get("message", "A clue was found.")), true)
	_animate_tile(board_tiles[int(result.get("slot", 0))], true)

func on_tile_tapped(zone: String, index: int) -> void:
	var item := GameState.get_slot(zone, index)
	if selected_source.is_empty():
		if item.is_empty():
			return
		selected_source = {"from_zone": zone, "from_index": index}
		_refresh_selection()
		_show_feedback("Clue selected — choose its match or an empty space.", true)
		return
	var from_zone := str(selected_source.get("from_zone", ""))
	var from_index := int(selected_source.get("from_index", -1))
	if from_zone == zone and from_index == index:
		selected_source.clear()
		_refresh_selection()
		_show_feedback("Selection cleared.", true)
		return
	perform_drop(from_zone, from_index, zone, index)

func perform_drop(from_zone: String, from_index: int, to_zone: String, to_index: int) -> void:
	var result := GameState.transfer_item(from_zone, from_index, to_zone, to_index)
	selected_source.clear()
	_refresh_selection()
	if not bool(result.get("ok", false)):
		_show_feedback(str(result.get("message", "That move is not possible.")), false)
		return
	var merged := str(result.get("kind", "")) == "merge"
	_show_feedback(str(result.get("message", "Clue moved.")), true)
	var target := _tile_for(to_zone, to_index)
	if target != null:
		_animate_tile(target, merged)

func _refresh_board() -> void:
	for i in range(board_tiles.size()):
		board_tiles[i].render_item(GameState.get_slot("board", i))
	for i in range(inventory_tiles.size()):
		inventory_tiles[i].render_item(GameState.get_slot("inventory", i))
	if is_instance_valid(item_count_label):
		item_count_label.text = "%d/63" % GameState.board_item_count()
	if is_instance_valid(inventory_count_label):
		inventory_count_label.text = "%d/8" % GameState.inventory_item_count()
	_refresh_selection()

func _refresh_economy() -> void:
	if is_instance_valid(energy_label):
		energy_label.text = "⚡ %d" % GameState.energy
	if is_instance_valid(coin_label):
		coin_label.text = "◉ %s" % _commas(GameState.coins)
	if is_instance_valid(gem_label):
		gem_label.text = "◆ %d" % GameState.gems

func _refresh_selection() -> void:
	for i in range(board_tiles.size()):
		board_tiles[i].set_selected(not selected_source.is_empty() and str(selected_source.get("from_zone", "")) == "board" and int(selected_source.get("from_index", -1)) == i)
	for i in range(inventory_tiles.size()):
		inventory_tiles[i].set_selected(not selected_source.is_empty() and str(selected_source.get("from_zone", "")) == "inventory" and int(selected_source.get("from_index", -1)) == i)

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
	tile.scale = Vector2(0.84, 0.84) if merged else Vector2(0.95, 0.95)
	var tween := create_tween()
	tween.tween_property(tile, "scale", Vector2(1.08, 1.08), 0.12).set_trans(Tween.TRANS_BACK).set_ease(Tween.EASE_OUT)
	tween.tween_property(tile, "scale", Vector2.ONE, 0.12)

func _show_feedback(message: String, success: bool) -> void:
	feedback_label.text = message
	feedback_label.add_theme_color_override("font_color", UI.GOLD if success else Color("f08b80"))

func _refresh_task() -> void:
	if is_instance_valid(task_label):
		task_label.text = GameState.get_task_progress_text()

func _on_task_completed(task_id: String, story_text: String) -> void:
	_refresh_task()
	var dialog := AcceptDialog.new()
	dialog.title = "EVIDENCE DISCOVERED"
	dialog.dialog_text = "%s\n\n%s" % [task_id.replace("_", " ").capitalize(), story_text]
	add_child(dialog)
	dialog.popup_centered()

func _commas(value: int) -> String:
	var s := str(value)
	var out := ""
	while s.length() > 3:
		out = "," + s.substr(s.length() - 3) + out
		s = s.substr(0, s.length() - 3)
	return s + out
