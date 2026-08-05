package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.core.designsystem.theme.HatliSpacing

/**
 * Overlay bottom sheet (~3.5/10 screen) — covers the lower third of the phone,
 * matching the FlexBox onboarding reference.
 */
@Composable
fun OnboardingBottomSheet(
    title: String,
    description: String,
    pageKey: Int,
    pageCount: Int,
    currentPage: Int,
    primaryLabel: String,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(
            topStart = HatliRadius.xl + 8.dp,
            topEnd = HatliRadius.xl + 8.dp,
        ),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HatliElevation.lg,
        shadowElevation = HatliElevation.lg,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(
                    start = HatliSpacing.lg,
                    end = HatliSpacing.lg,
                    top = HatliSpacing.md,
                    bottom = HatliSpacing.md,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = HatliSpacing.md)
                    .height(4.dp)
                    .fillMaxWidth(0.12f)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)),
            )

            AnimatedContent(
                targetState = Triple(pageKey, title, description),
                transitionSpec = {
                    (fadeIn() + slideInVertically { it / 6 }) togetherWith
                        (fadeOut() + slideOutVertically { -it / 6 })
                },
                label = "onboardingSheetCopy",
                modifier = Modifier.fillMaxWidth(),
            ) { (_, pageTitle, pageDescription) ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = pageTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(HatliSpacing.sm))
                    Text(
                        text = pageDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OnboardingIndicator(
                pageCount = pageCount,
                currentPage = currentPage,
            )

            Spacer(modifier = Modifier.height(HatliSpacing.md))

            HatliPrimaryButton(
                text = primaryLabel,
                onClick = onPrimaryClick,
            )
        }
    }
}
