package com.github.mvysny.kaributools

import com.vaadin.flow.component.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Input
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.dom.DomEventListener
import com.vaadin.flow.dom.DomListenerRegistration
import com.vaadin.flow.router.Location
import java.lang.reflect.Method
import kotlin.streams.toList

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
public fun <T: ClickNotifier<*>> T.serverClick(
    fromClient: Boolean = true,
    button: Int = 0,
    clickCount: Int = 1,
    shiftKey: Boolean = false,
    ctrlKey: Boolean = false,
    altKey: Boolean = false,
    metaKey: Boolean = false
) {
    (this as Component).fireEvent(ClickEvent<Component>(this,
        fromClient, -1, -1, -1, -1, clickCount, button, ctrlKey, shiftKey, altKey, metaKey))
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
@Suppress("EXTENSION_SHADOWED_BY_MEMBER") // Added in Vaadin 14.7.0; let's keep it here for older Vaadins.
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
 * True when the component has any children. Alias for [hasChildren].
 *
 * Deprecated: poorly named. A `form.isNotEmpty` may be ambiguous - it may refer to
 * whether the form has any children, or whether the form value is not empty, or something else.
 */
@Deprecated("use hasChildren", replaceWith = ReplaceWith("hasChildren"))
public val HasComponents.isNotEmpty: Boolean get() = hasChildren

/**
 * True when the component has any children.
 */
public val HasComponents.hasChildren: Boolean get() = (this as Component).children.findFirst().isPresent

/**
 * True when the component has no children.
 *
 * Deprecated: poorly named. A `form.isEmpty` may be ambiguous - it may refer to
 * whether the form has any children, or whether the form value is empty, or something else.
 */
@Deprecated("use !hasChildren")
public val HasComponents.isEmpty: Boolean get() = !hasChildren

/**
 * Splits [classNames] by whitespaces to obtain individual class names, then
 * calls [HasStyle.addClassName] on each class name. Does nothing if the string
 * is blank.
 */
public fun HasStyle.addClassNames2(classNames: String) {
    // workaround for https://github.com/vaadin/flow/issues/11709
    classNames.splitByWhitespaces().forEach { addClassName(it) }
}

/**
 * Splits [classNames] by whitespaces to obtain individual class names, then
 * calls [addClassNames2] on each class name. Does nothing if the string
 * is blank.
 */
public fun HasStyle.addClassNames2(vararg classNames: String) {
    // workaround for https://github.com/vaadin/flow/issues/11709
    classNames.forEach { addClassNames2(it) }
}

/**
 * Splits [classNames] by whitespaces to obtain individual class names, then
 * calls [HasStyle.removeClassName] on each class name. Does nothing if the string
 * is blank.
 */
public fun HasStyle.removeClassNames2(classNames: String) {
    // workaround for https://github.com/vaadin/flow/issues/11709
    classNames.splitByWhitespaces().forEach { removeClassName(it) }
}

/**
 * Splits [classNames] by whitespaces to obtain individual class names, then
 * calls [removeClassNames2] on each class name. Does nothing if the string
 * is blank.
 */
public fun HasStyle.removeClassNames2(vararg classNames: String) {
    // workaround for https://github.com/vaadin/flow/issues/11709
    classNames.forEach { removeClassNames2(it) }
}

/**
 * Splits [classNames] by whitespaces to obtain individual class names, then
 * clears the class names and calls [addClassNames2] on each class name. Does nothing if the string
 * is blank.
 */
public fun HasStyle.setClassNames2(classNames: String) {
    // workaround for https://github.com/vaadin/flow/issues/11709
    style.clear()
    addClassNames2(classNames)
}

/**
 * Splits [classNames] by whitespaces to obtain individual class names, then
 * clears the class names and calls [addClassNames2] on each class name. Does nothing if the string
 * is blank.
 */
public fun HasStyle.setClassNames2(vararg classNames: String) {
    // workaround for https://github.com/vaadin/flow/issues/11709
    style.clear()
    addClassNames2(*classNames)
}

/**
 * A component placeholder, usually shown when there's no value selected.
 * Not all components support a placeholder; those that don't return null.
 */
public var Component.placeholder: String?
    // modify when this is fixed: https://github.com/vaadin/flow/issues/4068
    get() = when (this) {
        is TextField -> placeholder
        is TextArea -> placeholder
        is PasswordField -> placeholder
        is ComboBox<*> -> this.placeholder  // https://youtrack.jetbrains.com/issue/KT-24275
        is DatePicker -> placeholder
        is Input -> placeholder.orElse(null)
        else -> null
    }
    set(value) {
        when (this) {
            is TextField -> placeholder = value
            is TextArea -> placeholder = value
            is PasswordField -> placeholder = value
            is ComboBox<*> -> this.placeholder = value
            is DatePicker -> placeholder = value
            is Input -> setPlaceholder(value)
            else -> throw IllegalStateException("${javaClass.simpleName} doesn't support setting placeholder")
        }
    }

/**
 * Concatenates texts from all elements placed in the `label` slot. This effectively
 * returns whatever was provided in the String label via [FormLayout.addFormItem].
 */
public val FormLayout.FormItem.label: String get() {
    val captions: List<Component> = children.toList().filter { it.element.getAttribute("slot") == "label" }
    return captions.joinToString("") { (it as? HasText)?.text ?: "" }
}

/**
 * The `HasLabel` interface has been introduced in Vaadin 21 but is missing in Vaadin 14.
 * Use reflection.
 */
private val _HasLabel: Class<*>? = try {
    Class.forName("com.vaadin.flow.component.HasLabel")
} catch (ex: ClassNotFoundException) {
    null
}
private val _HasLabel_getLabel: Method? = _HasLabel?.getDeclaredMethod("getLabel")
private val _HasLabel_setLabel: Method? = _HasLabel?.getDeclaredMethod("setLabel", String::class.java)

/**
 * Determines the component's `label` (usually it's the HTML element's `label` property, but it's [Checkbox.getLabel] for checkbox).
 * Intended to be used for fields such as [TextField].

 * *For `FormItem`:* Concatenates texts from all elements placed in the `label` slot. This effectively
 * returns whatever was provided in the String label via [FormLayout.addFormItem].
 *
 * Button is special: it has no label; the caption can be retrieved via [Button.getText].
 *
 * **WARNING:** the label is displayed by the component itself, rather than by the parent layout.
 * If a component doesn't contain necessary machinery
 * to display a label (for example doesn't respond to the JavaScript property 'label'), setting this property will have no visual effect.
 * For example, setting a label to a [FormLayout]
 * will show nothing since [FormLayout] doesn't display a label itself.
 * See [LabelWrapper] for a list of possible solutions.
 */
public var Component.label: String
    get() = when {
        _HasLabel != null && _HasLabel.isInstance(this) -> _HasLabel_getLabel!!.invoke(this) as String? ?: ""
        this is Checkbox -> label ?: ""
        this is FormLayout.FormItem -> this.label
        else -> element.getProperty("label") ?: ""
    }
    set(value) {
        when {
            _HasLabel != null && _HasLabel.isInstance(this) -> _HasLabel_setLabel!!.invoke(this, value)
            this is Checkbox -> label = value
            this is FormLayout.FormItem -> throw IllegalArgumentException("Setting the caption of FormItem is currently unsupported")
            else -> element.setProperty("label", value.ifBlank { null })
        }
    }

/**
 * The Component's caption: [Button.getText] for [Button], [label] for fields such as [TextField].
 *
 * Caption is generally displayed directly on the component (e.g. the Button text),
 * while [label] is displayed next to the component in a layout (e.g. a [TextField] nested in a form layout).
 *
 * **Deprecated:** this property was intended to unify captions and labels, but only managed to
 * create confusion between the two concepts. Also, there's only a [Button] which
 * has the notion of a caption. Will be removed with no replacement.
 */
@Suppress("DEPRECATION")
@Deprecated("don't use")
public var Component.caption: String
    get() = when (this) {
        is Button -> caption
        else -> label
    }
    set(value) {
        when (this) {
            is Button -> caption = value
            else -> label = value
        }
    }

/**
 * The Button's caption. Alias for [Button.getText].
 *
 * Caption is generally displayed directly on the component (e.g. the Button text),
 * while [label] is displayed next to the component in a layout (e.g. a [TextField] nested in a form layout).
 *
 * **Deprecated:** this property was intended to unify captions and labels, but only managed to
 * create confusion between the two concepts. Also, there's only a [Button] which
 * has the notion of a caption. Will be removed with no replacement.
 */
@Deprecated("don't use")
public var Button.caption: String
    get() = text
    set(value) {
        text = value
    }

internal fun HasElement.getChildComponentInSlot(slotName: String): Component? =
    element.getChildrenInSlot(slotName).firstOrNull()?.component?.get()

internal fun HasElement.setChildComponentToSlot(slotName: String, component: Component?) {
    element.clearSlot(slotName)
    if (component != null) {
        component.element.setAttribute("slot", slotName)
        element.appendChild(component.element)
    }
}
