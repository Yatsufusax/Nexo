package com.nexo.app.engine.avatar

import org.junit.Assert.assertEquals
import org.junit.Test

class AvatarRepositoryTest {
    private class FakeStore(override var selectedAvatarId: String? = null) : AvatarSelectionStore

    @Test
    fun startsWithDefaultWhenNothingStored() {
        assertEquals(AvatarCatalog.default, AvatarRepository(FakeStore()).selected.value)
    }

    @Test
    fun restoresStoredSelection() {
        assertEquals("luna", AvatarRepository(FakeStore("luna")).selected.value.id)
    }

    @Test
    fun selectPersistsAndUpdates() {
        val store = FakeStore()
        val repo = AvatarRepository(store)
        repo.select("sakura")
        assertEquals("sakura", repo.selected.value.id)
        assertEquals("sakura", store.selectedAvatarId)
    }

    @Test
    fun invalidIdIsIgnored() {
        val store = FakeStore("aqua")
        val repo = AvatarRepository(store)
        repo.select("fantasma")
        assertEquals("aqua", repo.selected.value.id)
        assertEquals("aqua", store.selectedAvatarId)
    }
}
