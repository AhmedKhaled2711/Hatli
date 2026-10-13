package com.app.hatli.feature.auth.presentation.profile

import android.util.Log
import app.cash.turbine.test
import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.usecase.auth.GetCurrentAuthUserUseCase
import com.app.hatli.core.domain.usecase.user.CreateUserProfileUseCase
import com.app.hatli.core.domain.usecase.user.GetPendingRoleUseCase
import com.app.hatli.core.model.AuthUser
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.model.UserRole
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
class CreateProfileViewModelTest {

    private val getPendingRole: GetPendingRoleUseCase = mockk()
    private val getCurrentAuthUser: GetCurrentAuthUserUseCase = mockk()
    private val createUserProfile: CreateUserProfileUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    private val testAuthUser = AuthUser(
        uid = "uid_123",
        phoneNumber = "+201012345678",
    )

    private val testCreatedProfile = UserProfile(
        uid = "uid_123",
        role = UserRole.Requester,
        phone = "+201012345678",
        firstName = "Ahmed",
        lastName = "Ali",
        email = "ahmed@example.com",
        gender = Gender.Male,
        createdAt = 1000L,
        updatedAt = 1000L,
        profileCompleted = true,
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)

        coEvery { getPendingRole() } returns UserRole.Requester
        every { getCurrentAuthUser() } returns testAuthUser
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun populateValidRequesterFields(viewModel: CreateProfileViewModel) {
        viewModel.onEvent(CreateProfileUiEvent.FirstName("Ahmed"))
        viewModel.onEvent(CreateProfileUiEvent.LastName("Ali"))
        viewModel.onEvent(CreateProfileUiEvent.Email("ahmed@example.com"))
        viewModel.onEvent(CreateProfileUiEvent.GenderSelected(Gender.Male))
        // Move to Address step
        viewModel.onEvent(CreateProfileUiEvent.Next)
        viewModel.onEvent(CreateProfileUiEvent.City("Cairo"))
        viewModel.onEvent(CreateProfileUiEvent.Area("Maadi"))
        viewModel.onEvent(CreateProfileUiEvent.Street("Street 9"))
        viewModel.onEvent(CreateProfileUiEvent.Building("12"))
    }

    @Test
    fun `Submit with valid inputs triggers createUserProfile and sends GoHome`() = runTest {
        coEvery { createUserProfile(any()) } returns Result.Success(testCreatedProfile)

        val viewModel = CreateProfileViewModel(getPendingRole, getCurrentAuthUser, createUserProfile)
        testDispatcher.scheduler.advanceUntilIdle()

        populateValidRequesterFields(viewModel)

        viewModel.uiEffect.test {
            viewModel.onEvent(CreateProfileUiEvent.Submit)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(CreateProfileUiEffect.GoHome)
        }

        coVerify(exactly = 1) { createUserProfile(any()) }
    }

    @Test
    fun `Submit while submitJob is active ignores duplicate concurrent calls`() = runTest {
        coEvery { createUserProfile(any()) } coAnswers {
            delay(200)
            Result.Success(testCreatedProfile)
        }

        val viewModel = CreateProfileViewModel(getPendingRole, getCurrentAuthUser, createUserProfile)
        testDispatcher.scheduler.advanceUntilIdle()

        populateValidRequesterFields(viewModel)

        viewModel.uiEffect.test {
            viewModel.onEvent(CreateProfileUiEvent.Submit)
            viewModel.onEvent(CreateProfileUiEvent.Submit)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(CreateProfileUiEffect.GoHome)
            expectNoEvents()
        }

        coVerify(exactly = 1) { createUserProfile(any()) }
    }

    @Test
    fun `Submit failure resets isLoading to false and populates errorMessage`() = runTest {
        coEvery { createUserProfile(any()) } returns Result.Error(AppError.NoInternet())

        val viewModel = CreateProfileViewModel(getPendingRole, getCurrentAuthUser, createUserProfile)
        testDispatcher.scheduler.advanceUntilIdle()

        populateValidRequesterFields(viewModel)

        viewModel.onEvent(CreateProfileUiEvent.Submit)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNotNull()
    }
}
