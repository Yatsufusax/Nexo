package com.nexo.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nexo.app.ui.avatar.AvatarViewModel
import com.nexo.app.ui.chat.ChatViewModel
import com.nexo.app.ui.provider.ProviderViewModel
import com.nexo.app.ui.settings.SettingsViewModel

class NexoViewModelFactory(private val c: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ProviderViewModel::class.java) ->
            ProviderViewModel(c.gemini, c.credentials, c.preferences) as T
        modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
            SettingsViewModel(c.gemini) as T
        modelClass.isAssignableFrom(ChatViewModel::class.java) ->
            ChatViewModel(c.gemini) as T
        modelClass.isAssignableFrom(AvatarViewModel::class.java) ->
            AvatarViewModel(c.avatars) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
