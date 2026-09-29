# Audit: GridUtils.kt, Renderers.kt, MenuBarUtils.kt, TabSheets.kt vs upstream master (25.4-SNAPSHOT)

Evidence keys: `javap` = bisected with javap over Maven Central jars (versions probed: 14.11, 18.0, 22.0, 23.0/23.1/23.2/23.3, 24.0/24.1/24.3/24.4/24.5, 25.0, 25.3; master = shallow clone grep);
`@since` = upstream javadoc tag (only where it agrees with javap); `baseline` = present in the Vaadin 14.11 jar.
Paths: G = karibu-tools/src/main/kotlin/GridUtils.kt, R = Renderers.kt, M = MenuBarUtils.kt, T = karibu-tools-23/src/main/kotlin/TabSheets.kt.

| # | Symbol | Where | README? | Upstream equivalent (master) | First Vaadin | Evidence | Upstream tickets | Bucket | Tags | Note |
|---|---|---|---|---|---|---|---|---|---|---|
| 1 | `Grid.refresh()` | G:23 | yes | `grid.getDataProvider().refreshAll()`; `grid.getGenericDataView().refreshAll()` | 14.0 (baseline); DataView ≤18.0 | javap | – | B | | One-liner over an existing call. Data views came in 17 per docs; jars confirm ≤18.0 (the 17.0.0 grid artifact has a different version number) |
| 2 | `Grid.refreshItem(item)` | G:30 | **no** | `getDataProvider().refreshItem(item)`; `getGenericDataView().refreshItem(item)` | 14.0 (baseline) | javap | – | B | | |
| 3 | `Grid.isMultiSelect` | G:36 | yes | `grid.getSelectionMode() == MULTI` | 24.4 | javap+@since | flow-components#1022 "provide a SelectionMode getter": **still open** though it shipped in 24.4 | B | | #1022 looks stale; worth a comment asking to close it |
| 4 | `Grid.isSingleSelect` | G:41 | yes | `grid.getSelectionMode() == SINGLE` | 24.4 | javap | as #3 | B | | |
| 5 | `SelectionModel.isMultiSelect` | G:46 | **no** | `model instanceof SelectionModel.Multi` | 14.0 (baseline) | javap | – | C | | instanceof sugar |
| 6 | `SelectionModel.isSingleSelect` | G:50 | **no** | `model instanceof SelectionModel.Single` | 14.0 (baseline) | javap | – | C | | |
| 7 | `SelectionModel.isSelectionAllowed` | G:55 | **no** | none (instanceof Single \|\| Multi) | – | grep | – | C | | |
| 8 | `Grid.selectionMode` (var) | G:60 | yes | `Grid.getSelectionMode()` / `setSelectionMode()` | 24.4 (getter; setter baseline) | javap+@since | #1022 (open, stale) | B | | |
| 9 | `Grid.isSelectionAllowed` | G:73 | yes | `grid.getSelectionMode() != NONE` | 24.4 | javap | – | B | | |
| 10 | `GridSelectionModel.isEmpty` | G:78 | **no** | `getSelectedItems().isEmpty()` / `getFirstSelectedItem().isEmpty()` | 14.0 (baseline) | javap | – | C | | property sugar |
| 11 | `Grid.isSelectionEmpty` | G:83 | yes | `grid.getSelectedItems().isEmpty()` | 14.0 (baseline) | javap | – | C | | |
| 12 | `SelectionEvent.isSelectionEmpty` | G:88 | **no** | `event.getAllSelectedItems().isEmpty()` | 14.0 (baseline) | javap | – | C | | |
| 13 | `Grid.selectedItemOrNull` | G:94 | **no** | `grid.getSelectionModel().getFirstSelectedItem().orElse(null)`; single: `asSingleSelect().getValue()` | 14.0 (baseline) | javap | – | C | | Optional→nullable is Kotlin idiom |
| 14 | `Grid.selectedItem` | G:99 | **no** | `getFirstSelectedItem().get()` | 14.0 (baseline) | javap | – | C | | |
| 15 | `Grid.addColumnFor(KProperty1, sortable, key, converter)` | G:118 | yes | none | – | – | – | C | | KProperty |
| 16 | `Grid.addColumnFor(KProperty1, Renderer, …)` | G:141 | yes | none | – | – | – | C | | KProperty |
| 17 | `Grid.getColumnBy(KProperty1)` | G:157 | yes | `getColumnByKey(String)` | 14.0 (baseline) | javap | – | C | | KProperty |
| 18 | `Grid.addColumnFor<T,V>(propertyName, …)` (reified) | G:181 | yes | `new Grid<>(Bean.class)` + `grid.addColumn("name")` (key, header, sort set automatically) | 14.0 (baseline) | javap | – | B | | Upstream needs a bean-typed Grid; the karibu variant works on any Grid via reified T. Reified = Kotlin-only too |
| 19 | `Grid.addColumnFor(propertyName, Renderer, …)` | G:210 | yes | `addColumn(renderer, sortProperties…)` + `setKey`/`setHeader` | 14.0 (baseline) | javap | – | B | | Only header/key defaults are extra |
| 20 | `HeaderRow.getCell(KProperty1)` | G:251 | yes | `HeaderRow.getCell(Column)` | 14.0 (baseline) | grep | – | C | | KProperty |
| 21 | `FooterRow.getCell(KProperty1)` | G:275 | yes | `FooterRow.getCell(Column)` | 14.0 (baseline) | grep | – | C | | KProperty |
| 22 | `HeaderCell.renderer` | G:290 | **no** | none; Vaadin 24+ cells have no Renderer (throws on 24+) | – | code | – | E | | Only for Vaadin ≤23. Delete or deprecate |
| 23 | `FooterCell.renderer` | G:306 | **no** | none (as #22) | – | code | – | E | | |
| 24 | `FooterCell.component` (var) | G:318 | yes | `FooterCell.getComponent()` / `setComponent()` | 23.2 (getter; setter baseline) | javap | – | B | | |
| 25 | `HeaderCell.component` (var) | G:349 | yes | `HeaderCell.getComponent()` / `setComponent()` | 23.2 (getter) | javap | – | B | | |
| 26 | `Grid.sort(vararg GridSortOrder)` | G:389 | yes | `grid.sort(List)` + `GridSortOrder.asc(col).thenDesc(col2).build()` | 14.0 (baseline) | javap | – | C | | vararg sugar; Java already has the builder |
| 27 | `Grid.setSortOrder(List)` | G:396 | **no** | `grid.sort(List)` | 14.0 (baseline) | javap | – | C | | Only exists so Kotlin gets a `sortOrder` property |
| 28 | `Grid.sort(vararg QuerySortOrder)` | G:417 | yes | none | – | grep | none found | A | | `Grid.sort(QuerySortOrder...)`: sorts by sort-property name (for example restored from a URL). Low priority; depends on #30 |
| 29 | `Grid.getColumnBySortOrder(QuerySortOrder)` | G:434 | **no** | none | – | grep | – | A | | Fold into the #30 PR; it is just `getColumnBySortProperty(order.getSorted())` |
| 30 | `Grid.getColumnBySortProperty(String)` | G:442 | **no** | none | – | grep | none found (#1513 is unrelated) | A | | `Grid.getColumnBySortProperty(String)` next to `getColumnByKey`. Low–medium value |
| 31 | `Column.asc` | G:455 | partially (in the sort example) | `GridSortOrder.asc(column)` | 14.0 (baseline) | javap | – | C | | property sugar |
| 32 | `Column.desc` | G:459 | partially | `GridSortOrder.desc(column)` | 14.0 (baseline) | javap | – | C | | |
| 33 | `TreeGrid.getRootItems()` | G:464 | yes | `getTreeData().getRootItems()`, only for a TreeDataProvider; generic: `getDataProvider().fetch(new HierarchicalQuery<>(null,null))` | 14.0 (baseline, partial) | javap | none found | A (fold into #34) | | Not worth a PR of its own |
| 34 | `TreeGrid.expandAll(depth=100)` | G:471 | yes | `expandRecursively(rootItems, depth)` | 14.0 (baseline, partial) | javap | none for expandAll; related: flow-components#4411 (closed; freeze when expanding everything), #9148 (closed) | A | | `TreeGrid.expandAll()`. Upstream may object: unbounded loading with lazy providers (#4411). Needs an API-design discussion first |
| 35 | `Column.header2` (var) | G:492 | yes | `Column.getHeaderText()` (+ `HeaderCell.getText()`) | 23.2 | javap | flow-components#1496 "Add Column.getHeader()" (filed by mvysny): **closed/completed** 2022-08-10 | B | | karibu also falls back to a single-child ColumnGroup's header; upstream doesn't. README should say "Built into 23.2+ as getHeaderText()" |
| 36 | `Column._internalId` | G:529 | **no** | none (`getInternalId()` is package-private) | – | grep | – | E | | Internal/debug use only |
| 37 | `ItemClickEvent.isDoubleClick` | G:532 | yes | `getClickCount() >= 2` (inherited from ClickEvent); `addItemDoubleClickListener` | 14.0 (baseline) | javap | – | E | | sugar; upstream already has a double-click listener |
| 38 | `TreeGrid.addHierarchyColumnFor(KProperty1, …)` | G:552 | yes | none | – | – | – | C | | KProperty |
| 39 | `TreeGrid.addHierarchyColumnFor<T,V>(propertyName, …)` (reified) | G:584 | yes | `new TreeGrid<>(Bean.class)` + `setHierarchyColumn("name")` / `addHierarchyColumn(ValueProvider)` | 14.0 (baseline) | grep | – | B | | as #18 |
| 40 | `BasicRenderer.valueProvider` | R:18 | yes | `BasicRenderer.getValueProvider()`: **protected** | 23.0 (protected; moved to flow-components by refactor #2009) | git log | none found | A (low) | | Make it public. Motivation is testing (Karibu-Testing). Likely pushback |
| 41 | `Renderer.template` | R:42 | **no** | `LitRenderer.getTemplateExpression()` / `BasicRenderer.getTemplateExpression()`: **protected** | 24.0 (protected) | javap+@since | none found | A (low) | | Make it public; same motivation as #40. The PolymerRenderer branch is obsolete (returns "" on 24+) |
| 42 | `MenuBar.close()` | M:13 | yes ("Vote for #5742") | `MenuBar.close()` | 24.4 | javap+@since | flow-components#5742: **closed/completed** 2024-01-24; web-components#728 (closed/completed); code comment's vaadin-menu-bar#102 now redirects to web-components#728 | B | | README: replace the "Vote for" link with "Built into 24.4+" |
| 43 | `MenuBar.addIconItem(icon, label, ariaLabel)` | M:23 | yes ("Vote for #2688") | none; only `addItem(Component)` + `MenuBarVariant.LUMO_ICON` | – | grep | flow-components#2688: **open** (convenience APIs for icons in list items); web-components#1118: **open** (menu-bar icon support) | A | theme-bound, web-component-side | Sets the Lumo `icon` theme. Upstream would want a web-component-side icon slot (#1118) rather than Flow-side styling |
| 44 | `SubMenu.addIconItem(…)` | M:46 | yes | none | – | grep | #2688, web-components#1118 (both open) | A | theme-bound, web-component-side | Hard-codes `--lumo-icon-size-s` / `--lumo-space-s`, so it breaks on Aura. Same PR/discussion as #43 |
| 45 | `Tab.index` | T:15 | yes | `TabSheet.getIndexOf(Tab)`; `Tabs.indexOf(Tab)` | 23.3 (TabSheet) / 24.0 (`Tabs.indexOf(Tab)`); 25.3 adds `Tabs.getIndexOf(Tab)` | javap+@since | – | B | | The Tab-side property is Kotlin sugar |
| 46 | `Tab.owner` | T:24 | yes | `tab.getParent()` cast to Tabs | 14.0 (baseline) | grep | – | C | | Cast sugar; relies on TabSheet internals |
| 47 | `Tab.ownerTabSheet` | T:30 | yes | none | – | grep | none found | E | | Low value; if anything, covered by a `ComponentUtil.findAncestor` PR (Q_bucket_d_alt_home) |
| 48 | `Tab.contents` | T:54 | yes | `TabSheet.getComponent(Tab)` | 24.1 | javap+@since | – | B | | The Tab-side accessor is sugar |
| 49 | `TabSheet.tabCount` | T:59 | yes | `TabSheet.getTabCount()` | 24.5 | javap+@since | – | B | | |
| 50 | `TabSheet.removeAll()` | T:64 | yes | none (`Tabs.removeAll()` exists) | – | javap | none found | A | | `TabSheet.removeAll()`, matching `Tabs.removeAll()`. Small, clean PR |
| 51 | `TabSheet.tabs` (live List) | T:71 | yes | none; `getTabAt(i)` + `getTabCount()` | – | grep | #5232 (open, internal tabs customization; tangential) | A (low) | | Maybe `TabSheet.getTabs(): List<Tab>`. Upstream may prefer not to expose it |
| 52 | `TabSheet.getTab(Component)` | T:82 | yes | `TabSheet.getTab(Component)` | 24.1 | javap+@since | – | B | | A port for 23 |
| 53 | `TabSheet.getComponent(Tab)` | T:88 | yes | `TabSheet.getComponent(Tab)` | 24.1 | javap+@since | – | B | | A port for 23 |
| 54 | `TabSheet.findTabContaining(Component)` | T:93 | yes | none | – | grep | none found | A | | For example, select the tab that holds a field with a validation error. Good PR candidate |

## Summary
- **A (PR candidates):** #50 TabSheet.removeAll, #54 findTabContaining, #30 (+#29) getColumnBySortProperty, #28 sort(QuerySortOrder...), #34 (+#33) TreeGrid.expandAll, #51 TabSheet.getTabs, #40/#41 public renderer getters (low), #43/#44 MenuBar icon items (theme-bound, web-component-side; tickets open).
- **B (upstream):** refresh/refreshItem (baseline), selection-mode family (24.4), header/footer component (23.2), header2 → getHeaderText (23.2), MenuBar.close (24.4), TabSheet getTab/getComponent (24.1), tabCount (24.5), Tab.index (23.3/24.0), string-property addColumnFor/addHierarchyColumnFor (baseline, bean-typed grid).
- **C (Kotlin-only):** KProperty column APIs, Optional/instanceof/isEmpty property sugar, vararg sort, asc/desc, setSortOrder, Tab.owner.
- **E (obsolete):** Header/FooterCell.renderer (≤23), `_internalId`, isDoubleClick, Tab.ownerTabSheet.
- **README gaps (in scope):** refreshItem, SelectionModel.isMultiSelect/isSingleSelect/isSelectionAllowed, GridSelectionModel.isEmpty, SelectionEvent.isSelectionEmpty, selectedItemOrNull, selectedItem, Header/FooterCell.renderer, setSortOrder, getColumnBySortOrder, getColumnBySortProperty, Column._internalId, Renderer.template; Column.asc/desc only shown in an example.
- **README "Vote for" to update:** MenuBar #5742 → shipped in 24.4; #2688 still open. Stale ticket: flow-components#1022 (SelectionMode getter) is still open though `getSelectionMode()` shipped in 24.4.
