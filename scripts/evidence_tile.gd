extends Button

const UI = preload("res://scripts/ui_theme.gd")
var board_controller: Control
var zone := "board"
var slot_index := 0
var item_id := ""
var is_selected := false

func configure(controller: Control, slot_zone: String, index: int) -> void:
	board_controller = controller
	zone = slot_zone
	slot_index = index
	custom_minimum_size = Vector2(0, 54 if zone == "board" else 42)
	size_flags_horizontal = Control.SIZE_EXPAND_FILL
	size_flags_vertical = Control.SIZE_EXPAND_FILL
	focus_mode = Control.FOCUS_NONE
	add_theme_font_size_override("font_size", 10 if zone == "board" else 9)
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
		tooltip_text = "Empty evidence slot"
		_apply_style(Color("102b36"), Color("31505a"))
		return
	var data := GameState.get_item(item_id)
	var symbol := str(data.get("symbol", "◈"))
	var short_name := str(data.get("short_name", "Clue"))
	var level := int(data.get("level", 1))
	text = "%s\n%s" % [symbol, short_name]
	tooltip_text = "%s · Level %d\n%s" % [str(data.get("display_name", item_id)), level, str(data.get("description", "A clue from the mansion."))]
	_apply_style(Color(str(data.get("tile_color", "#31515b"))), Color("b58b4c"))

func set_selected(selected: bool) -> void:
	is_selected = selected
	if item_id.is_empty():
		return
	var data := GameState.get_item(item_id)
	var tile_color := Color(str(data.get("tile_color", "#31515b")))
	_apply_style(tile_color.lightened(0.16) if selected else tile_color, UI.GOLD if selected else Color("b58b4c"))
	self_modulate = Color(1.12, 1.08, 0.86, 1.0) if selected else Color.WHITE

func _apply_style(bg: Color, border: Color) -> void:
	var style := StyleBoxFlat.new()
	style.bg_color = bg
	style.border_color = border
	style.set_border_width_all(2 if is_selected else 1)
	style.set_corner_radius_all(7)
	style.content_margin_left = 1
	style.content_margin_right = 1
	style.content_margin_top = 1
	style.content_margin_bottom = 1
	add_theme_stylebox_override("normal", style)
	var hover := style.duplicate() as StyleBoxFlat
	hover.bg_color = bg.lightened(0.12)
	add_theme_stylebox_override("hover", hover)
	var down := style.duplicate() as StyleBoxFlat
	down.bg_color = bg.darkened(0.12)
	add_theme_stylebox_override("pressed", down)
	add_theme_stylebox_override("focus", StyleBoxEmpty.new())

func _on_pressed() -> void:
	if is_instance_valid(board_controller):
		board_controller.on_tile_tapped(zone, slot_index)

func _get_drag_data(_at_position: Vector2) -> Variant:
	if item_id.is_empty():
		return null
	var preview := PanelContainer.new()
	var ps := StyleBoxFlat.new()
	ps.bg_color = Color(str(GameState.get_item(item_id).get("tile_color", "#31515b")))
	ps.border_color = UI.GOLD
	ps.set_border_width_all(2)
	ps.set_corner_radius_all(10)
	preview.add_theme_stylebox_override("panel", ps)
	var label := Label.new()
	label.text = str(GameState.get_item(item_id).get("display_name", item_id))
	label.add_theme_color_override("font_color", UI.CREAM)
	preview.add_child(label)
	set_drag_preview(preview)
	return {"kind": "evidence_item", "from_zone": zone, "from_index": slot_index}

func _can_drop_data(_at_position: Vector2, data: Variant) -> bool:
	return data is Dictionary and str(data.get("kind", "")) == "evidence_item"

func _drop_data(_at_position: Vector2, data: Variant) -> void:
	if is_instance_valid(board_controller):
		board_controller.perform_drop(str(data.get("from_zone", "")), int(data.get("from_index", -1)), zone, slot_index)
