-- FreelanceFlow — Supabase schema
--
-- Mirrors the Room entities in mobile/app/.../data/local/entity/ so the sync
-- worker can push/pull rows 1:1 with the local database. Run this once in the
-- Supabase SQL editor (Dashboard > SQL Editor > New query) on a fresh project.
--
-- Money is stored as integer minor units (e.g. cents) and dates as epoch
-- milliseconds (bigint), matching the on-device Room schema exactly — no
-- unit conversion needed when syncing in either direction.
--
-- Every table is scoped to its owner via Row Level Security: a row is only
-- readable/writable by the authenticated user who owns it (auth.uid() =
-- user_id). There is no cross-user sharing in this app (see
-- docs/04-scope-and-boundaries.md), so this is the only policy needed.

-- ── profiles ────────────────────────────────────────────────────────────
-- One row per auth user; mirrors BusinessProfileRepository's DataStore-backed
-- settings (business name, reporting base currency) so they survive reinstall.
create table if not exists profiles (
    user_id uuid primary key references auth.users (id) on delete cascade,
    business_name text not null default '',
    base_currency text not null default 'USD'
);

alter table profiles enable row level security;

create policy "profiles_owner_all" on profiles
    for all
    using (auth.uid() = user_id)
    with check (auth.uid() = user_id);

-- ── clients ─────────────────────────────────────────────────────────────
create table if not exists clients (
    id uuid primary key,
    user_id uuid not null references auth.users (id) on delete cascade,
    name text not null,
    company text,
    email text,
    phone text,
    default_currency text not null,
    notes text,
    status text not null default 'ACTIVE' check (status in ('ACTIVE', 'ARCHIVED')),
    created_at bigint not null
);

create index if not exists clients_user_id_idx on clients (user_id);
alter table clients enable row level security;

create policy "clients_owner_all" on clients
    for all
    using (auth.uid() = user_id)
    with check (auth.uid() = user_id);

-- ── projects ────────────────────────────────────────────────────────────
create table if not exists projects (
    id uuid primary key,
    user_id uuid not null references auth.users (id) on delete cascade,
    client_id uuid not null references clients (id) on delete restrict,
    name text not null,
    description text,
    rate_type text not null check (rate_type in ('HOURLY', 'FIXED')),
    rate_value_minor bigint not null,
    currency text not null,
    status text not null default 'ACTIVE' check (status in ('ACTIVE', 'ON_HOLD', 'COMPLETED')),
    created_at bigint not null
);

create index if not exists projects_user_id_idx on projects (user_id);
create index if not exists projects_client_id_idx on projects (client_id);
alter table projects enable row level security;

create policy "projects_owner_all" on projects
    for all
    using (auth.uid() = user_id)
    with check (auth.uid() = user_id);

-- ── time_entries ────────────────────────────────────────────────────────
create table if not exists time_entries (
    id uuid primary key,
    user_id uuid not null references auth.users (id) on delete cascade,
    project_id uuid not null references projects (id) on delete restrict,
    start_time bigint,
    end_time bigint, -- null means the timer was still running when last synced
    duration_minutes integer not null,
    description text,
    billed boolean not null default false
);

create index if not exists time_entries_user_id_idx on time_entries (user_id);
create index if not exists time_entries_project_id_idx on time_entries (project_id);
alter table time_entries enable row level security;

create policy "time_entries_owner_all" on time_entries
    for all
    using (auth.uid() = user_id)
    with check (auth.uid() = user_id);

-- ── invoices ────────────────────────────────────────────────────────────
create table if not exists invoices (
    id uuid primary key,
    user_id uuid not null references auth.users (id) on delete cascade,
    client_id uuid not null references clients (id) on delete restrict,
    invoice_number text not null,
    issue_date bigint not null,
    due_date bigint not null,
    billing_currency text not null,
    base_currency text not null,
    exchange_rate double precision not null,
    exchange_rate_timestamp bigint not null,
    subtotal_minor bigint not null,
    total_minor bigint not null,
    status text not null default 'DRAFT' check (status in ('DRAFT', 'SENT', 'PAID', 'OVERDUE')),
    notes text,
    unique (user_id, invoice_number)
);

create index if not exists invoices_user_id_idx on invoices (user_id);
create index if not exists invoices_client_id_idx on invoices (client_id);
alter table invoices enable row level security;

create policy "invoices_owner_all" on invoices
    for all
    using (auth.uid() = user_id)
    with check (auth.uid() = user_id);

-- ── invoice_items ───────────────────────────────────────────────────────
create table if not exists invoice_items (
    id uuid primary key,
    user_id uuid not null references auth.users (id) on delete cascade,
    invoice_id uuid not null references invoices (id) on delete cascade,
    description text not null,
    quantity double precision not null,
    unit_price_minor bigint not null,
    line_total_minor bigint not null,
    source_time_entry_id uuid references time_entries (id) on delete set null
);

create index if not exists invoice_items_user_id_idx on invoice_items (user_id);
create index if not exists invoice_items_invoice_id_idx on invoice_items (invoice_id);
alter table invoice_items enable row level security;

create policy "invoice_items_owner_all" on invoice_items
    for all
    using (auth.uid() = user_id)
    with check (auth.uid() = user_id);

-- ── payments ────────────────────────────────────────────────────────────
create table if not exists payments (
    id uuid primary key,
    user_id uuid not null references auth.users (id) on delete cascade,
    invoice_id uuid not null references invoices (id) on delete cascade,
    amount_minor bigint not null,
    currency text not null,
    paid_date bigint not null,
    method text
);

create index if not exists payments_user_id_idx on payments (user_id);
create index if not exists payments_invoice_id_idx on payments (invoice_id);
alter table payments enable row level security;

create policy "payments_owner_all" on payments
    for all
    using (auth.uid() = user_id)
    with check (auth.uid() = user_id);

-- ── expenses ────────────────────────────────────────────────────────────
create table if not exists expenses (
    id uuid primary key,
    user_id uuid not null references auth.users (id) on delete cascade,
    client_id uuid references clients (id) on delete set null,
    project_id uuid references projects (id) on delete set null,
    amount_minor bigint not null,
    currency text not null,
    category text not null,
    date bigint not null,
    receipt_photo_url text,
    deductible boolean not null default false,
    deductible_percent double precision not null default 100
);

create index if not exists expenses_user_id_idx on expenses (user_id);
alter table expenses enable row level security;

create policy "expenses_owner_all" on expenses
    for all
    using (auth.uid() = user_id)
    with check (auth.uid() = user_id);

-- ── tax_profiles ────────────────────────────────────────────────────────
create table if not exists tax_profiles (
    id uuid primary key,
    user_id uuid not null references auth.users (id) on delete cascade,
    label text not null,
    deduction_rate_percent double precision not null,
    notes text
);

create index if not exists tax_profiles_user_id_idx on tax_profiles (user_id);
alter table tax_profiles enable row level security;

create policy "tax_profiles_owner_all" on tax_profiles
    for all
    using (auth.uid() = user_id)
    with check (auth.uid() = user_id);

-- ── receipts storage bucket ─────────────────────────────────────────────
-- Run once; safe to re-run (insert ... on conflict do nothing).
insert into storage.buckets (id, name, public)
values ('receipts', 'receipts', false)
on conflict (id) do nothing;

-- Objects must live under a path prefixed with the owner's uid, e.g.
-- "{auth.uid()}/expense-123.jpg" — enforced by comparing the first path
-- segment to the caller's uid.
create policy "receipts_owner_all" on storage.objects
    for all
    using (bucket_id = 'receipts' and (storage.foldername(name))[1] = auth.uid()::text)
    with check (bucket_id = 'receipts' and (storage.foldername(name))[1] = auth.uid()::text);
