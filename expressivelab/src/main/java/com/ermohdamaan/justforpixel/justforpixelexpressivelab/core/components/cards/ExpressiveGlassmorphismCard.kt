package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.cards

import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

/**
 * Material 3 Expressive Glassmorphic Card built using Official Compose Material 3 APIs.
 *
 * Separates the background frost glass layer from the foreground text content layer.
 * Setting [blurBehindContent] = true applies [blur] ONLY to the background glass surface,
 * ensuring text, icons, and buttons remain 100% crystal clear, sharp, and readable.
 *
 * @param onClick Optional callback when card is tapped
 * @param modifier Custom modifier
 * @param shape Card shape geometry (default [ShapeCache.smooth20])
 * @param backdropBlurRadius Glass backdrop blur depth (default 16.dp on Android 12+)
 * @param blurBehindContent When true, blurs ONLY the background glass layer keeping text 100% sharp.
 * @param frostAlpha Glass translucency alpha transparency (0.1f to 0.9f)
 * @param borderAlpha Glossy edge reflection outline alpha
 * @param content Composable slot inside the card
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveGlassmorphismCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = ShapeCache.smooth20,
    backdropBlurRadius: Dp = 16.dp,
    blurBehindContent: Boolean = true,
    frostAlpha: Float = 0.45f,
    borderAlpha: Float = 0.35f,
    content: @Composable BoxScope.() -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Bouncy press scale animation using TactileMotionTokens
    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) TactileMotionTokens.PRESS_SCALE_CARD else 1.0f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "GlassCardScale"
    )

    // Dynamic Material You Gradient Overlay (Primary to Tertiary frost tint)
    val glassGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = frostAlpha),
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = (frostAlpha * 0.6f)),
            MaterialTheme.colorScheme.surface.copy(alpha = frostAlpha)
        )
    )

    // Glossy Edge Highlight Border Brush (White / Primary outline)
    val glossyBorder = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = borderAlpha),
            MaterialTheme.colorScheme.primary.copy(alpha = borderAlpha * 0.7f),
            Color.White.copy(alpha = borderAlpha * 0.2f)
        )
    )

    // Official Compose Material 3 Card Container
    Card(
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 8.dp else 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(shape)
            .border(width = 1.2.dp, brush = glossyBorder, shape = shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = {
                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                            onClick()
                        }
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // LAYER 1: BACKDROP GLASS BACKGROUND (Blurs ONLY the background when blurBehindContent = true)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .then(
                        if (blurBehindContent && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Modifier.blur(radius = backdropBlurRadius)
                        } else {
                            Modifier
                        }
                    )
                    .background(brush = glassGradient)
            )

            // LAYER 2: FOREGROUND CONTENT (Kept 100% sharp, crisp, and un-blurred when blurBehindContent = true)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (!blurBehindContent && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            Modifier.blur(radius = backdropBlurRadius)
                        } else {
                            Modifier
                        }
                    )
                    .padding(20.dp),
                content = content
            )
        }
    }
}
