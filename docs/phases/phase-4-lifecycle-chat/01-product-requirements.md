# 01 — Product Requirements (Phase 4)

## Problem

After Accept, the request leaves Pending (correct) but also vanishes from both Homes (UX bug). Users need persistent active work, status progression without maps, and Chat for clarifications.

## Who

| Persona | Need |
|---------|------|
| Requester | See who accepted, chat, confirm delivery, rate |
| Runner | See Current Job, advance status, chat with requester |

## Success

- Current Request / Current Job always visible while active
- Chat realtime via Firestore
- Category-aware status path to Completed + rating
- Cancel with reason; no silent edit after Accept

## Non-goals

- Live GPS / maps / sockets / FCM (4b) / payments / runner→requester rating
