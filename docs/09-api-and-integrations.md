# 09 — APIs and Third-Party Integrations

## Exchange rate provider

Candidates (final choice made during implementation based on free-tier limits at the time): **ExchangeRate-API.com**, **Open Exchange Rates**, or **Frankfurter.app** (no key required, ECB-sourced, good for an academic project with no billing risk).

- **Usage pattern:** fetch a currency pair's rate when (a) creating a new invoice, (b) refreshing the dashboard's currency breakdown, capped to a reasonable refresh interval (e.g., once per hour per pair) to respect free-tier rate limits.
- **Caching:** every successful fetch is written to the `ExchangeRateCache` table ([08-database-design](08-database-design.md)); when offline or rate-limited, the app falls back to the most recent cached rate and visibly labels it with its fetch timestamp ("rate as of 14 Sep, 09:12").
- **Failure handling:** if no cached rate exists and the API is unreachable, the invoice draft is still allowed to save (as a draft) but cannot be finalized/sent until a rate is available — this prevents shipping an invoice with a fabricated conversion.

## Supabase

- **Auth:** email/password + Google sign-in (Supabase Auth, OAuth provider), single account per user.
- **Postgres database:** mirrors the Room schema per authenticated user — one `clients`/`projects`/`invoices`/`expenses`/... table each, every row scoped by a `user_id` column; **Row Level Security (RLS)** policies restrict all reads/writes to `auth.uid() = user_id`. Accessed from the app via `supabase-kt` (or plain PostgREST calls through Retrofit) rather than a custom server.
- **Storage:** stores expense receipt photos and generated invoice PDFs in per-user buckets/paths, governed by matching storage RLS policies, referenced by URL from Postgres/Room.
- **Realtime / push (optional/stretch):** Supabase Realtime could notify other signed-in sessions of changes if the device is online; local `WorkManager`-scheduled notifications cover the offline case for overdue invoices.

## PDF generation

- On-device rendering using Android's `PdfDocument` API (canvas-based layout) for a lightweight dependency footprint, or **iText7-core / PdfBox-Android** if the invoice template needs richer typography/layout than canvas drawing comfortably provides.
- Template includes: freelancer's business details/logo, client details, invoice number, issue/due dates, itemized table, subtotal, exchange rate + timestamp disclosure, total in billing currency, payment instructions/notes.
- Output saved locally and optionally uploaded to Cloud Storage, then shared via Android's native share sheet (email, WhatsApp, Drive, etc.).

## Why these integrations and not others

- A dedicated **payment gateway** (Stripe, PayPal API, M-Pesa Daraja) is deliberately **not integrated** in this iteration — invoices are generated and shared, not paid, within the app (see [04-scope-and-boundaries](04-scope-and-boundaries.md)). This keeps PCI/financial-compliance surface area out of an academic build.
- No dedicated **tax-authority API** is integrated (most jurisdictions don't expose one usable by an individual freelancer app); tax figures are estimates from a user-configured profile, not a filing integration.

Next: [10 — UI/UX and Screens](10-ui-ux-and-screens.md)
