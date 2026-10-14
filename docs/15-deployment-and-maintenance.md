# 15 — Deployment and Maintenance

## Build & release process

- **Debug builds** throughout development, installed directly to a test device/emulator via Android Studio.
- **Signed release build (APK/AAB)** produced for the final submission/demo, using a self-managed keystore (documented, not committed to the repo).
- Versioning follows semantic-ish `MAJOR.MINOR.BUILD` (e.g., `1.0.0`) tied to milestone tags in Git.

## Distribution (for this academic project)

- Primary distribution is a **signed APK shared directly** for grading/demo purposes (sideloaded onto the examiner's/lecturer's device or run on an emulator) — a full Google Play Store listing is **not required** for the module and is treated as a stretch/future step, not a deliverable.
- If pursued beyond the course, a Play Store release would require: a Play Console developer account, store listing assets, a privacy policy page (informed by [14-security-privacy-and-compliance](14-security-privacy-and-compliance.md)), and passing Play's data-safety review given the app handles financial/PII data.

## Environments

| Environment | Purpose |
|---|---|
| Local (debug) | Day-to-day development, connected to a Supabase **dev** project |
| Supabase dev project | Isolated Auth/Postgres/Storage instance for testing, safe to reset |
| Supabase prod-equivalent project | Used for the final demo build, so demo data doesn't mix with dev/test junk data |

## Post-launch / post-submission maintenance plan

Even as an academic deliverable, the system is designed so it *could* be maintained:
- **Backlog carried forward:** stretch items explicitly deferred in [16-risks-and-mitigation](16-risks-and-mitigation.md) (e.g., payment gateway integration, multi-device conflict resolution, iOS/web clients) are documented as a "Phase 2" backlog rather than lost decisions.
- **Crash/error visibility:** a lightweight crash reporter (e.g., Sentry's free tier) recommended as a low-effort addition if time allows, to catch issues that manual testing misses.
- **Data migration discipline:** Room schema changes go through Room's migration system rather than destructive rebuilds, so a future update wouldn't wipe a real freelancer's invoice history.

## Rollback approach

Since distribution is a shared APK rather than a live server, "rollback" means keeping the previous milestone's signed build available; for the Supabase backend, RLS policies and any database migrations are version-controlled (via the Supabase CLI's migration files) so a bad policy or schema change can be reverted quickly.

Next: [16 — Risks and Mitigation](16-risks-and-mitigation.md)
