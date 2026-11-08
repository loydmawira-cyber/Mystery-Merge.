extends Control

const UI = preload("res://scripts/ui_theme.gd")
var feedback: Label
var wallet: Label

func _ready() -> void:
	var bg := TextureRect.new()
	bg.texture = load("res://assets/detective_shop_bg.svg")
	bg.expand_mode = TextureRect.EXPAND_IGNORE_SIZE
	bg.stretch_mode = TextureRect.STRETCH_KEEP_ASPECT_COVERED
	bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(bg)
	var shade := ColorRect.new()
	shade.color = Color(0.01, 0.035, 0.055, 0.38)
	shade.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	add_child(shade)
	var margin := MarginContainer.new()
	margin.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
	margin.add_theme_constant_override("margin_left", 18)
	margin.add_theme_constant_override("margin_right", 18)
	margin.add_theme_constant_override("margin_top", 18)
	margin.add_theme_constant_override("margin_bottom", 18)
	add_child(margin)
	var root := VBoxContainer.new()
	root.add_theme_constant_override("separation", 12)
	margin.add_child(root)
	var header := HBoxContainer.new()
	root.add_child(header)
	var title_col := VBoxContainer.new()
	title_col.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	header.add_child(title_col)
	title_col.add_child(_label("DETECTIVE SHOP", 27, UI.GOLD))
	title_col.add_child(_label("SUPPORT THE MYSTERY · KEEP THE STORY GOING", 9, UI.MUTED))
	var back := _button("‹", UI.NAVY_LIGHT, UI.CREAM)
	back.custom_minimum_size = Vector2(44, 44)
	back.pressed.connect(func(): NavigationManager.show_screen("board"))
	header.add_child(back)
	wallet = _label("", 11, UI.CREAM)
	root.add_child(wallet)
	var hero := PanelContainer.new()
	hero.add_theme_stylebox_override("panel", UI.panel(Color("e2c99a"), UI.GOLD, 20))
	root.add_child(hero)
	var hero_col := VBoxContainer.new()
	hero_col.alignment = BoxContainer.ALIGNMENT_CENTER
	hero_col.add_theme_constant_override("separation", 9)
	hero.add_child(hero_col)
	hero_col.add_child(_center_label("✦  FIRST CASE SUPPLY KIT  ✦", 13, Color("7e5525")))
	hero_col.add_child(_center_label("A BRIGHTER BEGINNING", 11, Color("80643c")))
	hero_col.add_child(_center_label("300 ◆   ·   500 ⚡   ·   +1 BAG SLOT", 16, Color("302319")))
	hero_col.add_child(_center_label("A generous starter bundle for a new detective.", 11, Color("4c3926")))
	var starter := _button("STARTER PACK  ·  COMING SOON", UI.GOLD, UI.INK)
	starter.disabled = true
	hero_col.add_child(starter)
	var tabs := HBoxContainer.new()
	root.add_child(tabs)
	for t in ["⚡  ENERGY", "◆  GEMS", "✦  DECOR"]:
		var tab := _button(t, UI.NAVY_LIGHT, UI.CREAM)
		tab.size_flags_horizontal = Control.SIZE_EXPAND_FILL
		tab.add_theme_font_size_override("font_size", 10)
		tabs.add_child(tab)
	var section := _label("FIELD ENERGY", 14, UI.GOLD)
	root.add_child(section)
	var grid := GridContainer.new()
	grid.columns = 2
	grid.add_theme_constant_override("h_separation", 10)
	grid.add_theme_constant_override("v_separation", 10)
	root.add_child(grid)
	_add_pack(grid, "+10", "ENERGY", "250 coins", 250, 10)
	_add_pack(grid, "+25", "ENERGY", "500 coins", 500, 25)
	_add_pack(grid, "+50", "ENERGY", "900 coins", 900, 50)
	_add_pack(grid, "+75", "ENERGY", "1,300 coins", 1300, 75)
	var ad_card := PanelContainer.new()
	ad_card.add_theme_stylebox_override("panel", UI.panel(Color("123b45"), UI.TEAL_BRIGHT, 14))
	root.add_child(ad_card)
	var ad_col := VBoxContainer.new()
	ad_card.add_child(ad_col)
	ad_col.add_child(_label("OPTIONAL REWARDED VIDEO", 12, UI.GOLD))
	ad_col.add_child(_label("Development test only — no real ad network is connected.", 10, UI.MUTED))
	var ad := _button("CLAIM TEST REWARD  ·  +10 ENERGY", UI.TEAL, UI.CREAM)
	ad.pressed.connect(func():
		GameState.add_energy(10)
		feedback.text = "10 test energy added."
		_refresh_wallet()
	)
	ad_col.add_child(ad)
	feedback = _label("", 11, UI.GOLD)
	root.add_child(feedback)
	var spacer := Control.new()
	spacer.size_flags_vertical = Control.SIZE_EXPAND_FILL
	root.add_child(spacer)
	var footer := HBoxContainer.new()
	root.add_child(footer)
	var board := _button("‹  BACK TO INVESTIGATION", UI.NAVY_LIGHT, UI.CREAM)
	board.size_flags_horizontal = Control.SIZE_EXPAND_FILL
	board.pressed.connect(func(): NavigationManager.show_screen("board"))
	footer.add_child(board)
	var home := _button("HOME", UI.NAVY_LIGHT, UI.CREAM)
	home.pressed.connect(func(): NavigationManager.show_screen("home"))
	footer.add_child(home)
	_refresh_wallet()

func _add_pack(parent: GridContainer, amount: String, name: String, price: String, cost: int, energy: int) -> void:
	var card := PanelContainer.new()
	card.add_theme_stylebox_override("panel", UI.panel(Color("12313d"), UI.GOLD_DARK, 13))
	parent.add_child(card)
	var col := VBoxContainer.new()
	col.alignment = BoxContainer.ALIGNMENT_CENTER
	col.add_theme_constant_override("separation", 5)
	card.add_child(col)
	col.add_child(_center_label("⚡", 31, UI.GOLD))
	col.add_child(_center_label(amount, 23, UI.CREAM))
	col.add_child(_center_label(name, 10, UI.MUTED))
	col.add_child(_center_label(price, 10, UI.PAPER))
	var buy := _button("BUY", UI.GOLD, UI.INK)
	buy.pressed.connect(func():
		var result := GameState.purchase_energy_pack(cost, energy)
		feedback.text = str(result.get("message", "Purchase unavailable."))
		_refresh_wallet()
	)
	col.add_child(buy)

func _refresh_wallet() -> void:
	if is_instance_valid(wallet):
		wallet.text = "⚡ %d/%d       ◉ %s       ◆ %d" % [GameState.energy, GameState.max_energy, _commas(GameState.coins), GameState.gems]

func _label(value: String, size: int, color: Color) -> Label:
	var l := Label.new()
	l.text = value
	l.add_theme_font_size_override("font_size", size)
	l.add_theme_color_override("font_color", color)
	l.vertical_alignment = VERTICAL_ALIGNMENT_CENTER
	return l

func _center_label(value: String, size: int, color: Color) -> Label:
	var l := _label(value, size, color)
	l.horizontal_alignment = HORIZONTAL_ALIGNMENT_CENTER
	return l

func _button(value: String, bg: Color, fg: Color) -> Button:
	var b := Button.new()
	b.text = value
	b.custom_minimum_size.y = 42
	b.add_theme_font_size_override("font_size", 12)
	b.add_theme_color_override("font_color", fg)
	b.add_theme_color_override("font_hover_color", fg)
	b.add_theme_stylebox_override("normal", UI.button_style(bg, UI.GOLD_DARK))
	b.add_theme_stylebox_override("hover", UI.button_style(bg.lightened(0.08), UI.GOLD))
	return b

func _commas(value: int) -> String:
	var s := str(value)
	var out := ""
	while s.length() > 3:
		out = "," + s.substr(s.length() - 3) + out
		s = s.substr(0, s.length() - 3)
	return s + out
