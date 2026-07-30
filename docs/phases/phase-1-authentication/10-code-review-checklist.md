# 10 — Code Review Checklist (Phase 1)

- [ ] No feature → feature dependencies (`auth` ↛ `profile` at compile time; navigation via app callbacks)
- [ ] No Firebase imports in Composables or UseCases
- [ ] No business logic in Compose beyond state rendering / event dispatch
- [ ] OTP / profile errors mapped through `FirebaseErrorMapper` / `AppError`
- [ ] Phone validation before `verifyPhoneNumber`
- [ ] Create profile validation covers Requester vs Runner rules
- [ ] Storage uploads only when `HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED`
- [ ] Firestore document ID == Auth UID
- [ ] `profileCompleted` set true only after successful create
- [ ] Runner gets `verificationStatus = Pending`
- [ ] Design-system fields/buttons used on Login / OTP / Create Profile / Profile
- [ ] Loading disables duplicate submits
- [ ] Logout clears what the product requires (session + local profile cache)
- [ ] Routes use `Routes.*` helpers (encoded phone/verification id)
- [ ] Manual reinstall test (P1-13) considered before merge to `main`
- [ ] Docs updated if fields, rules, or flags change
