# 06 — Screen Specifications (Phase 1)

| Screen | States | Primary actions | Nav in/out |
|--------|--------|-----------------|------------|
| **Splash** | Loading (resolve) | None | → Onboarding / Login / Create Profile / Home |
| **Onboarding** | Content (pager) | Skip, Next, Get Started | → Login |
| **Login** | Idle / Submitting / Error / Offline | Send OTP, edit phone | → OTP |
| **OTP** | Entering / Verifying / Error / Resend cooldown | Auto-submit at 6 digits, Resend, Change phone | → Role / Create Profile / Home |
| **Role select** | Idle / Saving | Select Requester or Runner, Continue | → Create Profile |
| **Create Profile** | Editing / Loading / Field errors / Submitting | Save profile; Runner transport chips | → Home |
| **Home** | Content (foundation) | Profile, Sign out | → Profile / Auth |
| **Profile** | Loading / Content / Error / Empty | Retry, Back, Log out | → Auth on logout |

## Create Profile — field matrix

| Field | Requester | Runner | Notes |
|-------|-----------|--------|-------|
| First name | Required | Required | |
| Last name | Required | Required | |
| Email | Required | Required | Format validated |
| Phone | RO | RO | From Auth |
| Gender | Required | Required | Chip / button select |
| National ID | — | Required | |
| ID image | — | Hidden unless Storage flag | Placeholder in model |
| Selfie | — | Hidden unless Storage flag | Placeholder in model |
| Transport | — | Required | |
| Vehicle info | — | Required if not Walking | |

## UX notes

- Design-system buttons/fields only.
- Loading disables primary CTA (“Saving…”).
- Runner copy explains photos come later when Storage is disabled.
- Profile does not render selfie if URL blank.
