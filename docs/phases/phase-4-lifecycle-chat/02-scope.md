# 02 — Scope (Phase 4)

## In

- Status machine + `Purchased`; timeline fields; denormalized names
- Home Current Request/Job pins + next CTA
- Request Detail + cancel reason
- `:feature:chat` messages subcollection
- Confirm delivery + Rate Runner
- Rules/indexes updates
- Phase docs + screen specs

## Out

- FCM (4b), maps/GPS, image chat, payments, auto-relist, runner rating requester

## Assumptions

- Multiple actives allowed; Home pins latest `updatedAt`
- Runner advances to Delivered; Requester confirms Completed
- Chat only after Accept
