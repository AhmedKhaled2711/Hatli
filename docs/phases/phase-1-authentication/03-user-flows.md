# 03 — User Flows (Phase 1)

## Happy path — new user (Requester)

1. Cold start → **Splash** resolves destination.
2. Onboarding incomplete → **Onboarding** (Skip or finish) → marks onboarding done → **Login**.
3. User enters phone → **Send OTP** → **OTP** screen.
4. User enters 6-digit code → Auth succeeds.
5. No completed profile → **Role select** → Requester → **Create Profile**.
6. User fills shared fields → Save → Firestore + Room write → **Home**.
7. Optional: Home → **Profile** → view data → Logout → Auth graph.

## Happy path — new user (Runner)

Same through OTP, then:

5. Role = Runner → Create Profile shows Runner section.
6. National ID + transport (+ vehicle if not Walking) → Save.
7. `verificationStatus = Pending`; image URLs null while Storage disabled → Home.

## Happy path — returning user (reinstall)

1. Install app → Splash.
2. Onboarding may show again if local preference cleared (reinstall clears DataStore) → complete/skip.
3. Login + OTP with **same phone** → Firebase Auth UID restored / same account.
4. Splash/OTP resolution loads Firestore `users/{uid}` with `profileCompleted == true` → **Home**.
5. Profile screen shows previously saved fields (**review criterion**).

## Happy path — session still valid

1. Splash detects signed-in user + completed profile → **Home** (skip Login).

## Edge / failure paths

| Path | Behavior |
|------|----------|
| Invalid phone format | Inline / mapped validation error |
| Wrong OTP | `InvalidOtp` friendly message; allow retry |
| OTP expired / session expired | Prompt resend / restart verification |
| Network offline on send/verify | `NoInternet` message |
| Rate limited | Too-many-attempts message |
| Create profile validation fail | Field-level / form error; stay on screen |
| Firestore missing (DB not created) | Firebase failure surfaced; engineer must create DB in Console |
| Signed in, profile incomplete | Route to Create Profile (pending role or default Requester) |
| Logout | Clear session-sensitive local state as implemented; navigate to auth graph |

## Navigation map

```text
auth_graph (start: splash)
  splash → onboarding | login | create_profile | home_graph
  onboarding → login
  login → otp/{verificationId}/{phone}
  otp → role_select | home_graph | create_profile
  role_select → create_profile
  create_profile → home_graph

home_graph → profile_graph
profile_graph → auth_graph (logout)
```
