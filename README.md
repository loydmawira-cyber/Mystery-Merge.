# Mystery Merge

Portrait-first Godot 4.3 mystery merge game for Android.

## Implemented gameplay

- 7 × 9 evidence board with tap-to-select and drag-and-drop movement.
- 8 inventory slots for storing and rearranging evidence.
- 30 evidence items across 10 separate merge chains. Items merge only with their matching chain partner and produce the configured next item.
- Three weighted investigation generators: Study, Garden Room and Locked Attic. Each search costs 5 energy.
- Five ordered Case 01 evidence objectives with coin/gem rewards and story reveal dialogue.
- Garden Room and Locked Attic generator access is gated by case progress.
- Energy regeneration at 1 point every 2 minutes, including offline regeneration.
- Local JSON save for board, inventory, currencies, task progress, settings and daily reward claim.
- Daily reward can be claimed once per day.
- Working coin shop energy purchase and a clearly labelled rewarded-video test simulation.
- Settings toggles and local save reset confirmation.
- Case file view showing objectives and investigation location unlocks.

## Controls

- Tap a clue and tap another slot to move or merge it.
- Drag a clue onto another slot to move or merge it.
- Matching clues from the same chain merge into their configured next evidence item.
- Search the Study, Garden Room or Locked Attic generators to create new level-one clues.
- Move excess items into the eight-slot pocket inventory.

## Branding and Android launcher

- Custom Mystery Merge detective app icon (gold-framed portrait and magnifying glass).
- Android adaptive launcher foreground and deep-teal background layers.
- Branded portrait launch splash screen with the Mystery Merge title.
- The Android workflow renders the SVG artwork into the exact PNG sizes required by the Android launcher before Godot imports and exports the project.

## Android build

GitHub Actions builds a debug APK on pushes to main and manual workflow dispatch. The APK is uploaded as the MysteryMerge-debug-apk artifact when the export job passes.

## Release status

The main merge loop, task progression, three generator definitions, local save, economy, shop simulation, daily reward and settings are implemented in source. The Android export pipeline is still being debugged; do not treat an APK as available until the Actions run shows success and the artifact is present. Real ad-network integration, final authored story content, original production art/audio, accessibility and physical-device QA remain release work.