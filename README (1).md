# iMali Circle

iMali Circle is an Android app that helps South African stokvels (savings
clubs) manage their groups digitally — tracking contributions, payout
rotations, and membership, with support for members who capture their
records on behalf of others who don't have a smartphone.

This repository contains the **Android application** (Kotlin, Jetpack
Compose). The REST API and database that power it live in a separate
repository: **[imalicircle-backend](PASTE_BACKEND_REPO_LINK_HERE)**.

> 🎥 **Demo video:** PASTE_YOUTUBE_OR_DRIVE_LINK_HERE

## Why this app

*(Write 2–3 sentences here in your own words about the problem stokvels
face — informal record-keeping, disputes over who paid, who's next for a
payout — and why a simple digital tool helps. This ties back to your
Part 1 research on StokFella, Stoki and Mzansi Stokvel Co.)*

## Features

- Register and log in with a phone number and password (passwords are
  hashed with bcrypt on the server — never stored or transmitted in
  plain text)
- Create a stokvel, invite members by phone number, and set group rules
  (contribution amount, frequency, payout order)
- Always-visible payout rotation showing who's paid and who's next
- Capture contributions per member per period, including "capture on
  behalf" for members without a smartphone (admins/treasurers only)
- Works offline: contributions are saved locally first and sync
  automatically once the device is back online, with a clear
  synced/pending indicator
- Settings: change language, toggle push notifications, toggle
  biometric login, update phone number and password
- Multi-language support: English, isiZulu, isiXhosa — switchable at
  any time without restarting the app

## Design

*(Briefly describe the navy/green/gold colour palette and how it reflects
the app's identity/cultural grounding, referencing your Part 1 design doc.
Add a screenshot or two here once you have them — drag the images into
this README on GitHub's web editor and it will insert the markdown for
you.)*

## Architecture

| Layer | Technology |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Local storage / offline cache | Room |
| Background sync | WorkManager |
| Networking | Retrofit + OkHttp |
| Backend API | Node.js + Express (see [imalicircle-backend](PASTE_BACKEND_REPO_LINK_HERE)) |
| Database | PostgreSQL, hosted on Railway |
| Auth | JWT, bcrypt-hashed passwords |

The app follows an **offline-first** approach: every contribution is
written to the local Room database immediately, marked as "pending", and
a background WorkManager job pushes it to the server as soon as there's a
network connection. Nothing is lost if the app is used offline.

## Project structure

```
app/src/main/java/com/imalicircle/
├── data/            # Repository, Room entities/DAOs, session management
│   ├── local/
│   └── remote/       # Retrofit API service and DTOs
├── sync/             # WorkManager background sync
├── ui/                # Jetpack Compose screens, by feature
│   ├── auth/
│   ├── home/
│   ├── settings/
│   └── stokvel/
└── util/              # Validation, formatting, payout rotation logic
```

## Running it yourself

1. Clone this repository.
2. Open it in Android Studio.
3. Make sure the backend API is running (see the
   [backend repo](PASTE_BACKEND_REPO_LINK_HERE) for setup) and update the
   `BASE_URL` in `Config.kt` to point to it.
4. Run the app on an emulator or device.

## Continuous Integration

This repo uses **GitHub Actions** (`.github/workflows/android-build.yml`)
to automatically build the app and run its unit tests on every push to
`main`. You can see the run history under the **Actions** tab, and each
successful run publishes a debug APK as a downloadable build artifact.

## Testing

Unit tests cover the core logic that doesn't depend on the Android
framework: phone number/password validation, and payout rotation date
calculations. Run them locally with:

```
./gradlew testDebugUnitTest
```

## Author

Amandla Sekeleni (ST10456349)

## AI usage

*(If you're including the optional AI-usage write-up, link or summarise
it here — a sentence noting that Claude was used to help build this
prototype, with the write-up itself as a separate file, e.g.
`AI_USAGE.md`, keeps the README focused.)*
