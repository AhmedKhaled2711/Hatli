package com.app.hatli.core.data.repository

import com.app.hatli.core.common.error.AppError
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.data.auth.FirebaseAuthDataSource
import com.app.hatli.core.model.OtpSession
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private val authDataSource: FirebaseAuthDataSource = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var authRepository: AuthRepositoryImpl

    @Before
    fun setUp() {
        every { authDataSource.authState } returns emptyFlow()
        authRepository = AuthRepositoryImpl(
            authDataSource = authDataSource,
            ioDispatcher = testDispatcher,
        )
    }

    @Test
    fun `sendOtp with invalid phone number returns InvalidInput error`() = runTest {
        val invalidPhone = "12345"

        val result = authRepository.sendOtp(invalidPhone)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = (result as Result.Error).exception
        assertThat(error).isInstanceOf(AppError.InvalidInput::class.java)
    }

    @Test
    fun `sendOtp with valid phone number calls authDataSource`() = runTest {
        val validPhone = " +201012345678 "
        val expectedSession = OtpSession("ver_id", "+201012345678")
        coEvery { authDataSource.sendOtp("+201012345678", resend = false) } returns expectedSession

        val result = authRepository.sendOtp(validPhone)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(expectedSession)
    }

    @Test
    fun `verifyOtp with short code returns InvalidOtp error`() = runTest {
        val shortCode = "123"

        val result = authRepository.verifyOtp("ver_id", shortCode)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        val error = (result as Result.Error).exception
        assertThat(error).isInstanceOf(AppError.InvalidOtp::class.java)
    }
}
