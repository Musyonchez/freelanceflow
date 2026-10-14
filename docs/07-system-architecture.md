# 07 — System Architecture

## Architectural style

**Offline-first, layered (Clean Architecture) + MVVM**, native Android. The local database is the single source of truth for the UI; a remote backend (Supabase) is a sync target, not a dependency the app blocks on.

```
┌───────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                     │
│  Jetpack Compose screens  +  ViewModels (state holders)    │
│  Clients | Projects | Time | Invoices | Expenses | Reports │
└───────────────────────────▲─────────────────────────────---┘
                             │ StateFlow / UI state
┌───────────────────────────┴───────────────────────────────┐
│                       DOMAIN LAYER                         │
│  Use cases (plain Kotlin, no Android deps):                │
│  CreateInvoiceUseCase, ConvertCurrencyUseCase,              │
│  EstimateDeductionsUseCase, SyncUseCase, ...                │
└───────────────────────────▲─────────────────────────────---┘
                             │ Repository interfaces
┌───────────────────────────┴───────────────────────────────┐
│                        DATA LAYER                          │
│  Repositories (single source of truth logic)               │
│  ┌───────────────┐   ┌────────────────┐  ┌───────────────┐ │
│  │  Room (local)  │   │ Retrofit (API) │  │ Supabase SDK  │ │
│  │  Clients,      │   │ Exchange-rate  │  │ Auth,         │ │
│  │  Projects,     │   │ provider       │  │ Postgres,     │ │
│  │  Invoices,     │   └────────────────┘  │ Storage       │ │
│  │  Expenses...   │                       └───────────────┘ │
│  └───────────────┘                                          │
└───────────────────────────────────────────────────────────┘
```

## Layer responsibilities

- **Presentation:** Jetpack Compose UI + ViewModel per screen, exposing immutable UI state via `StateFlow`. No business logic here.
- **Domain:** Pure Kotlin use cases holding business rules (currency conversion, invoice numbering, deduction estimation, sync orchestration). Framework-agnostic and unit-testable in isolation.
- **Data:** Repositories combine a local Room database (always read/written first) with remote sources (Retrofit for the exchange-rate API, Supabase for auth/cloud sync/file storage). A background sync worker reconciles local changes with the cloud.

## Technology stack

| Concern | Choice | Why |
|---|---|---|
| Language | Kotlin | Modern Android standard, null-safety, coroutines. |
| UI toolkit | Jetpack Compose (Material 3) | Declarative UI, faster to build/iterate for a semester project. |
| Architecture | MVVM + Clean Architecture (light) | Testable, separates business rules from UI and framework. |
| DI | Hilt | Standard Android dependency injection, reduces boilerplate. |
| Local DB | Room (SQLite) | Offline-first source of truth, relational fit for clients/projects/invoices. |
| Async | Kotlin Coroutines + Flow | Structured concurrency, reactive UI state. |
| Networking | Retrofit + OkHttp | Exchange-rate API calls, standard and well-documented. |
| Auth / Cloud sync | Supabase Auth + Postgres (via `supabase-kt` / PostgREST) | Serverless backend appropriate for a solo-freelancer, single-owner data model; Postgres is also a natural sync target for a relational Room schema. Avoids building/hosting a custom backend for an academic timeline. |
| File storage | Supabase Storage | Receipt photos, generated invoice PDFs. |
| Background work | WorkManager | Deferred sync, periodic exchange-rate refresh, overdue-invoice checks. |
| PDF generation | Android `PdfDocument` API (or iText/PDFBox-Android if richer layout is needed) | Renders the invoice template to a shareable PDF on-device. |
| Testing | JUnit, Turbine, Compose UI testing, MockK | Unit + UI test coverage (see [13-testing-and-qa-plan](13-testing-and-qa-plan.md)). |

## Why Supabase over a custom backend

Given a single academic semester and a single-owner data model (no team collaboration, no complex server-side business rules beyond what the client can compute), a custom REST backend would spend scarce project time on server hosting/auth/infra rather than the app's actual value: client/project/invoice modeling, currency logic, and PDF generation. Supabase's free tier, built-in auth, and hosted Postgres (with Row Level Security) match the offline-first requirement directly, and its relational schema maps naturally onto the same tables already modeled for Room — no NoSQL re-modeling step. This is documented as a deliberate trade-off, not an oversight — see [17-resources-required](17-resources-required.md) for cost/limits.

## Sync strategy

- Room is written to first, immediately, for every user action (instant, offline-safe).
- Each write is queued for sync via WorkManager.
- On connectivity, queued changes push to Supabase Postgres (via `supabase-kt`/PostgREST); remote changes pull down and merge using **last-write-wins per record**, timestamped — a documented, simple conflict policy appropriate for a single-user-per-account app (see [04-scope-and-boundaries](04-scope-and-boundaries.md) for the limitation this implies).

Next: [08 — Database Design](08-database-design.md)
