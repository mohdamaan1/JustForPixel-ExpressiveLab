package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

/**
 * Data model for progress metrics inside [WavyProgressCard].
 *
 * @property currentProgress Current progress value from 0.0f to 1.0f
 * @property progressPercentage Display percentage string (e.g. "78%")
 * @property primaryStatLabel Label for main progress stat (e.g. "6.2 / 8.0 Hours")
 * @property targetStatLabel Label for target goal stat (e.g. "Goal: 8.0 Hours")
 * @property remainingStatLabel Label for remaining metric (e.g. "1.8 Hours Left")
 *
 * @author Er. Mohd Amaan
 */
data class WavyProgressCardStats(
    val currentProgress: Float = 0.78f,
    val progressPercentage: String = "78%",
    val primaryStatLabel: String = "6.2 / 8.0 Hrs",
    val targetStatLabel: String = "Goal: 8.0 Hrs",
    val remainingStatLabel: String = "1.8 Hrs Left"
)

/**
 * Material 3 Expressive Dashboard Progress Card Component.
 *
 * Combines a hero [CircularWavyProgressIndicator] with centered percentage readouts,
 * a 3-metric progress stats grid, and interactive play/pause controls.
 *
 * @param title Title header text of the card
 * @param subtitle Subtitle description of the card
 * @param stats Progress metrics payload ([WavyProgressCardStats])
 * @param isRunning Active timer/animation state
 * @param onPlayPauseClick Callback when play/pause control button is tapped
 * @param modifier Custom modifier
 * @param containerColor Card background container color
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WavyProgressCard(
    title: String,
    subtitle: String,
    stats: WavyProgressCardStats,
    isRunning: Boolean,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh
) {
    val hapticFeedback = LocalHapticFeedback.current
    val density = LocalDensity.current

    val ringStrokePx = with(density) { 10.dp.toPx() }
    val stroke = remember(ringStrokePx) { Stroke(width = ringStrokePx, cap = StrokeCap.Round) }

    Surface(
        shape = ShapeCache.smooth20,
        color = containerColor,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                shape = ShapeCache.smooth20
            )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row: Title, Subtitle & Play/Pause Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Interactive Play/Pause Control Button with Tactile Haptics
                IconButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                        onPlayPauseClick()
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (isRunning) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = if (isRunning) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Play",
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Hero Progress Centerpiece (Wavy Progress Ring + Center Percentage Text)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isRunning) {
                    CircularWavyProgressIndicator(
                        progress = { stats.currentProgress },
                        modifier = Modifier.size(130.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        stroke = stroke,
                        trackStroke = stroke
                    )
                } else {
                    CircularProgressIndicator(
                        progress = { stats.currentProgress },
                        modifier = Modifier.size(130.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 10.dp
                    )
                }

                // Centered Percentage & Stat Text
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stats.progressPercentage,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isRunning) "Active" else "Paused",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 3-Metric Stats Footer Grid Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(ShapeCache.smooth16)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatMetricBox(
                    icon = Icons.Rounded.TrendingUp,
                    label = "Completed",
                    value = stats.primaryStatLabel,
                    accentColor = MaterialTheme.colorScheme.primary
                )

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(28.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                )

                StatMetricBox(
                    icon = Icons.Rounded.Timer,
                    label = "Remaining",
                    value = stats.remainingStatLabel,
                    accentColor = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun StatMetricBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    accentColor: Color
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
