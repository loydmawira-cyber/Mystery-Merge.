extends Control
const UI = preload("res://scripts/ui_theme.gd")
func _ready() -> void:
	var bg := ColorRect.new()
	bg.color = UI.NAVY
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var margin := MarginContainer.new()
	margin.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	margin.add_theme_constant_override("margin_left", 20)
	margin.add_theme_constant_override("margin_right", 20)
	margin.add_theme_constant_override("margin_top", 24)
	margin.add_theme_constant_override("margin_bottom", 20)
	add_child(margin)
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
		panel.add_theme_stylebox_override("panel", UI.panel(Color("193542"), UI.GOLD_DARK, 12))
		box.add_child(panel)
		var col := VBoxContainer.new()
		panel.add_child(col)
		var h := Label.new()
		h.text = str(entry[0]) + ("  ·  UNLOCKED" if entry[2] else "  ·  LOCKED")
		h.add_theme_color_override("font_color", UI.GOLD if entry[2] else UI.MUTED)
		col.add_child(h)
		var d := Label.new()
		d.text = str(entry[1])
		d.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
		d.add_theme_color_override("font_color", UI.CREAM)
		col.add_child(d)
	var progress := Label.new()
	progress.text = GameState.get_task_progress_text()
	progress.add_theme_color_override("font_color", UI.GOLD)
	box.add_child(progress)
	for task in GameState.task_catalog:
		var completed := GameState.completed_tasks.has(str(task.get("id", "")))
		var row := Label.new()
		row.text = ("✓  " if completed else "○  ") + str(task.get("title", "Evidence task"))
		row.add_theme_color_override("font_color", UI.CREAM if completed else UI.MUTED)
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
