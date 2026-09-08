package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.rings

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sin

/**
 * Material 3 Expressive Oscillating Wavy Progress Ring for Timers & Playback Progress.
 *
 * Renders a circular progress arc with pulsating wave amplitude.
 * Slower cycle speed (4500ms) guarantees a relaxed, smooth 60fps wave motion.
 *
 * @param progress Value from 0.0f to 1.0f
 * @param modifier Custom modifier
 * @param strokeWidth Thickness of the ring stroke
 * @param color Active progress arc color
 * @param trackColor Inactive background ring track color
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun AnimatedWavyProgressRing(
    progress: Float,
    modifier: Modifier = Modifier.size(200.dp),
    strokeWidth: Dp = 10.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wavy_ring_transition")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_ring_offset"
    )

    Canvas(modifier = modifier) {
        val s = strokeWidth.toPx()
        val diameter = minOf(size.width, size.height) - s
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val arcSize = Size(diameter, diameter)

        // Track Arc
        drawArc(
            color = trackColor,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = s, cap = StrokeCap.Round)
        )

        // Dynamic Progress Arc with pulsating wave oscillation
        if (progress > 0f) {
            val sweep = 360f * progress.coerceIn(0f, 1f)
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = s + (2.dp.toPx() * sin(waveOffset)), cap = StrokeCap.Round)
            )
        }
    }
}
