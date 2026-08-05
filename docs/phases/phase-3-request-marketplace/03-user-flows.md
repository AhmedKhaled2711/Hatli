# 03 — User Flows (Phase 3)

## Happy path — Create (Requester)

1. Home / FAB → Category screen (Shopping | Pickup | Bills | Custom)
2. Branch wizard steps → Review
3. Submit → Firestore `Pending` → pop to Home
4. Home shows Current Request (Searching / Pending)

### Per category

| Category | Steps |
|----------|--------|
| Shopping | Shop name (opt) → items → notes → budget → fee → review |
| Pickup | Pickup addr → dropoff → item desc → fee → review |
| Bills | Bill type → account/ref → amount → fee → review |
| Custom | Title → body → budget (opt) → fee → review |

## Happy path — Accept (Runner)

1. Runner Home / Jobs listens `status == Pending`
2. Tap Accept on a card
3. Transaction: only if still Pending and `runnerId == null`
4. `runnerId = uid`, `status = Accepted`
5. Requester Home listener updates Current Request to Accepted

## Edge / failure paths

1. Validation fail → stay on step, show field errors
2. Offline / Firestore error → snackbar, keep wizard draft in VM
3. Accept race (already taken) → friendly message, list refreshes
4. Empty Pending → empty state on Runner
5. Cancel mid-wizard → discard, back to Home
