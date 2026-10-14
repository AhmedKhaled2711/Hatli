package com.app.hatli.feature.auth.presentation.approval

import android.util.Log
import app.cash.turbine.test
import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.usecase.auth.LogoutUseCase
import com.app.hatli.core.domain.usecase.user.GetUserProfileUseCase
import com.app.hatli.core.domain.usecase.user.ObserveUserProfileUseCase
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.TransportType
import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.model.UserRole
import com.app.hatli.core.model.VerificationStatus
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
class RunnerApprovalViewModelTest {

    private val observeUserProfileUseCase: ObserveUserProfileUseCase = mockk()
    private val getUserProfileUseCase: GetUserProfileUseCase = mockk()
    private val logoutUseCase: LogoutUseCase = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    private val testPendingProfile = UserProfile(
        uid = "runner_123",
        role = UserRole.Runner,
        phone = "+201012345678",
        firstName = "Mahmoud",
        lastName = "Saad",
        email = "runner@hatli.app",
        gender = Gender.Male,
        createdAt = 1000L,
        updatedAt = 1000L,
        profileCompleted = true,
        nationalId = "29901011234567",
        transportType = TransportType.Motorcycle,
        vehicleInfo = "Honda 150cc",
        verificationStatus = VerificationStatus.Pending,
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)

        every { observeUserProfileUseCase() } returns flowOf(testPendingProfile)
        coEvery { getUserProfileUseCase(any()) } returns Result.Success(testPendingProfile)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): RunnerApprovalViewModel {
        return RunnerApprovalViewModel(
            observeUserProfile = observeUserProfileUseCase,
            getUserProfile = getUserProfileUseCase,
            logoutUseCase = logoutUseCase,
        )
    }

    @Test
    fun `init observes user profile and populates state with Pending status`() = runTest {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.status).isEqualTo(VerificationStatus.Pending)
        assertThat(state.runnerName).isEqualTo("Mahmoud Saad")
        assertThat(state.phone).isEqualTo("+201012345678")
        assertThat(state.transportType).isEqualTo(TransportType.Motorcycle)
        assertThat(state.vehicleInfo).isEqualTo("Honda 150cc")
    }

    @Test
    fun `real-time transition to Approved emits GoHome effect exactly once`() = runTest {
        val profileFlow = MutableSharedFlow<UserProfile?>()
        every { observeUserProfileUseCase() } returns profileFlow

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiEffect.test {
            // Emit Pending
            profileFlow.emit(testPendingProfile)
            testDispatcher.scheduler.advanceUntilIdle()
            expectNoEvents()

            // Emit Approved -> GoHome emitted
            val approvedProfile = testPendingProfile.copy(verificationStatus = VerificationStatus.Approved)
            profileFlow.emit(approvedProfile)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(RunnerApprovalUiEffect.GoHome)

            // Emit Approved again -> should not duplicate GoHome
            profileFlow.emit(approvedProfile)
            testDispatcher.scheduler.advanceUntilIdle()
            expectNoEvents()
        }
    }

    @Test
    fun `Refresh event triggers getUserProfile with forceRefresh=true and updates state`() = runTest {
        val underReviewProfile = testPendingProfile.copy(verificationStatus = VerificationStatus.UnderReview)
        coEvery { getUserProfileUseCase(forceRefresh = true) } returns Result.Success(underReviewProfile)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RunnerApprovalUiEvent.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isRefreshing).isFalse()
        assertThat(state.status).isEqualTo(VerificationStatus.UnderReview)
        assertThat(state.lastCheckedEpochMs).isGreaterThan(0L)

        coVerify(exactly = 1) { getUserProfileUseCase(forceRefresh = true) }
    }

    @Test
    fun `Refresh while in-flight is ignored by concurrency guard`() = runTest {
        coEvery { getUserProfileUseCase(forceRefresh = true) } coAnswers {
            delay(300)
            Result.Success(testPendingProfile)
        }

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RunnerApprovalUiEvent.Refresh)
        viewModel.onEvent(RunnerApprovalUiEvent.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { getUserProfileUseCase(forceRefresh = true) }
    }

    @Test
    fun `Refresh error preserves current status and emits ShowMessage effect`() = runTest {
        coEvery { getUserProfileUseCase(forceRefresh = true) } returns Result.Error(AppError.NoInternet())

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onEvent(RunnerApprovalUiEvent.Refresh)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isInstanceOf(RunnerApprovalUiEffect.ShowMessage::class.java)

            val state = viewModel.uiState.value
            assertThat(state.isRefreshing).isFalse()
            assertThat(state.status).isEqualTo(VerificationStatus.Pending)
            assertThat(state.errorMessage).isNotNull()
        }
    }

    @Test
    fun `Refresh returning Approved status emits GoHome effect`() = runTest {
        val approvedProfile = testPendingProfile.copy(verificationStatus = VerificationStatus.Approved)
        coEvery { getUserProfileUseCase(forceRefresh = true) } returns Result.Success(approvedProfile)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onEvent(RunnerApprovalUiEvent.Refresh)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(RunnerApprovalUiEffect.GoHome)
        }
    }

    @Test
    fun `Logout event triggers logoutUseCase and emits GoAuth effect`() = runTest {
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onEvent(RunnerApprovalUiEvent.Logout)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(RunnerApprovalUiEffect.GoAuth)
        }

        coVerify(exactly = 1) { logoutUseCase() }
    }

    @Test
    fun `Logout while in-flight is guarded against duplicate execution`() = runTest {
        coEvery { logoutUseCase() } coAnswers {
            delay(300)
            Result.Success(Unit)
        }

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onEvent(RunnerApprovalUiEvent.Logout)
            viewModel.onEvent(RunnerApprovalUiEvent.Logout)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(RunnerApprovalUiEffect.GoAuth)
            expectNoEvents()
        }

        coVerify(exactly = 1) { logoutUseCase() }
    }

    @Test
    fun `ClearError event clears error message in state`() = runTest {
        coEvery { getUserProfileUseCase(forceRefresh = true) } returns Result.Error(AppError.NoInternet())

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(RunnerApprovalUiEvent.Refresh)
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.errorMessage).isNotNull()

        viewModel.onEvent(RunnerApprovalUiEvent.ClearError)
        assertThat(viewModel.uiState.value.errorMessage).isNull()
    }
}
