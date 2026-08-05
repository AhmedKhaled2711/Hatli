# 03 — User Flows (Phase 2)

## Requester — enter main

1. Auth complete → Main shell (Requester tabs).
2. Home shows state from repository (empty / searching / accepted / rate_prompt).
3. Empty: Hero CTA → Create Request stub.
4. Searching / Accepted: Current Request card → open Requests detail stub (or Requests tab).
5. FAB (+) from any tab → Create Request stub.
6. Requests tab → filter Active / Completed / Cancelled.
7. Messages → empty “Chat with Runner soon”.
8. Profile → Phase 1 Profile.

## Runner — enter main

1. Auth complete → Main shell (Runner tabs).
2. Home: Offline → encourage Go Online; Online empty → empty nearby; Nearby → top 3 + View All; Active job → focus card.
3. Toggle Online/Offline (persisted in fake repo / session for demo).
4. Jobs tab → Active / History.
5. Messages / Profile same pattern as Requester (Messages empty).

## Edge / failure

| Case | Behavior |
|------|----------|
| Profile null / loading | Shell shows loading until role known |
| Fake repo error | Friendly empty + retry on Home |
| Create stub | Snackbar or placeholder screen “Coming in Phase 3” |
| No nearby jobs | Online-empty copy, keep Online toggle |

## Happy path diagrams

### Requester (product loop — Phase 2 shows Home states only)

Home → Create (stub) → Waiting state on Home → Accepted state → Completed / Rate state

### Runner

Home Offline → Go Online → Nearby list → View All (Jobs) → (Accept deferred Phase 3+)
