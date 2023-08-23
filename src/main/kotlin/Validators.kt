package com.github.mvysny.kaributools

import com.vaadin.flow.data.binder.Validator
import com.vaadin.flow.data.binder.ValueContext
import com.vaadin.flow.data.validator.RangeValidator

/**
 * Checks whether the validator passes or fails given [value].
 */
public fun <T> Validator<T>.isValid(value: T?): Boolean = !apply(value, ValueContext()).isError

/**
 * Workaround for Kotlin requiring non-null values for [RangeValidator.of].
 * Requires that the value is in the range `min..max`, including.
 */
public fun <T: Comparable<T>> rangeValidatorOf(errorMessage: String, min: T?, max: T?): RangeValidator<T> =
    RangeValidator(errorMessage, Comparator.nullsFirst<T>(Comparator.naturalOrder<T>()), min, max)
