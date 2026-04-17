package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = GreenPrimary,
    primaryContainer = GreenContainer,
    onPrimaryContainer = GreenContainerText,

    background = WhiteBackground,
    surface = WhiteBackground,
    onPrimary = WhiteBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderLight,
    error = ErrorRed,

)

@Composable
fun RentPlatformTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        content = content
    )
}