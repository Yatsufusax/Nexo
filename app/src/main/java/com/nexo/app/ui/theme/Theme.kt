package com.nexo.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val NexoColors = darkColorScheme(
    primary = NexoViolet,
    onPrimary = Color.White,
    secondary = NexoCyan,
    onSecondary = NexoInk,
    background = NexoInk,
    onBackground = NexoText,
    surface = NexoSurface,
    onSurface = NexoText,
    onSurfaceVariant = NexoMuted,
)

private val NexoTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 56.sp,
        letterSpacing = 10.sp,
    ),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 15.sp, letterSpacing = 1.sp),
)

@Composable
fun NexoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NexoColors,
        typography = NexoTypography,
        content = content,
    )
}
