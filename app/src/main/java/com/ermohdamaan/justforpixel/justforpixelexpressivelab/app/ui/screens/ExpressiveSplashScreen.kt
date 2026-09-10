package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Surface
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.icons.PlayingEqIcon
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.segmented.ToggleSegmentRow
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.activity.compose.BackHandler
import racra.compose.smooth_corner_rect_library.AbsoluteSmoothCornerShape

/** Enum representing the application screen navigation stack */
enum class AppScreen {
    SPLASH,
    HOME,
    COLOR_PICKER_DETAIL,
    TOOLBAR_DETAIL,
    HEATMAP_DETAIL,
    WAVY_CARD_DETAIL,
    LIVE_NOTIFICATION_DETAIL,
    PULL_REFRESH_DETAIL,
    SWIPE_DISMISS_DETAIL,
    EXPANDABLE_FAB_DETAIL,
    GLASSMORPHISM_DETAIL,
    ORBIT_HALO_DETAIL,
    PUSH_DETAIL,
    MORPH_DETAIL,
    FLOATING_BAR_DETAIL,
    SHAPE_MORPH_DETAIL,
    ORGANIC_SHAPES_DETAIL,
    SLIDERS_DETAIL,
    PROGRESS_DETAIL,
    SEGMENTED_DETAIL,
    EQUALIZER_DETAIL,
    CARDS_DETAIL,
    SHOWCASE
}

/**
 * Main Navigation Orchestrator for ExpressiveLab Application.
 *
 * Flow:
 * Splash Screen ➔ Category Dashboard Home ➔ Dedicated Component Detail Playgrounds & Code Viewers
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun ExpressiveSplashScreenContainer() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.SPLASH) }
    val catalogLazyGridState = rememberLazyGridState()
    val saveableStateHolder = rememberSaveableStateHolder()

    // Er. Mohd Amaan - Intercept Android System Back Gesture / Hardware Back Button on all detail screens
    BackHandler(enabled = currentScreen != AppScreen.HOME && currentScreen != AppScreen.SPLASH) {
        currentScreen = AppScreen.HOME
    }

    Crossfade(
        targetState = currentScreen,
        animationSpec = tween(350),
        label = "AppNavigationCrossfade"
    ) { screen ->
        saveableStateHolder.SaveableStateProvider(key = screen) {
            when (screen) {
                AppScreen.SPLASH -> {
                    ExpressiveSplashScreen(
                        onSplashFinished = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.HOME -> {
                    ComponentCategoryHomeScreen(
                        catalogLazyGridState = catalogLazyGridState,
                        onCategorySelected = { categoryId ->
                            when (categoryId) {
                                "material_you_picker" -> currentScreen = AppScreen.COLOR_PICKER_DETAIL
                                "expressive_toolbar" -> currentScreen = AppScreen.TOOLBAR_DETAIL
                                "m3_heatmap" -> currentScreen = AppScreen.HEATMAP_DETAIL
                                "wavy_progress_card" -> currentScreen = AppScreen.WAVY_CARD_DETAIL
                                "live_notification" -> currentScreen = AppScreen.LIVE_NOTIFICATION_DETAIL
                                "pull_to_refresh" -> currentScreen = AppScreen.PULL_REFRESH_DETAIL
                                "swipe_to_dismiss" -> currentScreen = AppScreen.SWIPE_DISMISS_DETAIL
                                "expandable_fab" -> currentScreen = AppScreen.EXPANDABLE_FAB_DETAIL
                                "glassmorphism_card" -> currentScreen = AppScreen.GLASSMORPHISM_DETAIL
                                "orbit_halo_button" -> currentScreen = AppScreen.ORBIT_HALO_DETAIL
                                "push_buttons" -> currentScreen = AppScreen.PUSH_DETAIL
                                "morph_buttons" -> currentScreen = AppScreen.MORPH_DETAIL
                                "floating_bar" -> currentScreen = AppScreen.FLOATING_BAR_DETAIL
                                "shape_morph_loading" -> currentScreen = AppScreen.SHAPE_MORPH_DETAIL
                                "organic_shapes" -> currentScreen = AppScreen.ORGANIC_SHAPES_DETAIL
                                "sliders" -> currentScreen = AppScreen.SLIDERS_DETAIL
                                "progress" -> currentScreen = AppScreen.PROGRESS_DETAIL
                                "segmented" -> currentScreen = AppScreen.SEGMENTED_DETAIL
                                "visualizers" -> currentScreen = AppScreen.EQUALIZER_DETAIL
                                "cards" -> currentScreen = AppScreen.CARDS_DETAIL
                                "all_playgrounds" -> currentScreen = AppScreen.SHOWCASE
                                else -> currentScreen = AppScreen.HOME
                            }
                        }
                    )
                }
                AppScreen.COLOR_PICKER_DETAIL -> {
                    MaterialYouPickerDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.TOOLBAR_DETAIL -> {
                    ExpressiveToolbarDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.HEATMAP_DETAIL -> {
                    M3HeatmapDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.WAVY_CARD_DETAIL -> {
                    WavyProgressCardDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.LIVE_NOTIFICATION_DETAIL -> {
                    ExpressiveLiveNotificationDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.PULL_REFRESH_DETAIL -> {
                    ExpressivePullToRefreshDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.SWIPE_DISMISS_DETAIL -> {
                    ExpressiveSwipeToDismissDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.EXPANDABLE_FAB_DETAIL -> {
                    ExpressiveExpandableFabDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.GLASSMORPHISM_DETAIL -> {
                    ExpressiveGlassmorphismDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.ORBIT_HALO_DETAIL -> {
                    ExpressiveOrbitHaloDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.PUSH_DETAIL -> {
                    ElasticPushControlsDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.MORPH_DETAIL -> {
                    MorphingButtonDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.FLOATING_BAR_DETAIL -> {
                    FloatingBarDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.SHAPE_MORPH_DETAIL -> {
                    ShapeMorphLoadingDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.ORGANIC_SHAPES_DETAIL -> {
                    OrganicShapesDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.SLIDERS_DETAIL -> {
                    WavySlidersDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.PROGRESS_DETAIL -> {
                    ProgressIndicatorsDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.SEGMENTED_DETAIL -> {
                    SegmentedTogglesDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.EQUALIZER_DETAIL -> {
                    EqualizerDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.CARDS_DETAIL -> {
                    SquircleCardsDetailScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
                AppScreen.SHOWCASE -> {
                    ExpressiveShowcaseScreen(
                        onBackClick = { currentScreen = AppScreen.HOME }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun ExpressiveSplashScreen(
    onSplashFinished: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var isSkipTriggered by remember { mutableStateOf(false) }
    // Mode 0: CWP, Mode 1: CCP, Mode 2: LCP, Mode 3: LCI
    var selectedIndicatorMode by remember { mutableIntStateOf(0) }

    val hapticFeedback = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val motionScheme = remember { MotionScheme.expressive() }
    val spatialDpSpec = remember { motionScheme.defaultSpatialSpec<androidx.compose.ui.unit.Dp>() }

    // Auto-click Play after 400ms on launch
    LaunchedEffect(Unit) {
        delay(400)
        isPlaying = true
    }

    // Continuous Frame Clock Progress Accumulator - Exactly 60 Seconds (1 Minute) per cycle!
    var loopProgress by remember { mutableFloatStateOf(0.10f) }
    LaunchedEffect(isPlaying) {
        if (!isPlaying) return@LaunchedEffect
        var lastNanos = 0L
        val cycleDurationNanos = 60_000_000_000L // 60 Seconds (1 Minute) per full cycle

        while (isActive) {
            val frameNanos = withFrameNanos { it }
            if (lastNanos != 0L) {
                val deltaNanos = frameNanos - lastNanos
                val deltaFraction = (deltaNanos.toDouble() / cycleDurationNanos.toDouble()).toFloat()
                loopProgress = (loopProgress + deltaFraction) % 1.0f
            }
            lastNanos = frameNanos
        }
    }

    // Central Hero Play/Pause Button Corner Morphing & Scale
    val cornerRadius by animateDpAsState(
        targetValue = if (isPlaying) 20.dp else 48.dp,
        animationSpec = spatialDpSpec,
        label = "SplashCornerMorphAnim"
    )

    val logoScale by animateFloatAsState(
        targetValue = if (isPlaying) 1.05f else 0.95f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "SplashScaleAnim"
    )

    // SKIP BUTTON MORPHING & PRESS PHYSICS
    val skipInteractionSource = remember { MutableInteractionSource() }
    val isSkipPressed by skipInteractionSource.collectIsPressedAsState()

    val skipCornerRadius by animateDpAsState(
        targetValue = if (isSkipPressed || isSkipTriggered) 10.dp else 30.dp,
        animationSpec = spatialDpSpec,
        label = "SkipCornerMorphAnim"
    )

    val skipScale by animateFloatAsState(
        targetValue = if (isSkipPressed || isSkipTriggered) 0.88f else 1.0f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "SkipScaleAnim"
    )

    // EXPLORE BUTTON MORPHING & PRESS PHYSICS
    val exploreInteractionSource = remember { MutableInteractionSource() }
    val isExplorePressed by exploreInteractionSource.collectIsPressedAsState()

    val exploreCornerRadius by animateDpAsState(
        targetValue = if (isExplorePressed) 12.dp else 32.dp,
        animationSpec = spatialDpSpec,
        label = "ExploreCornerMorphAnim"
    )

    val exploreScale by animateFloatAsState(
        targetValue = if (isExplorePressed) 0.92f else 1.0f,
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "ExploreScaleAnim"
    )

    // Legend Mapping
    val modeFullName = when (selectedIndicatorMode) {
        0 -> "CWP: Circular Wavy Progressbar"
        1 -> "CCP: Circular Curvy Progress Indicator"
        2 -> "LCP: Linear Circular Progress Indicator"
        else -> "LCI: Linear Curvy Progress Indicator"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        // TOP-RIGHT MORPHING SKIP BUTTON
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 20.dp)
                .graphicsLayer {
                    scaleX = skipScale
                    scaleY = skipScale
                    clip = true
                    shape = AbsoluteSmoothCornerShape(
                        cornerRadius = skipCornerRadius,
                        smoothnessAsPercent = 60
                    )
                }
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable(
                    interactionSource = skipInteractionSource,
                    indication = null,
                    onClick = {
                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                        isSkipTriggered = true
                        coroutineScope.launch {
                            delay(220)
                            onSplashFinished()
                        }
                    }
                )
                .padding(horizontal = 22.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "SKIP",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Rounded.ArrowForward,
                    contentDescription = "Skip Splash Screen",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // CENTER HERO AREA
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // SHORT FORM TOGGLE ROW WITH PUSH PHYSICS (CWP, CCP, LCP, LCI)
            ToggleSegmentRow(
                options = listOf("CWP", "CCP", "LCP", "LCI"),
                selectedIndex = selectedIndicatorMode,
                onOptionSelected = { selectedIndicatorMode = it },
                modifier = Modifier
                    .fillMaxWidth(0.96f)
                    .height(48.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // HERO AREA WITH SELECTED GOOGLE OFFICIAL PROGRESS INDICATOR
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                val ringStrokePx = with(density) { 8.dp.toPx() }
                val stroke = remember(ringStrokePx) { Stroke(width = ringStrokePx, cap = StrokeCap.Round) }

                Crossfade(
                    targetState = selectedIndicatorMode,
                    animationSpec = tween(400),
                    label = "IndicatorTypeCrossfade"
                ) { mode ->
                    when (mode) {
                        0 -> {
                            // Mode 0: CWP - Circular Wavy Progressbar
                            CircularWavyProgressIndicator(
                                progress = { loopProgress },
                                modifier = Modifier.size(210.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                stroke = stroke,
                                trackStroke = stroke,
                                wavelength = WavyProgressIndicatorDefaults.CircularWavelength,
                                waveSpeed = WavyProgressIndicatorDefaults.CircularWavelength / 4f
                            )
                        }
                        1 -> {
                            // Mode 1: CCP - Circular Curvy Progress Indicator (Thick Curvy Stroke)
                            CircularWavyProgressIndicator(
                                progress = { loopProgress },
                                modifier = Modifier.size(210.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                stroke = Stroke(width = with(density) { 14.dp.toPx() }, cap = StrokeCap.Round),
                                trackStroke = Stroke(width = with(density) { 14.dp.toPx() }, cap = StrokeCap.Round),
                                wavelength = WavyProgressIndicatorDefaults.CircularWavelength * 1.8f,
                                waveSpeed = WavyProgressIndicatorDefaults.CircularWavelength / 5f
                            )
                        }
                        2 -> {
                            // Mode 2: LCP - Linear Circular Progress Indicator (Solid Arc)
                            CircularProgressIndicator(
                                progress = { loopProgress },
                                modifier = Modifier.size(210.dp),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                strokeWidth = 8.dp,
                                strokeCap = StrokeCap.Round
                            )
                        }
                        else -> {
                            // Mode 3: LCI - Linear Curvy Progress Indicator (Horizontal Wavy Bar)
                            Box(
                                modifier = Modifier
                                    .size(220.dp)
                                    .clip(RoundedCornerShape(28.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(16.dp),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                LinearWavyProgressIndicator(
                                    progress = { loopProgress },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(16.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    wavelength = WavyProgressIndicatorDefaults.LinearDeterminateWavelength,
                                    waveSpeed = WavyProgressIndicatorDefaults.LinearDeterminateWavelength / 3f
                                )
                            }
                        }
                    }
                }

                // BALANCED CENTRAL PLAY / PAUSE MORPHING HERO BUTTON (96dp Container)
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .graphicsLayer {
                            scaleX = logoScale
                            scaleY = logoScale
                            clip = true
                            shape = AbsoluteSmoothCornerShape(
                                cornerRadius = cornerRadius,
                                smoothnessAsPercent = 60
                            )
                        }
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                            isPlaying = !isPlaying
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Crossfade(
                        targetState = isPlaying,
                        animationSpec = motionScheme.fastEffectsSpec(),
                        label = "SplashPlayPauseCrossfade"
                    ) { playing ->
                        Icon(
                            imageVector = if (playing) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = if (playing) "Pause" else "Play",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // INFO CARD DEFINING THE FULL NAME OF ACTIVE MODE
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth(0.92f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = "Indicator Full Name Info",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = modeFullName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // App Title & Subtitle
            Text(
                text = "ExpressiveLab M3",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                PlayingEqIcon(
                    isPlaying = isPlaying,
                    modifier = Modifier.size(22.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isPlaying) "Playing Expressive Motion..." else "Tap Center Button to Play",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // MORPHING "EXPLORE CATALOG" BOTTOM BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .height(56.dp)
                    .graphicsLayer {
                        scaleX = exploreScale
                        scaleY = exploreScale
                        clip = true
                        shape = AbsoluteSmoothCornerShape(
                            cornerRadius = exploreCornerRadius,
                            smoothnessAsPercent = 60
                        )
                    }
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(
                        interactionSource = exploreInteractionSource,
                        indication = null,
                        onClick = {
                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                            coroutineScope.launch {
                                delay(180)
                                onSplashFinished()
                            }
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "EXPLORE CATALOG",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Author Credit Tag
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Created by Er. Mohd Amaan",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
