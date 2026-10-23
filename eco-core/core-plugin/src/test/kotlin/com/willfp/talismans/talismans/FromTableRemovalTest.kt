package com.willfp.talismans.talismans

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import java.io.File

class FromTableRemovalTest {
    @Test
    fun `fromtable removed by 2026_45`() {
        val removalDue = System.getProperty("talismans.version")
            .split(".")
            .map { it.substringBefore("-").toIntOrNull() ?: 0 }
            .let { (year, release) -> year > 2026 || year == 2026 && release >= 45 }

        assumeTrue(removalDue)

        assertFalse(
            File("src/main/kotlin/com/willfp/talismans/talismans/Talismans.kt").readText()
                .contains("fromTablePermission"),
            "The talismans.fromtable.<id> fallback is due for removal in 2026.45: " +
                "remove Talismans.fromTablePermission and opt-in.disable-fromtable-permission."
        )
    }
}
