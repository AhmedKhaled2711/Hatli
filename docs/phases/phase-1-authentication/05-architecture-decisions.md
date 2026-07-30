# 05 — Architecture Decisions (Phase 1)

| Decision | Why | Trade-offs | Scalability |
|----------|-----|------------|-------------|
| Phone OTP via Firebase Auth | Fast MENA-friendly onboarding; no passwords | SMS cost; Play Integrity / SHA setup | Can add more providers behind `AuthRepository` |
| Single `users/{uid}` document | Simple 1:1 with Auth; easy security rules | Large docs if we cram everything later | Split subcollections when marketplace grows |
| Role before profile | Captures intent; drives form sections | Role change later needs support tooling | Keep `role` field immutable in app for now |
| Pending role in DataStore | Survives process death between Role and Create Profile | Cleared on reinstall (user re-selects) | Acceptable for Phase 1 |
| Room cache of profile | Offline read; snappy Profile screen | Sync conflicts rare (single writer = device) | Observe Firestore as source of truth |
| Storage behind flag | Spark cannot use Storage | Runner images deferred | Flip `HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED` on Blaze |
| Walking skips vehicle info | Real-world runners without vehicles | Slightly more validation branches | Clear UX |
| `ResolveStartDestinationUseCase` | Centralizes Splash routing rules | Must stay updated as lifecycle grows | Single place for cold-start logic |
| Auth feature owns entry graph | Keeps Phase 1 cohesive | App still registers `authGraph` | Matches Phase 0 composition root |

## Layer mapping (auth create profile)

```text
CreateProfileScreen
  → CreateProfileViewModel (UDF)
    → CreateUserProfileUseCase
      → UserRepository (interface, domain)
        → UserRepositoryImpl (data)
          → FirebaseStorageDataSource (no-op path when flag false)
          → FirestoreUserDataSource  → users/{uid}
          → UserLocalDataSource      → Room
          → FirebaseAuthDataSource   → current uid
```

## Error strategy

- SDK exceptions → `FirebaseErrorMapper` → `AppError` → `userMessage()` on UI.
- Validation → `AppError.InvalidInput` thrown in repository validate (or use case) before network.
