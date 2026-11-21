package com.nexo.app.engine.ai.gemini

import com.nexo.app.engine.ai.ChatMessage
import com.nexo.app.engine.ai.Role
import org.json.JSONArray
import org.json.JSONObject

object GeminiJson {
    fun chatRequest(messages: List<ChatMessage>, systemPrompt: String?): String {
        val root = JSONObject()
        if (!systemPrompt.isNullOrBlank()) {
            root.put(
                "systemInstruction",
                JSONObject().put("parts", JSONArray().put(JSONObject().put("text", systemPrompt))),
            )
        }
        val contents = JSONArray()
        messages.forEach { m ->
            contents.put(
                JSONObject()
                    .put("role", if (m.role == Role.USER) "user" else "model")
                    .put("parts", JSONArray().put(JSONObject().put("text", m.text))),
            )
        }
        root.put("contents", contents)
        return root.toString()
    }

    /** Devuelve el texto de la primera respuesta, o null si está vacía o bloqueada. */
    fun parseReply(body: String): String? = runCatching {
        val parts = JSONObject(body)
            .optJSONArray("candidates")
            ?.optJSONObject(0)
            ?.optJSONObject("content")
            ?.optJSONArray("parts")
            ?: return@runCatching null
        val sb = StringBuilder()
        for (i in 0 until parts.length()) {
            sb.append(parts.optJSONObject(i)?.optString("text").orEmpty())
        }
        sb.toString().ifBlank { null }
    }.getOrNull()

    fun parseErrorMessage(body: String): String? = runCatching {
        JSONObject(body).getJSONObject("error").getString("message")
    }.getOrNull()
}
