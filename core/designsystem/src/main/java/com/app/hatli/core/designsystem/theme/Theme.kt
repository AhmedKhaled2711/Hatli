package com.app.hatli.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = HatliColors.Primary,
    onPrimary = HatliColors.SoftWhite,
    primaryContainer = HatliColors.Forest100,
    onPrimaryContainer = HatliColors.Forest900,
    secondary = HatliColors.Secondary,
    onSecondary = HatliColors.Slate900,
    secondaryContainer = HatliColors.Amber100,
    onSecondaryContainer = HatliColors.Amber700,
    tertiary = HatliColors.Info,
    onTertiary = HatliColors.OnInfo,
    tertiaryContainer = HatliColors.InfoContainer,
    onTertiaryContainer = HatliColors.Info,
    background = HatliColors.Background,
    onBackground = HatliColors.TextPrimary,
    surface = HatliColors.Surface,
    onSurface = HatliColors.TextPrimary,
    surfaceVariant = HatliColors.Slate100,
    onSurfaceVariant = HatliColors.TextSecondary,
    outline = HatliColors.Border,
    outlineVariant = HatliColors.Slate100,
    error = HatliColors.Error,
    onError = HatliColors.OnError,
    errorContainer = HatliColors.ErrorContainer,
    onErrorContainer = HatliColors.OnErrorContainer,
    inverseSurface = HatliColors.Slate800,
    inverseOnSurface = HatliColors.Slate50,
    inversePrimary = HatliColors.Forest300,
    surfaceTint = HatliColors.Primary,
)

private val DarkColors = darkColorScheme(
    primary = HatliColors.Forest300,
    onPrimary = HatliColors.Forest900,
    primaryContainer = HatliColors.Forest800,
    onPrimaryContainer = HatliColors.Forest100,
    secondary = HatliColors.Amber200,
    onSecondary = HatliColors.Slate900,
    secondaryContainer = HatliColors.Amber700,
    onSecondaryContainer = HatliColors.Amber100,
    tertiary = Color(0xFF8BB4E0),
    onTertiary = HatliColors.Slate900,
    tertiaryContainer = Color(0xFF1A334D),
    onTertiaryContainer = HatliColors.InfoContainer,
    background = HatliColors.DarkBackground,
    onBackground = HatliColors.Slate50,
    surface = HatliColors.DarkSurface,
    onSurface = HatliColors.Slate50,
    surfaceVariant = HatliColors.DarkSurfaceVariant,
    onSurfaceVariant = HatliColors.Slate400,
    outline = HatliColors.DarkBorder,
    outlineVariant = HatliColors.Slate800,
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    inverseSurface = HatliColors.Slate100,
    inverseOnSurface = HatliColors.Slate900,
    inversePrimary = HatliColors.Forest700,
    surfaceTint = HatliColors.Forest300,
)

/**
 * Controls which palette [HatliTheme] applies.
 * [FollowSystem] tracks the device dark-mode setting (default).
 */
enum class HatliThemeMode {
    FollowSystem,
    Light,
    Dark,
}

private val LocalHatliIsDarkTheme = staticCompositionLocalOf { false }

object HatliTheme {
    val extendedColors: HatliExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalHatliExtendedColors.current

    val isDark: Boolean
        @Composable
        @ReadOnlyComposable
        get() = LocalHatliIsDarkTheme.current
}

@Composable
fun HatliTheme(
    themeMode: HatliThemeMode = HatliThemeMode.FollowSystem,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (themeMode) {
        HatliThemeMode.FollowSystem -> systemDark
        HatliThemeMode.Light -> false
        HatliThemeMode.Dark -> true
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    val extendedColors = if (darkTheme) DarkHatliExtendedColors else LightHatliExtendedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalHatliExtendedColors provides extendedColors,
        LocalHatliIsDarkTheme provides darkTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = HatliTypography,
            shapes = HatliShapes,
        ) {
            Surface(
                color = colorScheme.background,
                contentColor = colorScheme.onBackground,
                content = content,
            )
        }
    }
}
