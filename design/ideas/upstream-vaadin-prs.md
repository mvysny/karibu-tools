# Upstream karibu-tools utilities into Vaadin

Open PRs against Vaadin so the gaps karibu-tools fills get fixed at the source. Target: the
newest Vaadin master (25.4-SNAPSHOT as of 2026-09-29).

## Decided so far

- **Branches go straight into `vaadin/*`, no fork.** The owner has push access to `vaadin/flow`,
  `vaadin/flow-components`, `vaadin/web-components` and `vaadin/platform` (checked with
  `gh api repos/vaadin/<repo> --jq .permissions`). Local working clone for PRs:
  `~/work/vaadin/flow-components/`.
- **One utility function per PR**, even within a family (`findAncestor(predicate)`, `findAncestorOrSelf` and
  `isNestedIn` are three PRs). That keeps the scope small and easy to review, and lets upstream accept or
  reject each one on its own.
- **#10269 is the probe PR.** It has no AI attribution and carries a "Reviewed by Martin Vysny (@mvysny)" note instead.
  If it goes through, every further PR takes the same shape.
- **First PRs**: `Notification.getText()` (fc#2088), `Dialog.requestClose()` (fc#6027),
  `TabSheet.removeAll()`.
- **Tickets are filed from here, PRs are made elsewhere.** Every A candidate without a ticket gets a
  feature request filed from this repo; the PR itself is written in a separate session inside the
  upstream checkout (`flow`, `flow-components`, …), linking that ticket.
- **A-low waits** until the highest-value PRs are done.
- **Version precision is `x.y`.** API lands in minors, so the README says "Built into Vaadin x.y+".
- **README gets fixed first**: every public utility gets a bullet, every one that Vaadin has
  since absorbed says since which Vaadin and under which name, every "Vote for" link is rechecked.
- **Something upstream keeps off `Component` isn't dead.** It may still fit in
  `ComponentUtil`, `Element` or a mixin interface. For `findAncestor` / `isNestedIn`, though, the home is
  `Component` itself, since flow#14002 put `findAncestor(Class)` there on purpose. `walk()` is upstream as
  `ComponentUtil.streamDescendants()` (25.2).
- **Needing a web-components change is a tag, not a bucket.** Such a PR also needs a
  `vaadin/web-components` change (TypeScript, its own tests), so it costs more.
- **Obsolete / superseded utilities are not pursued** (bucket E).

## Buckets

- **A — PR candidate**: with where it would live upstream.
- **B — already upstream** on master (README says since which Vaadin).
- **C — Kotlin-only**: reified generics, `KProperty`, operators, extension-property sugar over an
  existing getter/setter.
- **D — upstream explicitly rejected**: linked to the decision.
- **E — obsolete / superseded**: don't pursue.

Tags: `web-component-side`, `theme-bound`.

## Upstream PR rules

The full rules, with sources, are in [`upstream-vaadin-prs/pr-rules.md`](upstream-vaadin-prs/pr-rules.md). The ones that shape every PR:

- **Branch** `feat/<component>-<thing>` in `vaadin/flow-components`, targeting `main`. It's squash-merged, so the
  PR title and description become the commit. Backports go through a `target/25.3` label.
- **Title**: Conventional Commits, lowercase imperative, e.g. `feat: add setSelectionRange to TextField`.
- **Issue first.** Link an existing feature request (`Part of #` / `Closes #`), or file one; the
  template expects the feature to have been discussed there.
- **Tests**: a unit test for every getter/setter (JUnit Jupiter, `vaadin-<c>-flow/src/test`,
  asserting through the Element API, `MockUIExtension`); an IT (TestBench, `*Page` + `*IT` in
  `vaadin-<c>-flow-integration-tests`) only as one smoke test per client-side JS operation.
- **Javadoc**: `@since 25.4` on every new public member (flow-components; flow forbids `@since`),
  addressed to "you", stating the default.
- **API shape**: sibling components' naming, getter paired with every setter,
  `Objects.requireNonNull`, no fluent API, `Serializable`.
- **Before push**: `mvn spotless:apply` (CI fails otherwise); Java 21;
  `mvn test -pl vaadin-<c>-flow-parent/vaadin-<c>-flow`.
- **web-component-side** work is a separate `vaadin/web-components` PR (Yarn, `yarn test --group <c>`) that the
  flow-components PR references with `Depends on`.
- **No AI attribution** in upstream commits or PRs (flow's written rule, and the owner's #10269 follows it);
  #10269 adds a "Reviewed by Martin Vysny" note instead.

## Categorization

The per-symbol evidence (first version, how it was established, tickets) is in the sidecar tables
[`audit-core.md`](upstream-vaadin-prs/audit-core.md), [`audit-grid.md`](upstream-vaadin-prs/audit-grid.md)
and [`audit-fields.md`](upstream-vaadin-prs/audit-fields.md). They were researched 2026-09-29 against
flow / flow-components `main`.

### A: PR candidates

flow (`vaadin/flow`):

| Utility | Upstream shape | Ticket | Tags |
|---|---|---|---|
| `findAncestor(predicate)`, `findAncestorOrSelf`, `isNestedIn` | on `Component`; flow#14002 moved `findAncestor(Class)` *off* `ComponentUtil` on purpose ("ComponentUtil feels internal") | none | |
| `Element.textRecursively2` | bug fix of `Element.getTextRecursively()` | flow#3668 open | |
| `getRouteUrl(…, QueryParameters)` | `RouteConfiguration.getUrl(Class, RouteParameters, QueryParameters)` | none | |
| `RouterLink.target` (+ `setOpenInNewTab`) | `RouterLink.setTarget(AnchorTargetValue)` | flow#5791 open | |
| `TextField.onEnter` | fix the stale value a shortcut listener sees, not add `onEnter` | flow#7046 open | |
| `ExtendedClientDetails.timeZone` (+ `currentDateTime`) | `ExtendedClientDetails.getZoneId()`, falling back to the offset | none | |

flow-components (`vaadin/flow-components`):

| Utility | Upstream shape | Ticket | Tags |
|---|---|---|---|
| `Notification.getText()` | `getText()` reading the `text` property; trivial | fc#2088 open (owner's) | |
| `Dialog.requestClose()` | `Dialog.requestClose()` firing `DialogCloseActionEvent` | fc#6027 open (owner's) | |
| `TabSheet.removeAll()` | same, matching `Tabs.removeAll()` | fc#10272 open (owner's) | |
| `TabSheet.findTabContaining()` | same | fc#10273 open (owner's) | |
| `FormItem.label` | a label getter on `FormItem` | fc#1015 open | |
| text selection: `selectAll`, `selectNone`, `setCursorLocation`, `select(IntRange)` | `setSelectionRange(int, int)`, `setCursorPosition(int)`, `selectAll()` on `TextFieldBase` | fc#1377, fc#1152, wc#1375 open | maybe web-component-side |
| `Grid.getColumnBySortProperty` (+ `getColumnBySortOrder`, `sort(QuerySortOrder...)`) | next to `getColumnByKey` | none | |
| `TreeGrid.expandAll` (+ `getRootItems`) | needs an API discussion first: unbounded loading (fc#4411) | none | |
| `Dialog.center()` | re-center after a size change | wc#601 open | web-component-side |
| `Notification.addCloseButton()` | a built-in close button | wc#438, fc#5531 open | web-component-side, theme-bound |
| `MenuBar/SubMenu.addIconItem()` | an icon slot on items | fc#2688, wc#1118 open | web-component-side, theme-bound (hard-codes Lumo vars) |

**A-low**: small, weak case, likely "just write the one-liner" pushback:
`HasOrderedComponents/Element.insertBefore`, `hasChildren`, `setOrRemoveAttribute(IfNullOrEmpty)`,
`ClassList.toggle`, `AfterNavigationEvent.routeClass`, `QueryParameters.isEmpty`, `Validator.isValid`,
`HtmlSpan` (issue first: upstream is XSS-wary), `getAllDialogs`, public `AbstractLogin.getI18n()` (check fc#9923
first), `TabSheet.getTabs()`, public `BasicRenderer.getValueProvider()` / `getTemplateExpression()` (motivation is
testing only).

### B: already upstream

The README records each one, with its version and upstream name ("Built into Vaadin x.y+").
Ticket housekeeping upstream: fc#1022 (SelectionMode getter) is still open though `Grid.getSelectionMode()`
shipped in 24.4; comment to close it.

### C: Kotlin-only

The reified / `KClass` `navigateTo` and `RouterLink.setRoute`, `Router.configuration`, the shortcut operator DSL
(`Alt + Ctrl + KEY_C`, `Key.shortcut`), `KProperty`-based `addColumnFor` / `getColumnBy` / `getCell` /
`addHierarchyColumnFor`, `Person::name.asc` / `.comparator`, `Column.asc`/`desc`, vararg `Grid.sort` /
`setSortOrder`, the Optional / instanceof / isEmpty property sugar over the Grid selection, `Tab.owner`,
`StateNode.element`, `Anchor.target_` / `setOpenInNewTab`.

### D: upstream explicitly rejected

- `Component.fireEvent()`: protected by design; `ComponentUtil.fireEvent()` is the public route.
- `Upload.buttonCaption`: fc#3542, web-padawan: "We don't have `setCaption()` in any of the Flow components".

### E: obsolete / superseded, not pursued

- **Superseded upstream**:
  - `tooltip` (`title` attribute) → `HasTooltip`, 23.3
  - `Button.setPrimary()` / `setDanger()` → theme-neutral `ButtonVariant.PRIMARY` / `ERROR`, 25.1
  - `Badge` → the `vaadin-badge-flow` component, 25.1. karibu's Lumo span doesn't render under Aura; wc#12718 not planned.
  - `FormLayout.addRowBreak()` → `addFormRow()`, 24.8; fc#7682 closed as obsolete
  - StreamResource helpers → `DownloadHandler`; `StreamResource` is deprecated for removal in 24.8
  - `JsonValue.isNull` → elemental.json is gone from flow `main`
  - `BrowserTimeZone` session cache → `ExtendedClientDetails` is auto-collected in 25.0
  - `HeaderCell/FooterCell.renderer` → Vaadin ≤ 23 only
- **Test aids / one-liners**: `serverClick`, `RouterLink.navigateTo`, `addContextMenuListener` (one line
  with `preventDefault()` since 24.2), `fetchAll`, `ItemClickEvent.isDoubleClick`, `Column._internalId`,
  `Tab.ownerTabSheet`.
- **Generic, not a Vaadin gap**: `SemanticVersion`, `DepthFirstTreeIterator`, `MimeType`, the bean-getter
  comparators, jsoup `Node.textRecursively`, `jacksonReadToObject`, `IconName`.
- **Wrong shape for upstream**: `LabelWrapper` (a `CustomField<Void>` hack), the `caption` family (deprecated),
  the `label` fallback for non-`HasLabel` components, `addThemeVariantsCompat` (a 14-vs-24 shim).

### Answered: `Q_theme_specific`

Aura ships `primary`, `error`/`danger`, `small`, `align-*` and `helper-above-field`. Vaadin 25.1 added
theme-neutral constants (`ButtonVariant.PRIMARY`, `ComboBoxVariant.SMALL`, …), with fc#8126 and fc#8651 as the
upstream track. No ticket asks for a `setPrimary()` shorthand, and a boolean-ish setter would cut across the variant API.
The theme helpers therefore land in B or E, never A.

## Open questions

- `Q_a_low`: pursue any A-low at all, or drop them to E? Deferred until the first PRs are through.
- `Q_karibu_followups`: not upstream work, but the audit found some karibu fixes:
  - `setClassNames2` calls `style.clear()` (inline styles) instead of clearing the class names, and its test
    (`containsAll`) can't catch that.
  - `Badge` and `setPrimary()` / `setDanger()` could switch to the theme-neutral variants on 25.1+.
  - A `DownloadHandler` flavour of the StreamResource helpers.
