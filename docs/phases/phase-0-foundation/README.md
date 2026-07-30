# Phase 0 — Foundation

**Status:** Complete  
**Goal:** Establish a scalable Android platform so every subsequent feature plugs in with clear boundaries, without re-litigating architecture.

**Review gate**

> Can I start any Feature without touching the App module?

**Answer (honest):** **Almost — with one composition-root exception.** Feature UI, ViewModels, domain, and data live outside `:app`. Registering a new feature still requires `:app` to depend on the module and wire its navigation graph in `HatliNavHost`. That is intentional for a single-activity Compose app. Feature *implementation* does not live in `:app`.

Phase 0 is considered **closed** when the checklist in `09-definition-of-done.md` is green.

| Artifact | File |
|----------|------|
| Product requirements | [01-product-requirements.md](./01-product-requirements.md) |
| Scope | [02-scope.md](./02-scope.md) |
| User flows | [03-user-flows.md](./03-user-flows.md) |
| Data models | [04-data-models.md](./04-data-models.md) |
| Architecture decisions | [05-architecture-decisions.md](./05-architecture-decisions.md) |
| Screen specifications | [06-screen-specifications.md](./06-screen-specifications.md) |
| API / Firebase design | [07-api-firebase-design.md](./07-api-firebase-design.md) |
| Test cases | [08-test-cases.md](./08-test-cases.md) |
| Definition of Done | [09-definition-of-done.md](./09-definition-of-done.md) |
| Code review checklist | [10-code-review-checklist.md](./10-code-review-checklist.md) |

**Related:** [Design system](../../design/DESIGN_SYSTEM.md) · [Screen specs (product UX)](../../design/SCREEN_SPECIFICATIONS.md)
