package com.tecknobit.pandoro.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

// TODO: IMPORTED COLORS FROM THE LIBRARY

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    primaryContainer = DwarfWhiteColor,
    secondary = ErrorLight,
    secondaryContainer = IceGrayColor,
    onSecondaryContainer = PrimaryLight,
    background = BackgroundLight,
    surface = DwarfWhiteColor,
    onSurfaceVariant = PrimaryLight,
    error = ErrorLight
)

@Composable
fun PandoroTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = LightColorScheme.primary.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}