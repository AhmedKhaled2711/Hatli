# 08 — Test Cases (Phase 0)

| ID | Scenario | Expected |
|----|----------|----------|
| P0-01 | `./gradlew :app:assembleDebug` | SUCCESS |
| P0-02 | Open app on light system theme | Forest/amber light surfaces; readable text |
| P0-03 | Open app on dark system theme | Dark scaffold; status bar icons contrast correctly |
| P0-04 | Hilt graph | App launches; no missing binding crash on cold start |
| P0-05 | Navigation host | Start destination is auth graph (Splash) |
| P0-06 | Feature isolation | No `implementation(project(":feature:X"))` inside another feature module |
| P0-07 | Design system usage | Feature screens use `Hatli*` components for buttons/fields |
| P0-08 | CI (if enabled) | Push/PR to `develop`/`main` runs assemble + lint + unit tests |
| P0-09 | Module graph | New engineer can list all `:core:*` and `:feature:*` from `settings.gradle.kts` |
| P0-10 | Review question | Team agrees: feature code outside `:app`; wiring still in `:app` |

### Automated targets (baseline)

- Unit tests for pure domain/common where present.
- CI workflow: `.github/workflows/android-ci.yml`.
