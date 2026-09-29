# Karibu-Tools — AGENTS.md

## What this is

Utility functions missing from Vaadin 14+, for your [Kotlin](https://kotlinlang.org/)-based projects.
> Note: for Vaadin 23+, depend on `karibu-tools-23` instead, to bring some additional utilities.

## Promises

- **One jar for every Vaadin 14+.** An app adds `karibu-tools` whatever its Vaadin version; a new Vaadin major is supported in place, never forked into a new artifact.
- **Fill Vaadin's gaps, never replace Vaadin.** Extensions on Vaadin's own classes — no component hierarchy, no DSL, no framework to buy into.

## Design docs

| File | Owns | Loaded |
|---|---|---|
| `README.md` | the pitch, install, the catalogue of every public utility | — |
| `AGENTS.md` (this) | promises, invariants, the module map, conventions, commands | every turn |
| `CONTRIBUTING.md` | running the tests, the release procedure | — |
| `design/architecture.md` | how the pieces compose — the version-compat seam, the test matrix, adding a utility; normative | lazy |
| `design/decisions.md` | why this and not that — `D_` entries, FAQ-shaped | lazy |
| doc comments | what one symbol does and why it is shaped so | at the symbol |

Every fact lives in exactly one of these; the others link to it.

## Invariants

- **`karibu-tools` compiles against Vaadin 14 and must link on every Vaadin up to the newest.** API absent from 14 or changed since goes through `Class.forName` / `getMethod` behind a `VaadinVersion.get` check; a direct call compiles, then throws `NoSuchMethodError` elsewhere. See `D_one_jar_vaadin14`.
- **Vaadin and the servlet API are `compileOnly`.** The app brings its own; anything stronger drags Vaadin 14 or javax into a Vaadin 25 app.
- **Every utility is tested in the shared `Abstract*Tests` suites, never in one `testrun-*` module alone.** A test that runs on one Vaadin ships the others untested; the matrix is in architecture.md.

## Module map

- `karibu-tools` — the library: extensions over Vaadin 14 API, reflection for anything newer.
- `karibu-tools-23` — utilities over types born in Vaadin 23 (`TabSheet`); depends on `karibu-tools`.
- `testsuite:testbase` — shared test helpers (`expectList`, TOML parsing), JUnit 5, kotlin.test.
- `testsuite:vaadin14` — the main test bodies, `AbstractAllTests`, compiled against Vaadin 14.
- `testsuite:vaadin21` — test bodies needing Vaadin 21+ API, `AbstractAllTests21`.
- `testsuite:vaadin23` — test bodies for `karibu-tools-23`, `AbstractAllTests23`.
- `testsuite:testrun-*` — one per Vaadin / Hilla version (`vaadin14-stable`, `vaadin14-next`, `vaadin22`, `vaadin23`, `vaadin24`, `vaadin24next`, `vaadin25`, `vaadin25next`, `hilla`, `hilla-prev`, `hilla1`, `vaadin-hilla-hybrid`): pins it, nests the suites it supports.

## Conventions

- **Kotlin with `explicitApi()`, JVM 17 bytecode.** Every public declaration spells out `public` and its type; the Vaadin 25 test runs raise to 21 and skip on an older JDK.
- **Extension functions and properties on Vaadin's own classes**, package `com.github.mvysny.kaributools` (`.v23` in `karibu-tools-23`), one file per component family (`Comboboxes.kt`, `GridUtils.kt`).
- **Every public utility gets a README bullet**, with `Since x.y` when new and `Vote for <upstream issue>` when it patches a Vaadin gap.
- **Tests: JUnit 5, kotlin.test `expect`, Karibu-Testing's `MockVaadin`.** `karibu-tools/src/test` is only for Vaadin-independent code (`SemanticVersion`, `MimeType`).
- **Every version lives in `gradle/libs.versions.toml`.** The `testrun-*` tests read it to assert the detected version, so a bump there is the whole bump.

## Commands

- `./gradlew` — the default `clean build`: every `testrun-*` module, then `design/verify_design_tripwires.sh` (Linux only); what CI runs on push and PR, ubuntu × JDK 17 / 21 / 25, macOS and Windows on JDK 21 (`.github/workflows/gradle.yml`, which also runs the tripwires in a job of their own).
- `./gradlew test` — all tests on all Vaadin versions.
- `./gradlew :testsuite:testrun-vaadin25:test --tests '*GridUtilsTests*'` — one suite on one Vaadin version.
- `./gradlew koverHtmlReport` — the library jars' coverage merged over every `testrun-*`, in `build/reports/kover/html`.
- Releasing to Maven Central: `CONTRIBUTING.md`.

## Skills this project follows

- **Browserless Vaadin tests:** `MockVaadin.setup()` / `tearDown()` around each test, components driven server-side; the `karibu-testing` skill has the helpers and the artifact per Vaadin version.
- **Gradle:** the wrapper only, dependency sources from `~/.gradle/caches`, readable test failures; the `gradle` skill has the details.

## Maintenance of this file

Loaded every turn; cap 34 KB, a module's own `AGENTS.md` 10 KB. Over it, in this order:
delete what has no home — status, history, class lists, what the code already says; trim
each line to its fact plus one clause and send the explanation home — why →
`design/decisions.md`, how across symbols → `design/architecture.md`, how in one symbol →
its doc comment, what upstream does → `design/research.md`; only then a module's own
`AGENTS.md`, peripheral modules first, never the core. Never paraphrase a lazy entry into a
line here. `design/verify_design_tripwires.sh` checks the caps and the cites.
