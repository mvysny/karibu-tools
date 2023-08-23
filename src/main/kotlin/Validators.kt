package com.github.mvysny.kaributools

import com.vaadin.flow.data.binder.Validator
import com.vaadin.flow.data.binder.ValueContext

/**
 * Checks whether the validator passes or fails given [value].
 */
public fun <T> Validator<T>.isValid(value: T): Boolean = !apply(value, ValueContext()).isError
