# Architecture

How the pieces compose — what no single symbol can say and what would be expensive to overturn:
the version-compat seam, the test matrix, the flow of adding a utility. **Normative: the code
conforms.** Change this file first, then the code. Not here: why (`decisions.md` — cite the
`D_`), one symbol's behaviour (its doc comment), the module map (`AGENTS.md`). Cap 12 KB — over
it, research or doc-comment content has crept in.

---

## Wiring

- Dependencies point one way: `karibu-tools-23` → `karibu-tools` → Vaadin 14 API (`compileOnly`). Nothing in `karibu-tools` references a `.v23` symbol (`D_one_jar_vaadin14`).
- `VaadinVersion` is the version-compat seam: it detects the running Vaadin, Flow and Hilla versions once, and a version branch asks it; the one alternative is probing for a class that only newer Vaadin has (`HasLabel`, `HasPlaceholder`), `ClassNotFoundException` meaning "older".
- Newer API is reached reflectively and cached in a top-level `private val` (`_LitRenderer_Class` in `Renderers.kt`, the `Class.forName` lookups in `GridUtils.kt`), so the lookup runs once per JVM.
- Test bodies are written once, as `Abstract*Tests` classes in `testsuite:vaadin14` / `vaadin21` / `vaadin23` — `src/main`, so other modules can depend on them. Each `testrun-*` module pins one Vaadin and nests `AbstractAllTests`, `AbstractAllTests21` and `AbstractAllTests23` as far as its version reaches, excluding the suites' own `com.vaadin` dependencies.
- `testapp` depends on `karibu-tools-23` like an app would, on the `vaadin25` catalog version; it demoes utilities, never replaces their `Abstract*Tests`. Its Karibu tests only guard that the demo routes render.

## Flows

**Adding a utility whose Vaadin API changed across versions:**

1. Write it in `karibu-tools` against the Vaadin 14 API; branch on `VaadinVersion.get.isAtLeast(n)` where the API differs, reaching the newer side by reflection (`Upload.clear()` is the smallest example).
2. Test it in the `Abstract*Tests` suite of the oldest Vaadin that has the API, nested into that module's `AbstractAllTests*`.
3. `./gradlew test` runs it on every `testrun-*` version; a version-specific expectation branches on `VaadinVersion` inside the test, not in a `testrun-*` module.
4. Add its README bullet.

## Where to start reading

`VaadinVersion.kt` for the seam every version branch goes through, then `testsuite/vaadin14/src/main/kotlin/AllTests.kt` for the suite every Vaadin version runs.
