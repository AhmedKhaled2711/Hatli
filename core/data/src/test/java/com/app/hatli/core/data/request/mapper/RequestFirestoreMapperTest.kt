package com.app.hatli.core.data.request.mapper

import com.app.hatli.core.data.request.FirestoreRequestDataSource
import com.app.hatli.core.model.BillsDetails
import com.app.hatli.core.model.CancelledBy
import com.app.hatli.core.model.CreateRequestInput
import com.app.hatli.core.model.CustomDetails
import com.app.hatli.core.model.PickupDetails
import com.app.hatli.core.model.Request
import com.app.hatli.core.model.RequestCategory
import com.app.hatli.core.model.RequestRating
import com.app.hatli.core.model.RequestStatus
import com.app.hatli.core.model.ShoppingDetails
import com.app.hatli.core.model.StatusHistoryEntry
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.Date

class RequestFirestoreMapperTest {

    // -------------------------------------------------------------------------
    // Category mapping tests
    // -------------------------------------------------------------------------

    @Test
    fun `toRequestCategory maps known categories correctly`() {
        assertThat(RequestFirestoreMapper.toRequestCategory("Shopping")).isEqualTo(RequestCategory.Shopping)
        assertThat(RequestFirestoreMapper.toRequestCategory("Pickup")).isEqualTo(RequestCategory.Pickup)
        assertThat(RequestFirestoreMapper.toRequestCategory("Bills")).isEqualTo(RequestCategory.Bills)
        assertThat(RequestFirestoreMapper.toRequestCategory("Custom")).isEqualTo(RequestCategory.Custom)
    }

    @Test
    fun `toRequestCategory returns null for null, empty or invalid strings`() {
        assertThat(RequestFirestoreMapper.toRequestCategory(null as String?)).isNull()
        assertThat(RequestFirestoreMapper.toRequestCategory("")).isNull()
        assertThat(RequestFirestoreMapper.toRequestCategory("UnknownCategory")).isNull()
        assertThat(RequestFirestoreMapper.toRequestCategory("shopping")).isNull() // case-sensitive enum
    }

    // -------------------------------------------------------------------------
    // Status mapping tests
    // -------------------------------------------------------------------------

    @Test
    fun `toRequestStatus maps all known statuses correctly`() {
        val expected = listOf(
            RequestStatus.Pending,
            RequestStatus.Accepted,
            RequestStatus.InProgress,
            RequestStatus.Purchased,
            RequestStatus.OnTheWay,
            RequestStatus.Delivered,
            RequestStatus.Completed,
            RequestStatus.Cancelled,
        )
        for (status in expected) {
            assertThat(RequestFirestoreMapper.toRequestStatus(status.name)).isEqualTo(status)
        }
    }

    @Test
    fun `toRequestStatus returns null for invalid or null strings`() {
        assertThat(RequestFirestoreMapper.toRequestStatus(null as String?)).isNull()
        assertThat(RequestFirestoreMapper.toRequestStatus("")).isNull()
        assertThat(RequestFirestoreMapper.toRequestStatus("INVALID_STATUS")).isNull()
    }

    // -------------------------------------------------------------------------
    // Sub-details helpers tests
    // -------------------------------------------------------------------------

    @Test
    fun `toShoppingDetails parses valid map and returns null on missing required itemsText`() {
        val validMap = mapOf(
            "shopName" to "Carrefour",
            "itemsText" to "Milk, Bread",
            "notes" to "Whole milk",
        )
        val result = RequestFirestoreMapper.toShoppingDetails(validMap)
        assertThat(result).isNotNull()
        assertThat(result?.shopName).isEqualTo("Carrefour")
        assertThat(result?.itemsText).isEqualTo("Milk, Bread")
        assertThat(result?.notes).isEqualTo("Whole milk")

        val missingItems = mapOf("shopName" to "Carrefour")
        assertThat(RequestFirestoreMapper.toShoppingDetails(missingItems)).isNull()
        assertThat(RequestFirestoreMapper.toShoppingDetails(null)).isNull()
        assertThat(RequestFirestoreMapper.toShoppingDetails("Not a map")).isNull()
    }

    @Test
    fun `toPickupDetails parses valid map and returns null on invalid input`() {
        val validMap = mapOf("itemDescription" to "Keys")
        val result = RequestFirestoreMapper.toPickupDetails(validMap)
        assertThat(result).isEqualTo(PickupDetails("Keys"))

        assertThat(RequestFirestoreMapper.toPickupDetails(emptyMap<String, Any>())).isNull()
        assertThat(RequestFirestoreMapper.toPickupDetails(null)).isNull()
    }

    @Test
    fun `toBillsDetails parses valid map and returns null on incomplete fields`() {
        val validMap = mapOf(
            "billType" to "Electricity",
            "accountRef" to "ACC123",
            "amount" to 250.5,
        )
        val result = RequestFirestoreMapper.toBillsDetails(validMap)
        assertThat(result).isEqualTo(BillsDetails("Electricity", "ACC123", 250.5))

        val missingAmount = mapOf("billType" to "Electricity", "accountRef" to "ACC123")
        assertThat(RequestFirestoreMapper.toBillsDetails(missingAmount)).isNull()
        assertThat(RequestFirestoreMapper.toBillsDetails(null)).isNull()
    }

    @Test
    fun `toCustomDetails returns non-null when raw value is present`() {
        assertThat(RequestFirestoreMapper.toCustomDetails(mapOf("ok" to true))).isEqualTo(CustomDetails())
        assertThat(RequestFirestoreMapper.toCustomDetails(null)).isNull()
    }

    @Test
    fun `toRating parses valid map with stars and comment`() {
        val validMap = mapOf("stars" to 5, "comment" to "Excellent!")
        val result = RequestFirestoreMapper.toRating(validMap)
        assertThat(result).isNotNull()
        assertThat(result?.stars).isEqualTo(5)
        assertThat(result?.comment).isEqualTo("Excellent!")
        assertThat(result?.ratedAtEpochMs).isEqualTo(0L)

        val missingStars = mapOf("comment" to "Good")
        assertThat(RequestFirestoreMapper.toRating(missingStars)).isNull()
        assertThat(RequestFirestoreMapper.toRating(null)).isNull()
    }

    @Test
    fun `toCancelledBy parses valid enum and returns null on invalid`() {
        assertThat(RequestFirestoreMapper.toCancelledBy("Requester")).isEqualTo(CancelledBy.Requester)
        assertThat(RequestFirestoreMapper.toCancelledBy("Runner")).isEqualTo(CancelledBy.Runner)
        assertThat(RequestFirestoreMapper.toCancelledBy(null)).isNull()
        assertThat(RequestFirestoreMapper.toCancelledBy("Unknown")).isNull()
    }

    @Test
    fun `toStatusHistory filters out malformed entries`() {
        val raw = listOf(
            mapOf("status" to "Accepted", "at" to 1000L, "byUid" to "runner1"),
            mapOf("status" to "INVALID", "at" to 2000L, "byUid" to "runner1"),
            mapOf("at" to 3000L), // missing status
        )
        val result = RequestFirestoreMapper.toStatusHistory(raw)
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(StatusHistoryEntry(RequestStatus.Accepted, 1000L, "runner1"))
    }

    // -------------------------------------------------------------------------
    // Metadata mapping tests
    // -------------------------------------------------------------------------

    @Test
    fun `toRequestMetadata parses all fields from map`() {
        val map = mapOf(
            FirestoreRequestDataSource.FIELD_ACCEPTED_AT to 1000L,
            FirestoreRequestDataSource.FIELD_STARTED_AT to 2000L,
            FirestoreRequestDataSource.FIELD_PURCHASED_AT to 3000L,
            FirestoreRequestDataSource.FIELD_ON_THE_WAY_AT to 4000L,
            FirestoreRequestDataSource.FIELD_DELIVERED_AT to 5000L,
            FirestoreRequestDataSource.FIELD_COMPLETED_AT to 6000L,
            FirestoreRequestDataSource.FIELD_CANCELLED_AT to 7000L,
            FirestoreRequestDataSource.FIELD_CANCEL_REASON to "Changed mind",
            FirestoreRequestDataSource.FIELD_CANCELLED_BY to "Requester",
            FirestoreRequestDataSource.FIELD_LAST_MESSAGE to "Hello",
            FirestoreRequestDataSource.FIELD_LAST_MESSAGE_AT to 8000L,
            FirestoreRequestDataSource.FIELD_LAST_MESSAGE_SENDER to "runner1",
            FirestoreRequestDataSource.FIELD_REQUESTER_UNREAD to 2L,
            FirestoreRequestDataSource.FIELD_RUNNER_UNREAD to 1L,
            FirestoreRequestDataSource.FIELD_RATING to mapOf("stars" to 4, "comment" to "Good"),
            FirestoreRequestDataSource.FIELD_CREATED_AT to 100L,
            FirestoreRequestDataSource.FIELD_UPDATED_AT to 200L,
        )

        val metadata = RequestFirestoreMapper.toRequestMetadata(map)
        assertThat(metadata.acceptedAtEpochMs).isEqualTo(1000L)
        assertThat(metadata.startedAtEpochMs).isEqualTo(2000L)
        assertThat(metadata.purchasedAtEpochMs).isEqualTo(3000L)
        assertThat(metadata.onTheWayAtEpochMs).isEqualTo(4000L)
        assertThat(metadata.deliveredAtEpochMs).isEqualTo(5000L)
        assertThat(metadata.completedAtEpochMs).isEqualTo(6000L)
        assertThat(metadata.cancelledAtEpochMs).isEqualTo(7000L)
        assertThat(metadata.cancelReason).isEqualTo("Changed mind")
        assertThat(metadata.cancelledBy).isEqualTo(CancelledBy.Requester)
        assertThat(metadata.lastMessage).isEqualTo("Hello")
        assertThat(metadata.lastMessageAtEpochMs).isEqualTo(8000L)
        assertThat(metadata.lastMessageSenderId).isEqualTo("runner1")
        assertThat(metadata.requesterUnreadCount).isEqualTo(2)
        assertThat(metadata.runnerUnreadCount).isEqualTo(1)
        assertThat(metadata.rating?.stars).isEqualTo(4)
        assertThat(metadata.createdAtEpochMs).isEqualTo(100L)
        assertThat(metadata.updatedAtEpochMs).isEqualTo(200L)
    }

    @Test
    fun `parseEpochMs parses Number and Date`() {
        assertThat(RequestFirestoreMapper.parseEpochMs(12345L)).isEqualTo(12345L)
        assertThat(RequestFirestoreMapper.parseEpochMs(12345)).isEqualTo(12345L)
        val date = Date(54321L)
        assertThat(RequestFirestoreMapper.parseEpochMs(date)).isEqualTo(54321L)
        assertThat(RequestFirestoreMapper.parseEpochMs(null)).isNull()
        assertThat(RequestFirestoreMapper.parseEpochMs("invalid")).isNull()
    }

    // -------------------------------------------------------------------------
    // Full Request mapping tests
    // -------------------------------------------------------------------------

    @Test
    fun `toRequest maps valid map to Request object`() {
        val map = mapOf(
            FirestoreRequestDataSource.FIELD_REQUEST_ID to "req-123",
            FirestoreRequestDataSource.FIELD_REQUESTER_ID to "user-1",
            FirestoreRequestDataSource.FIELD_RUNNER_ID to "runner-1",
            FirestoreRequestDataSource.FIELD_CATEGORY to "Shopping",
            FirestoreRequestDataSource.FIELD_STATUS to "InProgress",
            FirestoreRequestDataSource.FIELD_TITLE to "Groceries",
            FirestoreRequestDataSource.FIELD_DESCRIPTION to "Buy apples and milk",
            FirestoreRequestDataSource.FIELD_BUDGET to 150.0,
            FirestoreRequestDataSource.FIELD_DELIVERY_FEE to 30.0,
            FirestoreRequestDataSource.FIELD_PICKUP_LABEL to "Supermarket",
            FirestoreRequestDataSource.FIELD_DROPOFF_LABEL to "Home",
            FirestoreRequestDataSource.FIELD_PICKUP_LATITUDE to 30.0444,
            FirestoreRequestDataSource.FIELD_PICKUP_LONGITUDE to 31.2357,
            FirestoreRequestDataSource.FIELD_DROPOFF_LATITUDE to 30.0500,
            FirestoreRequestDataSource.FIELD_DROPOFF_LONGITUDE to 31.2400,
            FirestoreRequestDataSource.FIELD_SHOPPING to mapOf("itemsText" to "Apples"),
            FirestoreRequestDataSource.FIELD_REQUESTER_NAME to "Mina",
            FirestoreRequestDataSource.FIELD_RUNNER_NAME to "Ahmed",
            FirestoreRequestDataSource.FIELD_CREATED_AT to 1000L,
            FirestoreRequestDataSource.FIELD_UPDATED_AT to 2000L,
        )

        val request = RequestFirestoreMapper.toRequest(map, documentId = "req-123")
        assertThat(request).isNotNull()
        assertThat(request?.id).isEqualTo("req-123")
        assertThat(request?.requesterId).isEqualTo("user-1")
        assertThat(request?.runnerId).isEqualTo("runner-1")
        assertThat(request?.category).isEqualTo(RequestCategory.Shopping)
        assertThat(request?.status).isEqualTo(RequestStatus.InProgress)
        assertThat(request?.title).isEqualTo("Groceries")
        assertThat(request?.description).isEqualTo("Buy apples and milk")
        assertThat(request?.budget).isEqualTo(150.0)
        assertThat(request?.deliveryFee).isEqualTo(30.0)
        assertThat(request?.pickupLabel).isEqualTo("Supermarket")
        assertThat(request?.dropoffLabel).isEqualTo("Home")
        assertThat(request?.pickupLatitude).isEqualTo(30.0444)
        assertThat(request?.pickupLongitude).isEqualTo(31.2357)
        assertThat(request?.dropoffLatitude).isEqualTo(30.0500)
        assertThat(request?.dropoffLongitude).isEqualTo(31.2400)
        assertThat(request?.shopping?.itemsText).isEqualTo("Apples")
        assertThat(request?.requesterName).isEqualTo("Mina")
        assertThat(request?.runnerName).isEqualTo("Ahmed")
        assertThat(request?.createdAtEpochMs).isEqualTo(1000L)
        assertThat(request?.updatedAtEpochMs).isEqualTo(2000L)
    }

    @Test
    fun `toRequest returns null when mandatory category or status is missing`() {
        val missingCategory = mapOf(
            FirestoreRequestDataSource.FIELD_STATUS to "Pending",
        )
        assertThat(RequestFirestoreMapper.toRequest(missingCategory, "1")).isNull()

        val missingStatus = mapOf(
            FirestoreRequestDataSource.FIELD_CATEGORY to "Shopping",
        )
        assertThat(RequestFirestoreMapper.toRequest(missingStatus, "1")).isNull()
    }

    @Test
    fun `toRequest applies fallback defaults for missing optional fields`() {
        val minimalMap = mapOf(
            FirestoreRequestDataSource.FIELD_CATEGORY to "Custom",
            FirestoreRequestDataSource.FIELD_STATUS to "Pending",
        )

        val request = RequestFirestoreMapper.toRequest(minimalMap, documentId = "doc-456")
        assertThat(request).isNotNull()
        assertThat(request?.id).isEqualTo("doc-456")
        assertThat(request?.requesterId).isEmpty()
        assertThat(request?.runnerId).isNull()
        assertThat(request?.title).isEmpty()
        assertThat(request?.description).isEmpty()
        assertThat(request?.budget).isNull()
        assertThat(request?.deliveryFee).isEqualTo(0.0)
        assertThat(request?.createdAtEpochMs).isEqualTo(0L)
        assertThat(request?.updatedAtEpochMs).isEqualTo(0L)
    }

    // -------------------------------------------------------------------------
    // Summary mapping tests
    // -------------------------------------------------------------------------

    @Test
    fun `toRequestSummary maps domain Request to RequestSummary correctly`() {
        val request = Request(
            id = "req-99",
            requesterId = "user-1",
            runnerId = "runner-1",
            category = RequestCategory.Shopping,
            status = RequestStatus.InProgress,
            title = "Need Food",
            description = "Some food",
            deliveryFee = 45.0,
            requesterName = "Mina",
            runnerName = "Tarek",
            requesterUnreadCount = 3,
            runnerUnreadCount = 1,
            lastMessage = "Coming soon",
            rating = RequestRating(stars = 5, comment = null, ratedAtEpochMs = 0L),
            pickupLabel = "Shop",
            dropoffLabel = "Home",
            createdAtEpochMs = 100L,
            updatedAtEpochMs = 200L,
        )

        val summary = RequestFirestoreMapper.toRequestSummary(request)
        assertThat(summary.id).isEqualTo("req-99")
        assertThat(summary.title).isEqualTo("Need Food")
        assertThat(summary.status).isEqualTo(RequestStatus.InProgress)
        assertThat(summary.category).isEqualTo(RequestCategory.Shopping)
        assertThat(summary.offerAmount).isEqualTo(45.0)
        assertThat(summary.requesterName).isEqualTo("Mina")
        assertThat(summary.runnerName).isEqualTo("Tarek")
        assertThat(summary.unreadCount).isEqualTo(3) // maxOf(3, 1)
        assertThat(summary.lastMessage).isEqualTo("Coming soon")
        assertThat(summary.hasRating).isTrue()
        assertThat(summary.pickupLabel).isEqualTo("Shop")
        assertThat(summary.dropoffLabel).isEqualTo("Home")
        assertThat(summary.description).isEqualTo("Some food")
        assertThat(summary.createdAtEpochMs).isEqualTo(100L)
        assertThat(summary.updatedAtEpochMs).isEqualTo(200L)
    }

    // -------------------------------------------------------------------------
    // Payload builders tests
    // -------------------------------------------------------------------------

    @Test
    fun `toCreateRequestMap constructs correct fields and Pending status`() {
        val input = CreateRequestInput(
            category = RequestCategory.Shopping,
            title = "Order",
            description = "Desc",
            deliveryFee = 25.0,
            budget = 100.0,
            pickupLabel = "Store",
            dropoffLabel = "House",
            shopping = ShoppingDetails("Shop", "Items", "Notes"),
        )

        val map = RequestFirestoreMapper.toCreateRequestMap(
            input = input,
            requestId = "req-id",
            requesterId = "uid-1",
            requesterName = "User 1",
        )

        assertThat(map[FirestoreRequestDataSource.FIELD_REQUEST_ID]).isEqualTo("req-id")
        assertThat(map[FirestoreRequestDataSource.FIELD_REQUESTER_ID]).isEqualTo("uid-1")
        assertThat(map[FirestoreRequestDataSource.FIELD_CATEGORY]).isEqualTo("Shopping")
        assertThat(map[FirestoreRequestDataSource.FIELD_STATUS]).isEqualTo("Pending")
        assertThat(map[FirestoreRequestDataSource.FIELD_TITLE]).isEqualTo("Order")
        assertThat(map[FirestoreRequestDataSource.FIELD_DESCRIPTION]).isEqualTo("Desc")
        assertThat(map[FirestoreRequestDataSource.FIELD_DELIVERY_FEE]).isEqualTo(25.0)
        assertThat(map[FirestoreRequestDataSource.FIELD_BUDGET]).isEqualTo(100.0)
        assertThat(map[FirestoreRequestDataSource.FIELD_REQUESTER_NAME]).isEqualTo("User 1")
        assertThat(map[FirestoreRequestDataSource.FIELD_REQUESTER_UNREAD]).isEqualTo(0)
        assertThat(map[FirestoreRequestDataSource.FIELD_RUNNER_UNREAD]).isEqualTo(0)
    }

    @Test
    fun `buildHistoryEntry constructs expected map`() {
        val entry = RequestFirestoreMapper.buildHistoryEntry("Accepted", "runner-1", 12345L)
        assertThat(entry["status"]).isEqualTo("Accepted")
        assertThat(entry["byUid"]).isEqualTo("runner-1")
        assertThat(entry["at"]).isEqualTo(12345L)
    }

    @Test
    fun `buildAdvanceStatusUpdateMap adds timestamp for specific transitions`() {
        val history = listOf(mapOf("status" to "InProgress"))
        val inProgressMap = RequestFirestoreMapper.buildAdvanceStatusUpdateMap(RequestStatus.InProgress, history)
        assertThat(inProgressMap).containsKey(FirestoreRequestDataSource.FIELD_STARTED_AT)
        assertThat(inProgressMap[FirestoreRequestDataSource.FIELD_STATUS]).isEqualTo(RequestStatus.InProgress.name)

        val purchasedMap = RequestFirestoreMapper.buildAdvanceStatusUpdateMap(RequestStatus.Purchased, history)
        assertThat(purchasedMap).containsKey(FirestoreRequestDataSource.FIELD_PURCHASED_AT)

        val onTheWayMap = RequestFirestoreMapper.buildAdvanceStatusUpdateMap(RequestStatus.OnTheWay, history)
        assertThat(onTheWayMap).containsKey(FirestoreRequestDataSource.FIELD_ON_THE_WAY_AT)

        val deliveredMap = RequestFirestoreMapper.buildAdvanceStatusUpdateMap(RequestStatus.Delivered, history)
        assertThat(deliveredMap).containsKey(FirestoreRequestDataSource.FIELD_DELIVERED_AT)
    }

    @Test
    fun `buildChatMetadataUpdateMap updates unread count for other participant`() {
        val requesterMap = RequestFirestoreMapper.buildChatMetadataUpdateMap("Hi", "user1", isRequester = true)
        assertThat(requesterMap).containsKey(FirestoreRequestDataSource.FIELD_RUNNER_UNREAD)
        assertThat(requesterMap).doesNotContainKey(FirestoreRequestDataSource.FIELD_REQUESTER_UNREAD)

        val runnerMap = RequestFirestoreMapper.buildChatMetadataUpdateMap("Hi", "runner1", isRequester = false)
        assertThat(runnerMap).containsKey(FirestoreRequestDataSource.FIELD_REQUESTER_UNREAD)
        assertThat(runnerMap).doesNotContainKey(FirestoreRequestDataSource.FIELD_RUNNER_UNREAD)
    }
}
