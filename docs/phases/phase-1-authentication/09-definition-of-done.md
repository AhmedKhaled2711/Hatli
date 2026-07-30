# 09 — Definition of Done (Phase 1)

## Product deliverables

- [x] User can log in with phone OTP
- [x] User can create Requester profile
- [x] User can create Runner profile (without image uploads on Spark)
- [x] Profile screen shows stored data
- [x] Profile persisted to Firestore `users/{uid}`
- [x] Local Room cache written
- [x] Reinstall recovery path documented and testable (P1-13)

## Screens

- [x] Splash
- [x] Onboarding
- [x] Login
- [x] OTP
- [x] Role select
- [x] Create Profile
- [x] Profile
- [x] Post-auth Home entry

## Engineering quality

- [x] Navigation matches flows
- [x] Domain free of Firebase/UI
- [x] Repository returns domain models
- [x] UDF on auth/profile ViewModels
- [x] Validation + loading + error handling
- [x] Design system components on forms
- [x] Storage gated; architecture kept for re-enable
- [x] Firestore rules file in repo
- [x] Phase docs complete

## Review criterion

> If I uninstall the app and sign in again with the same phone, all profile data comes back.

**Pass condition:** Firestore document remains; OTP restores Auth UID; app fetches `users/{uid}` and displays Profile.

**Note:** Onboarding “seen” flag may reset after reinstall (local preference). That is acceptable; **profile data** must return.

## Deferred (explicitly not blocking Phase 1 close)

- [ ] ID image / selfie upload (Blaze + flag)
- [ ] Admin verification workflow for Runners
- [ ] Edit profile
- [ ] Account deletion UX

## Phase verdict

✔ **Phase 1 complete** for Auth + User Lifecycle on Spark (Auth + Firestore), with Storage intentionally deferred.
