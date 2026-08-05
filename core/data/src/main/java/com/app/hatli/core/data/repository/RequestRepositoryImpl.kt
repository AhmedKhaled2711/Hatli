package com.app.hatli.core.data.repository

import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.data.auth.FirebaseErrorMapper
import com.app.hatli.core.data.request.AppRequestException
import com.app.hatli.core.data.request.FirestoreRequestDataSource
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.repository.RequestRepository
import com.app.hatli.core.domain.repository.UserRepository
import com.app.hatli.core.model.ChatMessage
import com.app.hatli.core.model.CreateRequestInput
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestRating
import com.app.hatli.core.model.RequestStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreRequestDataSource,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : RequestRepository {

    private fun requireUid(): String? = authRepository.currentUser()?.uid

    private suspend fun displayName(uid: String): String? =
        when (val r = userRepository.getProfile(uid)) {
            is Result.Success -> r.data?.fullName?.ifBlank { null }
            else -> null
        }

    private fun mapError(t: Throwable): Result.Error {
        if (t is AppRequestException || t.cause is AppRequestException) {
            val msg = (t as? AppRequestException)?.message
                ?: (t.cause as? AppRequestException)?.message
                ?: "Request update failed."
            return Result.Error(AppError.FirebaseFailure(msg, t))
        }
        return Result.Error(FirebaseErrorMapper.map(t))
    }

    override suspend fun create(input: CreateRequestInput): Result<String> {
        val uid = requireUid() ?: return Result.Error(AppError.Unauthorized())
        return try {
            val name = displayName(uid) ?: input.requesterName
            Result.Success(dataSource.create(uid, name, input.copy(requesterName = name)))
        } catch (t: Throwable) {
            mapError(t)
        }
    }

    override suspend fun accept(requestId: String): Result<Unit> {
        val uid = requireUid() ?: return Result.Error(AppError.Unauthorized())
        return try {
            dataSource.accept(
                requestId = requestId,
                runnerId = uid,
                runnerName = displayName(uid),
                requesterNameFallback = null,
            )
            Result.Success(Unit)
        } catch (t: Throwable) {
            mapError(t)
        }
    }

    override suspend fun advanceStatus(requestId: String, toStatus: RequestStatus): Result<Unit> {
        val uid = requireUid() ?: return Result.Error(AppError.Unauthorized())
        return try {
            dataSource.advanceStatus(requestId, uid, toStatus)
            Result.Success(Unit)
        } catch (t: Throwable) {
            mapError(t)
        }
    }

    override suspend fun confirmDelivery(requestId: String): Result<Unit> {
        val uid = requireUid() ?: return Result.Error(AppError.Unauthorized())
        return try {
            dataSource.confirmDelivery(requestId, uid)
            Result.Success(Unit)
        } catch (t: Throwable) {
            mapError(t)
        }
    }

    override suspend fun cancel(requestId: String, reason: String): Result<Unit> {
        val uid = requireUid() ?: return Result.Error(AppError.Unauthorized())
        if (reason.isBlank()) return Result.Error(AppError.InvalidInput("Enter a cancel reason."))
        return try {
            dataSource.cancel(requestId, uid, reason.trim())
            Result.Success(Unit)
        } catch (t: Throwable) {
            mapError(t)
        }
    }

    override suspend fun submitRating(requestId: String, rating: RequestRating): Result<Unit> {
        val uid = requireUid() ?: return Result.Error(AppError.Unauthorized())
        return try {
            dataSource.submitRating(requestId, uid, rating)
            Result.Success(Unit)
        } catch (t: Throwable) {
            mapError(t)
        }
    }

    override fun observeRequest(requestId: String): Flow<Request?> {
        if (requestId.isBlank()) return flowOf(null)
        return dataSource.observeRequest(requestId)
    }

    override fun observeRequesterRequests(requesterId: String): Flow<List<Request>> {
        if (requesterId.isBlank()) return flowOf(emptyList())
        return dataSource.observeByRequester(requesterId)
    }

    override fun observePendingMarketplace(limit: Int): Flow<List<Request>> =
        dataSource.observePending(limit)

    override fun observeRunnerAssigned(runnerId: String): Flow<List<Request>> {
        if (runnerId.isBlank()) return flowOf(emptyList())
        return dataSource.observeByRunner(runnerId)
    }

    override fun observeMessages(requestId: String): Flow<List<ChatMessage>> {
        if (requestId.isBlank()) return flowOf(emptyList())
        return dataSource.observeMessages(requestId)
    }

    override suspend fun sendMessage(
        requestId: String,
        text: String,
        clientId: String,
    ): Result<Unit> {
        val uid = requireUid() ?: return Result.Error(AppError.Unauthorized())
        return try {
            val req = dataSource.get(requestId)
                ?: return Result.Error(AppError.NotFound("Request not found."))
            val isRequester = req.requesterId == uid
            val isRunner = req.runnerId == uid
            if (!isRequester && !isRunner) {
                return Result.Error(AppError.Unauthorized("Not a participant."))
            }
            if (req.status == RequestStatus.Pending) {
                return Result.Error(AppError.InvalidInput("Chat opens after Accept."))
            }
            dataSource.sendMessage(requestId, uid, text, clientId, isRequester)
            Result.Success(Unit)
        } catch (t: Throwable) {
            mapError(t)
        }
    }

    override suspend fun markChatRead(requestId: String): Result<Unit> {
        val uid = requireUid() ?: return Result.Error(AppError.Unauthorized())
        return try {
            val req = dataSource.get(requestId)
                ?: return Result.Error(AppError.NotFound("Request not found."))
            dataSource.markChatRead(requestId, req.requesterId == uid)
            Result.Success(Unit)
        } catch (t: Throwable) {
            mapError(t)
        }
    }
}
