package com.github.mvysny.kaributools

import com.vaadin.flow.component.*
import com.vaadin.flow.dom.ClassList
import com.vaadin.flow.dom.DomEventListener
import com.vaadin.flow.dom.DomListenerRegistration
import com.vaadin.flow.dom.Element
import com.vaadin.flow.router.Location
import com.vaadin.flow.router.QueryParameters

/**
 * Fires given event on the component.
 */
public fun Component.fireEvent(event: ComponentEvent<*>) {
    ComponentUtil.fireEvent(this, event)
}

/**
 * Adds [com.vaadin.flow.component.button.Button.click] functionality to all [ClickNotifier]s. This function directly calls
 * all click listeners, thus it avoids the roundtrip to client and back. It even works with browserless testing.
 * @param fromClient see [ComponentEvent.isFromClient], defaults to true.
 * @param button see [ClickEvent.getButton], defaults to 0.
 * @param clickCount see [ClickEvent.getClickCount], defaults to 1.
 */
public fun <R> R.serverClick(
    fromClient: Boolean = true,
    button: Int = 0,
    clickCount: Int = 1,
    shiftKey: Boolean = false,
    ctrlKey: Boolean = false,
    altKey: Boolean = false,
    metaKey: Boolean = false
) where R : Component, R : ClickNotifier<R> {
    fireEvent(ClickEvent<R>(this, fromClient, -1, -1, -1, -1, clickCount, button, ctrlKey, shiftKey, altKey, metaKey))
}

/**
 * Sets the alignment of the text in the component. One of `center`, `left`, `right`, `justify`.
 */
public var Component.textAlign: String?
    get() = element.style.get("textAlign")
    set(value) { element.style.set("textAlign", value) }

/**
 * Sets or removes the `title` attribute on component's element.
 */
public var Component.tooltip: String?
    get() = element.getAttribute("title")
    set(value) { element.setOrRemoveAttribute("title", value) }

/**
 * Adds the right-click (context-menu) [listener] to the component. Also causes the right-click browser
 * menu not to be shown on this component (see [preventDefault]).
 */
public fun Component.addContextMenuListener(listener: DomEventListener): DomListenerRegistration =
        element.addEventListener("contextmenu", listener)
                .preventDefault()

/**
 * Makes the client-side listener call [Event.preventDefault()](https://developer.mozilla.org/en-US/docs/Web/API/Event/preventDefault)
 * on the event.
 *
 * @return this
 */
public fun DomListenerRegistration.preventDefault(): DomListenerRegistration = addEventData("event.preventDefault()")

/**
 * Removes the component from its parent. Does nothing if the component is not attached to a parent.
 */
public fun Component.removeFromParent() {
    (parent.orElse(null) as? HasComponents)?.remove(this)
}

/**
 * Finds component's parent, parent's parent (etc) which satisfies given [predicate].
 * Returns null if there is no such parent.
 */
public fun Component.findAncestor(predicate: (Component) -> Boolean): Component? =
        findAncestorOrSelf { it != this && predicate(it) }

/**
 * Finds component, component's parent, parent's parent (etc) which satisfies given [predicate].
 * Returns null if no component on the ancestor-or-self axis satisfies.
 */
public tailrec fun Component.findAncestorOrSelf(predicate: (Component) -> Boolean): Component? {
    if (predicate(this)) {
        return this
    }
    val p: Component = parent.orElse(null) ?: return null
    return p.findAncestorOrSelf(predicate)
}

/**
 * Checks if this component is nested in [potentialAncestor].
 */
public fun Component.isNestedIn(potentialAncestor: Component): Boolean =
        findAncestor { it == potentialAncestor } != null

/**
 * Checks whether this component is currently attached to a [UI].
 *
 * Returns true for attached components even if the UI itself is closed.
 */
public fun Component.isAttached(): Boolean {
    // see https://github.com/vaadin/flow/issues/7911
    return element.node.isAttached
}

/**
 * Inserts this component as a child, right before an [existing] one.
 *
 * In case the specified component has already been added to another parent,
 * it will be removed from there and added to this one.
 */
public fun HasOrderedComponents<*>.insertBefore(newComponent: Component, existing: Component) {
    val parent: Component = requireNotNull(existing.parent.orElse(null)) { "$existing has no parent" }
    require(parent == this) { "$existing is not nested in $this" }
    addComponentAtIndex(indexOf(existing), newComponent)
}

/**
 * Return the location of the currently shown view. The function will report the current (old)
 * view in [com.vaadin.flow.router.BeforeLeaveEvent] and [com.vaadin.flow.router.BeforeEnterEvent].
 */
public val UI.currentViewLocation: Location get() = internals.activeViewLocation

/**
 * True when the component has any children.
 */
public val HasComponents.isNotEmpty: Boolean get() = (this as Component).children.findFirst().isPresent

/**
 * True when the component has no children.
 */
public val HasComponents.isEmpty: Boolean get() = !isNotEmpty
