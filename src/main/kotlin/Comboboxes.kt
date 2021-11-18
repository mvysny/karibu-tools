package com.github.mvysny.kaributools

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import java.util.stream.Collectors
import java.util.stream.Stream

public enum class ComboBoxAlign {
    Left, Center, Right
}

/**
 * Sets/gets the current text align. See [ComboBox theme variants](https://vaadin.com/components/vaadin-combo-box/java-examples/theme-variants)
 * for more details.
 */
public var ComboBox<*>.textAlign: ComboBoxAlign
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
 * Makes the combobox smaller. See [ComboBox theme variants](https://vaadin.com/components/vaadin-combo-box/java-examples/theme-variants)
 * for more details.
 */
public var ComboBox<*>.isSmall: Boolean
    get() = element.themeList.contains("small")
    set(value) {
        element.themeList.set("small", value)
    }

/**
 * Moves the helper text above ComboBox. See [ComboBox theme variants](https://vaadin.com/components/vaadin-combo-box/java-examples/theme-variants)
 * for more details.
 */
public var ComboBox<*>.isHelperAboveField: Boolean
    get() = element.themeList.contains("helper-above-field")
    set(value) {
        element.themeList.set("helper-above-field", value)
    }

/**
 * Available combobox variants.
 */
public enum class ComboBoxVariant(public val variantName: String) {
    AlignLeft("align-left"), AlignCenter("align-center"), AlignRight("align-right"), Small("small"), HelperAboveField("helper-above-field")
}

/**
 * Adds theme [variants] to the component.
 */
public fun ComboBox<*>.addThemeVariants(vararg variants: ComboBoxVariant) {
    element.themeList.addAll(variants.map { it.variantName })
}

/**
 * Removes theme [variants] from the component.
 */
public fun ComboBox<*>.removeThemeVariants(vararg variants: ComboBoxVariant) {
    element.themeList.removeAll(variants.map { it.variantName }.toSet())
}

/**
 * Sets a custom dropdown popup overlay width. Examples `100px`, `30em` etc.
 * Set to null to use the default dropdown width.
 */
public var ComboBox<*>.dropdownWidth: String?
    get() = element.style.get("--vaadin-combo-box-overlay-width")
    set(value) {
        element.style.set("--vaadin-combo-box-overlay-width", value)
    }

/**
 * Sets a component to the `prefix` slot.
 */
public var ComboBox<*>.prefixComponent: Component?
    get() = getChildComponentInSlot("prefix")
    set(value) {
        setChildComponentToSlot("prefix", value)
    }
