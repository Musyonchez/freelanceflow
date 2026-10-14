# 2 — Getting the Code

## Step 1: Open the repository on GitHub

Go to **https://github.com/Musyonchez/freelanceflow** in your browser. This is the project's home — every file, every past change, and this guide are all there.

You'll see a file listing. The important ones at a glance:

- `README.md` — the project's front page (shown automatically below the file listing)
- `docs/` — academic system-design documents
- `guide/` — this walkthrough
- `mobile/` — the actual Android Studio project (this is what you'll open in Android Studio later)
- `supabase/` — the database schema for the backend

## Step 2: Get a copy onto your computer

You have two options. Pick whichever you're more comfortable with — they end up with the same result.

### Option A: Download a ZIP (no Git knowledge needed)

1. On the repository page, click the green **Code** button.
2. Click **Download ZIP**.
3. Extract the ZIP file somewhere you'll remember, e.g. `C:\Users\YourName\Code\freelanceflow` (Windows) or `~/Code/freelanceflow` (macOS/Linux).

This gives you a snapshot of the code. It won't update itself and you can't easily contribute changes back this way, but it's the fastest way to just try the app.

### Option B: Clone with Git (recommended if you installed Git)

Open a terminal (**Command Prompt**, **PowerShell**, **Git Bash**, or **Terminal** depending on your OS) and run:

```bash
git clone https://github.com/Musyonchez/freelanceflow.git
cd freelanceflow
```

This downloads the full project, including its history, into a new `freelanceflow` folder in whatever directory you ran the command from. `cd freelanceflow` moves your terminal into that new folder.

Unlike the ZIP, a clone can be updated later by running `git pull` from inside the folder, which fetches and applies the latest changes from GitHub.

## What you should have now

A folder on your computer named `freelanceflow` (or similar) containing `README.md`, `docs/`, `guide/`, `mobile/`, and `supabase/` — matching what you saw on the GitHub page.

Next: [3 — Opening the project in Android Studio](03-opening-in-android-studio.md)
