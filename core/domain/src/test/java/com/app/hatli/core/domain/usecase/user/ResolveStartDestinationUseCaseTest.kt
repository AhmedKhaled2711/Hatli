package com.app.hatli.core.domain.usecase.user

import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.repository.SessionPreferencesRepository
import com.app.hatli.core.domain.repository.UserRepository
import com.app.hatli.core.model.AuthUser
import com.app.hatli.core.model.Gender
import com.app.hatli.core.model.UserProfile
import com.app.hatli.core.model.UserRole
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ResolveStartDestinationUseCaseTest {

    private val authRepository: AuthRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val sessionPreferences: SessionPreferencesRepository = mockk(relaxed = true)

    private lateinit var useCase: ResolveStartDestinationUseCase

    private val testAuthUser = AuthUser(
        uid = "user_123",
        phoneNumber = "+201012345678",
    )

    private val completedProfile = UserProfile(
        uid = "user_123",
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

    private val incompleteProfile = completedProfile.copy(
        profileCompleted = false,
        role = UserRole.Runner,
    )

    @Before
    fun setUp() {
        useCase = ResolveStartDestinationUseCase(
            authRepository = authRepository,
            userRepository = userRepository,
            sessionPreferences = sessionPreferences,
        )
    }

    @Test
    fun `returns Onboarding when onboarding is not completed`() = runTest {
        every { sessionPreferences.onboardingCompleted } returns flowOf(false)

        val destination = useCase()

        assertThat(destination).isEqualTo(ResolveStartDestinationUseCase.StartDestination.Onboarding)
    }

    @Test
    fun `returns Start when user is not authenticated`() = runTest {
        every { sessionPreferences.onboardingCompleted } returns flowOf(true)
        every { authRepository.currentUser() } returns null

        val destination = useCase()

        assertThat(destination).isEqualTo(ResolveStartDestinationUseCase.StartDestination.Start)
    }

    @Test
    fun `returns Home when authenticated user has completed profile`() = runTest {
        every { sessionPreferences.onboardingCompleted } returns flowOf(true)
        every { authRepository.currentUser() } returns testAuthUser
        every { sessionPreferences.pendingRole } returns flowOf(null)
        coEvery { userRepository.getProfile("user_123") } returns Result.Success(completedProfile)

        val destination = useCase()

        assertThat(destination).isEqualTo(ResolveStartDestinationUseCase.StartDestination.Home)
    }

    @Test
    fun `returns CreateProfile and preserves role when profile exists but incomplete`() = runTest {
        every { sessionPreferences.onboardingCompleted } returns flowOf(true)
        every { authRepository.currentUser() } returns testAuthUser
        every { sessionPreferences.pendingRole } returns flowOf(null)
        coEvery { userRepository.getProfile("user_123") } returns Result.Success(incompleteProfile)

        val destination = useCase()

        assertThat(destination).isEqualTo(ResolveStartDestinationUseCase.StartDestination.CreateProfile)
        coVerify(exactly = 1) { sessionPreferences.setPendingRole(UserRole.Runner) }
    }

    @Test
    fun `returns CreateProfile when profile is null but pendingRole exists`() = runTest {
        every { sessionPreferences.onboardingCompleted } returns flowOf(true)
        every { authRepository.currentUser() } returns testAuthUser
        every { sessionPreferences.pendingRole } returns flowOf(UserRole.Requester)
        coEvery { userRepository.getProfile("user_123") } returns Result.Success(null)

        val destination = useCase()

        assertThat(destination).isEqualTo(ResolveStartDestinationUseCase.StartDestination.CreateProfile)
    }

    @Test
    fun `returns RoleSelect when profile is null and pendingRole is null`() = runTest {
        every { sessionPreferences.onboardingCompleted } returns flowOf(true)
        every { authRepository.currentUser() } returns testAuthUser
        every { sessionPreferences.pendingRole } returns flowOf(null)
        coEvery { userRepository.getProfile("user_123") } returns Result.Success(null)

        val destination = useCase()

        assertThat(destination).isEqualTo(ResolveStartDestinationUseCase.StartDestination.RoleSelect)
    }

    @Test
    fun `returns RoleSelect when getProfile errors and pendingRole is null`() = runTest {
        every { sessionPreferences.onboardingCompleted } returns flowOf(true)
        every { authRepository.currentUser() } returns testAuthUser
        every { sessionPreferences.pendingRole } returns flowOf(null)
        coEvery { userRepository.getProfile("user_123") } returns Result.Error(AppError.NoInternet())

        val destination = useCase()

        assertThat(destination).isEqualTo(ResolveStartDestinationUseCase.StartDestination.RoleSelect)
    }

    @Test
    fun `returns CreateProfile when getProfile errors but pendingRole exists`() = runTest {
        every { sessionPreferences.onboardingCompleted } returns flowOf(true)
        every { authRepository.currentUser() } returns testAuthUser
        every { sessionPreferences.pendingRole } returns flowOf(UserRole.Runner)
        coEvery { userRepository.getProfile("user_123") } returns Result.Error(AppError.NoInternet())

        val destination = useCase()

        assertThat(destination).isEqualTo(ResolveStartDestinationUseCase.StartDestination.CreateProfile)
    }
}
