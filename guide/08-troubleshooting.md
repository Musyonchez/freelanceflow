# 8 — Troubleshooting

Problems people actually run into, in the order they tend to show up.

## "The project is using an incompatible version of the Android Gradle plugin"

Android Studio shows this when it's older than the project's build tooling requires. This is a statement about your **Android Studio installation**, not the project — the fix is to update Android Studio, not to change any project file.

Go to **Help > Check for Updates** inside Android Studio, or download the latest stable release directly from [developer.android.com/studio](https://developer.android.com/studio) and install it over your current one. Re-open the project afterwards.

## Gradle sync fails or hangs on first open

- Check your internet connection — the first sync downloads a real amount of software (the Android build tools, Kotlin compiler, and every library dependency).
- If it hangs for a very long time with no progress, try **File > Invalidate Caches** (restart Android Studio when prompted) and let it resync.
- Make sure you opened the `mobile/` folder specifically, not the repository root — see [guide 3](03-opening-in-android-studio.md).

## "Invalid login credentials" when signing in

This means Supabase doesn't recognize the email+password combination you entered. Usually it's one of:

- A typo in the email or password (easy to introduce, especially on an emulator's on-screen keyboard)
- You haven't actually signed up yet with that email — try **New here? Create an account** instead of signing in
- You signed up *before* turning off "Confirm email" in Supabase (see [guide 4](04-setting-up-supabase.md)), so the account exists but is stuck waiting on a confirmation email that was never sent. The fix is to sign up again with a different email, after confirming the email-confirmation setting is off.

## "Email address is invalid" when signing up

Supabase blocks a short list of obviously-fake domains (like `example.com`) even when email confirmation is off, since it still validates the address shape. Use any plausible-looking address instead — it doesn't need to be a real, reachable inbox (see [guide 5](05-running-the-app.md)).

## Nothing happens after tapping "Sign up" — no error, no navigation

If you're testing against a Supabase project that still has "Confirm email" turned **on**, this is expected: the account was created, but no session starts until the confirmation link (sent to an inbox you likely don't actually control, since any plausible-looking address is accepted at signup) is clicked. Go back to [guide 4, Step 3](04-setting-up-supabase.md#step-3-make-sign-up-instant-recommended-for-trying-the-app-out) and turn email confirmation off, then sign up again with a fresh email address.

## The emulator shows "App isn't responding" right after a fresh boot

This is a known quirk of freshly-booted Android emulators under load (lots of background compilation/indexing happening at once right after boot), not something wrong with the app. Tap **Wait** (not **Close app**) and give it a few seconds — it reliably recovers once the emulator settles down. This tends to happen on the very first launch after booting a virtual device from cold, not on subsequent launches.

## `local.properties` doesn't have my Supabase values, or I can't find the file

`local.properties` is intentionally not part of the repository (it's listed in `.gitignore`, since it's meant to hold values specific to *your* machine and *your* Supabase project). Android Studio creates a bare-bones version of it automatically on first open — if you don't see it in Android Studio's project file tree, make sure you're looking at the "Project" view (not "Android" view) in the left sidebar, since some views hide dotfiles-adjacent config files by default. See [guide 4, Step 5](04-setting-up-supabase.md#step-5-add-them-to-the-android-project) for exactly what to add.

## Notifications never show up

- Make sure you tapped **Allow** on the permission prompt at first launch (see [guide 5](05-running-the-app.md)). If you tapped "Don't allow" by accident, re-enable it from the device's system **Settings > Apps > FreelanceFlow > Notifications**.
- Overdue-invoice notifications only fire for invoices that are **Sent** (not Draft or already Paid) and whose due date has passed — and the background check that looks for these runs periodically (roughly every 15 minutes while the app is installed), not instantly.

## Receipt photo thumbnail doesn't show up on another device

Receipt photos upload to Supabase Storage during the next sync *after* you capture them (not instantly), and are only downloaded to a second device the first time that device actually tries to display them. If you just captured a photo, give it a sync cycle (or pull-to-trigger-sync by signing out and back in) before checking on the other device.

## Build succeeds but the app crashes immediately on launch

This is uncommon, but if it happens, the single most useful next step is checking Android Studio's **Logcat** panel (bottom of the window) at the moment of the crash — it prints a full stack trace. If you're reporting this as an issue, include that stack trace; "it crashed" on its own isn't enough to diagnose.

## Still stuck?

Open an issue on the GitHub repository describing exactly what you did and what happened, including any error text verbatim — that's far more useful for getting help than a summary in your own words.
