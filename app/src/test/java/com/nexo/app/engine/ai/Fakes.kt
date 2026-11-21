package com.nexo.app.engine.ai

import com.nexo.app.core.security.CredentialStore
import java.io.IOException

class FakeCredentialStore(initial: Map<String, String> = emptyMap()) : CredentialStore {
    private val map = initial.toMutableMap()
    override fun save(name: String, secret: String) { map[name] = secret }
    override fun load(name: String): String? = map[name]
    override fun delete(name: String) { map.remove(name) }
}

/** Cada elemento de la cola es un HttpResponse o una IOException que se lanza. */
class FakeTransport(vararg items: Any) : HttpTransport {
    data class Req(
        val method: String,
        val url: String,
        val headers: Map<String, String>,
        val body: String?,
    )

    private val queue = ArrayDeque(items.toList())
    val requests = mutableListOf<Req>()

    override suspend fun execute(
        method: String,
        url: String,
        headers: Map<String, String>,
        body: String?,
    ): HttpResponse {
        requests += Req(method, url, headers, body)
        val next = queue.removeFirst()
        if (next is IOException) throw next
        return next as HttpResponse
    }
}
