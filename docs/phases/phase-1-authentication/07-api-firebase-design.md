# 07 — API / Firebase Design (Phase 1)

## Auth

| Step | API |
|------|-----|
| Send OTP | Firebase Auth `verifyPhoneNumber` |
| Verify OTP | `PhoneAuthProvider.getCredential` + `signInWithCredential` |
| Session | `FirebaseAuth.currentUser` |
| Logout | `signOut` + clear local profile cache / session prefs as implemented |

## Firestore

### Collection

```text
users/{uid}
```

- **Document ID** must equal Firebase Auth UID.
- Created on first successful Create Profile (`set` + merge).

### Fields written

| Field | Type | Requester | Runner |
|-------|------|-----------|--------|
| `uid` | string | ✓ | ✓ |
| `role` | string enum | `Requester` | `Runner` |
| `phone` | string | ✓ | ✓ |
| `firstName` | string | ✓ | ✓ |
| `lastName` | string | ✓ | ✓ |
| `email` | string | ✓ | ✓ |
| `gender` | string enum | ✓ | ✓ |
| `profileCompleted` | bool | `true` | `true` |
| `createdAt` | server timestamp | on create | on create |
| `updatedAt` | server timestamp | always | always |
| `nationalId` | string \| null | null | set |
| `idImage` | string \| null | null | null (Spark) |
| `selfie` | string \| null | null | null (Spark) |
| `transportType` | string \| null | null | set |
| `vehicleInfo` | string \| null | null | set / null Walking |
| `verificationStatus` | string \| null | null | `Pending` |

### Security rules (`firestore.rules`)

```
match /users/{userId} {
  allow read, write: if request.auth != null && request.auth.uid == userId;
}
```

**Console prerequisite:** Create Cloud Firestore database for project `hatli-d2e3f` before first write.

## Storage (deferred)

| Item | Status |
|------|--------|
| Paths (future) | `users/{uid}/documents/id.jpg`, `users/{uid}/selfie.jpg` |
| Flag | `HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED = false` |
| Rules file | `storage.rules` (deploy after Blaze) |

## Offline / sync

1. Write Firestore.
2. Upsert Room `user_profiles`.
3. Profile observe: prefer remote listener; seed from cache when available.
4. Reinstall: Room empty → OTP → fetch Firestore → restore UI.

## Local preferences

| Key (concept) | Purpose |
|---------------|---------|
| Onboarding completed | Skip onboarding on later launches (lost on reinstall) |
| Pending role | Bridge Role screen → Create Profile |
