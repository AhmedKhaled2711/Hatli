package com.app.hatli.feature.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.hatli.feature.home.presentation.components.MainBottomBar
import com.app.hatli.feature.home.presentation.components.MainTopBar
import com.app.hatli.feature.home.presentation.components.runner.RunnerHomeContent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.home.R
import com.app.hatli.feature.home.presentation.components.HomeHeader
import com.app.hatli.feature.home.presentation.components.JobsListContent
import com.app.hatli.feature.home.presentation.messages.MessagesContent
import com.app.hatli.feature.home.presentation.components.RequestsListContent
import com.app.hatli.feature.home.presentation.requester.RequesterHomeContent

@Composable
fun MainShellRoute(
    profileTab: @Composable () -> Unit,
    onCreateRequest: () -> Unit,
    onOpenChat: (String) -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenRate: (String) -> Unit,
    onMessage: (String) -> Unit,
    viewModel: MainShellViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            MainShellUiEffect.NavigateCreateRequest -> {
                onCreateRequest()
            }

            is MainShellUiEffect.NavigateChat -> {
                onOpenChat(effect.requestId)
            }

            is MainShellUiEffect.NavigateDetail -> {
                onOpenDetail(effect.requestId)
            }

            is MainShellUiEffect.NavigateRate -> {
                onOpenRate(effect.requestId)
            }

            is MainShellUiEffect.ShowMessage -> {
                onMessage(effect.message)
            }
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
        Box(
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
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
    val isAr = LocalConfiguration.current.locales[0]?.language == "ar"

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),

        topBar = {
            when (state.selectedTab) {
                MainTab.Home -> {
                    HomeHeader(
                        userName = "Ahmed",
                        unreadNotifications = state.unreadNotifications,
                        isAr = isAr,
                        completedRequests = state.requesterHome?.recent?.count {
                            it.status == RequestStatus.Delivered
                        } ?: 0,
                        totalRequests = state.requesterHome?.recent?.size ?: 0,
                        onNotifications = {
                            onEvent(MainShellUiEvent.OpenNotifications)
                        },
                    )
                }

                MainTab.RequestsOrJobs -> {
                    MainTopBar(
                        title = if (state.isRequester) {
                            stringResource(R.string.home_tab_requests)
                        } else {
                            stringResource(R.string.home_tab_jobs)
                        },
                        unread = state.unreadNotifications,
                        onNotifications = {
                            onEvent(MainShellUiEvent.OpenNotifications)
                        },
                        showNotifications = false,
                    )
                }

                MainTab.Messages -> {
                    MainTopBar(
                        title = stringResource(R.string.home_tab_messages),
                        unread = state.unreadNotifications,
                        onNotifications = {
                            onEvent(MainShellUiEvent.OpenNotifications)
                        },
                        showNotifications = false,
                    )
                }

                MainTab.Profile -> Unit
            }
        },

        floatingActionButton = {
            if (
                state.showFab &&
                state.selectedTab != MainTab.Profile
            ) {
                FloatingActionButton(
                    onClick = {
                        onEvent(
                            MainShellUiEvent.CreateRequest,
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = stringResource(
                            R.string.home_create_request,
                        ),
                    )
                }
            }
        },

        bottomBar = {
            MainBottomBar(
                isRequester = state.isRequester,
                selected = state.selectedTab,
                onSelect = {
                    onEvent(
                        MainShellUiEvent.TabSelected(it),
                    )
                },
            )
        },
    ) { paddingValues ->

        Box(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (state.selectedTab) {

                MainTab.Home -> {
                    if (state.isRequester) {
                        RequesterHomeContent(
                            snapshot = state.requesterHome,
                            isAr = isAr,
                            onCreate = {
                                onEvent(
                                    MainShellUiEvent.CreateRequest,
                                )
                            },
                            onQuickAction = {
                                onEvent(
                                    MainShellUiEvent.QuickActionClicked(it),
                                )
                            },
                            onChat = {
                                onEvent(
                                    MainShellUiEvent.OpenChat(it),
                                )
                            },
                            onDetails = {
                                onEvent(
                                    MainShellUiEvent.OpenDetail(it),
                                )
                            },
                            onConfirm = {
                                onEvent(
                                    MainShellUiEvent.ConfirmDelivery(it),
                                )
                            },
                            onRate = {
                                onEvent(
                                    MainShellUiEvent.OpenRate(it),
                                )
                            },
                            onOpenRequests = {
                                onEvent(
                                    MainShellUiEvent.TabSelected(
                                        MainTab.RequestsOrJobs,
                                    ),
                                )
                            },
                        )
                    } else {
                        RunnerHomeContent(
                            snapshot = state.runnerHome,
                            isAr = isAr,
                            onToggleOnline = {
                                onEvent(
                                    MainShellUiEvent.ToggleRunnerOnline,
                                )
                            },
                            onViewAll = {
                                onEvent(
                                    MainShellUiEvent.ViewAllJobs,
                                )
                            },
                            onAccept = {
                                onEvent(
                                    MainShellUiEvent.AcceptRequest(it),
                                )
                            },
                            onChat = {
                                onEvent(
                                    MainShellUiEvent.OpenChat(it),
                                )
                            },
                            onDetails = {
                                onEvent(
                                    MainShellUiEvent.OpenDetail(it),
                                )
                            },
                            onAdvance = { id, status ->
                                onEvent(
                                    MainShellUiEvent.AdvanceStatus(
                                        id,
                                        status,
                                    ),
                                )
                            },
                        )
                    }
                }

                MainTab.RequestsOrJobs -> {
                    if (state.isRequester) {
                        RequestsListContent(
                            filter = state.requestFilter,
                            items = state.filteredRequests,
                            onFilter = {
                                onEvent(
                                    MainShellUiEvent.RequestFilterSelected(it),
                                )
                            },
                            onCreate = {
                                onEvent(
                                    MainShellUiEvent.CreateRequest,
                                )
                            },
                            onOpenDetail = {
                                onEvent(
                                    MainShellUiEvent.OpenDetail(it),
                                )
                            },
                            onOpenChat = {
                                onEvent(
                                    MainShellUiEvent.OpenChat(it),
                                )
                            },
                            onRate = {
                                onEvent(
                                    MainShellUiEvent.OpenRate(it),
                                )
                            },
                        )
                    } else {
                        JobsListContent(
                            filter = state.jobFilter,
                            items = state.filteredJobs,
                            onFilter = {
                                onEvent(
                                    MainShellUiEvent.JobFilterSelected(it),
                                )
                            },
                            onAccept = {
                                onEvent(
                                    MainShellUiEvent.AcceptRequest(it),
                                )
                            },
                            onOpenDetail = {
                                onEvent(
                                    MainShellUiEvent.OpenDetail(it),
                                )
                            },
                            onOpenChat = {
                                onEvent(
                                    MainShellUiEvent.OpenChat(it),
                                )
                            },
                            isAr = isAr,
                        )
                    }
                }

                MainTab.Messages -> {
                    MessagesContent(
                        conversations = state.conversations,
                        isAr = isAr,
                        onOpenChat = {
                            onEvent(
                                MainShellUiEvent.OpenChat(it),
                            )
                        },
                    )
                }

                MainTab.Profile -> {
                    profileTab()
                }
            }
        }
    }
}
