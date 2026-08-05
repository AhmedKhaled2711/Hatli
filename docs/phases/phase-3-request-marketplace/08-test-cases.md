# 08 — Test Cases (Phase 3)

| ID | Scenario | Expected |
|----|----------|----------|
| P3-01 | Requester completes Shopping wizard | Doc in `requests`, status Pending, appears on Home |
| P3-02 | Pickup / Bills / Custom submit | Correct category payload fields set |
| P3-03 | Validation missing items | Cannot advance; error shown |
| P3-04 | Runner online sees new Pending | Appears without pull-to-refresh |
| P3-05 | Runner Accepts | runnerId set, status Accepted |
| P3-06 | Second Runner Accepts same | Fails gracefully; list updates |
| P3-07 | Requester after Accept | Current Request shows Accepted live |
| P3-08 | Empty marketplace | Runner empty state |
| P3-09 | Submit offline | Error message; can retry |
| P3-10 | Cancel wizard | Returns Home; no Firestore write |
| P3-11 | Stub removed | FAB/Hero open real wizard |
| P3-12 | Fee default 35 editable | Submitted deliveryFee matches edit |
