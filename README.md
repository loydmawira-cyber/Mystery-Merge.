# Mystery Merge

Portrait-first native Android mystery merge game built with Kotlin and Jetpack Compose.

## How to Play

### 0. Investigate the Scene
- Each case opens in a room you can **search**. Open the **Scene** tab, read the *Current Lead*, then tap the glowing 🔍 spots (a desk, a trunk, a fireplace) to examine them.
- Some spots are **locked** (🔒) until you follow an earlier lead, so the order you investigate in matters.
- Searching a spot hides **damaged clue pieces** that land on your evidence board. Not every spot is useful: each room has a red herring.
- Take the pieces to the **Board** (the evidence lab) and merge matching ones to restore the evidence an objective needs.
- Every objective's pieces are hidden in the scene, so a case can be solved without spending any energy. The Evidence Chest and its random searches remain as a backup for extra pieces.

### 1. The Core Merge Loop
- **Select**: Tap any evidence clue on the 7×9 board to select it (highlighted with a glowing golden ring).
- **Move**: Tap any empty board slot to move the selected clue and organize your workspace.
- **Merge**: Tap another matching clue from the same evidence chain to merge them into a higher-tier item (Level 1 → Level 2 → Level 3 Max).
- **Inspect**: When a clue is selected, an inspector bar appears below the board showing its name, level, lore description, and next merge preview.
- **Sell**: Liquidate excess clues for coins using the "Sell" button on the inspector bar.
- **Field Bag**: Tap "To Bag" (or an empty inventory slot) to store excess clues off the board. Tap an inventory item and an empty board slot (or "To Board") to retrieve it.

### 2. Searching with the Evidence Chest
- The 3D **Evidence Chest** at the bottom left generates new level-1 clues directly onto the board.
- Each search costs **5 Energy (⚡)**.
- Tap the **(i)** info button on the chest to switch between Manor investigation locations:
  - **Study Desk** *(Always Unlocked)*: Books, desk drawers, old keys, torn photographs, notes, and dust.
  - **Garden Room** *(Unlocks at Case Objective 2)*: Footprints, frayed blue ribbons, dried rose blooms.
  - **Locked Attic** *(Unlocks at Case Objective 4)*: Broken wax seals, torn music scores, empty chains, and clockwork.

### 3. The 10 Evidence Chains
Every clue belongs to one of ten distinct evidence chains and can only merge with its matching chain partner:
1. **The Photograph**: Torn Photograph (Lvl 1) ➔ Reassembled Photograph (Lvl 2) ➔ Annotated Photograph (Lvl 3 Max)
2. **The Brass Key**: Old Brass Key (Lvl 1) ➔ Cleaned Brass Key (Lvl 2) ➔ Engraved Mansion Key (Lvl 3 Max)
3. **The Hidden Note**: Folded Note (Lvl 1) ➔ Opened Note (Lvl 2) ➔ Decoded Hidden Note (Lvl 3 Max)
4. **The Garden Footprint**: Footprint Trace (Lvl 1) ➔ Garden Impression (Lvl 2) ➔ Matching Footprint (Lvl 3 Max)
5. **The Attic Dust**: Attic Dust Sample (Lvl 1) ➔ Sifted Attic Dust / Loupe (Lvl 2) ➔ Blue Attic Fiber (Lvl 3 Max)
6. **The Blue Ribbon**: Blue Ribbon (Lvl 1) ➔ Mended Blue Ribbon (Lvl 2) ➔ Embroidered Ribbon (Lvl 3 Max)
7. **The Family Seal**: Wax Fragment (Lvl 1) ➔ Mended Wax Seal (Lvl 2) ➔ Complete Family Seal (Lvl 3 Max)
8. **The Midnight Score**: Torn Music Score (Lvl 1) ➔ Restored Music Score (Lvl 2) ➔ Midnight Melody Pocket Watch (Lvl 3 Max)
9. **The Pressed Flower**: Dried Flower (Lvl 1) ➔ Pressed Flower (Lvl 2) ➔ Brass Garden Token (Lvl 3 Max)
10. **The Missing Locket**: Empty Locket Chain (Lvl 1) ➔ Locket Clasp (Lvl 2) ➔ The Missing Locket (Lvl 3 Max)

### 4. Solving Cases & Narrative Reveals
- The manila folder banner at the top displays your current **Case** and the required evidence item.
- Making the requested clue (or already holding it on the board or in the Field Bag) hands it in automatically. The clue is **used up**, and you earn **Coins (🪙)**, **Gems (💎)** and an authored detective story scene.
- Completing all 5 objectives solves the case, awards a completion bonus, and **opens the next case automatically**.
- There are **10 cases**:
  1. **The Missing Locket**
  2. **The Clock Tower Cipher**
  3. **The Rose Garden Mystery**
  4. **The Alchemist's Secret**
  5. **The Midnight Masquerade**
  6. **The Silent Conservatory**
  7. **The Sealed Letter**
  8. **The Violinist's Last Note**
  9. **The Cellar Beneath the Chapel**
  10. **The Heir's Return**

### 5. Energy & Economy
- **Regeneration**: Energy restores automatically at **1 point every 2 minutes (120 seconds)** up to 100 max, both in-game and while offline.
- **Daily Detective Gift**: Claim free supplies (+100 Coins, +10 Energy) once per calendar day from the Shop.
- **Detective Shop**: Purchase instant +10, +25, +50, or +75 Energy packs using earned coins. If your energy is nearly full you only pay for the energy that fits.

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