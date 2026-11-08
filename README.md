# Mystery Merge

A portrait-first Godot 4.3 Android mystery merge game.

## Playable systems currently in the project

- 7 × 9 evidence board with tap-to-select and adjacent merge / move controls.
- Eight evidence tiers, from paper clips and case notes to the final clue.
- Evidence generator that costs 5 energy and places a new item in an empty cell.
- Three-step Case 01 task progression with coin rewards and a case-completion state.
- Energy regeneration at 1 point every 2 minutes, including offline time.
- Local save data for board contents, currencies, tasks, energy and daily reward state.
- In-game coin shop, daily reward and a rewarded-video placeholder for integration testing.
- Portrait Android export preset and GitHub Actions APK build.

## Controls

1. Tap an occupied board cell to select it.
2. Tap an adjacent identical item to merge it into the next evidence tier.
3. Tap an adjacent empty cell to move a selected item.
4. Use Generate to spend 5 energy and create a Paper Clip in an empty cell.
5. Complete the task shown above the board to unlock the next evidence objective.

## Build

The repository includes .github/workflows/android-apk.yml. Push to main or run the workflow manually from the Actions tab. The debug APK is uploaded as the MysteryMerge-debug-apk workflow artifact when export succeeds.

## Current delivery scope

This is the first playable gameplay build, not the final content-complete release. The merge loop, task loop, local save, energy, shop and daily reward simulation are implemented. Remaining release work includes authored story scenes and locations, more cases and item-chain balancing, original art/audio, real rewarded-ad SDK integration, device testing and release signing.
