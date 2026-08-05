# 01 — Product Requirements (Phase 3)

## Problem

Phase 2 shipped a JTBD shell, but Create Request is a stub and Home still uses fake data. Hatli’s core loop — Requester posts a local request, nearby Runners accept, both sides update live — does not exist yet.

## Who

| Persona | Job-to-be-Done |
|---------|----------------|
| **Requester** | Describe what they need via guided steps and get a Runner without refreshing |
| **Runner** | See open (`Pending`) requests and accept one to earn |

## Success looks like

- Assisted wizard: category cards first (Shopping / Pickup / Bills / Custom) — never a blank free-text first screen
- Submit creates `requests/{id}` with `status = Pending`
- Runner Home / Jobs shows Pending via Snapshot Listener (no pull-to-refresh)
- Accept sets `runnerId` + `Accepted`; Requester Home Current Request updates live
- Suggested delivery fee (default 35 EGP), editable; no maps

## Non-goals (high level)

- Sockets, FCM inbox, chat
- Maps / geo queries / distance-based fee
- Butcher / Bakery / Supermarket as top-level categories
- Full execution UI after Accept (OnTheWay / Delivered screens)
- Payments
