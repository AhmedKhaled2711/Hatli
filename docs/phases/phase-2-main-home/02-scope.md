# 02 — Scope (Phase 2)

## In

| Item | Owner |
|------|--------|
| Role-aware bottom navigation | `:app` main shell + `:feature:home` tabs |
| Requester Home (hero, current, recent, quick actions, tips, states) | `:feature:home` |
| Runner Home (presence, stats, nearby top-3, states) | `:feature:home` |
| FAB Create Request (nav to stub) | Main shell |
| Notification bell + badge UI | Main shell / Home top bar |
| Requests list shell (Active / Completed / Cancelled) | `:feature:home` |
| Jobs list shell (Active / History) | `:feature:home` |
| Messages empty placeholder | `:feature:home` |
| Profile tab → existing Profile | `:feature:profile` via shell |
| Typed Home models + `HomeRepository` (fake impl) | `:core:model` / `:core:domain` / `:core:data` |
| Phase docs + design screen specs update | `docs/phases/phase-2-main-home`, `docs/design` |

## Out

- Create Request multi-step flow (real submit)
- Waiting / Tracking / Rating product screens (beyond Home state cards)
- Job accept / pickup / deliver execution
- Chat backend, FCM, notification inbox
- Menu / Cart / Checkout food patterns
- Map, payments, role change

## Assumptions

- Until Firestore `requests` is live, Home uses a **fake repository** behind interfaces.
- Role comes from `ObserveUserProfileUseCase` / `UserProfile.role`.
- One feature closed: Phase 2 = shell + Home + list stubs. Create Request = Phase 3.
