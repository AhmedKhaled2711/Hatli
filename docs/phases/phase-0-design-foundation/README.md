# Phase 0 — Design Foundation

## Product requirements (design scope)

**Problem:** Hatli needs a coherent Android-first Material 3 UI that supports two roles (Requester / Runner) without inheriting food-delivery commerce IA from community UI kits.

**Who:** New and returning Requesters and Runners on Android phones (RTL-capable markets).

**Success looks like:** Engineers can implement screens from specs using `:core:designsystem` only; every listed screen has layout, components, states, and UX rationale.

**Non-goals:** Implementing all Compose screens in this phase; building Menu/Cart/Checkout food modules; copying Figma kits pixel-for-pixel.

## Scope

### In

- Semantic color system (Primary → Disabled) + dark mode mapping in code
- Design system documentation (type, space, components, a11y)
- Full screen specifications for Auth + Requester + Runner + Profile
- Reference kit analysis (keep / improve / drop)

### Out

- New feature modules for requests/jobs (later phases)
- Custom font binary packaging (scale reserved; family TBD)
- iOS design parity

## Artifacts

| Artifact | Path |
|----------|------|
| Design system | `docs/design/DESIGN_SYSTEM.md` |
| Screen specs | `docs/design/SCREEN_SPECIFICATIONS.md` |
| Colors / theme | `core/designsystem/.../Color.kt`, `Theme.kt` |
| Overview canvas | `.cursor/.../canvases/hatli-android-design.canvas.tsx` |
