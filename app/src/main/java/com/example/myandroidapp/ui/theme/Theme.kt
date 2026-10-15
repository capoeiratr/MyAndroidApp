package com.example.myandroidapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AionDarkColorScheme = darkColorScheme(
    primary = AetherCyan,
    onPrimary = AbyssDarkVoid,
    primaryContainer = AbyssDarkNavy,
    onPrimaryContainer = AetherCyanBright,
    secondary = CelestialGold,
    onSecondary = AbyssDarkVoid,
    secondaryContainer = AbyssCardBg,
    onSecondaryContainer = CelestialGoldBright,
    tertiary = AsmodianViolet,
    background = AbyssDarkVoid,
    onBackground = ElyosPearl,
    surface = AbyssCardBg,
    onSurface = ElyosPearl
)

@Composable
fun MyAndroidAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AionDarkColorScheme,
        typography = Typography,
        content = content
    )
}
