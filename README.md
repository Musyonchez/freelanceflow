# FreelanceFlow

A native Android app that gives remote freelancers one place to manage clients, projects, time, multi-currency invoicing, expenses, and tax deductions — built as a university SWE unit project (student: Daleen Hunnipher Otieno, reg. 672511), expanded from the original brief ("Multi-Currency Invoice & Expense Generator") into a full freelance business-management platform.

## Status

Actively in development, built in staged, independently-committed increments. Implemented so far:

- **Clients** — add/edit/archive, list and detail views
- **Projects** — hourly/fixed rate, linked to a client, status tracking
- **Time tracking** — start/stop timer and manual entries, unbilled-time rollup
- **Invoicing** — builder pulling in logged time or manual line items, live exchange-rate conversion (cached, never fabricated), sequential invoice numbering, status tracking (draft/sent/paid/overdue), payment recording
- **PDF generation & sharing** — on-device invoice PDF via `FileProvider`
- **Expenses** — logging, deductible flag with partial-deduction percent
- **Tax estimation** — user-defined deduction profile, period-filtered deductible totals (explicitly labelled as an estimate, not a filing)
- **Dashboard** — multi-currency totals converted to a user-set base currency
- **Auth** — Supabase email/password sign-in and sign-up
- **Cloud sync** — bidirectional push/pull of every table against Supabase Postgres (RLS-scoped per user), triggered on sign-in and hourly
- **Unit tests** — money/currency-conversion math and invoice-numbering logic (the highest-risk-of-silent-bugs areas per the test plan)
- **Overdue-invoice notifications** — periodic local check flags sent invoices past their due date and fires a real system notification

Not yet built: receipt photo capture, reporting/CSV export, deletion-aware sync.

## Project structure

| Path | Contents |
|---|---|
| [`docs/`](docs/) | Full system design docs — read [`docs/README.md`](docs/README.md) first, documents are numbered and build on each other |
| [`guide/`](guide/) | Beginner's walkthrough — from opening the GitHub page to cloning, setup, running, and using the app, start to finish |
| [`mobile/`](mobile/) | The Android Studio project (Kotlin, Jetpack Compose, Hilt, Room) |
| [`supabase/`](supabase/) | `schema.sql` — the Postgres schema + RLS policies backing cloud sync, run once in a fresh Supabase project's SQL editor |
| [`miscellaneous/`](miscellaneous/) | The original ChatGPT-generated concept and the university's concept-format submission |

## Architecture

Offline-first, layered (Clean Architecture) + MVVM. Room is always the local source of truth; Supabase (Auth, Postgres, Storage) is a sync target the app never blocks on. Money is stored as integer minor units (cents) to avoid floating-point rounding. Full rationale and the complete tech stack are in [`docs/07-system-architecture.md`](docs/07-system-architecture.md).

## Running it

1. Open `mobile/` in Android Studio (requires AGP 9.4+ support — see [`docs/07-system-architecture.md`](docs/07-system-architecture.md) for the full version matrix).
2. Create a Supabase project and run [`supabase/schema.sql`](supabase/schema.sql) in its SQL editor.
3. In `mobile/local.properties`, set `SUPABASE_URL` and `SUPABASE_ANON_KEY` from the project's Settings → API page (never commit this file — it's gitignored).
4. Build and run.

New to the project or to Android development? [`guide/`](guide/README.md) walks through every one of these steps in much more detail, starting from the GitHub page itself.

## Documentation

See [`docs/README.md`](docs/README.md) for the full system design doc set (requirements, architecture, database design, UI/UX, testing plan, security/compliance, timeline, and more).
