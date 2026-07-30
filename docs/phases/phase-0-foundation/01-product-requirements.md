# 01 — Product Requirements (Phase 0)

## Problem

Without a deliberate foundation, every new screen becomes a one-off: duplicated themes, SDK calls in Composables, tangled Gradle graphs, and navigation that cannot scale past a demo. Phase 0 removes that tax before product features start.

## Who

- **Primary consumers:** Android engineers adding Hatli features (Requester / Runner marketplace).
- **Secondary:** Future contributors who must onboard from docs + module graph alone.

## Success looks like

- Debug APK builds reliably (`:app:assembleDebug`).
- Multi-module graph is explicit: features depend only on `core/*`.
- Clean Architecture + MVVM + UDF is enforced by structure, not tribal knowledge.
- Hilt provides app-wide DI; ViewModels are injectable.
- Design system owns theme, color, typography, and reusable components.
- Navigation skeleton exists; auth/home/profile graphs compose from the app host.
- Firebase project is configured (`google-services.json`); Auth/Firestore ready for Phase 1.
- Repository lives on GitHub with a clear branch model; CI optionally validates PRs.
- A senior engineer can answer: “Where does a new feature live, and what may it depend on?” in under five minutes.

## Non-goals (high level)

- Shipping Requester/Runner marketplace flows (requests, jobs, tracking).
- Completing every design-system component listed in the UX kit.
- iOS parity or multi-platform shared UI.
- Enabling Firebase Storage on Spark (deferred; gated by feature flag in later phases).
