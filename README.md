# Karibu-Tools: The Vaadin Missing Utilities

[![GitHub tag](https://img.shields.io/github/tag/mvysny/karibu-tools.svg)](https://github.com/mvysny/karibu-tools/tags)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mvysny.karibu-tools/karibu-tools/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mvysny.karibu-tools/karibu-tools)
[![CI](https://github.com/mvysny/karibu-tools/actions/workflows/gradle.yml/badge.svg)](https://github.com/mvysny/karibu-tools/actions/workflows/gradle.yml)

Utility functions missing from Vaadin 14+, for your [Kotlin](https://kotlinlang.org/)-based projects.

The jar is in Maven Central, so it's easy to add this library to your project.

Gradle:
```groovy
repositories {
  mavenCentral()
}
dependencies {
  api("com.github.mvysny.karibu-tools:karibu-tools:x.y")
}
```

> Note: for Vaadin 23+, depend on `karibu-tools-23` instead, to bring some additional utilities.

See the tag above for the latest version.

"Built into Vaadin x.y+" marks a utility Vaadin has since absorbed; on that version and
newer, prefer the built-in API it names. "Deprecated" marks one Vaadin 14 already has built in,
so the built-in works on every Vaadin this library supports. "Vote for" links an upstream ticket that is still open.

## General Vaadin Utilities

A set of general Vaadin utilities applicable to all components.

### Obtaining Vaadin version at runtime

* Retrieve the `VaadinVersion.get` property to get the Vaadin version such as 14.7.0.
  Built into Vaadin 23.0+ as `Platform.getVaadinVersion()`.
  * `VaadinVersion.vaadin` returns the same, or `null` when it can't be detected.
* call `VaadinVersion.flow` to obtain the Vaadin Flow `flow-server.jar` version: for example 2.6.7 for Vaadin 14.6.8.
* `VaadinVersion.hilla` returns the Hilla version, or `null` if Hilla isn't on the classpath.
  Built into Vaadin 24.2+ as `Platform.getHillaVersion()`.
* `SemanticVersion` is the version type all of the above return: comparable, with `isAtLeast()`/`isAtMost()`/`isExactly()`
  and `SemanticVersion.fromString()`.

### Events

* Call `component.fireEvent()` to fire any event on the component (a shortcut to `ComponentUtil.fireEvent()`).
* Call `ClickNotifier.serverClick()` to notify all click listeners (to fire a `ClickEvent`).
* Call `DomListenerRegistration.preventDefault()` to call `event.preventDefault()` in the browser.
  Built into Vaadin 24.2+ as `DomListenerRegistration.preventDefault()`.

### Component hierarchy

* Call `Component.findAncestor()` or `Component.findAncestorOrSelf()` to discover component's
  ancestor which satisfies given predicate. Vaadin 23.2+ has `Component.findAncestor(Class)`, by type only.
  `findAncestor()` is built into Vaadin 25.4+ as `Component.findAncestor(SerializablePredicate)`.
* call `Component.removeFromParent()` to remove the component from its parent.
  Built into Vaadin 24.0+ as `Component.removeFromParent()`.
* call `Component.isNestedIn(potentialAncestor: Component)` to discover whether a component
  is nested within given potential ancestor.
* query `Component.isAttached()` to see whether this component is currently attached to an UI.
  Deprecated: built into Vaadin 14.7+ / 18.0+ as `Component.isAttached()`.
* call `HasOrderedComponents<*>.insertBefore()` to insert a component before given component.
* query `HasComponents.hasChildren` to see whether a component has any children.
* `Component.walk()` will return an `Iterable<Component>` which walks the component child tree,
  depth-first: first the component, then its descendants, then its next sibling.
  Built into Vaadin 25.2+ as `ComponentUtil.streamDescendants()`, which leaves out the component itself
  and includes virtual children.
* `DepthFirstTreeIterator` is the generic depth-first iterator behind `walk()`, for any tree.

### Misc Component

* get/set `component.textAlign` to read/write the `text-align` CSS property.
  Built into Vaadin 24.1+ as `Style.setTextAlign()`.
* get/set `component.tooltip` to read/write the hovering tooltip (the `title` attribute).
  Vaadin 23.3+ has a real tooltip: `HasTooltip.setTooltipText()`.
* call `Component.addContextMenuListener()` to add the right-click context listener to a component.
  Also causes the right-click browser menu not to be shown on this component.
* query `UI.currentViewLocation` to return the location of the currently shown view.
  Built into Vaadin 24.3+ as `UI.getActiveViewLocation()`.
* call `div.addClassNames2("  foo bar   baz")` to add multiple class names.
  * also `div.removeClassNames2()` and `div.setClassNames2()`
  * Deprecated: built into Vaadin 14.8+ / 22.0+: `addClassNames()` and `removeClassNames()` split on spaces
    (but not on tabs or newlines, and throw on a blank string); `setClassName()` splits on any whitespace.
* `component.placeholder` unifies the various component placeholders, usually shown when there's no value selected.
  Built into Vaadin 24.3+ as `HasPlaceholder`.
* `component.caption` unifies component captions. Caption is displayed directly on the component (e.g. the Button text),
  while label is displayed next to the component in a layout (e.g. form layout).
  Deprecated, to be removed; `Button.caption` likewise, use `Button.text`.
* `component.label` unifies component labels, and also works for components which only have the `label` property.
  Uses `HasLabel` (Vaadin 14.8+ / 17.0+) where the component implements it.
* `FormItem.label` returns the label text set via `FormLayout.addFormItem()`.
  Vote for [flow-components #1015](https://github.com/vaadin/flow-components/issues/1015).
* Use `LabelWrapper` if you need to add a label on top of a component which doesn't support labels,
   and you can't nest the component in a `FormLayout`.
* `component.ariaLabel` gets/sets the `aria-label` attribute. Since 0.17.
  Built into Vaadin 22.0+ as `HasAriaLabel`.

### Misc Element

* call `ClassList.toggle` to set or remove given CSS class.
* call `Element.setOrRemoveAttribute` to set an attribute to given value, or remove the
  attribute if the value is null.
  * `Element.setOrRemoveAttributeIfNullOrEmpty` also removes the attribute if the value is empty.
* `Element.insertBefore()` to insert a child element before another child. A counterpart for JavaScript DOM
  `Node.insertBefore()`.
* `Element.textRecursively2` returns all the text recursively present in the element. Vote for
  [flow #3668](https://github.com/vaadin/flow/issues/3668).
* `Element.getVirtualChildren()` Returns all virtual child elements added via `Element.appendVirtualChild`.
  Vaadin 25.2+ has `ComponentUtil.getAllChildren()`, which includes the virtual children, at the component level.
* `StateNode.element` returns `Element` for that `StateNode`.
* `Element.getChildrenInSlot("prefix")` will return all child elements nested in the `prefix` slot.
  Built into Vaadin 23.1+ as `SlotUtils.getElementsInSlot()`.
* `Element.clearSlot("prefix")` will remove all child elements nested in the `prefix` slot.
  Built into Vaadin 23.1+ as `SlotUtils.clearSlot()`.

### Router

Navigating:

* Call `navigateTo<AdminRoute>()` or `navigateTo(AdminRoute::class)` or `navigateTo(DocumentRoute::class, 25L)`.
* Call `navigateTo(String)` to navigate anywhere within the app. Supports query parameters as well:
   * `""` (empty string) - the root view.
   * `foo/bar` - navigates to a view
   * `foo/25` - navigates to a view with parameters
   * `foo/25?token=bar` - any view with parameters and query parameters
   * `?token=foo` - the root view with query parameters
   * Built into Vaadin 25.3+: `UI.navigate(String)` parses the query parameters.
* Call `navigateTo(getRouteUrl(AdminRoute::class, "lang=en"))` to navigate to `admin?lang=en`.
  * `getRouteUrl(AdminRoute::class, RouteParameters, QueryParameters)` takes typed route and query parameters.
    Vote for [flow #26033](https://github.com/vaadin/flow/issues/26033).
* Call `routerLink.navigateTo()` to navigate to the link's target.
* `RouterLink.setRoute(AdminRoute::class)` or `RouterLink.setRoute(DocumentRoute::class, 25L)` sets the link's target.
* `Router.configuration` returns the `RouteConfiguration` for the router's registry.
* To obtain the route class from `AfterNavigationEvent`, query `event.routeClass`
* To obtain the route class from a `Location`, call `location.getRouteClass()`

QueryParameters:

* call `queryParameters["foo"]` to obtain the value of the `?foo=bar` query parameter.
  Built into Vaadin 24.2+ as `QueryParameters.getSingleParameter()`.
* call `queryParameters.getValues("foo")` to get all values of the `foo` query parameter.
  Built into Vaadin 24.2+ as `QueryParameters.getParameters(String)`.
* call the `QueryParameters("foo=bar")` factory method to parse the query part of the URL
  to the Vaadin `QueryParameters` class. Deprecated: built into Vaadin 14.7+ / 20.0+ as `QueryParameters.fromString()`,
  which doesn't strip a leading `?` and doesn't return an empty `QueryParameters` for a blank string.
* `queryParameters.isEmpty` / `isNotEmpty` checks whether there are any query parameters.

Links:

* `RouterLink.target` allows you to set `AnchorTargetValue`, e.g. `AnchorTargetValue.BLANK`. Since 0.16.
  Vote for [flow #5791](https://github.com/vaadin/flow/issues/5791).
* `RouterLink.setOpenInNewTab()` opens the router link in new browser tab/window (sets target to `AnchorTargetValue.BLANK`). Since 0.16
* `Anchor.target_` clears up the Anchor mess by adding yet another property :-D Since 0.16
* `Anchor.setOpenInNewTab()` opens the anchor in new browser tab/window (sets target to `AnchorTargetValue.BLANK`). Since 0.16

### Time Zone

In order to properly display `LocalDate` and `LocalDateTime` on client's machine, you need
to fetch the browser's TimeZone first. You can achieve that simply by calling `BrowserTimeZone.fetch()`,
for example when the [Vaadin UI is being initialized](https://vaadin.com/docs/v14/flow/advanced/tutorial-application-lifecycle.html).
`fetch()` will request the information from
the browser and will store it into the session. Afterwards, simply
call `BrowserTimeZone.get` to get the browser's time zone instantly.
Vaadin 25.0+ collects `ExtendedClientDetails` during UI init, so `UI.getPage().getExtendedClientDetails()`
is available without the fetch.

* Call `BrowserTimeZone.currentDateTime` to get the current date time at browser's current time zone.
* Call `BrowserTimeZone.toLocalDateTime(instant)` to convert an `Instant` to the browser's local date time.
* `BrowserTimeZone.extendedClientDetails` returns the details `fetch()` stored in the session.
* `ExtendedClientDetails.timeZone` returns the browser's `ZoneId`, falling back to the time zone offset.
  Vote for [flow #26034](https://github.com/vaadin/flow/issues/26034).
* `ExtendedClientDetails.currentDateTime` returns the current date time in that time zone.

### Text selection utils

The following functions are applicable to any field that edits text, e.g.
`TextField`, `TextArea`, `EmailField`:

* Call `field.selectAll()` to select all text within the field.
* Call `field.selectNone()` to select no text
* Call `field.setCursorLocation()` to place the cursor at given character.
  Vote for [flow-components #1152](https://github.com/vaadin/flow-components/issues/1152).
* Call `field.select(range)` to select a range within the text.
  Vote for [flow-components #1377](https://github.com/vaadin/flow-components/issues/1377).

### DataProviders

* Use `dataProvider.fetchAll()` to fetch all items provided by this data provider as an eager list. Careful with larger data!
* Use `Person::name.asc`/`Person::name.desc` to create a `QuerySortOrder` which is useful with DataProvider's `Query.sortOrders`
  or `Grid.sort()`.
* `Person::name.comparator` returns a `Comparator<Person>` comparing by the property;
  `Person::class.java.getPropertyComparator("name")` does the same by property name, and
  `Class.getGetter("name")` returns the property's getter.

### Grid

* Use `Grid.refresh()` to call `DataProvider.refreshAll()`, `Grid.refreshItem()` to call `DataProvider.refreshItem()`.
* Selection:
  * Check `Grid.isMultiSelect` to see whether a grid is configured as multi-select.
  * Check `Grid.isSingleSelect` to see whether a grid is configured as single-select.
  * `Grid.isSelectionEmpty` returns true if there's nothing selected in the grid.
  * `Grid.selectionMode` allows you to read/write the current selection mode.
  * `Grid.isSelectionAllowed` returns false if `Grid.SelectionMode.NONE` is currently set.
  * Built into Vaadin 24.4+ as `Grid.getSelectionMode()`.
  * `Grid.selectedItem` returns the selected item (fails if there's none); `Grid.selectedItemOrNull` returns it or `null`.
  * The same checks on the selection model itself: `SelectionModel.isMultiSelect`/`isSingleSelect`/`isSelectionAllowed`,
    `GridSelectionModel.isEmpty`, and `SelectionEvent.isSelectionEmpty` on the selection event.
* Multitude overloaded `Grid.addColumnFor()` which allow you to create a column
  using given converter or renderer to format a value. Allows both for passing in `KProperty`
  or a property by name. For example, `grid.addColumnFor(Person::name)` will create a
  sortable column displaying a name of a person, setting the column header to "Name".
* Multitude overloaded `TreeGrid.addHierarchyColumnFor()` which allow you to create a column
  using given converter or renderer to format a value. Allows both for passing in `KProperty`
  or a property by name. For example, `grid.addHierarchyColumnFor(Person::name)` will create a
  sortable column displaying a name of a person, setting the column header to "Name".
* `Grid.getColumnBy(Person::name)` retrieves a column created via `grid.addColumnFor(Person::name)`.
* `Grid.getColumnBySortProperty("name")` retrieves the column sorted by given property;
  `Grid.getColumnBySortOrder()` does the same for a `QuerySortOrder`.
  Vote for [flow-components #10276](https://github.com/vaadin/flow-components/issues/10276).
* `HeaderRow.getCell(Person::name)` retrieves header cell for given column.
* Similarly, `FooterRow.getCell(Person::name)` retrieves footer cell for given column.
* `HeaderCell.component`/`FooterCell.component` sets or returns a component set
  to given cell. Built into Vaadin 23.2+ as `getComponent()`.
* `HeaderCell.renderer`/`FooterCell.renderer` returns the cell's renderer. Vaadin 23 and lower only.
* `grid.sort()` sorts the grid:
  * `grid.sort(nameColumn.asc)` sorts ascending by given column; `column.asc`/`column.desc` create the `GridSortOrder`.
  * `grid.sort(Person::name.asc)` sorts ascending by column created via `grid.addColumnFor(Person::name)`;
    it takes any `QuerySortOrder`. Vote for [flow-components #10277](https://github.com/vaadin/flow-components/issues/10277).
  * `grid.setSortOrder(list)` does the same as `grid.sort(list)`, so that Kotlin sees a `sortOrder` property.
* `treeGrid.getRootItems()` will fetch the root items
* `treeGrid.expandAll()` will expand all nodes; may invoke massive data loading.
  Vote for [flow-components #1657](https://github.com/vaadin/flow-components/issues/1657).
* `column.header2` returns the header set via the `setHeader()` function.
  Built into Vaadin 23.2+ as `Column.getHeaderText()`, which doesn't fall back to the header of a
  single-child column group.
* `column._internalId` returns the column's internal ID, for debugging.
* `basicRenderer.valueProvider` returns the `ValueProvider` set to the renderer.
* `renderer.template` returns the renderer's template (the Lit template expression on Vaadin 24+).
* `ItemClickEvent.isDoubleClick`

### Keyboard Shortcuts

Make sure you use these imports:

```kotlin
import com.vaadin.flow.component.Key.*
```

Then:

* `button.addClickShortcut(Alt + Ctrl + KEY_C)` clicks the button when Alt+Ctrl+C is pressed.
  Pass `onlyWhenFocused = true` to only react while the button is focused.
* `button.addFocusShortcut(Alt + Ctrl + KEY_C)` focuses the button when Alt+Ctrl+C is pressed.
* `route.addShortcut(Alt + Ctrl + KEY_C) { println("Foo") }` will cause Vaadin to run
  given block when Alt+Ctrl+C is pressed. Ideal targets are therefore: routes (for creating a route-wide shortcut), modal dialogs,
  root layouts, UI.
* The `ModifierKey`s `Shift`, `Ctrl`, `Alt`, `AltGr`, `Meta` combine with `+` into a `KeyShortcut`;
  `KEY_C.shortcut` is a shortcut with no modifiers.
* `textField.onEnter { println("Enter") }` is called when ENTER is pressed while the TextField is focused. (since 0.16)
  Unlike a plain shortcut listener, it sees the field's current value; vote for [flow #7046](https://github.com/vaadin/flow/issues/7046).

Make sure to read the [Safe JavaScript Keyboard shortcuts](https://mvysny.github.io/safe-javascript-shortcuts/) article
before designing shortcuts for your app.

### MenuBar

* Call `MenuBar.close()` to close the submenu popup. Built into Vaadin 24.4+ as `MenuBar.close()`.
* Call `MenuBar.addIconItem()` and `SubMenu.addIconItem()` to add items with icons.
  Vote for [#2688](https://github.com/vaadin/flow-components/issues/2688). Since 0.17.

### Icon

* `icon.iconName` provides a type-safe access to setting icons.
  Built into Vaadin 24.4+ as `Icon.getCollection()` / `Icon.getIcon()`.
  * `IconName` is the collection + name pair; `VaadinIcon.iconName` converts a `VaadinIcon` to it.
* `icon.setIcon(VaadinIcon)` adds the missing API of setting icons. Built into Vaadin 24.4+ as `Icon.setIcon(VaadinIcon)`.

To use custom icons with Vaadin 14+, see [Custom Icons With Vaadin 14](https://mvysny.github.io/custom-icons-vaadin/).

### Dialogs

* `getAllDialogs()` will return all dialogs attached to the UI. There may be closed dialogs
  since they are cleaned up lately by Vaadin.
* `dialog.center()` centers the dialog within the screen. Vote for [web-components #601](https://github.com/vaadin/web-components/issues/601).
* `dialog.requestClose()` honors listeners registered via `addDialogCloseActionListener()`.
  Vote for [flow-components #6027](https://github.com/vaadin/flow-components/issues/6027).

### Button

* `button.setPrimary()` adds the `ButtonVariant.LUMO_PRIMARY` theme.
  Vaadin 25.1+ has the theme-neutral `ButtonVariant.PRIMARY`, which works in both Lumo and Aura.
* `button.setDanger()` adds the `LUMO_ERROR` and `LUMO_PRIMARY` themes, e.g. for an irreversible delete.
  Vaadin 25.1+ has the theme-neutral `ButtonVariant.ERROR`.
* `button.addThemeVariantsCompat()` adds theme variants on both Vaadin 14 and 24+.

### Badge

* `Badge` is a `Span` with the Lumo `badge` theme; `BadgeVariant` lists its variants.
  Lumo only: it doesn't render in Aura. Vaadin 25.1+ has a real `Badge` component (`vaadin-badge-flow`).

### Notification

* `notification.getText()` returns the text set to the notification. Built into Vaadin 25.4+ as `Notification.getText()`.
* `notification.addCloseButton()` adds a close button, which makes the notification closeable by the user
  (and the duration of `0` starts making sense). Vote for [#438](https://github.com/vaadin/web-components/issues/438).

### Meta tags

`PageConfigurator` is deprecated and `BootstrapListener` has no utility methods to add meta tags,
therefore we introduce the following utility methods (see examples below):

* `Element.addMetaTag()` (since 0.5) adds a `<meta name="foo" content="baz">` element to the html head.
  Vaadin 15.0+ has `AppShellSettings.addMetaTag()` and `@Meta` on the `AppShellConfigurator`.

```kotlin
class MyServiceInitListener : VaadinServiceInitListener {
    override fun serviceInit(event: ServiceInitEvent) {
        event.addBootstrapListener {
            it.document.head().addMetaTag("apple-mobile-web-app-capable", "yes")
            it.document.head().addMetaTag("apple-mobile-web-app-status-bar-style", "black")
        }
    }
}
```

### StreamResource

* `createStreamResource("foo.txt") { inputStream }` creates a `StreamResource` from a stream provider.
* `bytes.toStreamResource("image.jpg", MimeType.JPEG)`, `file.toStreamResource()` and
  `"text".toStreamResource("foo.txt")` create one from a `ByteArray`, a `File` or a `String`.
* `MimeType` is a typed MIME type, with constants for the common types.
* `StreamResource` is deprecated for removal in Vaadin 24.8+ in favour of `DownloadHandler`.

### Upload

* `Upload.isEnabled` (since 0.5) allows you to enable or disable the upload component.
  Built into Vaadin 24.6+: `Upload` implements `HasEnabled`.
* `Upload.clear()` (since 0.6) clears the list of uploaded files.
  Built into Vaadin 23.0+ as `Upload.clearFileList()`.
* `Upload.buttonCaption` gets/sets the upload button caption.

### LoginForm/LoginOverlay

* `AbstractLogin.setErrorMessage(title: String?, message: String?)` (since 0.5)
  shows an error message and sets `setError(true)`. Built into Vaadin 24.4+ as `AbstractLogin.showErrorMessage()`.
* `AbstractLogin.i18n` gets/sets the `LoginI18n`; write the object back to apply changes.

### FormLayout

* `FormLayout.addRowBreak()` forces the following fields onto a new row.
  Vaadin 24.8+ has `FormLayout.addFormRow()` in auto-responsive mode.

### RadioButtonGroup

* `setItemLabelGenerator()` sets the item label generator. Since 0.6. Built into Vaadin 23.0+.

### HTML

The `HtmlSpan` component (since 0.6) has an advantage over Vaadin-provided `Html` -
it will accept any HTML snippet and will set it as an `innerHTML` to a `<span>` element.

### ComboBox

* Implemented `ComboBoxVariant`, `addThemeVariants()` and `removeThemeVariants()`. Only applicable to Vaadin 14:
  built into Vaadin 22.0+.
* `ComboBox.isSmall` toggles the `small` theme
* `ComboBox.isHelperAboveField` toggles the `helper-above-field` theme
* `ComboBox.textAlign` toggles the `align-left`/`align-center`/`align-right` theme; `ComboBoxAlign` lists the values.
* The three themes above are built into Vaadin 22.0+ as `ComboBoxVariant`; Vaadin 25.1+ adds the
  theme-neutral `SMALL`, `HELPER_ABOVE` and `ALIGN_*`.
* `ComboBox.dropdownWidth` sets a custom dropdown popup overlay width to e.g. `100px`
   or `30em`. Built into Vaadin 24.4+ as `ComboBox.setOverlayWidth()`.
* `ComboBox.prefixComponent` sets a custom prefix component. Built into Vaadin 24.0+ as `HasPrefix`.

### DatePicker

* `DatePicker.prefixComponent` sets a custom prefix component. Built into Vaadin 24.0+ as `HasPrefix`.

### Select

* Implemented `SelectVariant`, `addThemeVariants()` and `removeThemeVariants()`. Built into Vaadin 23.1+.
* `Select.isSmall` toggles the `small` theme
* `Select.isHelperAboveField` toggles the `helper-above-field` theme
* `Select.textAlign` toggles the `align-left`/`align-center`/`align-right` theme.
* The three themes above are built into Vaadin 23.1+ as `SelectVariant`; Vaadin 25.1+ adds the
  theme-neutral `SMALL`, `HELPER_ABOVE` and `ALIGN_*`.
* `Select.prefixComponent` sets a custom prefix component. Built into Vaadin 24.0+ as `HasPrefix`.

### ListBox

* `ListBox.setItemLabelGenerator()` sets the label generator.
  Built into Vaadin 23.0+, both in `ListBox` and `MultiSelectListBox`.

### Validators

* `Validator.isValid("foo")` will run the validator on given value and will return either true or false,
  depending on whether the value passed or not. (since 0.16)
* `rangeValidatorOf("must be 0 or greater", 0, null)` accepts nulls for min/max. (since 0.16)
  `RangeValidator.of()` accepts them too; this function helps Kotlin infer the type.

### JSON

* `JsonValue?.isNull` checks whether the value is either `null` or `JsonNull`.
* `jacksonReadToObject(jsonNode, Foo::class.java)` reads a Jackson `JsonNode` into an object,
  via Vaadin's internal `JacksonUtils`.

### TabSheet

Since 0.18; depend on `karibu-tools-23` to gain access to these utility functions.

* Use `Tab.index` to obtain the index of the tab. Built into Vaadin 23.3+ as `TabSheet.getIndexOf(Tab)`.
* `Tab.owner` returns the `Tabs` owning this tab
* `Tab.ownerTabSheet` returns the `TabSheet` owning this tab.
* `Tab.contents`/`TabSheet.getComponent()` returns the contents component of given tab (TabSheet only).
  Built into Vaadin 24.1+ as `TabSheet.getComponent(Tab)`.
* `TabSheet.tabCount` returns the number of the tabs. Built into Vaadin 24.5+ as `TabSheet.getTabCount()`.
* `TabSheet.removeAll()` removes all tabs. Built into Vaadin 25.4+ as `TabSheet.removeAll()`.
* `TabSheet.tabs` returns a `List<Tab>` of all tabs
* `TabSheet.getTab()` returns `Tab` for its content component (TabSheet only).
  Built into Vaadin 24.1+ as `TabSheet.getTab(Component)`.
* `TabSheet.findTabContaining()` returns `Tab` which transitively contains given component.
  Vote for [flow-components #10273](https://github.com/vaadin/flow-components/issues/10273).

Java: use `TabSheetsKt.getTab()` etc.

# License

Licensed under [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

Copyright 2021-2022 Martin Vysny

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this software except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

# Contributing / Developing

See [Contributing](CONTRIBUTING.md).
