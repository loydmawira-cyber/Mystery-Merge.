extends Control

const UI = preload("res://scripts/ui_theme.gd")
@export var screen_title: String = "Mystery Merge"
@export_multiline var screen_description: String = "This screen will be implemented in a later development stage."

func _ready() -> void:
	var bg := ColorRect.new()
	bg.color = UI.NAVY
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var center := CenterContainer.new()
	center.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(center)
	var col := VBoxContainer.new()
	col.custom_minimum_size.x = 320
	col.add_theme_constant_override("separation", 18)
	center.add_child(col)
	var title := Label.new()
	title.text = screen_title
	title.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	title.add_theme_font_size_override("font_size", 30)
	title.add_theme_color_override("font_color", UI.GOLD)
	col.add_child(title)
	var desc := Label.new()
	desc.text = screen_description
	desc.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	desc.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	desc.add_theme_font_size_override("font_size", 17)
	desc.add_theme_color_override("font_color", UI.CREAM)
	col.add_child(desc)
	var back := Button.new()
	back.text = "‹  BACK TO HOME"
	back.custom_minimum_size.y = 58
	back.add_theme_font_size_override("font_size", 18)
	back.add_theme_color_override("font_color", UI.INK)
	back.add_theme_stylebox_override("normal", UI.button_style(UI.GOLD))
	back.pressed.connect(func(): NavigationManager.show_screen("home"))
	col.add_child(back)
