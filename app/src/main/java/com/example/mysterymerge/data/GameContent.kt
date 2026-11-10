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
        )
    )

    val caseMap: Map<String, CaseModel> = cases.associateBy { it.id }

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
        DialogueScene("scene_c5_truth", "Case Closed! The missing locket, the true deeds, and the complete family history are restored to the rightful heir.")
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
