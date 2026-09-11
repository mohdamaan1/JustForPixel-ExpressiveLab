package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.GridOn
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RadioButtonChecked
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SmartButton
import androidx.compose.material.icons.rounded.Style
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.components.ExpressiveFloatingBottomBar
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.cards.ExpressiveCard
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens
import kotlin.math.abs

/**
 * Data class representing a Component Category Item in the Dashboard.
 *
 * @author Er. Mohd Amaan
 */
data class ComponentCategory(
    val id: String,
    val title: String,
    val description: String,
    val countText: String,
    val icon: ImageVector,
    val badgeBgColor: Color,
    val badgeIconColor: Color
)

/**
 * Main Category Dashboard Home Screen for ExpressiveLab M3.
 *
 * Features:
 * - Numbering OUTSIDE the cards on the left for maximum space.
 * - Organized vertical card layout with 100% full title readability (NO truncation!).
 * - Ultra-fast zero lag tab switching using AnimatedContent.
 * - Centered Floating Navigation Bar.
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentCategoryHomeScreen(
    onCategorySelected: (String) -> Unit,
    catalogLazyGridState: LazyGridState = rememberLazyGridState()
) {
    var selectedBottomTab by rememberSaveable { mutableIntStateOf(0) } // 0 = Catalog, 1 = Settings

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = selectedBottomTab,
            transitionSpec = {
                fadeIn(animationSpec = tween(140)) togetherWith fadeOut(animationSpec = tween(120))
            },
            label = "FastTabTransition"
        ) { tab ->
            if (tab == 0) {
                CatalogGridView(
                    lazyGridState = catalogLazyGridState,
                    onCategorySelected = onCategorySelected
                )
            } else {
                AppSettingsScreen()
            }
        }

        // CENTERED FLOATING BOTTOM NAVIGATION BAR
        ExpressiveFloatingBottomBar(
            selectedTab = selectedBottomTab,
            onTabSelected = { selectedBottomTab = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatalogGridView(
    lazyGridState: LazyGridState = rememberLazyGridState(),
    onCategorySelected: (String) -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val hapticFeedback = LocalHapticFeedback.current

    val categories = listOf(
        ComponentCategory(
            id = "material_you_picker",
            title = "Material You Color Picker",
            description = "Monet dynamic color toggle & Expressive seed palette swatch selector",
            countText = "NEW • 2 Variants",
            icon = Icons.Rounded.Palette,
            badgeBgColor = MaterialTheme.colorScheme.primaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        ComponentCategory(
            id = "expressive_toolbar",
            title = "Expressive Floating Toolbar",
            description = "Floating pill container wrapper combining HorizontalToolbar + ToggleButtons",
            countText = "NEW • 2 Presets",
            icon = Icons.Rounded.Navigation,
            badgeBgColor = MaterialTheme.colorScheme.tertiaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        ComponentCategory(
            id = "m3_heatmap",
            title = "M3 Activity Heatmap",
            description = "Expressive contribution grid with streak counter & inspection tooltips",
            countText = "NEW • Grid View",
            icon = Icons.Rounded.GridOn,
            badgeBgColor = MaterialTheme.colorScheme.secondaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        ComponentCategory(
            id = "wavy_progress_card",
            title = "Wavy Progress Dashboard Card",
            description = "CircularWavyProgressIndicator ring combined with 3-metric stats grid & timer controls",
            countText = "NEW • Dashboard Card",
            icon = Icons.Rounded.RadioButtonChecked,
            badgeBgColor = MaterialTheme.colorScheme.primaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        ComponentCategory(
            id = "live_notification",
            title = "Android 16 Live Activity Notifications",
            description = "Promoted status bar chip & ongoing foreground service activity notifications",
            countText = "NEW • Live Service",
            icon = Icons.Rounded.Notifications,
            badgeBgColor = MaterialTheme.colorScheme.tertiaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        ComponentCategory(
            id = "pull_to_refresh",
            title = "Elastic Pull-To-Refresh",
            description = "Gesture-driven liquid wavy pull container with rubber-band damping physics",
            countText = "NEW • Liquid Wave",
            icon = Icons.Rounded.Refresh,
            badgeBgColor = MaterialTheme.colorScheme.secondaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        ComponentCategory(
            id = "swipe_to_dismiss",
            title = "Elastic Swipe-To-Dismiss",
            description = "Swipeable card row with morphing action icons, rubber-band physics & undo hook",
            countText = "NEW • Morph Actions",
            icon = Icons.Rounded.Delete,
            badgeBgColor = MaterialTheme.colorScheme.primaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        ComponentCategory(
            id = "expandable_fab",
            title = "Expandable Speed Dial FAB",
            description = "Hero FAB morphing from Squircle to expanded speed dial action cluster & pill bar",
            countText = "NEW • Speed Dial",
            icon = Icons.Rounded.SmartButton,
            badgeBgColor = MaterialTheme.colorScheme.tertiaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        ComponentCategory(
            id = "glassmorphism_card",
            title = "Material 3 Glassmorphism Card",
            description = "Official M3 Card + Material You dynamic frost gradient overlay & backdrop blur",
            countText = "NEW • M3 Glass",
            icon = Icons.Rounded.Style,
            badgeBgColor = MaterialTheme.colorScheme.secondaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        ComponentCategory(
            id = "orbit_halo_button",
            title = "Expressive Orbit Halo Button",
            description = "Wi-Fi connectivity button with clockwise arc sweep & sequential orbital dot ring",
            countText = "NEW • Orbit Halo",
            icon = Icons.Rounded.Wifi,
            badgeBgColor = MaterialTheme.colorScheme.primaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        ComponentCategory(
            id = "push_buttons",
            title = "Elastic Push Controls",
            description = "Layout weight expansion physics & neighbor push (dhakka) effect",
            countText = "1 Playground",
            icon = Icons.Rounded.TouchApp,
            badgeBgColor = MaterialTheme.colorScheme.primaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        ComponentCategory(
            id = "morph_buttons",
            title = "Morphing Icon Buttons",
            description = "Standalone corner radius morphing between Circle, Squircle & Pill",
            countText = "1 Playground",
            icon = Icons.Rounded.SmartButton,
            badgeBgColor = MaterialTheme.colorScheme.secondaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        ComponentCategory(
            id = "floating_bar",
            title = "Floating Bottom Bar",
            description = "Material 3 Expressive pill navigation bar with active label expansion",
            countText = "1 Playground",
            icon = Icons.Rounded.Navigation,
            badgeBgColor = MaterialTheme.colorScheme.tertiaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        ComponentCategory(
            id = "shape_morph_loading",
            title = "Shape-Morph Loading",
            description = "Material 3 Expressive spinners that continuously morph shapes while loading",
            countText = "1 Playground",
            icon = Icons.Rounded.Sync,
            badgeBgColor = MaterialTheme.colorScheme.surfaceVariant,
            badgeIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        ComponentCategory(
            id = "organic_shapes",
            title = "Organic & Geometric Shapes",
            description = "Star polygon, Heart, Teardrop, and Superellipse Squircle vector geometries",
            countText = "4 Shapes",
            icon = Icons.Rounded.Category,
            badgeBgColor = MaterialTheme.colorScheme.primaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        ComponentCategory(
            id = "sliders",
            title = "Wavy & Arc Sliders",
            description = "Live wave animation amplitude, dynamic gaps & curved arc seeking",
            countText = "2 Variants",
            icon = Icons.Rounded.Tune,
            badgeBgColor = MaterialTheme.colorScheme.secondaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        ComponentCategory(
            id = "progress",
            title = "Wavy & Solid Progress",
            description = "Official Google Circular & Linear Wavy indicators with 1-min loop",
            countText = "4 Variants",
            icon = Icons.Rounded.RadioButtonChecked,
            badgeBgColor = MaterialTheme.colorScheme.tertiaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        ComponentCategory(
            id = "segmented",
            title = "Segmented Toggle Rows",
            description = "Animated background fill & corner radius morphing with push physics",
            countText = "2 Variants",
            icon = Icons.Rounded.Equalizer,
            badgeBgColor = MaterialTheme.colorScheme.surfaceVariant,
            badgeIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        ComponentCategory(
            id = "visualizers",
            title = "Audio Equalizer Bar",
            description = "Jumping frequency bar icon with live active state morphing",
            countText = "1 Variant",
            icon = Icons.Rounded.GraphicEq,
            badgeBgColor = MaterialTheme.colorScheme.secondaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        ComponentCategory(
            id = "cards",
            title = "Bouncy Squircle Cards",
            description = "Tactile press response with elevation physics & smooth corners",
            countText = "2 Variants",
            icon = Icons.Rounded.Style,
            badgeBgColor = MaterialTheme.colorScheme.tertiaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
        ComponentCategory(
            id = "all_playgrounds",
            title = "Full Overview Playground",
            description = "View all ExpressiveLab components together in a single combined showcase",
            countText = "All 10 Sections",
            icon = Icons.Rounded.AutoAwesome,
            badgeBgColor = MaterialTheme.colorScheme.primaryContainer,
            badgeIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )

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
                            text = "ExpressiveLab Catalog",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "By Er. Mohd Amaan",
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
                                text = "v1.2.0",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            // Ultra-Compact 1-Line Support Banner Pill (Takes up ~38dp height!)
            Surface(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://buymeacoffee.com/justforpixel"))
                    context.startActivity(intent)
                },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.85f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "☕ Support ExpressiveLab",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• Buy Me a Coffee",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                        )
                    }

                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = "Support",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Official @AndroidDev Recognition Featured Pill Banner
            Surface(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://x.com/AndroidDev"))
                    try {
                        context.startActivity(intent)
                    } catch (_: Exception) { }
                },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Verified,
                            contentDescription = "Verified",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Featured by @AndroidDev: “Love this!”",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "Official",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Text(
                text = "SELECT COMPONENT CATEGORY",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            // Category List with Numbering OUTSIDE the Cards & Full Un-truncated Titles
            LazyVerticalGrid(
                state = lazyGridState,
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding() + 90.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(categories) { index, category ->
                    val numberTag = if (index + 1 < 10) "0${index + 1}" else "${index + 1}"

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 1. NUMBERING OUTSIDE THE CARD (LEFT SIDE)
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(34.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = numberTag,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        // 2. ORGANIZED EXPRESSIVE CARD WITH FULL TITLE (NO TRUNCATION!)
                        ExpressiveCard(
                            onClick = {
                                hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                onCategorySelected(category.id)
                            },
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // TOP ROW: Icon + Full Title + Arrow
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(category.badgeBgColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = category.icon,
                                            contentDescription = category.title,
                                            tint = category.badgeIconColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = category.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(modifier = Modifier.width(6.dp))

                                    Icon(
                                        imageVector = Icons.Rounded.ArrowForward,
                                        contentDescription = "Open Category",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                // MIDDLE ROW: Description Text
                                Text(
                                    text = category.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 16.sp
                                )

                                // BOTTOM ROW: Variant Count Chip Tag
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    val isNewItem = category.countText.contains("NEW")
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isNewItem) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = category.countText,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isNewItem) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                                            fontSize = 11.5.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
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
}
