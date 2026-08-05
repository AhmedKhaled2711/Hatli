package com.app.hatli.feature.auth.presentation.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.auth.presentation.onboarding.components.OnboardingBackground
import com.app.hatli.feature.auth.presentation.onboarding.components.OnboardingBottomSheet
import com.app.hatli.feature.auth.presentation.onboarding.components.OnboardingPage
import com.app.hatli.feature.auth.presentation.onboarding.components.OnboardingTopBar
import kotlinx.coroutines.flow.distinctUntilChanged

/** Bottom sheet height — matches FlexBox-style reference (3.5/10). */
private const val OnboardingSheetHeightFraction = 3.5f / 10f

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
 * FlexBox-style onboarding:
 * header under status bar · phone hero · sheet overlays lower third of the phone.
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

    val currentPage = state.currentPage
    val backgroundRes = when (pagerState.currentPage) {
        0 -> OnboardingStrings.bgPage1
        1 -> OnboardingStrings.bgPage2
        else -> OnboardingStrings.bgPage3
    }
    val darkHeader = pagerState.currentPage == 1

    OnboardingBackground(backgroundRes = backgroundRes) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                OnboardingTopBar(
                    showSkip = !state.isLast,
                    onSkip = { onEvent(OnboardingUiEvent.Skip) },
                    darkHeader = darkHeader,
                    modifier = Modifier.statusBarsPadding(),
                )

                // Phone fills remaining height (including area behind the sheet).
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = HatliSpacing.md),
                ) { page ->
                    OnboardingPage(
                        page = state.pages[page],
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            // Sheet overlays the lower portion of the phone (reference layout).
            OnboardingBottomSheet(
                title = currentPage.titleText(),
                description = currentPage.bodyText(),
                pageKey = state.pageIndex,
                pageCount = state.pages.size,
                currentPage = state.pageIndex,
                primaryLabel = onboardingPrimaryLabel(state.isLast),
                onPrimaryClick = {
                    if (state.isLast) onEvent(OnboardingUiEvent.GetStarted)
                    else onEvent(OnboardingUiEvent.Next)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(OnboardingSheetHeightFraction),
            )
        }
    }
}

@Preview(name = "1 · Order nearby · Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "1 · Order nearby · Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "1 · Order nearby · AR", showBackground = true, locale = "ar", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun OnboardingPage1Preview() {
    OnboardingPreviewHost(pageIndex = 0)
}

@Preview(name = "2 · Create request · Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "2 · Create request · Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "2 · Create request · AR", showBackground = true, locale = "ar", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun OnboardingPage2Preview() {
    OnboardingPreviewHost(pageIndex = 1)
}

@Preview(name = "3 · Nearby runners · Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "3 · Nearby runners · Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "3 · Nearby runners · AR", showBackground = true, locale = "ar", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun OnboardingPage3Preview() {
    OnboardingPreviewHost(pageIndex = 2)
}

@Composable
private fun OnboardingPreviewHost(pageIndex: Int) {
    HatliTheme {
        OnboardingScreen(
            state = OnboardingUiState(pageIndex = pageIndex),
            onEvent = {},
        )
    }
}
