extends Button
const UI = preload("res://scripts/ui_theme.gd")
var board_controller: Control
var zone: String = "board"
var slot_index: int = 0
var item_id: String = ""
var is_selected: bool = false
func configure(controller: Control, slot_zone: String, index: int) -> void:
	board_controller = controller
	zone = slot_zone
	slot_index = index
	custom_minimum_size = Vector2(82, 62) if zone == "board" else Vector2(70, 54)
	size_flags_horizontal = Control.SIZE_EXPAND_FILL
	size_flags_vertical = Control.SIZE_EXPAND_FILL
	focus_mode = Control.FOCUS_NONE
	add_theme_font_size_override("font_size", 15 if zone == "board" else 12)
	add_theme_color_override("font_color", UI.CREAM)
	add_theme_color_override("font_hover_color", UI.CREAM)
	pressed.connect(_on_pressed)
	render_item("")
func render_item(next_item_id: String) -> void:
	item_id = next_item_id
	is_selected = false
	self_modulate = Color.WHITE
	if item_id.is_empty():
		text = "＋" if zone == "inventory" else ""
		tooltip_text = "Empty space"
		_apply_style(Color("172b38"), Color("36505c"))
		return
	var item_data := GameState.get_item(item_id)
	var symbol := str(item_data.get("symbol", "◈"))
	var short_name := str(item_data.get("short_name", item_data.get("display_name", "Clue")))
	var level := int(item_data.get("level", 1))
	text = "%s\n%s · %d" % [symbol, short_name, level]
	tooltip_text = "%s\n%s" % [str(item_data.get("display_name", item_id)), str(item_data.get("description", "A clue from the mansion."))]
	var tile_color := Color(str(item_data.get("tile_color", "#31515b")))
	_apply_style(tile_color, Color("d9b66a"))
func set_selected(selected: bool) -> void:
	is_selected = selected
	if item_id.is_empty():
		return
	var item_data := GameState.get_item(item_id)
	var tile_color := Color(str(item_data.get("tile_color", "#31515b")))
	_apply_style(tile_color.lightened(0.18) if selected else tile_color, UI.GOLD if selected else Color("d9b66a"))
	if selected:
		self_modulate = Color(1.12, 1.08, 0.86, 1.0)
	else:
		self_modulate = Color.WHITE
func _apply_style(bg_color: Color, border_color: Color) -> void:
	var style := StyleBoxFlat.new()
	style.bg_color = bg_color
	style.border_color = border_color
	style.set_border_width_all(2 if is_selected else 1)
	style.set_corner_radius_all(9)
	style.content_margin_left = 2
	style.content_margin_right = 2
	style.content_margin_top = 3
	style.content_margin_bottom = 3
	add_theme_stylebox_override("normal", style)
	var hover := style.duplicate() as StyleBoxFlat
	hover.bg_color = bg_color.lightened(0.10)
	add_theme_stylebox_override("hover", hover)
	var pressed_style := style.duplicate() as StyleBoxFlat
	pressed_style.bg_color = bg_color.darkened(0.10)
	add_theme_stylebox_override("pressed", pressed_style)
	add_theme_stylebox_override("focus", StyleBoxEmpty.new())
func _on_pressed() -> void:
	if is_instance_valid(board_controller):
		board_controller.on_tile_tapped(zone, slot_index)
func _get_drag_data(_at_position: Vector2) -> Variant:
	if item_id.is_empty():
		return null
	var preview := PanelContainer.new()
	var preview_style := StyleBoxFlat.new()
	var item_data := GameState.get_item(item_id)
	preview_style.bg_color = Color(str(item_data.get("tile_color", "#31515b")))
	preview_style.border_color = UI.GOLD
	preview_style.set_border_width_all(2)
	preview_style.set_corner_radius_all(10)
	preview.add_theme_stylebox_override("panel", preview_style)
	var preview_label := Label.new()
	preview_label.text = str(item_data.get("display_name", item_id))
	preview_label.add_theme_color_override("font_color", UI.CREAM)
	preview_label.add_theme_font_size_override("font_size", 14)
	preview.add_child(preview_label)
	set_drag_preview(preview)
	return {"kind": "evidence_item", "from_zone": zone, "from_index": slot_index}
func _can_drop_data(_at_position: Vector2, data: Variant) -> bool:
	return data is Dictionary and str(data.get("kind", "")) == "evidence_item"
func _drop_data(_at_position: Vector2, data: Variant) -> void:
	if is_instance_valid(board_controller):
		board_controller.perform_drop(str(data.get("from_zone", "")), int(data.get("from_index", -1)), zone, slot_index)
