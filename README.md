# Mystery Merge

Portrait-first native Android mystery merge game built with Kotlin and Jetpack Compose.

## Implemented Gameplay & Features

- **7 × 9 Evidence Board**: Tap-to-select and tap-to-move/merge with intuitive feedback, level badges, and clue symbols.
- **8-Slot Field Bag (Inventory)**: Store clues, swap items, and manage board space.
- **30 Evidence Items across 10 Merge Chains**: Clues merge into their configured next evidence item:
  - Photograph (*Torn Photograph* → *Reassembled* → *Annotated*)
  - Brass Key (*Old Key* → *Cleaned* → *Engraved*)
  - Hidden Note (*Folded Note* → *Opened* → *Decoded*)
  - Garden Footprint (*Footprint Trace* → *Impression* → *Matched*)
  - Attic Dust (*Dust Sample* → *Sifted* → *Blue Fiber*)
  - Blue Ribbon (*Blue Ribbon* → *Mended* → *Embroidered*)
  - Family Seal (*Wax Fragment* → *Mended Seal* → *Complete Crest*)
  - Midnight Score (*Torn Score* → *Restored* → *Midnight Melody*)
  - Pressed Flower (*Dried Bloom* → *Pressed Bloom* → *Garden Token*)
  - Missing Locket (*Empty Chain* → *Locket Clasp* → *The Missing Locket*)
- **3 Investigation Generators**:
  - Study Desk (unlocked by default)
  - Garden Room (unlocked after 2 objectives)
  - Locked Attic (unlocked after 4 objectives)
  - 5 energy cost per search with weighted random clue pools.
- **Case 01 Dossier**: Five ordered case objectives with coin and gem rewards and story reveal dialogue dialogs.
- **Energy Regeneration**: 1 energy point every 2 minutes (120 seconds), including offline regeneration calculated on launch.
- **Local Persistence**: Save state stored locally in JSON (`mystery_merge_save.json`) preserving board, inventory, currencies, task progress, and settings.
- **Daily Detective Gift**: Claimable once per calendar day (+100 coins, +10 energy).
- **Detective Shop**: Coin-based energy supply packs and a development test rewarded video simulation.
- **Settings**: Audio/sound, music, and notification toggles, plus confirmation dialog to reset local game data.
- **Branding & Adaptive Launcher Icon**: Custom adaptive gold magnifying glass & confidential archive icon.

## Android Build & CI/CD

- **GitHub Actions Workflow** (`.github/workflows/android-apk.yml`):
  - Automatically triggered on push to `main` / `master`, pull requests, and manual `workflow_dispatch`.
  - Configures JDK 21 (Temurin) and Gradle 9.3.
  - Builds the debug APK via `./gradlew assembleDebug`.
  - Uploads `MysteryMerge-debug.apk` as a downloadable GitHub Actions artifact (retained 30 days).

## Architecture

- **Language**: Kotlin 2.2+
- **UI Toolkit**: Jetpack Compose with Material Design 3
- **Build System**: Android Gradle Plugin 9.1+ with built-in Kotlin support and Gradle 9.3+
- **Pattern**: Unidirectional Data Flow (UDF) with Kotlin StateFlow and Coroutines