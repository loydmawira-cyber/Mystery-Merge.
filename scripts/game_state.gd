extends Node

# Shared prototype state. Persistence is added in a later stage.
var player_level: int = 1
var energy: int = 72
var max_energy: int = 100
var coins: int = 12450
var gems: int = 38
var current_case: String = "case_01"
var current_location: String = "study"
var case_progress: float = 0.0
var sound_enabled: bool = true
var music_enabled: bool = true
var notifications_enabled: bool = true

signal economy_changed
signal progress_changed

func add_coins(amount: int) -> void:
	coins = maxi(0, coins + amount)
	economy_changed.emit()

func add_gems(amount: int) -> void:
	gems = maxi(0, gems + amount)
	economy_changed.emit()

func spend_energy(amount: int) -> bool:
	if energy < amount:
		return false
	energy -= amount
	economy_changed.emit()
	return true

func set_progress(value: float) -> void:
	case_progress = clampf(value, 0.0, 1.0)
	progress_changed.emit()
