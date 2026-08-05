# 04 — Data Models (Phase 4)

## RequestStatus

`Pending → Accepted → InProgress → Purchased → OnTheWay → Delivered → Completed | Cancelled`

Non-Shopping skips `Purchased` (InProgress → OnTheWay).

## Request additions

Timeline timestamps, cancelReason/cancelledBy, statusHistory, lastMessage*, unread counts, rating, requesterName, runnerName.

## ChatMessage

`id, requestId, senderId, text, createdAt, clientId`
