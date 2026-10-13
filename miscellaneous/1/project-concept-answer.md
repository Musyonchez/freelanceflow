# SWE ______ Final Class Project Concept

**FS 2026**

This document answers the concept format in [`Project Concept.pdf`](Project%20Concept.pdf) (also provided as [`Project Concept.docx`](Project%20Concept.docx)) for the expanded project. Full supporting detail for every section below lives in the [`../docs/`](../docs/) folder, linked inline.

---

## Proposed Project Title/Area

**FreelanceFlow: An Integrated Client, Project, Invoice & Expense Management Android Application for Remote Freelancers**

Area: Freelance / gig-economy business management (financial technology + productivity tooling).

*(Expanded from the original brief, "Multi-Currency Invoice & Expense Generator," into a full client/project/invoicing/expense system — see [docs/01-overview-and-vision.md](../docs/01-overview-and-vision.md) for the reasoning behind the expansion.)*

## Student Name(s)

- **672511 / Daleen Hunnipher Otieno**

## Previous Projects Done/Languages/Frameworks Used

*[To be completed by the student — list prior coursework/personal projects and the languages/frameworks used in each, e.g. previous SWE module projects, personal apps, languages such as Java, Python, Kotlin, or frameworks such as Django, React, etc. This section is personal academic history and cannot be filled in on the student's behalf.]*

## Project Background and Justification

Remote freelancers routinely work across currencies: a freelancer based in one country bills clients in another, and is paid in a currency different from the one they spend and pay tax in. This creates three recurring problems: fragmented tooling (client details, project notes, invoices, and expenses are tracked in separate, disconnected apps or spreadsheets), manual and error-prone currency conversion when pricing and issuing invoices, and a lack of connection between logged expenses and tax-deductible record-keeping.

Established tools such as Wave, FreshBooks, QuickBooks Self-Employed, Invoice Ninja, and Bonsai address parts of this problem, but are largely web-first with mobile as an afterthought, are often full accounting suites that are heavier than a solo freelancer needs, frequently gate multi-currency support behind paid tiers, and do not tightly integrate client and project management with invoicing and expense/tax tracking in one lightweight, mobile-first, offline-capable application.

This matters because freelance and remote work has grown substantially in recent years, and freelancers carry the full administrative burden of billing, currency conversion, and record-keeping themselves, with no employer finance department behind them. Poor invoicing practice contributes to payment delays, and poor expense record-keeping leads to freelancers under-claiming legitimate deductions or facing avoidable tax-compliance risk.

Full background, existing-system comparison, and sourcing is set out in [docs/02-problem-background-and-justification.md](../docs/02-problem-background-and-justification.md).

## Project Objectives

1. Unify client and project management in one system, so every invoice, expense, and time entry is linked to the right client and project.
2. Generate compliant, multi-currency PDF invoices automatically, using a live (or last-cached) exchange rate at the time of issue.
3. Track expenses with multi-currency support and tax-deduction tagging, linked to the client/project that caused them.
4. Provide a real-time financial overview (outstanding invoices, paid totals, expense totals) normalized into the user's base currency.
5. Deliver a secure, offline-capable mobile experience, so core functions work without an internet connection and sync once reconnected.

Each objective has a measurable success criterion — see [docs/03-objectives-and-success-metrics.md](../docs/03-objectives-and-success-metrics.md).

## Project Scope

**In scope:** client management; project management; basic time/work logging; multi-currency invoice generation with PDF export and live exchange-rate conversion; expense tracking with receipt photos and deductible tagging; a configurable, user-defined tax-deduction estimator; a reporting dashboard; authentication with cloud backup/sync; offline-first operation.

**Out of scope:** full double-entry accounting; in-app payment processing/collection; legally certified tax filing; multi-user/team collaboration; iOS or web clients (Android only for this project); payroll, contracts/e-signatures, and proposal generation.

**Area:** Financial technology / productivity tooling for the freelance and gig economy sector.

Full scope boundaries and stated limitations (e.g., exchange rates depend on a third-party API; tax rules are user-configured, not jurisdiction-certified) are in [docs/04-scope-and-boundaries.md](../docs/04-scope-and-boundaries.md).

## Proposed Methodology

An **Agile, Scrum-inspired methodology** adapted for a solo/small student team: 2-week sprints across the semester, a prioritized backlog tracked on a GitHub Projects board, a working build demoed at the end of each sprint, and a short retrospective per sprint. Client and project management are built first as the foundation, since invoicing and reporting depend on that data existing — de-risking the most uncertain parts of the system (exchange-rate integration, PDF generation, offline sync) early through incremental, testable slices rather than specifying everything up front.

**Architecture:** native Android app (Kotlin, Jetpack Compose, Material 3) using an offline-first, layered MVVM/Clean Architecture: a local Room database as the source of truth, Firebase (Authentication, Cloud Firestore, Cloud Storage) for cloud sync and backup, Retrofit for exchange-rate API calls, WorkManager for background sync, and on-device PDF generation for invoices.

**Tools/technologies:** Kotlin, Jetpack Compose, Room, Hilt, Retrofit, Firebase (free tier), a free exchange-rate API (e.g., Frankfurter.app), Figma for design, GitHub + GitHub Actions for version control and CI.

Full architecture, database design, and methodology detail: [docs/07-system-architecture.md](../docs/07-system-architecture.md), [docs/08-database-design.md](../docs/08-database-design.md), [docs/11-development-methodology.md](../docs/11-development-methodology.md).

## Expected Outcomes/Deliverables

- A functioning native Android application implementing client management, project management, time logging, multi-currency invoicing (PDF generation and share), expense tracking, tax-deduction estimation, and a reporting dashboard.
- A signed release build (APK) for demonstration/grading.
- A full system-documentation set (this `docs/` folder): requirements, architecture, database design, UI/UX plan, testing plan, and risk analysis.
- A tested, version-controlled GitHub repository with commit history reflecting the sprint plan.

**Benefit to users:** freelancers gain a single mobile tool that replaces a spreadsheet-plus-notes-app-plus-generic-invoice-template workflow, removes manual currency-conversion errors from their invoices, and gives them an organized, tax-ready record of deductible expenses tied to the client/project that generated them — see [docs/01-overview-and-vision.md](../docs/01-overview-and-vision.md) for the full value proposition.

## Timeline

A 14-week, 7-sprint semester plan: Sprint 0 (requirements/design/scaffolding) → Sprint 1 (auth + client management) → Sprint 2 (project management + time tracking) → Sprint 3 (exchange-rate integration + invoice builder) → Sprint 4 (PDF generation + invoice lifecycle) → Sprint 5 (expenses + tax estimation + dashboard) → Sprint 6 (testing, polish, final delivery).

Full week-by-week breakdown and a Gantt-style summary: [docs/12-timeline-and-milestones.md](../docs/12-timeline-and-milestones.md).

## Resources Required

Android Studio, Git/GitHub (with GitHub Actions), Figma, Firebase (free Spark tier), a free-tier exchange-rate API, a development machine capable of running the Android emulator, and at least one physical Android device for realistic testing. All tooling required is available at no cost at student-project scale.

Full list: [docs/17-resources-required.md](../docs/17-resources-required.md).

## Potential Challenges and Mitigation

Key risks include third-party exchange-rate API limits or downtime (mitigated by caching the last-known rate and labeling its age), scope creep beyond what a semester allows (mitigated by strict adherence to the documented scope and an explicit Phase 2 backlog), offline-sync conflicts (mitigated by a simple, tested last-write-wins policy), and the risk of incorrect currency-conversion math shipping in an invoice (mitigated by mandatory unit-test coverage on all money-calculation logic, plus always disclosing the rate and its timestamp on the invoice itself).

Full risk register: [docs/16-risks-and-mitigation.md](../docs/16-risks-and-mitigation.md).

## References

See [docs/18-references.md](../docs/18-references.md) for the full APA 7 reference list (Android/Firebase documentation, Material Design guidelines, freelance-economy industry reports, and applicable data-protection legislation). Statistics cited in the final written report should be verified against the latest edition of each source before submission.

## GitHub/Git

*[To be completed by the student — add the repository URL once created, e.g. `https://github.com/<username>/freelanceflow`.]*

## List of Previous Class Projects

*[To be completed by the student — one paragraph per previous class project, per the template's requirement. This is personal academic history and cannot be filled in on the student's behalf.]*
