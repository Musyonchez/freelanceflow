# 3 — Opening the Project in Android Studio

## Important: open the `mobile` folder, not the repo root

The repository root (`freelanceflow/`) holds documentation folders alongside the actual Android project. The Android project itself — the thing Android Studio understands — lives one level down, in `freelanceflow/mobile/`. Opening the wrong folder is the most common first mistake, so it's worth saying twice: **open `mobile/`, not the folder above it.**

## Step 1: Launch Android Studio and open the project

1. Open Android Studio.
2. On the welcome screen, choose **Open** (if you already have another project open, use **File > Open** instead).
3. Browse to where you cloned/extracted the repository, then into the `mobile` folder, and select it.
4. Click **OK**.

## Step 2: Let Gradle sync

The first time you open the project, Android Studio will show a progress bar at the bottom labelled something like "Gradle sync" or "Downloading...". This step:

- Downloads the exact versions of the Android build tools, Kotlin compiler, and every library the app depends on
- Reads the project's build configuration and sets up code completion, error-checking, etc.

**This can take several minutes the first time**, depending on your internet connection — it's downloading real software, not just reading local files. Subsequent opens are much faster since everything gets cached.

If it finishes with no red error banner at the top, you're done with this step. If you do see an error, check [guide 8 — Troubleshooting](08-troubleshooting.md) before anything else; most first-sync problems have a known fix there.

## Step 3: Create a virtual device (if you don't already have one)

To run the app, you need either a real Android phone plugged in with USB debugging enabled, or a virtual one ("emulator"). If you've never used Android Studio before, you likely need to create a virtual device:

1. Open **Tools > Device Manager** (or click the device icon in the toolbar).
2. Click **+ Add a new device** (or **Create device**).
3. Pick a phone profile — any modern phone (e.g. "Pixel 9") works fine, there's nothing special this app needs.
4. Pick a system image to run on it — choose a recent one (API level 33 or higher is comfortably within this app's supported range). Android Studio will prompt you to download it if you don't have it yet; this is another one-time download.
5. Finish the wizard. The new virtual device now appears in your Device Manager and in the device dropdown in the toolbar.

You only need to do this once — the virtual device persists for future runs.

## What you should have now

The project open in Android Studio with no red sync errors, and at least one device (emulator or physical phone) available to run on. You're not ready to press Run yet, though — the app needs a backend to sign in against first.

Next: [4 — Setting up Supabase](04-setting-up-supabase.md)
