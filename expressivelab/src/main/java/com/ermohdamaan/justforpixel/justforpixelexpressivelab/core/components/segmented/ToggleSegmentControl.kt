package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.segmented

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import kotlinx.coroutines.delay

/**
 * Interactive Segmented Control Item with Layout Weight Push Physics & Corner Morphing.
 *
 * Tapping a segment item expands its weight (1.28f) while compressing adjacent items (0.70f),
 * creating an elastic "dhakka/push" physics effect while morphing corners from Pill to Squircle.
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ToggleSegmentItem(
    active: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    activeCornerRadius: Dp = 12.dp,
    inactiveCornerRadius: Dp = 24.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    activeTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    inactiveTextColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val hapticFeedback = LocalHapticFeedback.current

    val bgColor by animateColorAsState(
        targetValue = if (active) activeColor else inactiveColor,
        animationSpec = tween(220),
        label = "BgColorAnim"
    )

    val cornerRadius by animateDpAsState(
        targetValue = if (active) activeCornerRadius else inactiveCornerRadius,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "CornerRadiusAnim"
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(bgColor)
            .clickable {
                hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                onClick()
            }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = if (active) activeTextColor else inactiveTextColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                color = if (active) activeTextColor else inactiveTextColor,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (active) FontWeight.ExtraBold else FontWeight.Bold,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Container Row with Interactive Weight Morphing Push Physics for Segment Items.
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ToggleSegmentRow(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
) {
    var lastClickedIndex by remember { mutableStateOf<Int?>(null) }
    var clickTrigger by remember { mutableStateOf(0) }

    // Auto-reset clicked button weight state after elastic press delay
    LaunchedEffect(lastClickedIndex, clickTrigger) {
        if (lastClickedIndex != null) {
            delay(320L)
            lastClickedIndex = null
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(containerColor)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEachIndexed { index, title ->
            val targetWeight = when {
                lastClickedIndex == index -> 1.30f // Expand tapped item
                lastClickedIndex != null -> 0.70f  // Compress adjacent items ("dhakka/push" effect)
                index == selectedIndex -> 1.15f    // Active item slightly wider
                else -> 0.90f
            }

            val weightAnim by animateFloatAsState(
                targetValue = targetWeight,
                animationSpec = TactileMotionTokens.bouncySpring(),
                label = "SegmentWeightPushAnim"
            )

            ToggleSegmentItem(
                active = index == selectedIndex,
                onClick = {
                    lastClickedIndex = index
                    clickTrigger++
                    onOptionSelected(index)
                },
                label = title,
                modifier = Modifier
                    .weight(weightAnim)
                    .fillMaxHeight()
            )
        }
    }
}
