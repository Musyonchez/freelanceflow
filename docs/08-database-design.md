# 08 — Database Design

Local schema (Room/SQLite), mirrored in structure to the Firestore cloud model for sync. All monetary fields store the amount plus an explicit currency code (ISO 4217, e.g. `USD`, `KES`, `GBP`) — never a bare number.

## Entity-relationship overview

```
User (1) ───< Client (1) ───< Project (1) ───< TimeEntry
                   │                │
                   │                └───< InvoiceItem >─── Invoice ───< Payment
                   │
                   └───< Expense
Invoice (1) ───< InvoiceItem
TaxProfile (1) ─── User
ExchangeRateCache — independent lookup table, currency pair + timestamp
```

## Entities

### User
| Field | Type | Notes |
|---|---|---|
| id | UUID (PK) | Firebase Auth UID |
| name, email | text | |
| baseCurrency | text (ISO code) | user's home/reporting currency |
| createdAt | datetime | |

### Client
| Field | Type | Notes |
|---|---|---|
| id | UUID (PK) | |
| userId | UUID (FK → User) | |
| name, company, email, phone | text | |
| defaultCurrency | text (ISO code) | |
| notes | text, nullable | |
| status | enum: active/archived | |

### Project
| Field | Type | Notes |
|---|---|---|
| id | UUID (PK) | |
| clientId | UUID (FK → Client) | |
| name, description | text | |
| rateType | enum: hourly/fixed | |
| rateValue | decimal | |
| currency | text (ISO code) | |
| status | enum: active/on_hold/completed | |

### TimeEntry
| Field | Type | Notes |
|---|---|---|
| id | UUID (PK) | |
| projectId | UUID (FK → Project) | |
| startTime, endTime | datetime, nullable | null endTime = running timer |
| durationMinutes | int | derived or manually entered |
| description | text | |
| billed | boolean | flips true once included on an invoice |

### Invoice
| Field | Type | Notes |
|---|---|---|
| id | UUID (PK) | |
| clientId | UUID (FK → Client) | |
| invoiceNumber | text, unique per user | sequential, e.g. `INV-2026-014` |
| issueDate, dueDate | date | |
| billingCurrency | text (ISO code) | client's currency |
| baseCurrency | text (ISO code) | freelancer's currency at issue time |
| exchangeRate | decimal | rate applied, snapshotted at creation |
| exchangeRateTimestamp | datetime | when that rate was fetched |
| subtotal, total | decimal | in billingCurrency |
| status | enum: draft/sent/paid/overdue | |
| notes | text, nullable | |

### InvoiceItem
| Field | Type | Notes |
|---|---|---|
| id | UUID (PK) | |
| invoiceId | UUID (FK → Invoice) | |
| description | text | |
| quantity | decimal | hours or units |
| unitPrice | decimal | in project currency |
| lineTotal | decimal | |
| sourceTimeEntryId | UUID, nullable (FK → TimeEntry) | traceability back to logged time |

### Payment
| Field | Type | Notes |
|---|---|---|
| id | UUID (PK) | |
| invoiceId | UUID (FK → Invoice) | |
| amount, currency | decimal, text | |
| paidDate | date | |
| method | text, nullable | free text (bank transfer, PayPal, etc.) |

### Expense
| Field | Type | Notes |
|---|---|---|
| id | UUID (PK) | |
| userId | UUID (FK → User) | |
| clientId, projectId | UUID, nullable (FK) | optional link |
| amount, currency | decimal, text | |
| category | text | e.g. software, hardware, travel |
| date | date | |
| receiptPhotoUrl | text, nullable | Firebase Storage reference |
| deductible | boolean | |
| deductiblePercent | decimal, default 100 | partial deductions |

### TaxProfile
| Field | Type | Notes |
|---|---|---|
| id | UUID (PK) | |
| userId | UUID (FK → User) | |
| label | text | e.g. "Kenya sole-proprietor estimate" |
| deductionRatePercent | decimal | user-defined, not authoritative |
| notes | text, nullable | disclaimer text shown with figures |

### ExchangeRateCache
| Field | Type | Notes |
|---|---|---|
| id | UUID (PK) | |
| baseCode, targetCode | text (ISO codes) | currency pair |
| rate | decimal | |
| fetchedAt | datetime | used for the "rate as of" indicator |

## Design notes

- **No hard currency conversion on write for historical records** — an invoice stores the rate it was issued with, so past invoices remain accurate even as live rates change. Only *current* totals on the dashboard are computed live from `ExchangeRateCache`.
- **Soft status flags over deletion** — clients/projects are archived, not deleted, so historical invoices/expenses retain valid foreign keys.
- Indexes on `clientId`, `projectId`, `invoiceId` foreign keys for the list/filter queries the dashboard depends on.

Next: [09 — APIs and Integrations](09-api-and-integrations.md)
