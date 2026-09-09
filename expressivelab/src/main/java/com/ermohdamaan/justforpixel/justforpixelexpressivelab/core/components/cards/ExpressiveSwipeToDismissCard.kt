package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Pin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * Enumeration of swipe directions and active action thresholds for [ExpressiveSwipeToDismissCard].
 *
 * @author Er. Mohd Amaan
 */
enum class SwipeDismissDirection {
    /** Swiped from left to right (e.g. Pin action) */
    StartToEnd,
    /** Swiped from right to left (e.g. Delete/Archive action) */
    EndToStart,
    /** Idle unswiped position */
    None
}

/**
 * Material 3 Expressive Elastic Swipe-To-Dismiss Card Row.
 *
 * Features:
 * - Rubber-band elastic resistance as swipe offset increases.
 * - Fluid off-screen slide animation before triggering dismissal collapse.
 * - Morphing action icons (Pin on Left Swipe, Delete/Archive on Right Swipe) scaling up with bouncy spring physics.
 * - Tactile haptic notch click upon crossing activation threshold.
 * - Background container color transition (Green for Pin, Red for Delete, Amber for Archive).
 * - Smooth dismissal shrink animation with Undo action hook.
 *
 * @param onDismissed Callback triggered when item is fully swiped past threshold
 * @param modifier Custom modifier
 * @param key Optional state key for Compose item preservation
 * @param startActionIcon Left swipe action icon (default Pin)
 * @param startActionColor Left swipe background color (default Emerald Green)
 * @param endActionIcon Right swipe action icon (default Delete)
 * @param endActionColor Right swipe background color (default Crimson Red)
 * @param content Main card content composable
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveSwipeToDismissCard(
    onDismissed: (SwipeDismissDirection) -> Unit,
    modifier: Modifier = Modifier,
    key: Any? = null,
    startActionIcon: ImageVector = Icons.Rounded.Pin,
    startActionColor: Color = Color(0xFF00E676),
    endActionIcon: ImageVector = Icons.Rounded.Delete,
    endActionColor: Color = Color(0xFFFF3D00),
    content: @Composable RowScope.() -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    // Swipe activation threshold in pixels (75.dp)
    val thresholdPx = with(density) { 75.dp.toPx() }

    // Animatable offset tracking X-axis translation
    val offsetX = remember(key) { Animatable(0f) }
    var isDismissed by remember(key) { mutableStateOf(false) }
    var hasHapticTriggered by remember(key) { mutableStateOf(false) }

    // Determine current active direction
    val currentDirection = when {
        offsetX.value > 0f -> SwipeDismissDirection.StartToEnd
        offsetX.value < 0f -> SwipeDismissDirection.EndToStart
        else -> SwipeDismissDirection.None
    }

    val isPastThreshold = abs(offsetX.value) >= thresholdPx

    // Trigger haptic notch click once when user crosses activation threshold
    LaunchedEffect(isPastThreshold) {
        if (isPastThreshold && !hasHapticTriggered) {
            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
            hasHapticTriggered = true
        } else if (!isPastThreshold) {
            hasHapticTriggered = false
        }
    }

    // Dynamic background color transition
    val activeBackgroundColor by animateColorAsState(
        targetValue = when (currentDirection) {
            SwipeDismissDirection.StartToEnd -> startActionColor
            SwipeDismissDirection.EndToStart -> endActionColor
            SwipeDismissDirection.None -> MaterialTheme.colorScheme.surfaceContainerHigh
        },
        animationSpec = TactileMotionTokens.gentleSpring(),
        label = "SwipeBgColor"
    )

    AnimatedVisibility(
        visible = !isDismissed,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically(animationSpec = tween(180)) + fadeOut(animationSpec = tween(150))
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(ShapeCache.smooth20)
                .background(activeBackgroundColor)
        ) {
            // REVEALED BACKGROUND ACTION ICONS (Left & Right)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // START ACTION (Left Icon)
                val startScale by animateFloatAsState(
                    targetValue = if (currentDirection == SwipeDismissDirection.StartToEnd && isPastThreshold) 1.25f else 0.85f,
                    animationSpec = TactileMotionTokens.bouncySpring(),
                    label = "StartIconScale"
                )
                Box(
                    modifier = Modifier.graphicsLayer {
                        scaleX = startScale
                        scaleY = startScale
                    }
                ) {
                    Icon(
                        imageVector = startActionIcon,
                        contentDescription = "Pin Action",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // END ACTION (Right Icon)
                val endScale by animateFloatAsState(
                    targetValue = if (currentDirection == SwipeDismissDirection.EndToStart && isPastThreshold) 1.25f else 0.85f,
                    animationSpec = TactileMotionTokens.bouncySpring(),
                    label = "EndIconScale"
                )
                Box(
                    modifier = Modifier.graphicsLayer {
                        scaleX = endScale
                        scaleY = endScale
                    }
                ) {
                    Icon(
                        imageVector = endActionIcon,
                        contentDescription = "Delete Action",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // FOREGROUND INTERACTIVE CARD ROW
            Surface(
                shape = ShapeCache.smooth20,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            coroutineScope.launch {
                                // Rubber-band elasticity formula
                                val current = offsetX.value
                                val resistance = (1f - (abs(current) / (thresholdPx * 3f))).coerceIn(0.25f, 0.7f)
                                val newOffset = current + (delta * resistance)
                                offsetX.snapTo(newOffset)
                            }
                        },
                        onDragStopped = {
                            coroutineScope.launch {
                                if (isPastThreshold) {
                                    val dismissedDir = currentDirection
                                    val targetOffscreen = if (dismissedDir == SwipeDismissDirection.StartToEnd) 1000f else -1000f

                                    // 1. FIRST smoothly slide the card all the way off-screen!
                                    offsetX.animateTo(
                                        targetValue = targetOffscreen,
                                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                                    )

                                    // 2. ONLY AFTER slide finishes, trigger dismissal collapse & callback!
                                    isDismissed = true
                                    onDismissed(dismissedDir)
                                } else {
                                    // Spring rebound back to center
                                    offsetX.animateTo(
                                        targetValue = 0f,
                                        animationSpec = TactileMotionTokens.bouncySpring()
                                    )
                                }
                            }
                        }
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                        shape = ShapeCache.smooth20
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}
