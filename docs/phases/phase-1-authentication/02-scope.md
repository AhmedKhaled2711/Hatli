# 02 — Scope (Phase 1)

## In

### Screens

| Screen | Module |
|--------|--------|
| Splash | `:feature:auth` |
| Onboarding (3 pages, FlexBox-inspired) | `:feature:auth` |
| Login (phone) | `:feature:auth` |
| OTP | `:feature:auth` |
| Role select (Requester / Runner) | `:feature:auth` |
| Create Profile (multi-step wizard) | `:feature:auth` |
| Profile (view + logout) | `:feature:profile` |
| Home (post-auth landing) | `:feature:home` |

### Backend / persistence

- Firebase Auth — phone OTP
- Cloud Firestore — `users/{uid}` profile document
- Room — local profile cache
- Session preferences — onboarding completed, pending role

### Onboarding UX (in)

- 3 swipeable pages (`HorizontalPager`): Request or deliver → Track every step → Pay and get paid simply
- Skip (hidden on last page), Next / Get Started, pill page indicators
- Illustration hero (soft forest wash + icon badges); Hatli brand tokens (not stock FlexBox palette)
- Completing Skip or Get Started sets local `onboarding_completed`

### Requester profile fields

- **Personal step:** First name, Last name, Gender (`Male` \| `Female`), Email, Phone (read-only from Auth)
- **Address step:** City, Area, Street, Building (**required**); Floor, Apartment, Notes (optional). Lat/lng reserved for future maps (null now).

### Runner profile fields

- All Requester steps, plus:
  - **Transport step:** Transport type (**required**); Vehicle info (**required** except Walking)
  - **Verification step:** National ID (**required**); ID Image / Selfie (**optional placeholders** while Storage flag is false)
  - `verificationStatus = Pending` on create

### Logout safety

- Cancel / stop Firestore profile observation before Auth `signOut` (avoids listener `PERMISSION_DENIED` crashes).

## Out

- Firebase Storage uploads (gated: `HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED = false`)
- Map / Current Location pin for address (manual entry only)
- Gender options beyond Male / Female
- Edit profile deep flows (basic view + logout only)
- Multi-factor beyond SMS OTP
- Account deletion UX (repository may support delete; not productized)
- Bottom navigation IA for marketplace

## Assumptions

- Firestore database is **created in Firebase Console** (app does not provision it).
- Phone numbers include country code (e.g. `+20…`).
- Role is chosen once before create profile; stored as pending until profile write.
- Spark plan is acceptable for Auth + Firestore.
