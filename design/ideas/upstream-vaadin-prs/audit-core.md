# Audit — core utilities (karibu-tools/src/main/kotlin)

Upstream = vaadin/flow `main` (25.4-SNAPSHOT, 2d99e9c) and vaadin/flow-components `main` (c1eb3a2).
"First" = first Vaadin **platform** x.y that has the equivalent. Evidence: `javap` = bisected
flow-server / flow-components-base jars (2.0, 2.6–2.11, 3.0, 4.0–9.0, 23.0–23.3, 24.0–24.4, 24.8, 25.0, 25.2);
`@since` = upstream javadoc; `gh` = PR/issue metadata. A `14.x / NN` pair means "backported to 14.x, and in the
new line since NN".

Buckets: **A** PR candidate (A-low = weak value, needs your call) · **B** already upstream · **C** Kotlin-only ·
**D** upstream rejected · **E** obsolete / not a Vaadin gap. Tags: `wc` = needs web-components change;
`theme` = theme-bound (none in this scope).

## ComponentUtils.kt

| Symbol | README | Upstream on main | First | Ev | Tickets | Bucket | Note |
|---|---|---|---|---|---|---|---|
| `Component.fireEvent()` :25 | yes | `ComponentUtil.fireEvent()` (public); `Component.fireEvent` is `protected` | 14.0 (baseline) | javap | none found | **D** | The protected modifier is the decision; no ticket to link. |
| `ClickNotifier.serverClick()` :36 | yes | `Button.click()` (server-side, Button only, no modifiers) | 14.0 (baseline) | grep | none found | **E** | Test aid; Karibu-Testing's `_click` covers it; generic `ClickNotifier.click()` would pollute every Div/Span. |
| `Component.textAlign` :52 | yes | `Style.setTextAlign(Style.TextAlign)` | 24.1 | javap | — | **B** | Typed enum, not String. |
| `Component.tooltip` :59 | yes | `HasTooltip.setTooltipText()` (flow-components-base) | 23.3 | javap | flow#13411 PR closed unmerged (`title` API) | **E** | Superseded: karibu sets the `title` attribute, Vaadin has a real Tooltip. |
| `Component.addContextMenuListener()` :67 | yes | `element.addEventListener("contextmenu", l).preventDefault()` | 24.2 (one-liner) | javap | none | **E** | One line once `preventDefault()` exists. |
| `DomListenerRegistration.preventDefault()` :77 | **no** | `DomListenerRegistration.preventDefault()` | 24.2 | javap | — | **B** | Missing from README. |
| `Component.removeFromParent()` :82 | yes | `Component.removeFromParent()` | 24.0 | javap | — | **B** | |
| `Component.findAncestor(predicate)` :90 | yes | `Component.findAncestor(Class<T>)` only | 23.2 (Class variant) | javap | flow#14002 (merged, Class variant), flow#13984, flow#26032 → **flow#26035 merged, 25.4** | **B** | Predicate overload, upstream since 25.4. Home: `Component` — in #14002 upstream moved it *from* ComponentUtil to a public Component method on purpose ("ComponentUtil feels internal", mstahv). |
| `Component.findAncestorOrSelf()` :97 | yes | none | — | grep | none | **A** | Same PR as findAncestor(predicate). |
| `Component.isNestedIn()` :108 | yes | none | — | grep | none | **A** | Same PR or ComponentUtil; trivially `findAncestor { it == x } != null`. |
| `Component.isAttached()` :117 | yes | `Component.isAttached()` | 14.7 / 18.0 | javap | flow#7911 closed-completed | **B** | |
| `HasOrderedComponents.insertBefore()` :128 | yes | `addComponentAtIndex(indexOf(existing), c)` | — | grep | none | **A-low** | Mirrors DOM `insertBefore`. |
| `UI.currentViewLocation` :138 | yes | `UI.getActiveViewLocation()` | 24.3 | javap | — | **B** | |
| `HasComponents.isNotEmpty` :147 (deprecated) | no | — | — | — | — | **E** | Deprecated. |
| `HasComponents.hasChildren` :152 | yes | none | — | grep | none | **A-low** | `getChildren().findAny().isPresent()`. |
| `HasComponents.isEmpty` :161 (deprecated) | no | — | — | — | — | **E** | Deprecated. |
| `HasStyle.addClassNames2(String)` :168, `(vararg)` :178 | yes | `HasStyle.addClassNames(String...)` splits on spaces | 14.8 / 22.0 | javap | flow#11709 closed-completed (fix flow#11861) | **B** | README "Vote for #11709" is stale. Upstream splits only on `" +"` (tabs/newlines not handled) and throws on a blank string. |
| `HasStyle.removeClassNames2()` :188, :198 | yes | `HasStyle.removeClassNames(String...)` splits too | 14.8 / 22.0 | grep | flow#11709 | **B** | |
| `HasStyle.setClassNames2()` :208, :219 | yes | `HasStyle.setClassName(String)` (sets `class` attribute verbatim) | 14.0 | grep | — | **B** | **BUG in karibu:** calls `style.clear()` (inline styles) instead of clearing class names; the test uses `containsAll`, so it misses that old classes stay. |
| `Component.placeholder` :243 | yes | `HasPlaceholder` | 24.3 | javap | flow#4068 closed-completed | **B** | README "Vote for #4068" is stale. |
| `FormLayout.FormItem.label` :275 | **no** | none (FormItem has no label getter) | — | grep | flow-components#1015 open ("modify label … afterwards") | **A** | Target flow-components#1015. |
| `Component.label` :310 | yes | `HasLabel` | 14.8 / 17.0 (interface) | javap | flow-components#5129 open (SideNav should implement HasLabel) | **B** | Fallback to the element `label` property for non-HasLabel components is the part upstream won't take (E). |
| `Component.caption` :340 (deprecated) | yes | — | — | — | — | **E** | Deprecated, "will be removed"; README still advertises it. |
| `Button.caption` :363 (deprecated) | no | `Button.getText()` | — | — | — | **E** | |
| `Component.ariaLabel` :395 | yes | `HasAriaLabel` | 22.0 | javap | — | **B** | README says "Vaadin 23+"; it's actually 22.0 (not in 14.x). |

## ElementUtils.kt

| Symbol | README | Upstream on main | First | Ev | Tickets | Bucket | Note |
|---|---|---|---|---|---|---|---|
| `Element.setOrRemoveAttribute()` :18 | yes | none; `setAttribute(name, null)` throws IAE | — | grep | flow#25819 open PR (`bindAttributeBoolean`), related only | **A-low** | Adding null-removes to `setAttribute` would change behavior; it would need a new method. |
| `Element.setOrRemoveAttributeIfNullOrEmpty()` :31 | **no** | none | — | grep | none | **A-low** | Bundle with the one above. |
| `ClassList.toggle()` :43 | yes | `ClassList.set(name, !contains(name))` | — | grep | flow#3688 open (Polymer, only loosely related) | **A-low** | Mirrors DOM `classList.toggle`. |
| `Element.insertBefore()` :52 | yes | `insertChild(indexOfChild(existing), e)` | — | grep | none | **A-low** | Mirrors DOM; bundle with HasOrderedComponents.insertBefore. |
| `Element.textRecursively2` :61 | yes | `Element.getTextRecursively()` still broken | — | gh | **flow#3668 open**; draft PR flow#26027 | **A** | Bug-fix PR against #3668, not new API. |
| `jsoup Node.textRecursively` :68 | **no** | — | — | — | — | **E** | jsoup helper, not Vaadin. |
| `Element.getVirtualChildren()` :77 | yes | `ComponentUtil.getAllChildren()` (component-level, includes virtual children) | 25.2 | javap | flow#24408 merged | **B** | Partial: no Element-level accessor. Karibu reaches into internal `VirtualChildrenList`. |
| `StateNode.element` :92 | yes | `Element.get(StateNode)` | 14.0 | — | — | **C** | |
| `Element.getChildrenInSlot()` :97 | yes | `SlotUtils.getElementsInSlot()` (flow-components-base) | 23.1 | javap | — | **B** | |
| `Element.clearSlot()` :103 | yes | `SlotUtils.clearSlot()` | 23.1 | javap | — | **B** | |

## RouterUtils.kt

| Symbol | README | Upstream on main | First | Ev | Tickets | Bucket | Note |
|---|---|---|---|---|---|---|---|
| `navigateTo<T>()` :17 | yes | `UI.navigate(Class)` | 14.0 | — | — | **C** | Reified. |
| `navigateTo(KClass)` :24 | yes | `UI.navigate(Class)` | 14.0 | — | — | **C** | Uses reflection because the signature changed in 24.1.2. |
| `navigateTo(KClass, param)` :40 | yes | `UI.navigate(Class, T)` | 14.0 | — | — | **C** | |
| `Router.configuration` :58 | **no** | `RouteConfiguration.forRegistry(router.getRegistry())` | 14.0 | — | — | **C** | |
| `getRouteUrl(KClass, RouteParameters, QueryParameters)` :64 | partially | `RouteConfiguration.getUrl(Class, RouteParameters)`; nothing with QueryParameters | — | javap (absent in every jar) | none | **A** | `RouteConfiguration.getUrl(Class, RouteParameters, QueryParameters)`. |
| `getRouteUrl(KClass, String)` :85 | yes | — | — | — | — | **C** | Covered by the A above plus `QueryParameters.fromString`. |
| `navigateTo(String)` with `?query` :107 | yes | `UI.navigate(String)` now parses query and fragment | 25.3 | gh (flow#25591 → CP flow#25618 merged 09-10, before the 25.3.0 tag) | flow#25591 merged | **B** | `UI.navigate(String, QueryParameters)` exists since 14.0. |
| `RouterLink.navigateTo()` :115 | **no** | `UI.navigate(link.getHref())` | — | — | none | **E** | Test aid. |
| `RouterLink.setRoute(KClass)` :136, `(KClass, param)` :146 | **no** | `RouterLink.setRoute(Class)` / `(Class, T)` without Router | 14.1 / 17.0 (param) | javap / @since | — | **C** | |
| `AfterNavigationEvent.routeClass` :153 | yes | `getActiveChain().get(0).getClass()` | — | grep | none | **A-low** | |
| `Location.getRouteClass()` :160 | yes | `RouteConfiguration.getRoute(String)` | 14.0 | grep | — | **B** | Karibu also passes query params into resolution. |
| `QueryParameters.get()` operator :171 | yes | `QueryParameters.getSingleParameter()` → Optional | 24.2 | javap | — | **B** | Upstream returns the first value; karibu throws on more than one. The operator form is C. |
| `QueryParameters.getValues()` :184 | yes | `QueryParameters.getParameters(String)` | 24.2 | javap | — | **B** | |
| `QueryParameters(String)` :191 | yes | `QueryParameters.fromString()` | 14.7 / 20.0 | javap | — | **B** | |
| `QueryParameters.isEmpty` :199 / `isNotEmpty` :204 | **no** | none | — | grep | none | **A-low** | |
| `Anchor.setOpenInNewTab()` :211 | yes | `setTarget(AnchorTarget.BLANK)` | 14.0 | — | — | **C** | Sugar. |
| `RouterLink.target` :218 | yes | none — RouterLink has no target API | — | grep | **flow#5791 open** ("RouterLink should support _blank, _self, _top targets") | **A** | Target flow#5791. |
| `RouterLink.setOpenInNewTab()` :230 | yes | none | — | — | flow#5791 | **A-low** | Bundle with RouterLink.target. |
| `Anchor.target_` :237 | yes | `Anchor.setTarget(AnchorTargetValue)` / `getTargetValue()` | 14.0 | — | — | **C** | |

## Shortcuts.kt

| Symbol | README | Upstream on main | First | Ev | Tickets | Bucket | Note |
|---|---|---|---|---|---|---|---|
| `ModifierKey` enum + `plus` operators :9, `Shift/Ctrl/Alt/AltGr/Meta` :25–42, `Set<ModifierKey>.plus` :45, `KeyShortcut` :53, `Key.shortcut` :102 | partially (via examples) | `KeyModifier`, varargs `addClickShortcut(Key, KeyModifier...)` | 14.0 | — | flow#5051 open (KeyModifier.ALT collides with Key.ALT; the `.vaadin` hack) | **C** | Operator DSL. |
| `ClickNotifier.addClickShortcut(KeyShortcut, onlyWhenFocused)` :65 | yes (param not) | `addClickShortcut(...).listenOn(this)` | 14.0 | — | — | **C** | |
| `Focusable.addFocusShortcut(KeyShortcut)` :76 | yes | `addFocusShortcut(Key, KeyModifier...)` | 14.0 | — | — | **C** | |
| `Component.addShortcut(KeyShortcut, block)` :91 | yes | `Shortcuts.addShortcutListener(...).listenOn(c)` | 14.0 | — | — | **C** | |
| `TextField.onEnter()` :107 | yes | none; the stale-value bug remains | — | gh | flow#17484 closed-not-planned as duplicate of **flow#7046 open** ("Synchronizing values along with a shortcutlistener") | **A** | The PR is a fix for flow#7046, not `onEnter`. |

## BrowserTimeZone.kt

| Symbol | README | Upstream on main | First | Ev | Tickets | Bucket | Note |
|---|---|---|---|---|---|---|---|
| `ExtendedClientDetails.timeZone: ZoneId` :15 | **no** | only `getTimeZoneId()` String, `getTimezoneOffset()`; `getBrowserTime(): Instant` | 25.3 (getBrowserTime) | grep / @since | flow#25480 merged (getBrowserTime); flow#26034 → PR flow#26036 open | **A** | `ExtendedClientDetails.getZoneId()` with the offset fallback. |
| `ExtendedClientDetails.currentDateTime` :27 | **no** | none | — | — | — | **A-low** | Bundle with getZoneId. |
| `BrowserTimeZone.fetch()` :43, `.extendedClientDetails` :71 | yes / **no** | `Page.getExtendedClientDetails()`: collected automatically during UI init | 25.0 | javap / @since | — | **B** | The session cache is obsolete on 25. |
| `BrowserTimeZone.get` :58, `.currentDateTime` :80, `.toLocalDateTime()` :64 | yes / yes / **no** | none (would follow from getZoneId) | — | — | — | **E** | Session-scoped facade; E once getZoneId lands upstream. |

## JsoupUtils.kt, MiscUtils.kt, StreamResourceUtils.kt, MimeType.kt

| Symbol | README | Upstream on main | First | Ev | Tickets | Bucket | Note |
|---|---|---|---|---|---|---|---|
| jsoup `Element.addMetaTag()` :13 | yes | `AppShellSettings.addMetaTag()` (+ `@Meta`) | 15.0 | javap | flow#4912 open (imperative meta API), flow#3383 open | **B** | The BootstrapListener/PageConfigurator era is over. |
| `Class.getGetter()` :17, `Class.getPropertyComparator()` :29 | **no** | — | — | — | — | **E** | JavaBeans reflection, not a Vaadin gap. |
| `KProperty1.comparator` :37 | **no** | — | — | — | — | **C** | |
| `JsonValue?.isNull` :43 | **no** | — | — | grep | — | **E** | elemental.json is gone: flow main has zero `elemental.json` imports (Jackson). |
| `jacksonReadToObject()` :51 | **no** | internal `JacksonUtils.readToObject` | — | — | — | **E** | Compat shim over internal API. |
| `createStreamResource()` :15, `ByteArray/File/String.toStreamResource()` :34/:49/:67 | **no** | `DownloadHandler.fromInputStream()` / `forFile()`; `StreamResource` `@Deprecated(since 24.8, forRemoval)` | 24.8 | javap | — | **E** | Superseded; a `DownloadHandler` flavor would be the thing to add in karibu. |
| `MimeType` (+ `ContentType`, constants, `of()`) :6 | **no** | none | — | — | — | **E** | Generic, not a Vaadin gap. |

## VaadinVersion.kt, DepthFirstTreeIterator.kt

| Symbol | README | Upstream on main | First | Ev | Tickets | Bucket | Note |
|---|---|---|---|---|---|---|---|
| `SemanticVersion` (+ `isExactly/isAtLeast/isAtMost/fromString`) :12 | **no** | — | — | — | — | **E** | Generic. |
| `VaadinVersion.flow` :75 | yes | `Version.getFullVersion()` / `getMajorVersion()`… | 14.0 | — | — | **B** | |
| `VaadinVersion.vaadin` :113, `.get` :120 | **no** / yes | `Platform.getVaadinVersion()` | 23.0 | javap | flow#17017 closed-completed (fix flow#17034, 2023-06) | **B** | Karibu still needed for 14–22. |
| `VaadinVersion.hilla` :153 | **no** | `Platform.getHillaVersion()` | 24.2 | javap | — | **B** | |
| `DepthFirstTreeIterator` :13 | **no** | — | — | — | — | **E** | Generic. |
| `Component.walk()` :28 | yes | `ComponentUtil.streamDescendants()` (pre-order, **excludes self**, **includes virtual children**) | 25.2 | javap / @since | flow#24408 merged | **B** | Confirms the user's belief: same traversal in a different shape. |

## HtmlSpan.kt, LabelWrapper.kt, Validators.kt, DataProviderUtils.kt

| Symbol | README | Upstream on main | First | Ev | Tickets | Bucket | Note |
|---|---|---|---|---|---|---|---|
| `HtmlSpan` (+ `innerHTML`) :21 | yes | `Html` still requires exactly one root element (now with Safelist sanitization) | — | grep | none found | **A-low** | Open an issue first; upstream is XSS-wary (Safelist work in flow#24585). |
| `LabelWrapper` :19 | yes | none (FormItem / CustomField) | — | — | flow-components#3170 closed (addFormItem without label), only loosely related | **E** | Subclasses `CustomField<Void>` as a hack; upstream wouldn't take that shape. |
| `Validator.isValid()` :10 | yes | `apply(v, new ValueContext()).isError()` | — | grep | none | **A-low** | A default method on `Validator`. |
| `rangeValidatorOf()` :16 | yes | `RangeValidator.of(msg, min, max)`: same nullsFirst comparator, null limits allowed | 14.0 | grep | — | **B** | The wrapper only helps Kotlin generic inference (C-ish). |
| `DataProvider.fetchAll()` :13 | yes | `fetch(new Query()).toList()`; `DataView.getItems()` on components | 17.0 (DataView) | — | none | **E** | One-liner. |
| `KProperty1.asc` / `.desc` :15/:16 | yes | `QuerySortOrder.asc("name")` | 14.0 | — | — | **C** | |

## Summary

- **A (8):** findAncestor(predicate) (now B: flow#26035, 25.4) + findAncestorOrSelf + isNestedIn, FormItem label getter (flow-components#1015), textRecursively fix (flow#3668), RouteConfiguration.getUrl with QueryParameters, RouterLink target (flow#5791), shortcut value-sync fix (flow#7046), ExtendedClientDetails.getZoneId.
- **A-low (12):** insertBefore ×2, hasChildren, setOrRemoveAttribute ×2, ClassList.toggle, AfterNavigationEvent.routeClass, QueryParameters.isEmpty, RouterLink.setOpenInNewTab, currentDateTime, HtmlSpan, Validator.isValid.
- **Stale README "Vote for" links:** flow#11709 (fixed in 14.8 / 22.0), flow#4068 (HasPlaceholder, 24.3). flow#3668 is still open and valid.
- **README gaps in this scope:** preventDefault, setOrRemoveAttributeIfNullOrEmpty, Node.textRecursively, FormItem.label, Router.configuration, RouterLink.navigateTo, RouterLink.setRoute, QueryParameters.isEmpty/isNotEmpty, Key.shortcut / KeyShortcut / ModifierKey, the onlyWhenFocused param, ECD.timeZone / currentDateTime, BrowserTimeZone.toLocalDateTime / extendedClientDetails, all of MiscUtils, StreamResourceUtils, MimeType, SemanticVersion, VaadinVersion.vaadin / hilla, DepthFirstTreeIterator.
- **Bug found:** `setClassNames2` clears inline styles (`style.clear()`), not class names, and the test can't catch it.
- **Out of scope:** none.
