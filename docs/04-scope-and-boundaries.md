# 04 — Project Scope

**Area:** Freelance / gig economy business management (cross-cutting: finance + productivity), delivered as a mobile application.

## In scope

- **Client management:** create/edit/archive clients, contact details, notes, default currency per client.
- **Project management:** projects linked to a client, status (active/completed/on hold), basic milestones, rate (hourly or fixed).
- **Time/work logging:** simple timer or manual entry against a project, feeding invoice line items.
- **Multi-currency invoicing:** invoice builder, itemization, automatic currency conversion via a live exchange-rate API (with caching), PDF export/share, sequential invoice numbering, payment status (draft/sent/paid/overdue).
- **Expense tracking:** log expenses with currency, category, receipt photo attachment, link to client/project, deductible flag.
- **Tax-deduction estimation:** a configurable tax profile (user sets their own rate/categories) that estimates deductible totals — **an estimation tool, not a certified tax-filing system.**
- **Reporting dashboard:** income vs expenses, outstanding balances, currency breakdown, exportable summary.
- **Authentication & data sync:** single-user accounts (email/password or Google sign-in), cloud backup/sync so data survives a device change.
- **Offline-first operation:** local database as source of truth, background sync when online.

## Out of scope (explicitly, for this iteration)

- **Full double-entry accounting / general ledger** (this is a freelancer tool, not an accounting suite).
- **Actual payment processing** (no in-app card/bank payment collection; invoices are generated and shared, payment happens outside the app and is marked manually — a stretch goal, not a commitment, is noted in [16-risks-and-mitigation](16-risks-and-mitigation.md)).
- **Legally certified tax filing or submission to tax authorities** — the app estimates and organizes, it does not file.
- **Multi-user/team collaboration** (shared workspaces, roles, permissions) — this is a single-freelancer tool.
- **iOS or web client** — Android only for this project; architecture is kept layered enough that a future web/iOS client is *feasible*, but building one is not part of this deliverable.
- **Payroll, contracts/e-signatures, proposal generation** — adjacent freelancer-tooling features that are deliberately left out to keep the build achievable in a semester.

## Boundaries and limitations

- Exchange rates come from a third-party API; accuracy and availability depend on that provider's terms (free-tier rate limits apply — see [09-api-and-integrations](09-api-and-integrations.md)).
- Tax rules are **user-configured**, not sourced from an authoritative, jurisdiction-specific tax database — the app does not claim legal tax compliance for any specific country.
- "Compliant" invoices means the PDF contains the fields generally expected on a professional invoice (unique sequential number, issue/due date, seller and client details, itemization, currency and rate disclosed, total) — not certification against a specific country's fiscal invoicing law.
- Single-device-per-account assumption for conflict resolution in sync (last-write-wins), documented as a known limitation rather than solved with full multi-device conflict resolution.

Next: [05 — User Personas and Stories](05-user-personas-and-stories.md)
