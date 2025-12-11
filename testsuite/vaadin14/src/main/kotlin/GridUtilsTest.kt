package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10._fetch
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.treegrid.TreeGrid
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.QuerySortOrder
import com.vaadin.flow.data.provider.SortDirection
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
