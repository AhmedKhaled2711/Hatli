# 08 — Test Cases (Phase 1)

| ID | Scenario | Expected |
|----|----------|----------|
| P1-01 | Fresh install → Splash | Lands on Onboarding (or Login if onboarding already marked) |
| P1-02 | Complete onboarding | Login shown; preference persisted |
| P1-03 | Valid phone → Send OTP | Navigate to OTP with verification id |
| P1-04 | Invalid phone | Error; stay on Login |
| P1-05 | Correct OTP | Auth session created |
| P1-06 | Wrong OTP | Friendly invalid code message |
| P1-07 | Role Requester → Create Profile | Runner fields hidden |
| P1-08 | Role Runner → Create Profile | Runner fields visible; images hidden when flag false |
| P1-09 | Save Requester profile | Firestore `users/{uid}` created; navigate Home |
| P1-10 | Save Runner profile | Doc includes nationalId, transport, verificationStatus Pending; images null |
| P1-11 | Open Profile | Shows saved names, role, phone, email |
| P1-12 | Logout | Returns to auth flow; requires OTP again |
| P1-13 | **Reinstall + same phone OTP** | **Profile data restored from Firestore** |
| P1-14 | Cold start with valid session + completed profile | Splash → Home |
| P1-15 | Session + incomplete profile | Splash/OTP path → Create Profile |
| P1-16 | Airplane mode on save | NoInternet (or equivalent) error |
| P1-17 | Walking runner without vehicle | Allowed |
| P1-18 | Motorcycle runner without vehicle | Validation error |
| P1-19 | Firestore DB not created | Failure message; no silent success |
| P1-20 | Storage flag false | No upload attempts; create still succeeds |

### Automated targets

- ViewModel unit tests for validation / mapping where feasible (fake repositories).
- Use-case tests for start-destination resolution.
- Manual QA mandatory for OTP (device / emulator + Firebase).
