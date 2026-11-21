package com.nexo.app.engine.avatar

/** Catálogo provisional: variantes de color de un mismo modelo vectorial. */
object AvatarCatalog {
    val all: List<AvatarDefinition> = listOf(
        AvatarDefinition("nexo", "Nexo", AvatarPalette(0xFF2B1F4F, 0xFF7C5CFF, 0xFF2A2250, 0xFF5B3FD6)),
        AvatarDefinition("luna", "Luna", AvatarPalette(0xFFE8E6F5, 0xFFB9A8FF, 0xFF3A3F66, 0xFF6C8CFF)),
        AvatarDefinition("sakura", "Sakura", AvatarPalette(0xFFE86FA5, 0xFFFF9EC7, 0xFF4A2A4F, 0xFFD64F8A)),
        AvatarDefinition("aqua", "Aqua", AvatarPalette(0xFF3FA9E0, 0xFF2DE2E6, 0xFF1F3A5A, 0xFF2DB3E6)),
        AvatarDefinition("mocha", "Mocha", AvatarPalette(0xFF6B4A3A, 0xFFB07A5A, 0xFF3A2A25, 0xFF8A5A3A)),
        AvatarDefinition("sol", "Sol", AvatarPalette(0xFFF2B84B, 0xFFFFD98A, 0xFF5A3A1F, 0xFFE08A2D)),
        AvatarDefinition("ciel", "Ciel", AvatarPalette(0xFF2D6CDF, 0xFF6CA8FF, 0xFF1B2A5A, 0xFF3F6CD6)),
        AvatarDefinition("nieve", "Nieve", AvatarPalette(0xFFCFD3E8, 0xFFEEF0FF, 0xFF2A2F4F, 0xFF8A8FD6)),
    )

    val default: AvatarDefinition = all.first()

    fun find(id: String?): AvatarDefinition? = all.firstOrNull { it.id == id }

    fun byId(id: String?): AvatarDefinition = find(id) ?: default
}
