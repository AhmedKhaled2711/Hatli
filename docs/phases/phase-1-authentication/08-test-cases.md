# 08 — Test Cases (Phase 1)

| ID | Scenario | Expected |
|----|----------|----------|
| P1-01 | Fresh install → Splash | Lands on Onboarding (or Login if onboarding already marked) |
| P1-02 | Complete onboarding via Get Started | Login shown; `onboarding_completed` persisted |
| P1-02a | Skip from page 1 or 2 | Login shown; preference persisted; last-page Skip hidden |
| P1-02b | Swipe between pages | Page index + pill indicator update; Next advances |
| P1-02c | Onboarding layout | Illustration hero, centered title/body, dots, sticky CTA visible |
| P1-03 | Valid phone → Send OTP | Navigate to OTP with verification id |
| P1-04 | Invalid phone | Error; stay on Login |
| P1-05 | Correct OTP | Auth session created |
| P1-06 | Wrong OTP | Friendly invalid code message |
| P1-07 | Role Requester → Create Profile | Wizard shows Personal → Address only (2 steps) |
| P1-08 | Role Runner → Create Profile | Wizard shows Personal → Address → Transport → Verification (4 steps); images hidden when flag false |
| P1-09 | Save Requester profile | Firestore `users/{uid}` created with `address` map; navigate Home |
| P1-10 | Save Runner profile | Doc includes address, nationalId, transport, verificationStatus Pending; images null |
| P1-11 | Open Profile | Shows saved names, role, phone, email, gender, address |
| P1-12 | Logout | Cancels Firestore listener; returns to auth flow without crash; requires OTP again |
| P1-13 | **Reinstall + same phone OTP** | **Profile data (incl. address) restored from Firestore** |
| P1-14 | Cold start with valid session + completed profile | Splash → Home |
| P1-15 | Session + incomplete profile | Splash/OTP path → Create Profile |
| P1-16 | Airplane mode on save | NoInternet (or equivalent) error |
| P1-17 | Walking runner without vehicle | Allowed |
| P1-18 | Motorcycle runner without vehicle | Validation error on Transport step |
| P1-19 | Firestore DB not created | Failure message; no silent success |
| P1-20 | Storage flag false | No upload attempts; create still succeeds |
| P1-21 | Gender options | Only Male / Female selectable |
| P1-22 | Address missing city/area/street/building | Step error; cannot advance / submit |
| P1-23 | Wizard Back from Address | Returns to Personal; data retained |

### Automated targets

- ViewModel unit tests for validation / mapping where feasible (fake repositories).
- Use-case tests for start-destination resolution.
- Manual QA mandatory for OTP (device / emulator + Firebase).
