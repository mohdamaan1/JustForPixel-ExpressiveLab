package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.screens

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.notifications.ExpressiveLiveNotificationService
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.notifications.ExpressiveSplitJourneyNotificationService
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.notifications.LiveServiceState
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.notifications.SplitJourneyState
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

/** Preset 3-Color Segment Palette for Android 17 Multi-Segment Progress */
data class JourneySegmentPalette(
    val name: String,
    val color1: Color,
    val color2: Color,
    val color3: Color
)

/**
 * Playground Detail Screen for Android 16 & Android 17 Live Activity Notifications.
 *
 * =========================================================================================================
 * 🚀 HOW LIVE NOTIFICATIONS SUCCESS WAS ACHIEVED & ARCHITECTURE STEPS:
 * =========================================================================================================
 * 1. SERVICE MANIFEST REGISTRATION (CRITICAL):
 *    - Both [ExpressiveLiveNotificationService] and [ExpressiveSplitJourneyNotificationService] MUST be registered
 *      in `AndroidManifest.xml` under `<application>` tag.
 *    - Include `android:foregroundServiceType="specialUse"` and property `android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE`.
 *
 * 2. NOTIFICATION CHANNEL WITH HIGH IMPORTANCE:
 *    - Create `NotificationChannel` with `NotificationManager.IMPORTANCE_HIGH` and `setShowBadge(true)`.
 *
 * 3. CATEGORY_STOPWATCH & PROMOTED EXTRA FLAGS:
 *    - Set `builder.setCategory(Notification.CATEGORY_STOPWATCH)` and `builder.extras.putBoolean("android.requestPromotedOngoing", true)`.
 *    - Set `builder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE)` for immediate chips.
 *
 * 4. ANDROID 16 NATIVE `Notification.ProgressStyle` REFLECTION (API 35+):
 *    - Construct native `android.app.Notification.Builder` and invoke `setStyle(progressStyleObj)`.
 *
 * =========================================================================================================
 * ⚠️ COMMON PITFALLS & ERRORS TO AVOID:
 * =========================================================================================================
 * ❌ PITFALL 1: Omitting the `<service>` from `AndroidManifest.xml`. Calling `ContextCompat.startForegroundService`
 *               without manifest entry fails silently or crashes.
 * ❌ PITFALL 2: Applying reflection to `NotificationCompat.Builder`'s `mStyle`. `NotificationCompat` overrides `mStyle`
 *               in `build()`. Always construct native `Notification.Builder` on API 35+.
 * ❌ PITFALL 3: Omitting `foregroundServiceType` on Android 14+ (API 34+), throwing `MissingForegroundServiceTypeException`.
 * ❌ PITFALL 4: Omitting `POST_NOTIFICATIONS` permission check on Android 13+ (API 33+).
 * =========================================================================================================
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveLiveNotificationDetailScreen(
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    // Android 16 Live Timer State Flow
    val timerText by LiveServiceState.formattedTime.collectAsState()
    val remainingSecs by LiveServiceState.remainingSeconds.collectAsState()
    val isServiceActive by LiveServiceState.isServiceActive.collectAsState()
    val isServicePaused by LiveServiceState.isPaused.collectAsState()

    // Android 17 Split Journey State Flow
    val journeyProgress by SplitJourneyState.progress.collectAsState()
    val journeyStageText by SplitJourneyState.stageText.collectAsState()
    val journeyTitleText by SplitJourneyState.titleText.collectAsState()
    val isJourneyActive by SplitJourneyState.isActive.collectAsState()

    // Material You / Material 3 3-Color Segment Palettes
    val segmentPalettes = listOf(
        JourneySegmentPalette("Material You 3-Color", MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary),
        JourneySegmentPalette("Vivid Focus", Color(0xFF00E676), Color(0xFFFFEA00), Color(0xFF00E5FF)),
        JourneySegmentPalette("Cyber Neon", Color(0xFFFF007F), Color(0xFF00E5FF), Color(0xFFC6FF00)),
        JourneySegmentPalette("Sunset Flame", Color(0xFFFF3D00), Color(0xFFFF9100), Color(0xFFD500F9))
    )
    var selectedPaletteIndex by remember { mutableIntStateOf(0) }
    val currentPalette = segmentPalettes[selectedPaletteIndex]

    var showSourceCode by remember { mutableStateOf(false) }
    var isCodeCopied by remember { mutableStateOf(false) }

    // Android 13+ Notification Permission State Check
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            Toast.makeText(context, "Notification permission granted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission needed for Live Notifications", Toast.LENGTH_LONG).show()
        }
    }

    val sampleCode = """
// Android 17 ProgressStyle Multi-Segment Split Timer Notification
// Author: Er. Mohd Amaan

val progressStyle = Notification.ProgressStyle()
    .setProgress(journeyProgress) // 0 to 1000
    .setProgressTrackerIcon(Icon.createWithResource(context, R.drawable.ic_timer))
    .setProgressSegments(
        listOf(
            Notification.ProgressStyle.Segment(350).setColor(color1), // Split 1: Focus
            Notification.ProgressStyle.Segment(350).setColor(color2), // Split 2: Rest
            Notification.ProgressStyle.Segment(300).setColor(color3)  // Split 3: Cooldown
        )
    )
""".trimIndent()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Live Activity Notifications",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Android 16 & Android 17 ProgressStyle",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
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
                                text = "v1.1.0",
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
                .consumeWindowInsets(innerPadding)
                .padding(top = innerPadding.calculateTopPadding()),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // PERMISSION CHECK BANNER
            item {
                AnimatedVisibility(
                    visible = !hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f)
                        ),
                        shape = ShapeCache.smooth20,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f),
                                shape = ShapeCache.smooth20
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.NotificationsActive,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Notification Permission Required",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Allow notifications to display live status bar chips & ongoing activity updates in background.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 16.sp
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                },
                                shape = ShapeCache.smoothPill,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = MaterialTheme.colorScheme.onTertiary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.NotificationsActive,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Grant Notification Permission",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // SECTION 1: ANDROID 16 LIVE ACTIVITY NOTIFICATION
            item {
                Text(
                    text = "SECTION 1: ANDROID 16 LIVE ACTIVITY NOTIFICATION",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shape = ShapeCache.smooth20,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (isServiceActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            ShapeCache.smooth20
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // HEADER ROW: TITLE & BADGE
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.NotificationsActive,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Live Status Bar Chip & Timer",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Surface(
                                shape = ShapeCache.smoothPill,
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "ANDROID 16",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    maxLines = 1,
                                    softWrap = false,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // STATUS BAR CHIP LIVE SIMULATION BANNER
                        Surface(
                            shape = ShapeCache.smoothPill,
                            color = if (isServiceActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = if (isServiceActive && !isServicePaused) Icons.Rounded.Timer else Icons.Rounded.Pause,
                                    contentDescription = null,
                                    tint = if (isServiceActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isServiceActive) "Live Status Chip • $timerText" else "Status Bar Chip Inactive • 05:00",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isServiceActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // SMOOTH ANIMATED LIVE PROGRESS BAR FOR ANDROID 16
                        val targetProgress16 = if (isServiceActive) ((300 - remainingSecs).coerceIn(0, 300) / 300f) else 0f
                        val animatedProgress16 by animateFloatAsState(
                            targetValue = targetProgress16,
                            animationSpec = tween(
                                durationMillis = if (isServiceActive && !isServicePaused && targetProgress16 > 0f) 1000 else 300,
                                easing = LinearEasing
                            ),
                            label = "Android16LiveProgressAnimation"
                        )

                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Live Moving Icon Indicator (Smooth Glide)
                            BoxWithConstraints(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                            ) {
                                val maxPx = maxWidth - 18.dp
                                val startPadding = (maxPx * animatedProgress16).coerceAtLeast(0.dp)

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = startPadding)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Timer,
                                        contentDescription = "Live Timer Icon",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            // 20dp Expressive Thick Linear Progress Bar with Animated Fill
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(animatedProgress16.coerceIn(0f, 1f))
                                        .height(20.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Progress Text & Percentage Label
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isServiceActive) "Live Elapsed: ${(animatedProgress16 * 100).toInt()}%" else "Progress: 0%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = if (isServiceActive) "Remaining: $timerText" else "Total: 05:00 Mins",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // CONTROLS
                        if (!isServiceActive) {
                            Button(
                                onClick = {
                                    hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                    if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        val startIntent = Intent(context, ExpressiveLiveNotificationService::class.java).apply {
                                            action = ExpressiveLiveNotificationService.ACTION_START
                                            putExtra(ExpressiveLiveNotificationService.EXTRA_DURATION, 300)
                                        }
                                        ContextCompat.startForegroundService(context, startIntent)
                                        Toast.makeText(context, "Android 16 Timer Notification Started!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = ShapeCache.smoothPill,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Rounded.Notifications, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Start Android 16 Live Activity", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                        val toggleIntent = Intent(context, ExpressiveLiveNotificationService::class.java).apply {
                                            action = if (isServicePaused) ExpressiveLiveNotificationService.ACTION_RESUME else ExpressiveLiveNotificationService.ACTION_PAUSE
                                        }
                                        context.startService(toggleIntent)
                                    },
                                    shape = ShapeCache.smoothPill,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (isServicePaused) Icons.Rounded.PlayArrow else Icons.Rounded.Pause,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isServicePaused) "Resume" else "Pause", fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                        val stopIntent = Intent(context, ExpressiveLiveNotificationService::class.java).apply {
                                            action = ExpressiveLiveNotificationService.ACTION_STOP
                                        }
                                        context.startService(stopIntent)
                                    },
                                    shape = ShapeCache.smoothPill,
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                ) {
                                    Icon(Icons.Rounded.Stop, null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Stop", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 2: ANDROID 17 PROGRESSSTYLE MULTI-SEGMENT SPLIT TIMER NOTIFICATION
            item {
                Text(
                    text = "SECTION 2: ANDROID 17 MULTI-SEGMENT SPLIT TIMER NOTIFICATION",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary,
                    letterSpacing = 1.sp
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shape = ShapeCache.smooth20,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            if (isJourneyActive) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant,
                            ShapeCache.smooth20
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // FIXED HEADER ROW: UN-TRUNCATED BADGE LAYOUT
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Timer,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Focus & Rest Split Timer",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Fixed Un-wrapped Badge Tag
                            Surface(
                                shape = ShapeCache.smoothPill,
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "ANDROID 17",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    maxLines = 1,
                                    softWrap = false,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = if (isJourneyActive) "$journeyTitleText\n$journeyStageText" else "Split Timer Inactive • Tap start to trigger live status bar notification",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        // 3-COLOR MATERIAL YOU / MATERIAL 3 SEGMENT PALETTE SELECTOR
                        Text(
                            text = "Select Segment Color Palette:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 2.dp)
                        ) {
                            items(segmentPalettes.indices.toList()) { idx ->
                                val palette = segmentPalettes[idx]
                                val isSelected = selectedPaletteIndex == idx

                                Surface(
                                    shape = ShapeCache.smooth12,
                                    color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                                    modifier = Modifier
                                        .clip(ShapeCache.smooth12)
                                        .clickable { selectedPaletteIndex = idx }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Box(Modifier.size(10.dp).clip(CircleShape).background(palette.color1))
                                            Box(Modifier.size(10.dp).clip(CircleShape).background(palette.color2))
                                            Box(Modifier.size(10.dp).clip(CircleShape).background(palette.color3))
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = palette.name,
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        // MATERIAL 3 EXPRESSIVE MULTI-SEGMENT LINEAR PROGRESS BAR
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Smooth Continuous GPS / Navigation Tracker Icon Interpolation (Linear Easing over 1000ms duration)
                            val targetOffset = if (isJourneyActive) (journeyProgress / 1000f) else 0f
                            val animatedTrackerOffset by animateFloatAsState(
                                targetValue = targetOffset,
                                animationSpec = tween(
                                    durationMillis = if (isJourneyActive && targetOffset > 0f) 1000 else 300,
                                    easing = LinearEasing
                                ),
                                label = "SmoothGPSTrackerAnimation"
                            )

                            BoxWithConstraints(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(18.dp)
                            ) {
                                val maxPx = maxWidth - 16.dp
                                val startPadding = (maxPx * animatedTrackerOffset).coerceAtLeast(0.dp)

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = startPadding)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Navigation,
                                        contentDescription = "Smooth GPS Tracker Icon",
                                        tint = currentPalette.color2,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            // Colorized Split Progress Segments (Split 1: Focus, Split 2: Rest, Split 3: Cooldown)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(0.35f)
                                        .height(20.dp)
                                        .background(currentPalette.color1)
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(0.35f)
                                        .height(20.dp)
                                        .background(currentPalette.color2)
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(0.30f)
                                        .height(20.dp)
                                        .background(currentPalette.color3)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Split 1: Focus", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = currentPalette.color1)
                                Text("Split 2: Rest", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = currentPalette.color2)
                                Text("Split 3: Cooldown", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = currentPalette.color3)
                            }
                        }

                        // Start / Stop Android 17 Journey Trigger Button
                        if (!isJourneyActive) {
                            Button(
                                onClick = {
                                    hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                    if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    } else {
                                        val startIntent = Intent(context, ExpressiveSplitJourneyNotificationService::class.java).apply {
                                            action = ExpressiveSplitJourneyNotificationService.ACTION_START_JOURNEY
                                            putExtra(ExpressiveSplitJourneyNotificationService.EXTRA_C1, currentPalette.color1.toArgb())
                                            putExtra(ExpressiveSplitJourneyNotificationService.EXTRA_C2, currentPalette.color2.toArgb())
                                            putExtra(ExpressiveSplitJourneyNotificationService.EXTRA_C3, currentPalette.color3.toArgb())
                                        }
                                        ContextCompat.startForegroundService(context, startIntent)
                                        Toast.makeText(context, "Android 17 Split Timer Notification Started!", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                shape = ShapeCache.smoothPill,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    contentColor = MaterialTheme.colorScheme.onSecondary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Rounded.Navigation, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Start Android 17 Split Timer Notification", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(
                                onClick = {
                                    hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                    val stopIntent = Intent(context, ExpressiveSplitJourneyNotificationService::class.java).apply {
                                        action = ExpressiveSplitJourneyNotificationService.ACTION_STOP_JOURNEY
                                    }
                                    context.startService(stopIntent)
                                },
                                shape = ShapeCache.smoothPill,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer,
                                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Rounded.Stop, null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("End Android 17 Split Session", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // SOURCE CODE VIEWER
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "INTEGRATION CODE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 1.sp
                        )

                        Button(
                            onClick = { showSourceCode = !showSourceCode },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Code,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (showSourceCode) "Hide Code" else "View Code",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = showSourceCode,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = ShapeCache.smooth16,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Kotlin Source Snippet",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("Live Notification Code", sampleCode)
                                            clipboard.setPrimaryClip(clip)
                                            isCodeCopied = true
                                            Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isCodeCopied) Icons.Rounded.Check else Icons.Rounded.ContentCopy,
                                            contentDescription = "Copy Code",
                                            tint = if (isCodeCopied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(ShapeCache.smooth12)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(12.dp)
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
}
