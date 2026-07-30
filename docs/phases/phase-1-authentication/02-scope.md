# 02 — Scope (Phase 1)

## In

### Screens

| Screen | Module |
|--------|--------|
| Splash | `:feature:auth` |
| Onboarding (3 pages) | `:feature:auth` |
| Login (phone) | `:feature:auth` |
| OTP | `:feature:auth` |
| Role select (Requester / Runner) | `:feature:auth` |
| Create Profile | `:feature:auth` |
| Profile (view + logout) | `:feature:profile` |
| Home (post-auth landing) | `:feature:home` |

### Backend / persistence

- Firebase Auth — phone OTP
- Cloud Firestore — `users/{uid}` profile document
- Room — local profile cache
- Session preferences — onboarding completed, pending role

### Requester profile fields

- First name, Last name, Gender, Email, Phone (read-only from Auth)

### Runner profile fields

- All Requester fields, plus:
  - National ID (**required**)
  - ID Image (**optional placeholder** while Storage flag is false)
  - Selfie (**optional placeholder** while Storage flag is false)
  - Transport type (**required**)
  - Vehicle info (**required** except Walking)
  - `verificationStatus = Pending` on create

## Out

- Firebase Storage uploads (gated: `HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED = false`)
- Edit profile deep flows (basic view + logout only)
- Multi-factor beyond SMS OTP
- Account deletion UX (repository may support delete; not productized)
- Bottom navigation IA for marketplace

## Assumptions

- Firestore database is **created in Firebase Console** (app does not provision it).
- Phone numbers include country code (e.g. `+20…`).
- Role is chosen once before create profile; stored as pending until profile write.
- Spark plan is acceptable for Auth + Firestore.
