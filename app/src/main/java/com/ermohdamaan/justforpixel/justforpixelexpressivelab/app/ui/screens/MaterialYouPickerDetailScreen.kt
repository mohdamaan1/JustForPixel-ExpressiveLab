package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Info
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.picker.AatrangiDefaults
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.picker.MaterialYouColorPickerDialog
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.picker.MaterialYouColorPickerListItem
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.picker.rememberMonetWallpaperPalette
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

/**
 * Interactive Playground Detail Screen for Material You & Custom Studio.
 *
 * Features:
 * - Real-time Wallpaper Monet color extraction preview.
 * - Live Settings Row Card (`MaterialYouColorPickerListItem`) displaying 4 roles.
 * - Advanced Floating Dialog (`MaterialYouColorPickerDialog`) with custom 4-role mixer.
 * - Interactive Accent Palette Preview displaying Primary, Secondary, Tertiary, and Surface.
 * - Dynamic Luminance Contrast for perfect readability in Light, Dark & OLED modes.
 * - Copyable Kotlin Integration Source Code Viewer.
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialYouPickerDetailScreen(
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    // Live Wallpaper Monet palette
    val monetPalette = rememberMonetWallpaperPalette()

    // Playground States
    var activePalette by remember { mutableStateOf(AatrangiDefaults.CyberpunkNeon) }
    var isMonetEnabled by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    var showSourceCode by remember { mutableStateOf(false) }
    var isCodeCopied by remember { mutableStateOf(false) }

    val sampleCode = """
// Advanced Material You & Custom Theme Studio
// Author: Er. Mohd Amaan

// 1. Fetch Real Android 12+ Wallpaper Monet Palette
val wallpaperMonetPalette = rememberMonetWallpaperPalette()

// 2. Settings Row Component
MaterialYouColorPickerListItem(
    activePalette = activePalette,
    onPaletteSelected = { palette -> activePalette = palette },
    isMonetActive = isMonetEnabled,
    onMonetToggle = { enabled -> isMonetEnabled = enabled }
)

// 3. Floating Dialog Component with Custom 4-Role Mixer
if (showDialog) {
    MaterialYouColorPickerDialog(
        onDismissRequest = { showDialog = false },
        activePalette = activePalette,
        onPaletteSelected = { palette -> activePalette = palette },
        isMonetActive = isMonetEnabled,
        onMonetToggle = { enabled -> isMonetEnabled = enabled }
    )
}
""".trimIndent()

    if (showDialog) {
        MaterialYouColorPickerDialog(
            onDismissRequest = { showDialog = false },
            activePalette = if (isMonetEnabled) monetPalette else activePalette,
            onPaletteSelected = { activePalette = it },
            isMonetActive = isMonetEnabled,
            onMonetToggle = { isMonetEnabled = it }
        )
    }

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
                            text = "Material You & Custom Studio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Wallpaper Monet & Custom 4-Role Mixer",
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
                                text = "ADVANCED v1.1.0",
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
            // HERO INTERACTIVE PLAYGROUND
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
                    // Settings Row Card
                    MaterialYouColorPickerListItem(
                        activePalette = if (isMonetEnabled) monetPalette else activePalette,
                        onPaletteSelected = { activePalette = it },
                        isMonetActive = isMonetEnabled,
                        onMonetToggle = { isMonetEnabled = it }
                    )

                    // Dialog Trigger Button
                    Button(
                        onClick = {
                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                            showDialog = true
                        },
                        shape = ShapeCache.smoothPill,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ColorLens,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Open Custom Theme Studio Dialog",
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    // Live Palette Role Showcase Box
                    val displayPalette = if (isMonetEnabled) monetPalette else activePalette
                    val cardBgColor = if (isMonetEnabled) MaterialTheme.colorScheme.surfaceContainerHigh else displayPalette.surface
                    val cardTitleColor = if (cardBgColor.luminance() > 0.45f) Color(0xFF141414) else Color.White

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = cardBgColor
                        ),
                        shape = ShapeCache.smooth20,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, displayPalette.primary.copy(alpha = 0.5f), ShapeCache.smooth20)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Live Theme Palette Showcase",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = cardTitleColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = displayPalette.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = displayPalette.primary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RoleDisplayCard("Primary", displayPalette.primary, Modifier.weight(1f))
                                RoleDisplayCard("Secondary", displayPalette.secondary, Modifier.weight(1f))
                                RoleDisplayCard("Tertiary", displayPalette.tertiary, Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // ARCHITECTURE & DOCUMENTATION
            item {
                Text(
                    text = "COMPONENT ARCHITECTURE",
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
                                text = "Custom Theme Engine Specs",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "• Real Android 12+ Wallpaper Monet dynamic color extraction\n" +
                                    "• Independent Primary, Secondary, Tertiary & Surface 4-color customization\n" +
                                    "• 6 Vibrant Custom Presets (Cyberpunk Neon, Sunset Flame, Emerald, etc.)\n" +
                                    "• Bouncy spring scale interactions & Tactile Motion Tokens haptic feedback",
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
                                            val clip = ClipData.newPlainText("Custom Studio Code", sampleCode)
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

@Composable
private fun RoleDisplayCard(roleName: String, color: Color, modifier: Modifier = Modifier) {
    val textColor = if (color.luminance() > 0.45f) Color(0xFF141414) else Color.White
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(ShapeCache.smooth12)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = roleName,
            color = textColor,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
