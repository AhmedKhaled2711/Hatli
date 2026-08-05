# 07 — API / Firebase Design (Phase 2)

## Collections / paths (sketch — not implemented)

```
requests/{requestId}
  requesterId: string
  runnerId: string?
  title: string
  status: SearchingRunner | Accepted | InProgress | Completed | Cancelled
  offerAmount: number?
  pickupLabel / dropoffLabel: string?
  createdAt / updatedAt: timestamp
```

## Home reads (future)

| Role | Query |
|------|--------|
| Requester | Active where `requesterId == uid` + recent limit 5 |
| Runner | Nearby open requests (geo later) limit 3; active where `runnerId == uid` |

## Auth & security notes

- Requester reads/writes own requests; Runner reads open + assigned.
- Phase 2 uses **fake data only** — no security rules change required.

## Offline / sync

- Fake is in-memory.
- Later: Room cache of `RequestSummary` + Firestore listeners.
