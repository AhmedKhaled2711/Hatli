# 04 — Data Models (Phase 2)

## Domain (Home-facing)

| Model | Fields | Notes |
|-------|--------|-------|
| `RequestStatus` | `Draft`, `SearchingRunner`, `Accepted`, `InProgress`, `Completed`, `Cancelled` | Shared Request/Job status |
| `RequestSummary` | `id`, `title`, `status`, `etaMinutes?`, `distanceMeters?`, `offerAmount?`, `runnerName?`, `updatedAt` | Home + list cards |
| `RequesterHomeSnapshot` | `active`, `recent`, `unreadNotifications`, `tips` | One read for Requester Home |
| `RunnerPresence` | `isOnline` | Uber-style toggle |
| `RunnerHomeStats` | `dailyEarnings`, `todayJobsCount`, `rating` | Dashboard chips |
| `RunnerHomeSnapshot` | `presence`, `stats`, `nearby`, `activeJob?`, `unreadNotifications` | One read for Runner Home |
| `QuickAction` | `id`, `labelResKey` / label | Shortcuts, not categories |

## Persistence (Phase 2)

- Fake in-memory / seeded demo data via `HomeRepository`.
- No Firestore writes for requests yet (sketch only in 07).

## Mapping later

Firestore → domain mappers live in `:core:data` when `requests` collection ships.
