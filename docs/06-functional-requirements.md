# 06 — Functional and Non-Functional Requirements

## Functional requirements by module

### FR-1 Authentication & Account
- FR-1.1 User can register/sign in with email+password or Google sign-in.
- FR-1.2 User can set a base (home) currency and default tax profile on first launch.
- FR-1.3 User can log out; local data remains cached, cloud data persists.

### FR-2 Client Management
- FR-2.1 Create, read, update, archive a client (name, company, email, phone, notes, default currency).
- FR-2.2 List clients with search/filter.
- FR-2.3 View a client's aggregated projects, invoices, and expense totals.

### FR-3 Project Management
- FR-3.1 Create a project under a client: name, description, status, rate type (hourly/fixed), rate value, currency.
- FR-3.2 Update project status (active/on hold/completed).
- FR-3.3 List/filter projects by client or status.

### FR-4 Time Tracking
- FR-4.1 Start/stop a timer against an active project.
- FR-4.2 Manually add/edit/delete a time entry (date, duration, description).
- FR-4.3 View total logged (unbilled) time per project.

### FR-5 Invoicing
- FR-5.1 Create an invoice for a client, pulling in unbilled time entries and/or manual line items.
- FR-5.2 Auto-assign a sequential, unique invoice number.
- FR-5.3 Fetch the current exchange rate between the project currency and the client's billing currency at time of invoice creation; display both the rate and its timestamp on the invoice.
- FR-5.4 Generate a PDF containing: business/freelancer details, client details, invoice number, issue/due date, itemized lines, subtotal, exchange rate used, total in billing currency, payment terms/notes.
- FR-5.5 Share/export the PDF (share sheet, save to device).
- FR-5.6 Track invoice status: draft, sent, paid, overdue (auto-flag overdue past due date).
- FR-5.7 Duplicate an existing invoice as a starting point for a new one.

### FR-6 Expense Tracking
- FR-6.1 Log an expense: amount, currency, category, date, optional linked client/project, optional receipt photo, deductible flag.
- FR-6.2 Edit/delete an expense.
- FR-6.3 Filter/search expenses by date range, category, client/project, currency.

### FR-7 Tax Estimation
- FR-7.1 User defines a tax profile: base currency, deduction rate/rules (simple, user-editable — not jurisdiction-sourced).
- FR-7.2 App computes an estimated deductible total from flagged expenses over a selected period.
- FR-7.3 App clearly labels tax figures as estimates, not filings.

### FR-8 Reporting / Dashboard
- FR-8.1 Home dashboard: total outstanding (unpaid) invoices, total paid (period), total expenses (period), all converted to base currency.
- FR-8.2 Currency breakdown view (how much is owed/held in each currency).
- FR-8.3 Exportable summary report (PDF/CSV) for a date range.

### FR-9 Sync & Offline
- FR-9.1 All core CRUD operations (client, project, time entry, expense, invoice draft) work fully offline against the local database.
- FR-9.2 Changes queue and sync to the cloud backend when connectivity returns.
- FR-9.3 Exchange rates are cached on last successful fetch and reused when offline, with a visible "rate as of [date]" indicator.

### FR-10 Notifications
- FR-10.1 Local notification/reminder when an invoice becomes overdue.
- FR-10.2 Optional reminder to log time/expenses (configurable, off by default).

## Non-functional requirements

| Category | Requirement |
|---|---|
| **Performance** | Core screens (dashboard, client list, project list) load from local cache in under 1 second on a mid-range device. |
| **Offline-first** | No core CRUD flow requires connectivity; only exchange-rate refresh and cloud sync require it. |
| **Security** | Financial data encrypted at rest on-device; all network calls over HTTPS; Firebase security rules restrict data access to the authenticated owner. |
| **Usability** | Primary flows (add client, log expense, create invoice) achievable in ≤5 taps/screens from the dashboard. |
| **Accessibility** | Material 3 components, minimum touch target sizes, content descriptions for screen readers, supports system font scaling and dark mode. |
| **Reliability** | No data loss on interrupted sync; local DB is always the source of truth until a sync confirms. |
| **Portability** | Domain/data layers kept UI-framework-agnostic (Clean Architecture) to ease a future multi-platform port, even though only Android ships in this project. |
| **Maintainability** | Modular MVVM structure, documented data models, automated tests for core business logic (currency conversion, invoice totals, tax estimation). |

Next: [07 — System Architecture](07-system-architecture.md)
