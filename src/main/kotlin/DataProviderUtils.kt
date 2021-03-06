package com.github.mvysny.kaributools

import com.vaadin.flow.data.provider.DataProvider
import com.vaadin.flow.data.provider.Query
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import kotlin.reflect.KProperty1
import kotlin.streams.toList

/**
 * Returns all items provided by this data provider as an eager list. Careful with larger data!
 */
public fun <T: Any, F> DataProvider<T, F>.fetchAll(): List<T> = fetch(Query()).toList()

public val <T> KProperty1<T, *>.asc: QuerySortOrder get() = QuerySortOrder(name, SortDirection.ASCENDING)
public val <T> KProperty1<T, *>.desc: QuerySortOrder get() = QuerySortOrder(name, SortDirection.DESCENDING)
