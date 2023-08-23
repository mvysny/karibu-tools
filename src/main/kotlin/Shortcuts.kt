package com.github.mvysny.kaributools

import com.vaadin.flow.component.*
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.value.ValueChangeMode
import com.vaadin.flow.server.Command
import java.io.Serializable

public enum class ModifierKey(private val hack: Int) {
    Shift(KeyModifier.SHIFT.ordinal),
    Ctrl(KeyModifier.CONTROL.ordinal),
    Alt(KeyModifier.ALT.ordinal),
    AltGr(KeyModifier.ALT_GRAPH.ordinal),
    Meta(KeyModifier.META.ordinal);

    public infix operator fun plus(other: ModifierKey): Set<ModifierKey> = setOf(this, other)
    public infix operator fun plus(key: Key): KeyShortcut = KeyShortcut(key, setOf(this))

    public val vaadin: KeyModifier get() = KeyModifier.values()[hack]  // workaround for https://github.com/vaadin/flow/issues/5051
}

/**
 * The "Shift" key. Constant is used to build up a [KeyShortcut].
 */
public val Shift: ModifierKey = ModifierKey.Shift
/**
 * The "Ctrl" key. Constant is used to build up a [KeyShortcut].
 */
public val Ctrl: ModifierKey = ModifierKey.Ctrl

/**
 * The "Alt" key. Constant is used to build up a [KeyShortcut].
 */
public val Alt: ModifierKey = ModifierKey.Alt
/**
 * The "Alt Gr" key. Constant is used to build up a [KeyShortcut].
 */
public val AltGr: ModifierKey = ModifierKey.AltGr
/**
 * The "Windows" key. Constant is used to build up a [KeyShortcut].
 */
public val Meta: ModifierKey = ModifierKey.Meta


public infix operator fun Set<ModifierKey>.plus(key: Key): KeyShortcut = KeyShortcut(key, this)

/**
 * Denotes a keyboard shortcut, such as [Ctrl] + [Alt] + [Key.KEY_C]. Import `Key.*` then:
 * ```kotlin
 * val shortcut: KeyShortcut = Ctrl + Alt + KEY_C
 * ```
 */
public data class KeyShortcut(val key: Key, val modifierKeys: Set<ModifierKey> = setOf()) : Serializable {
    val vaadinModifiers: Array<KeyModifier> = modifierKeys.map { it.vaadin }.toTypedArray()
}

/**
 * Allows you to [add click shortcuts][ClickNotifier.addClickShortcut] such as `addClickShortcut(ModifierKey.Ctrl + ModifierKey.Alt + Key.KEY_C)`.
 *
 * When you properly import `ModifierKey.*` and `Key.*`, the call can be written as `addClickShortcut(Ctrl + Alt + KEY_C)`
 *
 * The listener is global by default (notified even if the component is not focused).
 * @param onlyWhenFocused defaults to false. If true, the shortcut is only activated when the component is focused.
 */
public fun ClickNotifier<*>.addClickShortcut(shortcut: KeyShortcut, onlyWhenFocused: Boolean = false): ShortcutRegistration = addClickShortcut(shortcut.key, *shortcut.vaadinModifiers).apply {
    if (onlyWhenFocused) {
        listenOn(this@addClickShortcut as Component)
    }
}

/**
 * Allows you to [add focus shortcuts][Focusable.addFocusShortcut] such as `addFocusShortcut(ModifierKey.Ctrl + ModifierKey.Alt + Key.KEY_C)`.
 *
 * When you properly import `ModifierKey.*` and `Key.*`, the call can be written as `addFocusShortcut(Ctrl + Alt + KEY_C)`
 */
public fun Focusable<*>.addFocusShortcut(shortcut: KeyShortcut): ShortcutRegistration = addFocusShortcut(shortcut.key, *shortcut.vaadinModifiers)

/**
 * Attaches a keyboard shortcut to given component receiver. The keyboard shortcut is only active when the component is visible
 * and attached to the UI. Ideal targets are therefore: routes (for creating a route-wide shortcut), modal dialogs,
 * root layouts, UI. Example:
 *
 * ```kotlin
 * addShortcut(ModifierKey.Ctrl + ModifierKey.Alt + Key.KEY_C) { print("hello!") }
 *
 *
 * When you properly import `ModifierKey.*` and `Key.*`, the call can be written as `addShortcut(Ctrl + Alt + KEY_C) { print("hello!") }`
 *
 * The listener is local by default (notified only when the component, or one of its children/descendants, are focused).
 */
public fun Component.addShortcut(shortcut: KeyShortcut, block: ()->Unit): ShortcutRegistration =
        Shortcuts.addShortcutListener(this, Command { block() }, shortcut.key, *shortcut.vaadinModifiers)
                .listenOn(*arrayOf(this))  // keep the spread operator to call `listenOn(Component...)` since Vaadin20+ doesn't contain `listenOn(Component)` anymore

/**
 * Allows adding shortcut on a single key, e.g.
 * ```
 * addShortcut(Key.ENTER.shortcut) { login() }
 * addShortcut(ENTER.shortcut) { login() }
 * ```
 */
public val Key.shortcut: KeyShortcut get() = KeyShortcut(this)

/**
 * Calls [block] when the user presses the ENTER key while given [TextField] is focused.
 */
public fun TextField.onEnter(block: () -> Unit) {
    addShortcut(Key.ENTER.shortcut) {
        // workaround for https://github.com/vaadin/flow/issues/17484: flush the value and call
        // the server with the new value first, so that block can see the new value properly.
        element
            .executeJs("this._onChange(new Event('dummy'));")
            .then { block() }
    }
}
