extends Control
const UI = preload("res://scripts/ui_theme.gd")
func _ready() -> void:
	var bg := TextureRect.new()
	bg.texture = load("res://assets/merge_evidence_bg.svg")
	bg.expand_mode = TextureRect.EXPAND_IGNORE_SIZE
	bg.stretch_mode = TextureRect.STRETCH_KEEP_ASPECT_COVERED
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var shade := ColorRect.new()
	shade.color = Color(0.015, 0.035, 0.05, 0.42)
	shade.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(shade)
	var scroll := ScrollContainer.new()
	scroll.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	scroll.horizontal_scroll_mode = ScrollContainer.SCROLL_MODE_DISABLED
	add_child(scroll)
	var margin := MarginContainer.new()
	margin.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	margin.add_theme_constant_override("margin_left", 20)
	margin.add_theme_constant_override("margin_right", 20)
	margin.add_theme_constant_override("margin_top", 24)
	margin.add_theme_constant_override("margin_bottom", 20)
	scroll.add_child(margin)
	var box := VBoxContainer.new()
	box.add_theme_constant_override("separation", 12)
	margin.add_child(box)
	var title := Label.new()
	title.text = "CASE FILE · THE MISSING LOCKET"
	title.add_theme_font_size_override("font_size", 23)
	title.add_theme_color_override("font_color", UI.GOLD)
	box.add_child(title)
	var intro := Label.new()
	intro.text = "A family heirloom vanished after dinner at the Hale house. Search three areas and reconstruct the evidence."
	intro.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	intro.add_theme_color_override("font_color", UI.CREAM)
	box.add_child(intro)
	var locations := [
		["THE STUDY","Desk drawers, family photographs and the old brass key.",true],
		["THE GARDEN ROOM","Footprints, flowers and a torn blue ribbon.",GameState.current_task_index >= 2],
		["THE LOCKED ATTIC","Sealed keepsakes and the missing locket.",GameState.current_task_index >= 4]
	]
	for entry in locations:
		var panel := PanelContainer.new()
		panel.add_theme_stylebox_override("panel", UI.panel(Color("e2cfaa"), UI.GOLD_DARK, 12))
		box.add_child(panel)
		var col := VBoxContainer.new()
		panel.add_child(col)
		var h := Label.new()
		h.text = str(entry[0]) + ("  ·  UNLOCKED" if entry[2] else "  ·  LOCKED")
		h.add_theme_color_override("font_color", Color("765126") if entry[2] else Color("8b806c"))
		col.add_child(h)
		var d := Label.new()
		d.text = str(entry[1])
		d.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
		d.add_theme_color_override("font_color", Color("493a2a"))
		col.add_child(d)
	var progress := Label.new()
	progress.text = GameState.get_task_progress_text()
	progress.add_theme_color_override("font_color", UI.GOLD)
	box.add_child(progress)
	for task in GameState.task_catalog:
		var completed := GameState.completed_tasks.has(str(task.get("id", "")))
		var row := Label.new()
		row.text = ("✓  " if completed else "○  ") + str(task.get("title", "Evidence task"))
		row.add_theme_color_override("font_color", Color("f1dfb7") if completed else Color("c8b58f"))
		box.add_child(row)
	var spacer := Control.new()
	spacer.size_flags_vertical = Control.SIZE_EXPAND_FILL
	box.add_child(spacer)
	var back := Button.new()
	back.text = "RETURN TO INVESTIGATION"
	back.custom_minimum_size.y = 50
	back.add_theme_color_override("font_color", UI.INK)
	back.add_theme_stylebox_override("normal", UI.button_style(UI.GOLD))
	back.pressed.connect(func(): NavigationManager.show_screen("board"))
	box.add_child(back)
