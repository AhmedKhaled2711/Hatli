package com.app.hatli.feature.auth.presentation.onboarding

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.app.hatli.feature.auth.R

/**
 * Single place for onboarding [R.string] / [R.drawable] IDs.
 * Keeps Compose screens free of direct R access (helps IDE indexing).
 */
internal object OnboardingStrings {
    val brand: Int get() = R.string.onboarding_brand
    val skip: Int get() = R.string.onboarding_skip
    val next: Int get() = R.string.onboarding_next
    val getStarted: Int get() = R.string.onboarding_get_started

    // Mock Screen 1: Create Request
    val mockCreateTitle: Int get() = R.string.onboarding_mock_create_title
    val mockStep2Of3: Int get() = R.string.onboarding_mock_step_2_of_3
    val mockStepService: Int get() = R.string.onboarding_mock_step_service
    val mockStepDetails: Int get() = R.string.onboarding_mock_step_details
    val mockStepReview: Int get() = R.string.onboarding_mock_step_review
    val mockCategoryShopping: Int get() = R.string.onboarding_mock_category_shopping
    val mockCategoryShoppingSubtitle: Int get() = R.string.onboarding_mock_category_shopping_subtitle
    val mockChangeCategory: Int get() = R.string.onboarding_mock_change_category
    val mockShopNameLabel: Int get() = R.string.onboarding_mock_shop_name_label
    val mockShopNameValue: Int get() = R.string.onboarding_mock_shop_name_value
    val mockItemsLabel: Int get() = R.string.onboarding_mock_items_label
    val mockItemsValue: Int get() = R.string.onboarding_mock_items_value
    val mockItemsCharCount: Int get() = R.string.onboarding_mock_items_char_count
    val mockNotesLabel: Int get() = R.string.onboarding_mock_notes_label
    val mockNotesValue: Int get() = R.string.onboarding_mock_notes_value
    val mockBudgetLabel: Int get() = R.string.onboarding_mock_budget_label
    val mockBudgetValue: Int get() = R.string.onboarding_mock_budget_value
    val mockBtnContinue: Int get() = R.string.onboarding_mock_btn_continue
    val mockBtnBack: Int get() = R.string.onboarding_mock_btn_back

    // Mock Screen 2: Fee Review
    val mockStep3Of3: Int get() = R.string.onboarding_mock_step_3_of_3
    val mockReviewHeadline: Int get() = R.string.onboarding_mock_review_headline
    val mockFeeStepperTitle: Int get() = R.string.onboarding_mock_fee_stepper_title
    val mockFeeStepperDesc: Int get() = R.string.onboarding_mock_fee_stepper_desc
    val mockFeeStepperAmount: Int get() = R.string.onboarding_mock_fee_stepper_amount
    val mockOrderCostSummary: Int get() = R.string.onboarding_mock_order_cost_summary
    val mockReviewServiceTypeLabel: Int get() = R.string.onboarding_mock_review_service_type_label
    val mockReviewServiceTypeVal: Int get() = R.string.onboarding_mock_review_service_type_val
    val mockReviewItemsCostLabel: Int get() = R.string.onboarding_mock_review_items_cost_label
    val mockReviewItemsCostNote: Int get() = R.string.onboarding_mock_review_items_cost_note
    val mockReviewServiceChargeLabel: Int get() = R.string.onboarding_mock_review_service_charge_label
    val mockReviewServiceChargeFree: Int get() = R.string.onboarding_mock_review_service_charge_free
    val mockReviewDeliveryFeeLabel: Int get() = R.string.onboarding_mock_review_delivery_fee_label
    val mockReviewDeliveryFeeVal: Int get() = R.string.onboarding_mock_review_delivery_fee_val
    val mockBtnConfirm: Int get() = R.string.onboarding_mock_btn_confirm

    // Mock Screen 3: Live Tracking
    val mockTrackingDetailTitle: Int get() = R.string.onboarding_mock_tracking_detail_title
    val mockStatusHeroPendingTitle: Int get() = R.string.onboarding_mock_status_hero_pending_title
    val mockStatusHeroOrderTitle: Int get() = R.string.onboarding_mock_status_hero_order_title
    val mockStatusHeroPendingDesc: Int get() = R.string.onboarding_mock_status_hero_pending_desc
    val mockTrackProgressTitle: Int get() = R.string.onboarding_mock_track_progress_title
    val mockTrackStepAccepted: Int get() = R.string.onboarding_mock_track_step_accepted
    val mockTrackStepInProgress: Int get() = R.string.onboarding_mock_track_step_in_progress
    val mockTrackStepPurchased: Int get() = R.string.onboarding_mock_track_step_purchased
    val mockTrackStepOnTheWay: Int get() = R.string.onboarding_mock_track_step_on_the_way
    val mockTrackStepDelivered: Int get() = R.string.onboarding_mock_track_step_delivered
    val mockTrackStepCompleted: Int get() = R.string.onboarding_mock_track_step_completed
    val mockOrderInfoTitle: Int get() = R.string.onboarding_mock_order_info_title
    val mockOrderInfoFeeBadge: Int get() = R.string.onboarding_mock_order_info_fee_badge
    val mockOrderInfoDescLabel: Int get() = R.string.onboarding_mock_order_info_desc_label
    val mockOrderInfoDescVal: Int get() = R.string.onboarding_mock_order_info_desc_val
    val mockOrderInfoPickupLabel: Int get() = R.string.onboarding_mock_order_info_pickup_label
    val mockOrderInfoPickupVal: Int get() = R.string.onboarding_mock_order_info_pickup_val
    val mockOrderInfoDropoffLabel: Int get() = R.string.onboarding_mock_order_info_dropoff_label
    val mockOrderInfoDropoffVal: Int get() = R.string.onboarding_mock_order_info_dropoff_val
    val mockOrderInfoFeeLabel: Int get() = R.string.onboarding_mock_order_info_fee_label
    val mockOrderInfoFeeVal: Int get() = R.string.onboarding_mock_order_info_fee_val
    val mockBtnCancelOrder: Int get() = R.string.onboarding_mock_btn_cancel_order
}

@StringRes
internal fun OnboardingPageType.titleRes(): Int = when (this) {
    OnboardingPageType.OrderNearby -> R.string.onboarding_page1_title
    OnboardingPageType.CreateRequest -> R.string.onboarding_page2_title
    OnboardingPageType.NearbyRunners -> R.string.onboarding_page3_title
}

@StringRes
internal fun OnboardingPageType.bodyRes(): Int = when (this) {
    OnboardingPageType.OrderNearby -> R.string.onboarding_page1_body
    OnboardingPageType.CreateRequest -> R.string.onboarding_page2_body
    OnboardingPageType.NearbyRunners -> R.string.onboarding_page3_body
}

@Composable
internal fun OnboardingPageType.titleText(): String = stringResource(titleRes())

@Composable
internal fun OnboardingPageType.bodyText(): String = stringResource(bodyRes())

@Composable
internal fun onboardingPrimaryLabel(isLast: Boolean): String = stringResource(
    if (isLast) OnboardingStrings.getStarted else OnboardingStrings.next,
)
