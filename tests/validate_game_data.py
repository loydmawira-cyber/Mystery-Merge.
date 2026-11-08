#!/usr/bin/env python3
"""Static content checks for the Mystery Merge Godot project."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

def load(path):
    with (ROOT / path).open(encoding="utf-8") as stream:
        return json.load(stream)

items_doc = load("data/evidence_items.json")
generators_doc = load("data/generators.json")
tasks_doc = load("data/tasks.json")
items = {item["id"]: item for item in items_doc["items"]}
chains = {chain["id"] for chain in items_doc["chains"]}
assert len(items) >= 30, f"Expected at least 30 evidence items, got {len(items)}"
assert len(chains) >= 10, f"Expected at least 10 merge chains, got {len(chains)}"
assert len(items) == len(items_doc["items"]), "Evidence item IDs must be unique"
for item in items.values():
    result = item.get("merge_result_id", "")
    assert item["chain_id"] in chains, f"Unknown chain for {item['id']}"
    if result:
        assert result in items, f"Missing merge result {result} for {item['id']}"
        assert items[result]["chain_id"] == item["chain_id"], f"Cross-chain merge on {item['id']}"
        assert items[result]["level"] == item["level"] + 1, f"Bad level progression on {item['id']}"
for generator in generators_doc["generators"]:
    assert generator.get("pool"), f"Generator {generator['id']} has an empty pool"
    for entry in generator["pool"]:
        assert entry["item_id"] in items, f"Unknown generated item {entry['item_id']}"
        assert items[entry["item_id"]]["level"] == 1, "Generators should create level-one items"
for task in tasks_doc["tasks"]:
    for item_id in task["required_items"]:
        assert item_id in items, f"Task {task['id']} references missing item {item_id}"
print(f"OK: {len(items)} evidence items, {len(chains)} chains, {len(generators_doc['generators'])} generators, {len(tasks_doc['tasks'])} tasks")
