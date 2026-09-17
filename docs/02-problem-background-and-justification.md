# 02 — Problem, Background and Justification

## The problem

Remote freelancing crosses borders by default: a designer in Nairobi bills a client in London, gets paid in GBP or USD, but lives and pays tax in KES. This creates three compounding problems:

1. **Fragmented tooling.** Client info lives in a phone's contacts or a notes app; project details live in email threads or a spreadsheet; invoices are typed from scratch each time; expenses are tracked (if at all) in a separate spreadsheet or not at all.
2. **Manual, error-prone currency handling.** Converting a rate agreed in one currency into an invoice total in another, using a rate that's actually current, is manual work most freelancers do by searching "USD to KES" in a browser and doing the arithmetic themselves — which is slow and easy to get wrong.
3. **No link between money in and money out for tax purposes.** Expenses that should offset taxable income are rarely tracked against the project/client/currency that generated them, so at tax time freelancers either overpay or scramble to reconstruct records.

## Do such systems exist?

Yes — mature products exist in this space, and it's worth being honest about them:

- **Wave, FreshBooks, QuickBooks Self-Employed, Invoice Ninja, Bonsai** — invoicing/accounting tools aimed at freelancers and small businesses.
- **Zoho Invoice, PayPal Invoicing** — lighter-weight invoice generators.

Their common limitations, from the point of view of a remote, multi-currency freelancer on Android:

- Most are **web-first**, with the mobile app as a secondary, cut-down client rather than the primary experience.
- Full accounting suites (QuickBooks, Wave) are **feature-heavy for a solo freelancer** — built around general-ledger bookkeeping rather than "client → project → invoice → expense" simplicity.
- Multi-currency support is often **locked behind paid tiers**, or the exchange rate used isn't transparent/live at the point of invoice creation.
- **Tax handling is jurisdiction-specific** (usually US/UK/EU-centric) and doesn't generalize to a freelancer estimating deductible expenses under a different tax regime.
- None tightly couple **project/time tracking** with **invoicing** with **expense tracking** in a single lightweight mobile-first flow — a freelancer typically still moves data between two or three tools.

FreelanceFlow's justification isn't "no such tool exists" — it's that no existing tool combines *client + project + multi-currency invoicing + expense/tax tracking* in one lightweight, mobile-first, offline-capable Android app aimed at the solo remote freelancer rather than an SMB accounts department.

## Why this problem matters

- Remote and freelance work has grown substantially over the past decade, accelerated by widespread remote work adoption; cross-border freelance platforms (Upwork, Fiverr, Payoneer-connected freelancers) report large and growing shares of freelancers being paid across currencies. (See [18-references](18-references.md) for indicative industry reports — figures should be verified/updated with the latest published reports before being cited as hard statistics in the final submission.)
- Freelancers, unlike salaried employees, carry the **full administrative burden** of billing, currency conversion, and tax record-keeping themselves, with no employer/finance department behind them.
- Poor invoicing (unclear currency, inconsistent numbering, missing itemization) is a common cause of **payment delays and disputes** for independent workers.
- Poor expense record-keeping at tax time results in **freelancers either under-claiming legitimate deductions or facing tax-compliance risk**.

## Justification for scope choice

The project therefore addresses a genuine, personally-relatable gap (a solo freelancer's admin workflow), is scoped to something buildable in a semester (native Android, local-first data, a small number of well-chosen third-party integrations), and produces a system with enough moving parts — data modeling, offline sync, external APIs, PDF generation, financial calculations — to be a substantial software engineering exercise, not just a UI wrapper around a template.

Next: [03 — Objectives and Success Metrics](03-objectives-and-success-metrics.md)
