package com.github.mvysny.kaributools

import com.vaadin.flow.router.Location
import com.vaadin.flow.router.QueryParameters

/**
 * Returns the singleton value associated with given [parameterName].
 * Returns null if there is no such parameter.
 * @throws IllegalStateException if the parameter has 2 or more values.
 */
public operator fun QueryParameters.get(parameterName: String): String? {
    val value = getValues(parameterName)
    return when {
        value.isEmpty() -> null
        value.size == 1 -> value.first()
        else -> throw IllegalStateException("Multiple values present for $parameterName: $value")
    }
}

/**
 * Returns the values associated with given [parameterName]. Returns an empty list
 * if there is no such parameter.
 */
public fun QueryParameters.getValues(parameterName: String): List<String> =
    parameters[parameterName] ?: listOf()

/**
 * Parses given query as a QueryParameters.
 * @param query the query string e.g. `foo=bar&quak=foo`; the parameters may repeat.
 */
public fun QueryParameters(query: String): QueryParameters = when {
    query.isBlank() -> QueryParameters.empty()
    else -> Location("?${query.trim('?')}").queryParameters
}
