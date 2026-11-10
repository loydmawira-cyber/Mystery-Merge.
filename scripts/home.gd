extends Control

const UI = preload("res://scripts/ui_theme.gd")

func _ready() -> void:
	_build()

func _build() -> void:
	var backdrop := TextureRect.new()
	backdrop.texture = load("res://assets/home_mansion.svg")
	backdrop.expand_mode = TextureRect.EXPAND_IGNORE_SIZE
	backdrop.stretch_mode = TextureRect.STRETCH_KEEP_ASPECT_COVERED
	backdrop.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(backdrop)
	var shade := ColorRect.new()
	shade.color = Color(0.015, 0.045, 0.075, 0.48)
	shade.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(shade)

	var scroll := ScrollContainer.new()
	scroll.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	scroll.horizontal_scroll_mode = ScrollContainer.SCROLL_MODE_DISABLED
	add_child(scroll)
	var margin := MarginContainer.new()
	margin.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	margin.add_theme_constant_override("margin_left", 22)
	margin.add_theme_constant_override("margin_right", 22)
	margin.add_theme_constant_override("margin_top", 20)
	margin.add_theme_constant_override("margin_bottom", 24)
	scroll.add_child(margin)
	var root := VBoxContainer.new()
	root.add_theme_constant_override("separation", 13)
	margin.add_child(root)

	var header := HBoxContainer.new()
	root.add_child(header)
	var logo := Label.new()
	logo.text = "◉ MYSTERY\n    MERGE"
	logo.add_theme_font_size_override("font_size", 31)
	logo.add_theme_color_override("font_color", UI.GOLD)
	header.add_child(logo)
	var spacer := Control.new()
	spacer.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	header.add_child(spacer)
	var settings := _button("⚙", Color("142d3a"), UI.CREAM)
	settings.custom_minimum_size = Vector2(48, 48)
	settings.pressed.connect(func(): NavigationManager.show_screen("settings"))
	header.add_child(settings)

	var motto := _label("SMALL CLUES. BIGGER TRUTHS.", 11, Color("d5c49f"))
	motto.horizontal_alignment = HORIZONTAL_ALIGNMENT_RIGHT
	root.add_child(motto)

	var wallet := PanelContainer.new()
	wallet.add_theme_stylebox_override("panel", UI.panel(Color(0.025, 0.12, 0.17, 0.94), UI.GOLD_DARK, 13))
	root.add_child(wallet)
	var wallet_row := HBoxContainer.new()
	wallet_row.add_theme_constant_override("separation", 4)
	wallet.add_child(wallet_row)
	wallet_row.add_child(_stat("⚡", "%d/%d" % [GameState.energy, GameState.max_energy]))
	wallet_row.add_child(_stat("◉", _commas(GameState.coins)))
	wallet_row.add_child(_stat("◆", str(GameState.gems)))

	var spacer_top := Control.new()
	spacer_top.custom_minimum_size.y = 170
	root.add_child(spacer_top)

	var case_panel := PanelContainer.new()
	case_panel.add_theme_stylebox_override("panel", UI.panel(Color("e5d0a9"), Color("b98a43"), 17))
	root.add_child(case_panel)
	var case_content := VBoxContainer.new()
	case_content.add_theme_constant_override("separation", 10)
	case_panel.add_child(case_content)

	var eyebrow := _label("YOUR FIRST INVESTIGATION", 10, Color("86612e"))
	eyebrow.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	case_content.add_child(eyebrow)
	var heading := _label("CASE 01\nTHE MISSING LOCKET", 29, Color("241a14"))
	heading.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	heading.add_theme_font_size_override("font_size", 29)
	case_content.add_child(heading)
	var rule := ColorRect.new()
	rule.color = Color("b28a4d")
	rule.custom_minimum_size.y = 2
	case_content.add_child(rule)
	var desc := _label("A family heirloom has vanished from an old mansion. Search the rooms, restore the evidence and discover who hid the truth.", 14, Color("493a2a"))
	desc.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	desc.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	case_content.add_child(desc)
	var progress := _label(GameState.get_task_progress_text(), 13, Color("765326"))
	progress.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	case_content.add_child(progress)
	var progress_bar := ProgressBar.new()
	progress_bar.max_value = 1.0
	progress_bar.value = GameState.case_progress
	progress_bar.show_percentage = false
	progress_bar.custom_minimum_size.y = 9
	progress_bar.add_theme_stylebox_override("background", UI.button_style(Color("c6b184"), Color("b28a4d")))
	progress_bar.add_theme_stylebox_override("fill", UI.button_style(Color("17616a"), Color("17616a")))
	case_content.add_child(progress_bar)
	var task_detail := _label(GameState.get_current_task_detail(), 11, Color("5e4930"))
	task_detail.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	task_detail.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	case_content.add_child(task_detail)
	var play := _button("CONTINUE INVESTIGATION     ›", Color("07515a"), UI.CREAM)
	play.custom_minimum_size.y = 58
	play.pressed.connect(func(): NavigationManager.show_screen("board"))
	case_content.add_child(play)

	var daily := _button("✦  DAILY DETECTIVE GIFT  ·  +100 COINS / +10 ENERGY", Color("123a45"), UI.CREAM)
	daily.custom_minimum_size.y = 48
	daily.add_theme_font_size_override("font_size", 11)
	daily.pressed.connect(_daily_reward)
	root.add_child(daily)
	GameState.economy_changed.connect(func():
		wallet_row.get_child(0).text = "⚡  %d/%d" % [GameState.energy, GameState.max_energy]
		wallet_row.get_child(1).text = "◉  " + _commas(GameState.coins)
		wallet_row.get_child(2).text = "◆  " + str(GameState.gems)
	)
	GameState.progress_changed.connect(func():
		progress.text = GameState.get_task_progress_text()
		progress_bar.value = GameState.case_progress
		task_detail.text = GameState.get_current_task_detail()
	)

	var nav := HBoxContainer.new()
	nav.add_theme_constant_override("separation", 7)
	root.add_child(nav)
	_add_nav(nav, "▦\nMERGE", "board")
	_add_nav(nav, "▤\nCASES", "cases")
	_add_nav(nav, "♜\nSHOP", "shop")

func _label(value: String, size: int, color: Color) -> Label:
	var l := Label.new()
	l.text = value
	l.add_theme_font_size_override("font_size", size)
	l.add_theme_color_override("font_color", color)
	l.vertical_alignment = VERTICAL_ALIGNMENT_CENTER
	return l

func _stat(symbol: String, value: String) -> Label:
	var l := _label(symbol + "  " + value, 13, UI.CREAM)
	l.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	l.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	return l

func _button(caption: String, bg: Color, fg: Color) -> Button:
	var b := Button.new()
	b.text = caption
	b.add_theme_font_size_override("font_size", 15)
	b.add_theme_color_override("font_color", fg)
	b.add_theme_color_override("font_hover_color", fg)
	b.add_theme_stylebox_override("normal", UI.button_style(bg, UI.GOLD))
	b.add_theme_stylebox_override("hover", UI.button_style(bg.lightened(0.09), UI.GOLD))
	b.add_theme_stylebox_override("pressed", UI.button_style(bg.darkened(0.1), UI.GOLD))
	return b

func _add_nav(parent: HBoxContainer, caption: String, screen: String) -> void:
	var b := _button(caption, Color("102b38"), UI.CREAM)
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
