package com.app.hatli.feature.chat.presentation

import androidx.lifecycle.SavedStateHandle
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.repository.UserRepository
import com.app.hatli.core.domain.usecase.request.MarkChatReadUseCase
import com.app.hatli.core.domain.usecase.request.ObserveMessagesUseCase
import com.app.hatli.core.domain.usecase.request.ObserveRequestUseCase
import com.app.hatli.core.domain.usecase.request.SendMessageUseCase
import com.app.hatli.core.model.AuthUser
import com.app.hatli.core.model.ChatMessage
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.model.UserRole
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private val authRepository: AuthRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val observeMessagesUseCase: ObserveMessagesUseCase = mockk()
    private val observeRequestUseCase: ObserveRequestUseCase = mockk()
    private val sendMessageUseCase: SendMessageUseCase = mockk()
    private val markChatReadUseCase: MarkChatReadUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()
    private val testRequestId = "req_100"

    private val requesterUid = "uid_client"
    private val runnerUid = "uid_runner"

    private val sampleRequest = Request(
        id = testRequestId,
        requesterId = requesterUid,
        runnerId = runnerUid,
        requesterName = "Mina Client",
        runnerName = "Captain Tarek",
        category = RequestCategory.Shopping,
        status = RequestStatus.Accepted,
        title = "Grocery Order",
        description = "Milk and bread",
        deliveryFee = 35.0,
    )

    private val clientProfile = UserProfile(
        uid = requesterUid,
        role = UserRole.Requester,
        phone = "01000000000",
        firstName = "Mina",
        lastName = "Client",
        email = "mina@example.com",
        gender = Gender.Male,
        createdAt = 0L,
        updatedAt = 0L,
        profileCompleted = true,
    )

    private val runnerProfile = UserProfile(
        uid = runnerUid,
        role = UserRole.Runner,
        phone = "01111111111",
        firstName = "Captain",
        lastName = "Tarek",
        email = "tarek@example.com",
        gender = Gender.Male,
        createdAt = 0L,
        updatedAt = 0L,
        profileCompleted = true,
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { markChatReadUseCase(testRequestId) } returns Result.Success(Unit)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): ChatViewModel {
        return ChatViewModel(
            savedStateHandle = SavedStateHandle(mapOf("requestId" to testRequestId)),
            authRepository = authRepository,
            userRepository = userRepository,
            observeMessages = observeMessagesUseCase,
            observeRequest = observeRequestUseCase,
            sendMessage = sendMessageUseCase,
            markChatRead = markChatReadUseCase,
        )
    }

    @Test
    fun requester_chat_displays_runner_name_and_status() = runTest {
        every { authRepository.currentUser() } returns AuthUser(requesterUid, "01000000000")
        every { userRepository.observeProfile(requesterUid) } returns flowOf(clientProfile)
        every { observeMessagesUseCase(testRequestId) } returns flowOf(emptyList())
        every { observeRequestUseCase(testRequestId) } returns flowOf(sampleRequest)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isRequester).isTrue()
        assertThat(state.participantName).isEqualTo("Captain Tarek")
        assertThat(state.requestStatus).isEqualTo(RequestStatus.Accepted)
        assertThat(state.isRequestLoading).isFalse()
    }

    @Test
    fun runner_chat_displays_requester_name_and_status() = runTest {
        every { authRepository.currentUser() } returns AuthUser(runnerUid, "01111111111")
        every { userRepository.observeProfile(runnerUid) } returns flowOf(runnerProfile)
        every { observeMessagesUseCase(testRequestId) } returns flowOf(emptyList())
        every { observeRequestUseCase(testRequestId) } returns flowOf(sampleRequest)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isRequester).isFalse()
        assertThat(state.participantName).isEqualTo("Mina Client")
        assertThat(state.requestStatus).isEqualTo(RequestStatus.Accepted)
        assertThat(state.isRequestLoading).isFalse()
    }

    @Test
    fun order_status_transitions_update_reactively_in_real_time() = runTest {
        val requestFlow = MutableSharedFlow<Request?>()

        every { authRepository.currentUser() } returns AuthUser(requesterUid, "01000000000")
        every { userRepository.observeProfile(requesterUid) } returns flowOf(clientProfile)
        every { observeMessagesUseCase(testRequestId) } returns flowOf(emptyList())
        every { observeRequestUseCase(testRequestId) } returns requestFlow

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.requestStatus).isNull()
        assertThat(viewModel.uiState.value.isRequestLoading).isTrue()

        // Status: InProgress
        requestFlow.emit(sampleRequest.copy(status = RequestStatus.InProgress))
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.requestStatus).isEqualTo(RequestStatus.InProgress)

        // Status: Purchased
        requestFlow.emit(sampleRequest.copy(status = RequestStatus.Purchased))
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.requestStatus).isEqualTo(RequestStatus.Purchased)

        // Status: OnTheWay
        requestFlow.emit(sampleRequest.copy(status = RequestStatus.OnTheWay))
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.requestStatus).isEqualTo(RequestStatus.OnTheWay)

        // Status: Delivered
        requestFlow.emit(sampleRequest.copy(status = RequestStatus.Delivered))
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.requestStatus).isEqualTo(RequestStatus.Delivered)

        // Status: Completed
        requestFlow.emit(sampleRequest.copy(status = RequestStatus.Completed))
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.uiState.value.requestStatus).isEqualTo(RequestStatus.Completed)
    }

    @Test
    fun fallback_profile_lookup_resolves_missing_runner_name() = runTest {
        val requestWithoutRunnerName = sampleRequest.copy(runnerName = null)

        every { authRepository.currentUser() } returns AuthUser(requesterUid, "01000000000")
        every { userRepository.observeProfile(requesterUid) } returns flowOf(clientProfile)
        every { observeMessagesUseCase(testRequestId) } returns flowOf(emptyList())
        every { observeRequestUseCase(testRequestId) } returns flowOf(requestWithoutRunnerName)
        coEvery { userRepository.getProfile(runnerUid) } returns Result.Success(runnerProfile)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.participantName).isEqualTo("Captain Tarek")
    }

    @Test
    fun messages_render_correct_sender_labels() = runTest {
        val messages = listOf(
            ChatMessage(
                id = "m1",
                requestId = testRequestId,
                senderId = requesterUid,
                text = "Hello Captain",
                createdAtEpochMs = 1000L,
            ),
            ChatMessage(
                id = "m2",
                requestId = testRequestId,
                senderId = runnerUid,
                text = "On my way to the store",
                createdAtEpochMs = 2000L,
            ),
        )

        every { authRepository.currentUser() } returns AuthUser(requesterUid, "01000000000")
        every { userRepository.observeProfile(requesterUid) } returns flowOf(clientProfile)
        every { observeMessagesUseCase(testRequestId) } returns flowOf(messages)
        every { observeRequestUseCase(testRequestId) } returns flowOf(sampleRequest)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.messages).hasSize(2)

        val m1 = state.messages[0]
        assertThat(m1.isMine).isTrue()
        assertThat(m1.senderLabel).isEqualTo("You")

        val m2 = state.messages[1]
        assertThat(m2.isMine).isFalse()
        assertThat(m2.senderLabel).isEqualTo("Captain Tarek")
    }

    @Test
    fun sending_message_clears_draft_on_success() = runTest {
        every { authRepository.currentUser() } returns AuthUser(requesterUid, "01000000000")
        every { userRepository.observeProfile(requesterUid) } returns flowOf(clientProfile)
        every { observeMessagesUseCase(testRequestId) } returns flowOf(emptyList())
        every { observeRequestUseCase(testRequestId) } returns flowOf(sampleRequest)
        coEvery { sendMessageUseCase(testRequestId, "Need two bags of bread", any()) } returns Result.Success(Unit)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ChatUiEvent.DraftChanged("Need two bags of bread"))
        viewModel.onEvent(ChatUiEvent.Send)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.draft).isEmpty()
        assertThat(state.isSending).isFalse()

        coVerify { sendMessageUseCase(testRequestId, "Need two bags of bread", any()) }
    }
}
