# Hatli Screen Specifications

Production-ready Android screen designs for Hatli (Requester ↔ Runner).  
Companion: [DESIGN_SYSTEM.md](./DESIGN_SYSTEM.md).

Each screen: **purpose · layout · components · UX rationale · states · Compose notes**.

---

## Information architecture

```
Auth
├── Splash
├── Onboarding (3 pages)
├── Login (phone)
├── OTP
├── Role select
└── Create profile (Requester | Runner)

Main — Requester (bottom nav)
├── Home          (state-based dashboard · Hero CTA first)
├── Requests      (Active · Completed · Cancelled)
├── Messages      (chat with Runner — Phase 2 empty)
├── Profile
├── FAB (+)       Create Request (Phase 3 assisted wizard)
└── Bell + badge  Notifications affordance

Main — Runner (bottom nav)
├── Home          (Online/Offline · stats · Nearby Pending top 3)
├── Jobs          (Active · History)  ← not “Requests”
├── Messages
└── Profile

Create Request (Phase 3)
├── Category: Shopping | Pickup | Bills | Custom
├── Category-specific steps → Fee → Review → Submit (Pending)
└── Runner Accept → live update

Post-Accept (Phase 4)
├── Current Request / Current Job (pinned on Home)
├── Status: Accepted → InProgress → Purchased? → OnTheWay → Delivered → Completed
├── Chat (Firestore messages under request)
├── Request Detail + Cancel
└── Confirm delivery + Rate Runner

Deferred (Phase 4b+)
├── FCM push
└── Live GPS / map tracking
```

**JTBD:** User opens Hatli to **create a Request** or **fulfill a Job** — not to browse restaurants.

---

## A. Auth & onboarding

### A1. Splash

**Purpose:** Brand moment + resolve session/start destination (no user action).

**Layout**
- Full-bleed background (`Background`)
- Centered branded **delivery_man** Lottie (`lottie/splash/delivery_man.json`) — mint teal `#30E6C1` body accents, charcoal badge with Primary **H**, soft teal wash; same motion/timing as source
- Wordmark **Hatli** under animation (`displayLarge`, Primary)
- Short tagline (`bodyMedium`, Text Secondary): “Deliveries you can trust”
- Bottom: indeterminate `CircularProgressIndicator` (Primary)

**Components:** Lottie hero, brand text, progress indicator.

**UX**
- Brand is the hero (Lottie + wordmark; no competing marketing headline).
- No Skip — splash holds ~1.5s min then routes.

**States:** Loading only → navigate (Onboarding | Login | Create Profile | Home).

**Compose:** `Box` + `Column` + `HatliLottieAnimation`; respect system bars.

---

### A2. Onboarding (3 pages)

**Purpose:** Explain Requester/Runner value before phone auth.

**Pages (content)** — EN / AR via `feature/auth` strings  
1. **Order anything nearby** / اطلب أي حاجة من منطقتك  
2. **Create your request in seconds** / اطلب اللي أنت عايزه  
3. **Nearby runners are ready** / أقرب Runner هيجيلك  

**Layout** (premium delivery-app pattern, Hatli mint/charcoal tokens)
- **Background:** branded teal wash (`OnboardingBackground`) — swappable for a custom asset later  
- **Top bar:** Hatli wordmark (start) · **Skip** / **لاحقًا** (end, hidden on last) — RTL-aware  
- **Hero (~2/3):** centered `PhoneMockup` — page 1 shows a realistic request list + “Accepted by Ahmed” badge  
- **Bottom sheet (~1/3):** animated title + body, pill indicator, **Next** / **Get Started** (التالي / ابدأ دلوقتي)  

**UX**
- First screen teaches the product in under 3 seconds with a real request mock (not a generic delivery illustration).  
- Skip until last page. One idea per page; swipe or Next.  

**States:** Content only (local).  

**Compose:** `OnboardingTopBar` · `PhoneMockup` · `OnboardingPage` · `OnboardingPhoneScreen` · `OnboardingBottomSheet` · `OnboardingIndicator`.  
**i18n:** `values/strings.xml` + `values-ar/strings.xml`.  
**Complete:** Skip or Get Started → mark onboarding done → Login.

---

### A3. Login (phone)

**Purpose:** Start OTP auth with minimal friction.

**Layout**
- TopAppBar: none or transparent with no back (root of auth after onboarding)
- Title: “Enter your phone”
- Subtitle: “We’ll send a one-time code”
- Row: Country code selector (chip/button 48dp) + phone `HatliTextField`
- Helper: “Standard SMS rates may apply”
- Bottom pinned: **Send code** primary (enabled when valid national number)

**Components:** Text field, country picker (bottom sheet), primary button, inline error.

**UX**
- Phone-first (common in MENA/LATAM delivery markets) — no email password wall.
- Validate as-you-type lightly; hard validate on submit.
- Country sheet: search + flag + dial code; large rows.

**States:** Idle · Submitting (button loading) · Error (field + snackbar for network) · Offline banner.

**Compose:** IME Next/Done; `WindowInsets.ime` padding so CTA clears keyboard.

---

### A4. OTP

**Purpose:** Verify phone ownership.

**Layout**
- Back in TopAppBar
- Title: “Verify your number”
- Subtitle: masked phone + **Change** text button
- 6-box OTP (`HatliOtpField` — equal boxes, 48dp+ height, auto-advance)
- Resend row: “Resend in 0:45” → becomes **Resend code** text button
- Auto-submit when 6 digits entered

**UX**
- Auto-read SMS where Play Integrity / SMS Retriever available.
- Errors clear previous digit focus to first invalid or all.
- Avoid separate “Verify” as only path if auto-submit exists; keep button for a11y.

**States:** Entering · Verifying · Error (wrong code) · Locked/rate-limit (info dialog) · Resend cooldown.

---

### A5. Role select

**Purpose:** One-time role commitment (Requester vs Runner).

**Layout**
- Title: “How will you use Hatli?”
- Two large selectable cards (min 72dp content, full-width):
  - **I need deliveries** (Requester) — icon + short benefit
  - **I deliver** (Runner) — icon + short benefit
- Selected: Primary border 2dp + primaryContainer wash
- Bottom: **Continue** (disabled until selection)

**UX**
- Cards beat tiny radio rows (reference kits) — clearer affordance, bigger targets.
- Copy avoids “food” language.
- Role is sticky; changing later is Settings (advanced) — don’t bury regret; show brief note “You can request a role change in Support”.

**States:** Idle · Saving · Error.

---

### A6. Create profile (multi-step wizard)

**Purpose:** Collect identity + delivery address; Runner adds transport + KYC placeholders.

**Steps (role-aware)**
- **Requester:** Personal → Address (Save)
- **Runner:** Personal → Address → Transport → Verification (Save)

**Personal**
- First name*, Last name*, Email*, Phone (read-only), Gender* (`Male` \| `Female`)

**Address**
- City*, Area*, Street*, Building*
- Floor, Apartment, Notes (optional)
- Lat/lng reserved for future maps / current location (null now)

**Transport (Runner)**
- Transport type* (chip group: Bicycle, Motorcycle, Car, Walking, Other)
- Vehicle details (conditional if not Walking)

**Verification (Runner)**
- National ID*
- ID photo* + Selfie* only when Storage flag enabled (otherwise copy explains later)

**Layout**
- Progress header (`step / total`); step title + short description
- One step visible at a time; sticky footer **Back** + **Next** / **Save and continue**
- Per-step validation on Next; full re-validation on Submit

**UX**
- Don’t show Runner steps to Requesters.
- Address copy notes maps coming later.
- Validation messages under fields / step error, not only toast.

**States:** Step editing · Submitting · Field/step errors · Offline.

---

## B. Requester main

### B1. Home (Requester) — Phase 2 JTBD

**Purpose:** Primary action first — create or resume a Request. State-based, not browse.

**Layout (top → bottom)**
1. Top bar: title/brand · **notifications bell + badge**
2. **Hero card (always first when no blocking active job UI):** “عايز تطلب حاجة؟” / Want something? · primary **+ اطلب دلوقتي**
3. **Current Request** (if any) — status (searching / accepted / …) · title · ETA · CTA
4. **Recent Requests** (max 5)
5. **Quick Actions** — supermarket / butcher / bakery / pharmacy / anything (shortcuts, not fixed categories)
6. **Tips** — e.g. write clear request descriptions

**State-based UI**

| State | Home emphasis |
|-------|----------------|
| Empty | Hero CTA (+ optional short greeting secondary) |
| SearchingRunner | Current card: searching for Runner |
| Accepted / InProgress | Current card: runner name · follow |
| Completed (unrated) | Prompt to rate |
| Error / Loading | Standard |

**Bottom nav:** Home · Requests · Messages · Profile · **FAB (+)** Create

**UX**
- Never lead with restaurant grids or greeting-only chrome.
- One tap to Create from Hero or FAB.
- Design system + Mada only.

**States:** Loading · Empty · Searching · Accepted · Rate · Error.

**Compose:** `RequesterHomeScreen` + cards; data via `HomeRepository` (fake in Phase 2).

---

### B1b. Requests (list) — Phase 2 shell

**Purpose:** All requester requests with filters.

**Layout:** Segmented / chips: Active · Completed · Cancelled · `HatliCard` rows.

**States:** Loading · Empty · Content.

---

### B1c. Messages (Requester/Runner) — Phase 2

**Purpose:** Placeholder until chat.

**Layout:** Empty state — “Chat with your Runner soon.”

---

### B2. New request — Category & assisted wizard (Phase 3)

**Purpose:** Guided create — never a blank free-text first screen.

**Flow**
1. Large category cards: **Shopping · Pickup · Bills · Custom**
2. Branch steps (see Phase 3 docs) → suggested delivery fee (default 35 EGP, editable) → Review
3. Submit → `requests` doc `Pending` → back to Home

**Out of Phase 3:** Map pin picker, place-specific categories (Butcher/Bakery), payments.

**States:** Editing · Validation · Submitting · Offline error.

### B3. New request — Locations (Pickup category / later maps)

**Purpose:** Pickup & dropoff as **text addresses** in Phase 3 Pickup flow. Full map UX deferred.

**Phase 3 fields:** Pickup address string · Dropoff address string · Item description.

**Deferred:** Set on map, GPS, dual pins.

---

### B4. New request — Review & confirm

**Purpose:** Prevent mistakes before publish.

**Layout**
- Summary: Category · What · Addresses · Budget · Delivery fee
- Sticky **Confirm request**

**Success:** Pop to Home with Current Request = Pending (listener).

---

### B6. Activity (list) — superseded by **Requests** (B1b)

Kept for historical reference; Phase 2 uses **Requests** tab naming.

---

### B7. Request detail + tracking

**Purpose:** Status transparency + actions (**execution UI Phase 4+**).

**Phase 3:** Home cards + Accept only. Detail/timeline deferred.

**States:** Loading · Content · Runner pending · Error.

---

## C. Runner main

### C0. Home (Runner) — Phase 2 JTBD

**Purpose:** Control availability and surface nearby work without leaving Home.

**Layout**
1. Top bar · bell + badge
2. **Presence card:** Online (green) / Offline (red) · Go Offline / Go Online
3. **Daily earnings** · **Today’s jobs** · **Rating**
4. **Nearby Requests** — top 3 (title, distance, offer) · **View All** → Jobs tab
5. If **active job** — focus card above nearby

**States:** Offline · OnlineEmpty · Nearby · ActiveJob · Loading · Error.

**Bottom nav:** Home · **Jobs** · Messages · Profile (no FAB).

---

### C1. Jobs (list) — Phase 2 shell

**Purpose:** Browse assigned / available work (full accept map UI Phase 3+).

**Layout**
- Filters: Active · History
- Rows: title, distance, offer, status

**States:** Loading · Empty · Content.

---

### C1b. Jobs browse (map) — Phase 3+

**Purpose:** Find and accept work quickly (map toggle deferred).

**UX**
- Sort default: nearest + highest offer hybrid (document in product); UI shows current sort in chip.
- Accept from list is secondary; detail confirms (prevent mis-taps).

**States:** Empty area · Loading · Content · Offline (show cached with banner).

---

### C2. Job detail (open)

**Purpose:** Decide to accept.

**Content:** Full route, notes, size, offer, estimated distance, requester first name only.

**Sticky:** **Accept job** (Primary). Secondary: Navigate preview.

**Dialog on accept:** Confirm commitment; then navigate Active.

---

### C3. Active delivery

**Purpose:** Execution cockpit — minimal chrome.

**Layout**
- Status banner (Warning/Info/Success containers)
- Next action headline (“Head to pickup” / “Head to dropoff”)
- Addresses with **Navigate** (secondary)
- Requester contact
- Checklist actions: **Arrived pickup** → **Confirm pickup** → **Arrived dropoff** → **Complete delivery**
- Photo proof slot if required by policy

**UX**
- One primary action at a time (reduces cognitive load vs kits that show all steps enabled).
- Large buttons for gloved/outdoor use.

**States:** Per-step loading · Error with retry · Offline (queue action if architecture allows; else block with message).

---

### C4. Earnings (summary)

**Purpose:** Trust & motivation (can be Phase-later lightweight).

**Layout:** Today / Week totals, recent payouts list, payout method row.

**Empty:** “Complete a delivery to see earnings”.

---

## D. Profile (both roles)

### D1. Profile hub

**Layout**
- Header: avatar, name, phone, role chip
- Runner: verification `HatliStatusChip` (Pending/Approved/Rejected)
- Grouped list (Settings-style, not card spam):
  - Personal info  
  - Addresses (Requester) / Vehicle & documents (Runner)  
  - Notifications  
  - Language / RTL follows system  
  - Support  
  - Legal  
  - Log out (Error-colored text)

**UX:** Platform settings pattern — familiar, scannable, Compose `LazyColumn` friendly.

---

### D2. Personal info / edit

Standard form; Save app bar action; success snackbar.

### D3. Verification (Runner)

Read-only status + reason if rejected + **Resubmit** documents.

---

## E. System / shared screens & patterns

| Surface | Design |
|---------|--------|
| **Notifications list** | Rows with type icon, title, time; unread Primary dot |
| **Permission rationales** | Dialog before system prompt (location, camera, SMS) |
| **Force update** | Blocking dialog |
| **Snackbar** | Above nav; max 2 lines; one action |
| **Destructive confirm** | `HatliDialog` — Cancel (text) + Destroy (error emphasis) |

---

## F. Component → screen map (consistency)

| Screen | Key components |
|--------|----------------|
| Splash | Typography display, Progress |
| Onboarding | HorizontalPager, illustration hero, pill dots, TextButton (Skip), PrimaryButton |
| Login / OTP / Profile forms | TextField, PrimaryButton, BottomSheet |
| Role | Selectable HatliCard |
| Homes | TopAppBar, Card, ListRow, NavBar, FAB/CTA |
| Lists | Chip, ListRow, Empty/Error/Offline |
| Detail / Active | Timeline, StatusChip, Sticky CTA bar |
| Sheets | SearchBar, List, PrimaryButton |

---

## G. Responsive & large screens

- Phone: single column, 16dp horizontal inset  
- Unbounded width (fold / tablet): content max width **600dp** centered for forms; lists can use two-pane (list | detail) for Activity/Jobs  
- Never stretch full-width paragraphs on 10" tablets  

---

## H. Implementation priority (aligned with phase process)

1. **Design system completion** — chips, empty/error, snackbar, nav, status, OTP field  
2. **Auth polish** — match specs above (already scaffolded)  
3. **Requester Home + New request + Activity + Detail**  
4. **Runner Jobs + Active**  
5. **Earnings / notifications**  

Menu / Cart / Checkout feature modules in the repo are **legacy food scaffolding** — do not design those as Hatli IA; prefer Request/Jobs naming in new modules when Phase 1 starts.
