# Decisions

Why this project is the way it is and not otherwise — FAQ-shaped: each entry is a question and
its current answer. Rewrite the answer when it changes; delete the entry when nobody asks any
more. An entry is earned by what it would cost to reverse — half the code base — or by research
the next person would otherwise redo. Not an entry: windows → panels "because that's the
trend", this red over that red, `get_foo` over `is_foo?`, the testing library, the CI host, a
version bump — a comment at the site of the choice, or nothing; nothing about `design/` itself.
Cite by slug, `D_<slug>`, never by position; `grep '^## D_' design/decisions.md` is the index.
The first entry is the ruler: every later one trims to its length — which is how long this file
gets, so keep it short. When you have written an entry, re-read it against the one above, open
the doc comments it touches and cut what they already say, then cut the fat.

---

## D_one_jar_vaadin14 — Why compile against Vaadin 14 and reach newer API by reflection rather than ship one artifact per Vaadin version?

The promise is **One jar for every Vaadin 14+**: an app adds `karibu-tools` and it works on
whatever Vaadin the app runs, 14 through the current prerelease. Compiling against the oldest
supported API gives that for free wherever Vaadin stayed binary-compatible, which is most of
it; the few places it did not (`Upload.clear()`, `LitRenderer`, the Vaadin 24 Grid cells, the
Vaadin 25 Jackson switch) branch on `VaadinVersion` and go through reflection. Why not one
artifact per version (`-v14`, `-v24`, `-v25`): every utility would be copied or shared through
source sets, and the user would have to pick the right one and re-pick on every upgrade. Why
not compile against the newest: the jar would not link on 14. The one exception is
`karibu-tools-23`: `TabSheet` does not exist in Vaadin 14, and reflection cannot give an
extension function a receiver type the compiler has never seen, so it is a second jar on top
of the first rather than a fork of it. The cost we carry: every reflective call is invisible to
the compiler, so only the `testrun-*` matrix catches a break.

## D_deprecate_when_14_has_it — Why deprecate only the helpers Vaadin 14 already has, not every one Vaadin has since absorbed?

A deprecation tells the user to switch to the built-in, and under `D_one_jar_vaadin14` a
Vaadin 14 app has no built-in to switch to until 14 has it too; a warning with no way to act on
it is noise. So a helper is deprecated once the newest Vaadin 14 has the built-in (`isAttached()`,
`addClassNames()`, `QueryParameters.fromString()`), and otherwise only gets a README
"Built into Vaadin x.y+" note. The deprecation message names what the built-in does
differently, so switching is a choice, not a silent behavior change.
