package com.app.hatli.core.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RequestTest {

    private fun createBaseRequest(
        category: RequestCategory = RequestCategory.Shopping,
        status: RequestStatus = RequestStatus.Pending,
    ) = Request(
        id = "req_123",
        requesterId = "user_abc",
        runnerId = if (status == RequestStatus.Pending) null else "runner_xyz",
        category = category,
        status = status,
        title = "Test Request",
        description = "Test Description",
        deliveryFee = 35.0,
    )

    @Test
    fun `nextRunnerStatus returns correct next status for Shopping category`() {
        val acceptedReq = createBaseRequest(RequestCategory.Shopping, RequestStatus.Accepted)
        val inProgressReq = createBaseRequest(RequestCategory.Shopping, RequestStatus.InProgress)
        val purchasedReq = createBaseRequest(RequestCategory.Shopping, RequestStatus.Purchased)
        val onTheWayReq = createBaseRequest(RequestCategory.Shopping, RequestStatus.OnTheWay)
        val deliveredReq = createBaseRequest(RequestCategory.Shopping, RequestStatus.Delivered)

        assertThat(acceptedReq.nextRunnerStatus()).isEqualTo(RequestStatus.InProgress)
        assertThat(inProgressReq.nextRunnerStatus()).isEqualTo(RequestStatus.Purchased)
        assertThat(purchasedReq.nextRunnerStatus()).isEqualTo(RequestStatus.OnTheWay)
        assertThat(onTheWayReq.nextRunnerStatus()).isEqualTo(RequestStatus.Delivered)
        assertThat(deliveredReq.nextRunnerStatus()).isNull()
    }

    @Test
    fun `nextRunnerStatus skips Purchased for non-Shopping categories`() {
        val pickupReq = createBaseRequest(RequestCategory.Pickup, RequestStatus.InProgress)
        val billsReq = createBaseRequest(RequestCategory.Bills, RequestStatus.InProgress)
        val customReq = createBaseRequest(RequestCategory.Custom, RequestStatus.InProgress)

        assertThat(pickupReq.nextRunnerStatus()).isEqualTo(RequestStatus.OnTheWay)
        assertThat(billsReq.nextRunnerStatus()).isEqualTo(RequestStatus.OnTheWay)
        assertThat(customReq.nextRunnerStatus()).isEqualTo(RequestStatus.OnTheWay)
    }

    @Test
    fun `isTerminal evaluates Completed and Cancelled correctly`() {
        val completedReq = createBaseRequest(status = RequestStatus.Completed)
        val cancelledReq = createBaseRequest(status = RequestStatus.Cancelled)
        val activeReq = createBaseRequest(status = RequestStatus.InProgress)

        assertThat(completedReq.isTerminal).isTrue()
        assertThat(cancelledReq.isTerminal).isTrue()
        assertThat(activeReq.isTerminal).isFalse()
    }

    @Test
    fun `isActiveWork identifies non-pending active tasks`() {
        val pendingReq = createBaseRequest(status = RequestStatus.Pending)
        val acceptedReq = createBaseRequest(status = RequestStatus.Accepted)
        val completedReq = createBaseRequest(status = RequestStatus.Completed)

        assertThat(pendingReq.isActiveWork).isFalse()
        assertThat(acceptedReq.isActiveWork).isTrue()
        assertThat(completedReq.isActiveWork).isFalse()
    }

    @Test
    fun `lifecycleSteps excludes Purchased for non-shopping requests`() {
        val shoppingSteps = lifecycleSteps(RequestCategory.Shopping)
        val pickupSteps = lifecycleSteps(RequestCategory.Pickup)

        assertThat(shoppingSteps).contains(RequestStatus.Purchased)
        assertThat(pickupSteps).doesNotContain(RequestStatus.Purchased)
    }

    @Test
    fun `stepperIndex calculates correct index across categories`() {
        val pendingIdx = RequestStatus.Pending.stepperIndex(RequestCategory.Shopping)
        val acceptedIdx = RequestStatus.Accepted.stepperIndex(RequestCategory.Shopping)
        val onTheWayIdxShopping = RequestStatus.OnTheWay.stepperIndex(RequestCategory.Shopping)
        val onTheWayIdxPickup = RequestStatus.OnTheWay.stepperIndex(RequestCategory.Pickup)

        assertThat(pendingIdx).isEqualTo(-1)
        assertThat(acceptedIdx).isEqualTo(0)
        assertThat(onTheWayIdxShopping).isEqualTo(3)
        assertThat(onTheWayIdxPickup).isEqualTo(2)
        assertThat(RequestStatus.Completed.stepperIndex(RequestCategory.Shopping)).isEqualTo(5)
        assertThat(RequestStatus.Completed.stepperIndex(RequestCategory.Pickup)).isEqualTo(4)
        assertThat(RequestStatus.Cancelled.stepperIndex(RequestCategory.Shopping)).isEqualTo(-1)
    }

    @Test
    fun `stepperStepVisualState marks Accepted as Unreached when request is Pending`() {
        val steps = lifecycleSteps(RequestCategory.Shopping)
        steps.forEach { step ->
            val state = stepperStepVisualState(step, RequestStatus.Pending, RequestCategory.Shopping)
            assertThat(state).isEqualTo(StepperStepVisualState.Unreached)
        }
    }

    @Test
    fun `stepperStepVisualState activates Accepted as ActiveStable when request is Accepted`() {
        val acceptedState = stepperStepVisualState(
            step = RequestStatus.Accepted,
            currentStatus = RequestStatus.Accepted,
            category = RequestCategory.Shopping,
        )
        val inProgressState = stepperStepVisualState(
            step = RequestStatus.InProgress,
            currentStatus = RequestStatus.Accepted,
            category = RequestCategory.Shopping,
        )

        assertThat(acceptedState).isEqualTo(StepperStepVisualState.ActiveStable)
        assertThat(inProgressState).isEqualTo(StepperStepVisualState.Unreached)
    }

    @Test
    fun `stepperStepVisualState marks Accepted completed and InProgress ActivePulsing when InProgress`() {
        val acceptedState = stepperStepVisualState(
            step = RequestStatus.Accepted,
            currentStatus = RequestStatus.InProgress,
            category = RequestCategory.Shopping,
        )
        val inProgressState = stepperStepVisualState(
            step = RequestStatus.InProgress,
            currentStatus = RequestStatus.InProgress,
            category = RequestCategory.Shopping,
        )

        assertThat(acceptedState).isEqualTo(StepperStepVisualState.Completed)
        assertThat(inProgressState).isEqualTo(StepperStepVisualState.ActivePulsing)
    }

    @Test
    fun `stepperStepVisualState marks all steps as Completed without pulsing when Completed`() {
        val steps = lifecycleSteps(RequestCategory.Shopping)
        steps.forEach { step ->
            val state = stepperStepVisualState(step, RequestStatus.Completed, RequestCategory.Shopping)
            assertThat(state).isEqualTo(StepperStepVisualState.Completed)
        }
    }

    @Test
    fun `stepperStepVisualState marks all steps Unreached when Cancelled`() {
        val steps = lifecycleSteps(RequestCategory.Shopping)
        steps.forEach { step ->
            val state = stepperStepVisualState(step, RequestStatus.Cancelled, RequestCategory.Shopping)
            assertThat(state).isEqualTo(StepperStepVisualState.Unreached)
        }
    }
}

