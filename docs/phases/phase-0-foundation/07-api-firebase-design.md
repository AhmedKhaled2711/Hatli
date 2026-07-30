# 07 — API / Firebase Design (Phase 0)

## Project

| Key | Value |
|-----|--------|
| Firebase project | `hatli-d2e3f` |
| Android package | `com.app.hatli` |
| Config file | `app/google-services.json` |

## Services enabled for foundation

| Service | Phase 0 role |
|---------|----------------|
| Firebase Auth | Ready for Phase 1 phone OTP |
| Cloud Firestore | Ready for profile documents |
| Firebase Storage | **Configured in DI but uploads disabled** (`HatliFeatureFlags.STORAGE_IMAGE_UPLOADS_ENABLED = false`) — Spark limitation |
| Crashlytics / Analytics / Messaging | App plugins present; product usage later |

## Rules artifacts

| File | Purpose |
|------|---------|
| `firestore.rules` | Users may read/write only `users/{theirUid}` |
| `storage.rules` | Ready for Blaze; unused while flag is false |
| `firebase.json` | Deploy mapping for rules |

## Auth & security notes

- Never put Firebase API secrets in feature code; use official SDK + `google-services`.
- Domain/UI must not import Firebase.
- Console: create **Firestore database** manually before Phase 1 profile writes succeed.

## Offline / sync

- Room database module exists for local cache patterns.
- Phase 0 demo: posts network + cache on Home (foundation proof, not product).

## Collections

No product collections required to *close* Phase 0. Phase 1 introduces `users/{uid}`.
