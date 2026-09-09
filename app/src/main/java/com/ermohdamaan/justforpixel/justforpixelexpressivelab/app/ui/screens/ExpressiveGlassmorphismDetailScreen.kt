package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.cards.ExpressiveGlassmorphismCard
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.picker.rememberMonetWallpaperPalette
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

/** Data model representing a 3-Color Wallpaper Gradient Preset (Primary, Secondary, Tertiary) */
data class WallpaperGradientPreset(
    val name: String,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color
)

/**
 * Interactive Playground Detail Screen for Material 3 Glassmorphic Frost Card.
 *
 * Features:
 * - Real-time System Wallpaper Monet dynamic color extraction.
 * - Swipeable Wallpaper Palette Selector (Monet, Cyberpunk, Sunset, Emerald, Galactic).
 * - Sliders for Backdrop Blur Radius (0dp..32dp) & Translucency Frost Alpha.
 * - Toggle Switch for [blurBehindContent] (Blurs ONLY background keeping text 100% sharp).
 * - Source Code Viewer with Copy to Clipboard.
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveGlassmorphismDetailScreen(
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    // Live System Wallpaper Monet Palette
    val monetWallpaper = rememberMonetWallpaperPalette()

    // Wallpaper Preset Palettes
    val wallpaperPresets = listOf(
        WallpaperGradientPreset("Monet Wallpaper", monetWallpaper.primary, monetWallpaper.secondary, monetWallpaper.tertiary),
        WallpaperGradientPreset("Galactic Nebula", Color(0xFF7C4DFF), Color(0xFFE040FB), Color(0xFF00B0FF)),
        WallpaperGradientPreset("Cyberpunk Neon", Color(0xFF00E5FF), Color(0xFFFF007F), Color(0xFFFFEA00)),
        WallpaperGradientPreset("Sunset Flame", Color(0xFFFF3D00), Color(0xFFFF9100), Color(0xFFD500F9)),
        WallpaperGradientPreset("Emerald Forest", Color(0xFF00E676), Color(0xFF1DE9B6), Color(0xFFC6FF00))
    )

    var activeWallpaperIndex by remember { mutableIntStateOf(0) }
    val currentWallpaper = wallpaperPresets[activeWallpaperIndex]

    // Animated Color Transitions for Wallpaper Background
    val animPrimary by animateColorAsState(targetValue = currentWallpaper.primary, animationSpec = TactileMotionTokens.gentleSpring(), label = "AnimP")
    val animSecondary by animateColorAsState(targetValue = currentWallpaper.secondary, animationSpec = TactileMotionTokens.gentleSpring(), label = "AnimS")
    val animTertiary by animateColorAsState(targetValue = currentWallpaper.tertiary, animationSpec = TactileMotionTokens.gentleSpring(), label = "AnimT")

    // Playground States
    var blurVal by remember { mutableFloatStateOf(16f) }
    var alphaVal by remember { mutableFloatStateOf(0.45f) }
    var blurBehindState by remember { mutableStateOf(true) }

    var showSourceCode by remember { mutableStateOf(false) }
    var isCodeCopied by remember { mutableStateOf(false) }

    val sampleCode = """
// Material 3 Expressive Glassmorphic Card (Monet Wallpaper Background)
// Author: Er. Mohd Amaan

val monetWallpaper = rememberMonetWallpaperPalette()

ExpressiveGlassmorphismCard(
    backdropBlurRadius = 16.dp,
    blurBehindContent = true, // Text stays 100% sharp
    frostAlpha = 0.45f,
    onClick = { /* Handle Click */ }
) {
    Column {
        Text("Glassmorphic Frost Header", style = MaterialTheme.typography.titleMedium)
        Text("Dynamic Material You Primary, Secondary & Tertiary tint")
    }
}
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
                            text = "Material 3 Glassmorphism Card",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Monet Wallpaper & 3-Color Swatch Background",
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
                                text = "NEW v1.2.0",
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
            // HERO PLAYGROUND
            item {
                Text(
                    text = "INTERACTIVE PLAYGROUND",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Wallpaper Palette Swatch Selector
                    Text(
                        text = "Select Background Wallpaper Palette:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 2.dp)
                    ) {
                        itemsIndexed(wallpaperPresets) { idx, preset ->
                            val isSelected = activeWallpaperIndex == idx

                            Surface(
                                shape = ShapeCache.smooth12,
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                                modifier = Modifier
                                    .clip(ShapeCache.smooth12)
                                    .clickable {
                                        hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                        activeWallpaperIndex = idx
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                        Box(Modifier.size(10.dp).clip(CircleShape).background(preset.primary))
                                        Box(Modifier.size(10.dp).clip(CircleShape).background(preset.secondary))
                                        Box(Modifier.size(10.dp).clip(CircleShape).background(preset.tertiary))
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = preset.name,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    // Dynamic Monet Wallpaper Background Box with Floating Glass Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(ShapeCache.smooth20)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        animPrimary,
                                        animSecondary,
                                        animTertiary
                                    )
                                )
                            )
                            .padding(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Live Glassmorphic Card over Dynamic Wallpaper
                        ExpressiveGlassmorphismCard(
                            backdropBlurRadius = blurVal.dp,
                            blurBehindContent = blurBehindState,
                            frostAlpha = alphaVal,
                            onClick = {
                                Toast.makeText(context, "Glassmorphism Card Tapped!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.Palette,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = currentWallpaper.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                                Text(
                                    text = "Material You Primary, Secondary & Tertiary wallpaper gradient with 100% sharp text layering.",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White.copy(alpha = 0.95f)
                                )
                            }
                        }
                    }

                    // Controls Card
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shape = ShapeCache.smooth16,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Blur Behind Content Switch
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(ShapeCache.smooth12)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Blur Background Only (Keep Text Sharp)",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Prevents text & icons from blurring",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = blurBehindState,
                                    onCheckedChange = { blurBehindState = it }
                                )
                            }

                            Column {
                                Text(
                                    text = "Backdrop Blur Depth: ${blurVal.toInt()} dp",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Slider(
                                    value = blurVal,
                                    onValueChange = { blurVal = it },
                                    valueRange = 0f..32f
                                )
                            }

                            Column {
                                Text(
                                    text = "Translucency Alpha: ${String.format("%.2f", alphaVal)}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Slider(
                                    value = alphaVal,
                                    onValueChange = { alphaVal = it },
                                    valueRange = 0.1f..0.85f
                                )
                            }
                        }
                    }
                }
            }

            // ARCHITECTURE SPECS
            item {
                Text(
                    text = "OFFICIAL M3 ARCHITECTURE SPECS",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shape = ShapeCache.smooth20,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Dynamic Monet Wallpaper Glass Specs",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "• Real Android 12+ Wallpaper Monet Primary, Secondary & Tertiary color extraction\n" +
                                    "• 5 Selectable Wallpaper Palettes (Monet Wallpaper, Cyberpunk, Sunset, Emerald, Galactic)\n" +
                                    "• 2-Layer Glass Architecture keeps text 100% sharp & un-blurred\n" +
                                    "• Smooth animated gradient color transitions with TactileMotionTokens spring physics",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
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
                                            val clip = ClipData.newPlainText("GlassmorphicCard Code", sampleCode)
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
