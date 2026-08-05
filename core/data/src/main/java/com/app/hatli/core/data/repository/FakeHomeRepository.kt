package com.app.hatli.core.data.repository

import com.app.hatli.core.domain.repository.HomeRepository
import com.app.hatli.core.model.QuickAction
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.core.model.RunnerHomeSnapshot
import com.app.hatli.core.model.RunnerHomeStats
import com.app.hatli.core.model.RunnerPresence
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Seeded demo data for Phase 2 JTBD Home until Firestore `requests` ships.
 */
@Singleton
class FakeHomeRepository @Inject constructor() : HomeRepository {

    private val isOnline = MutableStateFlow(false)
    private val demoMode = MutableStateFlow(DemoMode.RequesterSearching)

    private val searching = RequestSummary(
        id = "req_1",
        title = "2 كيلو لحمة",
        status = RequestStatus.SearchingRunner,
        etaMinutes = 30,
        updatedAtEpochMs = System.currentTimeMillis(),
    )
    private val accepted = RequestSummary(
        id = "req_2",
        title = "طلب من السوبر ماركت",
        status = RequestStatus.Accepted,
        etaMinutes = 25,
        runnerName = "أحمد",
        updatedAtEpochMs = System.currentTimeMillis(),
    )
    private val completed = RequestSummary(
        id = "req_3",
        title = "دواء من الصيدلية",
        status = RequestStatus.Completed,
        offerAmount = 40.0,
        updatedAtEpochMs = System.currentTimeMillis() - 86_400_000,
    )
    private val nearby = listOf(
        RequestSummary(
            id = "job_1",
            title = "2 كيلو لحمة",
            status = RequestStatus.SearchingRunner,
            distanceMeters = 800,
            offerAmount = 35.0,
        ),
        RequestSummary(
            id = "job_2",
            title = "طلب من الفرن",
            status = RequestStatus.SearchingRunner,
            distanceMeters = 1200,
            offerAmount = 28.0,
        ),
        RequestSummary(
            id = "job_3",
            title = "مشوار صيدلية",
            status = RequestStatus.SearchingRunner,
            distanceMeters = 450,
            offerAmount = 22.0,
        ),
    )

    private val quickActions = listOf(
        QuickAction("supermarket", "From supermarket", "اطلب من السوبر ماركت"),
        QuickAction("butcher", "From butcher", "اطلب من الجزار"),
        QuickAction("bakery", "From bakery", "اطلب من الفرن"),
        QuickAction("pharmacy", "From pharmacy", "اطلب من الصيدلية"),
        QuickAction("anything", "Anything nearby", "اطلب أي حاجة"),
    )

    override fun observeRequesterHome(): Flow<RequesterHomeSnapshot> =
        demoMode.map { mode ->
            when (mode) {
                DemoMode.RequesterEmpty -> RequesterHomeSnapshot(
                    active = null,
                    recent = emptyList(),
                    quickActions = quickActions,
                    tipEn = "Write a clear description of what you need.",
                    tipAr = "اعمل وصف واضح للطلبات.",
                    unreadNotifications = 0,
                )
                DemoMode.RequesterSearching -> RequesterHomeSnapshot(
                    active = searching,
                    recent = listOf(searching, completed),
                    quickActions = quickActions,
                    tipEn = "Write a clear description of what you need.",
                    tipAr = "اعمل وصف واضح للطلبات.",
                    unreadNotifications = 2,
                )
                DemoMode.RequesterAccepted -> RequesterHomeSnapshot(
                    active = accepted,
                    recent = listOf(accepted, completed),
                    quickActions = quickActions,
                    tipEn = "Write a clear description of what you need.",
                    tipAr = "اعمل وصف واضح للطلبات.",
                    unreadNotifications = 1,
                )
                else -> RequesterHomeSnapshot(
                    active = searching,
                    recent = listOf(searching, completed),
                    quickActions = quickActions,
                    tipEn = "Write a clear description of what you need.",
                    tipAr = "اعمل وصف واضح للطلبات.",
                    unreadNotifications = 2,
                )
            }
        }

    override fun observeRunnerHome(): Flow<RunnerHomeSnapshot> =
        combine(isOnline, demoMode) { online, _ ->
            val stats = RunnerHomeStats(
                dailyEarnings = if (online) 120.0 else 0.0,
                todayJobsCount = if (online) 6 else 0,
                rating = 4.9,
            )
            when {
                !online -> RunnerHomeSnapshot(
                    presence = RunnerPresence(false),
                    stats = stats,
                    nearby = emptyList(),
                    unreadNotifications = 0,
                )
                else -> RunnerHomeSnapshot(
                    presence = RunnerPresence(true),
                    stats = stats,
                    nearby = nearby,
                    activeJob = null,
                    unreadNotifications = 3,
                )
            }
        }

    override fun observeRequesterRequests(): Flow<List<RequestSummary>> =
        observeRequesterHome().map { snap ->
            buildList {
                snap.active?.let { add(it) }
                addAll(snap.recent.filter { it.id != snap.active?.id })
                if (none { it.status == RequestStatus.Cancelled }) {
                    add(
                        RequestSummary(
                            id = "req_x",
                            title = "طلب ملغي",
                            status = RequestStatus.Cancelled,
                        ),
                    )
                }
            }
        }

    override fun observeRunnerJobs(): Flow<List<RequestSummary>> =
        observeRunnerHome().map { it.nearby }

    override suspend fun setRunnerOnline(online: Boolean) {
        isOnline.update { online }
    }

    /** Demo harness — not exposed to UI yet; default Searching for rich Home. */
    private enum class DemoMode {
        RequesterEmpty,
        RequesterSearching,
        RequesterAccepted,
    }
}
