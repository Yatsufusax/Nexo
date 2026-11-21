package com.nexo.app.engine.ai

import com.nexo.app.engine.ai.gemini.GeminiProvider
import java.io.IOException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GeminiProviderTest {

    private fun provider(transport: HttpTransport, withKey: Boolean = true) = GeminiProvider(
        FakeCredentialStore(if (withKey) mapOf(GeminiProvider.KEY_NAME to "KEY123") else emptyMap()),
        transport,
    ) { "gemini-test" }

    private val ok = HttpResponse(200, "{}")

    @Test
    fun withoutKeyStatusIsNotConnectedAndNoRequestIsMade() = runBlocking {
        val t = FakeTransport()
        val p = provider(t, withKey = false)
        p.refreshStatus()
        assertEquals(ConnectionStatus.NotConnected, p.status.value)
        assertTrue(t.requests.isEmpty())
    }

    @Test
    fun storedKeyStartsAsAuthPendingNotConnected() {
        assertEquals(ConnectionStatus.AuthPending, provider(FakeTransport()).status.value)
    }

    @Test
    fun successfulVerificationMeansApiConnected() = runBlocking {
        val p = provider(FakeTransport(ok))
        p.refreshStatus()
        assertTrue(p.status.value is ConnectionStatus.ApiConnected)
    }

    @Test
    fun invalidKeyIsReportedAsError() = runBlocking {
        val body = """{"error":{"code":400,"message":"API key not valid","details":[{"reason":"API_KEY_INVALID"}]}}"""
        val p = provider(FakeTransport(HttpResponse(400, body)))
        p.refreshStatus()
        val s = p.status.value as ConnectionStatus.Error
        assertEquals(ErrorKind.INVALID_CREDENTIALS, s.kind)
    }

    @Test
    fun unknownModelIsReported() = runBlocking {
        val p = provider(FakeTransport(HttpResponse(404, "{}")))
        p.refreshStatus()
        assertEquals(ErrorKind.MODEL_NOT_FOUND, (p.status.value as ConnectionStatus.Error).kind)
    }

    @Test
    fun networkFailureIsReported() = runBlocking {
        val p = provider(FakeTransport(IOException("sin red")))
        p.refreshStatus()
        assertEquals(ErrorKind.NETWORK, (p.status.value as ConnectionStatus.Error).kind)
    }

    @Test
    fun chatSendsKeyInHeaderNotUrlAndReturnsText() = runBlocking {
        val reply = """{"candidates":[{"content":{"parts":[{"text":"hola!"}]}}]}"""
        val t = FakeTransport(HttpResponse(200, reply))
        val p = provider(t)
        val r = p.chat(listOf(ChatMessage(Role.USER, "hi")), null)
        assertEquals("hola!", r.getOrNull())
        val req = t.requests.single()
        assertEquals("KEY123", req.headers["x-goog-api-key"])
        assertFalse(req.url.contains("KEY123"))
        assertTrue(req.url.endsWith("models/gemini-test:generateContent"))
        assertTrue(p.status.value is ConnectionStatus.ApiConnected)
    }

    @Test
    fun rateLimitIsReported() = runBlocking {
        val p = provider(FakeTransport(HttpResponse(429, "{}")))
        val r = p.chat(listOf(ChatMessage(Role.USER, "hi")), null)
        assertEquals(ErrorKind.RATE_LIMITED, (r.exceptionOrNull() as AiException).kind)
        assertEquals(ErrorKind.RATE_LIMITED, (p.status.value as ConnectionStatus.Error).kind)
    }

    @Test
    fun chatWithoutKeyFailsWithoutRequest() = runBlocking {
        val t = FakeTransport()
        val r = provider(t, withKey = false).chat(listOf(ChatMessage(Role.USER, "hi")), null)
        assertEquals(ErrorKind.NOT_CONFIGURED, (r.exceptionOrNull() as AiException).kind)
        assertTrue(t.requests.isEmpty())
    }
}
