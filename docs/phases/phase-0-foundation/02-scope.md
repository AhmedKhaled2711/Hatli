# 02 — Scope (Phase 0)

## In

### Technical platform

- Multi-module Android project (`:app`, `:core:*`, `:feature:*`)
- Convention plugins / build-logic
- Gradle Version Catalog (`gradle/libs.versions.toml`)
- Clean Architecture layering (presentation → domain → data)
- MVVM + Unidirectional Data Flow (`UiState` / `UiEvent` / `UiEffect`)
- Hilt
- Navigation Compose skeleton + typed route constants
- Design system module (theme, color, typography, shapes, spacing, base components)
- Core modules: `common`, `ui`, `model`, `domain`, `data`, `network`, `database`, `designsystem`
- Firebase configuration for the Android app
- GitHub repository + Git Flow (`main` / `develop`)
- Optional CI (assemble, lint, unit tests)

### Visual / UX foundation

- Light and dark Material 3 schemes
- Semantic color tokens (Primary → Disabled + extended success/warning/info)
- Typography scale
- Iconography via Material Icons Extended (design system dependency)

## Out

- Authentication product flows (Phase 1)
- Request / delivery domain features
- Full component library (chips, sheets, timeline, empty states — may land incrementally)
- Forced AppCompat DayNight XML parent (Compose theme is source of truth; XML is window chrome only)
- Auto-discovery of feature graphs (no SPI); app remains composition root

## Assumptions

- Single Activity + Compose Navigation.
- One mobile app serving both Requester and Runner roles (role branching later).
- Firebase is the primary backend for auth and profile persistence.
- Spark plan initially: Firestore + Auth yes; Storage deferred.

## Module inventory (delivered)

| Module | Responsibility |
|--------|----------------|
| `:app` | Application, Hilt root, NavHost, Firebase plugins |
| `:core:common` | Result, AppError, dispatchers, feature flags |
| `:core:model` | Pure domain models shared across layers |
| `:core:domain` | Repository interfaces, use cases |
| `:core:data` | Repository impls, Firebase/Room/network sources |
| `:core:network` | HTTP client surface |
| `:core:database` | Room DB / DAOs / entities |
| `:core:ui` | BaseViewModel, UDF contracts, routes, navigator |
| `:core:designsystem` | HatliTheme, tokens, Compose components |
| `:feature:*` | Vertical slices (auth, home, profile, stubs…) |
| `build-logic` | Convention plugins |
