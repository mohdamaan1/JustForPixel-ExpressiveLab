package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.GridOn
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RadioButtonChecked
import androidx.compose.material.icons.rounded.SmartButton
import androidx.compose.material.icons.rounded.Style
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material.icons.rounded.Tune
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.components.ExpressiveFloatingBottomBar
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.cards.ExpressiveCard
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

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
    onCategorySelected: (String) -> Unit
) {
    var selectedBottomTab by remember { mutableIntStateOf(0) } // 0 = Catalog, 1 = Settings

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = selectedBottomTab,
            transitionSpec = {
                fadeIn(animationSpec = tween(140)) togetherWith fadeOut(animationSpec = tween(120))
            },
            label = "FastTabTransition"
        ) { tab ->
            if (tab == 0) {
                CatalogGridView(onCategorySelected = onCategorySelected)
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
    onCategorySelected: (String) -> Unit
) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            // Header Info Banner
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Code,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Open Source Component Library",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "implementation(\"com.github.ermohdamaan:expressivelab:1.0.0\")",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                    val numberTag = String.format("%02d", index + 1)

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
