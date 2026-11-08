# Mystery Merge

An original portrait-first cozy detective merge-puzzle game built with Godot 4.x and GDScript. Its visual identity uses midnight navy, deep teal, cream parchment and brass/gold accents. All current interface elements and evidence content are original.

## Implemented in this snapshot

- Godot 4 portrait project setup, shared game state, navigation and the home screen.
- Functional 7-column × 9-row evidence board (63 slots).
- Eight-slot pocket inventory. Clues can be moved between board and inventory.
- Tap-to-select/tap-to-place controls and Control drag-and-drop.
- Matching identical clues merges them into the next level. Invalid combinations and max-level merges are rejected with feedback.
- Three search generators (Study, Garden Room, Locked Attic), each spending 5 energy and placing a weighted random level-one clue in an empty board slot.
- Merge/move feedback animations and live board, inventory and economy counts.
- Ten original three-level merge chains and 30 evidence items.
- Static data-contract validation in tests/validate_game_data.py.

## Run on desktop

1. Install Godot 4.3 or compatible Godot 4.x from https://godotengine.org/download/.
2. Import this folder by selecting project.godot in Godot Project Manager.
3. Press F5 to open the home screen.
4. Choose Continue Case to open the board.
5. Drag two identical clues together, or tap one clue and then tap its match. Move clues to an empty pocket slot. Use Study, Garden or Attic search to spend 5 energy and find a clue.

## Current board rules

- The board contains 63 slots and the pocket inventory contains 8.
- Each generator costs 5 energy and is blocked if the board has no free slot or energy is insufficient.
- Only identical item IDs merge. A successful merge consumes the source clue and upgrades the destination clue.
- Different clues cannot be combined. Two occupied inventory slots can be swapped.
- Board and inventory currently remain in the GameState autoload for the current app session. Versioned disk save/load is still planned; progress is not yet guaranteed to survive a full app restart.

## Architecture

- scripts/game_state.gd: shared economy, evidence catalog, generator logic, board/inventory slots and merge rules.
- scripts/merge_board.gd: board, generators, inventory UI, selection/drop handling and feedback.
- scripts/evidence_tile.gd: reusable evidence slot with drag-and-drop support.
- scripts/navigation_manager.gd: screen navigation.
- scripts/ui_theme.gd: shared navy/teal/cream/brass design tokens.
- data/evidence_items.json: ten merge chains and thirty clue definitions.
- data/generators.json: generator pools and weighted clue drops.
- data/tasks.json, data/cases.json, data/dialogue.json: initial Case 01 content definitions.

## Remaining work — not yet complete

- Versioned local save/load and timed energy regeneration.
- Case 01 task submission, evidence requirements, location unlocks, story reveals and case completion.
- Full Cases screen, simulated shop transactions, rewarded-ad simulation, daily reward claim limits and persistent settings.
- Accessibility, responsive layout and device-performance polish.
- Successful CI Android APK export and installation/touch test on a physical Android device.

## Validation

Run python3 tests/validate_game_data.py to verify unique evidence IDs, ten chains, valid merge targets, generator pools and task references. The GitHub Actions workflow also imports and smoke-runs the board scene before attempting the Android debug export. A successful debug APK artifact is not a signed store release.
