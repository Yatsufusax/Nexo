package com.nexo.app.engine.avatar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface AvatarSelectionStore {
    var selectedAvatarId: String?
}

class AvatarRepository(private val store: AvatarSelectionStore) {
    private val _selected = MutableStateFlow(AvatarCatalog.byId(store.selectedAvatarId))
    val selected: StateFlow<AvatarDefinition> = _selected.asStateFlow()

    /** Ignora ids que no existen en el catálogo. */
    fun select(id: String) {
        val avatar = AvatarCatalog.find(id) ?: return
        store.selectedAvatarId = avatar.id
        _selected.value = avatar
    }
}
