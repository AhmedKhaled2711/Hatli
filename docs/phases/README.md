# Hatli Phase Documentation

Senior Android delivery packs for Hatli. Each phase follows the artifact set in [PHASE_TEMPLATE.md](./PHASE_TEMPLATE.md) and the execution order in `.cursor/rules/phase-delivery-process.mdc`.

## Phase status

| Phase | Name | Status | Folder |
|-------|------|--------|--------|
| **0** | Foundation | ✔ Complete | [phase-0-foundation](./phase-0-foundation/) |
| **1** | Authentication & User Lifecycle | ✔ Complete (Storage deferred) | [phase-1-authentication](./phase-1-authentication/) |
| **2** | JTBD Main Home & Shell | ✔ Complete (Create Request deferred) | [phase-2-main-home](./phase-2-main-home/) |
| 3+ | Create Request / Jobs execution / Chat | Not started | — |

## Design references (cross-cutting)

| Doc | Purpose |
|-----|---------|
| [../design/DESIGN_SYSTEM.md](../design/DESIGN_SYSTEM.md) | Tokens, components, a11y, light/dark |
| [../design/SCREEN_SPECIFICATIONS.md](../design/SCREEN_SPECIFICATIONS.md) | Product UX screen inventory (Requester/Runner) |

## Phase 0 — Foundation (summary)

**Goal:** Build a platform where any later Feature plugs in cleanly.

**Delivered:** Multi-module Clean Architecture, MVVM+UDF, Hilt, Version Catalog, Build Logic, Design System, Core modules, Navigation skeleton, Theme/Typography/Color, Firebase config, GitHub + Git Flow, CI.

**Review question:** *Can I start any Feature without touching the App module?*  
**Answer:** Feature **code** yes; feature **registration** (Gradle + `HatliNavHost`) still touches `:app` by design. Phase closed under that rule.

## Phase 1 — Authentication & User Lifecycle (summary)

**Goal:** First-time and returning users can authenticate and own a durable profile.

**Delivered:** Splash, FlexBox-inspired Onboarding (pager + illustration + dots), Login, OTP, Role, multi-step Create Profile (address + role-aware steps), Profile, Firestore `users/{uid}` (+ nested address), Room cache, Auth phone OTP, safer logout (cancel listeners before `signOut`).

**Review criterion:** Uninstall → sign in with same phone → profile data (incl. address) returns from Firestore.

**Deferred:** ID image / selfie uploads until Blaze (`HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED`); map / current-location pin for address.

## Phase 2 — JTBD Main Home & Shell (summary)

**Goal:** Post-auth shell and Home that match Request/Job JTBD — not restaurant browse.

**Delivered:** Role-aware bottom nav (Requests vs Jobs), Requester Home (Hero CTA first + current/recent/quick actions/tips), Runner Home (Online/Offline + stats + nearby top 3), FAB + notification badge, Requests/Jobs list shells, Messages empty, Profile tab, `FakeHomeRepository` + domain models, phase docs.

**Review criterion:** Requester and Runner each see the correct tabs and a Home that leads with Create or Go Online.

**Deferred:** Create Request wizard, tracking, accept/pickup/deliver, real chat/FCM (Phase 3+).

## How to use these docs

1. Start a phase at **Product Requirements** — do not jump to Compose.
2. Lock **Scope** (In/Out) before coding.
3. Implement data → domain → presentation.
4. Close only when **Definition of Done** is binary-complete.
5. Keep one feature finished before starting the next.
