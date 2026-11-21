package com.nexo.app.ui.provider

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexo.app.core.security.CredentialStore
import com.nexo.app.core.storage.AppPreferences
import com.nexo.app.engine.ai.ConnectionStatus
import com.nexo.app.engine.ai.gemini.GeminiProvider
import kotlinx.coroutines.launch

class ProviderViewModel(
    private val provider: GeminiProvider,
    private val credentials: CredentialStore,
    private val prefs: AppPreferences,
) : ViewModel() {

    val status = provider.status

    var keyInput by mutableStateOf("")
    var modelInput by mutableStateOf(prefs.geminiModel)
    var hasStoredKey by mutableStateOf(credentials.load(GeminiProvider.KEY_NAME) != null)
        private set

    init {
        if (status.value is ConnectionStatus.AuthPending) {
            viewModelScope.launch { provider.refreshStatus() }
        }
    }

    fun saveAndVerify() {
        val key = keyInput.trim()
        if (key.isNotEmpty()) {
            credentials.save(GeminiProvider.KEY_NAME, key)
            keyInput = ""
            hasStoredKey = true
        }
        prefs.geminiModel = modelInput.trim().ifEmpty { GeminiProvider.DEFAULT_MODEL }
        viewModelScope.launch { provider.refreshStatus() }
    }

    fun disconnect() {
        credentials.delete(GeminiProvider.KEY_NAME)
        hasStoredKey = false
        viewModelScope.launch { provider.refreshStatus() }
    }
}
