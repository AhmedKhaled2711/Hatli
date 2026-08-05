package com.app.hatli.feature.home.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.hatli.core.designsystem.component.HatliCard
import com.app.hatli.core.designsystem.component.HatliPrimaryButton
import com.app.hatli.core.designsystem.component.HatliSecondaryButton
import com.app.hatli.core.designsystem.theme.HatliSpacing
import com.app.hatli.core.designsystem.theme.HatliTheme
import com.app.hatli.core.model.QuickAction
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.core.model.RunnerHomeSnapshot
import com.app.hatli.core.model.RunnerHomeStats
import com.app.hatli.core.model.RunnerPresence
import com.app.hatli.core.ui.base.CollectUiEffect
import com.app.hatli.feature.home.presentation.components.ConversationRowCard
import com.app.hatli.feature.home.presentation.components.CurrentJobCard
import com.app.hatli.feature.home.presentation.components.CurrentRequestCard
import com.app.hatli.feature.home.presentation.components.HeroCreateCard
import com.app.hatli.feature.home.presentation.components.MainTopBar
import com.app.hatli.feature.home.presentation.components.RequestRowCard
import com.app.hatli.feature.home.presentation.components.RunnerPresenceCard
import com.app.hatli.feature.home.presentation.components.RunnerStatsRow

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
            MainShellUiEffect.NavigateCreateRequest -> onCreateRequest()
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
                            onChat = { onEvent(MainShellUiEvent.OpenChat(it)) },
                            onDetails = { onEvent(MainShellUiEvent.OpenDetail(it)) },
                            onConfirm = { onEvent(MainShellUiEvent.ConfirmDelivery(it)) },
                            onRate = { onEvent(MainShellUiEvent.OpenRate(it)) },
                        )
                    } else {
                        RunnerHomeContent(
                            snapshot = state.runnerHome,
                            isAr = isAr,
                            onToggleOnline = { onEvent(MainShellUiEvent.ToggleRunnerOnline) },
                            onViewAll = { onEvent(MainShellUiEvent.ViewAllJobs) },
                            onAccept = { onEvent(MainShellUiEvent.AcceptRequest(it)) },
                            onChat = { onEvent(MainShellUiEvent.OpenChat(it)) },
                            onDetails = { onEvent(MainShellUiEvent.OpenDetail(it)) },
                            onAdvance = { id, status ->
                                onEvent(MainShellUiEvent.AdvanceStatus(id, status))
                            },
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
                            isAr = isAr,
                        )
                    }
                }
                MainTab.Messages -> MessagesContent(
                    conversations = state.conversations,
                    isAr = isAr,
                    onOpenChat = { onEvent(MainShellUiEvent.OpenChat(it)) },
                )
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
            if (isRequester) Icons.AutoMirrored.Outlined.ListAlt else Icons.Outlined.WorkOutline,
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
    onChat: (String) -> Unit,
    onDetails: (String) -> Unit,
    onConfirm: (String) -> Unit,
    onRate: (String) -> Unit,
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
                item {
                    CurrentRequestCard(
                        summary = active,
                        isAr = isAr,
                        onChat = { onChat(active.id) },
                        onDetails = { onDetails(active.id) },
                        onConfirm = if (active.status == RequestStatus.Delivered) {
                            { onConfirm(active.id) }
                        } else {
                            null
                        },
                        onRate = if (active.needsRating) {
                            { onRate(active.id) }
                        } else {
                            null
                        },
                    )
                }
            }
            if (snapshot.recent.isNotEmpty()) {
                item {
                    Text(
                        text = if (isAr) "آخر الطلبات" else "Recent requests",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                items(snapshot.recent.take(5), key = { it.id }) { row ->
                    RequestRowCard(
                        summary = row,
                        isAr = isAr,
                        onClick = { onDetails(row.id) },
                    )
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
    isAr: Boolean,
    onToggleOnline: () -> Unit,
    onViewAll: () -> Unit,
    onAccept: (String) -> Unit,
    onChat: (String) -> Unit,
    onDetails: (String) -> Unit,
    onAdvance: (String, RequestStatus) -> Unit,
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
            snapshot.activeJob?.let { active ->
                item {
                    val next = active.runnerNextStatus()
                    CurrentJobCard(
                        summary = active,
                        nextCta = active.runnerNextCtaLabel(),
                        onNextStatus = next?.let { status ->
                            { onAdvance(active.id, status) }
                        },
                        onChat = { onChat(active.id) },
                        onDetails = { onDetails(active.id) },
                        isAr = isAr,
                    )
                }
            } ?: item {
                HatliCard {
                    Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
                        Text(
                            text = if (isAr) "لا توجد وظيفة نشطة" else "No active job",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = if (isAr) {
                                "لما تقبل طلب، هيظهر هنا مع حالة التنفيذ والمحادثة."
                            } else {
                                "When you accept a request, it pins here with status actions and chat."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            if (!online) {
                item {
                    Text(
                        text = if (isAr) {
                            "افتح الاستقبال عشان تشوف الطلبات القريبة."
                        } else {
                            "Go online to browse nearby requests."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else if (snapshot.nearby.isEmpty()) {
                item {
                    Text(
                        text = if (isAr) {
                            "مفيش طلبات قريبة دلوقتي — خلي الاستقبال مفتوح."
                        } else {
                            "No nearby requests right now — stay online."
                        },
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            } else {
                item {
                    Text(
                        text = if (isAr) "طلبات قريبة" else "Nearby Requests",
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                items(snapshot.nearby.take(3), key = { it.id }) { job ->
                    PendingJobCard(
                        summary = job,
                        isAr = isAr,
                        onAccept = { onAccept(job.id) },
                        onDetails = { onDetails(job.id) },
                    )
                }
                item {
                    HatliSecondaryButton(
                        text = if (isAr) "عرض الكل" else "View all jobs",
                        onClick = onViewAll,
                    )
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
    onOpenDetail: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    onRate: (String) -> Unit,
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
                items(items, key = { it.id }) { row ->
                    RequestRowCard(
                        summary = row,
                        isAr = isAr,
                        onClick = { onOpenDetail(row.id) },
                        trailingAction = {
                            Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.xs)) {
                                if (row.canOpenChat) {
                                    HatliSecondaryButton(
                                        text = if (isAr) "محادثة" else "Chat",
                                        onClick = { onOpenChat(row.id) },
                                    )
                                }
                                if (row.needsRating) {
                                    HatliPrimaryButton(
                                        text = if (isAr) "قيّم" else "Rate",
                                        onClick = { onRate(row.id) },
                                    )
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun JobsListContent(
    filter: JobListFilter,
    items: List<RequestSummary>,
    onFilter: (JobListFilter) -> Unit,
    onAccept: (String) -> Unit,
    onOpenDetail: (String) -> Unit,
    onOpenChat: (String) -> Unit,
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
                text = if (isAr) {
                    "مافيش وظائف — اقبل طلب من Nearby أو افتح الاستقبال"
                } else {
                    "No jobs yet — accept a nearby request or go online"
                },
                style = MaterialTheme.typography.bodyLarge,
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
                items(items, key = { it.id }) { job ->
                    if (job.status == RequestStatus.Pending) {
                        PendingJobCard(
                            summary = job,
                            isAr = isAr,
                            onAccept = { onAccept(job.id) },
                            onDetails = { onOpenDetail(job.id) },
                        )
                    } else {
                        RequestRowCard(
                            summary = job,
                            isAr = isAr,
                            onClick = { onOpenDetail(job.id) },
                            trailingAction = {
                                if (job.canOpenChat) {
                                    HatliSecondaryButton(
                                        text = if (isAr) "محادثة" else "Chat",
                                        onClick = { onOpenChat(job.id) },
                                    )
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PendingJobCard(
    summary: RequestSummary,
    isAr: Boolean,
    onAccept: () -> Unit,
    onDetails: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm)) {
        RequestRowCard(
            summary = summary,
            isAr = isAr,
            onClick = onDetails,
        )
        HatliPrimaryButton(
            text = if (isAr) "قبول" else "Accept",
            onClick = onAccept,
        )
    }
}

@Composable
fun MessagesContent(
    conversations: List<RequestSummary>,
    isAr: Boolean,
    onOpenChat: (String) -> Unit,
) {
    if (conversations.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(HatliSpacing.lg),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
            ) {
                Text(
                    text = if (isAr) "مافيش محادثات لسه" else "No conversations yet",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = if (isAr) {
                        "لما يتقبل طلب، هتقدر تفتح الشات من هنا أو من الطلب الحالي."
                    } else {
                        "After a request is accepted, open chat here or from your Current Request / Active Job."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(HatliSpacing.md),
            verticalArrangement = Arrangement.spacedBy(HatliSpacing.sm),
        ) {
            items(conversations, key = { it.id }) { row ->
                ConversationRowCard(
                    summary = row,
                    isAr = isAr,
                    onClick = { onOpenChat(row.id) },
                )
            }
        }
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
                    title = "2 kg meat",
                    status = RequestStatus.Accepted,
                    category = RequestCategory.Shopping,
                    runnerName = "Ahmed",
                ),
                recent = emptyList(),
                quickActions = listOf(
                    QuickAction("a", "Shopping", "تسوق"),
                ),
                tipEn = "Write clearly",
                tipAr = "اكتب بوضوح",
            ),
            isAr = false,
            onCreate = {},
            onQuickAction = {},
            onChat = {},
            onDetails = {},
            onConfirm = {},
            onRate = {},
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
                activeJob = RequestSummary(
                    id = "2",
                    title = "Pharmacy pickup",
                    status = RequestStatus.Accepted,
                    category = RequestCategory.Pickup,
                    requesterName = "Sara",
                ),
                nearby = listOf(
                    RequestSummary(
                        id = "1",
                        title = "2 kg meat",
                        status = RequestStatus.Pending,
                        distanceMeters = 800,
                        offerAmount = 35.0,
                    ),
                ),
            ),
            isAr = false,
            onToggleOnline = {},
            onViewAll = {},
            onAccept = {},
            onChat = {},
            onDetails = {},
            onAdvance = { _, _ -> },
        )
    }
}
