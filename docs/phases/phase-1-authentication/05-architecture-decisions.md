# 05 — Architecture Decisions (Phase 1)

| Decision | Why | Trade-offs | Scalability |
|----------|-----|------------|-------------|
| FlexBox-inspired onboarding layout | Familiar delivery-app intro pattern; swipe + dots + sticky CTA | Kit illustrations not licensed assets — use Compose icon heroes | Swap in exported SVGs later without changing flow |
| Phone OTP via Firebase Auth | Fast MENA-friendly onboarding; no passwords | SMS cost; Play Integrity / SHA setup | Can add more providers behind `AuthRepository` |
| Single `users/{uid}` document | Simple 1:1 with Auth; easy security rules | Large docs if we cram everything later | Split subcollections when marketplace grows |
| Role before profile | Captures intent; drives wizard steps | Role change later needs support tooling | Keep `role` field immutable in app for now |
| Multi-step Create Profile wizard | Shorter cognitive load; role-aware steps (Requester: 2, Runner: 4) | State held in one ViewModel across steps | Maps / Current Location can plug into Address step later |
| Address on profile create | Needed for pickups/deliveries; persisted with user | Manual entry only (no maps yet) | Lat/lng fields reserved on `UserAddress` |
| Gender = Male / Female only | Matches product decision | Legacy docs/enums may have had more values | Parse unknown Firestore values safely → default Male |
| Cancel listener before logout | Auth rules deny reads after `signOut`; open snapshot listeners threw `PERMISSION_DENIED` | Must cancel observe job (and tolerate denied close in data source) | Same pattern for any auth-scoped Firestore observe |
| Pending role in DataStore | Survives process death between Role and Create Profile | Cleared on reinstall (user re-selects) | Acceptable for Phase 1 |
| Room cache of profile | Offline read; snappy Profile screen | Sync conflicts rare (single writer = device) | Observe Firestore as source of truth |
| Storage behind flag | Spark cannot use Storage | Runner images deferred | Flip `HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED` on Blaze |
| Walking skips vehicle info | Real-world runners without vehicles | Slightly more validation branches | Clear UX |
| `ResolveStartDestinationUseCase` | Centralizes Splash routing rules | Must stay updated as lifecycle grows | Single place for cold-start logic |
| Auth feature owns entry graph | Keeps Phase 1 cohesive | App still registers `authGraph` | Matches Phase 0 composition root |

## Layer mapping (auth create profile)

```text
CreateProfileScreen (wizard: Personal | Address | [Transport | Verification])
  → CreateProfileViewModel (UDF; per-step validation + Submit)
    → CreateUserProfileUseCase
      → UserRepository (interface, domain)
        → UserRepositoryImpl (data)
          → FirebaseStorageDataSource (no-op path when flag false)
          → FirestoreUserDataSource  → users/{uid} (+ nested address map)
          → UserLocalDataSource      → Room (flattened address columns)
          → FirebaseAuthDataSource   → current uid
```

## Logout sequencing

```text
ProfileViewModel.Logout
  → cancel observeProfile Job
  → LogoutUseCase → Auth.signOut + clear local cache
  → navigate auth_graph

FirestoreUserDataSource.observe: on PERMISSION_DENIED → trySend(null) + close (no crash)
```

## Error strategy

- SDK exceptions → `FirebaseErrorMapper` → `AppError` → `userMessage()` on UI.
- Validation → step-level in ViewModel; repository also validates address / runner rules before network.
