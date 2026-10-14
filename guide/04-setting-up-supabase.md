# 4 — Setting Up Supabase

FreelanceFlow stores all your data locally on-device first (it works fully offline), and separately syncs it to a cloud backend called **Supabase** so your data can follow you across devices once signed in. The app needs its own Supabase project to talk to — you're going to create one, entirely free, in a few minutes.

## Step 1: Create a Supabase account and project

1. Go to **https://supabase.com** and sign up (GitHub login is the fastest option, but email works too).
2. Once logged in, click **New project**.
3. Pick any organization (Supabase creates a default one for you), give the project a name (e.g. "freelanceflow-dev"), set a database password (Supabase will generate one for you if you don't — either is fine, just keep it somewhere, you won't need it for this guide but it's good practice to keep it), and pick any region close to you.
4. Click **Create new project**. It takes a minute or two to provision — Supabase shows a progress screen.

## Step 2: Run the database schema

The app expects a specific set of database tables to exist (clients, projects, invoices, and so on), each with security rules that keep one user's data private from another's. The exact SQL to create all of this is already written for you in the repository.

1. In your new Supabase project, open the **SQL Editor** from the left sidebar.
2. Click **New query**.
3. Open the file `supabase/schema.sql` from the repository you cloned, in any text editor, and copy its entire contents.
4. Paste it into the Supabase SQL editor.
5. Click **Run** (or press Ctrl+Enter / Cmd+Enter).

You should see a success message. If you want to double check, open **Table Editor** in the sidebar — you should now see tables named `clients`, `projects`, `invoices`, `expenses`, and several others.

## Step 3: Make sign-up instant (recommended for trying the app out)

By default, Supabase requires a new user to click a confirmation link sent to their email before they can sign in. For quickly trying the app out, it's easier to turn this off:

1. In Supabase, go to **Authentication > Sign In / Providers > Email**.
2. Turn off **Confirm email**.
3. Save.

(If you're setting this up for something closer to real-world use later, you'd want to turn this back on and configure a proper email provider — but for exploring the app, instant sign-up is much less friction.)

## Step 4: Get your project's connection details

The app needs two values to find and authenticate with your project — both are meant to be used from client apps, so there's no risk in having them in a local config file:

1. In Supabase, go to **Settings > API** (or **Project Settings > API**, naming varies slightly by Supabase version).
2. Note down the **Project URL** (looks like `https://xxxxxxxx.supabase.co`).
3. Note down the **anon / public key** (a long string starting with `eyJ...`) — not the `service_role` key, which is a much more powerful secret you should never put in a mobile app.

## Step 5: Add them to the Android project

1. In your cloned repository, open (or create) the file `mobile/local.properties` in a text editor. (This file doesn't exist until Android Studio or you create it — it's intentionally excluded from the repository since it holds machine-specific/local settings.)
2. Add these two lines, replacing the placeholders with your own values from Step 4:

   ```properties
   SUPABASE_URL=https://xxxxxxxx.supabase.co
   SUPABASE_ANON_KEY=eyJ...your-anon-key-here
   ```

3. Save the file.

If `local.properties` already exists (Android Studio creates it automatically on first open, to record your Android SDK location), just add these two lines to the bottom of the existing file rather than replacing it.

**This file should never be committed to Git** — it's already listed in `.gitignore` for exactly that reason, so a normal `git add`/`git commit` won't pick it up. That's deliberate: these values are specific to *your* Supabase project.

## What you should have now

A Supabase project with the full schema loaded, email confirmation turned off, and its URL + anon key saved into `mobile/local.properties`.

Next: [5 — Running the app](05-running-the-app.md)
