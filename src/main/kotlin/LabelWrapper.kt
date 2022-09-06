package com.github.mvysny.kaributools

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasComponents
import com.vaadin.flow.component.HasSize
import com.vaadin.flow.component.customfield.CustomField

/**
 * Used to show a label on top of a component which doesn't show a [Component.label] itself.
 *
 * **Used in highly specific situations only:** for example when you need to add a label
 * to a `HorizontalLayout`.
 *
 * Whenever possible, you should add your components into a `FormLayout`
 * instead, via the `FormLayout.addFormItem()` which
 * supports labels for all components. Alternatively, you can emulate labels by wrapping labels in a `H2`/`H3`/`H4`/`H5`/`H6`
 * then styling them accordingly.
 */
public class LabelWrapper(label: String) : CustomField<Void?>(), HasComponents, HasSize {
    init {
        setLabel(label)
    }

    override fun setPresentationValue(newPresentationValue: Void?) {
        // ignore; this component only serves to add a label to the wrapped contents
    }

    override fun generateModelValue(): Void? {
        // ignore - this component only serves to add a label to the wrapped contents")
        return null
    }

    public override fun add(vararg components: Component) {
        super<CustomField>.add(*components)
    }

    public override fun remove(vararg components: Component) {
        super<CustomField>.remove(*components)
    }
}
