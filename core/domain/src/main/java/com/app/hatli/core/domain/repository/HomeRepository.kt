package com.app.hatli.core.domain.repository

import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.core.model.RequestSummary
import com.app.hatli.core.model.RunnerHomeSnapshot
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun observeRequesterHome(): Flow<RequesterHomeSnapshot>
    fun observeRunnerHome(): Flow<RunnerHomeSnapshot>
    fun observeRequesterRequests(): Flow<List<RequestSummary>>
    fun observeRunnerJobs(): Flow<List<RequestSummary>>
    suspend fun setRunnerOnline(online: Boolean)
}
