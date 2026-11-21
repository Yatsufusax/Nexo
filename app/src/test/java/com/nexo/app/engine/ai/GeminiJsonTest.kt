package com.nexo.app.engine.ai

import com.nexo.app.engine.ai.gemini.GeminiJson
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GeminiJsonTest {
    @Test
    fun requestMapsRolesAndSystemPrompt() {
        val json = JSONObject(
            GeminiJson.chatRequest(
                listOf(ChatMessage(Role.USER, "hola"), ChatMessage(Role.MODEL, "buenas")),
                "sé breve",
            ),
        )
        val contents = json.getJSONArray("contents")
        assertEquals("user", contents.getJSONObject(0).getString("role"))
        assertEquals("model", contents.getJSONObject(1).getString("role"))
        assertEquals(
            "sé breve",
            json.getJSONObject("systemInstruction").getJSONArray("parts").getJSONObject(0).getString("text"),
        )
    }

    @Test
    fun parsesReplyText() {
        val body = """{"candidates":[{"content":{"parts":[{"text":"Hola"},{"text":" mundo"}]}}]}"""
        assertEquals("Hola mundo", GeminiJson.parseReply(body))
    }

    @Test
    fun blockedOrEmptyReplyIsNull() {
        assertNull(GeminiJson.parseReply("""{"promptFeedback":{"blockReason":"SAFETY"}}"""))
        assertNull(GeminiJson.parseReply("no es json"))
    }

    @Test
    fun parsesErrorMessage() {
        assertEquals("boom", GeminiJson.parseErrorMessage("""{"error":{"code":400,"message":"boom"}}"""))
    }
}
