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
