# Mystery Merge

An original cozy detective merge-puzzle prototype built with Godot 4.x and GDScript.

## Included in this repository
- Godot project configured for portrait Android.
- Home screen, shared economy state, navigation and Cases/Board/Shop/Settings placeholder screens.
- Case 01 data: The Missing Locket, evidence definitions, investigation tasks, dialogue and shop catalogue.
- Android export preset and GitHub Actions debug APK workflow.

## Current status
This is the Stage 1–2 project snapshot. The merge board is currently a placeholder; merge mechanics, save persistence, complete case progression, shop simulation, rewarded-ad simulation and device validation are not yet implemented. The APK workflow packages the current prototype, not the full planned game.

## Open in Godot
Import this folder by selecting `project.godot` in Godot Project Manager. Use Godot 4.x and matching Android export templates for local Android exports.

## GitHub Actions
Pushes to `main` run `.github/workflows/android-apk.yml`. A successful run uploads `MysteryMerge-debug.apk` as a workflow artifact.
