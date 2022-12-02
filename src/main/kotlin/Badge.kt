package com.github.mvysny.kaributools

import com.vaadin.flow.component.HasTheme
import com.vaadin.flow.component.html.Span

/**
 * The Badge component, see [Vaadin Badge Documentation](https://vaadin.com/docs/latest/components/badge)
 * for more details.
 */
public class Badge(text: String? = null) : Span(), HasTheme {
    init {
        setId(javaClass.simpleName)
        themeNames.add("badge")
        setText(text)
    }

    /**
     * Adds theme [variants] to the component.
     */
    public fun addThemeVariants(vararg variants: BadgeVariant) {
        themeNames.addAll(variants.map { it.variantName })
    }

    /**
     * Removes theme [variants] from the component.
     */
    public fun removeThemeVariants(vararg variants: BadgeVariant) {
        themeNames.removeAll(variants.map { it.variantName })
    }
}

/**
 * See [Vaadin Badge Documentation](https://vaadin.com/docs/latest/components/badge)
 * for examples for all variants and their combinations.
 */
public enum class BadgeVariant(public val variantName: String) {
    PRIMARY("primary"),
    SUCCESS("success"),
    ERROR("error"),
    CONTRAST("contrast"),
    PILL(        "pill"    ),
    SMALL("small")
}