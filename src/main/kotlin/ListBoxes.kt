package com.github.mvysny.kaributools

import com.vaadin.flow.component.ItemLabelGenerator
import com.vaadin.flow.component.listbox.ListBoxBase
import com.vaadin.flow.data.renderer.TextRenderer

/**
 * Sets the item label [generator] that is used to produce the strings shown
 * in the combo box for each item. By default,
 * `String.valueOf()` is used.
 *
 * Setting [ListBoxBase.setRenderer] removes any generator set via this function.
 */
public fun <T> ListBoxBase<*, T, *>.setItemLabelGenerator(generator: ItemLabelGenerator<T>) {
    // workaround for https://github.com/vaadin/platform/issues/2601
    setRenderer(TextRenderer(generator))
}
