package com.nexo.app.core.storage

import android.content.Context
import com.nexo.app.engine.avatar.AvatarSelectionStore
import com.nexo.app.engine.ai.gemini.GeminiProvider

class AppPreferences(context: Context) : AvatarSelectionStore {
    private val prefs = context.getSharedPreferences("nexo_prefs", Context.MODE_PRIVATE)

    var geminiModel: String
        get() = prefs.getString("gemini_model", null)?.takeIf { it.isNotBlank() }
            ?: GeminiProvider.DEFAULT_MODEL
        set(value) {
            prefs.edit().putString("gemini_model", value).apply()
        }

    override var selectedAvatarId: String?
        get() = prefs.getString("avatar_id", null)
        set(value) {
            prefs.edit().putString("avatar_id", value).apply()
        }
}
