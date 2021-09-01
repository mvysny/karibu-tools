# Karibu-Tools: The Vaadin Missing Utilities

[![GitHub tag](https://img.shields.io/github/tag/mvysny/karibu-tools.svg)](https://github.com/mvysny/karibu-tools/tags)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mvysny.karibu-tools/karibu-tools/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mvysny.karibu-tools/karibu-tools)

Utility functions missing from Vaadin 14+.

## General Vaadin Utilities

A set of general Vaadin utilities applicable to all components.

### Obtaining Vaadin version at runtime

* Retrieve the `VaadinVersion.get` property to get the Vaadin version such as 14.7.0
* call `VaadinVersion.flow` to obtain the Vaadin Flow `flow-server.jar` version: for example 2.6.7 for Vaadin 14.6.8.

### Events

* Call `component.fireEvent()` to fire any event on the component (a shortcut to `ComponentUtil.fireEvent()`).
* Call `ClickNotifier.serverClick()` to notify all click listeners (to fire a `ClickEvent`).

### Component hierarchy

* Call `Component.findAncestor()` or `Component.findAncestorOrSelf` to discover component's
  ancestor which satisfies given predicate.
* call `Component.removeFromParent()` to remove the component from its parent.
* call `Component.isNestedIn(potentialAncestor: Component)` to discover whether a component
  is nested within given potential ancestor.
* query `Component.isAttached()` to see whether this component is currently attached to an UI.
  Vote for [flow #7911](https://github.com/vaadin/flow/issues/7911).
* call `HasOrderedComponents<*>.insertBefore()` to insert a component before given component.
* query `HasComponents.isNotEmpty` or `HasComponents.isEmpty` to see whether a component
  has any children.
* `Component.walk()` will return an `Iterable<Component>` which walks the component child tree,
  depth-first: first the component, then its descendants, then its next sibling.

### Misc Component

* get/set `component.textAlign` to read/write the `text-align` CSS property
* get/set `component.tooltip` to read/write the hovering tooltip (the `title` CSS property)
* call `Component.addContextMenuListener()` to add the right-click context listener to a component.
  Also causes the right-click browser menu not to be shown on this component.
* query `UI.currentViewLocation` to return the location of the currently shown view.
* call `div.addClassNames2("  foo bar   baz")` to add multiple class names. Vote for [flow #11709](https://github.com/vaadin/flow/issues/11709).

### Misc Element

* call `ClassList.toggle` to set or remove given CSS class.
* call `Element.setOrRemoveAttribute` to set an attribute to given value, or remove the
  attribute if the value is null.

### Router

Navigating:

* Call `navigateTo<AdminRoute>()` or `navigateTo(AdminRoute::class)` or `navigateTo(DocumentRoute::class, 25L)`.
* Call `navigateTo(String)` to navigate anywhere within the app
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
* Check `Grid.isMultiSelect` to see whether a grid is configured as multi-select.
* Check `Grid.isSingleSelect` to see whether a grid is configured as single-select.
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
