package com.app.hatli.feature.auth.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingPageType
import com.app.hatli.feature.auth.presentation.onboarding.OnboardingStrings

/**
 * In-phone content for each onboarding page.
 * Page 1 shows a realistic request list so the product idea is clear in seconds.
 */
@Composable
fun OnboardingPhoneScreen(
    page: OnboardingPageType,
    modifier: Modifier = Modifier,
) {
    when (page) {
        OnboardingPageType.OrderNearby -> RequestPreviewScreen(modifier)
        OnboardingPageType.CreateRequest -> CreateRequestPreviewScreen(modifier)
        OnboardingPageType.NearbyRunners -> RunnerNearbyPreviewScreen(modifier)
    }
}

@Composable
private fun RequestPreviewScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(HatliSpacing.md),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
    ) {
        Text(
            text = stringResource(OnboardingStrings.mockRequestTitle),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        RequestItemRow(text = stringResource(OnboardingStrings.mockItemMeat))
        RequestItemRow(text = stringResource(OnboardingStrings.mockItemPepsi))
        RequestItemRow(text = stringResource(OnboardingStrings.mockItemBread))

        Spacer(modifier = Modifier.weight(1f))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(HatliRadius.md),
            color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = HatliSpacing.sm,
                    vertical = HatliSpacing.sm,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(HatliSpacing.xs))
                Text(
                    text = stringResource(OnboardingStrings.mockAcceptedBy),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun RequestItemRow(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(HatliRadius.md),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(
                horizontal = HatliSpacing.md,
                vertical = HatliSpacing.sm + 2.dp,
            ),
        )
    }
}

@Composable
private fun CreateRequestPreviewScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(HatliSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircleIcon(Icons.Outlined.EditNote)
        Spacer(modifier = Modifier.height(HatliSpacing.md))
        Text(
            text = stringResource(OnboardingStrings.mockPage2Hint),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(HatliSpacing.sm))
        PlaceholderLine(0.85f)
        Spacer(modifier = Modifier.height(HatliSpacing.xs))
        PlaceholderLine(0.70f)
        Spacer(modifier = Modifier.height(HatliSpacing.xs))
        PlaceholderLine(0.78f)
    }
}

@Composable
private fun RunnerNearbyPreviewScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(HatliSpacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircleIcon(Icons.Outlined.NearMe)
        Spacer(modifier = Modifier.height(HatliSpacing.md))
        Text(
            text = stringResource(OnboardingStrings.mockPage3Hint),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(HatliSpacing.sm))
        Surface(
            shape = RoundedCornerShape(HatliRadius.lg),
            color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Text(
                text = stringResource(OnboardingStrings.mockAcceptedBy),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(HatliSpacing.sm),
            )
        }
    }
}

@Composable
private fun CircleIcon(icon: ImageVector) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(36.dp),
        )
    }
}

@Composable
private fun PlaceholderLine(fraction: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth(fraction)
            .height(10.dp)
            .clip(RoundedCornerShape(HatliRadius.sm))
            .background(MaterialTheme.colorScheme.surfaceVariant),
    )
}
