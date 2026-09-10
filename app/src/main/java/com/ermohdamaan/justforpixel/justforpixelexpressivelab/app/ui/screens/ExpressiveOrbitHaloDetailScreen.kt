package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Info
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.buttons.ExpressiveHaloType
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.buttons.ExpressiveOrbitHaloButton
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.components.cards.ExpressiveCard
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.core.theme.ShapeCache

/**
 * Interactive Playground Detail Screen for Material 3 Expressive Status Halo Buttons.
 *
 * Displays all 5 Expressive Status Halo Buttons in a single line:
 * 1. Wi-Fi (Clockwise Arc Sweep + Sequential Dots)
 * 2. Bluetooth (Interlocking Dual Wave Halos)
 * 3. DND (Crescent Moon Arc + Muted Star Pulse)
 * 4. Airplane Mode (Flight Contrail Arc + Jet Sky Orbit)
 * 5. Music (Equalizer Frequency Wave Ring)
 *
 * @author Er. Mohd Amaan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpressiveOrbitHaloDetailScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // Independent power states for all 5 buttons in the single row
    var isWifiActive by remember { mutableStateOf(true) }
    var isBluetoothActive by remember { mutableStateOf(true) }
    var isDndActive by remember { mutableStateOf(false) }
    var isAirplaneActive by remember { mutableStateOf(false) }
    var isMusicActive by remember { mutableStateOf(true) }

    var isSearchingMode by remember { mutableStateOf(false) }
    var showContainerBg by remember { mutableStateOf(false) }
    var dotCountVal by remember { mutableFloatStateOf(5f) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Expressive Status Halo Buttons",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Wi-Fi, Bluetooth, DND, Airplane & Music Cluster",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back to Catalog"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "5 Buttons Cluster",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 11.sp
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HERO PLAYGROUND: ALL 5 EXPRESSIVE HALO BUTTONS IN A SINGLE LINE
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                shape = ShapeCache.smooth24,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HERO CLUSTER: ALL 5 STATUS HALOS IN ONE LINE",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // SINGLE LINE ROW WITH ALL 5 BUTTONS
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 1. Wi-Fi
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ExpressiveOrbitHaloButton(
                                onClick = { isWifiActive = !isWifiActive },
                                active = isWifiActive,
                                isSearching = isSearchingMode,
                                haloType = ExpressiveHaloType.Wifi,
                                size = 52.dp,
                                iconSize = 22.dp,
                                dotCount = dotCountVal.toInt(),
                                showContainerBackground = showContainerBg
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Wi-Fi", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, fontSize = 10.5.sp)
                        }

                        // 2. Bluetooth
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ExpressiveOrbitHaloButton(
                                onClick = { isBluetoothActive = !isBluetoothActive },
                                active = isBluetoothActive,
                                isSearching = isSearchingMode,
                                haloType = ExpressiveHaloType.Bluetooth,
                                size = 52.dp,
                                iconSize = 22.dp,
                                dotCount = dotCountVal.toInt(),
                                showContainerBackground = showContainerBg
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Bluetooth", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, fontSize = 10.5.sp)
                        }

                        // 3. DND
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ExpressiveOrbitHaloButton(
                                onClick = { isDndActive = !isDndActive },
                                active = isDndActive,
                                haloType = ExpressiveHaloType.DoNotDisturb,
                                size = 52.dp,
                                iconSize = 22.dp,
                                dotCount = dotCountVal.toInt(),
                                showContainerBackground = showContainerBg
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("DND", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, fontSize = 10.5.sp)
                        }

                        // 4. Airplane Mode
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ExpressiveOrbitHaloButton(
                                onClick = { isAirplaneActive = !isAirplaneActive },
                                active = isAirplaneActive,
                                haloType = ExpressiveHaloType.AirplaneMode,
                                size = 52.dp,
                                iconSize = 22.dp,
                                dotCount = dotCountVal.toInt(),
                                showContainerBackground = showContainerBg
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Airplane", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, fontSize = 10.5.sp)
                        }

                        // 5. Music
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            ExpressiveOrbitHaloButton(
                                onClick = { isMusicActive = !isMusicActive },
                                active = isMusicActive,
                                haloType = ExpressiveHaloType.Music,
                                size = 52.dp,
                                iconSize = 22.dp,
                                dotCount = dotCountVal.toInt(),
                                showContainerBackground = showContainerBg
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Music", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, fontSize = 10.5.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tap each status button step-by-step in line to test its unique expressive halo animation!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.5.sp
                    )
                }
            }

            // CONTROLS & CONFIGURATION PANEL
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                shape = ShapeCache.smooth20,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "ORBIT HALO CONTROLS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )

                    // 1. Continuous Searching Orbit Loop Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Searching / Rhythm Orbit Loop",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Continuous 360° rotation loop for Wi-Fi & Bluetooth",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                        Switch(
                            checked = isSearchingMode,
                            onCheckedChange = { isSearchingMode = it }
                        )
                    }

                    // 2. Transparent vs Solid Container Background Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Solid Container Background",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Off = Clean transparent status bar look (Pixel style)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                        Switch(
                            checked = showContainerBg,
                            onCheckedChange = { showContainerBg = it }
                        )
                    }

                    // 3. Dot Count Adjustment Slider (Max 6 Dots)
                    Column {
                        Text(
                            text = "Bottom Sequential Dots: ${dotCountVal.toInt()} Dots (Max 6)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Slider(
                            value = dotCountVal,
                            onValueChange = { dotCountVal = it },
                            valueRange = 3f..6f,
                            steps = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // PLAYGROUND 2: FULL QUICK SETTINGS & STATUS BAR MOCKUP CLUSTER
            ExpressiveCard(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "STATUS BAR & QUICK SETTINGS HALO CLUSTER",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )

                    // Phone Status Bar Simulation with All 5 Expressive Orbit Buttons
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "9:41",
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleMedium
                            )

                            // Status Bar Right Icon Cluster
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // 1. Wi-Fi
                                ExpressiveOrbitHaloButton(
                                    onClick = { isWifiActive = !isWifiActive },
                                    active = isWifiActive,
                                    haloType = ExpressiveHaloType.Wifi,
                                    size = 38.dp,
                                    iconSize = 18.dp,
                                    showContainerBackground = showContainerBg
                                )

                                // 2. Bluetooth
                                ExpressiveOrbitHaloButton(
                                    onClick = { isBluetoothActive = !isBluetoothActive },
                                    active = isBluetoothActive,
                                    haloType = ExpressiveHaloType.Bluetooth,
                                    size = 38.dp,
                                    iconSize = 18.dp,
                                    showContainerBackground = showContainerBg
                                )

                                // 3. DND
                                ExpressiveOrbitHaloButton(
                                    onClick = { isDndActive = !isDndActive },
                                    active = isDndActive,
                                    haloType = ExpressiveHaloType.DoNotDisturb,
                                    size = 38.dp,
                                    iconSize = 18.dp,
                                    showContainerBackground = showContainerBg
                                )

                                // 4. Airplane Mode
                                ExpressiveOrbitHaloButton(
                                    onClick = { isAirplaneActive = !isAirplaneActive },
                                    active = isAirplaneActive,
                                    haloType = ExpressiveHaloType.AirplaneMode,
                                    size = 38.dp,
                                    iconSize = 18.dp,
                                    showContainerBackground = showContainerBg
                                )

                                // 5. Music
                                ExpressiveOrbitHaloButton(
                                    onClick = { isMusicActive = !isMusicActive },
                                    active = isMusicActive,
                                    haloType = ExpressiveHaloType.Music,
                                    size = 38.dp,
                                    iconSize = 18.dp,
                                    showContainerBackground = showContainerBg
                                )
                            }
                        }
                    }
                }
            }

            // PRACTICAL INTEGRATION USE CASES & ARCHITECTURE SPECS
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                shape = ShapeCache.smooth20,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
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
                            text = "Practical Integration Use Cases",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "1. Expressive Floating Bottom Navigation Bar:\n" +
                               "   Place as an active connectivity status badge or custom tab action inside ExpressiveFloatingBottomBar to show live Wi-Fi or Bluetooth status.\n\n" +
                               "2. Quick Settings & Status Bar Header Cluster:\n" +
                               "   Integrate in top status bar or quick settings panel for 1-tap Wi-Fi, Bluetooth, DND, Airplane Mode & Music toggles.\n\n" +
                               "3. Floating Media Player Controls:\n" +
                               "   Use ExpressiveHaloType.Music as an animated audio frequency halo inside floating music player bars or audio visualizers.\n\n" +
                               "4. IoT & Smart Home Control Dashboards:\n" +
                               "   Ideal for smart device cards to indicate real-time searching, pairing, or connected device states with 2-tone Material You colors.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }

            // CODE IMPLEMENTATION VIEWER
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                shape = ShapeCache.smooth20,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "INTEGRATION CODE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 1.sp
                        )

                        IconButton(
                            onClick = {
                                val codeSnippet = """
                                    // Material 3 Expressive Orbit Halo Cluster
                                    ExpressiveOrbitHaloButton(
                                        onClick = { isWifi = !isWifi },
                                        active = isWifi,
                                        haloType = ExpressiveHaloType.Wifi
                                    )
                                    ExpressiveOrbitHaloButton(
                                        onClick = { isBluetooth = !isBluetooth },
                                        active = isBluetooth,
                                        haloType = ExpressiveHaloType.Bluetooth
                                    )
                                """.trimIndent()

                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("OrbitHalo Code", codeSnippet)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ContentCopy,
                                contentDescription = "Copy Code",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = """
                                // Material 3 Expressive Orbit Halo Cluster
                                ExpressiveOrbitHaloButton(
                                    onClick = { isWifi = !isWifi },
                                    active = isWifi,
                                    haloType = ExpressiveHaloType.Wifi
                                )
                                ExpressiveOrbitHaloButton(
                                    onClick = { isBluetooth = !isBluetooth },
                                    active = isBluetooth,
                                    haloType = ExpressiveHaloType.Bluetooth
                                )
                            """.trimIndent(),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
