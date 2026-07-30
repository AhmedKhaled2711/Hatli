# 04 — Data Models (Phase 1)

| Model | Module | Fields | Notes |
|-------|--------|--------|-------|
| `AuthUser` | `:core:model` | `uid`, `phoneNumber` | Firebase Auth identity only |
| `UserRole` | `:core:model` | `Requester`, `Runner` | Chosen once at onboarding of role |
| `Gender` | `:core:model` | `Male`, `Female`, `Other`, `PreferNotToSay` | |
| `TransportType` | `:core:model` | `Bicycle`, `Motorcycle`, `Car`, `Walking`, `Other` | Runner |
| `VerificationStatus` | `:core:model` | `Pending`, `UnderReview`, `Approved`, `Rejected` | Runner; set `Pending` on create |
| `OtpSession` | `:core:model` | verification id + phone context | In-flight OTP |
| `CreateProfileInput` | `:core:model` | role, names, email, phone, gender, runner fields, local image URIs | Input DTO to use case |
| `UserProfile` | `:core:model` | full profile + `profileCompleted` | Domain; Firestore doc shape |
| `UserProfileEntity` | `:core:database` | mirrors profile | Room table `user_profiles` |

## Relationships

```text
FirebaseAuth.User (uid, phone)
        │
        ▼
UserProfile.uid == Auth uid   (1:1)
        │
        ├── role == Requester → runner fields null
        └── role == Runner    → nationalId, transport, vehicle, verificationStatus;
                                idImageUrl / selfieUrl optional (null on Spark)
```

## Validation rules (create)

| Rule | Applies to |
|------|------------|
| First + last name non-blank | Both |
| Email format | Both |
| Phone non-blank | Both |
| National ID required | Runner |
| Transport required | Runner |
| Vehicle required unless Walking | Runner |
| ID image + selfie required | Runner **only if** `STORAGE_IMAGE_UPLOADS_ENABLED` |
