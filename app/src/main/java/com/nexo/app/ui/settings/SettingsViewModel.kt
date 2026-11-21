package com.nexo.app.ui.settings

import androidx.lifecycle.ViewModel
import com.nexo.app.engine.ai.AiProvider

class SettingsViewModel(provider: AiProvider) : ViewModel() {
    val providerName = provider.displayName
    val status = provider.status
}
