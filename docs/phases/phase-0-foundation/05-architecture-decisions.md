# 05 — Architecture Decisions (Phase 0)

| Decision | Why | Trade-offs | Scalability |
|----------|-----|------------|-------------|
| Multi-module by layer + feature | Isolate compile scope; enforce dependency rules; parallelize work | More Gradle ceremony; slower clean builds | New features = new modules without rewriting app core |
| Clean Architecture (UI → Domain → Data) | Testable domain; swap Firebase later without rewriting screens | Indirection; more types | Use cases stay stable as product grows |
| MVVM + UDF (`UiState`/`UiEvent`/`UiEffect`) | Predictable UI; one-shot effects (nav, snackbar) leave state | Slight boilerplate per screen | Same pattern on every feature — junior-friendly |
| Hilt | Standard Android DI; ViewModel factories; test overrides | Annotation processor cost | Scales with modules via `@InstallIn` |
| Version Catalog + build-logic | One version truth; shared Android/Compose/Hilt conventions | Convention plugins to maintain | Onboarding cost drops; fewer “works on my machine” Gradle drifts |
| Design system module | Brand + a11y consistency; features cannot invent rogue buttons | Component backlog to fill | Theme changes propagate in one place |
| App as composition root | Explicit NavHost; clear start destinations | Touching `:app` when adding a feature graph | Avoids magic auto-wiring bugs |
| No feature → feature deps | Prevents spaghetti and circular graphs | Shared UX extracted to designsystem/core | Features ship independently |
| Firebase in `:core:data` only | Domain/UI stay pure | Data module grows | Can add fakes for instrumentation tests |
| Dynamic color off by default | Brand (forest/amber) must remain trustworthy | Less Material You personalization | Optional later via `HatliTheme(dynamicColor = true)` |
| Storage behind feature flag | Spark plan has no Storage; avoid runtime 404s | Runner KYC images deferred | Flip one constant after Blaze |

## Dependency diagram

```text
:feature:* ──► :core:ui ──► :core:designsystem
     │              │
     ├──────────────┴──► :core:domain ──► :core:model
     │                        ▲
     └──────── (via Hilt) ─── :core:data ──► :core:network
                                    │         :core:database
                                    └── Firebase / Room / HTTP
:app ──► all features + data (composition + DI)
```

## Review question — deep answer

**“Can I start any Feature without touching the App module?”**

| Layer | Touch `:app`? |
|-------|----------------|
| Screens, ViewModels, contracts | No |
| Domain use cases / models | No |
| Repository implementations | No (`:core:data`) |
| Design system components | No |
| **Include module in Gradle** | **Yes** (`settings` + `app` deps) |
| **Register nav graph** | **Yes** (`HatliNavHost`) |

So: **feature code is module-local; app remains the wiring board.** That is an accepted Phase 0 trade-off. Auto-discovery can be a later ADR if module count becomes painful.
