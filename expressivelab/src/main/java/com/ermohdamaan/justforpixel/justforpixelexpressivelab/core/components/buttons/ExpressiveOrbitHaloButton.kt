package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.buttons

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AirplanemodeActive
import androidx.compose.material.icons.rounded.AirplanemodeInactive
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.BluetoothDisabled
import androidx.compose.material.icons.rounded.DoNotDisturbOff
import androidx.compose.material.icons.rounded.DoNotDisturbOn
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import racra.compose.smooth_corner_rect_library.AbsoluteSmoothCornerShape
import kotlin.math.cos
import kotlin.math.sin

/** Enum representing halo indicator animation styles for Quick Settings status buttons */
enum class ExpressiveHaloType {
    Wifi,          // Clockwise Arc Sweep + Sequential Dots
    DoNotDisturb,  // Crescent Moon Arc + Muted Star Pulse
    AirplaneMode,  // Flight Contrail Arc + Jet Sky Orbit
    Bluetooth,     // Interlocking Dual Wave Halos + Pairing Dots
    Music          // Equalizer Frequency Wave Ring + Beat Rhythm Orbit
}

/**
 * Refined Material 3 Expressive Orbit Status Button with 5 Custom Halo Styles.
 *
 * Inspired by Google Pixel / Nothing OS status bar connectivity & system indicators.
 * Supported Halo Types:
 * - [ExpressiveHaloType.Wifi]: Clockwise Arc Sweep + Sequential Dots.
 * - [ExpressiveHaloType.DoNotDisturb]: Crescent Moon Arc + Muted Star Pulse.
 * - [ExpressiveHaloType.AirplaneMode]: Flight Contrail Arc + Jet Sky Orbit.
 * - [ExpressiveHaloType.Bluetooth]: Interlocking Dual Wave Halos + Pairing Dots.
 * - [ExpressiveHaloType.Music]: Equalizer Frequency Wave Ring + Beat Rhythm Orbit.
 *
 * @param onClick Callback triggered on click
 * @param active Whether the halo and icon are in an active state
 * @param isSearching When true, plays a continuous clockwise orbit animation
 * @param haloType Visual halo animation preset ([ExpressiveHaloType])
 * @param modifier Custom modifier
 * @param icon Center icon vector (auto-selected based on [haloType] & [active])
 * @param size Outer container size (default 52.dp)
 * @param iconSize Inner icon size (default 22.dp)
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveOrbitHaloButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    active: Boolean = true,
    isSearching: Boolean = false,
    haloType: ExpressiveHaloType = ExpressiveHaloType.Wifi,
    icon: ImageVector = when (haloType) {
        ExpressiveHaloType.Wifi -> if (active) Icons.Rounded.Wifi else Icons.Rounded.WifiOff
        ExpressiveHaloType.DoNotDisturb -> if (active) Icons.Rounded.DoNotDisturbOn else Icons.Rounded.DoNotDisturbOff
        ExpressiveHaloType.AirplaneMode -> if (active) Icons.Rounded.AirplanemodeActive else Icons.Rounded.AirplanemodeInactive
        ExpressiveHaloType.Bluetooth -> if (active) Icons.Rounded.Bluetooth else Icons.Rounded.BluetoothDisabled
        ExpressiveHaloType.Music -> Icons.Rounded.MusicNote
    },
    size: Dp = 52.dp,
    iconSize: Dp = 22.dp,
    dotCount: Int = when (haloType) {
        ExpressiveHaloType.DoNotDisturb -> 4
        ExpressiveHaloType.AirplaneMode -> 3
        ExpressiveHaloType.Bluetooth -> 4
        else -> 5
    },
    showContainerBackground: Boolean = false,
    activeArcColor: Color = when (haloType) {
        ExpressiveHaloType.DoNotDisturb -> MaterialTheme.colorScheme.error
        ExpressiveHaloType.AirplaneMode -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    },
    activeDotColor: Color = when (haloType) {
        ExpressiveHaloType.DoNotDisturb -> MaterialTheme.colorScheme.error.copy(alpha = 0.85f)
        ExpressiveHaloType.AirplaneMode -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.tertiary
    },
    activeIconTint: Color = when (haloType) {
        ExpressiveHaloType.DoNotDisturb -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    },
    inactiveColor: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
    activeContainerColor: Color = when (haloType) {
        ExpressiveHaloType.DoNotDisturb -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    },
    inactiveContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    containerBackgroundColor: Color = Color.Unspecified
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val hapticFeedback = LocalHapticFeedback.current

    // Bouncy press scale animation
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) TactileMotionTokens.PRESS_SCALE_FAB else 1.0f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "HaloButtonPressScale"
    )

    // Center button corner morphing
    val buttonCornerRadius by animateFloatAsState(
        targetValue = if (active) 12f else 32f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "HaloButtonCorner"
    )

    // Clockwise Arc Sweep Progress (0.0f ➔ 1.0f)
    val arcProgress by animateFloatAsState(
        targetValue = if (active || isSearching) 1.0f else 0.0f,
        animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing),
        label = "HaloArcSweep"
    )

    // Sequential Clockwise Dots Progress (0.0f ➔ 1.0f)
    val dotsProgress by animateFloatAsState(
        targetValue = if (active || isSearching) 1.0f else 0.0f,
        animationSpec = tween(durationMillis = 750, delayMillis = 150, easing = FastOutSlowInEasing),
        label = "HaloDotsProgress"
    )

    // Continuous Orbit Rotation for Searching / Music Beat State
    val infiniteTransition = rememberInfiniteTransition(label = "HaloOrbitLoop")
    val orbitRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (haloType == ExpressiveHaloType.Music) 2400 else 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbitRotation"
    )

    val currentRotation = if (isSearching || (haloType == ExpressiveHaloType.Music && active)) orbitRotation else 0f

    Box(
        modifier = modifier
            .size(size)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        // OUTER ORBIT HALO CANVAS
        Canvas(
            modifier = Modifier
                .size(size)
                .graphicsLayer {
                    rotationZ = currentRotation
                }
        ) {
            val strokeWidthPx = 2.0.dp.toPx()
            val dotRadiusPx = 1.6.dp.toPx()
            val ringRadius = (this.size.minDimension - strokeWidthPx - (dotRadiusPx * 2.5f)) / 2f
            val canvasCenter = Offset(this.size.width / 2f, this.size.height / 2f)

            when (haloType) {
                // 1. WI-FI HALO: Clockwise Arc Sweep + Sequential Dots
                ExpressiveHaloType.Wifi -> {
                    val startAngle = -210f
                    val totalSweep = 230f
                    val currentSweep = totalSweep * arcProgress

                    // Inactive Background Ring
                    drawArc(
                        color = inactiveColor,
                        startAngle = startAngle,
                        sweepAngle = totalSweep,
                        useCenter = false,
                        topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                        size = Size(ringRadius * 2f, ringRadius * 2f),
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                    )

                    // Active Clockwise Sweeping Arc
                    if (currentSweep > 0f) {
                        drawArc(
                            color = activeArcColor,
                            startAngle = startAngle,
                            sweepAngle = currentSweep,
                            useCenter = false,
                            topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                            size = Size(ringRadius * 2f, ringRadius * 2f),
                            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                        )
                    }

                    // Bottom Dots
                    val dotAngleStart = 50f
                    val dotAngleEnd = 130f
                    val angleStep = if (dotCount > 1) (dotAngleEnd - dotAngleStart) / (dotCount - 1) else 0f

                    for (i in 0 until dotCount) {
                        val dotAngle = dotAngleStart + (i * angleStep)
                        val dotAngleRad = Math.toRadians(dotAngle.toDouble())
                        val dotX = canvasCenter.x + ringRadius * cos(dotAngleRad).toFloat()
                        val dotY = canvasCenter.y + ringRadius * sin(dotAngleRad).toFloat()

                        val dotThresholdStart = i.toFloat() / dotCount
                        val dotThresholdEnd = (i + 1).toFloat() / dotCount
                        val dotAlpha = ((dotsProgress - dotThresholdStart) / (dotThresholdEnd - dotThresholdStart)).coerceIn(0f, 1f)

                        drawCircle(color = inactiveColor, radius = dotRadiusPx, center = Offset(dotX, dotY))

                        if (dotAlpha > 0f) {
                            val activeDotRadius = dotRadiusPx * (1.22f - (0.32f * dotAlpha))
                            drawCircle(color = activeDotColor.copy(alpha = dotAlpha), radius = activeDotRadius, center = Offset(dotX, dotY))
                        }
                    }
                }

                // 2. DND HALO: Crescent Moon Arc + Muted Star Pulse
                ExpressiveHaloType.DoNotDisturb -> {
                    val startAngle = -190f
                    val totalSweep = 210f
                    val currentSweep = totalSweep * arcProgress

                    drawArc(
                        color = inactiveColor,
                        startAngle = startAngle,
                        sweepAngle = totalSweep,
                        useCenter = false,
                        topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                        size = Size(ringRadius * 2f, ringRadius * 2f),
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                    )

                    if (currentSweep > 0f) {
                        drawArc(
                            color = activeArcColor,
                            startAngle = startAngle,
                            sweepAngle = currentSweep,
                            useCenter = false,
                            topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                            size = Size(ringRadius * 2f, ringRadius * 2f),
                            style = Stroke(width = strokeWidthPx * 1.15f, cap = StrokeCap.Round)
                        )
                    }

                    // 4 Muted Star/Shield Dots
                    val dotAngleStart = 45f
                    val dotAngleEnd = 135f
                    val angleStep = (dotAngleEnd - dotAngleStart) / 3f

                    for (i in 0..3) {
                        val dotAngle = dotAngleStart + (i * angleStep)
                        val dotAngleRad = Math.toRadians(dotAngle.toDouble())
                        val dotX = canvasCenter.x + ringRadius * cos(dotAngleRad).toFloat()
                        val dotY = canvasCenter.y + ringRadius * sin(dotAngleRad).toFloat()

                        val dotAlpha = dotsProgress

                        drawCircle(color = inactiveColor, radius = dotRadiusPx, center = Offset(dotX, dotY))

                        if (dotAlpha > 0f) {
                            drawCircle(color = activeDotColor.copy(alpha = dotAlpha * 0.9f), radius = dotRadiusPx * 1.1f, center = Offset(dotX, dotY))
                        }
                    }
                }

                // 3. AIRPLANE MODE HALO: Flight Contrail Arc + Jet Sky Orbit
                ExpressiveHaloType.AirplaneMode -> {
                    val startAngle = -220f
                    val totalSweep = 260f
                    val currentSweep = totalSweep * arcProgress

                    drawArc(
                        color = inactiveColor,
                        startAngle = startAngle,
                        sweepAngle = totalSweep,
                        useCenter = false,
                        topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                        size = Size(ringRadius * 2f, ringRadius * 2f),
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                    )

                    if (currentSweep > 0f) {
                        // Contrail gradient effect
                        drawArc(
                            color = activeArcColor,
                            startAngle = startAngle,
                            sweepAngle = currentSweep,
                            useCenter = false,
                            topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                            size = Size(ringRadius * 2f, ringRadius * 2f),
                            style = Stroke(width = strokeWidthPx * 1.2f, cap = StrokeCap.Round)
                        )
                    }

                    // 3 Flight Waypoint Dots
                    val waypoints = listOf(55f, 90f, 125f)
                    for ((i, angle) in waypoints.withIndex()) {
                        val dotAngleRad = Math.toRadians(angle.toDouble())
                        val dotX = canvasCenter.x + ringRadius * cos(dotAngleRad).toFloat()
                        val dotY = canvasCenter.y + ringRadius * sin(dotAngleRad).toFloat()

                        val dotAlpha = if (dotsProgress > (i * 0.3f)) 1f else 0f

                        drawCircle(color = inactiveColor, radius = dotRadiusPx, center = Offset(dotX, dotY))

                        if (dotAlpha > 0f) {
                            drawCircle(color = activeDotColor, radius = dotRadiusPx * 1.25f, center = Offset(dotX, dotY))
                        }
                    }
                }

                // 4. BLUETOOTH HALO: Interlocking Dual Signal Arcs + Pairing Dots
                ExpressiveHaloType.Bluetooth -> {
                    val sweep1 = 110f * arcProgress
                    val sweep2 = 110f * arcProgress

                    // Left & Right Interlocking Signal Arcs
                    drawArc(
                        color = inactiveColor,
                        startAngle = -170f,
                        sweepAngle = 110f,
                        useCenter = false,
                        topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                        size = Size(ringRadius * 2f, ringRadius * 2f),
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = inactiveColor,
                        startAngle = 10f,
                        sweepAngle = -110f,
                        useCenter = false,
                        topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                        size = Size(ringRadius * 2f, ringRadius * 2f),
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                    )

                    if (sweep1 > 0f) {
                        drawArc(
                            color = activeArcColor,
                            startAngle = -170f,
                            sweepAngle = sweep1,
                            useCenter = false,
                            topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                            size = Size(ringRadius * 2f, ringRadius * 2f),
                            style = Stroke(width = strokeWidthPx * 1.2f, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = activeArcColor,
                            startAngle = 10f,
                            sweepAngle = -sweep2,
                            useCenter = false,
                            topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                            size = Size(ringRadius * 2f, ringRadius * 2f),
                            style = Stroke(width = strokeWidthPx * 1.2f, cap = StrokeCap.Round)
                        )
                    }

                    // 4 Pairing Dots
                    val dotAngles = listOf(45f, 75f, 105f, 135f)
                    for ((i, angle) in dotAngles.withIndex()) {
                        val dotAngleRad = Math.toRadians(angle.toDouble())
                        val dotX = canvasCenter.x + ringRadius * cos(dotAngleRad).toFloat()
                        val dotY = canvasCenter.y + ringRadius * sin(dotAngleRad).toFloat()

                        val dotAlpha = ((dotsProgress - (i * 0.25f)) * 4f).coerceIn(0f, 1f)

                        drawCircle(color = inactiveColor, radius = dotRadiusPx, center = Offset(dotX, dotY))

                        if (dotAlpha > 0f) {
                            drawCircle(color = activeDotColor.copy(alpha = dotAlpha), radius = dotRadiusPx * 1.2f, center = Offset(dotX, dotY))
                        }
                    }
                }

                // 5. MUSIC HALO: Equalizer Frequency Wave Ring + Beat Rhythm Orbit
                ExpressiveHaloType.Music -> {
                    val startAngle = -210f
                    val totalSweep = 230f
                    val currentSweep = totalSweep * arcProgress

                    drawArc(
                        color = inactiveColor,
                        startAngle = startAngle,
                        sweepAngle = totalSweep,
                        useCenter = false,
                        topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                        size = Size(ringRadius * 2f, ringRadius * 2f),
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                    )

                    if (currentSweep > 0f) {
                        drawArc(
                            color = activeArcColor,
                            startAngle = startAngle,
                            sweepAngle = currentSweep,
                            useCenter = false,
                            topLeft = Offset(canvasCenter.x - ringRadius, canvasCenter.y - ringRadius),
                            size = Size(ringRadius * 2f, ringRadius * 2f),
                            style = Stroke(width = strokeWidthPx * 1.3f, cap = StrokeCap.Round)
                        )
                    }

                    // 5 Music Beat Rhythm Dots
                    val dotAngleStart = 50f
                    val dotAngleEnd = 130f
                    val angleStep = (dotAngleEnd - dotAngleStart) / 4f

                    for (i in 0..4) {
                        val dotAngle = dotAngleStart + (i * angleStep)
                        val dotAngleRad = Math.toRadians(dotAngle.toDouble())
                        val dotX = canvasCenter.x + ringRadius * cos(dotAngleRad).toFloat()
                        val dotY = canvasCenter.y + ringRadius * sin(dotAngleRad).toFloat()

                        val dotAlpha = dotsProgress

                        drawCircle(color = inactiveColor, radius = dotRadiusPx, center = Offset(dotX, dotY))

                        if (dotAlpha > 0f) {
                            drawCircle(color = activeDotColor, radius = dotRadiusPx * 1.25f, center = Offset(dotX, dotY))
                        }
                    }
                }
            }
        }

        // CENTER ICON & CONTAINER BACKGROUND
        val resolvedBgColor = if (containerBackgroundColor != Color.Unspecified && containerBackgroundColor != Color.Transparent) {
            containerBackgroundColor
        } else {
            if (active) activeContainerColor else inactiveContainerColor
        }

        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = pressScale
                    scaleY = pressScale
                }
                .then(
                    if (showContainerBackground) {
                        Modifier
                            .size(size * 0.62f)
                            .clip(
                                AbsoluteSmoothCornerShape(
                                    cornerRadius = buttonCornerRadius.dp,
                                    smoothnessAsPercent = 60
                                )
                            )
                            .background(resolvedBgColor)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Status Button",
                tint = if (showContainerBackground && active) {
                    when (haloType) {
                        ExpressiveHaloType.DoNotDisturb -> MaterialTheme.colorScheme.onErrorContainer
                        else -> MaterialTheme.colorScheme.onPrimaryContainer
                    }
                } else if (active) {
                    activeIconTint
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                },
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
