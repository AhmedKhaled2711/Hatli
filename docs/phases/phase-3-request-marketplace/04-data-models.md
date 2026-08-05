# 04 — Data Models (Phase 3)

## RequestStatus

`Pending → Accepted → InProgress → OnTheWay → Delivered → Completed | Cancelled`

Slice B transitions: Create → `Pending`; Accept → `Accepted`. Others reserved.

## RequestCategory

`Shopping` | `Pickup` | `Bills` | `Custom`

## Request (domain)

| Field | Type | Notes |
|-------|------|--------|
| id | String | = Firestore doc id |
| requesterId | String | |
| runnerId | String? | set on Accept |
| category | RequestCategory | |
| status | RequestStatus | |
| title | String | list/Home summary |
| description | String | |
| budget | Double? | EGP |
| deliveryFee | Double | EGP |
| pickupLabel | String? | |
| dropoffLabel | String? | |
| shopping | ShoppingDetails? | |
| pickup | PickupDetails? | |
| bills | BillsDetails? | |
| custom | CustomDetails? | |
| createdAtEpochMs | Long | |
| updatedAtEpochMs | Long | |

### Category payloads

| Type | Fields |
|------|--------|
| ShoppingDetails | shopName?, itemsText, notes? |
| PickupDetails | itemDescription |
| BillsDetails | billType, accountRef, amount |
| CustomDetails | (title/description on parent) |

## RequestSummary (Home cards)

Unchanged role; map from `Request` (id, title, status, offerAmount←deliveryFee, runnerName?, updatedAt).
