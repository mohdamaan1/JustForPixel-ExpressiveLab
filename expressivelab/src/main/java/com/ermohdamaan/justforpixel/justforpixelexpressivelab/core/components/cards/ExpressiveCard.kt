package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.cards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

/**
 * Expressive Interactive Squircle Card with Bouncy Spring Physics & Tactile Feedback.
 *
 * @param onClick Triggered when card is tapped
 * @param modifier Custom modifier
 * @param containerColor Background color of the card
 * @param cornerRadius Smooth corner radius
 * @param elevation Idle elevation depth
 * @param content Composable slot inside the card
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 4.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val hapticFeedback = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) TactileMotionTokens.PRESS_SCALE_CARD else 1.0f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "CardScaleAnim"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                clip = true
                shape = ShapeCache.smooth20
            }
            .shadow(
                elevation = if (isPressed) elevation * 1.5f else elevation,
                shape = ShapeCache.smooth20
            )
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                    onClick()
                }
            )
            .padding(16.dp),
        content = content
    )
}
