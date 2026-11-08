extends Control

const UI = preload("res://scripts/ui_theme.gd")

func _ready() -> void:
	_build()

func _build() -> void:
	var bg := ColorRect.new()
	bg.color = Color("091923")
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var decor := ColorRect.new()
	decor.color = Color(0.08, 0.23, 0.27, 0.25)
	decor.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(decor)
	var margin := MarginContainer.new()
	margin.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	margin.add_theme_constant_override("margin_left", 22)
	margin.add_theme_constant_override("margin_right", 22)
	margin.add_theme_constant_override("margin_top", 20)
	margin.add_theme_constant_override("margin_bottom", 18)
	add_child(margin)
	var root := VBoxContainer.new()
	root.add_theme_constant_override("separation", 12)
	margin.add_child(root)

	var top := HBoxContainer.new()
	root.add_child(top)
	var logo := Label.new()
	logo.text = "✦ MYSTERY\n   MERGE"
	logo.add_theme_font_size_override("font_size", 28)
	logo.add_theme_color_override("font_color", UI.GOLD)
	top.add_child(logo)
	var spacer := Control.new()
	spacer.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	top.add_child(spacer)
	var gear := _button("⚙", UI.NAVY_LIGHT, UI.CREAM)
	gear.custom_minimum_size = Vector2(48, 48)
	gear.pressed.connect(func(): NavigationManager.show_screen("settings"))
	top.add_child(gear)

	var motto := Label.new()
	motto.text = "SMALL CLUES. BIGGER TRUTHS."
	motto.horizontal_alignment = HORIZONTAL_ALIGNMENT_RIGHT
	motto.add_theme_color_override("font_color", UI.MUTED)
	motto.add_theme_font_size_override("font_size", 11)
	root.add_child(motto)

	var wallet := PanelContainer.new()
	wallet.add_theme_stylebox_override("panel", UI.panel(Color("102c38"), UI.GOLD_DARK, 14))
	root.add_child(wallet)
	var wallet_row := HBoxContainer.new()
	wallet.add_child(wallet_row)
	wallet_row.add_child(_stat("⚡", str(GameState.energy) + "/" + str(GameState.max_energy)))
	wallet_row.add_child(_stat("◉", _commas(GameState.coins)))
	wallet_row.add_child(_stat("◆", str(GameState.gems)))

	var spacer_top := Control.new()
	spacer_top.size_flags_vertical = Control.SIZE_EXPAND_FILL
	root.add_child(spacer_top)
	var case_panel := PanelContainer.new()
	case_panel.size_flags_vertical = Control.SIZE_EXPAND_FILL
	case_panel.add_theme_stylebox_override("panel", UI.panel(Color("173744"), UI.GOLD, 22))
	root.add_child(case_panel)
	var case_content := VBoxContainer.new()
	case_content.alignment = BoxContainer.ALIGNMENT_CENTER
	case_content.add_theme_constant_override("separation", 12)
	case_panel.add_child(case_content)
	var icon := Label.new()
	icon.text = "⌂"
	icon.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	icon.add_theme_font_size_override("font_size", 92)
	icon.add_theme_color_override("font_color", UI.GOLD)
	case_content.add_child(icon)
	var eyebrow := Label.new()
	eyebrow.text = "YOUR FIRST INVESTIGATION"
	eyebrow.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	eyebrow.add_theme_font_size_override("font_size", 10)
	eyebrow.add_theme_color_override("font_color", UI.MUTED)
	case_content.add_child(eyebrow)
	var heading := Label.new()
	heading.text = "CASE 01\nTHE MISSING LOCKET"
	heading.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	heading.add_theme_font_size_override("font_size", 29)
	heading.add_theme_color_override("font_color", UI.CREAM)
	case_content.add_child(heading)
	var desc := Label.new()
	desc.text = "A family heirloom has vanished from an old mansion. Search the rooms, restore the evidence and discover who hid the truth."
	desc.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	desc.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	desc.add_theme_color_override("font_color", UI.PAPER)
	case_content.add_child(desc)
	var progress := Label.new()
	progress.text = GameState.get_task_progress_text()
	progress.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	progress.add_theme_color_override("font_color", UI.GOLD)
	case_content.add_child(progress)
	var play := _button("CONTINUE INVESTIGATION   ›", UI.GOLD, UI.INK)
	play.custom_minimum_size.y = 62
	play.pressed.connect(func(): NavigationManager.show_screen("board"))
	case_content.add_child(play)
	var spacer_bottom := Control.new()
	spacer_bottom.size_flags_vertical = Control.SIZE_EXPAND_FILL
	root.add_child(spacer_bottom)

	var nav := HBoxContainer.new()
	nav.add_theme_constant_override("separation", 7)
	root.add_child(nav)
	_add_nav(nav, "▦\nMERGE", "board")
	_add_nav(nav, "▤\nCASES", "cases")
	_add_nav(nav, "♜\nSHOP", "shop")
	var daily := _button("✦  DAILY DETECTIVE GIFT  ·  +100 COINS / +10 ENERGY", UI.TEAL, UI.CREAM)
	daily.custom_minimum_size.y = 48
	daily.add_theme_font_size_override("font_size", 11)
	daily.pressed.connect(_daily_reward)
	root.add_child(daily)

func _stat(symbol: String, value: String) -> Label:
	var l := Label.new()
	l.text = symbol + "  " + value
	l.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	l.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	l.add_theme_color_override("font_color", UI.CREAM)
	l.add_theme_font_size_override("font_size", 12)
	return l

func _button(text_value: String, bg: Color, fg: Color) -> Button:
	var b := Button.new()
	b.text = text_value
	b.add_theme_font_size_override("font_size", 16)
	b.add_theme_color_override("font_color", fg)
	b.add_theme_color_override("font_hover_color", fg)
	b.add_theme_stylebox_override("normal", UI.button_style(bg))
	b.add_theme_stylebox_override("hover", UI.button_style(bg.lightened(0.08), UI.GOLD))
	return b

func _add_nav(parent: HBoxContainer, caption: String, screen: String) -> void:
	var b := _button(caption, UI.NAVY_LIGHT, UI.CREAM)
	b.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	b.custom_minimum_size.y = 58
	b.add_theme_font_size_override("font_size", 13)
	b.pressed.connect(func(): NavigationManager.show_screen(screen))
	parent.add_child(b)

func _daily_reward() -> void:
	var result := GameState.claim_daily_reward()
	var dialog := AcceptDialog.new()
	dialog.title = "DAILY DETECTIVE GIFT"
	dialog.dialog_text = str(result.get("message", "Today's gift is unavailable."))
	add_child(dialog)
	dialog.popup_centered()

func _commas(value: int) -> String:
	var s := str(value)
	var out := ""
	while s.length() > 3:
		out = "," + s.substr(s.length() - 3) + out
		s = s.substr(0, s.length() - 3)
	return s + out
