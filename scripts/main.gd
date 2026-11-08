extends Control

func _ready() -> void:
	NavigationManager.set_host(self)
	NavigationManager.show_screen("home")
