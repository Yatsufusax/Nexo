package com.nexo.app.ui.avatar

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import com.nexo.app.engine.avatar.AvatarDefinition
import com.nexo.app.engine.avatar.AvatarPalette
import com.nexo.app.engine.avatar.AvatarState

/**
 * Mascota chibi PROVISIONAL dibujada con vectores. Estados con pose propia:
 * Idle, Sleep, Happy (y Greet), Think, Talk. Walk y Sit usan la pose base por ahora.
 * Proporción recomendada: ancho = 0.62 * alto.
 */
@Composable
fun ChibiAvatar(
    definition: AvatarDefinition,
    state: AvatarState,
    modifier: Modifier = Modifier,
    animate: Boolean = true,
) {
    val bob: State<Float> = if (animate) {
        rememberInfiniteTransition(label = "bob").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(tween(1400), RepeatMode.Reverse),
            label = "bobValue",
        )
    } else {
        remember { mutableStateOf(0f) }
    }
    Canvas(modifier) { drawChibi(definition.palette, state, bob.value) }
}

private val Skin = Color(0xFFF6D9C8)
private val Blush = Color(0xFFF29BB8)
private val Ink = Color(0xFF1B1638)
private val Cyan = Color(0xFF2DE2E6)
private val MouthColor = Color(0xFF8A4A5A)
private val Cushion = Color(0xFFB9A8FF)
private val Bubble = Color(0xFFEEF0FF)

private fun DrawScope.drawChibi(p: AvatarPalette, state: AvatarState, bob: Float) {
    val s = size.height / 165f
    withTransform({
        translate(size.width / 2f, size.height - 8f * s)
        scale(s, s, Offset.Zero)
    }) {
        oval(Color(0x33000000), 0f, 0f, 32f, 5f)
        if (state == AvatarState.Sleep) {
            oval(Cushion, 0f, -2f, 46f, 12f)
            translate(0f, -8f - bob) {
                rotate(-5f, Offset.Zero) { figure(p, state, bob) }
            }
        } else {
            translate(0f, -2.5f * bob) { figure(p, state, bob) }
        }
    }
}

private fun DrawScope.figure(p: AvatarPalette, state: AvatarState, bob: Float) {
    base(p)
    when (state) {
        AvatarState.Sleep -> {
            armsDown(p)
            curve(Ink, 2.2f, -18f, -76f, -12f, -71f, -6f, -76f)
            curve(Ink, 2.2f, 6f, -76f, 12f, -71f, 18f, -76f)
            curve(MouthColor, 1.8f, -2f, -62f, 0f, -60f, 2f, -62f)
            line(Cushion, 2f, 40f, -110f, 48f, -110f)
            line(Cushion, 2f, 48f, -110f, 40f, -100f)
            line(Cushion, 2f, 40f, -100f, 48f, -100f)
        }
        AvatarState.Happy, AvatarState.Greet -> {
            line(Color(p.outfit), 7f, -18f, -44f, -42f, -50f)
            line(Color(p.outfit), 7f, 18f, -44f, 42f, -50f)
            circle(Skin, -43f, -51f, 4.5f)
            circle(Skin, 43f, -51f, 4.5f)
            curve(Ink, 2.2f, -18f, -74f, -12f, -83f, -6f, -74f)
            curve(Ink, 2.2f, 6f, -74f, 12f, -83f, 18f, -74f)
            openMouth()
            translate(38f, -128f) {
                val heart = Path().apply {
                    moveTo(0f, 4f)
                    cubicTo(-7f, -4f, -15f, 2f, -9f, 9f)
                    lineTo(0f, 17f)
                    lineTo(9f, 9f)
                    cubicTo(15f, 2f, 7f, -4f, 0f, 4f)
                    close()
                }
                drawPath(heart, Color(0xFFFF6FA5))
            }
        }
        AvatarState.Think -> {
            line(Color(p.outfit), 7f, -18f, -46f, -24f, -26f)
            circle(Skin, -24f, -24f, 4.5f)
            curve(Color(p.outfit), 7f, 17f, -44f, 30f, -48f, 12f, -55f)
            circle(Skin, 11f, -56f, 4.5f)
            openEyes(p, -1f, -3f)
            line(MouthColor, 1.8f, -3f, -62f, 3f, -62f)
            circle(Bubble, 38f, -112f, 2.5f)
            circle(Bubble, 46f, -124f, 3.5f)
            oval(Bubble, 60f, -142f, 20f, 12f)
            circle(Color(p.eye), 52f, -142f, 2f)
            circle(Color(p.eye), 60f, -142f, 2f)
            circle(Color(p.eye), 68f, -142f, 2f)
        }
        AvatarState.Talk -> {
            armsDown(p)
            openEyes(p)
            if (bob > 0.5f) openMouth() else smile()
        }
        else -> {
            armsDown(p)
            openEyes(p)
            smile()
        }
    }
}

private fun DrawScope.base(p: AvatarPalette) {
    val hair = Color(p.hair)
    curve(Color(p.outfit), 7f, 16f, -30f, 42f, -34f, 38f, -66f)
    rrect(Ink, -14f, -20f, 11f, 18f, 4f)
    rrect(Ink, 3f, -20f, 11f, 18f, 4f)
    rrect(Color(p.outfit), -18f, -52f, 36f, 34f, 10f)
    rrect(Cyan, -3f, -44f, 6f, 8f, 2f)
    polygon(hair, -31f to -102f, -35f to -130f, -9f to -112f)
    polygon(Blush, -29f to -106f, -31f to -122f, -17f to -112f)
    polygon(hair, 31f to -102f, 35f to -130f, 9f to -112f)
    polygon(Blush, 29f to -106f, 31f to -122f, 17f to -112f)
    circle(hair, 0f, -82f, 35f)
    oval(Color(p.hairTip), -33f, -62f, 5f, 12f)
    oval(Color(p.hairTip), 33f, -62f, 5f, 12f)
    circle(Skin, 0f, -78f, 30f)
    val bangs = Path().apply {
        moveTo(-31f, -80f)
        quadraticTo(-28f, -112f, 0f, -110f)
        quadraticTo(28f, -112f, 31f, -80f)
        quadraticTo(18f, -98f, 0f, -92f)
        quadraticTo(-18f, -98f, -31f, -80f)
        close()
    }
    drawPath(bangs, hair)
    circle(Cyan, 22f, -102f, 4f)
    oval(Blush, -20f, -66f, 5f, 3f, 0.7f)
    oval(Blush, 20f, -66f, 5f, 3f, 0.7f)
}

private fun DrawScope.armsDown(p: AvatarPalette) {
    line(Color(p.outfit), 7f, -18f, -46f, -24f, -26f)
    line(Color(p.outfit), 7f, 18f, -46f, 24f, -26f)
    circle(Skin, -24f, -24f, 4.5f)
    circle(Skin, 24f, -24f, 4.5f)
}

private fun DrawScope.openEyes(p: AvatarPalette, lookX: Float = 0f, lookY: Float = 0f) {
    for (sx in listOf(-1f, 1f)) {
        val cx = 12f * sx
        oval(Color(p.eye), cx, -76f, 6.5f, 8.5f)
        oval(Ink, cx + lookX, -75f + lookY, 3f, 5f)
        circle(Color.White, cx - 2f + lookX, -79f + lookY, 2f)
    }
}

private fun DrawScope.smile() = curve(MouthColor, 1.8f, -3f, -62f, 0f, -59f, 3f, -62f)

private fun DrawScope.openMouth() {
    val m = Path().apply {
        moveTo(-5f, -64f)
        quadraticTo(0f, -55f, 5f, -64f)
        close()
    }
    drawPath(m, MouthColor)
}

private fun DrawScope.polygon(color: Color, vararg pts: Pair<Float, Float>) {
    val path = Path()
    pts.forEachIndexed { i, (x, y) -> if (i == 0) path.moveTo(x, y) else path.lineTo(x, y) }
    path.close()
    drawPath(path, color)
}

private fun DrawScope.line(color: Color, w: Float, x1: Float, y1: Float, x2: Float, y2: Float) =
    drawLine(color, Offset(x1, y1), Offset(x2, y2), w, StrokeCap.Round)

private fun DrawScope.oval(color: Color, cx: Float, cy: Float, rx: Float, ry: Float, alpha: Float = 1f) =
    drawOval(color, Offset(cx - rx, cy - ry), Size(rx * 2f, ry * 2f), alpha)

private fun DrawScope.circle(color: Color, cx: Float, cy: Float, r: Float) =
    drawCircle(color, r, Offset(cx, cy))

private fun DrawScope.rrect(color: Color, x: Float, y: Float, w: Float, h: Float, r: Float) =
    drawRoundRect(color, Offset(x, y), Size(w, h), CornerRadius(r, r))

private fun DrawScope.curve(
    color: Color, w: Float, x1: Float, y1: Float, cx: Float, cy: Float, x2: Float, y2: Float,
) {
    val path = Path().apply {
        moveTo(x1, y1)
        quadraticTo(cx, cy, x2, y2)
    }
    drawPath(path, color, style = Stroke(width = w, cap = StrokeCap.Round))
}
