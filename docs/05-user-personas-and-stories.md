# 05 — User Personas and User Stories

## Personas

### 1. Amina — Freelance Graphic Designer (primary persona)
- Based in Nairobi, bills clients in the US and UK, paid in USD/GBP, spends in KES.
- Works with 4–6 active clients at a time, each with 1–3 projects.
- Currently uses WhatsApp for client comms, a notes app for project tracking, and Canva-made invoice templates.
- Pain point: manually converting USD quotes to KES to know what she's really earning, and losing track of which expenses (software subscriptions, a new tablet stylus) are deductible.

### 2. Brian — Freelance Web Developer (secondary persona)
- Works fixed-price and hourly contracts simultaneously.
- Needs to time-track hourly work accurately and turn logged hours directly into an invoice.
- Pain point: forgets to invoice promptly, loses track of which invoices are still unpaid.

### 3. Cynthia — Freelance Virtual Assistant / Consultant (secondary persona)
- Multiple small retainer clients, recurring monthly invoices.
- Pain point: recreates the same invoice structure every month by hand; wants a fast repeat/duplicate flow.

## User stories

Grouped by module; format: *As a [persona], I want [capability], so that [benefit].*

### Client management
- As Amina, I want to add a new client with their name, contact info, and billing currency, so that I don't have to re-enter it every time I invoice them.
- As Amina, I want to see all projects and invoices tied to one client on a single screen, so that I have full context before a client call.

### Project management
- As Brian, I want to create a project under a client with a rate type (hourly/fixed) and status, so that I can track its progress separately from other work.
- As Brian, I want to mark a project complete, so that it stops showing up in my active work list.

### Time tracking
- As Brian, I want to start/stop a timer against a project, so that my logged hours are accurate without manual guessing.
- As Brian, I want to manually add a time entry for work I forgot to time, so that I don't lose billable hours.

### Invoicing
- As Amina, I want to generate an invoice from a project's logged time/items, so that I don't have to retype line items.
- As Amina, I want the invoice total shown in my client's currency and in my own, using a current exchange rate, so that both sides understand the amount.
- As Cynthia, I want to duplicate a previous invoice for a recurring retainer client, so that I don't rebuild it monthly.
- As Amina, I want to export/share the invoice as a PDF, so that I can send it by email or WhatsApp.
- As Brian, I want to mark an invoice as paid/unpaid/overdue, so that I know who still owes me money.

### Expenses
- As Amina, I want to log an expense with an amount, currency, category, and optional receipt photo, so that I have a record for tax time.
- As Amina, I want to flag an expense as tax-deductible, so that my deduction total is ready when I need it.
- As Amina, I want to link an expense to the project that caused it, so that I understand true project profitability.

### Reporting / dashboard
- As Brian, I want a dashboard showing total outstanding invoices and total paid this month, converted to my base currency, so that I know my cash position at a glance.
- As Cynthia, I want an expense summary for a date range, so that I can hand it to my accountant at tax time.

### Account & sync
- As Amina, I want my data backed up to the cloud, so that switching phones doesn't mean losing my client history.
- As Brian, I want to keep working (view clients, draft invoices) when I have no signal, so that a bad connection doesn't block my work.

Next: [06 — Functional & Non-Functional Requirements](06-functional-requirements.md)
