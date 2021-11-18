package com.github.mvysny.kaributools

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.select.Select

/**
 * Sets a component to the `prefix` slot.
 */
public var Select<*>.prefixComponent: Component?
    get() = getChildComponentInSlot("prefix")
    set(value) {
        setChildComponentToSlot("prefix", value)
    }

/**
 * Sets/gets the current text align. See [Select theme variants](https://vaadin.com/components/vaadin-select/html-examples/select-theme-variants-demos)
 * for more details.
 */
public var Select<*>.textAlign: ComboBoxAlign
    get() = when {
        element.themeList.contains("align-right") -> ComboBoxAlign.Right
        element.themeList.contains("align-center") -> ComboBoxAlign.Center
        else -> ComboBoxAlign.Left
    }
    set(value) {
        element.themeList.removeAll(
            listOf("align-left", "align-center", "align-right")
        )
        when (value) {
            ComboBoxAlign.Center -> element.themeList.add("align-center")
            ComboBoxAlign.Right -> element.themeList.add("align-right")
            ComboBoxAlign.Left -> {
            } // do nothing, left is the default alignment
        }
    }

/**
 * Makes the combobox smaller. See [Select theme variants](https://vaadin.com/components/vaadin-select/html-examples/select-theme-variants-demos)
 * for more details.
 */
public var Select<*>.isSmall: Boolean
    get() = element.themeList.contains("small")
    set(value) {
        element.themeList.set("small", value)
    }

/**
 * Moves the helper text above Select. See [Select theme variants](https://vaadin.com/components/vaadin-select/html-examples/select-theme-variants-demos)
 * for more details.
 */
public var Select<*>.isHelperAboveField: Boolean
    get() = element.themeList.contains("helper-above-field")
    set(value) {
        element.themeList.set("helper-above-field", value)
    }

/**
 * Available select variants.
 */
public enum class SelectVariant(public val variantName: String) {
    AlignLeft("align-left"), AlignCenter("align-center"), AlignRight("align-right"), Small("small"), HelperAboveField("helper-above-field")
}

/**
 * Adds theme [variants] to the component.
 */
public fun Select<*>.addThemeVariants(vararg variants: SelectVariant) {
    element.themeList.addAll(variants.map { it.variantName })
}

/**
 * Removes theme [variants] from the component.
 */
public fun Select<*>.removeThemeVariants(vararg variants: SelectVariant) {
    element.themeList.removeAll(variants.map { it.variantName }.toSet())
}

