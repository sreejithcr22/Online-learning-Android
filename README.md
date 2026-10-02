# Aptitude & GK Guide

A free, fully offline aptitude, reasoning, verbal ability and general knowledge app with a
timer for every question, mock tests, interview tips, formulas and detailed progress tracking.

<a href='https://play.google.com/store/apps/details?id=com.codit.interview.aptitude'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>

## Architecture

This release is a **Kotlin / Jetpack Compose rewrite** of the original Java/View app, built on
**MVVM + Clean Architecture** with **Hilt** dependency injection and **Kotlin Flows**.

The app is a single-module Gradle project with strict package-level layering. Dependencies only
point inwards:

```
presentation/  ──depends on──▶  domain/  ◀──implemented by──  data/
   (Compose, ViewModels)        (models,               (repositories,
                                 repository             SQLite, prefs)
                                 interfaces,
                                 use cases)
```

| Package | Responsibility |
| --- | --- |
| `domain/model` | Pure Kotlin models (`Question`, `Topic`, `ProgressSnapshot`, `SessionReport`, …) |
| `domain/repository` | Interfaces the domain layer depends on |
| `domain/usecase` | Single-purpose actions the UI invokes |
| `data/local` | SQLite access: the pre-seeded asset databases and the app-created `userstate.db` |
| `data/prefs` | `SharedPreferences` wrappers exposing `Flow`s |
| `data/mapper` | Normalises loosely-typed SQLite columns into domain models |
| `data/repository` | Interface implementations; the only place both sides are known |
| `presentation/*` | Compose screens, one `@HiltViewModel` per screen, reusable components |
| `di` | Hilt modules binding interfaces to implementations |
| `core` | Dispatcher abstraction, ticker `Flow`, `Outcome`, formatting helpers |

### Tech stack

- Kotlin 2.0.21, AGP 8.7.3, Gradle 8.9, JDK 17
- Jetpack Compose + Material 3 (BOM 2024.12.01)
- MVVM: `ViewModel` + `StateFlow<UiState>` + one-shot events via `SharedFlow`-style state
- Navigation Compose (single `Activity`, route arguments carry all navigation state)
- Hilt 2.52 (KSP) for DI
- Coroutines 1.9.0 + `StateFlow` for all observable state
- `minSdk 24`, `targetSdk 36`

### Data

Content ships as SQLite databases in `assets/databases` (1.7 MB: 1 560 questions, 10 mock tests,
~250 interview tips and formulas). `AssetDatabaseHelper` copies an asset to the app's private
files directory on first use and opens it from there, so the databases stay writable without any
third-party asset-DB library.

`userstate.db` holds app-created state (mock-test scores, per-topic timer durations) and is seeded
from the `Topic` enum, so adding a topic no longer requires a manual migration.

### Testing

```bash
./gradlew :app:testDebugUnitTest
```

Unit tests cover the pure logic: the calculator engine, the SQLite text parsers that handle the
`"null"`/`"option3"`/`"mm:ss"` sentinels in the shipped content, and the derived statistics behind
the analytics and report screens.

### Building

```bash
./gradlew :app:assembleDebug
./gradlew :app:assembleRelease   # requires the keystore described below
```

Release signing reads `storeFile`, `storePassword`, `keyAlias` and `keyPassword` from
`local.properties` (all optional, defaults shown below); the keystore itself is kept out of the
repository.

## Notes on the rewrite

Everything the app does is preserved. A few things got better along the way:

- **Question totals are read from SQLite** instead of hardcoded constants. The old values
  (372 quantitative / 300 GK / 228 verbal) no longer matched the shipped content, which made the
  overall progress percentage meaningless. The real figures are 467 / 303 / 215 for the aptitude
  sections and 325 for GK.
- **Answer status is always derived**, never stored, so a resumed question cannot show a stale
  "not answered" state.
- **Navigation state lives in route arguments** rather than mutable `static` fields, so a session
  survives process death.
- **One timer implementation.** The old app had several hand-rolled `Timer` + `TimerTask` +
  `Handler` loops; they are now a single cold `Flow` scoped to the ViewModel.
- **Charts are Compose `Canvas`**, which removes the `achartengine` and `CircleProgressBar`
  libraries. The circular progress indicators, donut chart and per-question time chart are all
  drawn directly.
- **Theme switching is instant** — Compose recomposes instead of restarting the activity.

Dependencies dropped in the rewrite: AppCompat, Material Components, CardView, RecyclerView,
Legacy Support, VectorDrawable, `achartengine`, `sqliteassethelper`, `dinuscxj/CircleProgressBar`,
and a set of unused ad-SDK jars.

## Content

1. Free and fully offline (works without internet)
2. Timer for every question, plus a whole-test timer for mock tests
3. Built-in calculator and per-question notes
4. Progress tracking: totals, accuracy, average/highest time, per-section and per-topic completion
5. Questions from Aptitude, Reasoning, Verbal and GK
6. Mock tests that simulate a real exam
7. HR, technical and behavioural interview questions, answers and tips
8. Formulas and concepts for every aptitude, reasoning and verbal topic
9. Favourites for questions, tips and formulas

## Repo owner

sreejithcr2@gmail.com