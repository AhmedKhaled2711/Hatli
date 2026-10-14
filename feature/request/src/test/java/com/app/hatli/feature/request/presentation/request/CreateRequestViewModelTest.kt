package com.app.hatli.feature.request.presentation.request

import androidx.lifecycle.SavedStateHandle
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

import com.app.hatli.core.domain.usecase.user.GetUserProfileUseCase
import com.app.hatli.core.model.UserAddress
import com.app.hatli.core.model.UserProfile

@OptIn(ExperimentalCoroutinesApi::class)
class CreateRequestViewModelTest {

    private val createRequestUseCase: CreateRequestUseCase = mockk()
    private val getUserProfileUseCase: GetUserProfileUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: CreateRequestViewModel

    private fun createViewModel(
        savedStateHandle: SavedStateHandle = SavedStateHandle(),
        useCase: CreateRequestUseCase = createRequestUseCase,
        profileUseCase: GetUserProfileUseCase = getUserProfileUseCase,
    ) = CreateRequestViewModel(savedStateHandle, useCase, profileUseCase)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getUserProfileUseCase() } returns Result.Success(null)
        viewModel = createViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `direct initial category grocery skips category step directly to Details`() = runTest {
        val directVm = createViewModel(SavedStateHandle(mapOf("initialCategory" to "grocery")))
        val state = directVm.uiState.value
        assertThat(state.category).isEqualTo(RequestCategory.Shopping)
        assertThat(state.categorySubtype).isEqualTo("grocery")
        assertThat(state.step).isEqualTo(CreateWizardStep.Details)
        assertThat(state.wasDirectCategory).isTrue()
    }

    @Test
    fun `direct initial category pharmacy skips category step directly to Details`() = runTest {
        val directVm = createViewModel(SavedStateHandle(mapOf("initialCategory" to "pharmacy")))
        val state = directVm.uiState.value
        assertThat(state.category).isEqualTo(RequestCategory.Shopping)
        assertThat(state.categorySubtype).isEqualTo("pharmacy")
        assertThat(state.step).isEqualTo(CreateWizardStep.Details)
        assertThat(state.wasDirectCategory).isTrue()
    }

    @Test
    fun `back on direct category Details step navigates back out`() = runTest {
        val directVm = createViewModel(SavedStateHandle(mapOf("initialCategory" to "courier")))
        directVm.uiEffect.test {
            directVm.onEvent(CreateRequestUiEvent.Back)
            val effect = awaitItem()
            assertThat(effect).isEqualTo(CreateRequestUiEffect.NavigateBack)
        }
    }

    @Test
    fun `ChangeCategory switches step to Category and allows selecting different category`() = runTest {
        val directVm = createViewModel(SavedStateHandle(mapOf("initialCategory" to "pharmacy")))
        directVm.onEvent(CreateRequestUiEvent.ChangeCategory)
        assertThat(directVm.uiState.value.step).isEqualTo(CreateWizardStep.Category)
        assertThat(directVm.uiState.value.wasDirectCategory).isFalse()
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

    @Test
    fun `next on Pickup step with blank pickup address sets validation error`() = runTest {
        viewModel.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Pickup))
        viewModel.onEvent(CreateRequestUiEvent.DropoffAddressChanged("Home Address"))
        viewModel.onEvent(CreateRequestUiEvent.PickupItemChanged("Keys"))
        viewModel.onEvent(CreateRequestUiEvent.Next)

        val state = viewModel.uiState.value
        assertThat(state.step).isEqualTo(CreateWizardStep.Details)
        assertThat(state.fieldError).isEqualTo("Enter pickup address.")
    }

    @Test
    fun `next on Pickup step with blank dropoff address sets validation error`() = runTest {
        viewModel.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Pickup))
        viewModel.onEvent(CreateRequestUiEvent.PickupAddressChanged("Office"))
        viewModel.onEvent(CreateRequestUiEvent.PickupItemChanged("Keys"))
        viewModel.onEvent(CreateRequestUiEvent.Next)

        val state = viewModel.uiState.value
        assertThat(state.step).isEqualTo(CreateWizardStep.Details)
        assertThat(state.fieldError).isEqualTo("Enter drop-off address.")
    }

    @Test
    fun `next on Pickup step with blank item description sets validation error`() = runTest {
        viewModel.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Pickup))
        viewModel.onEvent(CreateRequestUiEvent.PickupAddressChanged("Office"))
        viewModel.onEvent(CreateRequestUiEvent.DropoffAddressChanged("Home Address"))
        viewModel.onEvent(CreateRequestUiEvent.Next)

        val state = viewModel.uiState.value
        assertThat(state.step).isEqualTo(CreateWizardStep.Details)
        assertThat(state.fieldError).isEqualTo("Describe the item.")
    }

    @Test
    fun `next on Pickup step with valid addresses and description advances to FeeReview`() = runTest {
        viewModel.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Pickup))
        viewModel.onEvent(CreateRequestUiEvent.PickupAddressChanged("Old Square Landmark"))
        viewModel.onEvent(CreateRequestUiEvent.DropoffAddressChanged("Hay El Ashgar Bldg 14"))
        viewModel.onEvent(CreateRequestUiEvent.PickupItemChanged("House keys in envelope"))
        viewModel.onEvent(CreateRequestUiEvent.Next)

        val state = viewModel.uiState.value
        assertThat(state.step).isEqualTo(CreateWizardStep.FeeReview)
        assertThat(state.fieldError).isNull()
    }

    @Test
    fun `successful submit for Pickup request passes addresses and emits Submitted effect`() = runTest {
        coEvery { createRequestUseCase(any()) } returns Result.Success("req_pickup_1")

        viewModel.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Pickup))
        viewModel.onEvent(CreateRequestUiEvent.PickupAddressChanged("Old Square Landmark"))
        viewModel.onEvent(CreateRequestUiEvent.DropoffAddressChanged("Hay El Ashgar Bldg 14"))
        viewModel.onEvent(CreateRequestUiEvent.PickupItemChanged("House keys in envelope"))
        viewModel.onEvent(CreateRequestUiEvent.Next)

        viewModel.uiEffect.test {
            viewModel.onEvent(CreateRequestUiEvent.Submit)
            testDispatcher.scheduler.advanceUntilIdle()

            val effect = awaitItem()
            assertThat(effect).isEqualTo(CreateRequestUiEffect.Submitted)
        }
    }

    @Test
    fun `UseSavedAddress with user profile address populates dropoffAddress from profile`() = runTest {
        val userAddress = UserAddress(
            city = "El Mahalla El Kubra",
            area = "Hay El Ashgar",
            street = "Main Sea St",
            building = "Bldg 14",
            floor = "3",
            apartment = "12",
        )
        val profile = mockk<UserProfile>()
        coEvery { profile.address } returns userAddress
        coEvery { getUserProfileUseCase() } returns Result.Success(profile)

        val vm = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        vm.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Pickup))
        vm.onEvent(CreateRequestUiEvent.UseSavedAddress(fallback = "Fallback Address"))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertThat(state.dropoffAddress).isEqualTo(userAddress.formattedDeliveryAddress())
        assertThat(state.savedDeliveryAddress).isEqualTo(userAddress.formattedDeliveryAddress())
    }

    @Test
    fun `UseSavedAddress when user profile has no address uses fallback`() = runTest {
        coEvery { getUserProfileUseCase() } returns Result.Success(null)

        val vm = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        vm.onEvent(CreateRequestUiEvent.CategorySelected(RequestCategory.Pickup))
        vm.onEvent(CreateRequestUiEvent.UseSavedAddress(fallback = "Fallback Address"))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = vm.uiState.value
        assertThat(state.dropoffAddress).isEqualTo("Fallback Address")
    }
}
