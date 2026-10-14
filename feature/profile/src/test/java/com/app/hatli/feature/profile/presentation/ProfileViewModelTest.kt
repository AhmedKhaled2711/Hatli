package com.app.hatli.feature.profile.presentation

import app.cash.turbine.test
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.repository.SessionPreferencesRepository
import com.app.hatli.core.domain.usecase.auth.LogoutUseCase
import com.app.hatli.core.domain.usecase.request.ObserveRunnerAssignedRequestsUseCase
import com.app.hatli.core.domain.usecase.user.GetUserProfileUseCase
import com.app.hatli.core.domain.usecase.user.ObserveUserProfileUseCase
import com.app.hatli.core.model.AppThemeMode
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val observeUserProfile: ObserveUserProfileUseCase = mockk()
    private val getUserProfile: GetUserProfileUseCase = mockk()
    private val observeRunnerAssigned: ObserveRunnerAssignedRequestsUseCase = mockk()
    private val logout: LogoutUseCase = mockk()
    private val sessionPreferences: SessionPreferencesRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    private fun createTestProfile(
        role: UserRole = UserRole.Requester,
        verificationStatus: VerificationStatus? = null,
        transportType: TransportType? = null
    ): UserProfile = UserProfile(
        uid = "test_user_id",
        role = role,
        phone = "+201012345678",
        firstName = "Ahmed",
        lastName = "Ali",
        email = "ahmed@example.com",
        gender = Gender.Male,
        createdAt = 1000L,
        updatedAt = 2000L,
        profileCompleted = true,
        verificationStatus = verificationStatus,
        transportType = transportType
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { sessionPreferences.themeMode } returns flowOf(AppThemeMode.FollowSystem)
        every { sessionPreferences.appLanguage } returns flowOf("ar")
        every { observeRunnerAssigned(any()) } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initialization observes profile and triggers refresh successfully`() = runTest {
        val testProfile = createTestProfile()
        every { observeUserProfile() } returns flowOf(testProfile)
        coEvery { getUserProfile(any()) } returns Result.Success(testProfile)

        val viewModel = ProfileViewModel(
            observeUserProfile = observeUserProfile,
            getUserProfile = getUserProfile,
            observeRunnerAssigned = observeRunnerAssigned,
            logout = logout,
            sessionPreferences = sessionPreferences
        )

        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.profile).isEqualTo(testProfile)
            assertThat(state.errorMessage).isNull()
        }
    }

    @Test
    fun `refresh failure with no prior profile sets error message`() = runTest {
        val profileFlow = MutableSharedFlow<UserProfile?>()
        every { observeUserProfile() } returns profileFlow
        coEvery { getUserProfile(any()) } returns Result.Error(Exception("Network failure"))

        val viewModel = ProfileViewModel(
            observeUserProfile = observeUserProfile,
            getUserProfile = getUserProfile,
            observeRunnerAssigned = observeRunnerAssigned,
            logout = logout,
            sessionPreferences = sessionPreferences
        )

        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.profile).isNull()
            assertThat(state.errorMessage).isNotNull()
        }
    }

    @Test
    fun `logout triggers logoutUseCase and emits LoggedOut effect`() = runTest {
        val testProfile = createTestProfile()
        every { observeUserProfile() } returns flowOf(testProfile)
        coEvery { getUserProfile(any()) } returns Result.Success(testProfile)
        coEvery { logout() } returns Result.Success(Unit)

        val viewModel = ProfileViewModel(
            observeUserProfile = observeUserProfile,
            getUserProfile = getUserProfile,
            observeRunnerAssigned = observeRunnerAssigned,
            logout = logout,
            sessionPreferences = sessionPreferences
        )

        advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onEvent(ProfileUiEvent.Logout)
            advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(ProfileUiEffect.LoggedOut)
            coVerify(exactly = 1) { logout() }
        }
    }

    @Test
    fun `runner profile with approved status preserves verification status correctly`() = runTest {
        val runnerProfile = createTestProfile(
            role = UserRole.Runner,
            verificationStatus = VerificationStatus.Approved,
            transportType = TransportType.Motorcycle
        )
        every { observeUserProfile() } returns flowOf(runnerProfile)
        coEvery { getUserProfile(any()) } returns Result.Success(runnerProfile)

        val viewModel = ProfileViewModel(
            observeUserProfile = observeUserProfile,
            getUserProfile = getUserProfile,
            observeRunnerAssigned = observeRunnerAssigned,
            logout = logout,
            sessionPreferences = sessionPreferences
        )

        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.profile?.role).isEqualTo(UserRole.Runner)
            assertThat(state.profile?.verificationStatus).isEqualTo(VerificationStatus.Approved)
            assertThat(state.profile?.transportType).isEqualTo(TransportType.Motorcycle)
        }
    }

    @Test
    fun `setThemeMode updates themeMode in sessionPreferences and state`() = runTest {
        val testProfile = createTestProfile()
        every { observeUserProfile() } returns flowOf(testProfile)
        coEvery { getUserProfile(any()) } returns Result.Success(testProfile)

        val viewModel = ProfileViewModel(
            observeUserProfile = observeUserProfile,
            getUserProfile = getUserProfile,
            observeRunnerAssigned = observeRunnerAssigned,
            logout = logout,
            sessionPreferences = sessionPreferences
        )

        advanceUntilIdle()

        viewModel.onEvent(ProfileUiEvent.SetThemeMode(AppThemeMode.Dark))
        advanceUntilIdle()

        coVerify(exactly = 1) { sessionPreferences.setThemeMode(AppThemeMode.Dark) }
        assertThat(viewModel.uiState.value.themeMode).isEqualTo(AppThemeMode.Dark)
    }

    @Test
    fun `setAppLanguage updates appLanguage and emits AppLanguageChanged effect`() = runTest {
        val testProfile = createTestProfile()
        every { observeUserProfile() } returns flowOf(testProfile)
        coEvery { getUserProfile(any()) } returns Result.Success(testProfile)

        val viewModel = ProfileViewModel(
            observeUserProfile = observeUserProfile,
            getUserProfile = getUserProfile,
            observeRunnerAssigned = observeRunnerAssigned,
            logout = logout,
            sessionPreferences = sessionPreferences
        )

        advanceUntilIdle()

        viewModel.uiEffect.test {
            viewModel.onEvent(ProfileUiEvent.SetAppLanguage("en"))
            advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(ProfileUiEffect.AppLanguageChanged("en"))
            coVerify(exactly = 1) { sessionPreferences.setAppLanguage("en") }
            assertThat(viewModel.uiState.value.appLanguage).isEqualTo("en")
        }
    }

    @Test
    fun `runner profile fetches live assigned requests and computes orders count and average rating`() = runTest {
        val runnerProfile = createTestProfile(
            role = UserRole.Runner,
            verificationStatus = VerificationStatus.Approved,
            transportType = TransportType.Bicycle
        )
        val mockCompletedRequest1 = mockk<com.app.hatli.core.model.Request> {
            every { status } returns com.app.hatli.core.model.RequestStatus.Completed
            every { rating } returns com.app.hatli.core.model.RequestRating(stars = 5, comment = null)
        }
        val mockCompletedRequest2 = mockk<com.app.hatli.core.model.Request> {
            every { status } returns com.app.hatli.core.model.RequestStatus.Completed
            every { rating } returns com.app.hatli.core.model.RequestRating(stars = 4, comment = null)
        }

        every { observeUserProfile() } returns flowOf(runnerProfile)
        coEvery { getUserProfile(any()) } returns Result.Success(runnerProfile)
        every { observeRunnerAssigned(runnerProfile.uid) } returns flowOf(listOf(mockCompletedRequest1, mockCompletedRequest2))

        val viewModel = ProfileViewModel(
            observeUserProfile = observeUserProfile,
            getUserProfile = getUserProfile,
            observeRunnerAssigned = observeRunnerAssigned,
            logout = logout,
            sessionPreferences = sessionPreferences
        )

        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.profile?.role).isEqualTo(UserRole.Runner)
            assertThat(state.runnerOrdersCount).isEqualTo(2)
            assertThat(state.runnerRating).isEqualTo(4.5)
            assertThat(state.runnerAcceptanceRate).isEqualTo(100)
        }
    }
}
