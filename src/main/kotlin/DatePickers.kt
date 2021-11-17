package com.github.mvysny.kaributools

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.datepicker.DatePicker

/**
 * Sets a component to the `prefix` slot.
 */
public var DatePicker.prefixComponent: Component?
    get() = getChildComponentInSlot("prefix")
    set(value) {
        setChildComponentToSlot("prefix", value)
    }
