package com.example.mysterymerge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MysteryGold,
    onPrimary = MysteryInk,
    primaryContainer = MysteryGoldDark,
    onPrimaryContainer = MysteryCream,
    secondary = MysteryTealBright,
    onSecondary = Color.White,
    secondaryContainer = MysteryTeal,
    onSecondaryContainer = MysteryCream,
    background = MysteryNavy,
    onBackground = MysteryCream,
    surface = MysteryNavyLight,
    onSurface = MysteryCream,
    surfaceVariant = MysteryCardBg,
    onSurfaceVariant = MysteryPaper
)

@Composable
fun MysteryMergeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
