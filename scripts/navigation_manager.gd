extends Node

var host: Control
var current_screen: String = ""
var screen_scenes := {
	"home": "res://scenes/Home.tscn",
	"board": "res://scenes/MergeBoard.tscn",
	"shop": "res://scenes/Shop.tscn",
	"cases": "res://scenes/Cases.tscn",
	"settings": "res://scenes/Settings.tscn"
}

func set_host(root: Control) -> void:
	host = root

func show_screen(screen_id: String) -> void:
	if host == null:
		return
	if not screen_scenes.has(screen_id):
		push_warning("Unknown screen: " + screen_id)
		return
	var packed: PackedScene = load(screen_scenes[screen_id])
	if packed == null:
		push_error("Could not load screen: " + screen_id)
		return
	for child in host.get_children():
		child.queue_free()
	var screen := packed.instantiate()
	host.add_child(screen)
	current_screen = screen_id
