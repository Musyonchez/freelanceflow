# 14 — Security, Privacy and Compliance

## Data protection

- **Encryption in transit:** all network calls (Firebase SDK, exchange-rate API) go over HTTPS.
- **Encryption at rest:** local Room database considered for SQLCipher (encrypted SQLite) given it holds client PII and financial data; at minimum, Android's standard app-sandboxed storage plus device-level encryption is relied upon, with SQLCipher noted as a hardening upgrade if time allows.
- **Authentication:** Firebase Authentication (email/password + Google sign-in); no financial data accessible without a signed-in session.
- **Authorization:** Firestore security rules scope every read/write to `request.auth.uid == resource owner's uid` — no user can read another user's clients, invoices, or expenses.
- **Receipt photos:** stored in Firebase Cloud Storage under a per-user path, governed by matching storage security rules.

## Privacy considerations

- The app stores **personal data about third parties** (clients) on behalf of the freelancer — names, emails, phone numbers. This is handled the same as any personal-data-processing app: data is only used to power the app's own features (invoicing, contact), never shared with third parties, and deletable by the user (deleting a client removes their PII; historical invoices retain only what's needed for the freelancer's own records).
- Freelancers operating across borders should be aware that **local data protection law applies to their own data handling of clients**, e.g. Kenya's **Data Protection Act (2019)**, or GDPR when dealing with EU-based clients. The app's design (owner-only data access, ability to edit/delete client records) supports basic compliance, but the app itself does not certify legal compliance for the freelancer — this is disclosed to the user in-app (e.g., in Settings/About).

## Invoice "compliance" — what it means here

"Compliant" in this project's context means the generated PDF includes the fields generally expected of a professional invoice:
- Unique, sequential invoice number
- Issue date and due date
- Freelancer's (seller's) name/business details and the client's details
- Itemized description of goods/services
- Currency and exchange rate disclosed when currencies differ
- Clear total

It does **not** mean certified compliance with any specific country's fiscal/tax invoicing law (e.g., VAT invoice requirements, e-invoicing mandates), since that varies by jurisdiction and is out of scope ([04-scope-and-boundaries](04-scope-and-boundaries.md)). The app should display a brief disclaimer near tax/deduction figures: *"Estimate only — consult a licensed tax professional for filing."*

## Threat considerations (lightweight)

| Risk | Mitigation |
|---|---|
| Lost/stolen device exposing financial data | Device-level lock + app data sandboxed; optional in-app PIN/biometric lock as a stretch feature |
| Firestore misconfiguration exposing other users' data | Security rules unit-tested against the Firebase emulator before release |
| Man-in-the-middle on API/Firebase calls | Enforced HTTPS by default via Firebase SDK / OkHttp config |
| Receipt photos containing sensitive info (e.g., card numbers) | User-controlled; app does not OCR/parse receipt contents, purely stores the image |

Next: [15 — Deployment and Maintenance](15-deployment-and-maintenance.md)
