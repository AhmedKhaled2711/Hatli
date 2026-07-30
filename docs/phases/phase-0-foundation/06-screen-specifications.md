# 06 — Screen Specifications (Phase 0)

Phase 0 does **not** ship product screens. It ships **platform surfaces** that host later UI.

| Surface | States | Primary actions | Nav in/out |
|---------|--------|-----------------|------------|
| `MainActivity` + `HatliTheme` | System light/dark | Edge-to-edge; snackbar host | Hosts `HatliNavHost` |
| `HatliNavHost` | Start = `auth_graph` | Graph composition | Cross-graph callbacks |
| Design system previews | Light / Dark `@Preview` | Visual QA | N/A |
| Stub feature shells (menu/cart/checkout/orders) | Static title | Back | Reserved routes — **not product IA** |

**Theme / tokens (must remain consistent)**

- Primary forest, secondary amber, full semantic set — see `docs/design/DESIGN_SYSTEM.md`.
- Components available: Primary / Secondary / Text buttons, TextField, Card, Dialog.
- Touch targets ≥ 48dp; 8dp spacing grid.

Product screen specs for Requester/Runner lives in `docs/design/SCREEN_SPECIFICATIONS.md` and Phase 1+.
