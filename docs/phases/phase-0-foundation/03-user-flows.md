# 03 — User Flows (Phase 0)

Phase 0 has **no end-user product journeys**. Flows below are **engineer / platform** flows.

## Happy path — add a new feature module

1. Create `:feature:<name>` with `hatli.android.feature` convention plugin.
2. Implement presentation (Compose + ViewModel + Contract) depending only on `core/*`.
3. Put domain contracts in `:core:domain` if shared; feature-specific use cases may live next to feature only if they do not pull in other features.
4. Implement data in `:core:data` (or feature-local data only if explicitly allowed later — current rule: repositories live in `:core:data`).
5. Expose a `NavGraphBuilder` extension (e.g. `fooGraph(...)`).
6. Register module in `settings.gradle.kts`, `app/build.gradle.kts`, and `HatliNavHost`.
7. Use design-system components; map errors through `AppError` / `userMessage()`.
8. Verify assemble + navigation entry.

## Happy path — apply theme

1. Wrap app content in `HatliTheme(themeMode = FollowSystem)`.
2. Screens consume `MaterialTheme.colorScheme` and `HatliTheme.extendedColors`.
3. System bars follow light/dark via theme SideEffect + edge-to-edge.

## Edge / failure paths

1. Feature depends on another feature → reject in review; extract shared code to `core/*`.
2. Firebase/Room imported in a Composable or UseCase → reject; keep SDKs in `:core:data`.
3. Raw Material `Button` in feature UI → replace with `Hatli*` components.
4. CI fails assemble/lint/tests → fix before merge to `develop`/`main`.
