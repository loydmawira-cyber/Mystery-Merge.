extends RefCounted

const NAVY := Color("101e2b")
const NAVY_LIGHT := Color("1b3444")
const TEAL := Color("17616a")
const TEAL_BRIGHT := Color("2a8990")
const GOLD := Color("e6b85b")
const GOLD_DARK := Color("9c6b2e")
const CREAM := Color("f3e5c8")
const PAPER := Color("dfcba6")
const INK := Color("251b16")
const MUTED := Color("b8aa91")

static func panel(bg: Color = NAVY_LIGHT, border: Color = GOLD_DARK, radius: int = 18) -> StyleBoxFlat:
	var s := StyleBoxFlat.new()
	s.bg_color = bg
	s.border_color = border
	s.set_border_width_all(2)
	s.set_corner_radius_all(radius)
	s.content_margin_left = 18
	s.content_margin_right = 18
	s.content_margin_top = 16
	s.content_margin_bottom = 16
	s.shadow_color = Color(0, 0, 0, 0.32)
	s.shadow_size = 8
	s.shadow_offset = Vector2(0, 4)
	return s

static func button_style(bg: Color, border: Color = GOLD) -> StyleBoxFlat:
	var s := StyleBoxFlat.new()
	s.bg_color = bg
	s.border_color = border
	s.set_border_width_all(2)
	s.set_corner_radius_all(16)
	s.content_margin_left = 18
	s.content_margin_right = 18
	s.content_margin_top = 14
	s.content_margin_bottom = 14
	return s
