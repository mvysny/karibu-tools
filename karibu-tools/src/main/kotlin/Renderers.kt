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

private val _LitRenderer_Class: Class<*>? = if (VaadinVersion.get.major >= 22) {
    Class.forName("com.vaadin.flow.data.renderer.LitRenderer")
} else { null }

private val _LitRenderer_templateExpression: Field by lazy(LazyThreadSafetyMode.PUBLICATION) {
    check(VaadinVersion.get.major >= 22) { "LitRenderer is only supported by Vaadin 22+ but we got ${VaadinVersion.get}" }
    val f = _LitRenderer_Class!!.getDeclaredField("templateExpression")
    f.isAccessible = true
    f
}

/**
 * Returns the Template set to the [Renderer]. Supports both `LitRenderer` and `PolymerRenderer`.
 * Returns "" for `PolymerRenderer` in Vaadin 24+ since `PolymerRenderer`s are no longer supported.
 */
public val Renderer<*>.template: String
    get() {
        if (VaadinVersion.get.major >= 22 && _LitRenderer_Class!!.isInstance(this)) {
            val template = _LitRenderer_templateExpression.get(this) as String?
            return template ?: ""
        }
        if (VaadinVersion.get.major >= 24) return ""
        val template: String? = _Renderer_template.get(this) as String?
        return template ?: ""
    }
