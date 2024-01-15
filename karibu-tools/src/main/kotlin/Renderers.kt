package com.github.mvysny.kaributools

import com.vaadin.flow.data.renderer.BasicRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.function.ValueProvider
import java.lang.reflect.Field

private val _BasicRenderer_valueProvider: Field by lazy(LazyThreadSafetyMode.PUBLICATION) {
    val javaField: Field = BasicRenderer::class.java.getDeclaredField("valueProvider")
    javaField.isAccessible = true
    javaField
}

/**
 * Returns the [ValueProvider] set to [BasicRenderer].
 */
@Suppress("UNCHECKED_CAST", "ConflictingExtensionProperty")
public val <T, V> BasicRenderer<T, V>.valueProvider: ValueProvider<T, V>
    get() = _BasicRenderer_valueProvider.get(this) as ValueProvider<T, V>

private val _Renderer_template: Field by lazy(LazyThreadSafetyMode.PUBLICATION) {
    val templateF: Field = Renderer::class.java.getDeclaredField("template")
    templateF.isAccessible = true
    templateF
}

/**
 * Returns the Polymer Template set to the [Renderer]. Returns "" for Vaadin 24+ since PolymerTemplates
 * are no longer supported.
 */
public val Renderer<*>.template: String
    get() {
        if (VaadinVersion.get.major >= 24) return ""
        val template: String? = _Renderer_template.get(this) as String?
        return template ?: ""
    }
