package com.app.hatli.feature.request.presentation.request.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.designsystem.theme.HatliThemeMode
import com.app.hatli.feature.request.R

@Composable
fun RequestCreatedSuccessOverlay(
    isVisible: Boolean,
    onReturnToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(isVisible) {
        if (isVisible) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    if (isVisible) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
                exit = fadeOut() + scaleOut(),
                modifier = modifier
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(HatliSpacing.xl)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        AnimatedSuccessCheckmark(
                            size = 100.dp,
                            circleColor = HatliColors.BrandTeal,
                            checkColor = HatliColors.DeepTeal,
                            repeatCheckmark = true
                        )

                        Spacer(modifier = Modifier.height(HatliSpacing.xl))

                        Text(
                            text = stringResource(R.string.create_request_success_title),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(HatliSpacing.sm))

                        Text(
                            text = stringResource(R.string.create_request_success_subtitle),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = HatliSpacing.md)
                        )
                    }

                    HatliPrimaryButton(
                        text = stringResource(R.string.create_request_return_home),
                        onClick = onReturnToHome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode", device = Devices.PIXEL_7, showBackground = true)
@Composable
private fun RequestCreatedSuccessOverlayPreview() {
    HatliTheme(themeMode = HatliThemeMode.Light) {
        RequestCreatedSuccessOverlay(
            isVisible = true,
            onReturnToHome = {}
        )
    }
}

@Preview(name = "Dark Mode", device = Devices.PIXEL_7, showBackground = true)
@Composable
private fun RequestCreatedSuccessOverlayDarkPreview() {
    HatliTheme(themeMode = HatliThemeMode.Dark) {
        RequestCreatedSuccessOverlay(
            isVisible = true,
            onReturnToHome = {}
        )
    }
}