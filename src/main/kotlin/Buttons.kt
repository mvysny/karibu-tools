package com.github.mvysny.kaributools

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant

/**
 * Sets the button as primary. It effectively adds the [ButtonVariant.LUMO_PRIMARY] theme variant.
 */
public fun Button.setPrimary() {
    addThemeVariants(ButtonVariant.LUMO_PRIMARY)
}
