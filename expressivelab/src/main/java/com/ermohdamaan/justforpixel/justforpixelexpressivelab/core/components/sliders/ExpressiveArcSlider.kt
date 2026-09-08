package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.sliders

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Curved Arc Wavy Progress Slider for Expressive Material 3 UI.
 *
 * Renders an interactive arc slider with tactile feedback, active sweep angle animation,
 * and circular thumb indicator tracking the curved path.
 *
 * @param progress Value from 0.0f to 1.0f
 * @param onProgressChange Callback triggered on touch drag along the arc
 * @param modifier Custom modifier
 * @param startAngle Angle in degrees where the arc begins (default 140° for bottom arc)
 * @param sweepAngle Total sweep angle of the arc in degrees (default 260°)
 * @param strokeWidth Arc line stroke width
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveArcSlider(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    startAngle: Float = 140f,
    sweepAngle: Float = 260f,
    strokeWidth: Dp = 12.dp,
    activeTrackColor: Color = MaterialTheme.colorScheme.primary,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    thumbColor: Color = MaterialTheme.colorScheme.primary
) {
    var isDragging by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = if (isDragging) tween(0) else TactileMotionTokens.gentleSpring(),
        label = "ArcProgressAnim"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false },
                        onDrag = { change, _ ->
                            change.consume()
                            val center = Offset(size.width / 2f, size.height / 2f)
                            val touch = change.position
                            val angleRad = atan2(touch.y - center.y, touch.x - center.x)
                            var angleDeg = Math.toDegrees(angleRad.toDouble()).toFloat()
                            if (angleDeg < 0) angleDeg += 360f

                            var normAngle = angleDeg - startAngle
                            if (normAngle < 0) normAngle += 360f

                            if (normAngle <= sweepAngle) {
                                val newProgress = (normAngle / sweepAngle).coerceIn(0f, 1f)
                                onProgressChange(newProgress)
                            }
                        }
                    )
                }
        ) {
            val strokePx = strokeWidth.toPx()
            val diameter = minOf(size.width, size.height) - strokePx
            val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
            val arcSize = Size(diameter, diameter)

            // Inactive Track Arc
            drawArc(
                color = inactiveTrackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // Active Progress Arc
            val activeSweep = sweepAngle * animatedProgress
            drawArc(
                color = activeTrackColor,
                startAngle = startAngle,
                sweepAngle = activeSweep,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx * 1.15f, cap = StrokeCap.Round)
            )

            // Calculate Thumb Position along the Arc Curve
            val thumbAngleRad = Math.toRadians((startAngle + activeSweep).toDouble())
            val radius = diameter / 2f
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val thumbX = centerX + radius * cos(thumbAngleRad).toFloat()
            val thumbY = centerY + radius * sin(thumbAngleRad).toFloat()

            // Draw Thumb Circle
            drawCircle(
                color = thumbColor,
                radius = strokePx * (if (isDragging) 1.2f else 0.9f),
                center = Offset(thumbX, thumbY)
            )
        }
    }
}
