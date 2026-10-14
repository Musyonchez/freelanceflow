# 19 — UI Design System

This document transcribes the visual language from the Figma Make export (`miscellaneous/figma-ui-export/`) into a concrete, Compose-ready design system. Per the authority rule for this pass: **functionality, screen structure, and data stay exactly as implemented in the app today** ([10-ui-ux-and-screens](10-ui-ux-and-screens.md)); **only visual styling — color, type, spacing, shape, elevation, iconography, component skin — comes from the Figma mock.** The app's current theme (`Color.kt`, `Theme.kt`, `Type.kt`) is the untouched Android Studio template default, so nothing there is being "preserved" — this is a wholesale replacement.

All token names below (`brand`, `brandDark`, `mint`, `canvas`, etc.) are used identically in [20-ui-migration-plan](20-ui-migration-plan.md) — treat this doc as the single source of truth for names and values.

## 1. Color palette

### 1.1 Raw tokens (from `index.css` `:root`)

| Token | CSS value | Kotlin |
|---|---|---|
| `ink` | `#17201c` | `Color(0xFF17201C)` |
| `muted` | `#76817b` | `Color(0xFF76817B)` |
| `line` | `#e6ebe7` | `Color(0xFFE6EBE7)` |
| `canvas` | `#f3f5f2` | `Color(0xFFF3F5F2)` |
| `surface` | `#ffffff` | `Color(0xFFFFFFFF)` |
| `surfaceSoft` | `#f7f9f6` | `Color(0xFFF7F9F6)` |
| `brand` | `#175f4a` | `Color(0xFF175F4A)` |
| `brandDark` | `#104737` | `Color(0xFF104737)` |
| `mint` | `#c8f2df` | `Color(0xFFC8F2DF)` |
| `purple` | `#ddd9fa` | `Color(0xFFDDD9FA)` |
| `peach` | `#f9ddcf` | `Color(0xFFF9DDCF)` |
| `amber` | `#f7e8b7` | `Color(0xFFF7E8B7)` |
| `blue` | `#d9eaf8` | `Color(0xFFD9EAF8)` |

### 1.2 Derived tints (from component rules, not `:root`, but used consistently — needed for icon badges, avatars, tags)

| Token | Value | Source rule |
|---|---|---|
| `timerDark` | `#163e32` | `.timer-card` background |
| `onMintBrand` | same as `brand` (`#175F4A`) | text-on-mint (`.brand-mark`, `.metric-icon.green`) uses `brand-dark`/`brand` interchangeably; standardize on `brand` |
| `onPurple` | `#625b8d` | `.metric-icon.purple` icon color |
| `avatarOnPurple` | `#585081` | `.avatar-violet` text color (slightly darker than `onPurple` — keep distinct, avatars vs. icon badges use different shades in the source) |
| `onPeach` | `#8b563f` | `.metric-icon.peach` icon color |
| `onBlue` | `#3e6887` | `.metric-icon.blue` icon color |
| `avatarOnAmber` | `#735f2b` | `.avatar-amber` text color |
| `avatarOnDark` | `#26372f` | `.avatar-dark` background |
| `tagBlueBg` / `tagBlueFg` | `#e6f2fa` / `#3b6e91` | `.tag-blue` (invoice "sent") |
| `tagAmberBg` / `tagAmberFg` | `#fff1d4` / `#8a681f` | `.tag-amber` (invoice "due"/overdue) |
| `tagGreenBg` / `tagGreenFg` | `#e3f6eb` / `#175f4a` (=`brand`) | `.tag-green` (invoice "paid") |
| `trendBg` / `trendFg` | `#e7f7ee` / `#175f4a` | `.metric-trend` chip |
| `alertBg` / `alertFg` | `#fff2d8` / `#906b20` | `.metric-alert` chip |
| `heroSubtext` | `#b9d6cc` | `.overline` / `.hero-stats span` inside hero card |
| `heroAccent` | `#bff3d7` | `.balance-note span` highlighted figure |
| `timerSubtext` | `#c3d9d1` / `#91ada3` | `.timer-label` / `.timer-row p` |
| `liveDotIdle` | `#8b9b95` | `.live-dot` |
| `liveDotRunning` | `#78dfa6` | `.live-dot.running` |
| `navInactive` | `#8a948e` | `.bottom-nav button` (inactive) |

```kotlin
// ui/theme/Color.kt
val Ink = Color(0xFF17201C)
val Muted = Color(0xFF76817B)
val Line = Color(0xFFE6EBE7)
val Canvas = Color(0xFFF3F5F2)
val Surface = Color(0xFFFFFFFF)
val SurfaceSoft = Color(0xFFF7F9F6)
val Brand = Color(0xFF175F4A)
val BrandDark = Color(0xFF104737)
val Mint = Color(0xFFC8F2DF)
val Purple = Color(0xFFDDD9FA)
val Peach = Color(0xFFF9DDCF)
val Amber = Color(0xFFF7E8B7)
val Blue = Color(0xFFD9EAF8)
val TimerDark = Color(0xFF163E32)

val OnPurpleIcon = Color(0xFF625B8D)
val AvatarOnPurple = Color(0xFF585081)
val OnPeachIcon = Color(0xFF8B563F)
val OnBlueIcon = Color(0xFF3E6887)
val AvatarOnAmber = Color(0xFF735F2B)
val AvatarOnDark = Color(0xFF26372F)

val TagBlueBg = Color(0xFFE6F2FA); val TagBlueFg = Color(0xFF3B6E91)
val TagAmberBg = Color(0xFFFFF1D4); val TagAmberFg = Color(0xFF8A681F)
val TagGreenBg = Color(0xFFE3F6EB); val TagGreenFg = Brand
val TrendBg = Color(0xFFE7F7EE); val TrendFg = Brand
val AlertBg = Color(0xFFFFF2D8); val AlertFg = Color(0xFF906B20)

val HeroSubtext = Color(0xFFB9D6CC)
val HeroAccent = Color(0xFFBFF3D7)
val TimerSubtextStrong = Color(0xFFC3D9D1)
val TimerSubtextWeak = Color(0xFF91ADA3)
val LiveDotIdle = Color(0xFF8B9B95)
val LiveDotRunning = Color(0xFF78DFA6)
val NavInactive = Color(0xFF8A948E)
```

### 1.3 Material 3 role mapping — light scheme (observed, not inferred)

| M3 role | Token | Value |
|---|---|---|
| `primary` | `brand` | `#175F4A` |
| `onPrimary` | white | `#FFFFFF` |
| `primaryContainer` | `mint` | `#C8F2DF` |
| `onPrimaryContainer` | `brandDark` | `#104737` |
| `secondary` | `purple` | `#DDD9FA` |
| `onSecondary` | `onPurple` | `#625B8D` |
| `secondaryContainer` | `blue` | `#D9EAF8` |
| `onSecondaryContainer` | `onBlue` | `#3E6887` |
| `tertiary` | `peach` | `#F9DDCF` |
| `onTertiary` | `onPeach` | `#8B563F` |
| `tertiaryContainer` | `amber` | `#F7E8B7` |
| `onTertiaryContainer` | `avatarOnAmber` | `#735F2B` |
| `background` | `canvas` | `#F3F5F2` |
| `onBackground` | `ink` | `#17201C` |
| `surface` | `surface` | `#FFFFFF` |
| `onSurface` | `ink` | `#17201C` |
| `surfaceVariant` | `surfaceSoft` | `#F7F9F6` |
| `onSurfaceVariant` | `muted` | `#76817B` |
| `outline` | `line` | `#E6EBE7` |
| `outlineVariant` | `line` at reduced emphasis | `#E6EBE7` |
| `error` | not present in mock — keep M3 default red (`#B3261E`) | not observed, standard M3 |
| `onError` | M3 default | `#FFFFFF` |
| `inverseSurface` | `brandDark` | `#104737` |
| `inverseOnSurface` | white | `#FFFFFF` |

`error` is not represented anywhere in the Figma source (no error/destructive states were mocked), so the standard Material 3 baseline error red is kept as-is — this is a gap, not a judgment call to override.

### 1.4 Dark scheme — **inferred, not observed**

The Figma Make export has no dark-mode artboards or `prefers-color-scheme` rules. The app currently supports system dark mode via `isSystemInDarkTheme()` in `Theme.kt`, so a dark scheme must exist; the following is a derivation, flagged explicitly as a judgment call:

| M3 role | Value | Rationale |
|---|---|---|
| `primary` | `#8FD6BC` (lightened `brand`) | brand green needs to lighten to stay legible on a dark background, same hue |
| `onPrimary` | `#0B2A20` | dark ink-on-mint-like pairing |
| `primaryContainer` | `#1B5143` | darkened `brand`, roughly between `brand` and `brandDark` |
| `onPrimaryContainer` | `mint` `#C8F2DF` | kept as the light accent it already is |
| `background` | `#121613` | deepened `canvas`, keeping its slightly-warm neutral-green cast |
| `onBackground` | `#E7ECE8` | light `ink` inverse |
| `surface` | `#1A1F1C` | deepened `surface`, same relationship to background as light mode (+1 step lighter) |
| `onSurface` | `#E7ECE8` | |
| `surfaceVariant` | `#222822` | deepened `surfaceSoft` |
| `onSurfaceVariant` | `#A9B3AD` | lightened `muted` |
| `outline` | `#38423C` | deepened `line` |
| `secondary` / `tertiary` and containers | keep light-mode hex as containers, use darker text-safe pairs | the purple/peach/amber/blue tints are pastel accents that still read fine on dark surfaces as small badges; invert only the large-surface roles |

This dark scheme is a reasonable derivation, not a design decision verified against any mock. Flag it for designer review before shipping; it is listed again in the risk list at the end of [20-ui-migration-plan](20-ui-migration-plan.md).

## 2. Typography

Two Google Fonts are used in the mock: **Manrope** (600/700/800 — headings, balances, numbers) and **DM Sans** (400/500/600/700 — body default). Both are real, freely licensed Google Fonts and both are available through Compose's downloadable-font API (`androidx.compose.ui.text.googlefonts.GoogleFont`), so no font files need to be bundled as raw assets — see Phase 1 setup in [20-ui-migration-plan](20-ui-migration-plan.md) for the exact dependency and provider wiring.

### 2.1 Anchors observed directly in `index.css`

| CSS rule | Size/weight/line-height/letter-spacing | Font |
|---|---|---|
| `h1` | 28px / 800 / 1.25 / −0.035em | Manrope |
| `h2` | 19px / 700 / 1.3 / −0.025em | Manrope |
| `.balance` | 42px / 800 / 1.1 / −0.05em | Manrope |
| `.timer-value` | 21px / 700 / normal / 0.02em | Manrope |
| `.metric-card strong` | 19px / 700 | Manrope |
| `.workspace-stat p` | 28px / 800 / −0.04em | Manrope |
| `.project-copy strong` / `.invoice-copy strong` | 13px / 400 (body weight) | DM Sans |
| `.eyebrow` / `.overline` | 11px / 700 / 0.11em / uppercase | DM Sans |
| `.tag` | 9px / 700 | DM Sans |
| `.bottom-nav button` | 10px / 600 | DM Sans |
| base body (`:root` font-family) | 400 default | DM Sans |

### 2.2 Full Material 3 `Typography` scale (extrapolated from the anchors above)

sp is used 1:1 with the CSS px values (standard mdpi baseline assumption, consistent with the rest of the app's existing dp/sp usage).

| M3 slot | Font | Weight | Size | Line height | Letter spacing | Basis |
|---|---|---|---|---|---|---|
| `displayLarge` | Manrope | 800 | 48sp | 54sp | −0.05em | extrapolated above `.balance` |
| `displayMedium` | Manrope | 800 | 42sp | 46sp | −0.05em | `.balance` (observed) |
| `displaySmall` | Manrope | 700 | 34sp | 40sp | −0.04em | between `.balance` and `h1` |
| `headlineLarge` | Manrope | 800 | 28sp | 35sp | −0.035em | `h1` (observed) |
| `headlineMedium` | Manrope | 700 | 24sp | 30sp | −0.03em | mobile `h1` override (`font-size: 24px` under 800px) |
| `headlineSmall` | Manrope | 700 | 21sp | 27sp | −0.025em | `.timer-value` (observed) |
| `titleLarge` | Manrope | 700 | 19sp | 25sp | −0.025em | `h2` / `.metric-card strong` (observed) |
| `titleMedium` | Manrope | 700 | 16sp | 22sp | −0.02em | extrapolated step down from `titleLarge` |
| `titleSmall` | DM Sans | 600 | 14sp | 20sp | 0em | extrapolated, body-weight heading |
| `bodyLarge` | DM Sans | 400 | 15sp | 22sp | 0em | extrapolated, app's general body text |
| `bodyMedium` | DM Sans | 400 | 13sp | 19sp | 0em | `.project-copy strong` / `.invoice-copy strong` (observed) |
| `bodySmall` | DM Sans | 400 | 12sp | 17sp | 0em | `.workspace-row small` / `.balance-note` (observed ~12-13px) |
| `labelLarge` | DM Sans | 700 | 13sp | 18sp | 0.01em | buttons, e.g. `.primary-button` |
| `labelMedium` | DM Sans | 700 | 11sp | 15sp | 0.11em, uppercase | `.eyebrow` / `.overline` (observed) |
| `labelSmall` | DM Sans | 700 | 9sp | 12sp | 0.02em | `.tag` (observed); bottom-nav label uses 10sp but maps closest to this slot |

```kotlin
// ui/theme/Type.kt (sketch)
val ManropeFamily = FontFamily(Font(GoogleFont("Manrope"), provider, FontWeight.SemiBold /*600*/),
    Font(GoogleFont("Manrope"), provider, FontWeight.Bold /*700*/),
    Font(GoogleFont("Manrope"), provider, FontWeight.ExtraBold /*800*/))
val DMSansFamily = FontFamily(Font(GoogleFont("DM Sans"), provider, FontWeight.Normal),
    Font(GoogleFont("DM Sans"), provider, FontWeight.Medium),
    Font(GoogleFont("DM Sans"), provider, FontWeight.SemiBold),
    Font(GoogleFont("DM Sans"), provider, FontWeight.Bold))
```

## 3. Spacing scale

The source is not a clean 8dp grid — observed padding/gap values include 5, 7, 8, 9, 10, 12, 13, 14, 16, 17, 18, 20, 22, 24, 28, 32. Rather than force these onto 8dp, use a **4dp-based scale** (covers every observed value either exactly or within 1dp, which is an acceptable rounding given these are CSS px, not a strict dp grid):

| Name | Value | Covers observed |
|---|---|---|
| `space1` | 4dp | — |
| `space2` | 8dp | 7, 8, 9 (round to nearest 4) |
| `space3` | 12dp | 12, 13 |
| `space4` | 16dp | 16, 17 |
| `space5` | 20dp | 18 (round up), 20 |
| `space6` | 24dp | 22 (round up), 24 |
| `space7` | 28dp | 28 |
| `space8` | 32dp | 32 |

Use `space2`/`space3` for tight internal gaps (icon-to-text, chip padding), `space4`/`space5` for card internal padding, `space6` for card-to-card and section gaps, `space7`/`space8` for page-level top padding and hero card padding.

## 4. Shape / corner radius

| Component | Radius | Source |
|---|---|---|
| Hero / brand card | 24dp (22dp on narrow layouts) | `.hero-card` / mobile override |
| Workspace summary card | 24dp | `.workspace-summary` |
| Timer card | 0dp top / 18dp bottom corners only | `.timer-card` (`border-radius: 0 0 18px 18px`) — sits flush under the hero card |
| Panel (list container) | 20dp | `.panel` |
| Metric card / workspace-stat card | 18dp | `.metric-card` / `.workspace-stat` |
| Action-grid button (sheet) | 16dp | `.action-grid button` |
| Avatar / metric-icon / document-icon badge | 13dp (brand-mark uses 14dp) | `.avatar`, `.metric-icon`, `.document-icon`, `.brand-mark` |
| Icon button | 11dp | `.icon-button` |
| Primary button | 13dp | `.primary-button` |
| Timer control (round button) | 12dp | `.timer-control` |
| FAB (floating-button) | 18dp | `.floating-button` |
| Month chip / badge pill | 12dp | `.month-chip` |
| Tag / status chip | 7dp | `.tag` |
| Action sheet | 24dp top corners only, 0 bottom (full-bleed to screen edge on mobile) | `.action-sheet` mobile override |
| Sheet drag handle | fully rounded (10dp on a 4dp-tall bar = pill) | `.sheet-handle` |

```kotlin
// ui/theme/Shape.kt (sketch) — M3 Shapes slots + named extras for things M3 has no slot for
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(7.dp),   // tags
    small = RoundedCornerShape(11.dp),       // icon buttons
    medium = RoundedCornerShape(18.dp),      // metric/workspace-stat cards
    large = RoundedCornerShape(20.dp),       // panels
    extraLarge = RoundedCornerShape(24.dp),  // hero cards, action sheet top
)
val FabShape = RoundedCornerShape(18.dp)
val TimerControlShape = RoundedCornerShape(12.dp)
val PrimaryButtonShape = RoundedCornerShape(13.dp)
val TimerCardShape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 18.dp, bottomEnd = 18.dp)
val ActionSheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
```

## 5. Elevation / shadow

The mock uses CSS box-shadows (soft, diffuse, tinted toward the dark-green ink rather than pure black), which Android's default tonal elevation does not reproduce. Use `Modifier.shadow()` with an explicit, tinted ambient/spot color via a custom approach (M3's `Modifier.shadow(elevation, shape, ambientColor, spotColor)` supports colored shadows directly):

| Token | CSS | Compose approximation |
|---|---|---|
| `shadowHero` | `0 16px 50px rgba(38,61,51,.09)` | `Modifier.shadow(elevation = 16.dp, shape = RoundedCornerShape(24.dp), ambientColor = Color(0x17263D33), spotColor = Color(0x17263D33))` — used on hero card and action sheet |
| `shadowPanel` | `0 8px 30px rgba(41,58,50,.035)` | `Modifier.shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp), ambientColor = Color(0x09293A32), spotColor = Color(0x09293A32))` — used on panels |
| `shadowFab` | `0 10px 25px rgba(23,95,74,.28)` | `Modifier.shadow(elevation = 10.dp, shape = FabShape, ambientColor = Color(0x47175F4A), spotColor = Color(0x47175F4A))` — used on the FAB |

(Alpha hex: `.09`→`0x17`, `.035`→`0x09`, `.28`→`0x47`, applied to the RGB from each `rgba()`.) Metric cards and standard `ListItem` rows use **no shadow** in the mock — just a 1px `line`-colored border — so leave those at Compose's default flat `Card`/border treatment rather than adding elevation.

## 6. Iconography

The mock's icons are hand-drawn 24×24 stroke SVGs (`strokeWidth="1.8"`, round caps/joins, `currentColor`) — not a bundled icon font. Recommendation: **do not hand-author custom vector icons for this pass.** `material-icons-extended` is already a dependency (`mobile/app/build.gradle.kts`), and its **Outlined** variants are visually close enough (thin stroke weight, rounded terminals) to stand in:

| Mock icon | Used for | Material icon substitute |
|---|---|---|
| `home` | bottom-nav Home | `Icons.Outlined.Home` |
| `briefcase` | bottom-nav Work | `Icons.Outlined.Work` (or `WorkOutline`) |
| `wallet` | bottom-nav Money | `Icons.Outlined.AccountBalanceWallet` |
| `people` | bottom-nav Clients | `Icons.Outlined.People` |
| `clock` | timer, time entries | `Icons.Outlined.Schedule` |
| `plus` | add actions, FAB | `Icons.Outlined.Add` (FAB stays filled-weight per M3 FAB convention) |
| `receipt` | expense receipts | `Icons.Outlined.Receipt` |
| `invoice`/`file` | invoice/document rows | `Icons.Outlined.Description` |
| `calendar` | due dates | `Icons.Outlined.CalendarToday` |
| `check` | confirmations | `Icons.Outlined.Check` |
| `chevron` | row disclosure | `Icons.Outlined.ChevronRight` |
| `close` | sheet/dialog dismiss | `Icons.Outlined.Close` |
| `menu` | overflow | `Icons.Outlined.Menu` |
| `pause` | timer pause | `Icons.Outlined.Pause` |
| `arrow` | text-button trailing arrow | `Icons.AutoMirrored.Outlined.ArrowForward` |

If pixel-exact custom icons are wanted later, that is a separate follow-up (hand-tracing the SVGs as `ImageVector`s) — out of scope for this pass.

## 7. Component visual specs

### 7.1 Hero / brand card
`brand` (`#175F4A`) background, white text, `Shapes.extraLarge` (24dp) corners, `shadowHero`, min-height ~220dp, internal padding `space6`×`space7` (24dp horizontal-ish/28dp). Top row: eyebrow label in `heroSubtext` + a `monthChip`-style pill (`rgba(255,255,255,.08)` fill ≈ `Color(0x14FFFFFF)`, `rgba(255,255,255,.18)` 1dp border ≈ `Color(0x2EFFFFFF)`, 12dp corners). Big figure in `headlineLarge`/`displayMedium` (Manrope 800). Below: a 3-column stat row, hairline white-alpha (`Color(0x24FFFFFF)`) vertical dividers between columns, each column's label in `heroSubtext` 12sp and value in `titleLarge`-ish 18sp/700 Manrope.

### 7.2 Dark timer card
Sits flush under the hero card (negative top margin in source — in Compose, just omit the gap/overlap by placing it directly after with 0 spacing and clipping its top corners to 0). `timerDark` (`#163E32`) background, bottom corners only `TimerCardShape` (18dp), white text. A "live dot" (7dp circle) — `liveDotIdle` normally, `liveDotRunning` with a soft glow ring when a timer is active (approximate the CSS glow with a second, larger, low-alpha circle behind it, or a `Modifier.shadow` with `spotColor = LiveDotRunning.copy(alpha = .12f)`). Elapsed time in `headlineSmall` (Manrope 700, tabular figures — use `FontFeatureSettings("tnum")` or a monospaced-digit-safe font variant). Round pause/resume control, 38dp, `TimerControlShape` (12dp), `mint` fill when running / white fill when paused, icon in `brandDark`.

### 7.3 Metric card (2-column grid)
White `surface`, 1dp `line` border, `Shapes.medium` (18dp), padding `space4` (17px≈16dp), `Row` layout: icon badge (42dp square, `Shapes.small`-ish 13dp, tinted background from {purple, peach, mint, blue} with matching `on*` icon color) + label/value/sublabel column (label `labelMedium`-ish 11sp muted, value `titleLarge` 19sp/700 Manrope, sublabel 11sp muted) + optional trailing chip (`trendBg`/`trendFg` or `alertBg`/`alertFg`, 8dp corners, 11sp/700, padded 5dp×8dp). No shadow — border only.

### 7.4 Panel / list rows
Panel container: white `surface`, `Shapes.large` (20dp), `shadowPanel`. Header row: title (`titleLarge`) + either a text-button ("See all", `brand` color, 12sp/700, trailing chevron) or an `icon-button` (36dp, `Shapes.small`-ish 11dp, 1dp `line` border).

- **Project row** (Work-style, progress-bar variant): avatar/leading element + title/value row + client name (muted, 11sp) + a 4dp-tall progress track (`#EDF0ED` background, `brand` fill, fully rounded), 1dp `line` top divider between rows, 14dp vertical row padding.
- **Invoice/workspace row**: document-icon or avatar leading (38-42dp, `Shapes.small`), title/meta column, trailing value (right-aligned, 12sp/700) stacked above a status `tag`, or a trailing chevron for pure navigation rows. Same 1dp top divider rhythm.

### 7.5 Status tag / chip
Pill, `Shapes.extraSmall` (7dp) corners, padding 3dp×7dp, text 9sp/700. Color pairs: blue (sent) = `TagBlueBg`/`TagBlueFg`, amber (due/overdue) = `TagAmberBg`/`TagAmberFg`, green (paid) = `TagGreenBg`/`TagGreenFg`. Reuse this exact pill for any other binary/tri-state status in the app (deductible flag, sync status) by picking the closest semantic color.

### 7.6 Bottom navigation bar
4 equal-width items — Home / Work / Money / Clients, in that order, mapped 1:1 to the app's existing `FreelanceFlowDestination` entries (no change to destinations or order). Visually: translucent white (`Surface.copy(alpha = 0.95f)`) with a blur approximation (see migration-plan risk list — Compose's `Modifier.blur`/`HazeMaterials`-style backdrop blur is the closest match, a flat 95%-alpha fill is the pragmatic fallback), 1dp top border in `line`, height ~72dp. Inactive icon/label color `navInactive` (`#8A948E`), active color `brand`. Labels 10sp/600 DM Sans.

### 7.7 Floating action button
54dp square (round up to M3's standard 56dp — negligible difference, keeps default FAB touch-target guidance), `FabShape` (18dp corners — a "squircle," not M3's default fully-round small FAB), `brand` background, white icon, `shadowFab`. Bottom-end placement, matches the app's existing per-screen FAB placement exactly.

### 7.8 Bottom sheet ("action sheet" style)
Scrim: `rgba(13,27,21,.5)` ≈ `Color(0x80163E32)`-ish dark-green-tinted scrim with blur — Compose `ModalBottomSheet` already supplies a scrim; recolor it via `scrimColor` param to this tinted value instead of the M3 default. Sheet container: white, `ActionSheetShape` (24dp top corners only), drag handle 42dp×4dp pill in `#DFE4E0`, title row (title + `icon-button`-style close). Where the sheet's content is a picker/launcher grid (e.g. the receipt-photo Camera/Gallery choice), lay it out as a 2-column grid of large tappable cards: 1dp `line` border, `Shapes.medium`-ish (16dp) corners, 16dp padding, icon badge (same tint badges as metric-icon) + title (`titleSmall`/14sp 600) + subtitle (muted, 11sp) stacked top-aligned.

### 7.9 Icon buttons / primary / text buttons
Icon button: 36dp square, `Shapes.small` (11dp), 1dp `line` border, white fill. Primary button: `brand` fill, white text, `PrimaryButtonShape` (13dp), padding 12dp×18dp, `labelLarge` 13sp/700, leading/trailing icon gap 8dp. Text button: transparent, `brand` text color, 12sp/700, used for inline "See all"/navigational actions.

### 7.10 Avatars
42dp square, `Shapes.small`-ish (13dp) corners (never circular — the whole system uses rounded-square badges, not circles), initials in 12sp/700. Four tone pairs to rotate across clients/users for visual variety: dark (`AvatarOnDark` bg / white text), violet (`purple` bg / `AvatarOnPurple` text), amber (`amber` bg / `AvatarOnAmber` text), mint (`mint` bg / `brand` text).

Next: [20 — UI Migration Plan](20-ui-migration-plan.md)
