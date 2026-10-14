# 7 — Everyday Usage: A Worked Example

This walks through a complete, realistic use of the app from an empty account to a paid invoice and a tax estimate — the same sequence a freelancer would actually follow. Follow along on your own running copy of the app.

## 1. Add a client

Go to **Clients > +**. Fill in:

- **Name**: `Acme Studio` (required)
- **Billing currency**: `USD`

Tap **Add client**. You'll see it appear in the Clients list.

## 2. Add a project for that client

Go to **Work > +**. Fill in:

- **Client**: `Acme Studio` (pre-filled if it's your only client)
- **Project name**: `Website Redesign`
- **Rate type**: Hourly
- **Rate / hour**: `50`
- **Currency**: `USD`

Tap **Add project**. It now shows in the Work tab as `$50.00 / hr · active`.

## 3. Track some time

Still on the Work tab, tap the **play** button on the `Website Redesign` project row. A "running timer" card appears at the top of the screen. Let it run for a bit (or just tap **stop** shortly after — the app records whatever elapsed time actually passed, down to the minute).

## 4. Create an invoice from that time

Go to **Money > Invoices > +**. Select the `Website Redesign` project — any unbilled time you logged in step 3 should be pulled in automatically as a line item. You can also add a manual line item (e.g. a flat "Setup fee" of $25) using the description/quantity/unit price fields and **Add item**.

Set **Due in (days)** to something like `14`, optionally add notes, then tap **Create invoice**. It appears in the list as a **Draft**, numbered automatically (e.g. `INV-2026-001`).

## 5. Send it

Tap **Mark sent** on the invoice row. Its status changes to **Sent**.

Tap the share icon to generate a PDF and open your device's share sheet — try sharing it to yourself via email or saving it, to see the actual invoice document with your logged time/line items, the client's details, and the total.

## 6. Mark it paid

Once a client actually pays (in real use), tap **Mark paid** on the invoice row. Its status flips to **Paid**, and the Home dashboard's "Outstanding" total drops while "Paid" increases by the invoice total.

## 7. Log an expense

Go to **Money > Expenses > +**. Log something like:

- **Amount**: `15`, **Currency**: `USD`
- **Category**: `Software`
- Toggle **Deductible** on
- Optionally tap **Camera** or **Gallery** to attach a photo of the receipt

Tap **Add expense**. It shows in the Expenses list (with a thumbnail if you attached a photo), and the Home dashboard's "Expenses" total updates.

## 8. Set up a tax profile and see the estimate

Go to **Money > Tax**, tap the pencil icon, and set:

- **Label**: `My estimate`
- **Deduction rate %**: `20`

Save. Back on the Tax tab, with the **This month** (or **All time**, depending on when you logged the expense) filter selected, you should see the deductible total (matching the $15 expense from step 7) and an estimated deduction figure (20% of that).

## 9. Check the dashboard

Go to **Home**. All three summary cards now reflect real data: paid total from the invoice, expense total, and — if you have any other invoices still outstanding — the outstanding total too.

## What this demonstrated

The full day-to-day loop the app is built around: client → project → logged time → invoice → PDF → payment, plus expense tracking feeding into a tax estimate — all converted consistently into whatever base currency you've set, and all synced to your Supabase project so it'd survive reinstalling the app or signing in on another device.

Next: [8 — Troubleshooting](08-troubleshooting.md) if anything along the way didn't go as described.
