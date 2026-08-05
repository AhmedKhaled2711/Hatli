# 03 — User Flows (Phase 4)

## Happy path

1. Accept → both see Current with names + Chat
2. Runner: Accepted → InProgress → (Purchased if Shopping) → OnTheWay → Delivered
3. Chat anytime after Accept
4. Requester Confirm received → Completed → Rate Runner

## Cancel

Either party until Delivered: reason required → Cancelled; clears Current; does not reopen Pending.

## Edge

Accept race, offline chat fail+retry, Delivered waiting on confirm, missing index soft-fail.
