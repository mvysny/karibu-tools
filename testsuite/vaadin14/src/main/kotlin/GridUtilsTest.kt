package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10._fetch
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.ItemClickEvent
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.DataChangeEvent
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
import com.vaadin.flow.data.renderer.TextRenderer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.streams.toList
import kotlin.test.expect

abstract class AbstractGridUtilsTests {
    @Nested inner class `addColumnFor tests` {
        @Test fun `grid addColumnFor works both for nullable and non-null properties`() {
            data class TestingClass(var foo: String?, var bar: String, var nonComparable: List<String>)
            Grid<TestingClass>().apply {
                addColumnFor(TestingClass::foo)   // this must compile
                addColumnFor(TestingClass::bar)   // this must compile
                addColumnFor(TestingClass::nonComparable)  // this must compile
            }
        }

        @Test fun `sets column by default to sortable`() {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
            }
            expectList("fullName") {
                grid.getColumnBy(Person::fullName).getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted }
            }
        }

        @Test fun `sorting property is set to key by default`() {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName, key = "FOO")
            }
            expectList("FOO") {
                grid.getColumnByKey("FOO").getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted }
            }
        }
        @Test fun `column header is set properly`() {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                addColumnFor(Person::alive)
                addColumnFor(Person::dateOfBirth)
            }
            expect("Full Name") { grid.getColumnBy(Person::fullName).header2 }
            expect("Alive") { grid.getColumnBy(Person::alive).header2 }
            expect("Date Of Birth") { grid.getColumnBy(Person::dateOfBirth).header2 }
        }

        @Test fun `property with renderer`() {
            val grid = Grid<Person>()
            val column = grid.addColumnFor(Person::fullName, TextRenderer { it.fullName })
            expect(column) { grid.getColumnBy(Person::fullName) }
            expect("Full Name") { column.header2 }
            expectList("fullName") { column.getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted } }
        }

        @Test fun `property name`() {
            val grid = Grid<Person>()
            val column = grid.addColumnFor<Person, String>("fullName", key = "name")
            expect(column) { grid.getColumnByKey("name") }
            expect("Full Name") { column.header2 }
            expectList("fullName") { column.getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted } }
        }

        @Test fun `property name with renderer`() {
            val grid = Grid<Person>()
            val column = grid.addColumnFor("fullName", TextRenderer { it.fullName })
            expect(column) { grid.getColumnByKey("fullName") }
            expect("Full Name") { column.header2 }
            expectList("fullName") { column.getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted } }
        }

        @Test fun `sortable = false`() {
            val grid = Grid<Person>()
            expect(false) { grid.addColumnFor(Person::fullName, sortable = false).isSortable }
            expect(false) { grid.addColumnFor(Person::fullName, TextRenderer { it.fullName }, sortable = false, key = "a").isSortable }
            expect(false) { grid.addColumnFor<Person, String>("fullName", sortable = false, key = "b").isSortable }
            expect(false) { grid.addColumnFor("fullName", TextRenderer { it.fullName }, sortable = false, key = "c").isSortable }
        }
    }

    @Nested inner class `addHierarchyColumnFor tests` {
        @Test fun `grid addHierarchyColumnFor works both for nullable and non-null properties`() {
            data class TestingClass(var foo: String?, var bar: String, var nonComparable: List<String>)
            TreeGrid<TestingClass>().apply {
                addHierarchyColumnFor(TestingClass::foo)   // this must compile
                addHierarchyColumnFor(TestingClass::bar)   // this must compile
                addHierarchyColumnFor(TestingClass::nonComparable)  // this must compile
            }
        }

        @Test fun `sets column by default to sortable`() {
            val grid = TreeGrid<Person>().apply {
                addHierarchyColumnFor(Person::fullName)
            }
            expectList("fullName") {
                grid.getColumnBy(Person::fullName).getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted }
            }
        }

        @Test fun `column header is set properly`() {
            val grid = TreeGrid<Person>().apply {
                addHierarchyColumnFor(Person::fullName)
                addHierarchyColumnFor(Person::alive)
                addHierarchyColumnFor(Person::dateOfBirth)
            }
            expect("Full Name") { grid.getColumnBy(Person::fullName).header2 }
            expect("Alive") { grid.getColumnBy(Person::alive).header2 }
            expect("Date Of Birth") { grid.getColumnBy(Person::dateOfBirth).header2 }
        }

        @Test fun `property name`() {
            val grid = TreeGrid<Person>()
            val column = grid.addHierarchyColumnFor<Person, String>("fullName")
            expect(column) { grid.getColumnByKey("fullName") }
            expect("Full Name") { column.header2 }
            expectList("fullName") { column.getSortOrder(SortDirection.ASCENDING).toList().map { it.sorted } }
        }

        @Test fun `sortable = false`() {
            val grid = TreeGrid<Person>()
            expect(false) { grid.addHierarchyColumnFor(Person::fullName, sortable = false).isSortable }
            expect(false) { grid.addHierarchyColumnFor<Person, String>("fullName", sortable = false, key = "b").isSortable }
        }
    }

    @Nested inner class treeGrid {
        private fun treeGrid() = TreeGrid<String>().apply {
            setItems(listOf("a", "b")) { if (it.length < 3) listOf("${it}1") else listOf() }
        }
        @Test fun getRootItems() {
            expectList("a", "b") { treeGrid().getRootItems() }
        }
        @Test fun expandAll() {
            val grid = treeGrid()
            grid.expandAll()
            expect(true) { listOf("a", "b", "a1", "b1").all { grid.isExpanded(it) } }
        }
        @Test fun `expandAll honors depth`() {
            val grid = treeGrid()
            grid.expandAll(0)
            expect(true) { grid.isExpanded("a") }
            expect(false) { grid.isExpanded("a1") }
        }
    }

    @Test fun refresh() {
        val person = Person(fullName = "a")
        val dp = ListDataProvider2(listOf(person))
        val grid = Grid<Person>().apply { setDataProvider(dp) }
        val events = mutableListOf<DataChangeEvent<Person>>()
        dp.addDataProviderListener { events.add(it) }
        grid.refresh()
        expect(false) { events.single() is DataChangeEvent.DataRefreshEvent<*> }
        grid.refreshItem(person)
        expect(person) { (events[1] as DataChangeEvent.DataRefreshEvent<Person>).item }
    }

    @Nested inner class sort {
        @Test fun `sorting by column also works with in-memory container`() {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                setItems2((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            grid.sort(Person::fullName.desc)
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }

        @Test fun `sorting by column also works with in-memory container 2`() {
            val grid = Grid<Person>().apply {
                addColumnFor<Person, String>("fullName")
                setItems2((0..9).map { Person(fullName = it.toString()) })
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            grid.sort(Person::fullName.desc)
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }

        @Test fun `sorting by column also works with in-memory container 3`() {
            val grid = Grid<Person>().apply {
                val fullNameColumn = addColumnFor(Person::fullName)
                setItems2((0..9).map { Person(fullName = it.toString()) })
                sort(fullNameColumn.desc)
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }

        @Test fun `sort(QuerySortOrder) honors sorting property`() {
            val grid = Grid<Person>().apply {
                val fullNameColumn = addColumnFor(Person::fullName).apply { setSortProperty("a") }
                setItems2((0..9).map { Person(fullName = it.toString()) })
                sort(QuerySortOrder("a", SortDirection.DESCENDING))
            }
            expect<Class<*>>(ListDataProvider2::class.java) { grid.dataProvider.javaClass }
            expect((9 downTo 0).map { it.toString() }) { grid._fetch(0, 1000).map { it.fullName } }
        }
    }

    @Test fun `column sort orders`() {
        val grid = Grid<Person>()
        val column = grid.addColumnFor(Person::fullName)
        grid.sort(column.asc)
        expect(listOf(column to SortDirection.ASCENDING)) { grid.sortOrder.map { it.sorted to it.direction } }
        grid.setSortOrder(listOf(column.desc))
        expect(listOf(column to SortDirection.DESCENDING)) { grid.sortOrder.map { it.sorted to it.direction } }
    }

    @Test fun `header2 setter`() {
        val column = Grid<Person>().addColumnFor(Person::fullName)
        column.header2 = "Name"
        expect("Name") { column.header2 }
    }

    @Test fun isDoubleClick() {
        // Karibu's _doubleClickItem() fakes clickCount=1, hence the hand-made event
        fun click(clickCount: Int) = ItemClickEvent(Grid<String>(), true, null, null, -1, -1, -1, -1, clickCount, 1, false, false, false, false)
        expect(false) { click(1).isDoubleClick }
        expect(true) { click(2).isDoubleClick }
    }

    @Test fun getColumnBySortProperty() {
        Grid<Person>().apply {
            val fullNameColumn = addColumnFor(Person::fullName).apply {
                setSortProperty("a", "b", "c")
            }
            expect(fullNameColumn) { getColumnBySortProperty("a") }
            expect(fullNameColumn) { getColumnBySortProperty("b") }
            expect(fullNameColumn) { getColumnBySortProperty("c") }
            assertThrows<IllegalArgumentException> { getColumnBySortProperty(Person::fullName.name) }
        }
    }

    @Test fun `column isExpand`() {
        val grid = Grid<Person>()
        val col = grid.addColumnFor(Person::alive)
        expect(1) { col.flexGrow }  // by default the flexGrow is 1
        val col2 = grid.addColumnFor(Person::fullName).apply { flexGrow = 0 }
        expect(0) { col2.flexGrow }
    }

    @Nested inner class `header cell retrieval test` {
        @Test fun `one component`() {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                appendHeaderRow().getCell(Person::fullName).component = TextField("Foo!")
            }
            expect("Foo!") {
                val tf = grid.headerRows.last().getCell(Person::fullName).component
                (tf as TextField).label
            }
            grid.headerRows.last().getCell(Person::fullName).component = null
            expect(null) { grid.headerRows.last().getCell(Person::fullName).component }
        }
        @Test fun `two components`() {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                appendHeaderRow().getCell(Person::fullName).component = TextField("Foo!")
                appendHeaderRow().getCell(Person::fullName).component = TextField("Bar!")
            }
            expect("Bar!") {
                val tf = grid.headerRows.last().getCell(Person::fullName).component
                (tf as TextField).label
            }
        }
    }

    @Nested inner class `footer cell retrieval test`() {
        @Test fun `one component`() {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                appendFooterRow().getCell(Person::fullName).component = TextField("Foo!")
            }
            expect("Foo!") {
                val tf = grid.footerRows.last().getCell(Person::fullName).component
                (tf as TextField).label
            }
            grid.footerRows.last().getCell(Person::fullName).component = null
            expect(null) { grid.footerRows.last().getCell(Person::fullName).component }
        }
        @Test fun `two components`() {
            val grid = Grid<Person>().apply {
                addColumnFor(Person::fullName)
                appendFooterRow().getCell(Person::fullName).component = TextField("Foo!")
                appendFooterRow().getCell(Person::fullName).component = TextField("Bar!")
            }
            expect("Bar!") {
                val tf = grid.footerRows.last().getCell(Person::fullName).component
                (tf as TextField).label
            }
        }
    }

    @Nested inner class header2 {
        @Test fun `simple column`() {
            val grid: Grid<Person> = Grid(Person::class.java)
            expect("") { grid.addColumn(Person::fullName).header2 }
            expect("Foo") { grid.addColumn(Person::fullName).apply { setHeader("Foo") }.header2 }
            expect("") { grid.addColumn(Person::fullName).apply { setHeader(Span("Foo")) }.header2 }
            expect("Foo") { grid.addColumn(Person::fullName).apply { setHeader("Foo"); setSortProperty("name") }.header2 }
        }

        @Test fun `joined columns`() {
            lateinit var col1: Grid.Column<Person>
            lateinit var col2: Grid.Column<Person>
            Grid(Person::class.java).apply {
                col1 = addColumn(Person::fullName).setHeader("foo")
                col2 = addColumn(Person::fullName).setHeader("bar")
                appendHeaderRow()
                prependHeaderRow().join(col1, col2).setComponent(TextField("Filter:"))
            }
            expect("foo") { col1.header2 }
            expect("bar") { col2.header2 }
        }
    }

    @Nested inner class selection {
        @Nested inner class mode {
            @Test fun `single-multi select`() {
                val g = Grid<String>()
                g.setSelectionMode(Grid.SelectionMode.NONE)
                expect(false) { g.isMultiSelect }
                expect(false) { g.isSingleSelect }
                expect(false) { g.isSelectionAllowed }
                g.setSelectionMode(Grid.SelectionMode.SINGLE)
                expect(false) { g.isMultiSelect }
                expect(true) { g.isSingleSelect }
                expect(true) { g.isSelectionAllowed }
                g.setSelectionMode(Grid.SelectionMode.MULTI)
                expect(true) { g.isMultiSelect }
                expect(false) { g.isSingleSelect }
                expect(true) { g.isSelectionAllowed }
            }
            @Test fun mode() {
                val g = Grid<String>()
                g.selectionMode = Grid.SelectionMode.NONE
                expect(Grid.SelectionMode.NONE) { g.selectionMode }
                g.selectionMode = Grid.SelectionMode.SINGLE
                expect(Grid.SelectionMode.SINGLE) { g.selectionMode }
                g.selectionMode = Grid.SelectionMode.MULTI
                expect(Grid.SelectionMode.MULTI) { g.selectionMode }
            }
        }
        @Nested inner class isEmpty {
            @Test fun `initially empty`() {
                val g = Grid<String>()
                g.setSelectionMode(Grid.SelectionMode.NONE)
                expect(true) { g.isSelectionEmpty }
                g.setSelectionMode(Grid.SelectionMode.SINGLE)
                expect(true) { g.isSelectionEmpty }
                g.setSelectionMode(Grid.SelectionMode.MULTI)
                expect(true) { g.isSelectionEmpty }
            }
            @Test fun `false on selection`() {
                val g = Grid<String>()
                g.setSelectionMode(Grid.SelectionMode.SINGLE)
                g.select("foo")
                expect(false) { g.isSelectionEmpty }
                g.deselectAll()
                expect(true) { g.isSelectionEmpty }
            }
        }
        @Nested inner class SelectedItemOrNull {
            @Test fun `initially empty`() {
                val g = Grid<String>()
                g.setSelectionMode(Grid.SelectionMode.NONE)
                expect(null) { g.selectedItemOrNull }
                g.setSelectionMode(Grid.SelectionMode.SINGLE)
                expect(null) { g.selectedItemOrNull }
                g.setSelectionMode(Grid.SelectionMode.MULTI)
                expect(null) { g.selectedItemOrNull }
            }
            @Test fun `false on selection`() {
                val g = Grid<String>()
                g.setSelectionMode(Grid.SelectionMode.SINGLE)
                g.select("foo")
                expect("foo") { g.selectedItemOrNull }
                g.deselectAll()
                expect(null) { g.selectedItemOrNull }
            }
        }
        @Test fun selectedItem() {
            val g = Grid<String>()
            assertThrows<NoSuchElementException> { g.selectedItem }
            g.select("foo")
            expect("foo") { g.selectedItem }
        }
        @Test fun `SelectionEvent isSelectionEmpty`() {
            val g = Grid<String>()
            val empty = mutableListOf<Boolean>()
            g.addSelectionListener { empty.add(it.isSelectionEmpty) }
            g.select("foo")
            g.deselectAll()
            expectList(false, true) { empty }
        }
    }

    @Test fun _internalId() {
        val g = Grid<String>()
        expect("col0") { g.addColumn { it } ._internalId }
    }
}

fun <T> Grid<T>.setItems2(items: Collection<T>) {
    // this way it's compatible both with Vaadin 14 and  Vaadin 20+.
    setDataProvider(ListDataProvider2(items))
}

/**
 * Need to have this class because of https://github.com/vaadin/flow/issues/8553
 */
class ListDataProvider2<T>(items: Collection<T>): ListDataProvider<T>(items) {
    override fun toString(): String = "ListDataProvider2{${items.size} items}"
}
