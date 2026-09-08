package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.progress

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import racra.compose.smooth_corner_rect_library.AbsoluteSmoothCornerShape

/**
 * Official Google Material 3 Expressive Loading Indicator Component.
 * Uses official M3 Expressive [LoadingIndicator] API from androidx.compose.material3.
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OfficialExpressiveLoadingIndicator(
    modifier: Modifier = Modifier.size(48.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator(
            color = color
        )
    }
}

/**
 * Custom Material 3 Expressive Shape-Morphing Loading Indicator.
 * Continuously morphs its corner geometry between Circle, Smooth Squircle, Pill, and Diamond
 * while rotating infinitely with bouncy spring physics.
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveShapeMorphLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    innerColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ShapeMorphInfinite")

    // Continuous 360 degree Rotation
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotationAnim"
    )

    // Corner Radius Morphing Animation
    val cornerRadiusFraction by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CornerMorphAnim"
    )

    // Inner Core Pulsating Scale
    val coreScale by infiniteTransition.animateFloat(
        initialValue = 0.60f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CoreScaleAnim"
    )

    val currentCornerRadius = 8.dp + (24.dp * cornerRadiusFraction)

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                rotationZ = rotation
                scaleX = 0.90f + (0.15f * cornerRadiusFraction)
                scaleY = 0.90f + (0.15f * cornerRadiusFraction)
                clip = true
                shape = AbsoluteSmoothCornerShape(
                    cornerRadius = currentCornerRadius,
                    smoothnessAsPercent = 60
                )
            }
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        // Inner Pulsating Core Shape
        Box(
            modifier = Modifier
                .size(size * 0.35f)
                .graphicsLayer {
                    scaleX = coreScale
                    scaleY = coreScale
                }
                .clip(CircleShape)
                .background(innerColor)
        )
    }
}