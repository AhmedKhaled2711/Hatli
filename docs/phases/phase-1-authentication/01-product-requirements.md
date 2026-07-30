# 01 — Product Requirements (Phase 1)

## Problem

Hatli cannot show marketplace value until a real user identity exists. First launch must establish trust: who the person is, how they will use the app (Requester vs Runner), and durable profile data that survives reinstall.

## Who

| Persona | Intent |
|---------|--------|
| **Requester** | Needs deliveries; creates profile with identity, contact, and delivery address |
| **Runner** | Delivers; creates profile with identity, address, transport + verification placeholders |
| **Returning user** | Reinstalls or new device; signs in with same phone and sees existing profile |

## Success looks like

- New user completes Splash → **Onboarding** (FlexBox-inspired 3-page pager) → Login → OTP → Role → **role-aware Create Profile wizard** → Home.
- Create Profile collects personal info + **delivery address** (and Runner transport / verification steps).
- Gender is limited to **Male / Female**.
- Returning user with completed profile skips onboarding/profile and lands on Home after Splash resolution.
- Profile is readable on Profile screen (including address when present).
- Profile document exists in Firestore at `users/{uid}` and is cached locally.
- Reinstall + same phone OTP restores Firestore profile (review criterion).
- Logout cancels active Firestore profile listeners before `signOut` so the UI does not crash on `PERMISSION_DENIED`.
- Friendly errors for invalid OTP, network loss, validation failures.

## Non-goals (high level)

- Social login, email/password, or passwordless email links.
- Full Runner KYC image pipeline on Spark (ID image / selfie **fields retained**, uploads **disabled** until Blaze).
- Map pin / current-location capture for address (manual fields only; lat/lng reserved).
- Changing role after profile completion (support flow later).
- Push notification preference center.
- Marketplace requests / jobs (later phases).
