package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.buttons

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import racra.compose.smooth_corner_rect_library.AbsoluteSmoothCornerShape

/** Enum representing the control button selection state */
enum class ExpressiveButtonType { NONE, PREVIOUS, PLAY_PAUSE, NEXT }

/**
 * Expressive Playback Control Row with Interactive Weight Morphing and Push Physics.
 *
 * Tapping a button smoothly expands its layout weight (e.g. 1.0f -> 1.30f) while
 * compressing neighboring buttons (1.0f -> 0.60f), creating an elastic "push" effect.
 * The central button morphs its corner radius dynamically when toggling state.
 *
 * @param isPlaying Current playback/toggle state
 * @param onPrevious Action triggered when previous button is tapped
 * @param onPlayPause Action triggered when central play/pause button is tapped
 * @param onNext Action triggered when next button is tapped
 * @param modifier Optional modifier for row styling
 * @param height Height of the control row
 * @param baseWeight Default weight of buttons in idle state
 * @param expansionWeight Weight assigned to the tapped button during press state
 * @param compressionWeight Weight assigned to neighboring un-tapped buttons
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressivePushButtonRow(
    isPlaying: Boolean,
    onPrevious: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 90.dp,
    baseWeight: Float = 1f,
    expansionWeight: Float = 1.30f,
    compressionWeight: Float = 0.60f,
    pressAnimationSpec: AnimationSpec<Float> = TactileMotionTokens.bouncySpring(),
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    onActiveColor: Color = MaterialTheme.colorScheme.onPrimary,
    onInactiveColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    var lastClicked by remember { mutableStateOf<ExpressiveButtonType?>(null) }
    var clickTrigger by remember { mutableStateOf(0) }
    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val motionScheme = remember { MotionScheme.expressive() }
    val spatialDpSpec = remember { motionScheme.defaultSpatialSpec<Dp>() }

    // Auto-reset clicked button state after press delay
    LaunchedEffect(lastClicked, clickTrigger) {
        if (lastClicked != null) {
            delay(280L)
            lastClicked = null
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Helper function to calculate weight dynamically
            fun weightFor(button: ExpressiveButtonType): Float = when (lastClicked) {
                button -> expansionWeight
                null -> baseWeight
                else -> compressionWeight
            }

            // Previous Button
            val prevWeight by animateFloatAsState(
                targetValue = weightFor(ExpressiveButtonType.PREVIOUS),
                animationSpec = pressAnimationSpec,
                label = "prevWeightAnim"
            )
            Box(
                modifier = Modifier
                    .weight(prevWeight)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(containerColor)
                    .clickable {
                        lastClicked = ExpressiveButtonType.PREVIOUS
                        clickTrigger++
                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                        coroutineScope.launch {
                            delay(120)
                            onPrevious()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = "Previous",
                    tint = onInactiveColor,
                    modifier = Modifier.size(36.dp)
                )
            }

            // Play / Pause Central Button with Shape Morphing
            val playWeight by animateFloatAsState(
                targetValue = weightFor(ExpressiveButtonType.PLAY_PAUSE),
                animationSpec = pressAnimationSpec,
                label = "playWeightAnim"
            )
            val playCornerRadius by animateDpAsState(
                targetValue = if (isPlaying) 22.dp else 50.dp, // Morphs between Smooth Square & Pill
                animationSpec = spatialDpSpec,
                label = "playCornerAnim"
            )
            Box(
                modifier = Modifier
                    .weight(playWeight)
                    .fillMaxHeight()
                    .graphicsLayer {
                        clip = true
                        shape = AbsoluteSmoothCornerShape(
                            cornerRadius = playCornerRadius,
                            smoothnessAsPercent = 60
                        )
                    }
                    .background(activeColor)
                    .clickable {
                        lastClicked = ExpressiveButtonType.PLAY_PAUSE
                        clickTrigger++
                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                        onPlayPause()
                    },
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = isPlaying,
                    animationSpec = motionScheme.fastEffectsSpec(),
                    label = "playPauseIconCrossfade"
                ) { playing ->
                    Icon(
                        imageVector = if (playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = if (playing) "Pause" else "Play",
                        tint = onActiveColor,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            // Next Button
            val nextWeight by animateFloatAsState(
                targetValue = weightFor(ExpressiveButtonType.NEXT),
                animationSpec = pressAnimationSpec,
                label = "nextWeightAnim"
            )
            Box(
                modifier = Modifier
                    .weight(nextWeight)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(containerColor)
                    .clickable {
                        lastClicked = ExpressiveButtonType.NEXT
                        clickTrigger++
                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                        coroutineScope.launch {
                            delay(120)
                            onNext()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = "Next",
                    tint = onInactiveColor,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
