package com.app.hatli.feature.auth.presentation.login

import app.cash.turbine.test
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.usecase.auth.SendOtpUseCase
import com.app.hatli.core.model.OtpSession
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        val phone = "+201012345678"
        val session = OtpSession("ver_id", phone)
        coEvery { sendOtpUseCase(phone) } returns Result.Success(session)

        viewModel.onEvent(LoginUiEvent.PhoneChanged(phone))

        viewModel.uiEffect.test {
            viewModel.onEvent(LoginUiEvent.Submit)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(LoginUiEffect.GoOtp("ver_id", phone))
        }
    }
}
