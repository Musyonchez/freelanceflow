# 5 — Running the App

## Step 1: Pick a device

In Android Studio's toolbar, near the Run button, there's a device dropdown. Make sure it shows either your virtual device (created in [guide 3](03-opening-in-android-studio.md)) or a physical phone connected via USB with debugging enabled. If it says "No devices", open **Device Manager** and start your virtual device first (click the ▶ play icon next to it), then come back.

## Step 2: Press Run

Click the green ▶ **Run** button in the toolbar (or `Shift+F10` on Windows/Linux, `Ctrl+R` on macOS).

The first build can take a few minutes — Android Studio is compiling the whole app from source. Progress shows in the status bar at the bottom. Subsequent runs are much faster since only changed code gets rebuilt.

Once it finishes, the emulator (or your phone) window should come to the foreground and the app launches automatically.

## Step 3: Allow notifications

On first launch, the app asks for permission to send notifications (used for overdue-invoice alerts later). Tap **Allow**. If you accidentally tap "Don't allow", nothing breaks — you just won't get those alerts, and you can re-enable the permission later from the device's app settings if you change your mind.

## Step 4: Sign up

You'll land on a sign-in screen titled "Welcome back". Since this is your first time, tap **New here? Create an account** to switch to sign-up mode, then:

1. Enter any email address. It doesn't need to be a real, reachable inbox — Supabase doesn't actually send mail with email confirmation turned off (per [guide 4](04-setting-up-supabase.md)), it just needs something in the right `name@domain` shape. Note: Supabase does reject a handful of obviously-fake domains like `example.com`, so use something that looks plausible, e.g. `yourname@gmail.com` — it doesn't need to exist.
2. Enter a password (Supabase's default minimum is 6 characters).
3. Tap **Sign up**.

You should land straight on the app's Home dashboard — no email confirmation step, since that was disabled during setup.

If you instead see an error message, check [guide 8 — Troubleshooting](08-troubleshooting.md).

## What you should have now

The app running on your emulator or phone, signed in with an account you just created, looking at an empty Home dashboard (all zeroes — you haven't added anything yet).

Next: [6 — App tour](06-app-tour.md) to see what everything does, or jump straight to [7 — Everyday usage](07-everyday-usage.md) for a hands-on walkthrough.
