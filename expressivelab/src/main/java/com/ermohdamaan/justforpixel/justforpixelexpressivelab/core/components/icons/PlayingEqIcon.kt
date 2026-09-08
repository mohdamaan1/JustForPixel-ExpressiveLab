package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.icons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive
import kotlin.math.PI
import kotlin.math.sin

/**
 * Animated Equalizer Bar Icon with Active State Morphing.
 *
 * Smoothly morphs between static dots when paused and animated jumping frequency bars when active.
 *
 * @param modifier Custom modifier
 * @param color Bar tint color
 * @param isPlaying Active state boolean
 * @param bars Number of vertical equalizer bars (default 4)
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun PlayingEqIcon(
    modifier: Modifier = Modifier.size(24.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    isPlaying: Boolean = true,
    bars: Int = 4,
    minHeightFraction: Float = 0.25f,
    maxHeightFraction: Float = 1.0f,
    phaseDurationMillis: Int = 2400
) {
    val fullRotation = (2f * PI).toFloat()
    val phaseAnim = remember { Animatable(0f) }

    LaunchedEffect(isPlaying) {
        if (!isPlaying) return@LaunchedEffect
        while (isActive) {
            val start = (phaseAnim.value % fullRotation).let { if (it < 0f) it + fullRotation else it }
            phaseAnim.snapTo(start)
            phaseAnim.animateTo(
                targetValue = start + fullRotation,
                animationSpec = tween(durationMillis = phaseDurationMillis, easing = LinearEasing)
            )
        }
    }

    val activity by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "EqActivityAnim"
    )

    val speeds = remember(bars) { List(bars) { (it + 1).toFloat() * 1.2f } }
    val shifts = remember(bars) { List(bars) { i -> i * 0.8f } }

    Canvas(modifier = modifier) {
        val phase = phaseAnim.value
        val w = size.width
        val h = size.height
        val gapFraction = 0.35f

        val tentativeBarW = w / (bars + (bars - 1) * (1f + gapFraction))
        val gap = tentativeBarW * gapFraction
        val barW = tentativeBarW
        val corner = CornerRadius(barW / 2f, barW / 2f)

        repeat(bars) { i ->
            val v = (sin(phase * speeds[i] + shifts[i]) + 1f) * 0.5f
            val eased = v * v * (3 - 2 * v)

            val fracBars = minHeightFraction + (maxHeightFraction - minHeightFraction) * eased
            val barH = h * fracBars
            val dotH = barW

            val blendedH = dotH + (barH - dotH) * activity
            val top = (h - blendedH) / 2f
            val left = i * (barW + gap)

            drawRoundRect(
                color = color,
                topLeft = Offset(left, top),
                size = Size(barW, blendedH),
                cornerRadius = corner
            )
        }
    }
}
