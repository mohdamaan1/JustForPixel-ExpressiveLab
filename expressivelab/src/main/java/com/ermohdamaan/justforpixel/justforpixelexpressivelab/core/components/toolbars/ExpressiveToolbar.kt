package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.toolbars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.material.icons.rounded.FormatItalic
import androidx.compose.material.icons.rounded.FormatListBulleted
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.FormatUnderlined
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

/**
 * Data model for an Expressive Toolbar Action Item.
 *
 * @property id Unique identifier for the toolbar item
 * @property icon Vector icon displayed in idle or active state
 * @property selectedIcon Optional distinct icon when item is toggled active
 * @property label Tooltip or accessible content description
 * @property isSelected Current toggle active state
 * @property badgeText Optional badge label or count overlay
 *
 * @author Er. Mohd Amaan
 */
data class ExpressiveToolbarItem(
    val id: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector? = null,
    val label: String,
    val isSelected: Boolean = false,
    val badgeText: String? = null
)

/**
 * Pre-configured sets of toolbar action items for quick implementation.
 *
 * @author Er. Mohd Amaan
 */
object ExpressiveToolbarDefaults {

    /** Rich Text Format Editor Preset (Bold, Italic, Underline, Bullet List, Quote, Link) */
    val RichTextPreset = listOf(
        ExpressiveToolbarItem("bold", Icons.Rounded.FormatBold, label = "Bold"),
        ExpressiveToolbarItem("italic", Icons.Rounded.FormatItalic, label = "Italic"),
        ExpressiveToolbarItem("underline", Icons.Rounded.FormatUnderlined, label = "Underline"),
        ExpressiveToolbarItem("list", Icons.Rounded.FormatListBulleted, label = "Bullet List"),
        ExpressiveToolbarItem("quote", Icons.Rounded.FormatQuote, label = "Block Quote"),
        ExpressiveToolbarItem("link", Icons.Rounded.Link, label = "Hyperlink")
    )
}

/**
 * Individual Expressive Toggle Action Button with shape morphing & spring physics.
 *
 * When active, morphs corner radius from Circle (48dp) to Squircle (14dp),
 * with spring scale compression on press and tactile haptic feedback.
 *
 * @param item Toolbar item configuration data
 * @param isSelected Whether the toggle is currently active
 * @param onToggleClick Callback triggered when item is tapped
 * @param modifier Custom modifier
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveToggleButton(
    item: ExpressiveToolbarItem,
    isSelected: Boolean,
    onToggleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val hapticFeedback = LocalHapticFeedback.current

    // Bouncy scale physics animation on tap
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> TactileMotionTokens.PRESS_SCALE_FAB
            isSelected -> 1.08f
            else -> 1.0f
        },
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "ExpressiveToggleScale"
    )

    // Smooth corner radius morphing animation (Circle 48dp -> Squircle 14dp)
    val cornerRadius by animateDpAsState(
        targetValue = if (isSelected) 14.dp else 24.dp,
        animationSpec = TactileMotionTokens.gentleSpring(),
        label = "ExpressiveToggleCorner"
    )

    // Background color animation
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        animationSpec = TactileMotionTokens.gentleSpring(),
        label = "ExpressiveToggleBg"
    )

    // Icon tint color animation
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = TactileMotionTokens.gentleSpring(),
        label = "ExpressiveToggleIconColor"
    )

    val currentIcon = if (isSelected && item.selectedIcon != null) item.selectedIcon else item.icon

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .size(44.dp)
            .clip(ShapeCache.smooth16)
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                    onToggleClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (item.badgeText != null) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ) {
                        Text(text = item.badgeText, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            ) {
                Icon(
                    imageVector = currentIcon,
                    contentDescription = item.label,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        } else {
            Icon(
                imageVector = currentIcon,
                contentDescription = item.label,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/**
 * Floating Expressive Horizontal Toolbar Wrapper.
 *
 * Combining a floating pill container with interactive [ExpressiveToggleButton] controls.
 * Features background blur/elevation, squircle container edges, and optional expandable overflow actions.
 *
 * @param items List of [ExpressiveToolbarItem] to render in the toolbar
 * @param selectedIds Set of currently toggled item IDs
 * @param onItemToggle Callback when any item is toggled
 * @param modifier Custom modifier
 * @param containerColor Background color of the toolbar pill
 * @param elevation Floating depth elevation
 * @param allowOverflow Whether to show an expandable overflow button
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveHorizontalToolbar(
    items: List<ExpressiveToolbarItem>,
    selectedIds: Set<String>,
    onItemToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    elevation: Dp = 6.dp,
    allowOverflow: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(false) }
    val hapticFeedback = LocalHapticFeedback.current

    val visibleItems = if (allowOverflow && !isExpanded && items.size > 4) items.take(4) else items

    Surface(
        shape = ShapeCache.smoothPill,
        color = containerColor,
        tonalElevation = elevation,
        shadowElevation = elevation,
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                shape = ShapeCache.smoothPill
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            visibleItems.forEach { item ->
                val isSelected = selectedIds.contains(item.id) || item.isSelected
                ExpressiveToggleButton(
                    item = item,
                    isSelected = isSelected,
                    onToggleClick = { onItemToggle(item.id) }
                )
            }

            if (allowOverflow && items.size > 4) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isExpanded) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
                        .clickable {
                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                            isExpanded = !isExpanded
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreHoriz,
                        contentDescription = "More Actions",
                        tint = if (isExpanded) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
