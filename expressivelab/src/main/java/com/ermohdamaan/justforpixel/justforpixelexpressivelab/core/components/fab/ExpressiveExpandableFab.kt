package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.fab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Mic
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
 * Size variants according to Material Design 3 FAB specifications.
 *
 * @author Er. Mohd Amaan
 */
enum class ExpressiveFabSize(val sizeDp: Dp, val iconSizeDp: Dp) {
    /** Small FAB (40dp container, 20dp icon) */
    Small(40.dp, 20.dp),
    /** Standard FAB (56dp container, 24dp icon) */
    Standard(56.dp, 24.dp),
    /** Large FAB (96dp container, 36dp icon) */
    Large(96.dp, 36.dp)
}

/**
 * Data model representing a secondary speed dial action item inside [ExpressiveExpandableFab].
 *
 * @property id Unique identifier for the action item
 * @property icon Vector icon to display
 * @property label Text description or action title
 * @property containerColor Optional custom background color
 * @property contentColor Optional custom icon color
 *
 * @author Er. Mohd Amaan
 */
data class ExpressiveFabActionItem(
    val id: String,
    val icon: ImageVector,
    val label: String,
    val containerColor: Color? = null,
    val contentColor: Color? = null
)

/**
 * Orientation modes for speed dial expansion inside [ExpressiveExpandableFab].
 *
 * @author Er. Mohd Amaan
 */
enum class FabExpansionMode {
    /** Speed dial actions expand vertically above the main FAB */
    VerticalSpeedDial,
    /** Speed dial actions expand horizontally into a floating pill bar */
    HorizontalPillBar
}

/**
 * Pre-defined speed dial action sets for quick implementation.
 *
 * @author Er. Mohd Amaan
 */
object ExpressiveFabDefaults {

    /** Media creation cluster (Camera, Image, Audio, Note) */
    val CreationCluster = listOf(
        ExpressiveFabActionItem("camera", Icons.Rounded.CameraAlt, "Take Photo"),
        ExpressiveFabActionItem("gallery", Icons.Rounded.Image, "Attach Image"),
        ExpressiveFabActionItem("audio", Icons.Rounded.Mic, "Voice Note"),
        ExpressiveFabActionItem("note", Icons.Rounded.Edit, "New Note")
    )
}

/**
 * Material 3 Expressive Morphing Speed Dial Floating Action Button.
 *
 * Upgraded to match Official Material Design 3 FAB Specifications:
 * - Supports Standard (56dp), Small (40dp), Large (96dp), and Extended (Icon + Text) FAB states.
 * - Collapsed Hero FAB morphs from Squircle (20dp) to expanded Pill (50dp) upon activation.
 * - Icon rotates 135° smoothly from Add (+) to Close (X) on expansion.
 * - Supports both [FabExpansionMode.VerticalSpeedDial] and [FabExpansionMode.HorizontalPillBar].
 * - Bouncy spring physics on touch press + tactile haptic feedback.
 *
 * @param actions List of [ExpressiveFabActionItem] to display upon expansion
 * @param onActionClick Callback triggered when a speed dial action item is tapped
 * @param modifier Custom modifier
 * @param fabSize Choice of [ExpressiveFabSize] (Small, Standard, Large)
 * @param extendedLabel Optional extended text label (e.g. "Create New") for Extended FAB
 * @param expansionMode Expansion layout orientation ([FabExpansionMode.VerticalSpeedDial] or [FabExpansionMode.HorizontalPillBar])
 * @param mainIcon Hero FAB icon in collapsed state (default Add +)
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveExpandableFab(
    actions: List<ExpressiveFabActionItem>,
    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    fabSize: ExpressiveFabSize = ExpressiveFabSize.Standard,
    extendedLabel: String? = null,
    expansionMode: FabExpansionMode = FabExpansionMode.VerticalSpeedDial,
    mainIcon: ImageVector = Icons.Rounded.Add
) {
    var isExpanded by remember { mutableStateOf(false) }
    val hapticFeedback = LocalHapticFeedback.current

    // Rotation animation for main icon (0° when collapsed ➔ 135° rotation when expanded)
    val iconRotation by animateFloatAsState(
        targetValue = if (isExpanded) 135f else 0f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "FabIconRotation"
    )

    // Main FAB Background Color Animation
    val mainFabColor by animateColorAsState(
        targetValue = if (isExpanded) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary,
        animationSpec = TactileMotionTokens.gentleSpring(),
        label = "FabMainBgColor"
    )

    // Main FAB Icon Color Animation
    val mainIconColor by animateColorAsState(
        targetValue = if (isExpanded) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary,
        animationSpec = TactileMotionTokens.gentleSpring(),
        label = "FabMainIconColor"
    )

    // Main FAB Touch Press Scale
    val mainInteractionSource = remember { MutableInteractionSource() }
    val isMainPressed by mainInteractionSource.collectIsPressedAsState()
    val fabScale by animateFloatAsState(
        targetValue = if (isMainPressed) TactileMotionTokens.PRESS_SCALE_FAB else 1.0f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "FabPressScale"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        if (expansionMode == FabExpansionMode.VerticalSpeedDial) {
            // VERTICAL SPEED DIAL EXPANSION
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Secondary Speed Dial Action Items
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + scaleIn(animationSpec = TactileMotionTokens.bouncySpring()),
                    exit = fadeOut() + scaleOut()
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        actions.forEach { action ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                // Action Item Label Pill
                                Surface(
                                    shape = ShapeCache.smooth12,
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    shadowElevation = 3.dp,
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = action.label,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }

                                // Action Item Circle Button
                                Surface(
                                    shape = CircleShape,
                                    color = action.containerColor ?: MaterialTheme.colorScheme.secondaryContainer,
                                    shadowElevation = 4.dp,
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                            isExpanded = false
                                            onActionClick(action.id)
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = action.icon,
                                            contentDescription = action.label,
                                            tint = action.contentColor ?: MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // HERO MAIN FAB BUTTON (Supports Extended Label)
                Surface(
                    shape = if (extendedLabel != null && !isExpanded) ShapeCache.smoothPill else ShapeCache.smooth20,
                    color = mainFabColor,
                    shadowElevation = 8.dp,
                    tonalElevation = 6.dp,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = fabScale
                            scaleY = fabScale
                        }
                        .size(
                            width = if (extendedLabel != null && !isExpanded) Dp.Unspecified else fabSize.sizeDp,
                            height = fabSize.sizeDp
                        )
                        .clip(if (extendedLabel != null && !isExpanded) ShapeCache.smoothPill else ShapeCache.smooth20)
                        .clickable(
                            interactionSource = mainInteractionSource,
                            indication = null,
                            onClick = {
                                hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                isExpanded = !isExpanded
                            }
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = if (extendedLabel != null && !isExpanded) 18.dp else 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = mainIcon,
                            contentDescription = if (isExpanded) "Close Menu" else "Expand Menu",
                            tint = mainIconColor,
                            modifier = Modifier
                                .size(fabSize.iconSizeDp)
                                .graphicsLayer {
                                    rotationZ = iconRotation
                                }
                        )

                        if (extendedLabel != null && !isExpanded) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = extendedLabel,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = mainIconColor
                            )
                        }
                    }
                }
            }
        } else {
            // HORIZONTAL PILL BAR EXPANSION
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + scaleIn(animationSpec = TactileMotionTokens.bouncySpring()),
                    exit = fadeOut() + scaleOut()
                ) {
                    Surface(
                        shape = ShapeCache.smoothPill,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                ShapeCache.smoothPill
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            actions.forEach { action ->
                                Surface(
                                    shape = CircleShape,
                                    color = action.containerColor ?: MaterialTheme.colorScheme.secondaryContainer,
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                            isExpanded = false
                                            onActionClick(action.id)
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = action.icon,
                                            contentDescription = action.label,
                                            tint = action.contentColor ?: MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // HERO MAIN FAB BUTTON
                Surface(
                    shape = if (extendedLabel != null && !isExpanded) ShapeCache.smoothPill else ShapeCache.smooth20,
                    color = mainFabColor,
                    shadowElevation = 8.dp,
                    tonalElevation = 6.dp,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = fabScale
                            scaleY = fabScale
                        }
                        .size(
                            width = if (extendedLabel != null && !isExpanded) Dp.Unspecified else fabSize.sizeDp,
                            height = fabSize.sizeDp
                        )
                        .clip(if (extendedLabel != null && !isExpanded) ShapeCache.smoothPill else ShapeCache.smooth20)
                        .clickable(
                            interactionSource = mainInteractionSource,
                            indication = null,
                            onClick = {
                                hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                isExpanded = !isExpanded
                            }
                        )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = if (extendedLabel != null && !isExpanded) 18.dp else 0.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = mainIcon,
                            contentDescription = if (isExpanded) "Close Menu" else "Expand Menu",
                            tint = mainIconColor,
                            modifier = Modifier
                                .size(fabSize.iconSizeDp)
                                .graphicsLayer {
                                    rotationZ = iconRotation
                                }
                        )

                        if (extendedLabel != null && !isExpanded) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = extendedLabel,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = mainIconColor
                            )
                        }
                    }
                }
            }
        }
    }
}
