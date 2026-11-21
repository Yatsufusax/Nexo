package com.nexo.app.core.security

interface CredentialStore {
    fun save(name: String, secret: String)
    fun load(name: String): String?
    fun delete(name: String)
}
