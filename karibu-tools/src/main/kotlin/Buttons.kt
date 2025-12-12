package com.github.mvysny.kaributools

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant

/**
 * Sets the button as primary. It effectively adds the [ButtonVariant.LUMO_PRIMARY] theme variant.
 */
public fun Button.setPrimary() {
    addThemeVariantsCompat(ButtonVariant.LUMO_PRIMARY)
}

// in Vaadin 24, the addThemeVariants no longer comes from GeneratedVaadinButton but rather from HasThemeVariants, but that interface is not in Vaadin 14.
public fun Button.addThemeVariantsCompat(vararg variants: ButtonVariant) {
    _Button_addThemeVariants.invoke(this, variants)
}
@Suppress("ObjectPropertyName")
private val _Button_addThemeVariants = Button::class.java.methods.first { it.name == "addThemeVariants" }

/**
 * Sets the button as dangerous, e.g. when you're confirming a deletion action
 * that can not be reversed.
 *
 * Example of usage:
 * ```kotlin
 * setConfirmButton(Button("Delete", VaadinIcon.Trash.create()).apply {
 *          setDanger()
 *          onClick {...}
 *      }
 * )
 * ```
 */
public fun Button.setDanger() {
    addThemeVariantsCompat(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY)
}
