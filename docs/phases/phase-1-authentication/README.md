# Phase 1 — Authentication & User Lifecycle

**Status:** Delivered (with Storage uploads deferred)  
**Goal:** A first-time user can enter Hatli, authenticate with phone OTP, choose a role, complete a role-aware profile setup wizard (incl. address), and recover that profile after reinstall.

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
| Code Review checklist | [10-code-review-checklist.md](./10-code-review-checklist.md) |

**Module owners:** `:feature:auth`, `:feature:profile`, `:feature:home` (post-auth landing), `:core:data` / `:core:domain` / `:core:model`

## Changelog (post-delivery updates)

| Change | Summary |
|--------|---------|
| Multi-step Create Profile | Role-aware wizard: Requester Personal→Address; Runner +Transport→Verification |
| Address persistence | Nested Firestore `address` map + Room flattened columns; shown on Profile |
| Gender | Limited to `Male` / `Female` |
| Safer logout | Cancel Firestore profile listener before `signOut`; tolerate `PERMISSION_DENIED` on observe close |
