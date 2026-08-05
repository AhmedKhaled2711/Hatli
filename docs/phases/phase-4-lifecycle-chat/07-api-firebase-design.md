# 07 — API / Firebase (Phase 4)

## requests fields

Add: acceptedAt, startedAt, purchasedAt, onTheWayAt, deliveredAt, completedAt, cancelledAt, cancelReason, cancelledBy, statusHistory, lastMessage*, unread counts, rating, requesterName, runnerName.

## messages subcollection

`requests/{id}/messages/{messageId}` — senderId, text, createdAt, clientId

## Rules

Participants read/write messages; runner advances status; requester confirms Completed / cancel with reason.
