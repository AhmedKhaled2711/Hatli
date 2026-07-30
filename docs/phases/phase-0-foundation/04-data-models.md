# 04 — Data Models (Phase 0)

Phase 0 introduces **platform models** used by later phases. Auth/profile domain models are owned by Phase 1 but the modules that host them exist here.

| Model / type | Module | Fields / notes |
|--------------|--------|----------------|
| `Result` | `:core:common` | `Success` / `Error` / `Loading` for use-case returns |
| `AppError` | `:core:common` | Typed failures (network, auth, validation, Firebase) |
| `HatliFeatureFlags` | `:core:common` | Compile-time switches (e.g. Storage uploads) |
| `UiState` / `UiEvent` / `UiEffect` | `:core:ui` | UDF contracts |
| `Destination` / `Routes` | `:core:ui` | Navigation route constants |
| `Post` (foundation demo) | `:core:model` | Temporary home demo content — not product domain |
| Room `PostEntity` | `:core:database` | Local cache for network demo |
| Design tokens | `:core:designsystem` | `HatliColors`, `HatliExtendedColors`, spacing, radius |

**Relationship rule:** Domain models in `:core:model` have **zero** Android / Firebase imports. Persistence shapes live in `:core:database` / DTOs in `:core:data`.
