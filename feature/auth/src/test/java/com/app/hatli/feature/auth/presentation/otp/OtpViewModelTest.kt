package com.app.hatli.feature.auth.presentation.otp

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.usecase.auth.ResendOtpUseCase
import com.app.hatli.core.domain.usecase.auth.VerifyOtpUseCase
import com.app.hatli.core.domain.usecase.user.GetPendingRoleUseCase
import com.app.hatli.core.domain.usecase.user.GetUserProfileUseCase
import com.app.hatli.core.domain.usecase.user.SetPendingRoleUseCase
import com.app.hatli.core.model.Gender
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
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OtpViewModelTest {

    private val verifyOtpUseCase: VerifyOtpUseCase = mockk()
    private val resendOtpUseCase: ResendOtpUseCase = mockk()
    private val getUserProfileUseCase: GetUserProfileUseCase = mockk()
    private val getPendingRoleUseCase: GetPendingRoleUseCase = mockk()
    private val setPendingRoleUseCase: SetPendingRoleUseCase = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    private val testProfile = UserProfile(
        uid = "uid_123",
        role = UserRole.Requester,
        phone = "+201012345678",
        firstName = "Test",
        lastName = "User",
        email = "test@example.com",
        gender = Gender.Male,
        createdAt = 1000L,
        updatedAt = 1000L,
        profileCompleted = true,
    )

    private fun createViewModel(
        verificationId: String = "test_ver_id",
        phone: String = "+201012345678",
    ): OtpViewModel {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "verificationId" to verificationId,
                "phone" to phone,
            ),
        )
        return OtpViewModel(
            savedStateHandle = savedStateHandle,
            verifyOtp = verifyOtpUseCase,
            resendOtp = resendOtpUseCase,
            getUserProfile = getUserProfileUseCase,
            getPendingRole = getPendingRoleUseCase,
            setPendingRole = setPendingRoleUseCase,
        )
    }

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Entering 6th digit automatically triggers verify exactly once`() = runTest {
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } returns Result.Success(Unit)
        coEvery { getUserProfileUseCase() } returns Result.Success(testProfile)

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(OtpUiEffect.GoHome)
        }

        coVerify(exactly = 1) { verifyOtpUseCase("test_ver_id", "123456") }
    }

    @Test
    fun `Concurrent verify invocations while in-flight are ignored by re-entrancy guard`() = runTest {
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } coAnswers {
            delay(200)
            Result.Success(Unit)
        }
        coEvery { getUserProfileUseCase() } returns Result.Success(testProfile)

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
            viewModel.onEvent(OtpUiEvent.Verify)
            viewModel.onEvent(OtpUiEvent.Verify)

            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(OtpUiEffect.GoHome)
            expectNoEvents()
        }

        coVerify(exactly = 1) { verifyOtpUseCase("test_ver_id", "123456") }
    }

    @Test
    fun `routeAfterLogin sends GoCreateProfile and sets pending role when profile is incomplete`() = runTest {
        val incompleteProfile = testProfile.copy(profileCompleted = false, role = UserRole.Runner)
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } returns Result.Success(Unit)
        coEvery { getUserProfileUseCase() } returns Result.Success(incompleteProfile)

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(OtpUiEffect.GoCreateProfile)
        }

        coVerify(exactly = 1) { setPendingRoleUseCase(UserRole.Runner) }
    }

    @Test
    fun `routeAfterLogin sends GoRunnerApproval when runner profile is completed but verificationStatus is Pending`() = runTest {
        val runnerProfile = testProfile.copy(
            role = UserRole.Runner,
            verificationStatus = VerificationStatus.Pending,
        )
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } returns Result.Success(Unit)
        coEvery { getUserProfileUseCase() } returns Result.Success(runnerProfile)

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(OtpUiEffect.GoRunnerApproval)
        }
    }

    @Test
    fun `routeAfterLogin sends GoRunnerApproval when runner profile is completed but verificationStatus is UnderReview`() = runTest {
        val runnerProfile = testProfile.copy(
            role = UserRole.Runner,
            verificationStatus = VerificationStatus.UnderReview,
        )
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } returns Result.Success(Unit)
        coEvery { getUserProfileUseCase() } returns Result.Success(runnerProfile)

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(OtpUiEffect.GoRunnerApproval)
        }
    }

    @Test
    fun `routeAfterLogin sends GoRunnerApproval when runner profile is completed but verificationStatus is Rejected`() = runTest {
        val runnerProfile = testProfile.copy(
            role = UserRole.Runner,
            verificationStatus = VerificationStatus.Rejected,
        )
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } returns Result.Success(Unit)
        coEvery { getUserProfileUseCase() } returns Result.Success(runnerProfile)

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(OtpUiEffect.GoRunnerApproval)
        }
    }

    @Test
    fun `routeAfterLogin sends GoHome when runner profile is completed and verificationStatus is Approved`() = runTest {
        val runnerProfile = testProfile.copy(
            role = UserRole.Runner,
            verificationStatus = VerificationStatus.Approved,
        )
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } returns Result.Success(Unit)
        coEvery { getUserProfileUseCase() } returns Result.Success(runnerProfile)

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(OtpUiEffect.GoHome)
        }
    }

    @Test
    fun `routeAfterLogin sends GoCreateProfile when profile is null but pendingRole exists`() = runTest {
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } returns Result.Success(Unit)
        coEvery { getUserProfileUseCase() } returns Result.Success(null)
        coEvery { getPendingRoleUseCase() } returns UserRole.Requester

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(OtpUiEffect.GoCreateProfile)
        }
    }

    @Test
    fun `routeAfterLogin sends GoRoleSelect when profile is null and pendingRole is null`() = runTest {
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } returns Result.Success(Unit)
        coEvery { getUserProfileUseCase() } returns Result.Success(null)
        coEvery { getPendingRoleUseCase() } returns null

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(OtpUiEffect.GoRoleSelect)
        }
    }

    @Test
    fun `Failed verification resets isLoading to false and populates errorMessage`() = runTest {
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } returns Result.Error(AppError.InvalidOtp())

        val viewModel = createViewModel()

        viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNotNull()
    }

    @Test
    fun `Resend while verification is in-flight is ignored`() = runTest {
        coEvery { verifyOtpUseCase("test_ver_id", "123456") } coAnswers {
            delay(500)
            Result.Success(Unit)
        }

        val viewModel = createViewModel()

        viewModel.onEvent(OtpUiEvent.CodeChanged("123456"))
        viewModel.onEvent(OtpUiEvent.Resend)

        coVerify(exactly = 0) { resendOtpUseCase(any()) }
    }
}
