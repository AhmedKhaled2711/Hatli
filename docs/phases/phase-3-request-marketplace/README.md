# Phase 3 — Request Creation & Marketplace (Slice B)

**Status:** ✔ Complete  
**Depends on:** Phase 2 Main Shell (FAB / Home stubs)

## Goal

Assisted **Create Request** wizard (Shopping / Pickup / Bills / Custom) writes to Firestore `requests`. Runners see `Pending` jobs via Snapshot Listeners and **Accept**; Requester Home updates live. No sockets, no maps.

## Slice B

| In | Out |
|----|-----|
| Create wizard + Firestore write (`Pending`) | Full tracking UI (OnTheWay / Delivered screens) |
| Runner Pending feed + Accept | Maps / geo fee / FCM / chat / payments |
| Live Home updates via listeners | Place-specific category cards (Butcher, …) |

## Artifacts

| # | Doc |
|---|-----|
| 01 | [Product requirements](./01-product-requirements.md) |
| 02 | [Scope](./02-scope.md) |
| 03 | [User flows](./03-user-flows.md) |
| 04 | [Data models](./04-data-models.md) |
| 05 | [Architecture decisions](./05-architecture-decisions.md) |
| 06 | [Screen specifications](./06-screen-specifications.md) |
| 07 | [API / Firebase](./07-api-firebase-design.md) |
| 08 | [Test cases](./08-test-cases.md) |
| 09 | [Definition of Done](./09-definition-of-done.md) |
| 10 | [Code review checklist](./10-code-review-checklist.md) |

## Review criterion

Requester creates any category → Runner sees Pending without refresh → Accept → Requester Home shows Accepted live.
