package com.example.mysterymerge.data

import com.example.R
import com.example.mysterymerge.model.*

object GameContent {
    const val BOARD_COLUMNS = 7
    const val BOARD_ROWS = 9
    const val BOARD_SIZE = BOARD_COLUMNS * BOARD_ROWS
    const val INVENTORY_SIZE = 8
    const val GENERATOR_COST = 5

    val chains = listOf(
        EvidenceChain("photograph", "The Photograph", "study"),
        EvidenceChain("key", "The Brass Key", "study"),
        EvidenceChain("hidden_note", "The Hidden Note", "study"),
        EvidenceChain("footprint", "The Garden Footprint", "garden_room"),
        EvidenceChain("dust", "The Attic Dust", "locked_attic"),
        EvidenceChain("ribbon", "The Blue Ribbon", "garden_room"),
        EvidenceChain("wax_seal", "The Family Seal", "locked_attic"),
        EvidenceChain("music", "The Midnight Score", "locked_attic"),
        EvidenceChain("flower", "The Pressed Flower", "garden_room"),
        EvidenceChain("locket", "The Missing Locket", "locked_attic")
    )

    val items = listOf(
        // Photograph chain
        EvidenceItem("torn_photo", "Torn Photograph", "Photo", "Two torn halves from the family portrait.", 1, "photograph", 5, "reassembled_photo", "▧", "#365a68"),
        EvidenceItem("reassembled_photo", "Reassembled Photograph", "Portrait", "The pieces fit. Someone is standing behind the window.", 2, "photograph", 10, "annotated_photo", "▣", "#426b72"),
        EvidenceItem("annotated_photo", "Annotated Photograph", "Marked Photo", "A pencilled time stamp points to just after midnight.", 3, "photograph", 20, "", "▤", "#547c78"),

        // Key chain
        EvidenceItem("old_key", "Old Brass Key", "Old Key", "A tarnished key found beneath the study desk.", 1, "key", 5, "cleaned_key", "⚿", "#725b39"),
        EvidenceItem("cleaned_key", "Cleaned Brass Key", "Key", "The dust is gone and a tiny crest appears.", 2, "key", 10, "engraved_key", "⚿", "#8b7045"),
        EvidenceItem("engraved_key", "Engraved Mansion Key", "Crest Key", "The engraving matches the locked attic door.", 3, "key", 20, "", "⚿", "#a18350"),

        // Hidden Note chain
        EvidenceItem("folded_note", "Folded Note", "Note", "A small note folded into the lining of a book.", 1, "hidden_note", 5, "opened_note", "✉", "#76644e"),
        EvidenceItem("opened_note", "Opened Note", "Open Note", "The paper has a faint message pressed into it.", 2, "hidden_note", 10, "decoded_note", "✉", "#8c7659"),
        EvidenceItem("decoded_note", "Decoded Hidden Note", "Decoded", "The message reads: check the clock when the house is quiet.", 3, "hidden_note", 20, "", "✉", "#a48b68"),

        // Footprint chain
        EvidenceItem("footprint_trace", "Footprint Trace", "Footprint", "A partial impression in the damp garden path.", 1, "footprint", 5, "garden_impression", "⌁", "#3d6653"),
        EvidenceItem("garden_impression", "Garden Impression", "Impression", "The heel and narrow toe become clear.", 2, "footprint", 10, "matching_footprint", "⌁", "#4c795f"),
        EvidenceItem("matching_footprint", "Matching Footprint", "Matched", "The print matches a pair of shoes kept upstairs.", 3, "footprint", 20, "", "⌁", "#638b6b"),

        // Attic Dust chain
        EvidenceItem("dust_sample", "Attic Dust Sample", "Dust", "A little grey dust collected near the hatch.", 1, "dust", 5, "sifted_dust", "⁙", "#53616a"),
        EvidenceItem("sifted_dust", "Sifted Attic Dust", "Sifted", "A strand of blue wool is trapped in the dust.", 2, "dust", 10, "attic_fiber", "⁙", "#68747a"),
        EvidenceItem("attic_fiber", "Blue Attic Fiber", "Fiber", "The fiber matches the ribbon found in the garden room.", 3, "dust", 20, "", "⁙", "#7e8989"),

        // Blue Ribbon chain
        EvidenceItem("blue_ribbon", "Blue Ribbon", "Ribbon", "A frayed blue ribbon caught on a rose thorn.", 1, "ribbon", 5, "mended_ribbon", "≋", "#385b77"),
        EvidenceItem("mended_ribbon", "Mended Blue Ribbon", "Mended", "The torn end has been carefully repaired.", 2, "ribbon", 10, "embroidered_ribbon", "≋", "#466d8c"),
        EvidenceItem("embroidered_ribbon", "Embroidered Ribbon", "Stitched", "A tiny initial is stitched into the underside.", 3, "ribbon", 20, "", "≋", "#5b82a0"),

        // Wax Seal chain
        EvidenceItem("wax_fragment", "Wax Fragment", "Wax", "A broken piece of dark red sealing wax.", 1, "wax_seal", 5, "melted_seal", "◉", "#75434a"),
        EvidenceItem("melted_seal", "Mended Wax Seal", "Seal", "The broken edges form part of a family crest.", 2, "wax_seal", 10, "family_seal", "◉", "#8d5157"),
        EvidenceItem("family_seal", "Complete Family Seal", "Crest Seal", "The seal belongs to the Cole family archive.", 3, "wax_seal", 20, "", "◉", "#a76162"),

        // Music Score chain
        EvidenceItem("torn_score", "Torn Music Score", "Score", "A few bars of music torn from an old composition.", 1, "music", 5, "restored_score", "♫", "#5c526f"),
        EvidenceItem("restored_score", "Restored Music Score", "Restored", "The missing notes reveal a familiar melody.", 2, "music", 10, "midnight_melody", "♫", "#716484"),
        EvidenceItem("midnight_melody", "Midnight Melody", "Melody", "The melody was played at the time of the photograph.", 3, "music", 20, "", "♫", "#88769a"),

        // Pressed Flower chain
        EvidenceItem("dried_bloom", "Dried Flower", "Flower", "A faded bloom pressed between the pages of a diary.", 1, "flower", 5, "pressed_bloom", "✿", "#6b5a4d"),
        EvidenceItem("pressed_bloom", "Pressed Flower", "Pressed", "Its petals are still faintly scented with rosewater.", 2, "flower", 10, "garden_token", "✿", "#856d59"),
        EvidenceItem("garden_token", "Garden Token", "Token", "A tiny brass token was hidden beneath the petals.", 3, "flower", 20, "", "✿", "#a08265"),

        // Locket chain
        EvidenceItem("empty_chain", "Empty Locket Chain", "Chain", "A delicate chain with an empty clasp.", 1, "locket", 5, "locket_clasp", "◇", "#766443"),
        EvidenceItem("locket_clasp", "Locket Clasp", "Clasp", "The clasp bears a tiny scratch shaped like a crescent.", 2, "locket", 10, "missing_locket", "◇", "#947c4e"),
        EvidenceItem("missing_locket", "The Missing Locket", "Locket", "The family heirloom is found tucked behind the attic clock.", 3, "locket", 20, "", "◇", "#b3945d")
    )

    val itemMap: Map<String, EvidenceItem> = items.associateBy { it.id }

    val generators = listOf(
        GeneratorLocation(
            id = "study",
            title = "Study Desk",
            description = "Search drawers, books and vintage photos for overlooked clues.",
            pool = listOf(
                GeneratorPoolItem("torn_photo", 4),
                GeneratorPoolItem("old_key", 3),
                GeneratorPoolItem("folded_note", 3),
                GeneratorPoolItem("dust_sample", 2)
            )
        ),
        GeneratorLocation(
            id = "garden_room",
            title = "Garden Room",
            description = "Look along the damp garden path, flower beds, and terrace.",
            pool = listOf(
                GeneratorPoolItem("footprint_trace", 4),
                GeneratorPoolItem("blue_ribbon", 3),
                GeneratorPoolItem("dried_bloom", 3),
                GeneratorPoolItem("old_key", 1)
            )
        ),
        GeneratorLocation(
            id = "locked_attic",
            title = "Locked Attic",
            description = "Search old keepsakes, antique clockwork, and sealed chests upstairs.",
            pool = listOf(
                GeneratorPoolItem("wax_fragment", 4),
                GeneratorPoolItem("torn_score", 3),
                GeneratorPoolItem("empty_chain", 3),
                GeneratorPoolItem("dust_sample", 2)
            )
        )
    )

    // 5 Full Detective Cases!
    val cases = listOf(
        CaseModel(
            id = "case_01",
            number = 1,
            title = "The Missing Locket",
            subtitle = "A family heirloom vanished from Hale Manor",
            description = "During a stormy midnight dinner at Hale Manor, the grandmother's heirloom locket disappeared without a trace. Follow the clues through the study, garden, and attic.",
            thumbResId = R.drawable.case_thumb_01,
            locations = listOf("study", "garden_room", "locked_attic"),
            tasks = listOf(
                TaskItem("task_c1_photo", "case_01", "Reconstruct the photograph", listOf("reassembled_photo"), 80, 0, "scene_midnight_photo"),
                TaskItem("task_c1_key", "case_01", "Identify the old key", listOf("cleaned_key"), 90, 1, "scene_hidden_room"),
                TaskItem("task_c1_note", "case_01", "Decode the hidden note", listOf("decoded_note"), 100, 1, "scene_note"),
                TaskItem("task_c1_footprint", "case_01", "Match the garden footprint", listOf("garden_impression"), 110, 1, "scene_footprint"),
                TaskItem("task_c1_locket", "case_01", "Find the missing locket", listOf("missing_locket"), 150, 5, "scene_locket_truth")
            ),
            completionCoins = 500,
            completionGems = 10
        ),
        CaseModel(
            id = "case_02",
            number = 2,
            title = "The Clock Tower Cipher",
            subtitle = "The grandfather clock stopped dead at 12:17 AM",
            description = "The antique pendulum clock in the western tower abruptly ceased its chimes. Hidden beneath the brass weights is a cylinder cipher pointing to a secret family trust.",
            thumbResId = R.drawable.case_thumb_02,
            locations = listOf("locked_attic", "study", "garden_room"),
            tasks = listOf(
                TaskItem("task_c2_dust", "case_02", "Sift the clock tower dust", listOf("sifted_dust"), 120, 1, "scene_c2_dust"),
                TaskItem("task_c2_ribbon", "case_02", "Repair the pendulum silk ribbon", listOf("mended_ribbon"), 130, 1, "scene_c2_ribbon"),
                TaskItem("task_c2_key", "case_02", "Forge the engraved tower key", listOf("engraved_key"), 140, 2, "scene_c2_key"),
                TaskItem("task_c2_score", "case_02", "Restructure the midnight score", listOf("restored_score"), 160, 2, "scene_c2_score"),
                TaskItem("task_c2_watch", "case_02", "Unlock the pocket watch cipher", listOf("midnight_melody"), 200, 6, "scene_c2_cipher")
            ),
            completionCoins = 750,
            completionGems = 15
        ),
        CaseModel(
            id = "case_03",
            number = 3,
            title = "The Rose Garden Mystery",
            subtitle = "Broken glass and muddy traces on the terrace",
            description = "At 2:00 AM, the glass conservatory doors were found ajar. Rare rosebuds were crushed, and a damp footprint trail leads toward the family archive.",
            thumbResId = R.drawable.case_thumb_03,
            locations = listOf("garden_room", "study", "locked_attic"),
            tasks = listOf(
                TaskItem("task_c3_flower", "case_03", "Examine the pressed rose bloom", listOf("pressed_bloom"), 150, 1, "scene_c3_flower"),
                TaskItem("task_c3_footprint", "case_03", "Match the terrace footprints", listOf("matching_footprint"), 170, 2, "scene_c3_footprint"),
                TaskItem("task_c3_ribbon", "case_03", "Inspect the embroidered silk ribbon", listOf("embroidered_ribbon"), 190, 2, "scene_c3_ribbon"),
                TaskItem("task_c3_token", "case_03", "Clean the brass garden token", listOf("garden_token"), 210, 3, "scene_c3_token"),
                TaskItem("task_c3_photo", "case_03", "Develop the marked terrace photo", listOf("annotated_photo"), 260, 8, "scene_c3_truth")
            ),
            completionCoins = 1000,
            completionGems = 20
        ),
        CaseModel(
            id = "case_04",
            number = 4,
            title = "The Alchemist's Secret",
            subtitle = "A hidden vault behind the library bookcase",
            description = "Behind a secret bookshelf in the Hale library, an antique study containing sealing wax, vintage cipher notes, and an ancient crest seal reveals a decades-old deception.",
            thumbResId = R.drawable.case_thumb_04,
            locations = listOf("study", "locked_attic", "garden_room"),
            tasks = listOf(
                TaskItem("task_c4_fiber", "case_04", "Trace the blue wool fiber", listOf("attic_fiber"), 190, 2, "scene_c4_fiber"),
                TaskItem("task_c4_wax", "case_04", "Mend the broken wax seal", listOf("melted_seal"), 210, 2, "scene_c4_wax"),
                TaskItem("task_c4_chain", "case_04", "Restore the engraved locket clasp", listOf("locket_clasp"), 230, 3, "scene_c4_chain"),
                TaskItem("task_c4_seal", "case_04", "Complete the Cole family archive seal", listOf("family_seal"), 260, 4, "scene_c4_seal"),
                TaskItem("task_c4_note", "case_04", "Translate the master decoded note", listOf("decoded_note"), 320, 10, "scene_c4_truth")
            ),
            completionCoins = 1200,
            completionGems = 25
        ),
        CaseModel(
            id = "case_05",
            number = 5,
            title = "The Midnight Masquerade",
            subtitle = "The grand gala where everyone wore masks",
            description = "On the night of the annual masquerade ball, five guests had access to the master suite safe. Reconstruct the final clues to unmask who orchestrated the grand plot.",
            thumbResId = R.drawable.case_thumb_05,
            locations = listOf("locked_attic", "study", "garden_room"),
            tasks = listOf(
                TaskItem("task_c5_photo", "case_05", "Unmask the portrait photograph", listOf("reassembled_photo"), 250, 3, "scene_c5_photo"),
                TaskItem("task_c5_key", "case_05", "Forge the engraved mansion key", listOf("engraved_key"), 280, 4, "scene_c5_key"),
                TaskItem("task_c5_score", "case_05", "Play the full midnight melody score", listOf("midnight_melody"), 310, 5, "scene_c5_score"),
                TaskItem("task_c5_token", "case_05", "Verify the brass garden token", listOf("garden_token"), 350, 6, "scene_c5_token"),
                TaskItem("task_c5_locket", "case_05", "Recover the true heirloom locket", listOf("missing_locket"), 500, 15, "scene_c5_truth")
            ),
            completionCoins = 2000,
            completionGems = 50
        ),
        CaseModel(
            id = "case_06",
            number = 6,
            title = "The Silent Conservatory",
            subtitle = "One broken pane, and the orchids are gone",
            description = "Hale Manor's glass conservatory fell silent overnight. A single pane is shattered from the inside, and someone took great care to hide what they carried out.",
            thumbResId = R.drawable.case_thumb_06,
            locations = listOf("garden_room", "study", "locked_attic"),
            tasks = listOf(
                TaskItem("task_c6_bloom", "case_06", "Identify the pressed orchid bloom", listOf("pressed_bloom"), 240, 2, "scene_c6_bloom"),
                TaskItem("task_c6_ribbon", "case_06", "Mend the ribbon from the broken pane", listOf("mended_ribbon"), 260, 2, "scene_c6_ribbon"),
                TaskItem("task_c6_key", "case_06", "Clean the potting-shed key", listOf("cleaned_key"), 280, 3, "scene_c6_key"),
                TaskItem("task_c6_fiber", "case_06", "Trace the fibre caught in the glass", listOf("attic_fiber"), 320, 4, "scene_c6_fiber"),
                TaskItem("task_c6_stitch", "case_06", "Read the embroidered initials", listOf("embroidered_ribbon"), 420, 12, "scene_c6_truth")
            ),
            completionCoins = 1400,
            completionGems = 30
        ),
        CaseModel(
            id = "case_07",
            number = 7,
            title = "The Sealed Letter",
            subtitle = "A letter nobody was ever meant to open",
            description = "A letter sealed with the Cole crest was found in the library fireplace, half burned. Whoever wrote it wanted the truth to survive, and someone else wanted it gone.",
            thumbResId = R.drawable.case_thumb_07,
            locations = listOf("study", "locked_attic", "garden_room"),
            tasks = listOf(
                TaskItem("task_c7_note", "case_07", "Open the scorched letter", listOf("opened_note"), 260, 2, "scene_c7_note"),
                TaskItem("task_c7_wax", "case_07", "Mend the broken wax seal", listOf("melted_seal"), 280, 2, "scene_c7_wax"),
                TaskItem("task_c7_photo", "case_07", "Reassemble the enclosed photograph", listOf("reassembled_photo"), 300, 3, "scene_c7_photo"),
                TaskItem("task_c7_seal", "case_07", "Complete the Cole family seal", listOf("family_seal"), 380, 5, "scene_c7_seal"),
                TaskItem("task_c7_decode", "case_07", "Decode the final paragraph", listOf("decoded_note"), 460, 14, "scene_c7_truth")
            ),
            completionCoins = 1600,
            completionGems = 32
        ),
        CaseModel(
            id = "case_08",
            number = 8,
            title = "The Violinist's Last Note",
            subtitle = "The music stopped mid-bar at the midnight recital",
            description = "Celeste Hale's violin fell silent in the middle of her own recital, and she hasn't been seen since. The unfinished score may be the only thing she left behind.",
            thumbResId = R.drawable.case_thumb_08,
            locations = listOf("locked_attic", "study", "garden_room"),
            tasks = listOf(
                TaskItem("task_c8_score", "case_08", "Restore the unfinished score", listOf("restored_score"), 300, 3, "scene_c8_score"),
                TaskItem("task_c8_dust", "case_08", "Sift the rosin dust from the case", listOf("sifted_dust"), 320, 3, "scene_c8_dust"),
                TaskItem("task_c8_clasp", "case_08", "Restore the violin case clasp", listOf("locket_clasp"), 340, 3, "scene_c8_clasp"),
                TaskItem("task_c8_melody", "case_08", "Play the full midnight melody", listOf("midnight_melody"), 440, 6, "scene_c8_melody"),
                TaskItem("task_c8_photo", "case_08", "Read the recital photograph", listOf("annotated_photo"), 520, 16, "scene_c8_truth")
            ),
            completionCoins = 1800,
            completionGems = 35
        ),
        CaseModel(
            id = "case_09",
            number = 9,
            title = "The Cellar Beneath the Chapel",
            subtitle = "A door that has not opened in sixty years",
            description = "Behind the manor chapel's altar, a cellar door has been forced from below. Every trail you have followed so far leads down these steps.",
            thumbResId = R.drawable.case_thumb_09,
            locations = listOf("study", "locked_attic", "garden_room"),
            tasks = listOf(
                TaskItem("task_c9_key", "case_09", "Clean the cellar door key", listOf("cleaned_key"), 340, 3, "scene_c9_key"),
                TaskItem("task_c9_print", "case_09", "Match the print on the stair", listOf("garden_impression"), 360, 3, "scene_c9_print"),
                TaskItem("task_c9_wax", "case_09", "Mend the wax from the lock", listOf("melted_seal"), 380, 4, "scene_c9_wax"),
                TaskItem("task_c9_engrave", "case_09", "Read the engraved crest key", listOf("engraved_key"), 480, 7, "scene_c9_engrave"),
                TaskItem("task_c9_footprint", "case_09", "Follow the matching footprints", listOf("matching_footprint"), 560, 18, "scene_c9_truth")
            ),
            completionCoins = 2000,
            completionGems = 40
        ),
        CaseModel(
            id = "case_10",
            number = 10,
            title = "The Heir's Return",
            subtitle = "Every thread leads back to Hale Manor",
            description = "The last heir has come home, and the final pieces of the Hale mystery lie on your desk. Assemble the complete evidence and close the book for good.",
            thumbResId = R.drawable.case_thumb_10,
            locations = listOf("locked_attic", "garden_room", "study"),
            tasks = listOf(
                TaskItem("task_c10_seal", "case_10", "Present the complete family seal", listOf("family_seal"), 500, 8, "scene_c10_seal"),
                TaskItem("task_c10_token", "case_10", "Present the brass garden token", listOf("garden_token"), 540, 8, "scene_c10_token"),
                TaskItem("task_c10_fiber", "case_10", "Present the blue attic fibre", listOf("attic_fiber"), 580, 9, "scene_c10_fiber"),
                TaskItem("task_c10_melody", "case_10", "Present the midnight melody", listOf("midnight_melody"), 620, 10, "scene_c10_melody"),
                TaskItem("task_c10_locket", "case_10", "Present the missing locket", listOf("missing_locket"), 800, 25, "scene_c10_truth")
            ),
            completionCoins = 3000,
            completionGems = 75
        )
    )

    val caseMap: Map<String, CaseModel> = cases.associateBy { it.id }

    /** "CASE 07" style label that also works for two-digit case numbers. */
    fun caseLabel(number: Int): String = "CASE %02d".format(number)

    fun previousCase(case: CaseModel): CaseModel? = cases.getOrNull(case.number - 2)

    fun nextCase(case: CaseModel): CaseModel? = cases.getOrNull(case.number)

    fun isCaseUnlocked(case: CaseModel, completedCases: List<String>): Boolean {
        val prev = previousCase(case) ?: return true
        return completedCases.contains(prev.id)
    }

    /** Single source of truth for generator unlocks, used by both the UI and the repository. */
    fun isGeneratorUnlocked(generatorId: String, state: GameSaveState): Boolean {
        val activeIndex = state.caseTaskIndices[state.currentCaseId] ?: state.currentTaskIndex
        val anyCaseSolved = state.completedCases.isNotEmpty()
        return when (generatorId) {
            "garden_room" -> activeIndex >= 2 || anyCaseSolved
            "locked_attic" -> activeIndex >= 4 || anyCaseSolved
            else -> true
        }
    }

    val dialogues = listOf(
        // Case 1
        DialogueScene("scene_midnight_photo", "Nora turns the photograph toward the lamp. The timestamp is just after midnight — hours after everyone claimed the house was asleep."),
        DialogueScene("scene_hidden_room", "The brass key carries the Cole family crest. It belongs to a door that Richard insists has been locked for years."),
        DialogueScene("scene_note", "The note reads: “When the clock stops, look where the music ends.” Someone left instructions for whoever found it."),
        DialogueScene("scene_footprint", "The garden impression matches a narrow pair of shoes. The damp soil shows the visitor came back toward the house, not away from it."),
        DialogueScene("scene_locket_truth", "Behind the attic clock, Nora finds the missing locket wrapped in blue ribbon. It was hidden to protect a secret, not stolen for money."),

        // Case 2
        DialogueScene("scene_c2_dust", "Inside the clock casing, fine attic dust coats a tiny brass gear. It was wedged deliberately to halt the pendulum."),
        DialogueScene("scene_c2_ribbon", "The pendulum ribbon carries a stitched monogram: 'E.C.' It matches the ribbon found in the old study desk."),
        DialogueScene("scene_c2_key", "The engraved key turns the secondary lock of the clock case. Inside sits a brass cylinder with intricate musical pins."),
        DialogueScene("scene_c2_score", "The music pins align with the restored midnight score. When played, the chime mechanism clicks open a secret compartment!"),
        DialogueScene("scene_c2_cipher", "The pocket watch reveals the true date of the deed: 1894. The estate trust was never legitimately signed away."),

        // Case 3
        DialogueScene("scene_c3_flower", "The pressed rose bloom is a rare Queen Anne hybrid grown exclusively in the glass conservatory."),
        DialogueScene("scene_c3_footprint", "The footprints in the muddy gravel show that two people met by the terrace fountain at 2:00 AM."),
        DialogueScene("scene_c3_ribbon", "Caught on the fountain statue is an embroidered ribbon bearing the seal of the family solicitor."),
        DialogueScene("scene_c3_token", "The brass token clicks into the base of the sundial, revealing an underground archive hatch!"),
        DialogueScene("scene_c3_truth", "The annotated photograph proves the midnight visitor was the solicitor himself, removing original deeds."),

        // Case 4
        DialogueScene("scene_c4_fiber", "Under the loupe, the blue wool fiber matches the coat worn by Lord Richard on the night of the masquerade."),
        DialogueScene("scene_c4_wax", "The sealing wax contains traces of copper and sulfur — a formula only used by the family's private apothecary."),
        DialogueScene("scene_c4_chain", "The clasp fits an antique chain found in the grandfather's secret journal."),
        DialogueScene("scene_c4_seal", "The Cole family seal impression matches the false signature on the disputed will."),
        DialogueScene("scene_c4_truth", "The decoded note exposes the entire plot: the inheritance had been forged forty years ago."),

        // Case 5
        DialogueScene("scene_c5_photo", "By comparing the photograph against the ballroom guest list, the masked figure in the black domino is identified!"),
        DialogueScene("scene_c5_key", "The engraved mansion key unlocks the master vault behind the grand ballroom portrait."),
        DialogueScene("scene_c5_score", "As the midnight melody echoes through the mansion, the final safe dial falls into alignment."),
        DialogueScene("scene_c5_token", "The brass token acts as the key to the inner safe deposit box."),
        DialogueScene("scene_c5_truth", "Case Closed! The missing locket, the true deeds, and the complete family history are restored to the rightful heir."),

        // Case 6
        DialogueScene("scene_c6_bloom", "The pressed orchid is a species the conservatory has not grown in thirty years. Someone brought it in from outside."),
        DialogueScene("scene_c6_ribbon", "The mended ribbon is knotted in a sailor's hitch. Whoever tied it spent time at sea, which narrows the suspects."),
        DialogueScene("scene_c6_key", "The cleaned key opens the potting shed, where a muddy pair of boots sits too neatly in the corner."),
        DialogueScene("scene_c6_fiber", "The fibre in the broken pane is blue wool, matching the attic. The thief climbed up from the house, not in from the garden."),
        DialogueScene("scene_c6_truth", "The embroidered initials read 'M.H.'. Margaret Hale staged the break-in herself to protect the orchids' real owner."),

        // Case 7
        DialogueScene("scene_c7_note", "The scorched page opens to a single line: 'If you are reading this, I could not say it aloud.'"),
        DialogueScene("scene_c7_wax", "The wax carries a thumbprint pressed into the crest. Whoever sealed it did so in a hurry, and left-handed."),
        DialogueScene("scene_c7_photo", "The photograph inside shows two figures at the chapel door. One is Richard Cole. The other has been cut away."),
        DialogueScene("scene_c7_seal", "The complete seal matches the archive ledger, and the ledger has a missing page dated the night of the letter."),
        DialogueScene("scene_c7_truth", "The decoded paragraph names the writer: Eleanor Cole, who disappeared after learning who forged her father's will."),

        // Case 8
        DialogueScene("scene_c8_score", "The restored score ends on a rest that was never written by the composer. Someone added it to hide a message."),
        DialogueScene("scene_c8_dust", "Rosin dust, but grey and mixed with attic grit. The violin case was opened somewhere it should never have been."),
        DialogueScene("scene_c8_clasp", "The clasp was forced and re-bent by hand. The violin was taken out quickly, and was not meant to be seen leaving."),
        DialogueScene("scene_c8_melody", "Played in full, the melody unlocks the music box in the green room, and inside it is a folded programme."),
        DialogueScene("scene_c8_truth", "The recital photograph shows an empty seat in the front row. Celeste left of her own will, with help from the one person who stayed away."),

        // Case 9
        DialogueScene("scene_c9_key", "The cleaned key turns in the cellar lock with no resistance. Someone has oiled it recently."),
        DialogueScene("scene_c9_print", "The print on the stair is fresh, and a size smaller than the groundskeeper's boots."),
        DialogueScene("scene_c9_wax", "Mended wax from the lock matches the letter seal. Whoever sealed the letter has been down here before."),
        DialogueScene("scene_c9_engrave", "The engraved key's crest fits a recess in the cellar wall, and a stone panel slides aside."),
        DialogueScene("scene_c9_truth", "The footprints lead through the hidden passage to the chapel. Sixty years of secrets were being moved, one crate at a time."),

        // Case 10
        DialogueScene("scene_c10_seal", "The complete seal is pressed onto the final document, and the Hale family archive opens for the first time in a generation."),
        DialogueScene("scene_c10_token", "The brass token fits the lock of the heir's trunk. Inside lie letters from every case you have solved."),
        DialogueScene("scene_c10_fiber", "The blue fibre matches the heir's own coat. They were in the attic that night, but not as a thief."),
        DialogueScene("scene_c10_melody", "The midnight melody plays one last time, and Nora realises every clue was left on purpose, in order."),
        DialogueScene("scene_c10_truth", "The locket opens to a portrait of the heir as a child. The mystery was never a crime. It was a family finding its way home. Case Closed.")
    )

    val dialogueMap: Map<String, String> = dialogues.associate { it.id to it.text }

    val energyPacks = listOf(
        EnergyPack(10, "Quick Inspection", 250),
        EnergyPack(25, "Field Supply", 500),
        EnergyPack(50, "Lead Pursuit", 900),
        EnergyPack(75, "Night Search", 1300)
    )

    fun getItemDrawable(id: String): Int = when (id) {
        "torn_photo" -> R.drawable.item_torn_photo
        "reassembled_photo" -> R.drawable.item_reassembled_photo
        "annotated_photo" -> R.drawable.item_annotated_photo
        "old_key" -> R.drawable.item_old_key
        "cleaned_key" -> R.drawable.item_cleaned_key
        "engraved_key" -> R.drawable.item_engraved_key
        "folded_note" -> R.drawable.item_folded_note
        "opened_note" -> R.drawable.item_opened_note
        "decoded_note" -> R.drawable.item_decoded_note
        "footprint_trace" -> R.drawable.item_footprint_trace
        "garden_impression" -> R.drawable.item_garden_impression
        "matching_footprint" -> R.drawable.item_matching_footprint
        "dust_sample" -> R.drawable.item_dust_sample
        "sifted_dust" -> R.drawable.item_sifted_dust
        "attic_fiber" -> R.drawable.item_attic_fiber
        "blue_ribbon" -> R.drawable.item_blue_ribbon
        "mended_ribbon" -> R.drawable.item_mended_ribbon
        "embroidered_ribbon" -> R.drawable.item_embroidered_ribbon
        "wax_fragment" -> R.drawable.item_wax_fragment
        "melted_seal" -> R.drawable.item_melted_seal
        "family_seal" -> R.drawable.item_family_seal
        "torn_score" -> R.drawable.item_torn_score
        "restored_score" -> R.drawable.item_restored_score
        "midnight_melody" -> R.drawable.item_midnight_melody
        "dried_bloom" -> R.drawable.item_dried_bloom
        "pressed_bloom" -> R.drawable.item_pressed_bloom
        "garden_token" -> R.drawable.item_garden_token
        "empty_chain" -> R.drawable.item_empty_chain
        "locket_clasp" -> R.drawable.item_locket_clasp
        "missing_locket" -> R.drawable.item_missing_locket
        else -> R.drawable.tile_empty_recessed
    }
}
