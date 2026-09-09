package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.heatmap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

/**
 * Enum defining customizable expressive tile geometries for [M3Heatmap].
 *
 * @author Er. Mohd Amaan
 */
enum class HeatmapTileShape {
    /** Smooth squircle curvature (60% smoothness) */
    Squircle,
    /** Organic pill pebble shape */
    PebblePill,
    /** Rotated diamond squircle geometry */
    Diamond,
    /** Radiant circular glow tile */
    GlowCircle
}

/**
 * Data model for an individual cell in the [M3Heatmap] grid.
 *
 * @property dayIndex Day of the week index (0 = Monday, 6 = Sunday)
 * @property weekIndex Week column index
 * @property count Numerical activity count (e.g. 0 pomodoros, 15 commits, 50 points)
 * @property level Computed intensity level from 0 (empty) to 4 (maximum radiant intensity)
 * @property dateLabel Formatted date label string (e.g. "Feb 14, 2025")
 *
 * @author Er. Mohd Amaan
 */
data class HeatmapDayData(
    val dayIndex: Int,
    val weekIndex: Int,
    val count: Int,
    val level: Int, // 0..4
    val dateLabel: String
)

/**
 * Pre-defined sample dataset generators for [M3Heatmap].
 *
 * @author Er. Mohd Amaan
 */
object HeatmapDefaults {

    /** Generate mock activity heatmap data for 16 weeks (112 days) */
    fun generateSampleData(weeksCount: Int = 16): List<List<HeatmapDayData>> {
        val months = listOf("Jan", "Feb", "Mar", "Apr")
        return List(weeksCount) { weekIdx ->
            List(7) { dayIdx ->
                val randomValue = (0..20).random()
                val calculatedLevel = when {
                    randomValue == 0 -> 0
                    randomValue <= 4 -> 1
                    randomValue <= 9 -> 2
                    randomValue <= 14 -> 3
                    else -> 4
                }
                val month = months[(weekIdx / 4) % months.size]
                HeatmapDayData(
                    dayIndex = dayIdx,
                    weekIndex = weekIdx,
                    count = randomValue,
                    level = calculatedLevel,
                    dateLabel = "$month ${((weekIdx * 7) + dayIdx) % 28 + 1}"
                )
            }
        }
    }
}

/**
 * Individual Activity Heatmap Cell Tile with customizable expressive shape geometries,
 * radiant intensity glow effects, bouncy spring physics & interactive tooltips.
 *
 * @param day Data entry for this cell
 * @param isSelected Whether cell is currently selected/tapped
 * @param tileShape Custom [HeatmapTileShape] geometry option
 * @param onClick Triggered when cell is tapped
 * @param modifier Custom modifier
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun M3HeatmapCell(
    day: HeatmapDayData,
    isSelected: Boolean,
    tileShape: HeatmapTileShape = HeatmapTileShape.Squircle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val hapticFeedback = LocalHapticFeedback.current

    // Bouncy scale physics animation on press
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> TactileMotionTokens.PRESS_SCALE_FAB
            isSelected -> 1.30f
            else -> 1.0f
        },
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "HeatmapCellScale"
    )

    // Resolve tile shape geometry
    val resolvedShape: Shape = when (tileShape) {
        HeatmapTileShape.Squircle -> ShapeCache.smooth8
        HeatmapTileShape.PebblePill -> RoundedCornerShape(10.dp)
        HeatmapTileShape.Diamond -> RoundedCornerShape(4.dp)
        HeatmapTileShape.GlowCircle -> CircleShape
    }

    // Material 3 Expressive Color Gradient & Tinting
    val baseTileColor = when (day.level) {
        0 -> MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.30f)
        1 -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.50f)
        2 -> MaterialTheme.colorScheme.secondary
        3 -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.primary
    }

    val tileColor by animateColorAsState(
        targetValue = baseTileColor,
        animationSpec = TactileMotionTokens.gentleSpring(),
        label = "HeatmapCellColor"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.secondary
            day.level == 4 -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f)
            else -> Color.Transparent
        },
        animationSpec = TactileMotionTokens.gentleSpring(),
        label = "HeatmapCellBorder"
    )

    val isPeakDay = day.level == 4

    Box(
        modifier = modifier
            .padding(2.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                if (tileShape == HeatmapTileShape.Diamond) {
                    rotationZ = 45f
                }
            }
            .size(17.dp)
            .then(
                if (isPeakDay) Modifier.shadow(4.dp, resolvedShape) else Modifier
            )
            .clip(resolvedShape)
            .border(1.5.dp, borderColor, resolvedShape)
            .background(tileColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                    onClick()
                }
            )
    )
}

/**
 * Material 3 Expressive Contribution & Activity Heatmap Component.
 *
 * Features:
 * - 4 Expressive Tile Shape Geometries ([HeatmapTileShape.Squircle], [HeatmapTileShape.PebblePill], [HeatmapTileShape.Diamond], [HeatmapTileShape.GlowCircle]).
 * - Peak Day Radiant Glow Effects with elevation depth.
 * - Interactive Streak Summary Fire Badge & Date Tooltip Cards.
 * - Responsive 100% original weekly column layout.
 *
 * @param weeksData Matrix of weekly data columns (each column containing 7 days)
 * @param modifier Custom modifier
 * @param title Header title string
 * @param currentStreak Current streak count in days
 * @param totalContributions Total overall activity sum
 * @param tileShape Choice of [HeatmapTileShape] for cell rendering
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun M3Heatmap(
    weeksData: List<List<HeatmapDayData>>,
    modifier: Modifier = Modifier,
    title: String = "Activity Heatmap",
    currentStreak: Int = 12,
    totalContributions: Int = 348,
    tileShape: HeatmapTileShape = HeatmapTileShape.Squircle
) {
    var selectedDay by remember { mutableStateOf<HeatmapDayData?>(null) }
    val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeCache.smooth20)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        // Header Row with Title, Streak Counter Badge, and Total Count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$totalContributions Total Contributions",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = ShapeCache.smoothPill,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$currentStreak Day Streak",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Heatmap Grid Row (Day Labels + Weekly Columns)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Day Labels Column (M, T, W, T, F, S, S)
            Column(
                modifier = Modifier.padding(end = 6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                dayLabels.forEach { label ->
                    Box(
                        modifier = Modifier.size(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Scrollable Weekly Columns Grid
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                contentPadding = PaddingValues(horizontal = 2.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(weeksData) { weekIdx, weekDays ->
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        weekDays.forEach { dayData ->
                            M3HeatmapCell(
                                day = dayData,
                                isSelected = selectedDay == dayData,
                                tileShape = tileShape,
                                onClick = {
                                    selectedDay = if (selectedDay == dayData) null else dayData
                                }
                            )
                        }
                    }
                }
            }
        }

        // Interactive Tooltip Card on Tile Selection
        AnimatedVisibility(
            visible = selectedDay != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            selectedDay?.let { day ->
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = ShapeCache.smooth12,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (day.level) {
                                            0 -> MaterialTheme.colorScheme.outline
                                            else -> MaterialTheme.colorScheme.primary
                                        }
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${day.count} Activities on ${day.dateLabel}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = "Level ${day.level}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Legend Bar (Less ➔ More)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Less",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                Box(Modifier.size(10.dp).clip(ShapeCache.smooth8).background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.35f)))
                Box(Modifier.size(10.dp).clip(ShapeCache.smooth8).background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.50f)))
                Box(Modifier.size(10.dp).clip(ShapeCache.smooth8).background(MaterialTheme.colorScheme.secondary))
                Box(Modifier.size(10.dp).clip(ShapeCache.smooth8).background(MaterialTheme.colorScheme.primary))
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "More",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
