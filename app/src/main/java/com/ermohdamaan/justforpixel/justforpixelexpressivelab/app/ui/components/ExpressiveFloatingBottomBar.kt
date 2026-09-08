package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import racra.compose.smooth_corner_rect_library.AbsoluteSmoothCornerShape

/**
 * Centered Material 3 Expressive Floating Bottom Navigation Bar.
 *
 * Features:
 * - On-Click Elastic Bouncy Press Scale & Corner Radius Morphing.
 * - Fast zero-lag 60fps tab switching.
 * - Zero rectangular shadow artifacts behind pill.
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveFloatingBottomBar(
    selectedTab: Int, // 0 = Catalog, 1 = Settings
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            shadowElevation = 8.dp,
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 5.dp)
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Tab 0: Catalog
                FloatingNavItem(
                    label = "Catalog",
                    icon = Icons.Rounded.GridView,
                    selected = selectedTab == 0,
                    onClick = {
                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                        onTabSelected(0)
                    }
                )

                Spacer(modifier = Modifier.width(4.dp))

                // Tab 1: Settings
                FloatingNavItem(
                    label = "Settings",
                    icon = Icons.Rounded.Settings,
                    selected = selectedTab == 1,
                    onClick = {
                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                        onTabSelected(1)
                    }
                )
            }
        }
    }
}

@Composable
private fun FloatingNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Bouncy Scale Compression Physics
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.86f else 1.0f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "NavItemScaleAnim"
    )

    // Corner Morphing Physics between Squircle (12dp) on press & Pill (25dp) on idle
    val cornerRadius by animateDpAsState(
        targetValue = if (isPressed) 12.dp else 25.dp,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "NavItemCornerMorphAnim"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        animationSpec = tween(140),
        label = "BgColorAnim"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(140),
        label = "ContentColorAnim"
    )

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                clip = true
                shape = AbsoluteSmoothCornerShape(cornerRadius = cornerRadius, smoothnessAsPercent = 60)
            }
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = if (selected) 18.dp else 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )

            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(animationSpec = tween(120)) + expandHorizontally(animationSpec = tween(140)),
                exit = fadeOut(animationSpec = tween(100)) + shrinkHorizontally(animationSpec = tween(120))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = contentColor,
                        maxLines = 1,
                        fontSize = 13.5.sp
                    )
                }
            }
        }
    }
}
