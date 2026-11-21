package com.nexo.app.engine.avatar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AvatarCatalogTest {
    @Test
    fun idsAreUniqueAndNamesNotBlank() {
        assertEquals(AvatarCatalog.all.size, AvatarCatalog.all.map { it.id }.toSet().size)
        assertTrue(AvatarCatalog.all.all { it.displayName.isNotBlank() })
    }

    @Test
    fun unknownOrNullFallsBackToDefault() {
        assertEquals(AvatarCatalog.default, AvatarCatalog.byId(null))
        assertEquals(AvatarCatalog.default, AvatarCatalog.byId("no-existe"))
        assertNull(AvatarCatalog.find("no-existe"))
    }

    @Test
    fun allEightStatesExist() {
        assertEquals(
            listOf("Idle", "Walk", "Sit", "Sleep", "Greet", "Talk", "Think", "Happy"),
            AvatarState.entries.map { it.name },
        )
    }
}
