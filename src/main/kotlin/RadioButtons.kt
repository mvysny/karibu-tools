package com.github.mvysny.kaributools

import com.vaadin.flow.component.ItemLabelGenerator
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.data.renderer.TextRenderer

/**
 * Sets the item label [generator] that is used to produce the strings shown
 * in the combo box for each item. By default,
 * `String.valueOf()` is used.
 *
 * Setting [RadioButtonGroup.setRenderer] removes any generator set via this function.
 */
public fun <T> RadioButtonGroup<T>.setItemLabelGenerator(generator: ItemLabelGenerator<T>) {
    setRenderer(TextRenderer(generator))
}
