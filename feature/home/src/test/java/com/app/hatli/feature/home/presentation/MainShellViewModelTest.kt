package com.app.hatli.feature.home.presentation

import android.util.Log
import app.cash.turbine.test
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.repository.SessionPreferencesRepository
import com.app.hatli.core.domain.usecase.home.ObserveRequesterHomeUseCase
import com.app.hatli.core.domain.usecase.home.ObserveRequesterRequestsUseCase
import com.app.hatli.core.domain.usecase.home.ObserveRunnerHomeUseCase
import com.app.hatli.core.domain.usecase.home.ObserveRunnerJobsUseCase
import com.app.hatli.core.domain.usecase.home.SetRunnerOnlineUseCase
import com.app.hatli.core.domain.usecase.request.AcceptRequestUseCase
import com.app.hatli.core.domain.usecase.request.AdvanceRequestStatusUseCase
import com.app.hatli.core.domain.usecase.request.ConfirmDeliveryUseCase
import com.app.hatli.core.domain.usecase.user.ObserveUserProfileUseCase
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.RequesterHomeSnapshot
import com.app.hatli.core.model.RunnerHomeSnapshot
import com.app.hatli.core.model.RunnerHomeStats
import com.app.hatli.core.model.RunnerPresence
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainShellViewModelTest {

    private val observeUserProfile: ObserveUserProfileUseCase = mockk()
    private val observeRequesterHome: ObserveRequesterHomeUseCase = mockk()
    private val observeRunnerHome: ObserveRunnerHomeUseCase = mockk()
    private val observeRequesterRequests: ObserveRequesterRequestsUseCase = mockk()
    private val observeRunnerJobs: ObserveRunnerJobsUseCase = mockk()
    private val setRunnerOnline: SetRunnerOnlineUseCase = mockk()
    private val acceptRequest: AcceptRequestUseCase = mockk()
    private val advanceStatus: AdvanceRequestStatusUseCase = mockk()
    private val confirmDelivery: ConfirmDeliveryUseCase = mockk()
    private val sessionPreferences: SessionPreferencesRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    private fun createTestUserProfile(
        uid: String = "user_123",
        role: UserRole = UserRole.Requester,
        firstName: String = "Ahmed",
        lastName: String = "Khaled",
    ) = UserProfile(
        uid = uid,
        role = role,
        phone = "+201000000000",
        firstName = firstName,
        lastName = lastName,
        email = "test@example.com",
        gender = Gender.Male,
        createdAt = 1000L,
        updatedAt = 1000L,
        profileCompleted = true,
    )

    private fun createTestRunnerSnapshot(isOnline: Boolean = false) = RunnerHomeSnapshot(
        presence = RunnerPresence(isOnline = isOnline),
        stats = RunnerHomeStats(dailyEarnings = 0.0, todayJobsCount = 0, rating = 5.0),
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)

        every { observeUserProfile() } returns flowOf(null)
        every { observeRequesterHome() } returns flowOf(RequesterHomeSnapshot())
        every { observeRunnerHome() } returns flowOf(createTestRunnerSnapshot())
        every { observeRequesterRequests() } returns flowOf(emptyList())
        every { observeRunnerJobs() } returns flowOf(emptyList())
        every { sessionPreferences.runnerOnlineBannerDismissed } returns flowOf(false)
        every { sessionPreferences.runnerOnlineBannerShownCount } returns flowOf(0)
        coEvery { sessionPreferences.incrementRunnerOnlineBannerCount() } returns Unit
        coEvery { sessionPreferences.dismissRunnerOnlineBanner() } returns Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): MainShellViewModel {
        return MainShellViewModel(
            observeUserProfile = observeUserProfile,
            observeRequesterHome = observeRequesterHome,
            observeRunnerHome = observeRunnerHome,
            observeRequesterRequests = observeRequesterRequests,
            observeRunnerJobs = observeRunnerJobs,
            setRunnerOnline = setRunnerOnline,
            acceptRequest = acceptRequest,
            advanceStatus = advanceStatus,
            confirmDelivery = confirmDelivery,
            sessionPreferences = sessionPreferences,
        )
    }

    @Test
    fun `init with Requester profile populates role and requester snapshot`() = runTest {
        val profile = createTestUserProfile(uid = "user1", role = UserRole.Requester, firstName = "Ahmed")
        val snap = RequesterHomeSnapshot(neighborhood = "المحلة الكبرى")

        every { observeUserProfile() } returns flowOf(profile)
        every { observeRequesterHome() } returns flowOf(snap)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoadingProfile).isFalse()
        assertThat(state.role).isEqualTo(UserRole.Requester)
        assertThat(state.requesterHome?.neighborhood).isEqualTo("المحلة الكبرى")
    }

    @Test
    fun `init with Runner profile populates runner feeds`() = runTest {
        val profile = createTestUserProfile(uid = "runner1", role = UserRole.Runner, firstName = "Ali")
        val snap = createTestRunnerSnapshot(isOnline = true)

        every { observeUserProfile() } returns flowOf(profile)
        every { observeRunnerHome() } returns flowOf(snap)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.role).isEqualTo(UserRole.Runner)
        assertThat(state.runnerHome?.presence?.isOnline).isTrue()
    }

    @Test
    fun `TabSelected updates selectedTab`() = runTest {
        val viewModel = createViewModel()
        viewModel.onEvent(MainShellUiEvent.TabSelected(MainTab.Messages))

        assertThat(viewModel.uiState.value.selectedTab).isEqualTo(MainTab.Messages)
    }

    @Test
    fun `RequestFilterSelected updates requestFilter`() = runTest {
        val viewModel = createViewModel()
        viewModel.onEvent(MainShellUiEvent.RequestFilterSelected(RequestListFilter.Completed))

        assertThat(viewModel.uiState.value.requestFilter).isEqualTo(RequestListFilter.Completed)
    }

    @Test
    fun `ToggleRunnerOnline toggles runner presence online state`() = runTest {
        val profile = createTestUserProfile(uid = "runner1", role = UserRole.Runner)
        val snap = createTestRunnerSnapshot(isOnline = false)
        every { observeUserProfile() } returns flowOf(profile)
        every { observeRunnerHome() } returns flowOf(snap)
        coEvery { setRunnerOnline(true) } returns Unit

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(MainShellUiEvent.ToggleRunnerOnline)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { setRunnerOnline(true) }
    }

    @Test
    fun `AcceptRequest success emits ShowMessage and NavigateDetail effects`() = runTest {
        coEvery { acceptRequest("req_100") } returns Result.Success(Unit)

        val viewModel = createViewModel()

        viewModel.uiEffect.test {
            viewModel.onEvent(MainShellUiEvent.AcceptRequest("req_100"))
            testDispatcher.scheduler.advanceUntilIdle()

            val msgEffect = awaitItem()
            assertThat(msgEffect).isInstanceOf(MainShellUiEffect.ShowMessage::class.java)

            val navEffect = awaitItem()
            assertThat(navEffect).isEqualTo(MainShellUiEffect.NavigateDetail("req_100"))
        }
    }

    @Test
    fun `runner online shows banner when count less than 6 and not dismissed`() = runTest {
        val profile = createTestUserProfile(uid = "runner1", role = UserRole.Runner)
        val snap = createTestRunnerSnapshot(isOnline = true)
        every { observeUserProfile() } returns flowOf(profile)
        every { observeRunnerHome() } returns flowOf(snap)
        every { sessionPreferences.runnerOnlineBannerDismissed } returns flowOf(false)
        every { sessionPreferences.runnerOnlineBannerShownCount } returns flowOf(2)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.showRunnerOnlineInfoBanner).isTrue()
        coVerify(exactly = 1) { sessionPreferences.incrementRunnerOnlineBannerCount() }
    }

    @Test
    fun `runner offline does not show online banner`() = runTest {
        val profile = createTestUserProfile(uid = "runner1", role = UserRole.Runner)
        val snap = createTestRunnerSnapshot(isOnline = false)
        every { observeUserProfile() } returns flowOf(profile)
        every { observeRunnerHome() } returns flowOf(snap)
        every { sessionPreferences.runnerOnlineBannerDismissed } returns flowOf(false)
        every { sessionPreferences.runnerOnlineBannerShownCount } returns flowOf(2)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.showRunnerOnlineInfoBanner).isFalse()
        coVerify(exactly = 0) { sessionPreferences.incrementRunnerOnlineBannerCount() }
    }

    @Test
    fun `runner online banner dismissed does not show banner`() = runTest {
        val profile = createTestUserProfile(uid = "runner1", role = UserRole.Runner)
        val snap = createTestRunnerSnapshot(isOnline = true)
        every { observeUserProfile() } returns flowOf(profile)
        every { observeRunnerHome() } returns flowOf(snap)
        every { sessionPreferences.runnerOnlineBannerDismissed } returns flowOf(true)
        every { sessionPreferences.runnerOnlineBannerShownCount } returns flowOf(1)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.showRunnerOnlineInfoBanner).isFalse()
    }

    @Test
    fun `runner online banner with count 6 or more does not show banner`() = runTest {
        val profile = createTestUserProfile(uid = "runner1", role = UserRole.Runner)
        val snap = createTestRunnerSnapshot(isOnline = true)
        every { observeUserProfile() } returns flowOf(profile)
        every { observeRunnerHome() } returns flowOf(snap)
        every { sessionPreferences.runnerOnlineBannerDismissed } returns flowOf(false)
        every { sessionPreferences.runnerOnlineBannerShownCount } returns flowOf(6)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.showRunnerOnlineInfoBanner).isFalse()
    }

    @Test
    fun `DismissRunnerOnlineInfoBanner calls session preferences dismiss and updates state`() = runTest {
        val profile = createTestUserProfile(uid = "runner1", role = UserRole.Runner)
        val snap = createTestRunnerSnapshot(isOnline = true)
        every { observeUserProfile() } returns flowOf(profile)
        every { observeRunnerHome() } returns flowOf(snap)
        every { sessionPreferences.runnerOnlineBannerDismissed } returns flowOf(false)
        every { sessionPreferences.runnerOnlineBannerShownCount } returns flowOf(1)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.uiState.value.showRunnerOnlineInfoBanner).isTrue()

        viewModel.onEvent(MainShellUiEvent.DismissRunnerOnlineInfoBanner)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { sessionPreferences.dismissRunnerOnlineBanner() }
        assertThat(viewModel.uiState.value.showRunnerOnlineInfoBanner).isFalse()
    }
}
