package com.app.hatli.core.data.repository

import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.repository.HomeRepository
import com.app.hatli.core.domain.repository.RequestRepository
import com.app.hatli.core.model.QuickAction
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.core.model.RunnerHomeSnapshot
import com.app.hatli.core.model.RunnerHomeStats
import com.app.hatli.core.model.RunnerPresence
import com.app.hatli.core.model.toSummary
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
import com.app.hatli.core.domain.repository.UserRepository


/**
 * Home aggregation over Firestore [RequestRepository] + local runner presence.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val requestRepository: RequestRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : HomeRepository {

    private val isOnline = MutableStateFlow(false)

    private val quickActions = listOf(
        QuickAction("shopping", "Shopping", "تسوق"),
        QuickAction("pickup", "Pickup", "استلام وتوصيل"),
        QuickAction("bills", "Bills", "فواتير"),
        QuickAction("custom", "Custom", "طلب مخصص"),
    )

    private val emptyRequesterHome = RequesterHomeSnapshot(
        quickActions = quickActions,
        tipEn = "Describe what you need clearly.",
        tipAr = "اوصف اللي محتاجه بوضوح.",
    )

    override fun observeRequesterHome(): Flow<RequesterHomeSnapshot> =
        authRepository.authState.flatMapLatest { user ->
            val uid = user?.uid.orEmpty()
            if (uid.isBlank()) {
                flowOf(emptyRequesterHome)
            } else {
                combine(
                    requestRepository.observeRequesterRequests(uid),
                    userRepository.observeProfile(uid),
                ) { list, profile ->
                    val city = profile?.address?.city?.takeIf { it.isNotBlank() }
                    val area = profile?.address?.area?.takeIf { it.isNotBlank() }
                    val locationStr = listOfNotNull(city, area).joinToString(" · ")
                    val summaries = list.map { req ->
                        req.toSummary().copy(unreadCount = req.requesterUnreadCount)
                    }
                    // Prefer in-flight work; fall back to completed-but-unrated so Rate is reachable.
                    val active = summaries.firstOrNull { it.status.isActiveForRequester() }
                        ?: summaries.firstOrNull { it.status == RequestStatus.Completed && !it.hasRating }
                    emptyRequesterHome.copy(
                        neighborhood = locationStr,
                        active = active,
                        recent = summaries.take(5),
                        unreadNotifications = active?.unreadCount ?: 0,
                    )
                }
            }
        }

    override fun observeRunnerHome(): Flow<RunnerHomeSnapshot> =
        authRepository.authState.flatMapLatest { user ->
            val uid = user?.uid.orEmpty()
            val pendingFlow = requestRepository.observePendingMarketplace(limit = 20)
            val assignedFlow = if (uid.isBlank()) {
                flowOf(emptyList())
            } else {
                requestRepository.observeRunnerAssigned(uid)
            }
            combine(isOnline, pendingFlow, assignedFlow) { online, pending, assigned ->
                val nearby = if (online) {
                    pending.take(3).map { it.toSummary() }
                } else {
                    emptyList()
                }
                val activeReq = assigned.firstOrNull { it.status.isActiveForRunner() }
                val activeJob = activeReq?.toSummary()?.copy(
                    unreadCount = activeReq.runnerUnreadCount,
                )
                val completedOrders = assigned.filter { it.status == RequestStatus.Completed }
                val todayCompleted = completedOrders.count()
                val earnings = completedOrders.sumOf { it.deliveryFee }
                val ratedOrders = assigned.mapNotNull { it.rating?.stars }
                val calculatedRating = if (ratedOrders.isNotEmpty()) {
                    ratedOrders.average()
                } else {
                    0.0
                }
                RunnerHomeSnapshot(
                    presence = RunnerPresence(online),
                    stats = RunnerHomeStats(
                        dailyEarnings = if (online) earnings else 0.0,
                        todayJobsCount = if (online) {
                            todayCompleted + (if (activeJob != null) 1 else 0)
                        } else {
                            0
                        },
                        rating = calculatedRating,
                    ),
                    nearby = nearby,
                    activeJob = activeJob,
                    unreadNotifications = activeJob?.unreadCount ?: 0,
                )
            }
        }

    override fun observeRequesterRequests(): Flow<List<RequestSummary>> =
        authRepository.authState.flatMapLatest { user ->
            val uid = user?.uid.orEmpty()
            if (uid.isBlank()) {
                flowOf(emptyList())
            } else {
                requestRepository.observeRequesterRequests(uid).map { list ->
                    list.map { it.toSummary() }
                }
            }
        }

    override fun observeRunnerJobs(): Flow<List<RequestSummary>> =
        authRepository.authState.flatMapLatest { user ->
            val uid = user?.uid.orEmpty()
            val assignedFlow = if (uid.isBlank()) {
                flowOf(emptyList())
            } else {
                requestRepository.observeRunnerAssigned(uid)
            }
            // Assigned / Active Job always visible; Pending marketplace only while Online.
            combine(isOnline, assignedFlow, requestRepository.observePendingMarketplace(limit = 50)) {
                    online,
                    assigned,
                    pending,
                ->
                val marketplace = if (online) pending else emptyList()
                (assigned + marketplace)
                    .distinctBy { it.id }
                    .sortedByDescending { it.updatedAtEpochMs }
                    .map { it.toSummary() }
            }
        }

    override suspend fun setRunnerOnline(online: Boolean) {
        isOnline.update { online }
    }
}

private fun RequestStatus.isActiveForRequester(): Boolean = when (this) {
    RequestStatus.Pending,
    RequestStatus.Accepted,
    RequestStatus.InProgress,
    RequestStatus.Purchased,
    RequestStatus.OnTheWay,
    RequestStatus.Delivered,
    -> true
    RequestStatus.Completed, RequestStatus.Cancelled -> false
}

private fun RequestStatus.isActiveForRunner(): Boolean = when (this) {
    RequestStatus.Accepted,
    RequestStatus.InProgress,
    RequestStatus.Purchased,
    RequestStatus.OnTheWay,
    RequestStatus.Delivered,
    -> true
    else -> false
}
