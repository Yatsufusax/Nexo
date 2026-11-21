package com.nexo.app.ui.navigation

object Routes {
    const val HOME = "home"
    const val CHARACTERS = "characters"
    const val SETTINGS = "settings"
    const val PROVIDER = "provider"
    const val CHAT = "chat"
    const val ABOUT = "about"

    /** Pantallas con barra inferior. */
    val tabs: List<String> = listOf(HOME, CHARACTERS, SETTINGS)

    val all: List<String> = tabs + listOf(PROVIDER, CHAT, ABOUT)
}
