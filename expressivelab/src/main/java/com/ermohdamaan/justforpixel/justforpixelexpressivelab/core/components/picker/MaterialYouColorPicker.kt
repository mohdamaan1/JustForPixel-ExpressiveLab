package com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.picker

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

/**
 * Data model representing a 4-Role Advanced Material 3 Custom Color Palette.
 *
 * Allows full independent control over Primary, Secondary, Tertiary, and Surface
 * colors for creating wild, vibrant, and personalized theme combinations.
 *
 * @property name Human-readable title of the palette theme
 * @property primary Main brand / action color
 * @property secondary Accent / chip / toggle control color
 * @property tertiary Highlight / badge / status indicator color
 * @property surface Card / background container tint color
 * @property isMonetWallpaper Whether this palette is extracted from Android 12+ wallpaper Monet
 *
 * @author Er. Mohd Amaan
 */
data class AatrangiPalette(
    val name: String,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val surface: Color,
    val isMonetWallpaper: Boolean = false
)

/**
 * Pre-defined Custom Material 3 Color Palettes.
 *
 * @author Er. Mohd Amaan
 */
object AatrangiDefaults {

    /** Cyberpunk Neon Theme - Electric Blue + Hot Pink + Yellow */
    val CyberpunkNeon = AatrangiPalette(
        name = "Cyberpunk Neon",
        primary = Color(0xFF00E5FF),
        secondary = Color(0xFFFF007F),
        tertiary = Color(0xFFFFEA00),
        surface = Color(0xFF12121A)
    )

    /** Sunset Flame Theme - Crimson Fire + Amber Sunrise + Magenta Flame */
    val SunsetFlame = AatrangiPalette(
        name = "Sunset Flame",
        primary = Color(0xFFFF3D00),
        secondary = Color(0xFFFF9100),
        tertiary = Color(0xFFD500F9),
        surface = Color(0xFF1C1210)
    )

    /** Emerald Forest Theme - Mint Emerald + Forest Teal + Lime Glow */
    val EmeraldForest = AatrangiPalette(
        name = "Emerald Forest",
        primary = Color(0xFF00E676),
        secondary = Color(0xFF1DE9B6),
        tertiary = Color(0xFFC6FF00),
        surface = Color(0xFF0B1914)
    )

    /** Galactic Nebula Theme - Violet Deep + Cosmic Magenta + Azure Blue */
    val GalacticNebula = AatrangiPalette(
        name = "Galactic Nebula",
        primary = Color(0xFF7C4DFF),
        secondary = Color(0xFFE040FB),
        tertiary = Color(0xFF00B0FF),
        surface = Color(0xFF110E1C)
    )

    /** Gold Luxury Theme - Pure Gold + Deep Amber + Vivid Orange */
    val GoldLuxury = AatrangiPalette(
        name = "Gold Luxury",
        primary = Color(0xFFFFD700),
        secondary = Color(0xFFFFAB00),
        tertiary = Color(0xFFFF6D00),
        surface = Color(0xFF1A1608)
    )

    /** Pastel Candy Theme - Lavender Soft + Bubblegum Pink + Sky Cyan */
    val PastelCandy = AatrangiPalette(
        name = "Pastel Candy",
        primary = Color(0xFFB388FF),
        secondary = Color(0xFFFF80AB),
        tertiary = Color(0xFF80D8FF),
        surface = Color(0xFF16121D)
    )

    /** List of all pre-configured expressive presets */
    val AllPresets = listOf(
        CyberpunkNeon,
        SunsetFlame,
        EmeraldForest,
        GalacticNebula,
        GoldLuxury,
        PastelCandy
    )

    /** Preset Color Swatches for Custom Primary/Secondary/Tertiary/Surface Mixer */
    val SwatchOptions = listOf(
        Color(0xFF6750A4), Color(0xFF006399), Color(0xFF006C4C), Color(0xFFB52700),
        Color(0xFF00E5FF), Color(0xFFFF007F), Color(0xFFFFEA00), Color(0xFF7C4DFF),
        Color(0xFFFF3D00), Color(0xFF00E676), Color(0xFFFFD700), Color(0xFFE040FB),
        Color(0xFF80D8FF), Color(0xFFFF80AB), Color(0xFF1C1210), Color(0xFF12121A)
    )
}

/**
 * Extracts real Android 12+ (API 31+) Wallpaper Monet dynamic colors at runtime.
 *
 * If run on Android 12+, it uses [dynamicDarkColorScheme] or [dynamicLightColorScheme]
 * to extract actual wallpaper-generated primary, secondary, tertiary, and surface colors.
 *
 * @return [AatrangiPalette] populated with actual system wallpaper Monet colors.
 * @author Er. Mohd Amaan
 */
@Composable
fun rememberMonetWallpaperPalette(): AatrangiPalette {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    return remember(isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val monetScheme = if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            AatrangiPalette(
                name = "System Wallpaper (Monet)",
                primary = monetScheme.primary,
                secondary = monetScheme.secondary,
                tertiary = monetScheme.tertiary,
                surface = monetScheme.surface,
                isMonetWallpaper = true
            )
        } else {
            // Fallback for pre-Android 12 devices
            val fallback = if (isDark) darkColorScheme() else lightColorScheme()
            AatrangiPalette(
                name = "System Default",
                primary = fallback.primary,
                secondary = fallback.secondary,
                tertiary = fallback.tertiary,
                surface = fallback.surface,
                isMonetWallpaper = false
            )
        }
    }
}

/**
 * Expressive Swatch Circle Button with bouncy spring physics & haptics.
 *
 * @param color Color to display inside swatch circle
 * @param isSelected Whether this swatch is currently active
 * @param onClick Triggered when tapped
 * @param modifier Custom modifier
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun AatrangiColorSwatchItem(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val hapticFeedback = LocalHapticFeedback.current

    // Bouncy scale physics animation on tap
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> TactileMotionTokens.PRESS_SCALE_FAB
            isSelected -> 1.18f
            else -> 1.0f
        },
        animationSpec = TactileMotionTokens.bouncySpring(),
        label = "AatrangiSwatchScale"
    )

    // Smooth border color fade transition
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = TactileMotionTokens.gentleSpring(),
        label = "AatrangiSwatchBorder"
    )

    Box(
        modifier = modifier
            .padding(4.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .size(40.dp)
            .clip(CircleShape)
            .border(3.dp, borderColor, CircleShape)
            .background(color)
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
        if (isSelected) {
            val checkColor = if (color.luminance() > 0.45f) Color.Black else Color.White
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = "Selected",
                tint = checkColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Advanced Settings Row Card displaying active Custom Theme (Primary, Secondary, Tertiary, Surface) Palette.
 *
 * @param activePalette Currently selected [AatrangiPalette]
 * @param onPaletteSelected Callback when user chooses a palette
 * @param isMonetActive Whether system wallpaper Monet mode is active
 * @param onMonetToggle Callback for toggling system wallpaper Monet mode
 * @param modifier Custom modifier
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun MaterialYouColorPickerListItem(
    activePalette: AatrangiPalette,
    onPaletteSelected: (AatrangiPalette) -> Unit,
    modifier: Modifier = Modifier,
    isMonetActive: Boolean = false,
    onMonetToggle: ((Boolean) -> Unit)? = null
) {
    val supportsMonet = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val monetWallpaperPalette = rememberMonetWallpaperPalette()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeCache.smooth20)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(16.dp)
    ) {
        // Header Row with Icon, Title, Subtitle, and Monet Switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(ShapeCache.smooth12)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Palette,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Material You & Custom Theme",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isMonetActive) "Active: System Wallpaper Monet" else "Active: ${activePalette.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (supportsMonet && onMonetToggle != null) {
                Switch(
                    checked = isMonetActive,
                    onCheckedChange = { enabled ->
                        onMonetToggle(enabled)
                        if (enabled) {
                            onPaletteSelected(monetWallpaperPalette)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Live 4-Role Color Swatch Preview Pill (Primary, Secondary, Tertiary, Surface)
        val currentDisplay = if (isMonetActive) monetWallpaperPalette else activePalette
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ShapeCache.smooth16)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorRoleCircle("Primary", currentDisplay.primary)
            ColorRoleCircle("Secondary", currentDisplay.secondary)
            ColorRoleCircle("Tertiary", currentDisplay.tertiary)
            ColorRoleCircle("Surface", currentDisplay.surface)
        }

        // Horizontal Preset Selector when Monet is OFF
        AnimatedVisibility(visible = !isMonetActive) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(AatrangiDefaults.AllPresets) { preset ->
                        Surface(
                            shape = ShapeCache.smooth12,
                            color = if (activePalette.name == preset.name) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                            modifier = Modifier
                                .clip(ShapeCache.smooth12)
                                .clickable { onPaletteSelected(preset) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(preset.primary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = preset.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activePalette.name == preset.name) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Small helper composable displaying a labeled color role circle.
 */
@Composable
private fun ColorRoleCircle(label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Advanced Floating Dialog for configuring Material You & Custom Theme Palettes.
 *
 * Features 3 Modes:
 * 1. Monet System Wallpaper Color Extraction (Android 12+ real wallpaper colors)
 * 2. Pre-defined Custom Presets (Cyberpunk, Sunset Flame, Emerald, Nebula, Gold, Candy)
 * 3. Custom 4-Color Mixer (User can independently pick Primary, Secondary, Tertiary, Surface)
 *
 * @param onDismissRequest Dialog dismiss callback
 * @param activePalette Currently active palette
 * @param onPaletteSelected Callback when user confirms a new palette
 * @param isMonetActive System wallpaper Monet toggle state
 * @param onMonetToggle Callback for toggling Monet state
 * @param modifier Custom modifier
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialYouColorPickerDialog(
    onDismissRequest: () -> Unit,
    activePalette: AatrangiPalette,
    onPaletteSelected: (AatrangiPalette) -> Unit,
    modifier: Modifier = Modifier,
    isMonetActive: Boolean = false,
    onMonetToggle: ((Boolean) -> Unit)? = null
) {
    val haptic = LocalHapticFeedback.current
    val supportsMonet = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val monetWallpaperPalette = rememberMonetWallpaperPalette()

    // Dialog Internal Tab Selection: 0 = Wallpaper Monet, 1 = Presets, 2 = Custom Mixer
    var selectedTab by remember { mutableIntStateOf(if (isMonetActive) 0 else 1) }

    // Temporary working palette state before applying
    var tempPalette by remember { mutableStateOf(activePalette) }

    // Independent Custom Role Color Pickers
    var customPrimary by remember { mutableStateOf(activePalette.primary) }
    var customSecondary by remember { mutableStateOf(activePalette.secondary) }
    var customTertiary by remember { mutableStateOf(activePalette.tertiary) }
    var customSurface by remember { mutableStateOf(activePalette.surface) }

    // Role selector index for custom mixer: 0 = Primary, 1 = Secondary, 2 = Tertiary, 3 = Surface
    var activeRoleIndex by remember { mutableIntStateOf(0) }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        Surface(
            shape = ShapeCache.smooth24,
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Hero Header Icon
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ColorLens,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Custom Theme Studio",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Mix & match Primary, Secondary, Tertiary & Surface colors",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                // 3-Segment Tab Switcher (Monet / Presets / Custom Mixer)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(ShapeCache.smoothPill)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (supportsMonet) {
                        TabSegmentButton("Monet", selectedTab == 0) {
                            haptic.performHapticFeedback(TactileMotionTokens.hapticTap)
                            selectedTab = 0
                            tempPalette = monetWallpaperPalette
                        }
                    }
                    TabSegmentButton("Presets", selectedTab == 1) {
                        haptic.performHapticFeedback(TactileMotionTokens.hapticTap)
                        selectedTab = 1
                    }
                    TabSegmentButton("Custom Mixer", selectedTab == 2) {
                        haptic.performHapticFeedback(TactileMotionTokens.hapticTap)
                        selectedTab = 2
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // TAB 0: System Wallpaper Monet Extracted Palette
                if (selectedTab == 0 && supportsMonet) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(ShapeCache.smooth16)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Live Wallpaper Monet Palette",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Extracted directly from your Android device wallpaper",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ColorRoleCircle("Primary", monetWallpaperPalette.primary)
                            ColorRoleCircle("Secondary", monetWallpaperPalette.secondary)
                            ColorRoleCircle("Tertiary", monetWallpaperPalette.tertiary)
                            ColorRoleCircle("Surface", monetWallpaperPalette.surface)
                        }
                    }
                }

                // TAB 1: Pre-defined Custom Presets Grid
                if (selectedTab == 1) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Select Custom Theme Preset",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items(AatrangiDefaults.AllPresets) { preset ->
                                Surface(
                                    shape = ShapeCache.smooth16,
                                    color = if (tempPalette.name == preset.name) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                                    modifier = Modifier
                                        .clip(ShapeCache.smooth16)
                                        .clickable {
                                            haptic.performHapticFeedback(TactileMotionTokens.hapticTap)
                                            tempPalette = preset
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = preset.name,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (tempPalette.name == preset.name) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Box(Modifier.size(16.dp).clip(CircleShape).background(preset.primary))
                                            Box(Modifier.size(16.dp).clip(CircleShape).background(preset.secondary))
                                            Box(Modifier.size(16.dp).clip(CircleShape).background(preset.tertiary))
                                            Box(Modifier.size(16.dp).clip(CircleShape).background(preset.surface))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // TAB 2: Custom 4-Color Independent Mixer
                if (selectedTab == 2) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Pick Role to Customize:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        // Role Selector Chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            RoleChip("Primary", customPrimary, activeRoleIndex == 0, onClick = { activeRoleIndex = 0 }, modifier = Modifier.weight(1f))
                            RoleChip("Secondary", customSecondary, activeRoleIndex == 1, onClick = { activeRoleIndex = 1 }, modifier = Modifier.weight(1f))
                            RoleChip("Tertiary", customTertiary, activeRoleIndex == 2, onClick = { activeRoleIndex = 2 }, modifier = Modifier.weight(1f))
                            RoleChip("Surface", customSurface, activeRoleIndex == 3, onClick = { activeRoleIndex = 3 }, modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Color Swatch Matrix for selected role
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items(AatrangiDefaults.SwatchOptions) { color ->
                                val currentRoleColor = when (activeRoleIndex) {
                                    0 -> customPrimary
                                    1 -> customSecondary
                                    2 -> customTertiary
                                    else -> customSurface
                                }

                                AatrangiColorSwatchItem(
                                    color = color,
                                    isSelected = currentRoleColor == color,
                                    onClick = {
                                        when (activeRoleIndex) {
                                            0 -> customPrimary = color
                                            1 -> customSecondary = color
                                            2 -> customTertiary = color
                                            else -> customSurface = color
                                        }
                                        tempPalette = AatrangiPalette(
                                            name = "Custom Theme",
                                            primary = customPrimary,
                                            secondary = customSecondary,
                                            tertiary = customTertiary,
                                            surface = customSurface
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons (Cancel / Apply)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            val isMonetChosen = selectedTab == 0 && supportsMonet
                            if (onMonetToggle != null) {
                                onMonetToggle(isMonetChosen)
                            }
                            onPaletteSelected(if (isMonetChosen) monetWallpaperPalette else tempPalette)
                            onDismissRequest()
                        }
                    ) {
                        Text("Apply Theme", fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun TabSegmentButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = ShapeCache.smoothPill,
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        modifier = Modifier
            .clip(ShapeCache.smoothPill)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun RoleChip(
    label: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = ShapeCache.smooth12,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = modifier
            .clip(ShapeCache.smooth12)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(Modifier.size(10.dp).clip(CircleShape).background(color))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                softWrap = false,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
