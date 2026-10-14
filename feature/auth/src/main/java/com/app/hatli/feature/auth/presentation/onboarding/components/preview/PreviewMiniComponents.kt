package com.app.hatli.feature.auth.presentation.onboarding.components.preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingStrings

/**
 * Scaled mini UI primitives for rendering authentic, read-only app previews
 * inside onboarding phone mockups without clipping or visual distortion.
 */

@Composable
internal fun WizardStepTrackMini(currentStep: Int) {
    val steps = listOf(
        1 to stringResource(OnboardingStrings.mockStepService),
        2 to stringResource(OnboardingStrings.mockStepDetails),
        3 to stringResource(OnboardingStrings.mockStepReview),
    )

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            steps.forEach { (stepNumber, label) ->
                val isCompleted = stepNumber < currentStep
                val isCurrent = stepNumber == currentStep

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCompleted -> MaterialTheme.colorScheme.primary
                                    isCurrent -> MaterialTheme.colorScheme.primaryContainer
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                },
                            )
                            .border(
                                width = if (isCurrent) 1.dp else 0.dp,
                                color = if (isCurrent) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(8.dp),
                            )
                        } else {
                            Text(
                                text = "$stepNumber",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 7.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        ),
                        color = if (isCurrent) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        LinearProgressIndicator(
            progress = {
                when (currentStep) {
                    1 -> 0.33f
                    2 -> 0.66f
                    else -> 1.0f
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer,
        )
    }
}

@Composable
internal fun FormTextFieldMini(
    label: String,
    value: String,
    isMultiline: Boolean = false,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(HatliRadius.sm),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 6.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = if (isMultiline) 9.5.sp else 8.sp,
                ),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
internal fun SummaryRowMini(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    isBoldValue: Boolean = false,
    isMuted: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 7.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f, fill = false),
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = if (isMuted) 6.sp else 7.5.sp,
                fontWeight = if (isBoldValue) FontWeight.Bold else FontWeight.Normal,
            ),
            color = if (isMuted) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) else valueColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
internal fun DetailRowMini(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 1.dp)
                .size(10.dp),
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 6.5.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 9.sp,
                ),
                color = valueColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
