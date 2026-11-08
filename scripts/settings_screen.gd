extends Control
const UI = preload("res://scripts/ui_theme.gd")
var sound_button: Button
var music_button: Button
var notification_button: Button
func _ready() -> void:
	var bg := ColorRect.new()
	bg.color = UI.NAVY
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var margin := MarginContainer.new()
	margin.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	margin.add_theme_constant_override("margin_left", 24)
	margin.add_theme_constant_override("margin_right", 24)
	margin.add_theme_constant_override("margin_top", 30)
	margin.add_theme_constant_override("margin_bottom", 24)
	add_child(margin)
	var box := VBoxContainer.new()
	box.add_theme_constant_override("separation", 14)
	margin.add_child(box)
	var title := Label.new()
	title.text = "SETTINGS"
	title.add_theme_font_size_override("font_size", 28)
	title.add_theme_color_override("font_color", UI.GOLD)
	box.add_child(title)
	sound_button = _toggle_button(box, "Sound effects", GameState.sound_enabled, _toggle_sound)
	music_button = _toggle_button(box, "Music", GameState.music_enabled, _toggle_music)
	notification_button = _toggle_button(box, "Notifications", GameState.notifications_enabled, _toggle_notifications)
	var note := Label.new()
	note.text = "Settings are saved automatically on this device."
	note.add_theme_color_override("font_color", UI.MUTED)
	box.add_child(note)
	var spacer := Control.new()
	spacer.size_flags_vertical = Control.SIZE_EXPAND_FILL
	box.add_child(spacer)
	var reset := Button.new()
	reset.text = "RESET LOCAL GAME DATA"
	reset.custom_minimum_size.y = 48
	reset.add_theme_color_override("font_color", Color("ffd9d3"))
	reset.add_theme_stylebox_override("normal", UI.button_style(Color("653d46")))
	reset.pressed.connect(_confirm_reset)
	box.add_child(reset)
	var back := Button.new()
	back.text = "BACK TO HOME"
	back.custom_minimum_size.y = 48
	back.add_theme_color_override("font_color", UI.CREAM)
	back.add_theme_stylebox_override("normal", UI.button_style(UI.NAVY_LIGHT))
	back.pressed.connect(func(): NavigationManager.show_screen("home"))
	box.add_child(back)
func _toggle_button(parent: VBoxContainer, caption: String, enabled: bool, callback: Callable) -> Button:
	var b := Button.new()
	b.custom_minimum_size.y = 52
	b.add_theme_color_override("font_color", UI.CREAM)
	b.add_theme_stylebox_override("normal", UI.button_style(Color("1c3c49")))
	b.pressed.connect(callback)
	parent.add_child(b)
	b.set_meta("caption", caption)
	b.text = "%s: %s" % [caption, "ON" if enabled else "OFF"]
	return b
func _toggle_sound() -> void:
	GameState.sound_enabled = not GameState.sound_enabled
	_update_toggle(sound_button, "Sound effects", GameState.sound_enabled)
	GameState._save_game()
func _toggle_music() -> void:
	GameState.music_enabled = not GameState.music_enabled
	_update_toggle(music_button, "Music", GameState.music_enabled)
	GameState._save_game()
func _toggle_notifications() -> void:
	GameState.notifications_enabled = not GameState.notifications_enabled
	_update_toggle(notification_button, "Notifications", GameState.notifications_enabled)
	GameState._save_game()
func _update_toggle(button: Button, caption: String, enabled: bool) -> void:
	button.text = "%s: %s" % [caption, "ON" if enabled else "OFF"]
func _confirm_reset() -> void:
	var dialog := ConfirmationDialog.new()
	dialog.title = "Reset Mystery Merge?"
	dialog.dialog_text = "This permanently clears your local board, currencies and case progress on this device."
	add_child(dialog)
	dialog.confirmed.connect(func():
		GameState.reset_local_save()
		NavigationManager.show_screen("home")
	)
	dialog.popup_centered()
