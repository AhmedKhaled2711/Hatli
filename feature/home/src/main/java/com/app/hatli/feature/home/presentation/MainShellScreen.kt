package com.app.hatli.feature.home.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.theme.BrandCoral
import com.app.hatli.core.designsystem.theme.OffWhiteCanvas
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.JobsListContent
import com.app.hatli.feature.home.presentation.components.MainBottomBar
import com.app.hatli.feature.home.presentation.components.RequestsListContent
import com.app.hatli.feature.home.presentation.components.bar.MainTopBar
import com.app.hatli.feature.home.presentation.messages.MessagesContent
import com.app.hatli.feature.home.presentation.requester.RequesterHomeContent
import com.app.hatli.feature.home.presentation.runner.RunnerHomeContent

@Composable
fun MainShellRoute(
    profileTab: @Composable () -> Unit,
    onCreateRequest: (String?) -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenRate: (String) -> Unit,
    onMessage: (String) -> Unit,
    viewModel: MainShellViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            is MainShellUiEffect.NavigateCreateRequest -> onCreateRequest(effect.initialCategory)
            is MainShellUiEffect.NavigateChat -> onOpenChat(effect.requestId)
            is MainShellUiEffect.NavigateDetail -> onOpenDetail(effect.requestId)
            is MainShellUiEffect.NavigateRate -> onOpenRate(effect.requestId)
            is MainShellUiEffect.ShowMessage -> onMessage(effect.message)
        }
    }

    MainShellScreen(
        state = state,
        onEvent = viewModel::onEvent,
        profileTab = profileTab,
    )
}

@Composable
fun MainShellScreen(
    state: MainShellUiState,
    onEvent: (MainShellUiEvent) -> Unit,
    profileTab: @Composable () -> Unit,
) {
    if (state.isLoadingProfile || state.role == null) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = OffWhiteCanvas,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = BrandCoral)
            }
        }
        return
    }

    MainShellReady(
        state = state,
        onEvent = onEvent,
        profileTab = profileTab,
    )
}

@Composable
private fun MainShellReady(
    state: MainShellUiState,
    onEvent: (MainShellUiEvent) -> Unit,
    profileTab: @Composable () -> Unit,
) {
    val isAr = LocalLayoutDirection.current == LayoutDirection.Rtl
    val hasUnreadMessages = state.conversations.any { it.unreadCount > 0 }

    var isBottomBarCompact by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state.selectedTab) {
        isBottomBarCompact = false
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta < -12f && !isBottomBarCompact) {
                    isBottomBarCompact = true
                } else if (delta > 12f && isBottomBarCompact) {
                    isBottomBarCompact = false
                }
                return Offset.Zero
            }
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            when (state.selectedTab) {
                MainTab.Home -> Unit
                MainTab.RequestsOrJobs -> {
                    MainTopBar(
                        title = if (state.isRequester) {
                            stringResource(R.string.home_tab_requests)
                        } else {
                            stringResource(R.string.home_tab_jobs)
                        },
                        unread = state.unreadNotifications,
                        onNotifications = { onEvent(MainShellUiEvent.OpenNotifications) },
                        showNotifications = false,
                    )
                }
                MainTab.Messages -> {
                    MainTopBar(
                        title = stringResource(R.string.home_tab_messages),
                        unread = state.unreadNotifications,
                        onNotifications = { onEvent(MainShellUiEvent.OpenNotifications) },
                        showNotifications = false,
                    )
                }
                MainTab.Profile -> Unit
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(nestedScrollConnection),
        ) {
            // Animated Tab Content Container
            AnimatedContent(
                targetState = state.selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "MainShellTabTransition",
                modifier = Modifier.fillMaxSize(),
            ) { tab ->
                when (tab) {
                    MainTab.Home -> {
                        AnimatedContent(
                            targetState = state.isRequester,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "PersonaHomeTransition",
                        ) { isRequester ->
                            if (isRequester) {
                                RequesterHomeContent(
                                    snapshot = state.requesterHome,
                                    isAr = isAr,
                                    onCreate = { onEvent(MainShellUiEvent.CreateRequest) },
                                    onQuickAction = { onEvent(MainShellUiEvent.QuickActionClicked(it)) },
                                    onChat = { onEvent(MainShellUiEvent.OpenChat(it)) },
                                    onDetails = { onEvent(MainShellUiEvent.OpenDetail(it)) },
                                    onConfirm = { onEvent(MainShellUiEvent.ConfirmDelivery(it)) },
                                    onRate = { onEvent(MainShellUiEvent.OpenRate(it)) },
                                    onOpenRequests = { onEvent(MainShellUiEvent.TabSelected(MainTab.RequestsOrJobs)) },
                                )
                            } else {
                                RunnerHomeContent(
                                    snapshot = state.runnerHome,
                                    isAr = isAr,
                                    showOnlineInfoBanner = state.showRunnerOnlineInfoBanner,
                                    onDismissOnlineInfoBanner = { onEvent(MainShellUiEvent.DismissRunnerOnlineInfoBanner) },
                                    onToggleOnline = { onEvent(MainShellUiEvent.ToggleRunnerOnline) },
                                    onViewAll = { onEvent(MainShellUiEvent.ViewAllJobs) },
                                    onAccept = { onEvent(MainShellUiEvent.AcceptRequest(it)) },
                                    onChat = { onEvent(MainShellUiEvent.OpenChat(it)) },
                                    onDetails = { onEvent(MainShellUiEvent.OpenDetail(it)) },
                                    onAdvance = { id, status -> onEvent(MainShellUiEvent.AdvanceStatus(id, status)) },
                                    onNotifications = { onEvent(MainShellUiEvent.OpenNotifications) },
                                )
                            }
                        }
                    }

                    MainTab.RequestsOrJobs -> {
                        if (state.isRequester) {
                            RequestsListContent(
                                filter = state.requestFilter,
                                items = state.filteredRequests,
                                onFilter = { onEvent(MainShellUiEvent.RequestFilterSelected(it)) },
                                onCreate = { onEvent(MainShellUiEvent.CreateRequest) },
                                onOpenDetail = { onEvent(MainShellUiEvent.OpenDetail(it)) },
                                onOpenChat = { onEvent(MainShellUiEvent.OpenChat(it)) },
                                onRate = { onEvent(MainShellUiEvent.OpenRate(it)) },
                                isAr = isAr,
                            )
                        } else {
                            JobsListContent(
                                filter = state.jobFilter,
                                items = state.filteredJobs,
                                onFilter = { onEvent(MainShellUiEvent.JobFilterSelected(it)) },
                                onAccept = { onEvent(MainShellUiEvent.AcceptRequest(it)) },
                                onOpenDetail = { onEvent(MainShellUiEvent.OpenDetail(it)) },
                                onOpenChat = { onEvent(MainShellUiEvent.OpenChat(it)) },
                            )
                        }
                    }

                    MainTab.Messages -> {
                        MessagesContent(
                            conversations = state.conversations,
                            isAr = isAr,
                            onOpenChat = { onEvent(MainShellUiEvent.OpenChat(it)) },
                        )
                    }

                    MainTab.Profile -> {
                        profileTab()
                    }
                }
            }

            // Floating Bottom Bar Overlay
            MainBottomBar(
                isRequester = state.isRequester,
                selected = state.selectedTab,
                onSelect = {
                    isBottomBarCompact = false
                    onEvent(MainShellUiEvent.TabSelected(it))
                },
                hasUnreadMessages = hasUnreadMessages,
                isCompact = isBottomBarCompact,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}
