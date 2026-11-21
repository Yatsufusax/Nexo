package com.nexo.app.engine.avatar

/** Colores ARGB (0xAARRGGBB). Sin dependencias de Compose para poder probarlo en JVM. */
data class AvatarPalette(
    val hair: Long,
    val hairTip: Long,
    val outfit: Long,
    val eye: Long,
)

data class AvatarDefinition(
    val id: String,
    val displayName: String,
    val palette: AvatarPalette,
)
