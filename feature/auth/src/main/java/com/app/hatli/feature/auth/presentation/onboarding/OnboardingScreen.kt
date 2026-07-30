package com.app.hatli.feature.auth.presentation.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliTextButton
import com.app.hatli.core.designsystem.theme.HatliColors
import com.app.hatli.core.designsystem.theme.HatliRadius
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.ui.base.CollectUiEffect
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun OnboardingRoute(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            OnboardingUiEffect.GoLogin -> onFinished()
        }
    }
    OnboardingScreen(state = state, onEvent = viewModel::onEvent)
}

/**
 * FlexBox-inspired onboarding: Skip · illustration hero · title/body · dots · sticky CTA.
 * Brand colors follow Hatli forest/amber tokens (not the kit’s stock palette).
 */
@Composable
fun OnboardingScreen(
    state: OnboardingUiState,
    onEvent: (OnboardingUiEvent) -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = state.pageIndex,
        pageCount = { state.pages.size },
    )

    LaunchedEffect(state.pageIndex) {
        if (pagerState.currentPage != state.pageIndex) {
            pagerState.animateScrollToPage(state.pageIndex)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                onEvent(OnboardingUiEvent.PageSelected(page))
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = HatliSpacing.lg),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(HatliSpacing.xxl),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (!state.isLast) {
                HatliTextButton(
                    text = "Skip",
                    onClick = { onEvent(OnboardingUiEvent.Skip) },
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            OnboardingPageContent(page = state.pages[page])
        }

        OnboardingPageIndicator(
            pageCount = state.pages.size,
            currentPage = state.pageIndex,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = HatliSpacing.lg),
        )

        HatliPrimaryButton(
            text = if (state.isLast) "Get Started" else "Next",
            onClick = {
                if (state.isLast) onEvent(OnboardingUiEvent.GetStarted)
                else onEvent(OnboardingUiEvent.Next)
            },
            modifier = Modifier.padding(bottom = HatliSpacing.lg),
        )
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            OnboardingHeroIllustration(illustration = page.illustration)
        }

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = HatliSpacing.sm),
        )
        Spacer(modifier = Modifier.height(HatliSpacing.sm))
        Text(
            text = page.body,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = HatliSpacing.md),
        )
        Spacer(modifier = Modifier.height(HatliSpacing.md))
    }
}

@Composable
private fun OnboardingHeroIllustration(
    illustration: OnboardingIllustration,
) {
    val (primaryIcon, accentIcon) = when (illustration) {
        OnboardingIllustration.Roles -> Icons.Outlined.SwapHoriz to Icons.Outlined.LocalShipping
        OnboardingIllustration.Track -> Icons.Outlined.Route to Icons.Outlined.LocalShipping
        OnboardingIllustration.Pay -> Icons.Outlined.Payments to Icons.Outlined.LocalShipping
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(280.dp),
    ) {
        // Soft outer wash (FlexBox-style illustration backdrop)
        Box(
            modifier = Modifier
                .size(260.dp)
                .clip(CircleShape)
                .background(HatliColors.Forest100.copy(alpha = 0.55f)),
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(HatliColors.Forest100),
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IllustrationBadge(icon = primaryIcon, size = 88.dp)
            Spacer(modifier = Modifier.height(HatliSpacing.md))
            Row(horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
                IllustrationBadge(icon = accentIcon, size = 56.dp, secondary = true)
            }
        }
    }
}

@Composable
private fun IllustrationBadge(
    icon: ImageVector,
    size: Dp,
    secondary: Boolean = false,
) {
    val container = if (secondary) HatliColors.Amber100 else MaterialTheme.colorScheme.primary
    val tint = if (secondary) HatliColors.Amber700 else MaterialTheme.colorScheme.onPrimary
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(HatliRadius.xl))
            .background(container),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(size * 0.45f),
        )
    }
}

@Composable
private fun OnboardingPageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(HatliSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            val selected = index == currentPage
            val width by animateDpAsState(
                targetValue = if (selected) 24.dp else 8.dp,
                label = "onboardingDotWidth",
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant,
                    ),
            )
        }
    }
}
