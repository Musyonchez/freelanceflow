# FreelanceFlow — Beginner's Guide

This folder is a start-to-finish walkthrough for someone who has never touched this project before: opening the GitHub page for the first time, getting the code onto your computer, setting up the one external service it needs, running the app, and understanding what you're looking at once it's running.

If you're looking for the *academic* system-design documents (requirements, architecture, database design, and so on), those live in [`../docs/`](../docs/README.md) instead — that's a different kind of document, written for a course submission. This folder is a hands-on, practical guide.

No prior Android development experience is assumed. Where a step is "normal programmer knowledge" (like what a terminal is), it's explained briefly anyway.

## Read these in order

| # | Guide | What it covers |
|---|---|---|
| 1 | [Prerequisites](01-prerequisites.md) | Accounts and software to install before you start |
| 2 | [Getting the code](02-getting-the-code.md) | From the GitHub page to a copy of the project on your computer |
| 3 | [Opening the project](03-opening-in-android-studio.md) | Importing it into Android Studio and surviving the first sync |
| 4 | [Setting up Supabase](04-setting-up-supabase.md) | The one external service the app needs, and how to wire it up |
| 5 | [Running the app](05-running-the-app.md) | Picking a device, hitting Run, and signing in for the first time |
| 6 | [App tour](06-app-tour.md) | What every screen and tab does |
| 7 | [Everyday usage](07-everyday-usage.md) | A full worked example: client → project → time → invoice → PDF → expense → tax estimate |
| 8 | [Troubleshooting](08-troubleshooting.md) | Fixes for the problems people actually hit |

## What you'll end up with

A working copy of **FreelanceFlow** — a client/project/invoicing app for freelancers — running on your own Android emulator or phone, connected to your own free Supabase backend, with its own account you signed up yourself. Nothing you do following this guide touches the original project's data.
