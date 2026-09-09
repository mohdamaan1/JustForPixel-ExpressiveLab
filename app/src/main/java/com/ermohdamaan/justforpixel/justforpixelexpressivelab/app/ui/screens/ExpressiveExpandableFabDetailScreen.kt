package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Code
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
import androidx.compose.material3.Switch
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.fab.ExpressiveExpandableFab
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.fab.ExpressiveFabDefaults
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.fab.ExpressiveFabSize
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.fab.FabExpansionMode
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.TactileMotionTokens

/**
 * Interactive Playground Detail Screen for Expressive Morphing Expandable FAB.
 *
 * Features:
 * - Live Speed Dial FAB Playground with Vertical and Horizontal Expansion Modes.
 * - M3 FAB Size Selector (Small, Standard, Large, Extended FAB).
 * - Architecture & Design Specs Explanation Card.
 * - Source Code Viewer with Copy to Clipboard.
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveExpandableFabDetailScreen(
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val hapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    // Playground Mode State (Vertical or Horizontal)
    var expansionMode by remember { mutableStateOf(FabExpansionMode.VerticalSpeedDial) }
    var fabSize by remember { mutableStateOf(ExpressiveFabSize.Standard) }
    var isExtended by remember { mutableStateOf(false) }

    var showSourceCode by remember { mutableStateOf(false) }
    var isCodeCopied by remember { mutableStateOf(false) }

    val sampleCode = """
// Material Design 3 Expressive FAB Integration
// Author: Er. Mohd Amaan

ExpressiveExpandableFab(
    actions = ExpressiveFabDefaults.CreationCluster,
    fabSize = ExpressiveFabSize.Standard, // Small (40dp), Standard (56dp), Large (96dp)
    extendedLabel = if (isExtended) "Create New" else null,
    expansionMode = FabExpansionMode.VerticalSpeedDial,
    onActionClick = { actionId ->
        when (actionId) {
            "camera" -> // Open Camera
            "gallery" -> // Open Gallery
        }
    }
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
                            text = "Expandable Speed Dial FAB",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Morphing Action Cluster & M3 Specs",
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
                                text = "M3 SPECS v1.1.0",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shape = ShapeCache.smooth20,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Expansion Mode:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Mode Switcher Buttons
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(ShapeCache.smoothPill)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    shape = ShapeCache.smoothPill,
                                    color = if (expansionMode == FabExpansionMode.VerticalSpeedDial) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(ShapeCache.smoothPill)
                                        .clickable {
                                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                            expansionMode = FabExpansionMode.VerticalSpeedDial
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Vertical Speed Dial",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (expansionMode == FabExpansionMode.VerticalSpeedDial) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Surface(
                                    shape = ShapeCache.smoothPill,
                                    color = if (expansionMode == FabExpansionMode.HorizontalPillBar) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(ShapeCache.smoothPill)
                                        .clickable {
                                            hapticFeedback.performHapticFeedback(TactileMotionTokens.hapticTap)
                                            expansionMode = FabExpansionMode.HorizontalPillBar
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Horizontal Pill Bar",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (expansionMode == FabExpansionMode.HorizontalPillBar) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            // Extended FAB Toggle Row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(ShapeCache.smooth16)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Extended FAB (Icon + Text)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Switch(
                                    checked = isExtended,
                                    onCheckedChange = { isExtended = it }
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .clip(ShapeCache.smooth16)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Tap the floating FAB at bottom-right to test morphing speed dial expansion!",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // ARCHITECTURE SPECS
                item {
                    Text(
                        text = "MATERIAL DESIGN 3 FAB SPECS",
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
                                    text = "Official M3 FAB Guidelines",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "• Small FAB (40dp), Standard FAB (56dp), Large FAB (96dp)\n" +
                                        "• Extended FAB (Icon + Text label expansion)\n" +
                                        "• Icon rotates 135° smoothly on toggle (+ to X)\n" +
                                        "• Corner radius morphing from Squircle (20dp) to Pill Circle (50dp)",
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
                                                val clip = ClipData.newPlainText("ExpandableFab Code", sampleCode)
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

            // FLOATING EXPANDABLE FAB OVERLAY AT BOTTOM-RIGHT
            ExpressiveExpandableFab(
                actions = ExpressiveFabDefaults.CreationCluster,
                fabSize = fabSize,
                extendedLabel = if (isExtended) "Create New" else null,
                expansionMode = expansionMode,
                onActionClick = { actionId ->
                    Toast.makeText(context, "Triggered Speed Dial Action: $actionId", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            )
        }
    }
}
