package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BizBlueLight,
    onPrimary = Color.White,
    primaryContainer = BizBlue,
    onPrimaryContainer = Color.White,
    secondary = BizEmeraldLight,
    onSecondary = Color.White,
    secondaryContainer = BizEmerald,
    tertiary = BizViolet,
    background = BizDarkBackground,
    surface = BizDarkSurface,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = BizDarkCard,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF334155)
)

private val LightColorScheme = lightColorScheme(
    primary = BizBluePrimary,
    onPrimary = Color.White,
    primaryContainer = BizBlueContainer,
    onPrimaryContainer = BizBlue,
    secondary = BizEmerald,
    onSecondary = Color.White,
    secondaryContainer = BizEmeraldContainer,
    onSecondaryContainer = Color(0xFF064E3B),
    tertiary = BizViolet,
    background = BizSurfaceLight,
    surface = BizCardWhite,
    onBackground = BizTextPrimary,
    onSurface = BizTextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = BizTextSecondary,
    outline = BizBorder
)

@Composable
fun BizAdvisorTheme(
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
