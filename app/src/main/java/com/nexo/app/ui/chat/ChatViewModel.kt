package com.nexo.app.ui.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexo.app.engine.ai.AiProvider
import com.nexo.app.engine.ai.ChatMessage
import com.nexo.app.engine.ai.Role
import kotlinx.coroutines.launch

class ChatViewModel(private val provider: AiProvider) : ViewModel() {

    val status = provider.status
    val messages = mutableStateListOf<ChatMessage>()
    var sending by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun send(text: String) {
        val t = text.trim()
        if (t.isEmpty() || sending) return
        messages.add(ChatMessage(Role.USER, t))
        sending = true
        error = null
        viewModelScope.launch {
            provider.chat(messages.toList(), SYSTEM_PROMPT)
                .onSuccess { messages.add(ChatMessage(Role.MODEL, it)) }
                .onFailure { error = it.message ?: "Error desconocido" }
            sending = false
        }
    }

    private companion object {
        // Provisional: la personalidad real vendrá del Personality Engine.
        const val SYSTEM_PROMPT =
            "Eres NEXO, un compañero virtual amable y breve. Responde en el idioma del usuario."
    }
}
