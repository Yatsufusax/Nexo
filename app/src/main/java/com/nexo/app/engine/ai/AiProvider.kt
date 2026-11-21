package com.nexo.app.engine.ai

import kotlinx.coroutines.flow.StateFlow

enum class ProviderId { GEMINI, OPENAI, ANTHROPIC }

enum class AuthMethod { API_KEY, ACCOUNT_OAUTH }

enum class Role { USER, MODEL }

data class ChatMessage(val role: Role, val text: String)

enum class ErrorKind {
    NOT_CONFIGURED,
    INVALID_CREDENTIALS,
    RATE_LIMITED,
    MODEL_NOT_FOUND,
    NETWORK,
    SERVER,
    UNKNOWN,
}

class AiException(val kind: ErrorKind, message: String) : Exception(message)

/** Estados de conexión. Solo ApiConnected/AccountConnected tras una verificación real. */
sealed interface ConnectionStatus {
    data object NotConnected : ConnectionStatus
    data object AuthPending : ConnectionStatus
    data class AccountConnected(val accountLabel: String?) : ConnectionStatus
    data class ApiConnected(val detail: String?) : ConnectionStatus
    data class Error(val kind: ErrorKind, val message: String) : ConnectionStatus
    data class Unavailable(val reason: String) : ConnectionStatus
}

/** Contrato que los motores usan. Ningún motor conoce a un proveedor concreto. */
interface AiProvider {
    val id: ProviderId
    val displayName: String
    val authMethods: Set<AuthMethod>
    val status: StateFlow<ConnectionStatus>

    suspend fun refreshStatus()

    suspend fun chat(messages: List<ChatMessage>, systemPrompt: String?): Result<String>
}
