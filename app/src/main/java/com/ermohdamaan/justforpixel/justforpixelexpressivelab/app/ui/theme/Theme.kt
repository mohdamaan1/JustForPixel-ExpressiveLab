package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Er. Mohd Amaan - Enum representing application theme selection options
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

// Er. Mohd Amaan - Global App Settings State Holder for live preference updates
object AppSettingsConfig {
    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
    var isOledBlack by mutableStateOf(false)
    var isHapticsEnabled by mutableStateOf(true)
}

// Er. Mohd Amaan - Helper composable function to resolve active dark theme state
@Composable
fun isAppDarkTheme(): Boolean {
    return when (AppSettingsConfig.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
}

// Er. Mohd Amaan - Standard Material 3 Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Er. Mohd Amaan - Standard Material 3 Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

/**
 * Custom Material 3 Expressive Application Theme with OLED Pitch Black support.
 *
 * @author Er. Mohd Amaan
 */
@Composable
fun JustForPixelExpressiveLabTheme(
    darkTheme: Boolean = isAppDarkTheme(),
    oledBlack: Boolean = AppSettingsConfig.isOledBlack,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // Er. Mohd Amaan - Calculate base color scheme with Android 12+ dynamic colors
    val baseColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Er. Mohd Amaan - Override background and surface colors to pure AMOLED #000000 when OLED Black is active
    val finalColorScheme = if (darkTheme && oledBlack) {
        baseColorScheme.copy(
            background = Color.Black,
            surface = Color.Black,
            surfaceContainer = Color(0xFF101010),
            surfaceContainerLow = Color(0xFF080808),
            surfaceContainerLowest = Color.Black,
            surfaceContainerHigh = Color(0xFF181818),
            surfaceContainerHighest = Color(0xFF222222)
        )
    } else {
        baseColorScheme
    }

    MaterialTheme(
        colorScheme = finalColorScheme,
        typography = Typography,
        content = content
    )
}
