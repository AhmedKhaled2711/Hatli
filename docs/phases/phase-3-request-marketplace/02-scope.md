# 02 — Scope (Phase 3 — Slice B)

## In

| Item | Owner |
|------|--------|
| Phase docs 01–10 | `docs/phases/phase-3-request-marketplace` |
| `Request` / `RequestCategory` / updated `RequestStatus` | `:core:model` |
| Firestore `requests` + rules + indexes | `firestore.rules`, `firestore.indexes.json` |
| `RequestRepository` + Firestore impl | `:core:domain` / `:core:data` |
| Create / Accept / Observe use cases | `:core:domain` |
| Firestore-backed Home (replace Fake) | `:core:data` |
| `:feature:request` assisted wizard (4 categories) | new module |
| Nav: replace Create Request stub | `:app` / `:feature:home` / `:feature:request` |
| Runner Pending feed + Accept | `:feature:home` |
| Live Requester Home on Accept | `:feature:home` |

## Out

- Tracking / OnTheWay / Delivered execution screens
- Maps, geo, true distance fee
- Place-specific category cards
- Chat, FCM, payments, sockets
- Draft autosave beyond in-memory wizard state

## Assumptions

- Auth uid is requester on create; runner must be signed in to accept
- Delivery fee default = 35 EGP (constant), user may edit
- Address fields are plain strings (profile address optional later)
- Composite indexes deployed with rules for Pending + requester queries
