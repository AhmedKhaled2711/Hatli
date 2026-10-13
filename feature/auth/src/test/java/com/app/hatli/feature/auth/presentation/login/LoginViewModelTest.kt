package com.app.hatli.feature.auth.presentation.login

import android.util.Log
import app.cash.turbine.test
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.usecase.auth.SendOtpUseCase
import com.app.hatli.core.model.OtpSession
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
class LoginViewModelTest {

    private val sendOtpUseCase: SendOtpUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(sendOtpUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `PhoneChanged updates state and clears previous error`() = runTest {
        viewModel.onEvent(LoginUiEvent.PhoneChanged("+201012345678"))

        val state = viewModel.uiState.value
        assertThat(state.phone).isEqualTo("+201012345678")
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `Submit success sends GoOtp effect`() = runTest {
        val phone = "01012345678"
        val sanitized = "+201012345678"
        val session = OtpSession("ver_id", sanitized)
        coEvery { sendOtpUseCase(sanitized) } returns Result.Success(session)

        viewModel.onEvent(LoginUiEvent.PhoneChanged(phone))

        viewModel.uiEffect.test {
            viewModel.onEvent(LoginUiEvent.Submit)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(LoginUiEffect.GoOtp("ver_id", sanitized))
        }
    }

    @Test
    fun `Submit while already submitting ignores duplicate concurrent calls`() = runTest {
        val phone = "01012345678"
        val sanitized = "+201012345678"
        val session = OtpSession("ver_id", sanitized)
        coEvery { sendOtpUseCase(sanitized) } coAnswers {
            delay(100)
            Result.Success(session)
        }

        viewModel.onEvent(LoginUiEvent.PhoneChanged(phone))

        viewModel.uiEffect.test {
            viewModel.onEvent(LoginUiEvent.Submit)
            viewModel.onEvent(LoginUiEvent.Submit)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(LoginUiEffect.GoOtp("ver_id", sanitized))
            expectNoEvents()
        }

        coVerify(exactly = 1) { sendOtpUseCase(sanitized) }
    }
}
