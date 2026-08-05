package com.app.hatli.core.domain.repository

import com.app.hatli.core.common.result.Result
import com.app.hatli.core.model.ChatMessage
import com.app.hatli.core.model.CreateRequestInput
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestRating
import com.app.hatli.core.model.RequestStatus
import kotlinx.coroutines.flow.Flow

interface RequestRepository {
    suspend fun create(input: CreateRequestInput): Result<String>

    suspend fun accept(requestId: String): Result<Unit>

    /** Runner advances to the next legal status (or explicit target if legal). */
    suspend fun advanceStatus(requestId: String, toStatus: RequestStatus): Result<Unit>

    suspend fun confirmDelivery(requestId: String): Result<Unit>

    suspend fun cancel(requestId: String, reason: String): Result<Unit>

    suspend fun submitRating(requestId: String, rating: RequestRating): Result<Unit>

    fun observeRequest(requestId: String): Flow<Request?>

    fun observeRequesterRequests(requesterId: String): Flow<List<Request>>

    fun observePendingMarketplace(limit: Int = 20): Flow<List<Request>>

    fun observeRunnerAssigned(runnerId: String): Flow<List<Request>>

    fun observeMessages(requestId: String): Flow<List<ChatMessage>>

    suspend fun sendMessage(requestId: String, text: String, clientId: String): Result<Unit>

    suspend fun markChatRead(requestId: String): Result<Unit>
}
