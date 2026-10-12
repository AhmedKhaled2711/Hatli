package com.app.hatli.feature.request.presentation.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.usecase.request.AdvanceRequestStatusUseCase
import com.app.hatli.core.domain.usecase.request.CancelRequestUseCase
import com.app.hatli.core.domain.usecase.request.ConfirmDeliveryUseCase
import com.app.hatli.core.domain.usecase.request.ObserveRequestUseCase
import com.app.hatli.core.model.AuthUser
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RequestDetailViewModelTest {

    private val observeRequestUseCase: ObserveRequestUseCase = mockk()
    private val observeRunnerAssigned: com.app.hatli.core.domain.usecase.request.ObserveRunnerAssignedRequestsUseCase = mockk()
    private val authRepository: AuthRepository = mockk()
    private val cancelRequestUseCase: CancelRequestUseCase = mockk()
    private val advanceRequestStatusUseCase: AdvanceRequestStatusUseCase = mockk()
    private val confirmDeliveryUseCase: ConfirmDeliveryUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()
    private val testRequestId = "req_123"
    private val mockUser = AuthUser(uid = "user_req", phoneNumber = "01000000000")

    private val sampleRequest = Request(
        id = testRequestId,
        requesterId = "user_req",
        runnerId = "user_runner",
        category = RequestCategory.Shopping,
        status = RequestStatus.Accepted,
        title = "Grocery Shopping",
        description = "Buy milk and bread",
        deliveryFee = 35.0,
    )

    private fun createViewModel(): RequestDetailViewModel {
        return RequestDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("requestId" to testRequestId)),
            observeRequest = observeRequestUseCase,
            observeRunnerAssigned = observeRunnerAssigned,
            authRepository = authRepository,
            cancelRequest = cancelRequestUseCase,
            advanceStatus = advanceRequestStatusUseCase,
            confirmDelivery = confirmDeliveryUseCase,
        )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { authRepository.currentUser() } returns mockUser
        every { observeRunnerAssigned(any()) } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `observing request updates uiState correctly for requester`() = runTest {
        every { observeRequestUseCase(testRequestId) } returns flowOf(sampleRequest)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.request).isEqualTo(sampleRequest)
        assertThat(state.isRequester).isTrue()
        assertThat(state.isRunner).isFalse()
        assertThat(state.runnerNextCta).isNull()
    }

    @Test
    fun `observing request updates uiState correctly for runner`() = runTest {
        val runnerUser = AuthUser(uid = "user_runner", phoneNumber = "01100000000")
        every { authRepository.currentUser() } returns runnerUser
        every { observeRequestUseCase(testRequestId) } returns flowOf(sampleRequest)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.isRequester).isFalse()
        assertThat(state.isRunner).isTrue()
        assertThat(state.runnerNextCta).isEqualTo("Start work")
    }

    @Test
    fun `AdvanceStatus calls usecase and emits ShowMessage effect on success`() = runTest {
        val runnerUser = AuthUser(uid = "user_runner", phoneNumber = "01100000000")
        every { authRepository.currentUser() } returns runnerUser
        every { observeRequestUseCase(testRequestId) } returns flowOf(sampleRequest)
        coEvery { advanceRequestStatusUseCase(testRequestId, RequestStatus.InProgress) } returns Result.Success(Unit)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onEvent(RequestDetailUiEvent.AdvanceStatus)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(RequestDetailUiEffect.ShowMessage("Status updated"))
        }
    }

    @Test
    fun `ConfirmDelivery calls usecase and emits NavigateRate effect on success`() = runTest {
        val deliveredRequest = sampleRequest.copy(status = RequestStatus.Delivered)
        every { observeRequestUseCase(testRequestId) } returns flowOf(deliveredRequest)
        coEvery { confirmDeliveryUseCase(testRequestId) } returns Result.Success(Unit)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onEvent(RequestDetailUiEvent.ConfirmDelivery)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(RequestDetailUiEffect.NavigateRate(testRequestId))
        }
    }

    @Test
    fun `ConfirmCancel calls usecase and emits ShowMessage and NavigateBack on success`() = runTest {
        every { observeRequestUseCase(testRequestId) } returns flowOf(sampleRequest)
        coEvery { cancelRequestUseCase(testRequestId, "Changed mind") } returns Result.Success(Unit)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RequestDetailUiEvent.CancelReasonChanged("Changed mind"))

        viewModel.uiEffect.test {
            viewModel.onEvent(RequestDetailUiEvent.ConfirmCancel)
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(RequestDetailUiEffect.ShowMessage("Request cancelled"))
            assertThat(awaitItem()).isEqualTo(RequestDetailUiEffect.NavigateBack)
        }
    }

    @Test
    fun `OpenChat event emits NavigateChat effect`() = runTest {
        every { observeRequestUseCase(testRequestId) } returns flowOf(sampleRequest)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onEvent(RequestDetailUiEvent.OpenChat)
            val effect = awaitItem()
            assertThat(effect).isEqualTo(RequestDetailUiEffect.NavigateChat(testRequestId))
        }
    }

    @Test
    fun `Back event emits NavigateBack effect`() = runTest {
        every { observeRequestUseCase(testRequestId) } returns flowOf(sampleRequest)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onEvent(RequestDetailUiEvent.Back)
            val effect = awaitItem()
            assertThat(effect).isEqualTo(RequestDetailUiEffect.NavigateBack)
        }
    }
}
