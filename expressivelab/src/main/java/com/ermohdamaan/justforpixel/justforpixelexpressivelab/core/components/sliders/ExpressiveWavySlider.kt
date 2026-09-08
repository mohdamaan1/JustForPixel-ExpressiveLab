package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.sliders

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import kotlinx.coroutines.isActive
import kotlin.math.abs

/**
 * Material 3 Expressive Wavy Slider with Animated Wave Amplitude and Dynamic Thumb Expansion.
 *
 * @param value Current slider value producer
 * @param onValueChange Callback during drag/seek
 * @param modifier Custom modifier
 * @param enabled Whether slider accepts user interaction
 * @param valueRange Value range boundaries (default 0f..1f)
 * @param onValueChangeFinished Callback when user finishes drag/seek
 * @param isPlaying Controls whether the track wave animates
 * @param strokeWidth Track line stroke thickness
 * @param thumbRadius Thumb circle radius in idle state
 * @param thumbLineHeightWhenInteracting Thumb height when dragged
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveWavySlider(
    value: () -> Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    onValueChangeFinished: (() -> Unit)? = null,
    activeTrackColor: Color = MaterialTheme.colorScheme.primary,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    thumbColor: Color = MaterialTheme.colorScheme.primary,
    isPlaying: Boolean = true,
    strokeWidth: Dp = 5.dp,
    thumbRadius: Dp = 8.dp,
    wavelength: Dp = WavyProgressIndicatorDefaults.LinearDeterminateWavelength,
    waveSpeed: Dp = WavyProgressIndicatorDefaults.LinearDeterminateWavelength / 2.5f,
    thumbLineHeightWhenInteracting: Dp = 24.dp,
    semanticsLabel: String? = "Expressive Wavy Slider"
) {
    val density = LocalDensity.current
    val strokeWidthPx = with(density) { strokeWidth.toPx() }
    val thumbRadiusPx = with(density) { thumbRadius.toPx() }
    val thumbLineHeightPx = with(density) { thumbLineHeightWhenInteracting.toPx() }

    val stroke = remember(strokeWidthPx) {
        Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
    }

    val normalizedValueState = remember(valueRange) {
        derivedStateOf {
            val v = value()
            if (valueRange.endInclusive == valueRange.start) 0f
            else ((v - valueRange.start) / (valueRange.endInclusive - valueRange.start)).coerceIn(0f, 1f)
        }
    }

    val latestOnValueChange by rememberUpdatedState(onValueChange)
    val latestOnValueChangeFinished by rememberUpdatedState(onValueChangeFinished)
    var isPointerSeeking by remember { mutableStateOf(false) }

    val thumbInteractionFraction by animateFloatAsState(
        targetValue = if (isPointerSeeking) 1f else 0f,
        animationSpec = tween(220, easing = FastOutSlowInEasing),
        label = "ThumbInteractionAnim"
    )

    val animatedAmplitude by animateFloatAsState(
        targetValue = if (enabled && isPlaying && !isPointerSeeking) 1f else 0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "AmplitudeAnim"
    )

    val dynamicGapSize = remember {
        derivedStateOf {
            val fraction = thumbInteractionFraction
            val idleGap = 6.dp
            val draggingGap = thumbRadius + 2.dp
            idleGap + (draggingGap - idleGap) * fraction
        }
    }

    val renderedNormalizedProgress = remember {
        val initialNorm = normalizedValueState.value
        mutableFloatStateOf(initialNorm)
    }

    LaunchedEffect(isPointerSeeking, enabled) {
        snapshotFlow { normalizedValueState.value }.collect { target ->
            if (!enabled || isPointerSeeking) {
                renderedNormalizedProgress.floatValue = target
                return@collect
            }

            val start = renderedNormalizedProgress.floatValue
            if (abs(start - target) > 0.15f) {
                renderedNormalizedProgress.floatValue = target
                return@collect
            }

            var startFrameNanos = 0L
            val durationNanos = 120_000_000L
            while (isActive) {
                val frameNanos = withFrameNanos { it }
                if (startFrameNanos == 0L) startFrameNanos = frameNanos
                val elapsedNanos = (frameNanos - startFrameNanos).coerceAtLeast(0L)
                val fraction = (elapsedNanos.toDouble() / durationNanos.toDouble()).toFloat().coerceIn(0f, 1f)
                renderedNormalizedProgress.floatValue = start + (target - start) * fraction
                if (fraction >= 1f) break
            }
            renderedNormalizedProgress.floatValue = target
        }
    }

    val containerHeight = max(WavyProgressIndicatorDefaults.LinearContainerHeight, thumbLineHeightWhenInteracting)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(containerHeight)
            .clearAndSetSemantics {
                if (!semanticsLabel.isNullOrBlank()) {
                    contentDescription = semanticsLabel
                }
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = value(),
                    range = valueRange.start..valueRange.endInclusive,
                    steps = 0
                )
                if (enabled) {
                    setProgress { requested ->
                        val coerced = requested.coerceIn(valueRange.start, valueRange.endInclusive)
                        latestOnValueChange(coerced)
                        latestOnValueChangeFinished?.invoke()
                        true
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        LinearWavyProgressIndicator(
            progress = { renderedNormalizedProgress.floatValue },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = thumbRadius)
                .clearAndSetSemantics { },
            color = activeTrackColor,
            trackColor = inactiveTrackColor,
            stroke = stroke,
            trackStroke = stroke,
            gapSize = dynamicGapSize.value * 2f,
            stopSize = 3.dp,
            amplitude = { progress -> if (progress > 0f) animatedAmplitude else 0f },
            wavelength = wavelength,
            waveSpeed = waveSpeed
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val edgePaddingPx = thumbRadiusPx.coerceIn(0f, size.width / 2f)
            val trackStart = edgePaddingPx
            val trackEnd = size.width - edgePaddingPx
            val trackWidth = (trackEnd - trackStart).coerceAtLeast(0f)
            val thumbY = size.height / 2
            val renderedProgress = renderedNormalizedProgress.floatValue

            fun lerp(start: Float, stop: Float, fraction: Float): Float = start + (stop - start) * fraction

            val currentWidth = lerp(thumbRadiusPx * 2f, strokeWidthPx * 1.2f, thumbInteractionFraction)
            val currentHeight = lerp(thumbRadiusPx * 2f, thumbLineHeightPx, thumbInteractionFraction)
            val rawThumbX = trackStart + (trackWidth * renderedProgress)
            val thumbX = rawThumbX.coerceIn(currentWidth / 2f, size.width - currentWidth / 2f)

            drawRoundRect(
                color = thumbColor,
                topLeft = Offset(thumbX - currentWidth / 2f, thumbY - currentHeight / 2f),
                size = Size(currentWidth, currentHeight),
                cornerRadius = CornerRadius(currentWidth / 2f)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(enabled, valueRange, thumbRadiusPx) {
                    if (!enabled) return@pointerInput

                    fun valueForX(rawX: Float): Float {
                        val edgePadding = thumbRadiusPx.coerceIn(0f, size.width / 2f)
                        val trackStart = edgePadding
                        val trackEnd = size.width - edgePadding
                        val trackWidth = (trackEnd - trackStart).coerceAtLeast(1f)
                        val normalized = ((rawX - trackStart) / trackWidth).coerceIn(0f, 1f)
                        return valueRange.start + normalized * (valueRange.endInclusive - valueRange.start)
                    }

                    awaitEachGesture {
                        try {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            isPointerSeeking = true
                            down.consume()
                            var latestGestureValue = valueForX(down.position.x)
                            latestOnValueChange(latestGestureValue)

                            var pointerId = down.id
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == pointerId }
                                    ?: event.changes.firstOrNull { it.pressed }
                                    ?: break

                                pointerId = change.id
                                if (!change.pressed) {
                                    change.consume()
                                    break
                                }

                                if (change.position != change.previousPosition) {
                                    change.consume()
                                    latestGestureValue = valueForX(change.position.x)
                                    latestOnValueChange(latestGestureValue)
                                }
                            }

                            latestOnValueChangeFinished?.invoke()
                        } finally {
                            isPointerSeeking = false
                        }
                    }
                }
        )
    }
}
