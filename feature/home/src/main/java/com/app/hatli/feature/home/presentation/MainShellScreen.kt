package com.app.hatli.feature.home.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.QuickAction
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.core.model.RunnerHomeSnapshot
import com.app.hatli.core.model.RunnerHomeStats
import com.app.hatli.core.model.RunnerPresence
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.home.presentation.components.CurrentRequestCard
import com.app.hatli.feature.home.presentation.components.HeroCreateCard
import com.app.hatli.feature.home.presentation.components.MainTopBar
import com.app.hatli.feature.home.presentation.components.RequestRowCard
import com.app.hatli.feature.home.presentation.components.RunnerPresenceCard
import com.app.hatli.feature.home.presentation.components.RunnerStatsRow

@Composable
fun MainShellRoute(
    profileTab: @Composable () -> Unit,
    onCreateRequestStub: () -> Unit,
    onMessage: (String) -> Unit,
    viewModel: MainShellViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CollectUiEffect(viewModel.uiEffect) { effect ->
        when (effect) {
            MainShellUiEffect.NavigateCreateRequestStub -> onCreateRequestStub()
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        MainShellReady(
            state = state,
            onEvent = onEvent,
            profileTab = profileTab,
        )
    }
}

@Composable
private fun MainShellReady(
    state: MainShellUiState,
    onEvent: (MainShellUiEvent) -> Unit,
    profileTab: @Composable () -> Unit,
) {
    val isAr = LocalConfiguration.current.locales[0]?.language == "ar"
    val homeTitle = if (isAr) "الرئيسية" else "Home"

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (state.selectedTab != MainTab.Profile) {
                MainTopBar(
                    title = when (state.selectedTab) {
                        MainTab.Home -> homeTitle
                        MainTab.RequestsOrJobs ->
                            if (state.isRequester) {
                                if (isAr) "الطلبات" else "Requests"
                            } else {
                                if (isAr) "الوظائف" else "Jobs"
                            }
                        MainTab.Messages -> if (isAr) "الرسائل" else "Messages"
                        MainTab.Profile -> ""
                    },
                    unread = state.unreadNotifications,
                    onNotifications = { onEvent(MainShellUiEvent.OpenNotifications) },
                    // Bell only on Home — list/message tabs are title-only (production pattern).
                    showNotifications = state.selectedTab == MainTab.Home,
                )
            }
        },
        floatingActionButton = {
            if (state.showFab && state.selectedTab != MainTab.Profile) {
                FloatingActionButton(
                    onClick = { onEvent(MainShellUiEvent.CreateRequest) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(Icons.Outlined.Add, contentDescription = "Create request")
                }
            }
        },
        bottomBar = {
            MainBottomBar(
                isRequester = state.isRequester,
                selected = state.selectedTab,
                onSelect = { onEvent(MainShellUiEvent.TabSelected(it)) },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when (state.selectedTab) {
                MainTab.Home -> {
                    if (state.isRequester) {
                        RequesterHomeContent(
                            snapshot = state.requesterHome,
                            isAr = isAr,
                            onCreate = { onEvent(MainShellUiEvent.CreateRequest) },
                            onQuickAction = { onEvent(MainShellUiEvent.QuickActionClicked(it)) },
                        )
                    } else {
                        RunnerHomeContent(
                            snapshot = state.runnerHome,
                            onToggleOnline = { onEvent(MainShellUiEvent.ToggleRunnerOnline) },
                            onViewAll = { onEvent(MainShellUiEvent.ViewAllJobs) },
                        )
                    }
                }
                MainTab.RequestsOrJobs -> {
                    if (state.isRequester) {
                        RequestsListContent(
                            filter = state.requestFilter,
                            items = state.filteredRequests,
                            onFilter = { onEvent(MainShellUiEvent.RequestFilterSelected(it)) },
                            onCreate = { onEvent(MainShellUiEvent.CreateRequest) },
                            isAr = isAr,
                        )
                    } else {
                        JobsListContent(
                            filter = state.jobFilter,
                            items = state.filteredJobs,
                            onFilter = { onEvent(MainShellUiEvent.JobFilterSelected(it)) },
                            isAr = isAr,
                        )
                    }
                }
                MainTab.Messages -> MessagesEmptyContent(isAr = isAr)
                MainTab.Profile -> profileTab()
            }
        }
    }
}

@Composable
private fun MainBottomBar(
    isRequester: Boolean,
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
) {
    data class TabSpec(val tab: MainTab, val label: String, val icon: ImageVector)
    val tabs = listOf(
        TabSpec(MainTab.Home, "Home", Icons.Outlined.Home),
        TabSpec(
            MainTab.RequestsOrJobs,
            if (isRequester) "Requests" else "Jobs",
            if (isRequester) Icons.Outlined.ListAlt else Icons.Outlined.WorkOutline,
        ),
        TabSpec(MainTab.Messages, "Messages", Icons.Outlined.ChatBubbleOutline),
        TabSpec(MainTab.Profile, "Profile", Icons.Outlined.Person),
    )
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        tabs.forEach { spec ->
            NavigationBarItem(
                selected = selected == spec.tab,
                onClick = { onSelect(spec.tab) },
                icon = { Icon(spec.icon, contentDescription = spec.label) },
                label = { Text(spec.label) },
            )
        }
    }
}

@Composable
fun RequesterHomeContent(
    snapshot: RequesterHomeSnapshot?,
    isAr: Boolean,
    onCreate: () -> Unit,
    onQuickAction: (String) -> Unit,
) {
    if (snapshot == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
        ) {
            item {
                HeroCreateCard(
                    title = if (isAr) "عايز تطلب حاجة؟" else "Need something nearby?",
                    cta = if (isAr) "+ اطلب دلوقتي" else "+ Order now",
                    onCreate = onCreate,
                )
            }
            snapshot.active?.let { active ->
                item { CurrentRequestCard(summary = active) }
            }
            if (snapshot.recent.isNotEmpty()) {
                item {
                    Text(
                        text = if (isAr) "آخر الطلبات" else "Recent requests",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                items(snapshot.recent.take(5), key = { it.id }) { row ->
                    RequestRowCard(summary = row)
                }
            }
            item {
                Text(
                    text = if (isAr) "اختصارات" else "Quick actions",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
                    items(snapshot.quickActions, key = { it.id }) { action ->
                        QuickActionChip(
                            action = action,
                            isAr = isAr,
                            onClick = { onQuickAction(action.id) },
                        )
                    }
                }
            }
            item {
                val tip = if (isAr) snapshot.tipAr else snapshot.tipEn
                if (tip.isNotBlank()) {
                    HatliCard {
                        Text(
                            text = if (isAr) "نصيحة" else "Tip",
                            style = MaterialTheme.typography.titleSmall,
                        )
                        Text(tip, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionChip(
    action: QuickAction,
    isAr: Boolean,
    onClick: () -> Unit,
) {
    HatliCard(onClick = onClick) {
        Text(
            text = if (isAr) action.labelAr else action.labelEn,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun RunnerHomeContent(
    snapshot: RunnerHomeSnapshot?,
    onToggleOnline: () -> Unit,
    onViewAll: () -> Unit,
) {
    if (snapshot == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val online = snapshot.presence.isOnline
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
        ) {
            item {
                RunnerPresenceCard(isOnline = online, onToggle = onToggleOnline)
            }
            item {
                RunnerStatsRow(
                    earnings = snapshot.stats.dailyEarnings,
                    jobs = snapshot.stats.todayJobsCount,
                    rating = snapshot.stats.rating,
                )
            }
            if (!online) {
                item {
                    Text(
                        text = "افتح الاستقبال عشان تشوف الطلبات القريبة.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else if (snapshot.nearby.isEmpty()) {
                item {
                    Text(
                        text = "مفيش طلبات قريبة دلوقتي — خلي الاستقبال مفتوح.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else {
                item {
                    Text("Nearby Requests", style = MaterialTheme.typography.titleMedium)
                }
                items(snapshot.nearby.take(3), key = { it.id }) { job ->
                    RequestRowCard(summary = job)
                }
                item {
                    HatliSecondaryButton(text = "View All", onClick = onViewAll)
                }
            }
        }
    }
}

@Composable
fun RequestsListContent(
    filter: RequestListFilter,
    items: List<RequestSummary>,
    onFilter: (RequestListFilter) -> Unit,
    onCreate: () -> Unit,
    isAr: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(HatliSpacing.md),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
            items(RequestListFilter.entries.toList()) { f ->
                HatliCard(onClick = { onFilter(f) }, selected = f == filter) {
                    Text(f.name, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        if (items.isEmpty()) {
            Text(
                text = if (isAr) "مافيش طلبات هنا" else "No requests here",
                style = MaterialTheme.typography.bodyLarge,
            )
            HatliPrimaryButton(
                text = if (isAr) "اطلب دلوقتي" else "Create request",
                onClick = onCreate,
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
                items(items, key = { it.id }) { RequestRowCard(it) }
            }
        }
    }
}

@Composable
fun JobsListContent(
    filter: JobListFilter,
    items: List<RequestSummary>,
    onFilter: (JobListFilter) -> Unit,
    isAr: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(HatliSpacing.md),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
            items(JobListFilter.entries.toList()) { f ->
                HatliCard(onClick = { onFilter(f) }, selected = f == filter) {
                    Text(f.name, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        if (items.isEmpty()) {
            Text(
                text = if (isAr) "مافيش وظائف" else "No jobs yet",
                style = MaterialTheme.typography.bodyLarge,
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
                items(items, key = { it.id }) { RequestRowCard(it) }
            }
        }
    }
}

@Composable
fun MessagesEmptyContent(isAr: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(HatliSpacing.lg),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (isAr) "الشات مع الـ Runner قريبًا" else "Chat with your Runner soon",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun CreateRequestStubScreen(
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(HatliSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(HatliSpacing.md),
    ) {
        Text("Create Request", style = MaterialTheme.typography.headlineLarge)
        Text(
            text = "Full wizard ships in Phase 3. This stub confirms FAB / Hero navigation.",
            style = MaterialTheme.typography.bodyLarge,
        )
        HatliSecondaryButton(text = "Back", onClick = onBack)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RequesterHomePreview() {
    HatliTheme {
        RequesterHomeContent(
            snapshot = RequesterHomeSnapshot(
                active = RequestSummary(
                    id = "1",
                    title = "2 كيلو لحمة",
                    status = RequestStatus.SearchingRunner,
                    etaMinutes = 30,
                ),
                recent = emptyList(),
                quickActions = listOf(
                    QuickAction("a", "Supermarket", "سوبر ماركت"),
                ),
                tipEn = "Write clearly",
                tipAr = "اكتب بوضوح",
            ),
            isAr = true,
            onCreate = {},
            onQuickAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RunnerHomePreview() {
    HatliTheme {
        RunnerHomeContent(
            snapshot = RunnerHomeSnapshot(
                presence = RunnerPresence(true),
                stats = RunnerHomeStats(120.0, 6, 4.9),
                nearby = listOf(
                    RequestSummary(
                        id = "1",
                        title = "2 كيلو لحمة",
                        status = RequestStatus.SearchingRunner,
                        distanceMeters = 800,
                        offerAmount = 35.0,
                    ),
                ),
            ),
            onToggleOnline = {},
            onViewAll = {},
        )
    }
}
