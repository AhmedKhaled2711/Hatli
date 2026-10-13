package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.theme.HatliElevation
import com.app.hatli.core.designsystem.theme.HatliSpacing

/**
 * Bottom action sheet container:
 * Houses the animated page title, description, progress dots, and the primary navigation button.
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
            topStart = 28.dp,
            topEnd = 28.dp,
        ),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = HatliElevation.lg,
        shadowElevation = HatliElevation.lg,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = HatliSpacing.lg,
                    end = HatliSpacing.lg,
                    top = HatliSpacing.lg,
                    bottom = HatliSpacing.md,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.lg),
        ) {
            // Text copy animated on page transition
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
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(modifier = Modifier.height(HatliSpacing.sm))

                    Text(
                        text = pageDescription,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            lineHeight = 21.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // Bottom controls: Indicators and Action Button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
            ) {
                OnboardingIndicator(
                    pageCount = pageCount,
                    currentPage = currentPage,
                )

                HatliPrimaryButton(
                    text = primaryLabel,
                    onClick = onPrimaryClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
