# 01 — Product Requirements (Phase 2)

## Problem

Post-auth Home is still a foundation “posts” demo. That suggests Talabat/Uber Eats **browse** mental models. Hatli’s Job-to-be-Done is different: the user opens the app to **create a Request** (Requester) or **fulfill a Job** (Runner). Home must lead with that primary action and morph with live work — not restaurant grids or greeting-first chrome.

## Who

| Persona | Job-to-be-Done |
|---------|----------------|
| **Requester** | Get something nearby delivered — start or track a Request in seconds |
| **Runner** | Earn by executing nearby Requests — go online, see jobs, accept, deliver |
| **Returning user** | Land on a role-correct main shell with the right Home state |

## Success looks like

- Requester bottom nav: **Home · Requests · Messages · Profile**
- Runner bottom nav: **Home · Jobs · Messages · Profile** (Jobs, not Requests)
- Requester Home leads with a **Hero CTA** (“اطلب دلوقتي” / Create Request), then Current Request, Recent, Quick Actions, Tips
- Runner Home leads with **Online/Offline**, then earnings / today’s jobs / rating, then Nearby (top 3 + View All)
- Home is **state-based** (empty / searching / accepted / rate; offline / online-empty / nearby / active job)
- Requester has a **FAB (+)** on main tabs that starts Create Request (stub destination in Phase 2)
- Notification **bell + badge** in the top bar (UI; feed stub)
- No restaurant browse, menus, cart, or food-category Home

## Non-goals (high level)

- Full Create Request wizard (details / locations / pay) — Phase 3+
- Live map tracking, Accept → Pickup → Delivered execution UI
- Real-time chat / FCM
- Payments, surge pricing
- Replacing Profile auth lifecycle (Phase 1 remains source of truth)
