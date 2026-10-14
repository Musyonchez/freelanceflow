# 1 — Prerequisites

Before touching any code, get these in place. Every item here is free.

## Accounts

- **A GitHub account** — only needed if you want to clone over SSH or make your own copy (a "fork"). Not required just to download and run the code.
- **A Supabase account** — free, sign up at [supabase.com](https://supabase.com). This is the app's cloud backend (login + data sync). You'll create a project for it in [guide 4](04-setting-up-supabase.md).
- **A Google account**, if you want to run the app on a real phone via the Play Store's testing tools — not required. An emulator (a virtual Android phone running on your computer) needs no Google account.

## Software to install

| Tool | Why you need it | Where to get it |
|---|---|---|
| **Git** | To download ("clone") the code, and to keep it updated | [git-scm.com](https://git-scm.com/downloads) |
| **Android Studio** | The IDE (editor + build tools + emulator) this project is built with. Get the latest stable release. | [developer.android.com/studio](https://developer.android.com/studio) |

Android Studio bundles its own copy of Java (the language Android build tooling runs on), so you don't need to separately install a JDK.

**A note on versions:** Android Studio periodically drops support for older project configurations and this project's build tooling periodically moves forward too. If Android Studio ever complains that the project uses a newer "Android Gradle Plugin" version than it supports, the fix is almost always to update Android Studio itself (`Help > Check for Updates` inside the app, or redownload from the site above) — not to downgrade the project. See [guide 8](08-troubleshooting.md) if this happens to you.

## Hardware

- A computer that can run Android Studio comfortably: 8GB RAM is the bare minimum, 16GB is comfortable, especially since you'll also be running an Android emulator (a virtual phone) at the same time.
- Windows, macOS, or Linux all work. This guide's example commands use a few Windows-specific notes where relevant, since that's what this project was built on, but everything here works the same way cross-platform through Android Studio's own UI.

## What you do *not* need

- You do **not** need to know Kotlin, Android development, or Jetpack Compose to get the app running and try it out. You'll only need that if you want to read or change the actual app code.
- You do **not** need a real Android phone. Android Studio ships with an emulator that's a complete virtual phone.
- You do **not** need to pay for anything. Supabase's free tier and Android Studio are both entirely free for a project this size.

Next: [2 — Getting the code](02-getting-the-code.md)
