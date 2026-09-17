# 11 — Proposed Methodology

## Approach: Agile (Scrum-inspired, solo/small-team adapted)

A full Scrum ceremony set (daily standups, formal sprint reviews with stakeholders) doesn't map cleanly onto a solo/small student team, so this project runs a **lightweight adapted Scrum**:

- **Sprint length:** 2 weeks, aligned to the semester timeline ([12-timeline-and-milestones](12-timeline-and-milestones.md)).
- **Backlog:** the functional requirements in [06-functional-requirements.md](06-functional-requirements.md), broken into user-story-sized tickets in a GitHub Projects (or Trello) board with columns To Do / In Progress / In Review / Done.
- **Sprint planning:** at the start of each sprint, pull a small, achievable set of stories, prioritizing whatever the current milestone needs (e.g., "client + project CRUD" before "invoicing," since invoicing depends on it).
- **Sprint review:** a working build demoed at the end of each sprint (even if only to self/supervisor), against the sprint's goal.
- **Retrospective:** short written note per sprint — what slowed the sprint down, what to change next sprint (kept in the repo, e.g. `docs/retros/` if adopted during build).

## Why Agile over Waterfall here

The riskiest, least-certain parts of the system (exchange-rate API behavior/limits, PDF layout complexity, offline-sync edge cases) are exactly the parts best de-risked by building a thin vertical slice early and iterating, rather than fully specifying every screen before writing code. Client/project management is built first as the foundation everything else (invoices, expenses, reports) depends on — an incremental, testable core rather than a big-bang integration at the end.

## Tools

| Purpose | Tool |
|---|---|
| Source control | Git + GitHub (see [12-timeline-and-milestones](12-timeline-and-milestones.md) for repo cadence) |
| Task/backlog tracking | GitHub Projects (Kanban board) |
| IDE | Android Studio |
| Design/wireframes | Figma (screens from [10-ui-ux-and-screens](10-ui-ux-and-screens.md)) |
| CI (lightweight) | GitHub Actions — run unit tests + lint on push |
| Communication (if team-based) | Weekly check-in, shared doc of decisions |

## Branching strategy

- `main` — always buildable, represents the latest stable milestone.
- `dev` — integration branch for the current sprint.
- `feature/<short-name>` — one branch per user story/ticket, merged into `dev` via pull request (even solo, PRs create a reviewable diff trail and a natural changelog).
- Tag a release/build at the end of each milestone (see timeline) for traceability against the concept document's expected deliverables.

## Definition of Done (per feature)

A story is "Done" only when: it compiles and passes lint, has at least a minimal unit test for any business logic it introduces, works correctly offline where applicable, and has been manually exercised on an emulator/device — not just "the code was written."

Next: [12 — Timeline and Milestones](12-timeline-and-milestones.md)
