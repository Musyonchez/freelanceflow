# 01 — Overview and Vision

## Starting point

The original brief (ChatGPT-generated) proposed a **Multi-Currency Invoice & Expense Generator**: a platform for remote freelancers that produces compliant, multi-currency PDF invoices, auto-calculating real-time exchange rates and tax deductions.

That idea is a solid *feature*, but on its own it solves only one moment in a freelancer's workflow — the point of billing. It says nothing about where the invoice's line items come from (which client, which project, how much time or work was delivered), or what happens to the money and expenses around it.

## Expanded concept

**FreelanceFlow** widens the scope from "generate an invoice" to "run the business side of freelancing." It keeps multi-currency invoicing as the centerpiece, but wraps it in the workflow that actually produces an invoice's content and consumes its output:

```
Client  →  Project  →  Time / Work logged  →  Invoice (multi-currency, PDF)  →  Payment tracked
                                                     ↑
                                              Expenses logged (multi-currency)  →  Tax deduction estimate
                                                     ↓
                                              Reporting dashboard (income, expenses, outstanding balances)
```

## One-line pitch

> An Android app that lets a remote freelancer manage every client and project they work on, track time and expenses against them, and generate compliant, multi-currency, tax-aware invoices — all from their phone, and all in one connected system instead of four disconnected tools.

## Target platform

Native **Android application** (this document set treats Android as the sole delivery platform for the academic project; see [04-scope-and-boundaries](04-scope-and-boundaries.md) for why iOS/web are explicitly excluded from this iteration).

## Target users

Independent, remote-working freelancers and micro-consultants (developers, designers, writers, virtual assistants, consultants) who:
- Bill clients in a currency different from the one they spend in.
- Juggle more than one client/project at a time.
- Currently stitch together a notes app, a spreadsheet, and a generic invoice template to run their business.

## Why this is a better fit for the module

The university format asks for background/justification, objectives, scope, methodology, architecture, timeline, risks, and expected deliverables (see the [concept template](../miscellaneous/) in `miscellaneous/`). A single "generate a PDF" utility is too thin to justify a full system-design document set. Widening it into a client + project + finance manager gives a real domain model, a real database, real architecture decisions (offline-first, sync, third-party APIs), and a real multi-module Android app worth building and defending — while still keeping the original multi-currency invoicing idea as the flagship feature.

## Value proposition

| For the freelancer | Instead of |
|---|---|
| One app for clients, projects, invoices, expenses | A CRM note, a spreadsheet, an invoice template, a separate expenses list |
| Invoices auto-priced in the client's currency using live rates | Manually looking up exchange rates and doing the math |
| Expense records tagged to the project/client that caused them | Guessing which receipts belong to which write-off at tax time |
| A dashboard of what's owed, in what currency, by whom | Chasing payment status across email threads |

Next: [02 — Problem, Background and Justification](02-problem-background-and-justification.md)
