package com.nexo.app.engine.ai.gemini

import com.nexo.app.core.security.CredentialStore
import com.nexo.app.engine.ai.AiException
import com.nexo.app.engine.ai.AiProvider
import com.nexo.app.engine.ai.AuthMethod
import com.nexo.app.engine.ai.ChatMessage
import com.nexo.app.engine.ai.ConnectionStatus
import com.nexo.app.engine.ai.ErrorKind
import com.nexo.app.engine.ai.HttpResponse
import com.nexo.app.engine.ai.HttpTransport
import com.nexo.app.engine.ai.ProviderId
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GeminiProvider(
    private val credentials: CredentialStore,
    private val http: HttpTransport,
    private val modelName: () -> String,
) : AiProvider {

    override val id = ProviderId.GEMINI
    override val displayName = "Google Gemini"
    override val authMethods = setOf(AuthMethod.API_KEY)

    private val _status = MutableStateFlow<ConnectionStatus>(initialStatus())
    override val status: StateFlow<ConnectionStatus> = _status.asStateFlow()

    private fun initialStatus(): ConnectionStatus =
        if (apiKey() == null) ConnectionStatus.NotConnected else ConnectionStatus.AuthPending

    private fun apiKey(): String? = credentials.load(KEY_NAME)?.takeIf { it.isNotBlank() }

    private fun headers(key: String) = mapOf(
        "x-goog-api-key" to key,
        "Content-Type" to "application/json",
    )

    /** Verifica de verdad la clave y el modelo con una llamada a Google. */
    override suspend fun refreshStatus() {
        val key = apiKey()
        if (key == null) {
            _status.value = ConnectionStatus.NotConnected
            return
        }
        _status.value = ConnectionStatus.AuthPending
        val model = modelName()
        _status.value = try {
            val r = http.execute("GET", "$BASE_URL/models/$model", headers(key), null)
            if (r.code in 200..299) {
                ConnectionStatus.ApiConnected("Modelo: $model")
            } else {
                val e = toException(r)
                ConnectionStatus.Error(e.kind, e.message.orEmpty())
            }
        } catch (e: IOException) {
            ConnectionStatus.Error(ErrorKind.NETWORK, "No se pudo conectar con Google.")
        }
    }

    override suspend fun chat(messages: List<ChatMessage>, systemPrompt: String?): Result<String> {
        val key = apiKey() ?: return Result.failure(
            AiException(ErrorKind.NOT_CONFIGURED, "Falta la clave de API de Gemini."),
        )
        val model = modelName()
        return try {
            val r = http.execute(
                "POST",
                "$BASE_URL/models/$model:generateContent",
                headers(key),
                GeminiJson.chatRequest(messages, systemPrompt),
            )
            if (r.code in 200..299) {
                val text = GeminiJson.parseReply(r.body)
                if (text == null) {
                    Result.failure(
                        AiException(ErrorKind.UNKNOWN, "Gemini devolvió una respuesta vacía o bloqueada."),
                    )
                } else {
                    _status.value = ConnectionStatus.ApiConnected("Modelo: $model")
                    Result.success(text)
                }
            } else {
                failWith(toException(r))
            }
        } catch (e: IOException) {
            failWith(AiException(ErrorKind.NETWORK, "No se pudo conectar con Google. Revisa tu internet."))
        }
    }

    private fun failWith(e: AiException): Result<String> {
        _status.value = ConnectionStatus.Error(e.kind, e.message.orEmpty())
        return Result.failure(e)
    }

    private fun toException(r: HttpResponse): AiException {
        val apiMsg = GeminiJson.parseErrorMessage(r.body)
        return when {
            r.code == 429 -> AiException(
                ErrorKind.RATE_LIMITED,
                "Alcanzaste el límite de uso de Gemini. Espera un poco e inténtalo de nuevo.",
            )
            r.code == 404 -> AiException(
                ErrorKind.MODEL_NOT_FOUND,
                "El modelo \"${modelName()}\" no existe o no está disponible. Cámbialo en Configuración.",
            )
            r.code == 401 || r.code == 403 -> AiException(
                ErrorKind.INVALID_CREDENTIALS,
                "Google rechazó la clave o no tiene permiso (${r.code}).",
            )
            r.code == 400 && r.body.contains("API_KEY_INVALID") -> AiException(
                ErrorKind.INVALID_CREDENTIALS,
                "La clave de API no es válida.",
            )
            r.code >= 500 -> AiException(
                ErrorKind.SERVER,
                "Gemini tiene un problema temporal (${r.code}).",
            )
            else -> AiException(ErrorKind.UNKNOWN, "Error ${r.code}: ${apiMsg ?: "sin detalle"}")
        }
    }

    companion object {
        const val KEY_NAME = "gemini_api_key"
        const val DEFAULT_MODEL = "gemini-2.5-flash"
        const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta"
    }
}
