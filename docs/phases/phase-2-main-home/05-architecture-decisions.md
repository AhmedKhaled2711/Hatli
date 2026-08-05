# 05 — Architecture Decisions (Phase 2)

| Decision | Why | Trade-offs | Scalability |
|----------|-----|------------|-------------|
| Main shell in `:app` + tab content from `:feature:home` / `:feature:profile` | Avoid feature→feature deps; app wires graphs | App knows tab set | New tabs = app route + feature screen |
| Replace posts demo Home | JTBD ≠ foundation demo | Lose posts demo UI | Posts use cases remain for other demos if needed |
| Single `HomeRepository` for both roles | One gateway for Home snapshots | Fat interface | Split later if Jobs module grows |
| Fake repository first | Unblocks UI without Firestore schema lock | Demo data only | Swap binding to Firestore impl |
| Role from `UserProfile` | Already durable from Phase 1 | Shell waits on profile Flow | Same source as Profile screen |
| Messages/Requests/Jobs inside `:feature:home` for Phase 2 | One feature finish; stubs are thin | Module grows | Extract `:feature:requests` / `:feature:jobs` / `:feature:chat` in Phase 3+ |
| FAB only for Requester | Runner primary action is Go Online / Accept | Shell branches on role | Clear JTBD |

## Layering

UI → ViewModel → UseCase → `HomeRepository` → Fake / (later) Firestore.

No Firebase in UI/domain. Design system components only in feature UI.
