# Hatli Phase Documentation

Senior Android delivery packs for Hatli. Each phase follows the artifact set in [PHASE_TEMPLATE.md](./PHASE_TEMPLATE.md) and the execution order in `.cursor/rules/phase-delivery-process.mdc`.

## Phase status

| Phase | Name | Status | Folder |
|-------|------|--------|--------|
| **0** | Foundation | ✔ Complete | [phase-0-foundation](./phase-0-foundation/) |
| **1** | Authentication & User Lifecycle | ✔ Complete (Storage deferred) | [phase-1-authentication](./phase-1-authentication/) |
| 2+ | Marketplace features | Not started | — |

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

**Delivered:** Splash, Onboarding, Login, OTP, Role, Create Profile, Profile, Firestore `users/{uid}`, Room cache, Auth phone OTP.

**Review criterion:** Uninstall → sign in with same phone → profile data returns from Firestore.

**Deferred:** ID image / selfie uploads until Blaze (`HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED`).

## How to use these docs

1. Start a phase at **Product Requirements** — do not jump to Compose.
2. Lock **Scope** (In/Out) before coding.
3. Implement data → domain → presentation.
4. Close only when **Definition of Done** is binary-complete.
5. Keep one feature finished before starting the next.
