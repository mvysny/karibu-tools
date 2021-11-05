# Karibu-Tools: The Vaadin Missing Utilities

[![GitHub tag](https://img.shields.io/github/tag/mvysny/karibu-tools.svg)](https://github.com/mvysny/karibu-tools/tags)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mvysny.karibu-tools/karibu-tools/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mvysny.karibu-tools/karibu-tools)
[![CI](https://github.com/mvysny/karibu-tools/actions/workflows/gradle.yml/badge.svg)](https://github.com/mvysny/karibu-tools/actions/workflows/gradle.yml)

Utility functions missing from Vaadin 14+.

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

See the tag above for the latest version.

## General Vaadin Utilities

A set of general Vaadin utilities applicable to all components.

### Obtaining Vaadin version at runtime

* Retrieve the `VaadinVersion.get` property to get the Vaadin version such as 14.7.0
* call `VaadinVersion.flow` to obtain the Vaadin Flow `flow-server.jar` version: for example 2.6.7 for Vaadin 14.6.8.

### Events

* Call `component.fireEvent()` to fire any event on the component (a shortcut to `ComponentUtil.fireEvent()`).
* Call `ClickNotifier.serverClick()` to notify all click listeners (to fire a `ClickEvent`).

### Component hierarchy

* Call `Component.findAncestor()` or `Component.findAncestorOrSelf()` to discover component's
  ancestor which satisfies given predicate.
* call `Component.removeFromParent()` to remove the component from its parent.
* call `Component.isNestedIn(potentialAncestor: Component)` to discover whether a component
  is nested within given potential ancestor.
* query `Component.isAttached()` to see whether this component is currently attached to an UI.
  Vote for [flow #7911](https://github.com/vaadin/flow/issues/7911).
* call `HasOrderedComponents<*>.insertBefore()` to insert a component before given component.
* query `HasComponents.hasChildren` to see whether a component has any children.
* `Component.walk()` will return an `Iterable<Component>` which walks the component child tree,
  depth-first: first the component, then its descendants, then its next sibling.

### Misc Component

* get/set `component.textAlign` to read/write the `text-align` CSS property
* get/set `component.tooltip` to read/write the hovering tooltip (the `title` CSS property)
* call `Component.addContextMenuListener()` to add the right-click context listener to a component.
  Also causes the right-click browser menu not to be shown on this component.
* query `UI.currentViewLocation` to return the location of the currently shown view.
* call `div.addClassNames2("  foo bar   baz")` to add multiple class names. Vote for [flow #11709](https://github.com/vaadin/flow/issues/11709).
  * also `div.removeClassNames2()` and `div.setClassNames2()`
* `component.placeholder` unifies the various component placeholders, usually shown when there's no value selected.
  Vote for [flow #4068](https://github.com/vaadin/flow/issues/4068).
* `component.caption` unifies component captions. Caption is displayed directly on the component (e.g. the Button text),
  while label is displayed next to the component in a layout (e.g. form layout).
* `component.label` unifies component labels. Vote for [flow #3241](https://github.com/vaadin/flow/issues/3241).

### Misc Element

* call `ClassList.toggle` to set or remove given CSS class.
* call `Element.setOrRemoveAttribute` to set an attribute to given value, or remove the
  attribute if the value is null.
* `Element.insertBefore()` to insert a child element before another child. A counterpart for JavaScript DOM
  `Node.insertBefore()`.
* `Element.textRecursively2` returns all the text recursively present in the element. Vote for
  [flow #3668](https://github.com/vaadin/flow/issues/3668).
* `Element.getVirtualChildren()` Returns all virtual child elements added via `Element.appendVirtualChild`.
* `StateNode.element` returns `Element` for that `StateNode`.

### Router

Navigating:

* Call `navigateTo<AdminRoute>()` or `navigateTo(AdminRoute::class)` or `navigateTo(DocumentRoute::class, 25L)`.
* Call `navigateTo(String)` to navigate anywhere within the app. Supports query parameters as well:
   * `""` (empty string) - the root view.
   * `foo/bar` - navigates to a view
   * `foo/25` - navigates to a view with parameters
   * `foo/25?token=bar` - any view with parameters and query parameters
   * `?token=foo` - the root view with query parameters
* Call `navigateTo(getRouteUrl(AdminRoute::class, "lang=en"))` to navigate to `admin?lang=en`.
* To obtain the route class from `AfterNavigationEvent`, query `event.routeClass`
* To obtain the route class from a `Location`, call `location.getRouteClass()`

QueryParameters:

* call `queryParameters["foo"]` to obtain the value of the `?foo=bar` query parameter.
* call `queryParameters.getValues("foo")` to get all values of the `foo` query parameter.
* call the `QueryParameters("foo=bar")` factory method to parse the query part of the URL
  to the Vaadin `QueryParameters` class.

### Time Zone

In order to properly display `LocalDate` and `LocalDateTime` on client's machine, you need
to fetch the browser's TimeZone first. You can achieve that simply by calling `BrowserTimeZone.fetch()`,
for example when the [Vaadin Session is being initialized](https://vaadin.com/docs/v14/flow/advanced/tutorial-application-lifecycle.html).
`fetch()` will request the information from
the browser and will store it into the session. Afterwards, simply
call `BrowserTimeZone.get` to get the browser's time zone instantly.

* Call `BrowserTimeZone.currentDateTime` to get the current date time at browser's current time zone.

### Text selection utils

The following functions are applicable to any field that edits text, e.g.
`TextField`, `TextArea`, `EmailField`:

* Call `field.selectAll()` to select all text within the field.
* Call `field.selectNone()` to select no text
* Call `field.setCursorLocation()` to place the cursor at given character
* Call `field.select(range)` to select a range within the text.

### DataProviders

* Use `dataProvider.fetchAll()` to fetch all items provided by this data provider as an eager list. Careful with larger data!
* Use `Person::name.asc`/`Person::name.desc` to create a `QuerySortOrder` which is useful with DataProvider's `Query.sortOrders`
  or `Grid.sort()`.

### Grid

* Use `Grid.refresh()` to call `DataProvider.refreshAll()`
* Selection:
  * Check `Grid.isMultiSelect` to see whether a grid is configured as multi-select.
  * Check `Grid.isSingleSelect` to see whether a grid is configured as single-select.
  * `Grid.isSelectionEmpty` returns true if there's nothing selected in the grid.
  * `Grid.selectionMode` allows you to read/write the current selection mode.
  * `Grid.isSelectionAllowed` returns false if `Grid.SelectionMode.NONE` is currently set.
* Multitude overloaded `Grid.addColumnFor()` which allow you to create a column
  using given converter or renderer to format a value. Allows both for passing in `KProperty`
  or a property by name. For example, `grid.addColumnFor(Person::name)` will create a
  sortable column displaying a name of a person, setting the column header to "Name".
* `Grid.getColumnBy(Person::name)` retrieves a column created via `grid.addColumnFor(Person::name)`.
* `HeaderRow.getCell(Person::name)` retrieves header cell for given column.
* Similarly, `FooterRow.getCell(Person::name)` retrieves footer cell for given column.
* `HeaderCell.component`/`FooterCell.component` sets or returns a component set
  to given cell.
* `grid.sort()` sorts the grid:
  * `grid.sort(nameColumn.asc)` sorts ascending by given column;
  * `grid.sort(Person::name.asc)` sorts ascending by column created via `grid.addColumnFor(Person::name)`
* `treeGrid.getRootItems()` will fetch the root items
* `treeGrid.expandAll()` will expand all nodes; may invoke massive data loading.
* `column.header2` returns the header set via the `setHeader()` function.
* `basicRenderer.valueProvider` returns the `ValueProvider` set to the renderer.
* `ItemClickEvent.isDoubleClick`

### Keyboard Shortcuts

Make sure you use these imports:

```kotlin
import com.github.mvysny.kaributools.ModifierKey.*
import com.vaadin.flow.component.Key.*
```

Then:

* `button.addClickShortcut(Alt + Ctrl + KEY_C)` clicks the button when Alt+Ctrl+C is pressed.
* `button.addFocusShortcut(Alt + Ctrl + KEY_C)` focuses the button when Alt+Ctrl+C is pressed.
* `route.addShortcut(Alt + Ctrl + KEY_C) { println("Foo") }` will cause Vaadin to run
  given block when Alt+Ctrl+C is pressed. Ideal targets are therefore: routes (for creating a route-wide shortcut), modal dialogs,
  root layouts, UI.

Make sure to read the [Safe JavaScript Keyboard shortcuts](https://mvysny.github.io/safe-javascript-shortcuts/) article
before designing shortcuts for your app.

### MenuBar

* Call `MenuBar.close()` to close the submenu popup. Vote for [#102](https://github.com/vaadin/vaadin-menu-bar/issues/102).

### Icon / IronIcon

TODO how to register new icon collection?

* `icon.iconName` and `ironIcon.iconName` provides a type-safe access to setting icons.
* `icon.setIcon(VaadinIcon)`/`ironIcon.setIcon(VaadinIcon)` adds the missing API of setting icons.

### Dialogs

* `getAllDialogs()` will return all dialogs attached to the UI. There may be closed dialogs
  since they are cleaned up lately by Vaadin.
* `dialog.center()` centers the dialog within the screen. Vote for [#220](https://github.com/vaadin/vaadin-dialog/issues/220)

### Button

* `button.setPrimary()` adds the `ButtonVariant.LUMO_PRIMARY` theme.

### Notification

* `notification.getText()` returns the text set to the notification. Vote for [#2446](https://github.com/vaadin/web-components/issues/2446).
* `notification.addCloseButton()` adds a close button, which makes the notification closeable by the user
  (and the duration of `0` starts making sense). Vote for [#438](https://github.com/vaadin/web-components/issues/438).

### PageConfigurator

`PageConfigurator` is now deprecated, the suggestion is to use `BootstrapListener` but there
are no utility methods to add meta tags etc! Therefore we introduce the following utility methods (see examples below):

* `Element.addMetaTag()` (since 0.5) adds a `<meta name="foo" content="baz">` element to the html head.

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

### Upload

* `Upload.isEnabled` (since 0.5) allows you to enable or disable the upload component.
  Vote for [#2182](https://github.com/vaadin/flow-components/issues/2182)
* `Upload.clear()` (since 0.6) clears the list of uploaded files.
  Vote for [#1572](https://github.com/vaadin/flow-components/issues/1572).

### LoginForm/LoginOverlay

* `AbstractLogin.setErrorMessage(title: String?, message: String?)` (since 0.5)
  shows an error message and sets `setError(true)`. Vote for [issue #1525](https://github.com/vaadin/flow-components/issues/1525)

### RadioButtonGroup

* `setItemLabelGenerator()` sets the item label generator. See+vote for [ticket #1681](https://github.com/vaadin/flow-components/issues/1681). Since 0.6.

### HTML

The `HtmlSpan` component (since 0.6) has an advantage over Vaadin-provided `Html` -
it will accept any HTML snippet and will set it as an `innerHTML` to a `<span>` element.

### ComboBox

* Implemented `ComboBoxVariant`, `addThemeVariants()` and `removeThemeVariants()`.
  See+vote for [issue #454](https://github.com/vaadin/flow-components/issues/454).
* `ComboBox.isSmall` toggles the `small` theme
* `ComboBox.isHelperAboveField` toggles the `helper-above-field` theme
* `ComboBox.textAlign` toggles the `align-left`/`align-center`/`align-right` theme.

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
