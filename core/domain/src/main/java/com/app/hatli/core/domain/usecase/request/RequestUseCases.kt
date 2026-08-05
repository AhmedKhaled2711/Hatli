package com.app.hatli.core.domain.usecase.request

import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.repository.RequestRepository
import com.app.hatli.core.model.ChatMessage
import com.app.hatli.core.model.CreateRequestInput
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestRating
import com.app.hatli.core.model.RequestStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateRequestUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    suspend operator fun invoke(input: CreateRequestInput): Result<String> =
        requestRepository.create(input)
}

class AcceptRequestUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    suspend operator fun invoke(requestId: String): Result<Unit> =
        requestRepository.accept(requestId)
}

class AdvanceRequestStatusUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    suspend operator fun invoke(requestId: String, toStatus: RequestStatus): Result<Unit> =
        requestRepository.advanceStatus(requestId, toStatus)
}

class ConfirmDeliveryUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    suspend operator fun invoke(requestId: String): Result<Unit> =
        requestRepository.confirmDelivery(requestId)
}

class CancelRequestUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    suspend operator fun invoke(requestId: String, reason: String): Result<Unit> =
        requestRepository.cancel(requestId, reason)
}

class SubmitRatingUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    suspend operator fun invoke(requestId: String, stars: Int, comment: String?): Result<Unit> =
        requestRepository.submitRating(
            requestId,
            RequestRating(stars = stars, comment = comment?.ifBlank { null }),
        )
}

class ObserveRequestUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    operator fun invoke(requestId: String): Flow<Request?> =
        requestRepository.observeRequest(requestId)
}

class ObservePendingRequestsUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    operator fun invoke(limit: Int = 20): Flow<List<Request>> =
        requestRepository.observePendingMarketplace(limit)
}

class ObserveMessagesUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    operator fun invoke(requestId: String): Flow<List<ChatMessage>> =
        requestRepository.observeMessages(requestId)
}

class SendMessageUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    suspend operator fun invoke(requestId: String, text: String, clientId: String): Result<Unit> =
        requestRepository.sendMessage(requestId, text, clientId)
}

class MarkChatReadUseCase @Inject constructor(
    private val requestRepository: RequestRepository,
) {
    suspend operator fun invoke(requestId: String): Result<Unit> =
        requestRepository.markChatRead(requestId)
}
