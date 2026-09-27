package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dedicated Royal Mysore dark theme for that premium luxury look requested
private val MysoreDarkColorScheme = darkColorScheme(
    primary = MysoreGold,
    onPrimary = MidnightNavy,
    primaryContainer = MysoreAmber,
    onPrimaryContainer = WarmIvory,
    secondary = RoyalPurpleLight,
    onSecondary = WarmIvory,
    secondaryContainer = RoyalPurpleContainer,
    onSecondaryContainer = WarmIvory,
    tertiary = MysoreGoldLight,
    onTertiary = MidnightNavy,
    background = MidnightNavy,
    onBackground = WarmIvory,
    surface = MidnightNavyLight,
    onSurface = WarmIvory,
    surfaceVariant = MidnightNavyCard,
    onSurfaceVariant = WarmIvoryMuted,
    outline = GlassBorderGold,
    outlineVariant = GlassBorderPurple
)

// In case light theme is forced, keep high contrast with gold & royal purple
private val MysoreLightColorScheme = lightColorScheme(
    primary = MysoreGoldDark,
    onPrimary = WarmIvory,
    primaryContainer = MysoreGoldLight,
    onPrimaryContainer = MidnightNavy,
    secondary = RoyalPurple,
    onSecondary = WarmIvory,
    secondaryContainer = RoyalPurpleLight,
    onSecondaryContainer = MidnightNavy,
    background = WarmIvory,
    onBackground = MidnightNavy,
    surface = WarmIvory,
    onSurface = MidnightNavy,
    surfaceVariant = Color(0xFFECE7DA),
    onSurfaceVariant = Color(0xFF333333),
    outline = MysoreGoldDark
)

@Composable
fun MysoreExplorerTheme(
    darkTheme: Boolean = true, // Default to deep midnight royal aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MysoreDarkColorScheme else MysoreLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
