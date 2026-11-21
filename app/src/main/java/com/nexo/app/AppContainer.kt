package com.nexo.app

import android.content.Context
import com.nexo.app.core.security.CredentialStore
import com.nexo.app.core.security.KeystoreCredentialStore
import com.nexo.app.core.storage.AppPreferences
import com.nexo.app.engine.ai.UrlConnectionTransport
import com.nexo.app.engine.ai.gemini.GeminiProvider
import com.nexo.app.engine.avatar.AvatarRepository

/** Inyección manual de dependencias (se reemplazará por Hilt si hace falta). */
class AppContainer(context: Context) {
    val credentials: CredentialStore = KeystoreCredentialStore(context)
    val preferences = AppPreferences(context)
    val gemini = GeminiProvider(credentials, UrlConnectionTransport()) { preferences.geminiModel }
    val avatars = AvatarRepository(preferences)
}
