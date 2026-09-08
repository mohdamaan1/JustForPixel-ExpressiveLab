package com.ermohdamaan.justforpixel.justforpixelexpressivelab.app

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.screens.ExpressiveSplashScreenContainer
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.theme.JustForPixelExpressiveLabTheme
import com.ermohdamaan.justforpixel.justforpixelexpressivelab.app.ui.theme.isAppDarkTheme

/**
 * Main Entry Activity for JustForPixel ExpressiveLab Demo Application.
 *
 * @author Er. Mohd Amaan
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        enableEdgeToEdge()

        setContent {
            // Er. Mohd Amaan - Dynamic theme observation from global AppSettingsConfig
            val darkTheme = isAppDarkTheme()

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                        detectDarkMode = { darkTheme }
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT,
                        detectDarkMode = { darkTheme }
                    )
                )
                onDispose {}
            }

            JustForPixelExpressiveLabTheme {
                ExpressiveSplashScreenContainer()
            }
        }
    }
}
