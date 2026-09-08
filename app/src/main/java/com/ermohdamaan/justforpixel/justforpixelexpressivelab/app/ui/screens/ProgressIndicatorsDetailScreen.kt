package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.cards.ExpressiveCard
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.segmented.ToggleSegmentRow
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import kotlinx.coroutines.isActive

/**
 * Dedicated Interactive Detail Screen for Official Google Material 3 Progress Indicators.
 *
 * Features:
 * - 4 Official Google Progress Indicator Variants (CWP, CCP, LCP, LCI).
 * - 60 Seconds (1 Minute) continuous loop cycle with seamless Pause & Resume state.
 * - In-Depth Technical Explanation Card & Ready-to-Copy Source Code.
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProgressIndicatorsDetailScreen(
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current
    val density = LocalDensity.current

    var isPlaying by remember { mutableStateOf(true) }
    var selectedIndicatorMode by remember { mutableIntStateOf(0) }
    var showSourceCode by remember { mutableStateOf(false) }
    var isCodeCopied by remember { mutableStateOf(false) }

    val motionScheme = remember { MotionScheme.expressive() }

    // Continuous Frame Clock Progress Accumulator - Exactly 60 Seconds (1 Minute) per cycle!
    var loopProgress by remember { mutableFloatStateOf(0.15f) }
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

    val sampleCode = """
// Implementation Code for Google Official Material 3 Expressive Progress Indicators
// Author: Er. Mohd Amaan

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OfficialExpressiveProgressIndicators(
    progress: () -> Float
) {
    // 1. Circular Wavy Progress Indicator
    CircularWavyProgressIndicator(
        progress = progress,
        modifier = Modifier.size(200.dp),
        color = MaterialTheme.colorScheme.primary,
        stroke = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round),
        wavelength = WavyProgressIndicatorDefaults.CircularWavelength
    )

    // 2. Linear Wavy Progress Indicator
    LinearWavyProgressIndicator(
        progress = progress,
        modifier = Modifier.fillMaxWidth().height(16.dp),
        color = MaterialTheme.colorScheme.primary,
        wavelength = WavyProgressIndicatorDefaults.LinearDeterminateWavelength
    )
}
    """.trimIndent()

    val modeFullName = when (selectedIndicatorMode) {
        0 -> "CWP: Circular Wavy Progressbar"
        1 -> "CCP: Circular Curvy Progress Indicator"
        2 -> "LCP: Linear Circular Progress Indicator"
        else -> "LCI: Linear Curvy Progress Indicator"
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                            onBackClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back to Catalog"
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            text = "Official Progress Indicators",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Google M3 Expressive Variants",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "v1.0.0",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 8.dp,
                bottom = innerPadding.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. HERO PLAYGROUND CARD
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "OFFICIAL PROGRESS INDICATOR SWITCHER",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // SHORT FORM TOGGLE ROW WITH PUSH PHYSICS (CWP, CCP, LCP, LCI)
                        ToggleSegmentRow(
                            options = listOf("CWP", "CCP", "LCP", "LCI"),
                            selectedIndex = selectedIndicatorMode,
                            onOptionSelected = { selectedIndicatorMode = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // HERO INDICATOR DISPLAY
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

                            // Central Pause / Play Trigger Button
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .clickable {
                                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                        isPlaying = !isPlaying
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Active Mode Full Name Info Badge
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Info,
                                    contentDescription = null,
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
                    }
                }
            }

            // 2. TECHNICAL EXPLANATION CARD
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "1-Minute Timing & State Resumption",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "1. Exact 1-Minute Loop Duration:\n" +
                                    "The progress accumulator runs over 60 seconds (60,000,000,000 nanos) for a complete 360° progress loop.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "2. Seamless Pause & Resume State:\n" +
                                    "When paused, the frame clock freezes loopProgress at its exact frame offset. Toggling play resumes progress from the exact same frame without jumping to start.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Author & Architecture: Designed by Er. Mohd Amaan for ExpressiveLab M3 Open Source Library.",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // 3. SOURCE CODE VIEWER CARD
            item {
                ExpressiveCard(
                    onClick = { showSourceCode = !showSourceCode },
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Code,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (showSourceCode) "Hide Implementation Code" else "View Implementation Code",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "Ready-to-copy Kotlin & Compose snippet",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { showSourceCode = !showSourceCode },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = if (showSourceCode) "Close" else "View Code",
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            // CODE SNIPPET DISPLAY CARD WITH COPY BUTTON
            item {
                AnimatedVisibility(
                    visible = showSourceCode,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ExpressiveProgressIndicators.kt",
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Button(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("ProgressIndicators Code", sampleCode)
                                        clipboard.setPrimaryClip(clip)
                                        isCodeCopied = true
                                        Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isCodeCopied) Icons.Rounded.Check else Icons.Rounded.ContentCopy,
                                        contentDescription = "Copy Code",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isCodeCopied) "Copied!" else "Copy Code",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = sampleCode,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
