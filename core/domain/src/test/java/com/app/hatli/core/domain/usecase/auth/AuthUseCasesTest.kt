package com.app.hatli.core.domain.usecase.auth

import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.repository.AuthRepository
import com.app.hatli.core.domain.repository.SessionPreferencesRepository
import com.app.hatli.core.domain.repository.UserRepository
import com.app.hatli.core.model.OtpSession
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthUseCasesTest {

    private val authRepository: AuthRepository = mockk()
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val sessionPreferencesRepository: SessionPreferencesRepository = mockk(relaxed = true)

    private lateinit var sendOtpUseCase: SendOtpUseCase
    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun setUp() {
        sendOtpUseCase = SendOtpUseCase(authRepository)
        val clearLocalUserDataUseCase = ClearLocalUserDataUseCase(userRepository, sessionPreferencesRepository)
        logoutUseCase = LogoutUseCase(authRepository, clearLocalUserDataUseCase)
    }

    @Test
    fun `SendOtpUseCase trims input and delegates to AuthRepository`() = runTest {
        val rawPhone = "  +201012345678  "
        val expectedSession = OtpSession("ver_id", "+201012345678")
        coEvery { authRepository.sendOtp("+201012345678") } returns Result.Success(expectedSession)

        val result = sendOtpUseCase(rawPhone)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedSession)
        coVerify(exactly = 1) { authRepository.sendOtp("+201012345678") }
    }

    @Test
    fun `LogoutUseCase clears local user cache before triggering auth logout`() = runTest {
        coEvery { authRepository.logout() } returns Result.Success(Unit)

        val result = logoutUseCase()

        assertThat(result).isInstanceOf(Result.Success::class.java)
        coVerify(ordering = io.mockk.Ordering.ORDERED) {
            userRepository.clearLocalCache()
            sessionPreferencesRepository.clearSessionPreferences()
            authRepository.logout()
        }
    }
}
