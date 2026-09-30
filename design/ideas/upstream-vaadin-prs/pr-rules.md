# Upstream Vaadin PR rules (researched 2026-09-29)

Sources: shallow clones of `vaadin/flow`, `vaadin/flow-components` and `vaadin/web-components` at `main`
(2026-09-28/29, version `25.4-SNAPSHOT`), plus `gh` for the PRs and repo settings. `fc/` = flow-components.

## Where the branches go

- The user has **push** access to `vaadin/flow`, `vaadin/flow-components`, `vaadin/web-components` and
  `vaadin/platform` (`gh api repos/vaadin/<r> --jq .permissions`), so branches go **straight into upstream,
  with no fork**. Every recent feat PR, including the user's own #10269 and #8108, has `isCrossRepository=false`.
- Branch naming: `feat/<component>-<thing>` (fc #10182 `feat/form-layout-label-text-align`, #9614
  `feat/text-field-input-mode`, #10269 `feat/context-menu-lookup`). flow also uses `<issue>-<slug>`
  (flow #25480 `9291-browser-time-instant`).
- A same-repo branch also unlocks the opt-in `snapshot build` label: it publishes
  `25.4.<last-branch-segment>-SNAPSHOT` to `maven.vaadin.com/vaadin-prereleases` (`fc/.github/workflows/README.md`).
  Fork PRs cannot do this, which is one more reason to use a branch.
- Target branch is `main` (vaadin.com/docs/latest/contributing/pr). Backports happen afterwards via the
  `target/25.3` label and a bot cherry-pick (`vaadin-bot` "(CP: 25.3)" PRs such as fc #10139).
- The merge method is **squash or rebase, never a merge commit**, and the branch is deleted on merge (repo settings, all
  three repos). flow uses a merge queue (`merge_group` in `fc/.github/workflows/validation.yml`).

## CLA

- There is a `license/cla` status check on every PR (fc #10182 checks). The user's #10269 shows `license/cla SUCCESS`,
  so their account is already covered and nothing needs doing. The online guide does not mention a CLA.

## PR title / commit convention

- Conventional Commits: `feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:`, `ci:`, with `!` for breaking changes
  (`feat!:`). The subject is imperative and lowercase: "feat: add setLabelTextAlign to FormLayout"
  (flow `.claude/skills/commit-and-pr/SKILL.md`; online guide; every fc feat PR). The online guide says ≤50 characters
  and flow's skill says <72.
- With squash merge, the **PR title + description become the commit message verbatim**, so the
  description goes into `git log` (flow skill "What lands in git history").
- flow (explicit rule): **no `Co-Authored-By` trailer for Claude, no "Generated with" footer, no AI
  attribution** (flow skill, "Body" and "Before posting"). fc has no written rule, but the user's own #10269 has
  no footer, only a `> [!NOTE] Reviewed by Martin Vysny (@mvysny) before opening this PR.`. **This conflicts
  with Claude Code's default PR attribution, so drop it for upstream PRs.**

## PR description

- **flow-components** (`fc/PULL_REQUEST_TEMPLATE.md` plus practice): `## Description`, an issue link (`Fixes #`,
  `Part of #`, `Closes #`, `Depends on <url>`), then bullets of what was added and a sentence on why. Then
  `## Type of change` with `- Feature`. Recent PRs drop the checklist (#10182, #10180, #9915, #10269). The
  template's feature checkbox says: "Enhancement / new feature was discussed in a corresponding GitHub issue and
  Acceptance Criteria were created." When web-component work is needed, add `Depends on
  vaadin/web-components#…` and an optional `## How to test` (#9915).
- **flow** (`flow/.claude/skills/commit-and-pr/SKILL.md`, strict): plain markdown with no headings or HTML, about
  25 lines. The order is issue links, then a header line `**<problem type>** · <module> · <who is affected>` (problem
  types come from a closed list: `missing API`, `new feature`, …), an optional Background, a summary of at most
  3 sentences with no class names, **Risks** (a checklist, with non-empty flags such as ⚠️ Public API plus one ✅
  line), an optional Context, and past-tense "what changed" bullets. `## Type of change`, `## How to test` and
  `<details>` API changes go into the **first PR comment**, not the description. Open as a **draft** and
  self-review before marking ready. Relation words: `Fixes #` only for bugs, and `Part of #` for features.
- Separate features, fixes and enhancements into separate PRs, one logical change each (online guide; flow
  `CONVENTIONS.md` "Keep a pull request to one increment"). This matches the one-PR-per-utility decision.

## Issue first

- The fc and web-components PR templates expect a feature to be "discussed in a corresponding GitHub issue",
  with acceptance criteria. In practice most recent feat PRs link an issue (`Part of #1850`, `Closes #8903`,
  `Part of vaadin/web-components#1280`). #10182 links none, but it is a team member's. Plan on linking an
  existing feature-request issue, or filing one first.
- No API-review label or approval workflow is visible on the PRs: labels are only `target/*`,
  `cherry-picked-*` and `Released with …`. Review consists of 1–2 human approvals from the component team
  (`web-padawan`, `vursen`, `sissbruecker`, `tomivirkki`, `DiegoCardoso`), plus the automatic `vaadin-review-bot`,
  a `claude` check and Sonar comments.
- Review time: small team PRs land in 0–2 days (#10182, #10180, #9614). The user's own external-ish 2023 PRs
  took about 2 months (#5745 `MenuBar.close()`, 2023-11-21 → 2024-01-24, 11 review rounds).

## Tests (flow-components; `fc/CONVENTIONS.md` "Testing", `fc/guidelines/testing.md`)

- **Unit tests are mandatory for every getter and setter**, in `vaadin-<c>-flow/src/test/java/...`, using **JUnit 6
  (Jupiter)** with `Assertions.*` and Mockito. When a setter delegates to the Element API, **assert through the Element
  API** (for example `getElement().getProperty("inputMode")`, #9614). Use `MockUIExtension` when a UI is needed; do not
  roll your own setup. Test names describe the scenario and its outcome, never an `assert`/`expect` prefix. Cover
  `setXxx(null)` and the `Objects.requireNonNull` NPE (#10182).
- **Prefer unit tests over ITs** when the logic is pure server-side DOM/property/attribute mutation. Fire client
  events with `ComponentUtil.fireEvent`. When the server calls custom JS, unit-test the pending JS invocations (use
  `JsFunctionCallUtil` for `callJsFunction`) and add **one IT smoke test per distinct client operation**.
- **Integration tests** use JUnit 4 + TestBench on Jetty, in `vaadin-<c>-flow-integration-tests`: a `*Page` view under
  `src/main` with `@Route("vaadin-<c>/…")` and an `*IT extends AbstractComponentIT` with a matching `@TestPath`. Use
  component TestBench elements (`$(ButtonElement.class)`) and NativeButton for fixtures. Examples: #9915, #5745.
- Don't re-test mixin behaviour per component. Only assert that the interface is implemented.
- **flow** (`flow/CONVENTIONS.md` "Testing"): keep the test count minimal and assert concrete outputs. Static-import
  the assertions (`assertEquals`, not `Assertions.assertEquals`). Add an IT view under `flow-tests/test-root-context/`
  only for browser-facing features. Example: flow #25480 had a unit test plus a one-line IT tweak.

## Javadoc / API rules

- **fc: add `@since <major.minor>`** (from the root POM, currently `25.4`) to every new public member (#10182, #10180,
  #9614 all carry it). A workflow `update-since-tags.yml` reconciles it later against Maven Central anyway.
- **flow: "Do not add `@since` tags."** (`flow/CONVENTIONS.md` "Javadoc").
- fc Javadoc (`fc/CONVENTIONS.md` "JavaDoc"): address the reader as "you", state the default value, wrap literals in
  `{@code}`, and describe the contract, not the implementation.
- fc API (`fc/CONVENTIONS.md` "Public API", `fc/guidelines/api-design.md`): reuse the naming of sibling components,
  pair setters with getters, apply `Objects.requireNonNull` to object args, pair CSS-size String setters with a `(float, Unit)`
  overload, use positive-form booleans, no fluent/builder API, and make everything `Serializable`. Deprecations need
  `@Deprecated(since="25.4")` plus `@deprecated {@link replacement}`.
- flow API: find the precedent first and prefer `Signal<T>` accessors over listener API for observable state
  (`flow/CONVENTIONS.md` "Public API").

## Build, format, Java

- Java **21** (`maven.compiler.release` 21 in both root POMs; CI uses temurin 21), built with Maven.
- **Run `mvn spotless:apply` before every commit**. CI `format-check` fails the PR and posts a comment otherwise.
  Checkstyle also runs: `mvn checkstyle:check` (fc `CLAUDE.md`, `fc/.github/workflows/format-check.yml`,
  `flow/.github/workflows/formatter.yml`).
- fc, one component:
  - `mvn clean install -pl vaadin-<c>-flow-parent -DskipTests`
  - unit tests: `mvn test -pl vaadin-<c>-flow-parent/vaadin-<c>-flow [-Dtest='…']`
  - ITs: `mvn verify -am -pl vaadin-<c>-flow-parent/vaadin-<c>-flow-integration-tests [-Dit.test='FooIT#m*'] -DskipUnitTests`
    (needs port 8080 free and a browser)
  - manual: `mvn package jetty:run -Dvaadin.frontend.hotdeploy=true -am -B -q -DskipTests -pl …-integration-tests`
- flow: `mvn test -pl flow-server -Dtest=Foo`, ITs `mvn verify -pl flow-tests/test-root-context -Dit.test=FooIT`.
- web-components (JS, Yarn): `yarn test --group <component>`, plus visual tests `yarn test:lumo|aura`. Changes there are a
  separate PR that the fc PR `Depends on` (#9915 ↔ web-components#12451, #9889 ↔ web-components#12438).

## Prior art by the user (useful for categorizing)

The user has already upstreamed, in fc: `MenuBar.close()` (#5745), `AbstractLogin.showErrorMessage()` (#5749),
`ComboBox/MultiSelectComboBox.setOverlayWidth()` (#5743), `HasPlaceholder` on components (#5756),
`SubMenu.addSeparator()` (#5737) and `TabSheetElement.getTabLabels()` (#8108). Then #10269
`ContextMenuBase.getContextMenus(Component)` + `Grid.getContextMenus()` (merged 2026-09-29, 25.4). The rest of the user's PRs are in flow.
