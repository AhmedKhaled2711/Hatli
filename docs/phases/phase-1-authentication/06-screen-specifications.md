# 06 — Screen Specifications (Phase 1)

| Screen | States | Primary actions | Nav in/out |
|--------|--------|-----------------|------------|
| **Splash** | Loading (Lottie + resolve, ≥1.5s) | None | → Onboarding / Login / Create Profile / Home |
| **Onboarding** | Content (pager + illustration) | Skip, Next, Get Started | → Login |
| **Login** | Idle / Submitting / Error / Offline | Send OTP, edit phone | → OTP |
| **OTP** | Entering / Verifying / Error / Resend cooldown | Auto-submit at 6 digits, Resend, Change phone | → Role / Create Profile / Home |
| **Role select** | Idle / Saving | Select Requester or Runner, Continue | → Create Profile |
| **Create Profile** | Step editing / Loading / Step errors / Submitting | Next, Back, Save (last step) | → Home |
| **Home** | Content (foundation) | Profile, Sign out | → Profile / Auth |
| **Profile** | Loading / Content / Error / Empty | Retry, Back, Log out | → Auth on logout |

## Onboarding — FlexBox-inspired pager

| Page | Title | Idea |
|------|-------|------|
| 1 | Request or deliver | One marketplace, two roles |
| 2 | Track every step | Live status you can trust |
| 3 | Pay and get paid simply | Clear pricing / fast settlement |

**Chrome**
- Top end: **Skip** (`HatliTextButton`) — hidden on last page
- Hero: soft forest circle wash + icon badges (`OnboardingIllustration`)
- Title `headlineLarge` + body `bodyLarge` (centered, onSurfaceVariant)
- Pill page indicators (active Primary / inactive outlineVariant)
- Bottom: **Next** / **Get Started** (`HatliPrimaryButton`)
- `HorizontalPager` swipe syncs `pageIndex` via `PageSelected`

**Complete:** Skip or Get Started → `SetOnboardingCompletedUseCase` → Login.

**Reference:** FlexBox Delivery tracking UI kit onboarding structure; Hatli forest/amber tokens.

## Create Profile — wizard

Role-aware steps with progress header (`stepNumber` / `totalSteps`).

| Step | Requester | Runner | Title |
|------|-----------|--------|-------|
| **Personal** | ✓ | ✓ | Personal information |
| **Address** | ✓ | ✓ | Your address |
| **Transport** | — | ✓ | How do you deliver? |
| **Verification** | — | ✓ | Verification |

- **Requester:** 2 steps → Save on Address.
- **Runner:** 4 steps → Save on Verification.
- Next validates the current step only; Submit re-validates all steps before network write.
- Back returns to previous step (disabled on first step / while loading).

## Create Profile — field matrix

| Field | Step | Requester | Runner | Notes |
|-------|------|-----------|--------|-------|
| First name | Personal | Required | Required | |
| Last name | Personal | Required | Required | |
| Email | Personal | Required | Required | Format validated |
| Phone | Personal | RO | RO | From Auth |
| Gender | Personal | Required | Required | `Male` \| `Female` only |
| City | Address | Required | Required | |
| Area | Address | Required | Required | |
| Street | Address | Required | Required | |
| Building | Address | Required | Required | |
| Floor | Address | Optional | Optional | |
| Apartment | Address | Optional | Optional | |
| Address notes | Address | Optional | Optional | |
| Transport | Transport | — | Required | |
| Vehicle info | Transport | — | Required if not Walking | |
| National ID | Verification | — | Required | |
| ID image | Verification | — | Hidden unless Storage flag | Placeholder in model |
| Selfie | Verification | — | Hidden unless Storage flag | Placeholder in model |

## UX notes

- Design-system buttons/fields only.
- Loading disables primary CTA (“Saving…”).
- Address copy notes maps / current location come later.
- Runner verification copy explains photos come later when Storage is disabled.
- Profile shows address as a one-line formatted string when present.
- Profile does not render selfie if URL blank.
- Logout cancels Firestore observe before `signOut`.
