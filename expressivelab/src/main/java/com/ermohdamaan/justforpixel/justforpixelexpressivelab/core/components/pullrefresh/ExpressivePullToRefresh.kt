package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.pullrefresh

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * State holder managing gesture pull offset, overscroll spring damping,
 * and refresh lifecycle for [ExpressivePullToRefreshBox].
 *
 * DETAILED EXPLANATION OF HOW THIS WORKS:
 * 1. [pullOffset]: Tracks the physical pixels pulled down by user gesture.
 * 2. [refreshThresholdPx]: Distance in pixels required to activate the refresh trigger.
 * 3. [progress]: Normalized value from 0.0f to 1.0f indicating drag progress towards refresh.
 * 4. Damping Physics: Pull resistance increases as [pullOffset] exceeds threshold to simulate rubber-band liquid tension.
 *
 * @param refreshThreshold Distance in Dp to trigger refresh
 * @param coroutineScope Scope for launching spring rebound animations
 *
 * @author Er. Mohd Amaan
 */
class ExpressivePullToRefreshState(
    val refreshThresholdPx: Float,
    private val coroutineScope: CoroutineScope
) {
    /** Animated offset representing vertical translation in pixels */
    val pullOffsetAnimatable = Animatable(0f)

    /** Current pull offset value in pixels */
    val pullOffset: Float get() = pullOffsetAnimatable.value

    /** Whether refresh action is currently executing */
    var isRefreshing by mutableStateOf(false)
        private set

    /** Whether pull gesture has exceeded the required activation threshold */
    val isThresholdReached: Boolean get() = pullOffset >= refreshThresholdPx

    /** Normalized pull progress (0.0f when idle, 1.0f at threshold) */
    val progress: Float get() = (pullOffset / refreshThresholdPx).coerceIn(0f, 1f)

    /**
     * Handles incremental pull-down gesture distance with rubber-band dampening.
     *
     * Rubber-band dampening formula: Resistance multiplier decreases as distance grows,
     * producing a natural, elastic liquid feel like a stretched rubber band.
     *
     * @param delta Y-axis scroll delta in pixels
     */
    fun onPull(delta: Float) {
        if (isRefreshing) return
        coroutineScope.launch {
            // Apply rubber-band damping resistance multiplier (0.5f decreasing to 0.2f)
            val resistance = (1f - (pullOffset / (refreshThresholdPx * 2.5f))).coerceIn(0.2f, 0.55f)
            val newOffset = (pullOffset + (delta * resistance)).coerceAtLeast(0f)
            pullOffsetAnimatable.snapTo(newOffset)
        }
    }

    /**
     * Triggers when user releases touch gesture.
     *
     * If threshold is reached, snaps to threshold position and triggers [onRefresh].
     * Otherwise, executes a bouncy spring rebound back to 0.
     *
     * @param onRefresh Callback function to execute refresh work
     * @param hapticTrigger Callback for haptic feedback
     */
    fun onRelease(onRefresh: () -> Unit, hapticTrigger: () -> Unit) {
        coroutineScope.launch {
            if (isThresholdReached && !isRefreshing) {
                isRefreshing = true
                hapticTrigger()
                // Snap to threshold and execute refresh callback
                pullOffsetAnimatable.animateTo(
                    targetValue = refreshThresholdPx,
                    animationSpec = TactileMotionTokens.bouncySpring()
                )
                onRefresh()
            } else if (!isRefreshing) {
                // Spring rebound back to idle top position
                pullOffsetAnimatable.animateTo(
                    targetValue = 0f,
                    animationSpec = TactileMotionTokens.bouncySpring()
                )
            }
        }
    }

    /**
     * Resets the refresh state and executes spring rebound animation back to top.
     */
    fun endRefresh() {
        coroutineScope.launch {
            isRefreshing = false
            pullOffsetAnimatable.animateTo(
                targetValue = 0f,
                animationSpec = TactileMotionTokens.bouncySpring()
            )
        }
    }
}

/**
 * Creates and remembers a instance of [ExpressivePullToRefreshState].
 *
 * @param refreshThreshold Distance required to activate refresh trigger (default 80.dp)
 * @return Remembered [ExpressivePullToRefreshState]
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun rememberExpressivePullToRefreshState(
    refreshThreshold: Dp = 80.dp
): ExpressivePullToRefreshState {
    val density = LocalDensity.current
    val thresholdPx = with(density) { refreshThreshold.toPx() }
    val coroutineScope = rememberCoroutineScope()

    return remember(thresholdPx) {
        ExpressivePullToRefreshState(
            refreshThresholdPx = thresholdPx,
            coroutineScope = coroutineScope
        )
    }
}

/**
 * Material 3 Expressive Liquid Wavy Pull-To-Refresh Container Wrapper.
 *
 * DIFFERENCE FROM BASIC SPINNERS:
 * - Basic spinners spin continuously in a fixed box without touch interaction.
 * - [ExpressivePullToRefreshBox] intercepts touch pull-down gestures on any scrollable list,
 *   stretches an elastic liquid wavy indicator in real-time based on touch pull distance,
 *   morphs into a bouncy squircle checkmark upon trigger, and rebounds back smoothly!
 *
 * @param isRefreshing External state indicating if refresh work is in progress
 * @param onRefresh Callback function triggered when user pulls down past threshold
 * @param modifier Custom modifier
 * @param state State holder managing pull offset and spring physics
 * @param content Scrollable child composable content (e.g. LazyColumn, Column, Grid)
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressivePullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: ExpressivePullToRefreshState = rememberExpressivePullToRefreshState(),
    content: @Composable BoxScope.() -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val density = LocalDensity.current

    val ringStrokePx = with(density) { 3.dp.toPx() }
    val stroke = remember(ringStrokePx) { Stroke(width = ringStrokePx, cap = StrokeCap.Round) }

    // Sync external isRefreshing state with internal state
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing && state.isRefreshing) {
            state.endRefresh()
        }
    }

    // NestedScrollConnection intercepting pull-down touch scroll events
    val nestedScrollConnection = remember(state) {
        object : NestedScrollConnection {
            /**
             * Intercepts pre-scroll deltas before child scrollable consumes them.
             * Used when pulling down while list is at top position (index 0).
             */
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput && available.y < 0 && state.pullOffset > 0f) {
                    state.onPull(available.y)
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            /**
             * Intercepts post-scroll deltas unconsumed by child scrollable.
             */
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (source == NestedScrollSource.UserInput && available.y > 0) {
                    state.onPull(available.y)
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            /**
             * Intercepts gesture release event (finger lifted off screen).
             */
            override suspend fun onPostFling(consumed: androidx.compose.ui.unit.Velocity, available: androidx.compose.ui.unit.Velocity): androidx.compose.ui.unit.Velocity {
                if (state.pullOffset > 0f) {
                    state.onRelease(
                        onRefresh = onRefresh,
                        hapticTrigger = {
                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                        }
                    )
                }
                return super.onPostFling(consumed, available)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(nestedScrollConnection)
    ) {
        // Child Content Offset according to gesture pull distance
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, state.pullOffset.roundToInt()) },
            content = content
        )

        // ELASTIC LIQUID WAVY HEADER INDICATOR AT TOP
        if (state.pullOffset > 0f || state.isRefreshing) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset { IntOffset(0, (state.pullOffset * 0.45f).roundToInt() - 40) }
                    .graphicsLayer {
                        // Elastic scale bounce based on pull progress
                        val scale = if (state.isRefreshing) 1.15f else (0.6f + (state.progress * 0.45f))
                        scaleX = scale
                        scaleY = scale
                    },
                contentAlignment = Alignment.Center
            ) {
                // Outer Liquid Wave Glow Pill
                Surface(
                    shape = ShapeCache.smooth20,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shadowElevation = 6.dp,
                    tonalElevation = 6.dp,
                    modifier = Modifier
                        .border(
                            width = 1.5.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            shape = ShapeCache.smooth20
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (state.isRefreshing) {
                            // Active Refreshing State: Circular Wavy Progress Ring
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularWavyProgressIndicator(
                                    progress = { 0.75f },
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    stroke = stroke,
                                    trackStroke = stroke
                                )
                                Spacer(modifier = Modifier.padding(start = 10.dp))
                                Text(
                                    text = "Refreshing...",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        } else {
                            // Pulling State: Elastic Wavy Progress Indicator with Checkmark Preview
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (state.isThresholdReached) Icons.Rounded.Check else Icons.Rounded.Refresh,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .graphicsLayer {
                                            // Rotation animation matching pull progress
                                            rotationZ = state.progress * 180f
                                        }
                                )
                                Spacer(modifier = Modifier.padding(start = 8.dp))
                                Text(
                                    text = if (state.isThresholdReached) "Release to Refresh" else "Pull to Refresh",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
