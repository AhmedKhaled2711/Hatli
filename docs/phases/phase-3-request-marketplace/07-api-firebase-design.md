# 07 — API / Firebase Design (Phase 3)

## Collection

```
requests/{requestId}
  requestId: string
  requesterId: string
  runnerId: string | null
  category: Shopping | Pickup | Bills | Custom
  status: Pending | Accepted | InProgress | OnTheWay | Delivered | Completed | Cancelled
  title: string
  description: string
  budget: number | null
  deliveryFee: number
  pickupLabel: string | null
  dropoffLabel: string | null
  shopping: { shopName?, itemsText, notes? } | null
  pickup: { itemDescription } | null
  bills: { billType, accountRef, amount } | null
  custom: { } | null
  createdAt: timestamp
  updatedAt: timestamp
```

## Queries

| Use | Query |
|-----|--------|
| Requester Home / lists | `requesterId == uid` orderBy `updatedAt` desc |
| Marketplace Pending | `status == Pending` orderBy `createdAt` desc limit 20 |
| Runner active | `runnerId == uid` where status in active set |

## Security rules (summary)

- Create: auth && `requesterId == uid` && `status == Pending` && `runnerId == null`
- Read: requester owns OR status Pending OR runnerId == uid
- Accept update: auth && resource Pending && runnerId null → set runnerId + Accepted
- Cancel: requester while Pending (optional MVP)

## Indexes

- `status` ASC + `createdAt` DESC
- `requesterId` ASC + `updatedAt` DESC
- `runnerId` ASC + `updatedAt` DESC

## Offline

Firestore SDK persistence; UI shows last snapshot / error via `AppError`.
