package com.app.hatli.core.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = HatliColors.BrandCoral,
    onPrimary = HatliColors.White,
    primaryContainer = HatliColors.CoralContainer,
    onPrimaryContainer = HatliColors.CharcoalDark,
    secondary = HatliColors.BrandTeal,
    onSecondary = HatliColors.CharcoalDark,
    secondaryContainer = HatliColors.TealContainer,
    onSecondaryContainer = HatliColors.DeepTeal,
    tertiary = HatliColors.Info,
    onTertiary = HatliColors.OnInfo,
    tertiaryContainer = HatliColors.InfoContainer,
    onTertiaryContainer = HatliColors.Info,
    background = HatliColors.OffWhiteCanvas,
    onBackground = HatliColors.CharcoalDark,
    surface = HatliColors.White,
    onSurface = HatliColors.CharcoalDark,
    surfaceVariant = HatliColors.Gray100,
    onSurfaceVariant = HatliColors.CharcoalMuted,
    outline = HatliColors.Border,
    outlineVariant = HatliColors.Gray100,
    error = HatliColors.Error,
    onError = HatliColors.OnError,
    errorContainer = HatliColors.ErrorContainer,
    onErrorContainer = HatliColors.OnErrorContainer,
    inverseSurface = HatliColors.Gray800,
    inverseOnSurface = HatliColors.White,
    inversePrimary = HatliColors.Teal300,
    surfaceTint = HatliColors.BrandCoral,
)

private val DarkColors = darkColorScheme(
    primary = HatliColors.BrandCoral,
    onPrimary = HatliColors.White,
    primaryContainer = Color(0xFF4A1F12),
    onPrimaryContainer = HatliColors.White,
    secondary = HatliColors.BrandTeal,
    onSecondary = HatliColors.OnStatsCardDark,
    secondaryContainer = HatliColors.DarkSurfaceVariant,
    onSecondaryContainer = HatliColors.Gray100,
    tertiary = Color(0xFF8BB4E0),
    onTertiary = HatliColors.Gray800,
    tertiaryContainer = Color(0xFF1A334D),
    onTertiaryContainer = HatliColors.InfoContainer,
    background = HatliColors.DarkBackground,
    onBackground = HatliColors.White,
    surface = HatliColors.DarkSurface,
    onSurface = HatliColors.White,
    surfaceVariant = HatliColors.DarkSurfaceVariant,
    onSurfaceVariant = HatliColors.Gray400,
    outline = HatliColors.DarkBorder,
    outlineVariant = HatliColors.DarkSurfaceVariant,
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    inverseSurface = HatliColors.Gray100,
    inverseOnSurface = HatliColors.Gray800,
    inversePrimary = HatliColors.Teal700,
    surfaceTint = HatliColors.BrandCoral,
)

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

    val fontFamily: FontFamily
        get() = HatliFontFamily
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

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    val extendedColors = if (darkTheme) {
        DarkHatliExtendedColors
    } else {
        LightHatliExtendedColors
    }

    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
                ?: return@SideEffect

            val insetsController = WindowCompat.getInsetsController(
                window,
                view,
            )

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
            CompositionLocalProvider(
                LocalTextStyle provides HatliTypography.bodyLarge,
            ) {
                Surface(
                    color = colorScheme.background,
                    contentColor = colorScheme.onBackground,
                    content = content,
                )
            }
        }
    }
}