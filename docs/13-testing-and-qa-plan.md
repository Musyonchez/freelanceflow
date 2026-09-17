# 13 — Testing and QA Plan

## Test levels

| Level | Tooling | Focus |
|---|---|---|
| Unit tests | JUnit, MockK, Turbine (for Flow) | Domain use cases: currency conversion math, invoice total/tax calculations, deduction estimation, invoice numbering logic — the parts where a silent bug directly produces a wrong invoice or wrong money figure. |
| Repository/DB tests | Room in-memory database, Robolectric/instrumented tests | CRUD correctness, foreign-key integrity (e.g., archiving a client doesn't orphan its invoices), offline queue behavior. |
| UI tests | Jetpack Compose testing APIs, Espresso where needed | Critical flows: add client → add project → log time → create invoice → generate PDF; add expense → see it reflected in dashboard totals. |
| Manual UAT | Test script executed on a physical device/emulator | End-to-end scenarios against the personas in [05-user-personas-and-stories.md](05-user-personas-and-stories.md). |

## Priority areas (highest risk of silent bugs)

1. **Currency conversion correctness** — wrong rate direction (multiplying instead of dividing) is the single most damaging possible bug in this app; covered by dedicated unit tests with known input/output pairs.
2. **Invoice numbering uniqueness** — no duplicate/skipped numbers under concurrent/offline creation.
3. **Offline → online sync** — data created offline must survive reconnection without loss or duplication.
4. **PDF field accuracy** — every figure on the generated PDF must match the underlying invoice record exactly (no rounding-display mismatch between UI and PDF).

## Sample manual test cases

| ID | Scenario | Steps | Expected result |
|---|---|---|---|
| TC-01 | Create client and project | Add client "Acme Ltd" (USD) → add project "Website Redesign" (hourly, $40/hr) | Client and project appear in respective lists, linked correctly |
| TC-02 | Log time and invoice it | Start timer on project, stop after logging time → create invoice from project | Invoice line item matches logged duration × rate |
| TC-03 | Multi-currency conversion | User base currency KES, client currency USD, create invoice for $100 | Invoice shows $100 (billing) and correct KES equivalent using the currently cached/live rate, with rate + timestamp visible |
| TC-04 | Offline invoice draft | Enable airplane mode, create a new invoice draft | Draft saves locally; on reconnect, syncs to cloud without duplication |
| TC-05 | Expense deduction estimate | Log 3 expenses, 2 marked deductible, over a date range | Reports screen shows correct deductible sum matching only the flagged expenses |
| TC-06 | Overdue invoice flag | Set an invoice due date in the past, status "sent" | Invoice auto-flags as "overdue" and triggers a local notification |
| TC-07 | Archive client with history | Archive a client that has invoices | Client hidden from active list but its invoices remain viewable/intact |

## Bug tracking

Issues logged in GitHub Issues, labeled by severity (blocker/major/minor) and module (client, project, invoice, expense, sync, ui). No feature is marked "Done" ([11-development-methodology](11-development-methodology.md)) with an open blocker-severity issue against it.

## Coverage goal

Not a specific numeric coverage percentage target (which can incentivize testing trivial code) — instead, **100% of domain-layer money/date logic** (currency conversion, totals, tax estimation, invoice numbering) must have unit tests, since that is where correctness matters most for a financial tool.

Next: [14 — Security, Privacy and Compliance](14-security-privacy-and-compliance.md)
