# 17 — Resources Required

## Software / tooling

| Resource | Purpose | Cost |
|---|---|---|
| Android Studio | IDE, emulator, build tooling | Free |
| Git + GitHub | Source control, issue tracking, GitHub Actions CI | Free (public/student repo) |
| Figma | Wireframes/UI design ([10-ui-ux-and-screens](10-ui-ux-and-screens.md)) | Free tier sufficient |
| Supabase (free plan) | Auth, Postgres database, Storage | Free tier sufficient at student-project scale |
| Exchange-rate API (Frankfurter.app or ExchangeRate-API free tier) | Live currency conversion | Free |
| PDF library (if native API insufficient) | iText7-core or PDFBox-Android | Free/open-source (check license terms for iText's AGPL vs. commercial use — acceptable for an academic, non-commercial project) |

## Hardware

- A development machine capable of running Android Studio + emulator (the primary constraint is RAM; 8GB minimum, 16GB comfortable).
- At least one physical Android device (mid-range, Android 10+) for realistic manual testing, particularly of camera (receipt photos), offline mode, and PDF share flows.

## Skills / knowledge required

- Kotlin and Jetpack Compose (or willingness to learn during Sprint 0–1, per [12-timeline-and-milestones](12-timeline-and-milestones.md)).
- Basic relational data modeling (for Room schema, [08-database-design](08-database-design.md)).
- REST API consumption (Retrofit) for the exchange-rate integration.
- Supabase fundamentals (Auth, Postgres, Row Level Security).
- Testing fundamentals (JUnit, Compose testing).

## Support

- Academic supervisor/lecturer feedback at milestone checkpoints ([12-timeline-and-milestones](12-timeline-and-milestones.md)).
- Public documentation: Android Developers site, Supabase documentation, chosen exchange-rate API's docs (see [18-references](18-references.md)).
- No paid resources are required to complete the project at student scale — every tool above has a free tier sufficient for development, testing, and a course demo.

Next: [18 — References](18-references.md)
