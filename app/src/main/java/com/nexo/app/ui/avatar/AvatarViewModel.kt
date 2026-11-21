package com.nexo.app.ui.avatar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nexo.app.engine.avatar.AvatarRepository

class AvatarViewModel(private val repo: AvatarRepository) : ViewModel() {
    val selected = repo.selected

    /** Selección en pantalla, aún sin aplicar. */
    var pendingId by mutableStateOf(repo.selected.value.id)

    fun apply() = repo.select(pendingId)
}
