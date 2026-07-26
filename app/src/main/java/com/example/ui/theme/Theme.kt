package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = DialysisBluePrimary,
    onPrimary = Color.White,
    primaryContainer = DialysisBlueLight,
    onPrimaryContainer = DialysisBlueDark,
    secondary = DialysisTealSecondary,
    onSecondary = Color.White,
    secondaryContainer = DialysisTealLight,
    onSecondaryContainer = Color(0xFF004D40),
    error = EmergencyRed,
    errorContainer = EmergencyRedContainer,
    onError = Color.White,
    onErrorContainer = EmergencyRed,
    background = MedicalSurfaceLight,
    onBackground = MedicalTextPrimary,
    surface = Color.White,
    onSurface = MedicalTextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = MedicalTextSecondary,
    outline = MedicalCardBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF64B5F6),
    onPrimary = Color(0xFF00296B),
    primaryContainer = Color(0xFF003F88),
    onPrimaryContainer = Color(0xFFD0E1FD),
    secondary = Color(0xFF80CBC4),
    onSecondary = Color(0xFF003731),
    secondaryContainer = Color(0xFF004D40),
    onSecondaryContainer = Color(0xFFB2DFDB),
    error = Color(0xFFFF8A80),
    errorContainer = Color(0xFFB71C1C),
    onError = Color.Black,
    background = DialysisDarkBackground,
    onBackground = Color.White,
    surface = DialysisDarkSurface,
    onSurface = Color.White,
    outline = Color(0xFF334155)
)

@Composable
fun DialysisBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyDialysisBookTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    DialysisBookTheme(darkTheme = darkTheme, content = content)
}
