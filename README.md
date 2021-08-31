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

### Misc Component

* get/set `component.textAlign` to read/write the `text-align` CSS property
* get/set `component.tooltip` to read/write the hovering tooltip (the `title` CSS property)
* call `Component.addContextMenuListener()` to add the right-click context listener to a component.
  Also causes the right-click browser menu not to be shown on this component.
* query `UI.currentViewLocation` to return the location of the currently shown view.

### Misc Element

* call `ClassList.toggle` to set or remove given CSS class.
* call `Element.setOrRemoveAttribute` to set an attribute to given value, or remove the
  attribute if the value is null.

### Router

* call `queryParameters["foo"]` to obtain the value of the `?foo=bar` query parameter.
* call `queryParameters.getValues("foo")` to get all values of the `foo` query parameter.
* call the `QueryParameters("foo=bar")` factory method to parse the query part of the URL
  to the Vaadin `QueryParameters` class.

### Time Zone

In order to properly display `LocalDate` and `LocalDateTime` on client's machine, you need
to fetch the browser's TimeZone first. You can achieve that simply by calling `BrowserTimeZone.fetch()`,
for example when the session is being initialized. `fetch()` will request the information from
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
