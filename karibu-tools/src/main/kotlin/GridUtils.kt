package com.github.mvysny.kaributools

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.grid.*
import com.vaadin.flow.component.grid.Grid.Column
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.provider.hierarchy.HierarchicalQuery
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.data.selection.SelectionEvent
import com.vaadin.flow.data.selection.SelectionModel
import com.vaadin.flow.shared.util.SharedUtil
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KProperty1
import kotlin.streams.toList

/**
 * Refreshes the Grid and re-polls for data.
 */
public fun Grid<*>.refresh() {
    dataProvider.refreshAll()
}

/**
 * [Grid] Shorthand for convenience
 */
public fun <T> Grid<T>.refreshItem(item: T) = dataProvider.refreshItem(item)

/**
 * Checks whether the grid is configured as multi-select. Returns false if the
 * grid is single-select or the selection is disabled.
 */
public val Grid<*>.isMultiSelect: Boolean get() = selectionModel.isMultiSelect
/**
 * Checks whether the grid is configured as single-select. Returns false if the
 * grid is multi-select or the selection is disabled.
 */
public val Grid<*>.isSingleSelect: Boolean get() = selectionModel.isSingleSelect

/**
 * Checks whether this model is multi-select.
 */
public val SelectionModel<*, *>.isMultiSelect: Boolean get() = this is SelectionModel.Multi<*, *>
/**
 * Checks whether this model is single-select.
 */
public val SelectionModel<*, *>.isSingleSelect: Boolean get() = this is SelectionModel.Single<*, *>

/**
 * Checks whether this is either [isMultiSelect] or [isSingleSelect].
 */
public val SelectionModel<*, *>.isSelectionAllowed: Boolean get() = isMultiSelect || isSingleSelect

/**
 * Sets or returns the current [Grid.SelectionMode].
 */
public var Grid<*>.selectionMode: Grid.SelectionMode
    get() = when {
        isSingleSelect -> Grid.SelectionMode.SINGLE
        isMultiSelect -> Grid.SelectionMode.MULTI
        else -> Grid.SelectionMode.NONE
    }
    set(value) {
        setSelectionMode(value)
    }

/**
 * Checks whether this is either [isMultiSelect] or [isSingleSelect].
 */
public val Grid<*>.isSelectionAllowed: Boolean get() = selectionModel.isSelectionAllowed

/**
 * If true, nothing is selected.
 */
public val GridSelectionModel<*>.isEmpty: Boolean get() = !firstSelectedItem.isPresent

/**
 * If true, nothing is selected.
 */
public val Grid<*>.isSelectionEmpty: Boolean get() = selectionModel.isEmpty

/**
 * Checks whether the new selection is empty.
 */
public val SelectionEvent<*, *>.isSelectionEmpty: Boolean get() = !firstSelectedItem.isPresent

/**
 * [Grid] Shorthands for convenience
 * [selectedItemOrNull], [selectedItem] are relevant if [selectionMode] is [Grid.SelectionMode.SINGLE]
 */
public val <T> Grid<T>.selectedItemOrNull: T? get() = selectionModel.firstSelectedItem.orElseGet(null)
public val <T> Grid<T>.selectedItem: T get() = selectionModel.firstSelectedItem.get()

/**
 * Adds a column for given [property]. The column key is set to the property name, so that you can look up the column
 * using [getColumnBy]. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise.
 *
 * The header title is set to the property name, converted from camelCase to Human Friendly.
 *
 * WARNING: if an in-memory data provider is used, the sorting will be performed according to the
 * outcome of the [converter]. This may not be wanted, e.g. when the converter converts
 * date to a string. In this case, it's better to use the `addColumnFor(renderer)`.
 * @param converter optionally converts the property value [V] to something else,
 * typically to a String. Use this for formatting of the value. By default, simply returns the
 * value of the property.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display, deduced from the type of the [property].
 * @return the newly created column
 */
public fun <T, V> Grid<T>.addColumnFor(
    property: KProperty1<T, V?>,
    sortable: Boolean = true,
    key: String = property.name,
    converter: (V?) -> Any? = { it }
): Grid.Column<T> =
    addColumn { converter(property.get(it)) }.apply {
        this.key = key
        if (sortable) sortProperty = property
        setHeader(SharedUtil.camelCaseToHumanFriendly(property.name))
    }

/**
 * Adds a column for given [property], using given [renderer]. The column key is
 * set to the property name, so that you can look up the column
 * using [getColumnBy]. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise. The header title is set to
 * the property name, converted from camelCase to Human Friendly.
 * @param renderer the renderer used to create the grid cell structure
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display, deduced from the type of the [property].
 * @return the newly created column
 */
public fun <T, V> Grid<T>.addColumnFor(
    property: KProperty1<T, V?>,
    renderer: Renderer<T>,
    sortable: Boolean = true,
    key: String = property.name,
): Grid.Column<T> =
    addColumn(renderer).apply {
        this.key = key
        if (sortable) sortProperty = property
        setHeader(SharedUtil.camelCaseToHumanFriendly(property.name))
    }

/**
 * Sets the property by which this column will sort. Setting this property will automatically make the column sortable.
 * You can use the [addColumnFor] which also sets the column by default to sortable.
 *
 * Example of usage:
 * ```
 * grid<Person> {
 *     addColumn({ it.name }).apply {
 *         setHeader("Name")
 *         sortProperty = Person::name
 *     }
 * }
 * ```
 */
public var <T> Grid.Column<T>.sortProperty: KProperty1<T, *>
    @Deprecated("Cannot read this property", level = DeprecationLevel.ERROR)
    get() = throw UnsupportedOperationException("Unsupported")
    set(value) {
        setSortProperty(value.name)
    }

/**
 * Retrieves the column for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @throws IllegalArgumentException if no such column exists.
 */
public fun <T> Grid<T>.getColumnBy(property: KProperty1<T, *>): Grid.Column<T> =
    getColumnByKey(property.name)
            ?: throw IllegalArgumentException("No column with key $property; available column keys: ${columns.mapNotNull { it.key }}")

/**
 * Adds a column for given [propertyName]. The column key is set to the property name, so that you can look up the column
 * using [getColumnBy]. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise. The header title is set to
 * the property name, converted from camelCase to Human Friendly.
 *
 * This method should only be used when you have a Grid backed by a Java class
 * which does not have properties exposed as [KProperty1]; for Kotlin
 * class-backed Grids you should use `addColumnFor(KProperty1)`
 *
 * WARNING: if an in-memory data provider is used, the sorting will be performed according to the
 * outcome of the [converter]. This may not be wanted, e.g. when the converter converts
 * date to a string. In this case, it's better to use the `addColumnFor(renderer)`.
 * @param converter optionally converts the property value [V] to something else,
 * typically to a String. Use this for formatting of the value. By default, simply returns the
 * value of the property.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display.
 * @return the newly created column
 */
public inline fun <reified T, reified V> Grid<T>.addColumnFor(
    propertyName: String,
    sortable: Boolean = true,
    key: String = propertyName,
    noinline converter: (V?) -> Any? = { it }
): Grid.Column<T> {
    val getter: Method = T::class.java.getGetter(propertyName)
    val column: Grid.Column<T> = addColumn { converter(V::class.java.cast(getter.invoke(it))) }
    return column.apply {
        this.key = key
        if (sortable) {
            setSortProperty(propertyName)
        }
        setHeader(SharedUtil.camelCaseToHumanFriendly(propertyName))
    }
}

/**
 * Adds a column for given [propertyName], using given [renderer]. The column key is set to the property name, so that you can look up the column
 * using [getColumnBy]. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise. The header title is set to the property name, converted from camelCase to Human Friendly.
 *
 * This method should only be used when you have a Grid backed by a Java class which does not have properties exposed as [KProperty1]; for Kotlin
 * class-backed Grids you should use `addColumnFor(KProperty1)`
 * @param renderer the renderer used to create the grid cell structure
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display, deduced from the type of the [propertyName].
 * @return the newly created column
 */
public fun <T, V> Grid<T>.addColumnFor(
    propertyName: String,
    renderer: Renderer<T>,
    sortable: Boolean = true,
    key: String = propertyName,
): Grid.Column<T> =
    addColumn(renderer).apply {
        this.key = key
        if (sortable) {
            setSortProperty(propertyName)
        }
        setHeader(SharedUtil.camelCaseToHumanFriendly(propertyName))
    }

/**
 * Returns `com.vaadin.flow.component.grid.AbstractColumn`
 */
@Suppress("ConflictingExtensionProperty")  // conflicting property is "protected"
internal val HeaderRow.HeaderCell.column: Any
    get() = _AbstractCell_getColumn.invoke(this)

private val abstractCellClass: Class<*> = Class.forName("com.vaadin.flow.component.grid.AbstractRow\$AbstractCell")
private val abstractColumnClass: Class<*> = Class.forName("com.vaadin.flow.component.grid.AbstractColumn")
private val _AbstractCell_getColumn: Method by lazy(LazyThreadSafetyMode.PUBLICATION) {
    val m: Method = abstractCellClass.getDeclaredMethod("getColumn")
    m.isAccessible = true
    m
}

/**
 * Returns `com.vaadin.flow.component.grid.AbstractColumn`
 */
@Suppress("ConflictingExtensionProperty")  // conflicting property is "protected"
private val FooterRow.FooterCell.column: Any
    get() = _AbstractCell_getColumn.invoke(this)

/**
 * Retrieves the cell for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @return the corresponding cell
 * @throws IllegalArgumentException if no such column exists.
 */
public fun HeaderRow.getCell(property: KProperty1<*, *>): HeaderRow.HeaderCell {
    val cell: HeaderRow.HeaderCell? = cells.firstOrNull { it.column.columnKey == property.name }
    require(cell != null) { "This grid has no property named ${property.name}: $cells" }
    return cell
}

private val _AbstractColumn_getBottomLevelColumn: Method by lazy(LazyThreadSafetyMode.PUBLICATION) {
    val method: Method = abstractColumnClass.getDeclaredMethod("getBottomLevelColumn")
    method.isAccessible = true
    method
}

private val Any.columnKey: String?
get() {
    abstractColumnClass.cast(this)
    val gridColumn: Grid.Column<*> = _AbstractColumn_getBottomLevelColumn.invoke(this) as Grid.Column<*>
    return gridColumn.key
}

/**
 * Retrieves the cell for given [property]; it matches [Grid.Column.getKey] to [KProperty1.name].
 * @return the corresponding cell
 * @throws IllegalArgumentException if no such column exists.
 */
public fun FooterRow.getCell(property: KProperty1<*, *>): FooterRow.FooterCell {
    val cell: FooterRow.FooterCell? = cells.firstOrNull { it.column.columnKey == property.name }
    require(cell != null) { "This grid has no property named ${property.name}: $cells" }
    return cell
}

private val _AbstractColumn_getHeaderRenderer: Method by lazy(LazyThreadSafetyMode.PUBLICATION) {
    val method: Method = abstractColumnClass.getDeclaredMethod("getHeaderRenderer")
    method.isAccessible = true
    method
}

/**
 * Returns the renderer which renders the contents of a cell. Only works for Vaadin 23 and lower.
 */
public val HeaderRow.HeaderCell.renderer: Renderer<*>?
    get() {
        check(VaadinVersion.get.major < 24) { "Vaadin 24+ cells do not have Renderer" }
        val renderer: Any = _AbstractColumn_getHeaderRenderer.invoke(column)
        return renderer as Renderer<*>?
    }

private val _AbstractColumn_getFooterRenderer: Method by lazy(LazyThreadSafetyMode.PUBLICATION) {
    val method: Method = abstractColumnClass.getDeclaredMethod("getFooterRenderer")
    method.isAccessible = true
    method
}

/**
 * Returns the renderer which renders the contents of a cell.
 */
public val FooterRow.FooterCell.renderer: Renderer<*>?
    get() {
        check(VaadinVersion.get.major < 24) { "Vaadin 24+ cells do not have Renderer" }
        val renderer = _AbstractColumn_getFooterRenderer.invoke(column)
        return renderer as Renderer<*>?
    }

/**
 * Returns or sets the component in grid's footer cell.
 * Returns `null` if the cell contains String, something else than a component or
 * nothing at all.
 */
public var FooterRow.FooterCell.component: Component?
    get() {
        if (VaadinVersion.get.major >= 24) {
            return abstractCellClass.getDeclaredMethod("getComponent").invoke(this) as Component?
        }
        val cr: ComponentRenderer<*, *> = (renderer as? ComponentRenderer<*, *>) ?: return null
        return cr.createComponent(null)
    }
    set(value) {
        setComponent(value)
    }

private val gridSorterComponentRendererClass: Class<*>? = try {
    Class.forName("com.vaadin.flow.component.grid.GridSorterComponentRenderer")
} catch (e: ClassNotFoundException) {
    // Vaadin 18.0.3+ and Vaadin 14.5.0+ doesn't contain this class anymore and simply uses ComponentRenderer
    null
}
private val _GridSorterComponentRenderer_component: Field? =
    if (gridSorterComponentRendererClass == null) { null } else {
        val field = gridSorterComponentRendererClass.getDeclaredField("component")
        field.isAccessible = true
        field
    }

/**
 * Returns or sets the component in grid's header cell.
 * Returns `null` if the cell contains String, something else than a component or
 * nothing at all.
 */
@Suppress("UNCHECKED_CAST")
public var HeaderRow.HeaderCell.component: Component?
    get() {
        if (VaadinVersion.get.major >= 24) {
            return abstractCellClass.getDeclaredMethod("getComponent").invoke(this) as Component?
        }
        val r: Renderer<*>? = renderer
        if (gridSorterComponentRendererClass != null && gridSorterComponentRendererClass.isInstance(r)) {
            return _GridSorterComponentRenderer_component!!.get(r) as Component?
        }
        if (r is ComponentRenderer<*, *>) {
            return (r as ComponentRenderer<*, Any?>).createComponent(null)
        }
        return null
    }
    set(value) {
        setComponent(value)
    }

/**
 * Forces a defined sort [order] for the columns in the Grid. Setting
 * empty list resets the ordering of all columns.
 * Columns not mentioned in the list are reset to the unsorted state.
 *
 * For Grids with multi-sorting, the index of a given column inside the list
 * defines the sort priority. For example, the column at index 0 of the list
 * is sorted first, then on the index 1, and so on.
 *
 * Exampe of usage:
 * ```
 * grid<Person> {
 *   val nameColumn = addColumnFor(Person::name)
 *   sort(nameColumn.asc)
 * }
 * ```
 * @param order
 *            the list of sort orders to set on the client, or
 *            <code>null</code> to reset any sort orders.
 * @see [Grid.setMultiSort]
 * @see [Grid.getSortOrder]
*/
public fun <T> Grid<T>.sort(vararg order: GridSortOrder<T>) {
    sort(order.toList())
}

/**
 * Alias for [sort]. A setter which merely accompanies the [Grid.getSortOrder] getter.
 */
public fun <T> Grid<T>.setSortOrder(order: List<GridSortOrder<T>>) {
    sort(order)
}

/**
 * Forces a defined sort [criteria] for the columns in the Grid. Setting
 * empty list resets the ordering of all columns.
 * Columns not mentioned in the list are reset to the unsorted state.
 *
 * For Grids with multi-sorting, the index of a given column inside the list
 * defines the sort priority. For example, the column at index 0 of the list
 * is sorted first, then on the index 1, and so on.
 *
 * Exampe of usage:
 * ```
 * grid<Person> {
 *   addColumnFor(Person::name)
 *   sort(Person::name.asc)
 * }
 * ```
 */
public fun <T> Grid<T>.sort(vararg criteria: QuerySortOrder) {
    // check that columns are sortable
    val crit: List<GridSortOrder<T>> = criteria.map { sortOrder ->
        val col: Column<T> = getColumnBySortOrder(sortOrder)
        require(col.isSortable) { "Column for ${sortOrder.sorted} is not marked sortable" }
        GridSortOrder(col, sortOrder.direction)
    }

    sort(crit)
}

/**
 * Returns Grid column having [QuerySortOrder.sorted] as one of its sort properties.
 *
 * By default, column's sort property is set to [Column.getKey] but can be changed via [Column.setSortProperty].
 * @throws IllegalArgumentException if there's no such column.
 */
public fun <T> Grid<T>.getColumnBySortOrder(sortOrder: QuerySortOrder): Column<T> = getColumnBySortProperty(sortOrder.sorted)

/**
 * Returns Grid column having given [sortProperty].
 *
 * By default, column's sort property is set to [Column.getKey] but can be changed via [Column.setSortProperty].
 * @throws IllegalArgumentException if there's no such column.
 */
public fun <T> Grid<T>.getColumnBySortProperty(sortProperty: String): Column<T> {
    val column = columns.firstOrNull {  column ->
        column.getSortOrder(SortDirection.ASCENDING).anyMatch { it.sorted == sortProperty }
    }
    requireNotNull(column) {
        "No column with sort order '${sortProperty}'; available column sort orders: ${columns.map { column -> column.getSortOrder(SortDirection.ASCENDING).map { it.sorted } .toList() }}"
    }
    return column
}

/**
 * Creates a [GridSortOrder] sorting this column ascending.
 */
public val <T> Grid.Column<T>.asc: GridSortOrder<T> get() = GridSortOrder(this, SortDirection.ASCENDING)
/**
 * Creates a [GridSortOrder] sorting this column descending.
 */
public val <T> Grid.Column<T>.desc: GridSortOrder<T> get() = GridSortOrder(this, SortDirection.DESCENDING)

/**
 * Fetches the root items of a tree grid.
 */
public fun <T> TreeGrid<T>.getRootItems(): List<T> =
    dataProvider.fetch(HierarchicalQuery(null, null)).toList()

/**
 * Expands all nodes. May invoke massive data loading.
 */
@JvmOverloads
public fun <T> TreeGrid<T>.expandAll(depth: Int = 100) {
    expandRecursively(getRootItems(), depth)
}

private fun Any.gridAbstractHeaderGetHeader(): String {
    check(VaadinVersion.get.major < 24) { "Vaadin 24+ cells do not have Renderer" }
    // nasty reflection. Added a feature request to have this: https://github.com/vaadin/vaadin-grid-flow/issues/567
    val e: Renderer<*>? = _AbstractColumn_getHeaderRenderer.invoke(this) as Renderer<*>?
    return e?.template ?: ""
}

private fun Component.gridColumnGetHeaderTextVaadin24(): String {
    val m = abstractColumnClass.getDeclaredMethod("getHeaderText")
    m.isAccessible = true
    return m.invoke(this) as String? ?: ""
}

/**
 * Sets and retrieves the column header as set by [Grid.Column.setHeader] (String).
 * The result value is undefined if a component has been set as the header.
 */
public var <T> Grid.Column<T>.header2: String
    get() {
        if (VaadinVersion.get.major >= 24) {
            var result: String = gridColumnGetHeaderTextVaadin24()
            if (result.isBlank()) {
                // in case of grouped cells, the header is stored in a parent ColumnGroup.
                val parent: Component? = parent.orElse(null)
                if (parent != null && parent.javaClass.name == "com.vaadin.flow.component.grid.ColumnGroup" && parent.children.count() == 1L) {
                    result = parent.gridColumnGetHeaderTextVaadin24()
                }
            }
            return result
        }
        var result: String = gridAbstractHeaderGetHeader()
        if (result.isEmpty()) {
            // in case of grouped cells, the header is stored in a parent ColumnGroup.
            val parent: Component? = parent.orElse(null)
            if (parent != null && parent.javaClass.name == "com.vaadin.flow.component.grid.ColumnGroup" && parent.children.count() == 1L) {
                result = parent.gridAbstractHeaderGetHeader()
            }
        }
        return result
    }
    set(value) {
        setHeader(value)
    }

private val _Column_getInternalId: Method by lazy(LazyThreadSafetyMode.PUBLICATION) {
    val m = Grid.Column::class.java.getDeclaredMethod("getInternalId")
    m.isAccessible = true
    m
}

/**
 * Returns the column's Internal ID. Not related to [Grid.Column.getKey] in any way.
 * Auto-generated by the Grid. Not really useful; mostly used internally by Vaadin.
 */
public val Grid.Column<*>._internalId: String
    get() = _Column_getInternalId.invoke(this) as String

public val ItemClickEvent<*>.isDoubleClick: Boolean
    get() = clickCount >= 2

/**
 * Adds a column for given [property]. The column key is set to the property name, so that you can look up the column
 * using [getColumnBy]. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise.
 *
 * The header title is set to the property name, converted from camelCase to Human Friendly.
 *
 * WARNING: if an in-memory data provider is used, the sorting will be performed according to the
 * outcome of the [converter]. This may not be wanted, e.g. when the converter converts
 * date to a string. In this case, it's better to use the `addColumnFor(renderer)`.
 * @param converter optionally converts the property value [V] to something else,
 * typically to a String. Use this for formatting of the value. By default, simply returns the
 * value of the property.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display, deduced from the type of the [property].
 * @return the newly created column
 */
public fun <T, V> TreeGrid<T>.addHierarchyColumnFor(
    property: KProperty1<T, V?>,
    sortable: Boolean = true,
    key: String = property.name,
    converter: (V?) -> Any? = { it }
): Column<T> =
    addHierarchyColumn { converter(property.get(it)) }.apply {
        this.key = key
        if (sortable) sortProperty = property
        setHeader(SharedUtil.camelCaseToHumanFriendly(property.name))
    }

/**
 * Adds a column for given [propertyName]. The column key is set to the property name, so that you can look up the column
 * using [getColumnBy]. The column is also by default set to sortable
 * unless the [sortable] parameter is set otherwise. The header title is set to
 * the property name, converted from camelCase to Human Friendly.
 *
 * This method should only be used when you have a Grid backed by a Java class
 * which does not have properties exposed as [KProperty1]; for Kotlin
 * class-backed Grids you should use `addColumnFor(KProperty1)`
 *
 * WARNING: if an in-memory data provider is used, the sorting will be performed according to the
 * outcome of the [converter]. This may not be wanted, e.g. when the converter converts
 * date to a string. In this case, it's better to use the `addColumnFor(renderer)`.
 * @param converter optionally converts the property value [V] to something else,
 * typically to a String. Use this for formatting of the value. By default, simply returns the
 * value of the property.
 * @param T the type of the bean stored in the Grid
 * @param V the value that the column will display.
 * @return the newly created column
 */
public inline fun <reified T, reified V> TreeGrid<T>.addHierarchyColumnFor(
    propertyName: String,
    sortable: Boolean = true,
    key: String = propertyName,
    noinline converter: (V?) -> Any? = { it }
): Column<T> {
    val getter: Method = T::class.java.getGetter(propertyName)
    val column: Column<T> = addHierarchyColumn { converter(V::class.java.cast(getter.invoke(it))) }
    return column.apply {
        this.key = key
        if (sortable) {
            setSortProperty(propertyName)
        }
        setHeader(SharedUtil.camelCaseToHumanFriendly(propertyName))
    }
}
