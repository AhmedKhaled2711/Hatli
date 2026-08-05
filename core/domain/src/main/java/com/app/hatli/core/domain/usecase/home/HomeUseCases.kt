package com.app.hatli.core.domain.usecase.home

import com.app.hatli.core.domain.repository.HomeRepository
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.core.model.RunnerHomeSnapshot
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRequesterHomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    operator fun invoke(): Flow<RequesterHomeSnapshot> = homeRepository.observeRequesterHome()
}

class ObserveRunnerHomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    operator fun invoke(): Flow<RunnerHomeSnapshot> = homeRepository.observeRunnerHome()
}

class ObserveRequesterRequestsUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    operator fun invoke(): Flow<List<RequestSummary>> = homeRepository.observeRequesterRequests()
}

class ObserveRunnerJobsUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    operator fun invoke(): Flow<List<RequestSummary>> = homeRepository.observeRunnerJobs()
}

class SetRunnerOnlineUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke(online: Boolean) = homeRepository.setRunnerOnline(online)
}
