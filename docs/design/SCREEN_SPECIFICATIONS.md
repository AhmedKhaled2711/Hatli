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

Main — Requester
├── Home
├── New request (flow)
│   ├── Request details
│   ├── Locations (pickup / dropoff)
│   ├── Review & confirm
│   └── Success
├── Activity (list)
├── Request detail + tracking
└── Profile (+ sub: addresses, settings, support)

Main — Runner
├── Jobs (list + map toggle)
├── Job detail (accept)
├── Active delivery
├── Earnings (summary)
└── Profile (+ verification status)
```

---

## A. Auth & onboarding

### A1. Splash

**Purpose:** Brand moment + resolve session/start destination (no user action).

**Layout**
- Full-bleed background (`Background` → subtle Forest900 radial wash optional via brush, not loud gradient kits)
- Centered wordmark **Hatli** (`displayLarge`, Primary)
- Short tagline under brand (`bodyMedium`, Text Secondary): e.g. “Deliveries you can trust”
- Bottom: indeterminate `CircularProgressIndicator` (Primary), 48dp clear area

**Components:** Brand text, progress indicator only.

**UX**
- Brand is the hero (not a marketing headline competing with logo).
- No Skip — splash is ≤ ~1.5s perceived; routing is the job.

**States:** Loading only → navigate (Onboarding | Login | Create Profile | Home).

**Compose:** `Box` + `Column`; no scroll; respect system bars with `WindowInsets`.

---

### A2. Onboarding (3 pages)

**Purpose:** Explain Requester/Runner value before phone auth.

**Pages (content)**
1. **Request or deliver** — one marketplace, two roles  
2. **Track every step** — live status you can trust  
3. **Pay and get paid simply** — clear, fast settlement  

**Layout** (FlexBox kit pattern, Hatli brand tokens)
- Top end: **Skip** (`HatliTextButton`, 48dp) — hidden on last page  
- Upper ~45%: illustration hero — soft forest circle wash + icon badges (vector, not photo collage)  
- Title `headlineLarge` + body `bodyLarge` (Text Secondary), centered  
- Page indicator: 3 dots — active pill (Primary) / inactive (outline)  
- Bottom sticky: **Next** / **Get Started** (`HatliPrimaryButton`)  

**UX**
- Skip always available until last page — respect returning curiosity without trapping.  
- One idea per page (reference kits often overload with 4 CTAs — we don’t).  
- Android `HorizontalPager` + edge swipe; dots are indicators (not the only navigation).  

**States:** Content only (local).  

**Compose:** `HorizontalPager`; mark onboarding complete on Skip or Get Started.  
**Reference:** [FlexBox Delivery tracking UI kit](https://www.figma.com/design/fHoNXiKXvhN4zbDE6ZL96p/flexbox-Delivery-tracking-app-ui-kit---Free-%F0%9F%98%89--Community-?node-id=101-2) onboarding structure.

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

### B1. Home (Requester)

**Purpose:** Orient + start a request + surface active jobs.

**Layout**
1. TopAppBar: location chip (start) · notifications (end)
2. Optional offline banner
3. **Active request** card (if any) — status chip + ETA + tap → detail  
4. Primary CTA card/button: **New delivery request**
5. Short “How it works” 3-step inline (collapsible after first sessions)
6. Recent activity preview (max 3 rows) → See all

**Bottom nav:** Home · Activity · Profile (+ FAB or in-home CTA for New)

**UX**
- Task completion first: create request is never more than one tap from Home.
- Active card beats burying status only in Activity (anxiety reduction from kits’ tracking focus).
- No restaurant carousels.

**States:** Loading skeletons · Empty (no active/recent) · Content · Error · Offline.

---

### B2. New request — Details

**Purpose:** What needs moving.

**Fields**
- Title / short description*
- Category chips (Parcel, Documents, Food/grocery pickup, Other) — optional metadata, not restaurant menu
- Size/weight estimate chips (Small / Medium / Large)
- Notes (optional)
- Offer amount* (secondary emphasis — amber label “Your offer”)

**UX:** Categories as chips (keep from kits) but labeled for parcels, not cuisines.

**States:** Editing · Validation · Offline (block submit).

---

### B3. New request — Locations

**Purpose:** Pickup & dropoff clarity (core of delivery UX).

**Layout**
- Two blocks: **Pickup** / **Dropoff**
  - Address line + “Set on map” 
  - Contact name/phone optional toggles
- Map preview strip (full-bleed width, ~160dp) showing both pins when set
- Bottom: **Continue**

**UX**
- Map is supporting context, not a floating card collage.
- Address search uses M3 SearchBar + results list + confirm pin sheet.
- RTL: pickup/dropoff icons mirrored carefully (arrow direction follows locale).

**States:** Missing location errors · GPS loading · Permission denied (dialog → settings).

---

### B4. New request — Review & confirm

**Purpose:** Prevent costly mistakes before publish.

**Layout**
- Summary sections: What · Route · Offer · Payment method
- Edit pencils per section (48dp)
- Sticky **Confirm request**

**Success (B5):** Full-page success — check icon (Success color), “Request posted”, primary **Track request**, text **Back to home**.

---

### B6. Activity (list)

**Purpose:** All requester requests.

**Layout**
- TopAppBar title “Activity”
- Filter chips: Active · Completed · Cancelled · All
- `HatliListRow`: title, route snippet, status chip, time, price trailing

**States:** Loading · Empty (“No requests yet” → New request) · Content · Error · Offline cached.

---

### B7. Request detail + tracking

**Purpose:** Status transparency + actions.

**Layout**
- TopAppBar + overflow (Cancel, Support)
- Status chip + last updated
- **Timeline** (Placed → Accepted → Picked up → Delivered)
- Runner card when assigned (avatar, name, transport, call/chat actions)
- Route summary
- Map section
- Sticky actions by state: Cancel | Contact runner | Confirm delivery | Rate

**UX**
- Timeline from food kits is the right pattern — keep it; use semantic colors per step.
- Call/chat only after accept (privacy).

**States:** Loading · Content · Runner pending (empty runner card with waiting copy) · Error · Success snackbars on status change.

---

## C. Runner main

### C1. Jobs (browse)

**Purpose:** Find and accept work quickly.

**Layout**
- Top: location + **List | Map** toggle (segmented, 48dp)
- Filter chips: Distance · Offer · Size
- List rows: offer (emphasized), distance, pickup→dropoff, size chip, time posted
- Map mode: markers; tap → peek sheet with **View** / **Accept**

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
