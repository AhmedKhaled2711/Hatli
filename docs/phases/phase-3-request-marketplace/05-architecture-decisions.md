# 05 — Architecture Decisions (Phase 3)

| Decision | Why | Trade-offs | Scalability |
|----------|-----|------------|-------------|
| Firestore Snapshot Listeners | Realtime MVP without sockets | Cost per listener; offline via SDK cache | Later: Cloud Functions + FCM |
| Collection `requests` | Not an order yet | Rename later if commerce expands | Stable id space |
| High-level categories only | Places ≠ categories | Less specific first tap | Shop name / tags later |
| Suggested fixed fee (35) | No maps in slice | Fee not distance-accurate | Geo + pricing service later |
| `:feature:request` module | Feature isolation; home stays shell | Extra Gradle module | Wizard grows without touching home |
| Accept via transaction / conditional update | Prevent double-accept | Slightly more complex write | Same pattern at scale |
| Home composed from RequestRepository | Single source of truth | HomeRepository may thin-wrap | Keep UI free of Firestore |

## Constraints (Phase 0)

- No Firebase in UI / domain
- Features → `core/*` only
- MVVM + UDF
