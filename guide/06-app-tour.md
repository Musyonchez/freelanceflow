# 6 — App Tour

Once signed in, the app has four main areas reachable from the bottom navigation bar: **Home**, **Work**, **Money**, and **Clients**. A top bar is present on every screen, showing the current screen's name and a sign-out icon on the right.

## Bottom navigation

```
┌─────────────────────────────────────┐
│          (screen content)            │
│                                       │
├───────┬───────┬────────┬────────────┤
│ Home  │ Work  │ Money  │  Clients   │
└───────┴───────┴────────┴────────────┘
```

Tapping a tab switches the whole screen; your place in each tab is remembered as you switch back and forth within the same app session.

## Home

The dashboard. Three cards summarize your business at a glance, all converted into a single "base currency" you choose (tap the pencil icon next to "Reporting in ___" to change it):

- **Outstanding** — total of all unpaid invoices (draft, sent, or overdue), converted to your base currency
- **Paid** — total of everything marked paid
- **Expenses** — total of everything logged as an expense

If you have invoices in more than one currency, a breakdown list shows each currency's outstanding total unconverted too, alongside the converted summary.

Below that, **Export report** lets you pick a period (this month / this year / all time) and tap **Export CSV** to generate a single CSV covering invoices and expenses for that period — including totals converted to your base currency and the estimated tax deduction — then share it anywhere via the system share sheet.

## Work

Where projects and time tracking live.

- The project list shows every project, its client, hourly/fixed rate, and status.
- Tap the **+** button (bottom-right) to add a new project — you'll need at least one client to exist first (add one from the Clients tab).
- Tapping a project's **play** button starts a timer against it; tapping it again (now showing **stop**) ends the timer and records the elapsed time as a time entry. Only one timer can run at a time — starting a new one automatically stops whichever was already running.
- A running timer also shows as a card at the top of the screen so it's visible without scrolling to find the project.

## Money

Has three sub-tabs of its own, switchable via a row of tabs just under the "Invoices & expenses" header: **Invoices**, **Expenses**, and **Tax**.

### Invoices sub-tab

- Lists every invoice: number, client, due date, total, and status (draft/sent/paid/overdue).
- Tap **+** to open the invoice builder: pick a project, pull in any unbilled logged time automatically, or add manual line items (description, quantity, unit price). The builder shows a running subtotal and lets you set a due date and notes before creating the invoice.
- Once created, an invoice starts as **Draft**. Tap **Mark sent** to move it to **Sent**; from there tap **Mark paid** once the client pays, or it'll auto-flip to **Overdue** (with a notification) if the due date passes while still unpaid.
- The share icon generates a PDF of the invoice and opens your device's share sheet so you can send it anywhere (email, messaging apps, save to Drive, etc.).

### Expenses sub-tab

- Lists logged expenses with date and amount. A thumbnail appears on the left for any expense with a receipt photo attached — tap it to view full-size.
- Tap **+** to log a new one: amount, currency, category, optionally linked to a client/project, a toggle for whether it's tax-deductible, and an optional receipt photo via **Camera** or **Gallery**.

### Tax sub-tab

- Shows your tax deduction profile (a label and a deduction rate percentage you set yourself — this is explicitly an estimate, never a filing or certified figure) and lets you edit it via the pencil icon.
- Three period filters (**This month**, **This year**, **All time**) control which deductible expenses get totaled.
- Two figures are shown: the raw total of deductible expenses in that period, and the estimated deduction at your profile's rate.

## Clients

- Lists every client, their billing currency, and a status badge.
- Tap **+** to add a new client: name (required), company, email, phone, billing currency, and notes.
- Clients can be archived rather than deleted, which hides them from active lists without losing their invoice/project history.

## Signing out

Tap the icon in the top-right corner of any screen (an arrow pointing out of a box) to sign out and return to the sign-in screen. Your data isn't lost — it's synced to your Supabase project and will reappear the next time you sign back in with the same account, even on a different device.

Next: [7 — Everyday usage](07-everyday-usage.md) for a full worked example tying all of this together.
