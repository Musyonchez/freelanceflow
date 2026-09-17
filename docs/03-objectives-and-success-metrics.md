# 03 — Project Objectives and Success Metrics

Five objectives, each with a way to tell it's actually been met — not just "built" but "works."

## 1. Unify client and project management in one system
Freelancers can create, view, edit, and archive clients and the projects tied to them, with all invoices/expenses/time entries linked back to the right client and project.
- **Success metric:** A user can create a client, attach 2+ projects to it, and see all related invoices/expenses filtered by that client or project, with zero orphaned records.

## 2. Generate compliant, multi-currency PDF invoices automatically
Invoices are generated as polished PDFs, itemized, sequentially numbered, and priced in the client's currency using a live (or last-cached) exchange rate at time of issue.
- **Success metric:** From a project with logged time/line items, a user produces a shareable PDF invoice in under 60 seconds, with the correct converted total and rate disclosed on the document.

## 3. Track expenses with multi-currency support and tax-deduction tagging
Expenses can be logged with currency, category, and an optional deductible flag/percentage, linked to the client/project that caused them.
- **Success metric:** The app can produce a filtered expense report (by date range, currency, category) with a total converted into the user's base currency, and an estimated deductible amount.

## 4. Provide a real-time financial overview
A dashboard shows outstanding invoices, paid vs unpaid totals, and expense totals — normalized into the user's base currency — across all clients and currencies.
- **Success metric:** The dashboard reflects a new invoice or expense within the same session without manual refresh/recalculation steps by the user.

## 5. Deliver a secure, offline-capable mobile experience
The app remains usable (viewing clients/projects/invoices/expenses, drafting new invoices) without an active internet connection, syncing once connectivity returns, and protects the user's financial data.
- **Success metric:** Core CRUD flows (add client, add expense, draft invoice) complete successfully in airplane mode and sync correctly once the device reconnects, with no data loss in a manual test pass.

Next: [04 — Scope and Boundaries](04-scope-and-boundaries.md)
