extends Control
const UI = preload("res://scripts/ui_theme.gd")
var feedback: Label
func _ready() -> void:
	var bg := ColorRect.new()
	bg.color = UI.NAVY
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var margin := MarginContainer.new()
	margin.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	margin.add_theme_constant_override("margin_left", 22)
	margin.add_theme_constant_override("margin_right", 22)
	margin.add_theme_constant_override("margin_top", 28)
	margin.add_theme_constant_override("margin_bottom", 24)
	add_child(margin)
	var box := VBoxContainer.new()
	box.add_theme_constant_override("separation", 14)
	margin.add_child(box)
	var title := Label.new()
	title.text = "FIELD SUPPLIES"
	title.add_theme_font_size_override("font_size", 28)
	title.add_theme_color_override("font_color", UI.GOLD)
	box.add_child(title)
	var wallet := Label.new()
	wallet.text = "Coins: %s     Energy: %d/%d     Gems: %d" % [_commas(GameState.coins), GameState.energy, GameState.max_energy, GameState.gems]
	wallet.add_theme_color_override("font_color", UI.CREAM)
	box.add_child(wallet)
	var card := _card(box, "ENERGY PACK", "25 energy · 500 coins", "Useful for searching the mansion generators.")
	var buy := _button("BUY FOR 500 COINS", UI.GOLD, UI.INK)
	buy.pressed.connect(func():
		var result := GameState.purchase_energy()
		feedback.text = str(result.message)
		wallet.text = "Coins: %s     Energy: %d/%d     Gems: %d" % [_commas(GameState.coins), GameState.energy, GameState.max_energy, GameState.gems])
	)
	card.add_child(buy)
	var ad_card := _card(box, "WATCH & EARN", "Rewarded video test offer · +10 energy", "Ad network integration is not connected in this build.")
	var ad := _button("CLAIM TEST REWARD", UI.TEAL, UI.CREAM)
	ad.pressed.connect(func():
		GameState.add_energy(10)
		feedback.text = "Test reward granted: 10 energy. This is a development simulation, not a real ad."
		wallet.text = "Coins: %s     Energy: %d/%d     Gems: %d" % [_commas(GameState.coins), GameState.energy, GameState.max_energy, GameState.gems]
	)
	ad_card.add_child(ad)
	feedback = Label.new()
	feedback.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	feedback.add_theme_color_override("font_color", UI.GOLD)
	box.add_child(feedback)
	var spacer := Control.new()
	spacer.size_flags_vertical = Control.SIZE_EXPAND_FILL
	box.add_child(spacer)
	var back := _button("BACK TO CASE", UI.NAVY_LIGHT, UI.CREAM)
	back.pressed.connect(func(): NavigationManager.show_screen("board"))
	box.add_child(back)
func _card(parent: VBoxContainer, heading: String, detail: String, description: String) -> VBoxContainer:
	var panel := PanelContainer.new()
	panel.add_theme_stylebox_override("panel", UI.panel(Color("1a3542"), UI.GOLD_DARK, 16))
	parent.add_child(panel)
	var col := VBoxContainer.new()
	col.add_theme_constant_override("separation", 6)
	panel.add_child(col)
	var h := Label.new()
	h.text = heading
	h.add_theme_color_override("font_color", UI.GOLD)
	h.add_theme_font_size_override("font_size", 17)
	col.add_child(h)
	var d := Label.new()
	d.text = detail
	d.add_theme_color_override("font_color", UI.CREAM)
	col.add_child(d)
	var desc := Label.new()
	desc.text = description
	desc.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	desc.add_theme_color_override("font_color", UI.MUTED)
	col.add_child(desc)
	return col
func _button(caption: String, color: Color, fg: Color) -> Button:
	var b := Button.new()
	b.text = caption
	b.custom_minimum_size.y = 48
	b.add_theme_color_override("font_color", fg)
	b.add_theme_stylebox_override("normal", UI.button_style(color))
	return b
func _commas(value: int) -> String:
	var digits := str(value)
	var out := ""
	while digits.length() > 3:
		out = "," + digits.substr(digits.length() - 3) + out
		digits = digits.substr(0, digits.length() - 3)
	return digits + out
