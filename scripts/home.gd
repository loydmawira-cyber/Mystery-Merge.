extends Control

const UI = preload("res://scripts/ui_theme.gd")

func _ready() -> void:
	var bg := ColorRect.new()
	bg.color = UI.NAVY
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var root := VBoxContainer.new()
	root.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	root.add_theme_constant_override("separation", 18)
	root.add_theme_constant_override("margin_left", 24)
	root.add_theme_constant_override("margin_right", 24)
	root.add_theme_constant_override("margin_top", 40)
	root.add_theme_constant_override("margin_bottom", 30)
	add_child(root)
	var resources := Label.new()
	resources.text = "⚡ %d/%d     ◉ %d     ◆ %d" % [GameState.energy, GameState.max_energy, GameState.coins, GameState.gems]
	resources.add_theme_color_override("font_color", UI.CREAM)
	resources.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	root.add_child(resources)
	var spacer := Control.new()
	spacer.size_flags_vertical = Control.SIZE_EXPAND_FILL
	root.add_child(spacer)
	var logo := Label.new()
	logo.text = "MYSTERY MERGE"
	logo.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	logo.add_theme_font_size_override("font_size", 34)
	logo.add_theme_color_override("font_color", UI.GOLD)
	root.add_child(logo)
	var subtitle := Label.new()
	subtitle.text = "SMALL CLUES. BIGGER TRUTHS."
	subtitle.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	subtitle.add_theme_color_override("font_color", UI.MUTED)
	root.add_child(subtitle)
	var card := PanelContainer.new()
	card.add_theme_stylebox_override("panel", UI.panel(Color("1a3542"), UI.GOLD_DARK, 22))
	root.add_child(card)
	var info := VBoxContainer.new()
	info.add_theme_constant_override("separation", 14)
	card.add_child(info)
	var title := Label.new()
	title.text = "CASE 01\nTHE MISSING LOCKET"
	title.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	title.add_theme_font_size_override("font_size", 25)
	title.add_theme_color_override("font_color", UI.CREAM)
	info.add_child(title)
	var desc := Label.new()
	desc.text = "A family heirloom has vanished from an old mansion. Follow the evidence. Discover the truth."
	desc.autowrap_mode = TextServer.AUTOWRAP_WORD_SMART
	desc.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	desc.add_theme_color_override("font_color", UI.PAPER)
	info.add_child(desc)
	var board := _button("CONTINUE CASE  ›", UI.GOLD, UI.INK)
	board.pressed.connect(func(): NavigationManager.show_screen("board"))
	info.add_child(board)
	var spacer2 := Control.new()
	spacer2.size_flags_vertical = Control.SIZE_EXPAND_FILL
	root.add_child(spacer2)
	for entry in [["▦  MERGE BOARD","board"],["▣  CASE FILES","cases"],["♜  SHOP","shop"],["⚙  SETTINGS","settings"]]:
		var b := _button(entry[0], UI.NAVY_LIGHT, UI.CREAM)
		b.pressed.connect(func(): NavigationManager.show_screen(entry[1]))
		root.add_child(b)
	var daily := _button("DAILY REWARD  +100 COINS / +10 ENERGY", UI.TEAL, UI.CREAM)
	daily.pressed.connect(_daily_reward)
	root.add_child(daily)

func _button(label: String, background: Color, foreground: Color) -> Button:
	var button := Button.new()
	button.text = label
	button.custom_minimum_size.y = 58
	button.add_theme_font_size_override("font_size", 17)
	button.add_theme_color_override("font_color", foreground)
	button.add_theme_stylebox_override("normal", UI.button_style(background))
	button.add_theme_stylebox_override("hover", UI.button_style(background.lightened(0.08)))
	return button

func _daily_reward() -> void:
	GameState.energy = mini(GameState.max_energy, GameState.energy + 10)
	GameState.add_coins(100)
	var dialog := AcceptDialog.new()
	dialog.title = "Daily Detective Gift"
	dialog.dialog_text = "You received 10 energy and 100 coins."
	add_child(dialog)
	dialog.popup_centered()
