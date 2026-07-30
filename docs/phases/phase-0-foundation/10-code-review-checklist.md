# 10 — Code Review Checklist (Phase 0)

Use on every PR that touches foundation or adds a module.

- [ ] No `:feature` → `:feature` dependencies
- [ ] No Firebase / Room / Retrofit in UI or domain modules
- [ ] No business logic in Composables (events → ViewModel)
- [ ] New screens use design-system components (no ad-hoc Material buttons)
- [ ] Errors mapped to `AppError` / friendly `userMessage()`
- [ ] UDF: `UiState` / `UiEvent` / `UiEffect` + `BaseViewModel` where applicable
- [ ] Route constants live in `:core:ui` (`Routes`), not magic strings in features
- [ ] New feature registered in `settings.gradle.kts`, `app/build.gradle.kts`, `HatliNavHost`
- [ ] Version changes go through Version Catalog
- [ ] Convention plugins preferred over copy-pasted Gradle blocks
- [ ] Light and dark still look correct after theme token changes
- [ ] No secrets committed (API keys only via `google-services` / CI secrets)
- [ ] Dead stubs / unused modules called out intentionally (or removed)
- [ ] Docs updated if architecture or module graph changed
