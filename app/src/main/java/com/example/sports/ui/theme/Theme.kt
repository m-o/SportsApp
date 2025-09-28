package com.example.sports.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    secondary = BlueGrey40,
    tertiary = LightBlue40,
    surface = SurfaceColor,
    background = BackgroundColor,
    primaryContainer = BlueBackround,
    secondaryContainer = DarkerGray,
)

@Composable
fun SportsTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}