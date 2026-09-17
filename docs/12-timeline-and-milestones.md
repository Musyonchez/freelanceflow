# 12 — Timeline and Milestones

A 14-week semester plan (FS 2026), organized as seven two-week sprints. Adjust start date to the actual semester calendar; weeks are relative (W1–W14).

| Weeks | Sprint | Focus | Deliverable / Milestone |
|---|---|---|---|
| W1–W2 | Sprint 0 | Requirements finalization, this document set, wireframes in Figma, project scaffolding (Android Studio project, Hilt/Room/Compose set up), GitHub repo initialized | Approved concept document; buildable empty-shell app in repo |
| W3–W4 | Sprint 1 | Auth (Firebase), onboarding (base currency/tax profile setup), Client management (CRUD + list/detail) | Working sign-in; add/view/edit/archive a client end-to-end |
| W5–W6 | Sprint 2 | Project management (CRUD, link to client), Time tracking (timer + manual entry) | Add a project to a client; log time against it |
| W7–W8 | Sprint 3 | Exchange-rate API integration + caching, Invoice builder (line items from time entries + manual), invoice numbering | Draft an invoice pulling in logged time, correct currency conversion shown |
| W9–W10 | Sprint 4 | PDF generation + share flow, invoice status tracking (sent/paid/overdue), payment recording | Generate and share a real PDF invoice; mark it paid |
| W11 | Sprint 5 | Expense tracking (CRUD, receipt photos, deductible flag), Tax profile + deduction estimate | Log expenses; view an estimated deduction total |
| W12 | Sprint 5 (cont.) | Dashboard/reports (income vs expense, currency breakdown, export), offline-sync hardening | Dashboard reflects live data; app fully usable offline with correct sync on reconnect |
| W13 | Sprint 6 | Testing pass (unit + UI + manual UAT), bug fixing, accessibility/polish pass | Test report; known-issues list closed or triaged |
| W14 | Sprint 6 (cont.) | Final packaging, documentation, demo/defense prep | Final APK/build, presentation, submission of docs + repo |

## Major milestones (Gantt-style summary)

```
W1  ████ Requirements & Design
W2  ████
W3  ░░░░ Auth + Client Mgmt
W4  ░░░░
W5  ▓▓▓▓ Project Mgmt + Time Tracking
W6  ▓▓▓▓
W7  ██░░ Currency API + Invoice Builder
W8  ██░░
W9  ▓░▓░ PDF Generation + Invoice Lifecycle
W10 ▓░▓░
W11 ░▓░▓ Expenses + Tax Estimation
W12 ░▓░▓ Dashboard + Offline Sync
W13 ████ Testing & Polish
W14 ████ Final Delivery & Demo
```

## Dependencies to respect

- Invoicing (Sprint 3) cannot start meaningfully before Client + Project management (Sprints 1–2) exist, since invoices are built from that data — this ordering is deliberate, not incidental.
- PDF generation (Sprint 4) depends on the invoice data model being stable from Sprint 3.
- Dashboard/reports (Sprint 5–6 boundary) depend on invoices and expenses both existing, so it is scheduled last among features, before the dedicated testing sprint.

Next: [13 — Testing and QA Plan](13-testing-and-qa-plan.md)
