package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.buttons

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import racra.compose.smooth_corner_rect_library.AbsoluteSmoothCornerShape

/**
 * Single Icon Button with Expressive Corner Morphing and Elastic Press Physics.
 *
 * @param onClick Action triggered on click
 * @param icon Vector drawable to display inside the button
 * @param contentDescription Accessibility label
 * @param modifier Custom modifier
 * @param active Whether the button is currently in an active/toggled state
 * @param size Outer container size
 * @param iconSize Inner icon size
 * @param activeCornerRadius Corner radius when active (e.g., 16.dp for squircle)
 * @param inactiveCornerRadius Corner radius when inactive (e.g., 32.dp for circle/pill)
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MorphingIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    size: Dp = 64.dp,
    iconSize: Dp = 30.dp,
    activeCornerRadius: Dp = 16.dp,
    inactiveCornerRadius: Dp = 32.dp,
    activeContainerColor: Color = MaterialTheme.colorScheme.primary,
    inactiveContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    activeIconTint: Color = MaterialTheme.colorScheme.onPrimary,
    inactiveIconTint: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val hapticFeedback = LocalHapticFeedback.current

    val motionScheme = remember { MotionScheme.expressive() }
    val spatialSpec = remember { motionScheme.defaultSpatialSpec<Dp>() }

    // Animate scale on press
    val scale by animateFloatAsState(
        targetValue = if (isPressed) TactileMotionTokens.PRESS_SCALE_FAB else 1.0f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "buttonScaleAnim"
    )

    // Morph corner radius based on active/inactive state
    val cornerRadius by animateDpAsState(
        targetValue = if (active) activeCornerRadius else inactiveCornerRadius,
        animationSpec = spatialSpec,
        label = "cornerRadiusAnim"
    )

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                clip = true
                shape = AbsoluteSmoothCornerShape(
                    cornerRadius = cornerRadius,
                    smoothnessAsPercent = 60
                )
            }
            .background(if (active) activeContainerColor else inactiveContainerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (active) activeIconTint else inactiveIconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}
