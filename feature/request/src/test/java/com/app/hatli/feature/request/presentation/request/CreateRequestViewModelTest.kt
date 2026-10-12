package com.app.hatli.feature.request.presentation.request

import app.cash.turbine.test
import com.app.hatli.core.common.result.Result
import com.app.hatli.core.domain.usecase.request.CreateRequestUseCase
import com.app.hatli.core.model.RequestCategory
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
class CreateRequestViewModelTest {

    private val createRequestUseCase: CreateRequestUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CreateRequestViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CreateRequestViewModel(createRequestUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selecting category advances wizard step to Details`() = runTest {
        viewModel.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Shopping))

        val state = viewModel.uiState.value
        assertThat(state.category).isEqualTo(RequestCategory.Shopping)
        assertThat(state.step).isEqualTo(CreateWizardStep.Details)
        assertThat(state.fieldError).isNull()
    }

    @Test
    fun `next on Details step with blank shopping list sets validation error`() = runTest {
        viewModel.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Shopping))
        viewModel.onEvent(CreateRequestUiEvent.Next)

        val state = viewModel.uiState.value
        assertThat(state.step).isEqualTo(CreateWizardStep.Details)
        assertThat(state.fieldError).isNotNull()
        assertThat(state.fieldError).contains("shopping list")
    }

    @Test
    fun `next on Details step with valid shopping list advances to FeeReview`() = runTest {
        viewModel.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Shopping))
        viewModel.onEvent(CreateRequestUiEvent.ItemsChanged("1x Milk, 2x Bread"))
        viewModel.onEvent(CreateRequestUiEvent.Next)

        val state = viewModel.uiState.value
        assertThat(state.step).isEqualTo(CreateWizardStep.FeeReview)
        assertThat(state.fieldError).isNull()
    }

    @Test
    fun `successful submit emits Submitted effect`() = runTest {
        coEvery { createRequestUseCase(any()) } returns Result.Success("req_789")

        viewModel.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Shopping))
        viewModel.onEvent(CreateRequestUiEvent.ItemsChanged("Grocery list"))
        viewModel.onEvent(CreateRequestUiEvent.Next)

        viewModel.uiEffect.test {
            viewModel.onEvent(CreateRequestUiEvent.Submit)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(CreateRequestUiEffect.Submitted)
        }
    }
}
