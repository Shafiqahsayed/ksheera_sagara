package com.ksheera.sagara.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Agriculture-inspired colors
val SoftForestGreen = Color(0xFF2E7D32)
val MilkWhite = Color(0xFFFAFAFA)
val CharcoalGray = Color(0xFF37474F)

val LightGreen = Color(0xFF4CAF50)
val DarkGreen = Color(0xFF1B5E20)
val ErrorRed = Color(0xFFD32F2F)

private val LightColorScheme = lightColorScheme(
    primary = SoftForestGreen,
    secondary = LightGreen,
    tertiary = DarkGreen,
    background = MilkWhite,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = CharcoalGray,
    onSurface = CharcoalGray,
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = LightGreen,
    secondary = SoftForestGreen,
    tertiary = DarkGreen,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFEF5350)
)

@Composable
fun KsheeraSagaraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
