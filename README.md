<p align="center">
  <img src="https://raw.githubusercontent.com/AhmedKhaled2711/Hatli/main/docs/assets/hatli_logo_banner.png" alt="Hatli Banner" width="100%" />
</p>

<h1 align="center">📐 Hatli (هاتلي) — Hyperlocal Community Errand Engine</h1>

<p align="center">
  <strong>A production-grade, Clean Architecture Android application built with Jetpack Compose & Material 3, enabling peer-to-peer hyperlocal deliveries in emerging and small-town markets.</strong>
</p>

<p align="center">
  <a href="https://kotlinlang.org/"><img src="https://img.shields.io/badge/Kotlin-2.0.0-7F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" /></a>
  <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Compose-Material%203-4285F4.svg?style=for-the-badge&logo=android&logoColor=white" alt="Jetpack Compose" /></a>
  <a href="https://firebase.google.com/"><img src="https://img.shields.io/badge/Firebase-Firestore%20%26%20Auth-FFCA28.style=for-the-badge&style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase" /></a>
  <a href="https://developer.android.com/about/versions/14"><img src="https://img.shields.io/badge/Min%20SDK-26%2B%20(Android%208.0%2B)-3DDC84.svg?style=for-the-badge&logo=android&logoColor=white" alt="Min SDK 26+" /></a>
  <a href="https://developer.android.com/guide/topics/ui/look-and-feel/darktheme"><img src="https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-FF6B35.svg?style=for-the-badge&logo=architecture&logoColor=white" alt="Clean Architecture" /></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-008080.svg?style=for-the-badge" alt="License" /></a>
</p>

---

## 📸 Showcase & Visual Experience

| **Feed & Persona Switcher** | **3-Step Errand Wizard** | **Live Tracking & Chat** |
| :---: | :---: | :---: |
| <img src="https://raw.githubusercontent.com/AhmedKhaled2711/Hatli/main/docs/assets/showcase_feed.gif" width="260" alt="Feed & Dual Persona Switcher" /> | <img src="https://raw.githubusercontent.com/AhmedKhaled2711/Hatli/main/docs/assets/showcase_wizard.gif" width="260" alt="3-Step Errand Wizard" /> | <img src="https://raw.githubusercontent.com/AhmedKhaled2711/Hatli/main/docs/assets/showcase_tracking.gif" width="260" alt="Live Order Tracking & Chat" /> |
| *Floating 64dp pill bar with ambient blur shadow and instant Requester ↔ Runner mode transition.* | *Interactive category matrix, flexible landmark picker, and fee calculation slider.* | *5-stage active status timeline, Warm Coral radar pulse, and real-time messaging thread.* |

---

## 🧠 The Engineering "Why": Emerging Market Hyperlocal Errand Problem

Global delivery giants (e.g., UberEats, Deliveroo, DoorDash) fail in emerging small-towns, rural-adjacent districts, and dense non-standardized postal zones due to three fundamental market friction points:

1. **Absence of Geocoded Street Numbers:** Conventional delivery platforms require pinpoint street addresses and building numbers—data that simply does not exist in small towns across the Middle East and North Africa.
2. **Rigid Store Catalog Constraints:** Small-town commerce relies on unstructured, conversational errands (e.g., *"Buy 2 kg of pita bread from Al-Baraka Bakery and pick up my prescription from the corner pharmacy"*), which structured e-commerce cart engines cannot process.
3. **Rigid Single-Role Architectures:** Traditional apps enforce hard boundaries between customers and delivery drivers, requiring two distinct binaries and onboarding bottlenecks.

### How Hatli Solves This Architectural Gap:
* **Landmark-First Navigation Engine:** Replaces street addresses with recognizable local landmarks (e.g., *"Next to Al-Nour Mosque"*), coupled with custom micro-dropoff instructions.
* **Flexible 3-Step Errand Engine:** Supports custom shopping lists, package pick-and-drop, utility bill settlements, and unstructured custom requests with user-suggested delivery fees.
* **Dual-Persona Single Binary Engine:** Users switch seamlessly between **Requester (طالب)** and **Runner (كابتن)** personas in real-time within a single app install, empowering community peer-to-peer fulfillment.

---

## 🏗 Modular Clean Architecture

Hatli strictly enforces **Clean Architecture** combined with **MVVM**, **Kotlin Flow**, and **Android Jetpack recommended practices**. The project is split into decoupled Gradle modules to ensure modularity, high build parallelization, and zero cross-feature dependency leakage.

```
Hatli (Root Project)
├── app/                              # Application entry point, Hilt dependency graph, Navigation Host
├── core/
│   ├── model/                        # Domain entities (Request, UserProfile, ChatMessage, Enums)
│   ├── common/                       # Result wrappers, Dispatchers, Extensions, Utilities
│   ├── database/                     # Room database for offline caching & pending syncs
│   ├── datastore/                    # Proto/Preferences DataStore for auth & persona states
│   ├── network/                      # Firebase Firestore & Auth integration layer
│   ├── domain/                       # Pure Kotlin Use Cases (CreateRequest, ObserveRequest, Chat)
│   ├── designsystem/                 # Hatli Brand Tokens (Color, Typography, Shape, Core Components)
│   └── ui/                           # Base ViewModels, UiState / UiEvent / UiEffect contracts
└── feature/
    ├── auth/                         # Splash, Onboarding Carousel, Phone Auth, OTP, Role Select
    ├── home/                         # Main Shell, Floating Bottom Bar, Requester & Runner Feeds
    ├── request/                      # 3-Step Errand Wizard, Stepper Timeline Tracking, Rate Runner
    ├── chat/                         # Real-Time Chat Thread, Quick Replies, Image Attachment
    └── profile/                      # User Profile, Driver Verification, Persona Switcher, Settings
```

---

## ⚡ Technical Highlights & Production Edge-Case Engineering

### 1. Resource-Safe Lifecycle-Aware Firestore Snapshots
To prevent memory leaks, unnecessary network bandwidth consumption, and costly Firestore read spikes, real-time snapshot listeners are wrapped inside Kotlin `callbackFlow` builders with explicit resource teardown upon cancellation:

```kotlin
fun observeRequest(requestId: String): Flow<Result<Request>> = callbackFlow {
    val registration = firestore.collection("requests")
        .document(requestId)
        .addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.Error(error))
                return@addSnapshotListener
            }
            snapshot?.toObject(RequestDto::class.java)?.let { dto ->
                trySend(Result.Success(dto.toDomain()))
            }
        }
    awaitClose { registration.remove() } // Guarantees listener detachment on Scope cancellation
}.flowOn(ioDispatcher)
```

In the Compose UI layer, collectors utilize `collectAsStateWithLifecycle()` to pause data streams whenever the Activity or Fragment enters the background.

### 2. Seamless IME Keyboard & System Insets Orchestration
Chat threads and multiline input forms utilize `Modifier.imePadding()` combined with `Modifier.systemBarsPadding()` to guarantee zero layout jumps when soft keyboards animate onto the screen:

```kotlin
Surface(
    color = Color.White,
    shadowElevation = 8.dp,
    modifier = Modifier.windowInsetsPadding(WindowInsets.ime) // Smooth keyboard animation sync
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = HatliSpacing.md, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) { ... }
}
```

### 3. Bidirectional Text & Native RTL / LTR Isolation
While the app defaults to full Arabic RTL (`LayoutDirection.Rtl`), numeric fields, phone inputs, EGP currency prices, and OTP verification cells enforce strict LTR directionality using localized composition providers to prevent reversed Arabic string bugs:

```kotlin
CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
    Text(
        text = "${req.deliveryFee.toInt()} EGP",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = BrandCoral
    )
}
```

---

## 🎨 Design System Tokens

Hatli is styled with an energetic, modern brand palette designed for high contrast and legibility under direct sunlight:

| Token Name | Hex Code | Visual Swatch | Semantic Purpose |
| :--- | :---: | :---: | :--- |
| **Brand Warm Coral** | `#FF6B35` | <img src="https://via.placeholder.com/15/FF6B35/000000?text=+" /> | Primary Action Buttons, Active Tabs, Radar Pulses |
| **Mint Teal** | `#30E6C1` | <img src="https://via.placeholder.com/15/30E6C1/000000?text=+" /> | Verified Badges, Completed Steppers, Success States |
| **Off-White Canvas** | `#F8F9FA` | <img src="https://via.placeholder.com/15/F8F9FA/000000?text=+" /> | Background Canvas, Surface Containers |
| **Deep Charcoal** | `#1E2022` | <img src="https://via.placeholder.com/15/1E2022/000000?text=+" /> | Primary Headlines & High-Contrast Typography |

---

## 🛠 Tech Stack & Tooling

* **UI Engine:** 100% Declarative Jetpack Compose with Material 3 components.
* **Architecture:** Clean Architecture + MVVM + Unidirectional Data Flow (UDF).
* **Navigation:** Jetpack Navigation 2.8+ using Type-Safe `@Serializable` routes.
* **Dependency Injection:** Dagger Hilt (`@HiltViewModel`, `@AndroidEntryPoint`).
* **Asynchronous Streams:** Kotlin Coroutines (`StateFlow`, `SharedFlow`, `callbackFlow`).
* **Backend Integration:** Firebase Cloud Firestore Realtime Snapshots & Phone Auth.
* **Image Loading:** Coil for Compose (`AsyncImage` with memory/disk caching).
* **Testing:** JUnit 5, Mockk, kotlinx-coroutines-test, Turbine.

---

## 🚀 Getting Started & Local Setup

### Prerequisites
* **Android Studio:** Ladybug (2024.2.1+) or Koala Feature Update.
* **JDK:** Version 17+.
* **Android SDK:** API 34+ (Min SDK 26).

### Quickstart Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/AhmedKhaled2711/Hatli.git
   cd Hatli
   ```

2. **Setup Firebase Configuration:**
   * Create a project in the [Firebase Console](https://console.firebase.google.com/).
   * Enable **Phone Authentication** under Firebase Auth.
   * Enable **Cloud Firestore** in test or production mode.
   * Download `google-services.json` and place it into the `app/` directory:
     ```
     Hatli/app/google-services.json
     ```

3. **Build & Execute:**
   * Open the project in Android Studio.
   * Sync Gradle files (`Gradle Sync`).
   * Run unit tests via CLI:
     ```bash
     ./gradlew testDebugUnitTest
     ```
   * Build the debug APK:
     ```bash
     ./gradlew assembleDebug
     ```

---

## 👤 Author & Engineering Contact

**Ahmed Khaled**  
* Android Engineer & Mobile UI/UX Specialist*

* 📧 **Email:** [ahmedkhaled.dev@gmail.com](mailto:ahmedkhaled.dev@gmail.com)
* 🐙 **GitHub:** [@AhmedKhaled2711](https://github.com/AhmedKhaled2711)
* 💼 **LinkedIn:** [linkedin.com/in/ahmedkhaled2711](https://linkedin.com/in/ahmedkhaled2711)
* 🌐 **Portfolio:** [ahmedkhaled.dev](https://ahmedkhaled.dev)

---

<p align="center">
  <sub>Built with ❤️ and Kotlin Jetpack Compose for emerging communities.</sub>
</p>
