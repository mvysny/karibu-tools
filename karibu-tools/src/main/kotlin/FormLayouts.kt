package com.github.mvysny.kaributools

import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.dom.ElementFactory

/**
 * Fields can be forced to break into multiple rows using html `<br>` line break elements.
 *
 * See [Row breaks](https://vaadin.com/docs/latest/components/form-layout#row-breaks)
 * for more details.
 */
public fun FormLayout.addRowBreak() {
    // Vaadin feature request:  https://github.com/vaadin/flow-components/issues/7682
    element.appendChild(ElementFactory.createBr())
}
