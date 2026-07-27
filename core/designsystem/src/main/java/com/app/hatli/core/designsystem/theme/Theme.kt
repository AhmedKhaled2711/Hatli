package com.app.hatli.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = HatliColors.Forest700,
    onPrimary = HatliColors.Slate50,
    primaryContainer = HatliColors.Forest500,
    onPrimaryContainer = HatliColors.Slate50,
    secondary = HatliColors.Amber500,
    onSecondary = HatliColors.Slate900,
    secondaryContainer = HatliColors.Amber200,
    onSecondaryContainer = HatliColors.Slate900,
    background = HatliColors.Slate50,
    onBackground = HatliColors.Slate900,
    surface = HatliColors.Slate50,
    onSurface = HatliColors.Slate900,
    surfaceVariant = HatliColors.Slate100,
    onSurfaceVariant = HatliColors.Slate800,
    error = HatliColors.Error,
    onError = HatliColors.OnError,
)

private val DarkColors = darkColorScheme(
    primary = HatliColors.Forest500,
    onPrimary = HatliColors.Slate50,
    primaryContainer = HatliColors.Forest900,
    onPrimaryContainer = HatliColors.Slate50,
    secondary = HatliColors.Amber200,
    onSecondary = HatliColors.Slate900,
    secondaryContainer = HatliColors.Amber500,
    onSecondaryContainer = HatliColors.Slate900,
    background = HatliColors.Slate900,
    onBackground = HatliColors.Slate50,
    surface = HatliColors.Slate900,
    onSurface = HatliColors.Slate50,
    surfaceVariant = HatliColors.Slate800,
    onSurfaceVariant = HatliColors.Slate100,
    error = HatliColors.Error,
    onError = HatliColors.OnError,
)

@Composable
fun HatliTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HatliTypography,
        shapes = HatliShapes,
        content = content,
    )
}
