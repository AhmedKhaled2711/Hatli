# 04 — Data Models (Phase 1)

| Model | Module | Fields | Notes |
|-------|--------|--------|-------|
| `AuthUser` | `:core:model` | `uid`, `phoneNumber` | Firebase Auth identity only |
| `UserRole` | `:core:model` | `Requester`, `Runner` | Chosen once at onboarding of role |
| `Gender` | `:core:model` | `Male`, `Female` | Other options removed from product |
| `UserAddress` | `:core:model` | `city`, `area`, `street`, `building`, optional `floor` / `apartment` / `notes`, optional `latitude` / `longitude` | Manual entry now; lat/lng reserved for maps |
| `TransportType` | `:core:model` | `Bicycle`, `Motorcycle`, `Car`, `Walking`, `Other` | Runner |
| `VerificationStatus` | `:core:model` | `Pending`, `UnderReview`, `Approved`, `Rejected` | Runner; set `Pending` on create |
| `OtpSession` | `:core:model` | verification id + phone context | In-flight OTP |
| `CreateProfileInput` | `:core:model` | role, names, email, phone, gender, **address**, runner fields, local image URIs | Input DTO to use case |
| `UserProfile` | `:core:model` | full profile + optional `address` + `profileCompleted` | Domain; Firestore doc shape |
| `UserProfileEntity` | `:core:database` | mirrors profile incl. flattened address columns | Room table `user_profiles` |

## Relationships

```text
FirebaseAuth.User (uid, phone)
        │
        ▼
UserProfile.uid == Auth uid   (1:1)
        │
        ├── address: UserAddress?   (required on create; both roles)
        ├── role == Requester → runner fields null
        └── role == Runner    → nationalId, transport, vehicle, verificationStatus;
                                idImageUrl / selfieUrl optional (null on Spark)
```

## Validation rules (create)

| Rule | Applies to |
|------|------------|
| First + last name non-blank | Both (Personal step) |
| Email format | Both (Personal step) |
| Phone non-blank | Both (Personal step) |
| Gender selected (`Male` \| `Female`) | Both (Personal step) |
| City, area, street, building non-blank | Both (Address step) |
| National ID required | Runner (Verification step) |
| Transport required | Runner (Transport step) |
| Vehicle required unless Walking | Runner (Transport step) |
| ID image + selfie required | Runner **only if** `STORAGE_IMAGE_UPLOADS_ENABLED` |
