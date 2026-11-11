package com.example.mysterymerge.data

import com.example.R
import com.example.mysterymerge.model.CaseScene
import com.example.mysterymerge.model.ClueReward
import com.example.mysterymerge.model.SceneHotspot

/** Investigation scenes: one searchable room per case. Spots 1-5 hold the evidence for objectives 1-5, spot 6 is a red herring. */
object SceneContent {

    val scenes: List<CaseScene> = listOf(
        CaseScene(
            caseId = "case_01",
            title = "Hale Manor Study",
            intro = "Lady Hale's locket vanished from the study on the night of the storm. Search the room. Every drawer, shelf and sill may hold a piece of the truth.",
            imageResId = R.drawable.scene_case_01,
            hotspots = listOf(
                SceneHotspot(id = "c01_h1", label = "Writing Desk", x = 0.50f, y = 0.50f, searchText = "Under the blotter, a family portrait has been ripped in two. Someone wanted a face removed.", clue = ClueReward("torn_photo", 2, "task_c1_photo"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c01_h2", label = "Fireplace Mantel", x = 0.50f, y = 0.86f, searchText = "Behind the cold ashes lie two brass keys, wrapped in a handkerchief. One is blackened with soot.", clue = ClueReward("old_key", 2, "task_c1_key"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c01_h3", label = "Bookshelf", x = 0.74f, y = 0.27f, searchText = "The hollow volume was glued shut, but it gives way. Inside are folded notes in a tight, nervous hand.", clue = ClueReward("folded_note", 4, "task_c1_note"), coinReward = 0, requiresId = "c01_h1", lockedHint = "The shelf is wedged tight. Something from the desk might help."),
                SceneHotspot(id = "c01_h4", label = "Garden Window", x = 0.23f, y = 0.70f, searchText = "Muddy prints cross the sill. Someone climbed in this way, and in a hurry.", clue = ClueReward("footprint_trace", 2, "task_c1_footprint"), coinReward = 0, requiresId = "c01_h2", lockedHint = "The latch is rusted shut. The mantel may hold something to force it."),
                SceneHotspot(id = "c01_h5", label = "Locked Trunk", x = 0.24f, y = 0.30f, searchText = "The old key turns. Under the linen lie loose locket chains, each one snapped clean.", clue = ClueReward("empty_chain", 4, "task_c1_locket"), coinReward = 0, requiresId = "c01_h3", lockedHint = "The trunk is locked tight. The notes in the bookshelf may tell you where its key is."),
                SceneHotspot(id = "c01_h6", label = "Portrait of Lord Hale", x = 0.77f, y = 0.68f, searchText = "Just an old painting. Nothing behind it but dust, though the eyes seem to follow you.", clue = null, coinReward = 40, requiresId = null, lockedHint = "")
            )
        ),
        CaseScene(
            caseId = "case_02",
            title = "Clock Tower Workshop",
            intro = "The great clock stopped at 12:17 and has not moved since. The workshop above the gears is where its keeper hid his cipher.",
            imageResId = R.drawable.scene_case_02,
            hotspots = listOf(
                SceneHotspot(id = "c02_h1", label = "Gear Cabinet", x = 0.24f, y = 0.30f, searchText = "Behind rows of brass gears you find grey attic dust, swept into little piles on purpose.", clue = ClueReward("dust_sample", 2, "task_c2_dust"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c02_h2", label = "Pendulum Case", x = 0.50f, y = 0.86f, searchText = "A blue silk ribbon has been tied around the pendulum rod and cut in two. Someone wanted it to swing wrong.", clue = ClueReward("blue_ribbon", 2, "task_c2_ribbon"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c02_h3", label = "Workbench Vise", x = 0.74f, y = 0.27f, searchText = "Four old keys, all cut from the same blank, hang on a nail behind the vise.", clue = ClueReward("old_key", 4, "task_c2_key"), coinReward = 0, requiresId = "c02_h1", lockedHint = "The vise is clamped shut. The dust in the gear cabinet may show where to look."),
                SceneHotspot(id = "c02_h4", label = "Music Box Shelf", x = 0.50f, y = 0.50f, searchText = "The box lid is lined with torn sheet music. Two scraps, torn from the same score.", clue = ClueReward("torn_score", 2, "task_c2_score"), coinReward = 0, requiresId = "c02_h2", lockedHint = "The box won't open. The ribbon on the pendulum may be part of the lock."),
                SceneHotspot(id = "c02_h5", label = "Bell Chamber", x = 0.23f, y = 0.70f, searchText = "Four more torn score pieces are taped behind the great bell, hidden from anyone who didn't climb up here.", clue = ClueReward("torn_score", 4, "task_c2_watch"), coinReward = 0, requiresId = "c02_h3", lockedHint = "The bell chamber is out of reach. You need the tower key first."),
                SceneHotspot(id = "c02_h6", label = "Tower Clock Face", x = 0.77f, y = 0.68f, searchText = "Stopped at 12:17. A mystery, but only the clock's.", clue = null, coinReward = 40, requiresId = null, lockedHint = "")
            )
        ),
        CaseScene(
            caseId = "case_03",
            title = "The Rose Garden",
            intro = "Someone trampled the rose beds in the dark, and the brass token that hung on the gate is gone. The garden remembers more than it seems.",
            imageResId = R.drawable.scene_case_03,
            hotspots = listOf(
                SceneHotspot(id = "c03_h1", label = "Rose Bed", x = 0.77f, y = 0.68f, searchText = "Between the crushed stems lie two dried blooms, pressed flat under a heavy boot.", clue = ClueReward("dried_bloom", 2, "task_c3_flower"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c03_h2", label = "Garden Path", x = 0.50f, y = 0.50f, searchText = "Clear prints in the damp gravel. You find four distinct traces, all from the same boot.", clue = ClueReward("footprint_trace", 4, "task_c3_footprint"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c03_h3", label = "Gazebo", x = 0.24f, y = 0.30f, searchText = "A blue ribbon has been shredded into four pieces and stuffed between the slats.", clue = ClueReward("blue_ribbon", 4, "task_c3_ribbon"), coinReward = 0, requiresId = "c03_h1", lockedHint = "The gazebo door is chained. The roses may show who chained it."),
                SceneHotspot(id = "c03_h4", label = "Potting Shed", x = 0.74f, y = 0.27f, searchText = "Pressed flowers fill a biscuit tin on the shelf, four blooms, all carefully dried.", clue = ClueReward("dried_bloom", 4, "task_c3_token"), coinReward = 0, requiresId = "c03_h2", lockedHint = "The shed is locked, and the lock is fresh. Follow the path to see who uses it."),
                SceneHotspot(id = "c03_h5", label = "Garden Bench", x = 0.23f, y = 0.70f, searchText = "Behind the bench cushion, four torn pieces of a terrace photograph. Someone circled a face.", clue = ClueReward("torn_photo", 4, "task_c3_photo"), coinReward = 0, requiresId = "c03_h3", lockedHint = "The cushion is nailed down. The gazebo may hide what pries it free."),
                SceneHotspot(id = "c03_h6", label = "Sundial", x = 0.50f, y = 0.86f, searchText = "The sundial reads a quarter past three, which is no help. The shadow points at nothing.", clue = null, coinReward = 40, requiresId = null, lockedHint = "")
            )
        ),
        CaseScene(
            caseId = "case_04",
            title = "The Alchemist's Laboratory",
            intro = "The alchemist locked himself in this laboratory the night before the locket vanished. The furnace is cold, but the room is still full of secrets.",
            imageResId = R.drawable.scene_case_04,
            hotspots = listOf(
                SceneHotspot(id = "c04_h1", label = "Cold Furnace", x = 0.50f, y = 0.50f, searchText = "Blue wool fibres cling to the soot, four of them, from a coat that doesn't belong in a laboratory.", clue = ClueReward("dust_sample", 4, "task_c4_fiber"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c04_h2", label = "Seal Press", x = 0.77f, y = 0.68f, searchText = "Two crumbs of red wax are stuck in the press. Someone sealed a letter here in a hurry.", clue = ClueReward("wax_fragment", 2, "task_c4_wax"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c04_h3", label = "Specimen Cabinet", x = 0.74f, y = 0.27f, searchText = "Behind a jar of dried moths sit two snapped locket chains, wrapped in cloth.", clue = ClueReward("empty_chain", 2, "task_c4_chain"), coinReward = 0, requiresId = "c04_h1", lockedHint = "The cabinet is locked. The soot near the furnace may show where the key was kept."),
                SceneHotspot(id = "c04_h4", label = "Candle Table", x = 0.50f, y = 0.86f, searchText = "Four more wax fragments lie in a drip pan, the same red as the seal press.", clue = ClueReward("wax_fragment", 4, "task_c4_seal"), coinReward = 0, requiresId = "c04_h2", lockedHint = "The table is covered in a cloth that is stuck fast. The press may explain why."),
                SceneHotspot(id = "c04_h5", label = "Recipe Lectern", x = 0.23f, y = 0.70f, searchText = "A recipe book is held shut by a strap. Inside, four folded notes in a secret hand.", clue = ClueReward("folded_note", 4, "task_c4_note"), coinReward = 0, requiresId = "c04_h3", lockedHint = "The strap needs a blade or a key. The chains in the cabinet may help."),
                SceneHotspot(id = "c04_h6", label = "Bubbling Flask", x = 0.24f, y = 0.30f, searchText = "Green liquid bubbles away over a tiny flame. Fascinating, but not evidence.", clue = null, coinReward = 40, requiresId = null, lockedHint = "")
            )
        ),
        CaseScene(
            caseId = "case_05",
            title = "The Masquerade Ballroom",
            intro = "At midnight, the masks came off, and the heirloom locket was gone. The ballroom is still dressed for the party, and the guests are long gone.",
            imageResId = R.drawable.scene_case_05,
            hotspots = listOf(
                SceneHotspot(id = "c05_h1", label = "Portrait Gallery", x = 0.74f, y = 0.27f, searchText = "A portrait has been slashed and its photograph torn out in two pieces. Someone knew who they were looking for.", clue = ClueReward("torn_photo", 2, "task_c5_photo"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c05_h2", label = "Coat Check Desk", x = 0.24f, y = 0.30f, searchText = "Four brass keys, left in four coat pockets. All cut from the same blank.", clue = ClueReward("old_key", 4, "task_c5_key"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c05_h3", label = "Orchestra Pit", x = 0.50f, y = 0.50f, searchText = "Torn sheet music litters the floor, four pieces from the midnight waltz.", clue = ClueReward("torn_score", 4, "task_c5_score"), coinReward = 0, requiresId = "c05_h1", lockedHint = "The pit is roped off. The gallery portrait may show who ordered that."),
                SceneHotspot(id = "c05_h4", label = "Flower Arrangements", x = 0.77f, y = 0.68f, searchText = "Four dried blooms, hidden inside the vases, have been pressed flat by a heavy hand.", clue = ClueReward("dried_bloom", 4, "task_c5_token"), coinReward = 0, requiresId = "c05_h2", lockedHint = "The arrangements are wired to the wall. The coat-check keys may free them."),
                SceneHotspot(id = "c05_h5", label = "Masked Statue", x = 0.23f, y = 0.70f, searchText = "Behind the statue's mask, four locket chains have been looped over the ears.", clue = ClueReward("empty_chain", 4, "task_c5_locket"), coinReward = 0, requiresId = "c05_h3", lockedHint = "The mask won't lift. The orchestra pit may hold the signal."),
                SceneHotspot(id = "c05_h6", label = "Champagne Table", x = 0.50f, y = 0.86f, searchText = "Half-empty glasses and a lipstick-stained napkin. A party, nothing more.", clue = null, coinReward = 40, requiresId = null, lockedHint = "")
            )
        ),
        CaseScene(
            caseId = "case_06",
            title = "The Silent Conservatory",
            intro = "One pane is shattered from the inside, and the orchids are gone. Whoever did this took great care to hide what they carried out.",
            imageResId = R.drawable.scene_case_06,
            hotspots = listOf(
                SceneHotspot(id = "c06_h1", label = "Orchid Table", x = 0.50f, y = 0.86f, searchText = "Two pressed orchid blooms lie beneath the empty pot, crushed flat and tied with thread.", clue = ClueReward("dried_bloom", 2, "task_c6_bloom"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c06_h2", label = "Broken Pane", x = 0.24f, y = 0.30f, searchText = "A blue ribbon is caught on the glass, snagged in two pieces by someone leaving fast.", clue = ClueReward("blue_ribbon", 2, "task_c6_ribbon"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c06_h3", label = "Potting Shed Door", x = 0.74f, y = 0.27f, searchText = "A key hangs on a hook behind the door, two in fact, one older than the other.", clue = ClueReward("old_key", 2, "task_c6_key"), coinReward = 0, requiresId = "c06_h1", lockedHint = "The shed door is locked. The orchid table may hold the answer."),
                SceneHotspot(id = "c06_h4", label = "Dusty Rafters", x = 0.77f, y = 0.68f, searchText = "You climb the ladder. Four heaps of attic dust are swept into the corners of the rafters.", clue = ClueReward("dust_sample", 4, "task_c6_fiber"), coinReward = 0, requiresId = "c06_h2", lockedHint = "The ladder is missing a rung. The pane may explain who used it."),
                SceneHotspot(id = "c06_h5", label = "Gardener's Coat", x = 0.23f, y = 0.70f, searchText = "Four blue ribbon scraps are sewn into the lining of the old coat. Someone was hiding something here.", clue = ClueReward("blue_ribbon", 4, "task_c6_stitch"), coinReward = 0, requiresId = "c06_h3", lockedHint = "The coat is locked in a chest. You'll need the shed key."),
                SceneHotspot(id = "c06_h6", label = "Watering Can", x = 0.50f, y = 0.50f, searchText = "Still half full. The gardener was careful, nothing more.", clue = null, coinReward = 40, requiresId = null, lockedHint = "")
            )
        ),
        CaseScene(
            caseId = "case_07",
            title = "The Library",
            intro = "The sealed letter was found half burned in the library fireplace. Whoever wrote it wanted the truth to survive. Someone else wanted it gone.",
            imageResId = R.drawable.scene_case_07,
            hotspots = listOf(
                SceneHotspot(id = "c07_h1", label = "Fireplace Grate", x = 0.77f, y = 0.68f, searchText = "Two scorched notes survive in the ashes, curled and brittle, but the ink is still legible.", clue = ClueReward("folded_note", 2, "task_c7_note"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c07_h2", label = "Letter Desk", x = 0.50f, y = 0.86f, searchText = "The seal stick is still warm in its holder, and two crumbs of wax have been scraped from the blotter.", clue = ClueReward("wax_fragment", 2, "task_c7_wax"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c07_h3", label = "Photo Album", x = 0.74f, y = 0.27f, searchText = "The album has a page missing, and two torn photo pieces are tucked in the spine.", clue = ClueReward("torn_photo", 2, "task_c7_photo"), coinReward = 0, requiresId = "c07_h1", lockedHint = "The album is locked in a drawer. The burned notes may tell you which one."),
                SceneHotspot(id = "c07_h4", label = "Candle Stand", x = 0.23f, y = 0.70f, searchText = "Four wax fragments sit in the drip tray, the same crimson as the seal on the letter.", clue = ClueReward("wax_fragment", 4, "task_c7_seal"), coinReward = 0, requiresId = "c07_h2", lockedHint = "The stand is bolted to the floor. The wax on the desk may show how it opens."),
                SceneHotspot(id = "c07_h5", label = "Locked Bureau", x = 0.24f, y = 0.30f, searchText = "Four folded notes, written in the same hand as the burned letter, were hidden under the false bottom.", clue = ClueReward("folded_note", 4, "task_c7_decode"), coinReward = 0, requiresId = "c07_h3", lockedHint = "The bureau is locked. The album may hold its key."),
                SceneHotspot(id = "c07_h6", label = "Antique Globe", x = 0.50f, y = 0.50f, searchText = "It spins freely, and every country is exactly where it should be.", clue = null, coinReward = 40, requiresId = null, lockedHint = "")
            )
        ),
        CaseScene(
            caseId = "case_08",
            title = "Backstage at the Recital Hall",
            intro = "Celeste Hale's violin fell silent in the middle of her own recital. Her dressing room backstage is the last place anyone saw her.",
            imageResId = R.drawable.scene_case_08,
            hotspots = listOf(
                SceneHotspot(id = "c08_h1", label = "Music Stand", x = 0.50f, y = 0.86f, searchText = "The score on the stand is torn in half, two pieces missing a bar that was never written.", clue = ClueReward("torn_score", 2, "task_c8_score"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c08_h2", label = "Violin Case", x = 0.50f, y = 0.50f, searchText = "Grey rosin dust is mixed with attic grit under the lining, two small heaps.", clue = ClueReward("dust_sample", 2, "task_c8_dust"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c08_h3", label = "Dressing Table", x = 0.77f, y = 0.68f, searchText = "In the drawer, two snapped chains were wound round a pencil.", clue = ClueReward("empty_chain", 2, "task_c8_clasp"), coinReward = 0, requiresId = "c08_h1", lockedHint = "The drawer is jammed. The music stand may hold a clue."),
                SceneHotspot(id = "c08_h4", label = "Piano Bench", x = 0.23f, y = 0.70f, searchText = "Under the lid, four torn score pieces form the missing bars.", clue = ClueReward("torn_score", 4, "task_c8_melody"), coinReward = 0, requiresId = "c08_h2", lockedHint = "The bench is locked. Look at the rosin in the case."),
                SceneHotspot(id = "c08_h5", label = "Programme Wall", x = 0.24f, y = 0.30f, searchText = "Four torn pieces of the recital photograph are hidden behind the framed programmes.", clue = ClueReward("torn_photo", 4, "task_c8_photo"), coinReward = 0, requiresId = "c08_h3", lockedHint = "The frames are screwed to the wall. The dressing table may hold a tool."),
                SceneHotspot(id = "c08_h6", label = "Stage Curtain", x = 0.74f, y = 0.27f, searchText = "Heavy velvet, and nothing behind it but the dark.", clue = null, coinReward = 40, requiresId = null, lockedHint = "")
            )
        ),
        CaseScene(
            caseId = "case_09",
            title = "The Chapel Cellar",
            intro = "Behind the chapel altar, a cellar door has been forced from below. Every trail you have followed so far leads down these steps.",
            imageResId = R.drawable.scene_case_09,
            hotspots = listOf(
                SceneHotspot(id = "c09_h1", label = "Cellar Door Lock", x = 0.77f, y = 0.68f, searchText = "Two old keys hang from a nail inside the doorframe. One has been oiled recently.", clue = ClueReward("old_key", 2, "task_c9_key"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c09_h2", label = "Cellar Stairs", x = 0.74f, y = 0.27f, searchText = "Two fresh prints in the stair dust, a size smaller than the groundskeeper's boots.", clue = ClueReward("footprint_trace", 2, "task_c9_print"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c09_h3", label = "Candle Niche", x = 0.50f, y = 0.86f, searchText = "Two lumps of wax have melted into the stone, the same red as the letter seal.", clue = ClueReward("wax_fragment", 2, "task_c9_wax"), coinReward = 0, requiresId = "c09_h1", lockedHint = "The niche is bricked up. The cellar door may open a way in."),
                SceneHotspot(id = "c09_h4", label = "Wine Rack", x = 0.23f, y = 0.70f, searchText = "Four old keys lie behind the dusty bottles, cut from the same blank as the cellar door key.", clue = ClueReward("old_key", 4, "task_c9_engrave"), coinReward = 0, requiresId = "c09_h2", lockedHint = "The rack is heavy. The prints on the stairs show where it was moved."),
                SceneHotspot(id = "c09_h5", label = "Hidden Passage", x = 0.50f, y = 0.50f, searchText = "A stone panel slides aside and reveals footprints in the dust, four clear ones, leading toward the chapel.", clue = ClueReward("footprint_trace", 4, "task_c9_footprint"), coinReward = 0, requiresId = "c09_h3", lockedHint = "There is no passage to be seen. The candle niche may be a lever."),
                SceneHotspot(id = "c09_h6", label = "Old Barrel", x = 0.24f, y = 0.30f, searchText = "Empty, except for a sour smell and a spider.", clue = null, coinReward = 40, requiresId = null, lockedHint = "")
            )
        ),
        CaseScene(
            caseId = "case_10",
            title = "The Great Hall",
            intro = "The last heir has come home, and the final pieces of the Hale mystery lie in this hall. Assemble the complete evidence and close the book for good.",
            imageResId = R.drawable.scene_case_10,
            hotspots = listOf(
                SceneHotspot(id = "c10_h1", label = "Family Crest Wall", x = 0.74f, y = 0.27f, searchText = "Four wax fragments are lodged in the carved crest, pressed there by a ring.", clue = ClueReward("wax_fragment", 4, "task_c10_seal"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c10_h2", label = "Heir's Trunk", x = 0.77f, y = 0.68f, searchText = "Four pressed blooms lie in an old trunk, each one tied with a letter, each from a different case.", clue = ClueReward("dried_bloom", 4, "task_c10_token"), coinReward = 0, requiresId = null, lockedHint = ""),
                SceneHotspot(id = "c10_h3", label = "Attic Hatch", x = 0.50f, y = 0.86f, searchText = "Dust falls as you lift the hatch. Four little heaps sit swept into the corner, blue fibres in each.", clue = ClueReward("dust_sample", 4, "task_c10_fiber"), coinReward = 0, requiresId = "c10_h1", lockedHint = "The hatch is bolted from above. The crest on the wall may be the signal."),
                SceneHotspot(id = "c10_h4", label = "Grand Piano", x = 0.23f, y = 0.70f, searchText = "Four torn pieces of the midnight score lie under the lid, in the right order.", clue = ClueReward("torn_score", 4, "task_c10_melody"), coinReward = 0, requiresId = "c10_h2", lockedHint = "The lid is locked. The trunk may hold the key."),
                SceneHotspot(id = "c10_h5", label = "Grand Staircase", x = 0.24f, y = 0.30f, searchText = "Behind the newel post, four locket chains. The last pieces of the mystery.", clue = ClueReward("empty_chain", 4, "task_c10_locket"), coinReward = 0, requiresId = "c10_h3", lockedHint = "The post is sealed. The hatch may open the way."),
                SceneHotspot(id = "c10_h6", label = "Suit of Armour", x = 0.50f, y = 0.50f, searchText = "Empty. It has stood here for two hundred years and has never once been suspicious.", clue = null, coinReward = 40, requiresId = null, lockedHint = "")
            )
        )
    )

    val sceneByCase: Map<String, CaseScene> = scenes.associateBy { it.caseId }

    val hotspotMap: Map<String, SceneHotspot> = scenes.flatMap { it.hotspots }.associateBy { it.id }
}
