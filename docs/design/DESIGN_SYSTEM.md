# Hatli Design System

**Product:** Hatli — peer delivery marketplace (Requester ↔ Runner)  
**Platform:** Android-first · Material Design 3 · Jetpack Compose  
**Status:** Production design foundation

---

## 1. Product positioning (design implications)

Hatli is **not** a restaurant food-ordering app. Users either:

- **Requester** — post a delivery request (pickup → dropoff), track progress, pay.
- **Runner** — browse nearby requests, accept jobs, navigate, complete delivery, get verified.

UI kits referenced for inspiration (Speedy Chow, [FlexBox Delivery tracking](https://www.figma.com/design/fHoNXiKXvhN4zbDE6ZL96p/flexbox-Delivery-tracking-app-ui-kit---Free-%F0%9F%98%89--Community-?node-id=101-2), Delivery App UI Kit) are food/courier oriented. We keep their **strong UX patterns** (status timelines, sticky CTAs, clear location context, empty states with one action, onboarding pager + hero + dots) and discard food-grid / cart / menu patterns that fight Hatli’s mental model. Onboarding / splash use Hatli **mint teal + charcoal** tokens — not the kit’s stock palette.

---

## 2. Reference analysis — keep / improve / drop

### Keep (best UX ideas)

| Pattern | Why it works | Hatli adaptation |
|---------|--------------|------------------|
| Persistent location / context bar | Anchors “where am I operating?” | Header: delivery area + address chevron |
| Horizontal filter chips | Fast scan without modal tax | Status / distance / category chips |
| Sticky primary CTA | One obvious next step | Bottom bar: Post request / Accept job |
| Order status timeline | Reduces “where is my stuff?” anxiety | Vertical M3 timeline on request detail |
| Strong empty + error illustrations | Prevents dead ends | One headline, one sentence, one CTA |
| Light + dark parity | Trust on night deliveries | Full dual schemes in tokens |

### Improve (weak patterns in typical kits)

| Weak pattern | Problem | Hatli fix |
|--------------|---------|-----------|
| Food-photo masonry grids | Wrong content type; heavy scrolling | Compact request list rows + optional map |
| Nested card-in-card | Visual noise, hard Compose nesting | Single surface elevation; dividers inside |
| Oversaturated neon accents | Feels cheap; fails a11y | Mint teal primary + charcoal neutrals; dark onPrimary on teal |
| iOS large-title + blur chrome | Not Android idiomatic | M3 TopAppBar + NavigationBar |
| Floating promo badges on hero | Clutter, hard RTL | No overlays on media |
| Tiny icon-only filters | Miss 48dp targets | Chips ≥ 48dp height, text + optional icon |

### Drop

- Cart badge / checkout funnel as primary IA  
- Restaurant menu categories as home hero  
- Decorative glassmorphism / multi-layer shadows  
- Role-ambiguous “Order food” copy  

---

## 3. Color system

Brand: **mint teal** `#30E6C1` (energy, delivery motion) + **charcoal** `#1E1E1E` / `#262626` + **white** `#FFFFFF`.  
Not purple-indigo. Not cream/terracotta. Not deep forest (legacy).

### Brand anchors (from product)

| Swatch | Hex | Role |
|--------|-----|------|
| Primary | `#30E6C1` | Filled CTAs, key icons, active nav (`Teal500`) |
| Primary alt | `#30E5C0` | Same family (`Teal400`) |
| White | `#FFFFFF` | Light surfaces, onSecondary |
| Gray700 | `#262626` | Dark elevated surface / secondary |
| Gray800 | `#1E1E1E` | Dark background / onPrimary text |

### Semantic tokens (required palette)

| Token | Light | Usage |
|-------|-------|--------|
| **Primary** | `#30E6C1` Teal500 | Filled buttons, key icons, active nav |
| **OnPrimary** | `#1E1E1E` | Label on primary fills (contrast) |
| **Secondary** | `#262626` Gray700 | Dark chrome, secondary emphasis |
| **Surface** | `#FFFFFF` | Cards, sheets, dialogs |
| **Background** | `#F7F7F7` Gray50 | Scaffold |
| **Error** | `#B3261E` | Destructive, validation |
| **Warning** | `#C47F0A` | Pending verification, delays |
| **Success** | `#129A84` Teal700 | Delivered, verified, paid |
| **Info** | `#2F6FED` | Neutral tips, system notices |
| **Text Primary** | `#1E1E1E` | Titles, body |
| **Text Secondary** | `#757575` | Meta, hints |
| **Border** | `#E0E0E0` | Outlines, dividers |
| **Disabled** | `#9E9E9E` text / `#EEEEEE` fill | Inactive controls |

Containers (chips, banners): `ErrorContainer`, `WarningContainer`, `SuccessContainer`, `InfoContainer` — see `HatliColors` in `core/designsystem`.

### Contrast rules

- Body text on Background/Surface: ≥ **4.5:1**
- Large text / icons: ≥ **3:1**
- Primary filled button: **onPrimary (`#1E1E1E`) on Primary (`#30E6C1`)** — never white on teal
- Secondary charcoal is for chrome / dark CTAs; long body text uses TextPrimary
- Disabled must remain distinguishable from Secondary text (token, not opacity alone)

### Dark mode (fully applied)

| Layer | Light | Dark |
|-------|-------|------|
| Background | `#F7F7F7` | `#1E1E1E` |
| Surface | `#FFFFFF` | `#262626` |
| Primary | Teal500 `#30E6C1` | Teal500 `#30E6C1` |
| OnPrimary | `#1E1E1E` | `#1E1E1E` |
| Border | `#E0E0E0` | `#3A3A3A` |
| Success / Warning / Info | Light containers | Dark containers via `HatliExtendedColors` |

**Runtime wiring**

- Compose: `HatliTheme(themeMode = FollowSystem | Light | Dark)` switches M3 `ColorScheme` + `LocalHatliExtendedColors`
- Root `Surface` + `Scaffold` use `colorScheme.background`
- Status / nav bar icon contrast synced in `HatliTheme` (`WindowCompat`)
- XML: `Theme.Material.DayNight` + `values` / `values-night` colors for splash window
- Components (buttons, fields, cards, dialogs) read theme tokens — no hardcoded light-only colors
- Access semantics: `HatliTheme.extendedColors.success` (etc.), never raw `HatliColors.Success` in UI

Implementation: `HatliColors`, `HatliExtendedColors`, `HatliTheme` in `:core:designsystem`.

---

## 4. Typography

M3 type scale via `HatliTypography`. Hierarchy (Compose → role):

| Style | Size / weight | Use |
|-------|---------------|-----|
| `displayLarge` | 40 / Bold | Brand splash only |
| `headlineLarge` | 28 / SemiBold | Screen titles (auth, empty) |
| `titleLarge` | 22 / SemiBold | Section headers, dialog titles |
| `titleMedium` | 16 / Medium | List primary line, card titles |
| `bodyLarge` | 16 / Regular | Forms, readable body |
| `bodyMedium` | 14 / Regular | Secondary lines, helpers |
| `labelLarge` | 14 / Medium | Buttons, chips, nav labels |

**Rules**

- One headline per screen; never stack two `headline*` styles.
- Prefer `titleMedium` + `bodyMedium` for list density.
- Future: load a single custom family (e.g. Plus Jakarta Sans) via `FontFamily` — keep weights aligned to this scale. Avoid Inter/Roboto defaults in marketing surfaces when custom fonts land.

---

## 5. Spacing, touch, shape

### 8dp grid (`HatliSpacing`)

| Token | dp | Typical use |
|-------|-----|-------------|
| xxxs | 2 | Icon optical padding |
| xxs | 4 | Chip gap fine |
| xs | 8 | Tight related group |
| sm | 12 | Label → field |
| md | 16 | Screen horizontal inset (default) |
| lg | 24 | Section gap |
| xl | 32 | Auth vertical rhythm |
| xxl | 48 | Min touch / FAB clearance |

### Touch targets

- Minimum **48 × 48 dp** for all interactive elements
- List rows: min height 56–72 dp depending on two-line vs three-line
- Icon buttons: 48 dp with 24 dp glyph

### Radius (`HatliRadius`)

| Token | dp | Component |
|-------|-----|-----------|
| sm | 8 | Text fields, small chips |
| md | 12 | Buttons, list cards |
| lg | 16 | Sheets, large cards |
| xl | 24 | Bottom sheets top corners, FABs |

### Elevation

Prefer **outline + tonal surface** over heavy shadow (M3). Use `HatliElevation.sm/md` only for floating bars and FABs.

---

## 6. Component inventory

All feature UI must use design-system components — no raw Material in features (architecture rule).

| Component | Exists today | Spec |
|-----------|--------------|------|
| `HatliPrimaryButton` | Yes | Full-width default; height ≥ 48; loading spinner replaces label |
| `HatliSecondaryButton` | Yes | Outlined; same hit target |
| `HatliTextButton` | Yes | Tertiary / Skip |
| `HatliTextField` | Yes | Outlined; error + supporting text; 56 dp height |
| `HatliCard` | Yes | Surface + optional border; no nested cards |
| `HatliDialog` | Yes | Title, body, dismiss + confirm |
| **HatliChip** | Build | Filter / status; selected = primaryContainer |
| **HatliSearchBar** | Build | M3 SearchBar; leading search, trailing clear |
| **HatliBottomSheet** | Build | Modal sheet; drag handle; peeks for filters |
| **HatliSnackbarHost** | Build | Short, action optional; above nav bar |
| **HatliBadge** | Build | Count / status dot on nav & icons |
| **HatliProgress** | Build | Linear for determinate; circular for page load |
| **HatliStatusChip** | Build | Maps to Success/Warning/Error/Info containers |
| **HatliEmptyState** | Build | Illustration slot + title + body + CTA |
| **HatliErrorState`** | Build | Retry primary |
| **HatliOfflineBanner** | Build | Sticky under TopAppBar |
| **HatliNavBar** | Build | 3–4 destinations; role-aware labels |
| **HatliListRow`** | Build | Leading icon/avatar, title, subtitle, trailing meta |
| **HatliTimeline`** | Build | Vertical steps for delivery progress |

### Interaction states (every interactive component)

`Enabled → Pressed → Focused → Hovered (large screen) → Disabled → Loading`

Use M3 ripple; do not invent custom glow.

---

## 7. Navigation patterns

### Auth (no bottom nav)

Linear graph: Splash → Onboarding → Login → OTP → Role → Create Profile → Home.

### Main (role-aware bottom nav)

**Requester**

| Tab | Purpose |
|-----|---------|
| Home | Nearby context + quick post + active summary |
| Activity | My requests (active + history) |
| Profile | Account, addresses, payments, support |

Center FAB or Home primary: **New request**.

**Runner**

| Tab | Purpose |
|-----|---------|
| Jobs | Browse / map of open requests |
| Active | Current accepted job(s) |
| Earnings | Payouts summary (phase later can stub) |
| Profile | Verification, vehicle, support |

Back stack: single top for tabs; detail screens push on tab’s nav graph.

### Android system

- Predictive back on all stacks  
- Prefer Up in TopAppBar for nested screens  
- RTL: `start`/`end` only — never raw Left/Right in layout logic  

---

## 8. Cross-cutting UI states

Every list/detail screen must implement:

| State | Pattern |
|-------|---------|
| **Loading** | Skeleton rows (lists) or centered circular (short pages) |
| **Content** | Default |
| **Empty** | `HatliEmptyState` + one CTA |
| **Error** | Inline or full-page + Retry |
| **Offline** | Banner; allow cached read; disable mutating CTAs |
| **Success** | Snackbar or dedicated confirmation; then navigate |

ViewModels expose sealed `UiState` branches — Compose maps 1:1 (UDF).

---

## 9. Accessibility

- Content descriptions on all icon-only controls  
- Live regions for OTP timer and delivery status updates  
- Font scale up to 200%: avoid fixed-height text boxes that clip  
- TalkBack order = visual reading order; group related actions  
- Color is never the only status signal (icon + label + chip)  
- Minimum contrast as above  

---

## 10. Compose / architecture fit

Designed for:

- Jetpack Compose + Material 3  
- MVVM + UDF (`UiState` / `UiEvent` / `UiEffect`)  
- Clean Architecture (features depend on `core/*` only)  
- Design-system components only in feature UI  

Avoid: overlapping `Box` absolute layouts, nested scroll fights, custom canvas chrome that cannot use `Modifier` semantics.
