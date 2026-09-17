# 10 — UI/UX and Screens

## Design principles

- **Material 3** components and theming, with light/dark mode support.
- **Mobile-first speed:** the most common actions (log an expense, start a timer, create an invoice) are reachable in ≤2 taps from the dashboard via a floating action button with quick-action shortcuts.
- **Currency clarity:** anywhere an amount appears, its currency code is shown next to it — never an unlabeled number.
- **Offline transparency:** a persistent (dismissible) banner indicates when the app is offline and data shown may be based on cached exchange rates.
- **Accessibility:** minimum 48dp touch targets, content descriptions on icons, supports system text scaling, sufficient color contrast (validated against WCAG AA).

## Screen map

```
Onboarding / Auth
 └─ Sign in / Sign up → Set base currency & tax profile (first run)

Dashboard (home)
 ├─ Outstanding vs paid summary (base currency)
 ├─ Currency breakdown
 ├─ Quick actions: + Client, + Project, + Expense, + Invoice
 └─ Overdue invoice alerts

Clients
 ├─ Client list (search/filter, active/archived)
 ├─ Client detail → Projects | Invoices | Expenses (tabs)
 └─ Add/Edit client form

Projects
 ├─ Project list (filter by client/status)
 ├─ Project detail → Time entries | Unbilled items | Related invoices
 └─ Add/Edit project form

Time Tracking
 ├─ Active timer (start/stop, tied to a project)
 └─ Manual time entry form

Invoices
 ├─ Invoice list (filter by status/client)
 ├─ Invoice builder (pull unbilled time/items, add manual line, choose client currency)
 ├─ Invoice preview (PDF render) → Share / Mark as sent
 └─ Invoice detail → mark paid, record payment, duplicate

Expenses
 ├─ Expense list (filter by date/category/client/project/currency)
 ├─ Add/Edit expense form (amount, currency, category, receipt photo, deductible toggle)
 └─ Expense detail

Reports
 ├─ Income vs expense summary (date range)
 ├─ Deduction estimate (from Tax Profile)
 └─ Export (PDF/CSV)

Settings
 ├─ Profile & base currency
 ├─ Tax profile (deduction rate, notes/disclaimer)
 ├─ Notification preferences
 └─ Sync status / manual sync trigger
```

## Navigation

Bottom navigation bar with five top-level destinations: **Dashboard, Clients, Projects, Invoices, Expenses**; Reports and Settings reachable from the dashboard's overflow menu and a profile icon respectively. Client and Project detail screens use tabs to avoid deep navigation stacks.

## Key interaction notes

- Creating an invoice from a project's detail screen pre-fills client, currency, and unbilled time entries — the builder screen is reachable, but never *required* to start empty-handed.
- The invoice preview screen shows the PDF exactly as it will be shared, including the exchange-rate disclosure line, before the user commits to "Send."
- Expense entry supports capturing a receipt photo directly from the camera or picking from gallery, attached inline.

Next: [11 — Development Methodology](11-development-methodology.md)
