import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributools.v23.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.TabSheet
import com.vaadin.flow.component.tabs.Tabs
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractTabSheetTests() {
    @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    @Nested inner class tabCount {
        @Test fun `fun zero when empty`() {
            expect(0) { TabSheet().tabCount }
        }
        @Test fun `adding 1 tab`() {
            val ts = TabSheet()
            ts.add(Tab("foo"), Span("it works!"))
            expect(1) { ts.tabCount }
        }
        @Test fun `two tabs`() {
            val ts = TabSheet()
            ts.add(Tab("foo"), Span("it works!"))
            ts.add(Tab("bar"), Span("it works 2!"))
            expect(2) { ts.tabCount }
        }
        @Test fun `Removing last tab brings count to 0`() {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            ts.remove(tab)
            expect(0) { ts.tabCount }
        }
        @Test fun `Removing all tabs brings count to 0`() {
            val ts = TabSheet()
            ts.add(Tab("foo"), Span("it works!"))
            ts.add(Tab("bar"), Span("it works 2!"))
            ts.removeAll()
            expect(0) { ts.tabCount }
        }
    }

    @Nested inner class tabs {
        @Test fun `empty when no tabs`() {
            expectList() { TabSheet().tabs }
        }
        @Test fun `adding 1 tab`() {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            expectList(tab) { ts.tabs }
        }
        @Test fun `two tabs`() {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            val tab2 = ts.add(Tab("bar"), Span("it works 2!"))
            expectList(tab, tab2) { ts.tabs }
        }
        @Test fun `Removing last tab clears the tab list`() {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            ts.remove(tab)
            expectList() { ts.tabs }
        }
        @Test fun `Removing all tabs clears the tab list`() {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            val tab2 = ts.add(Tab("bar"), Span("it works 2!"))
            ts.removeAll()
            expectList() { ts.tabs }
        }
    }

    @Test fun owner() {
        val tabs = Tabs()
        val tab2 = Tab("foo")
        tabs.add(tab2)
        expect(tabs) { tab2.owner }

        val ts = TabSheet()
        val tab = ts.add(Tab("foo"), Span("it works!"))
        expect(ts._get<Tabs>()) { tab.owner }
    }

    @Test fun ownerTabSheet() {
        val ts = TabSheet()
        val tab = ts.add(Tab("foo"), Span("it works!"))
        expect(ts) { tab.ownerTabSheet }
    }

    @Nested inner class `Tab-contents` {
        @Test fun `non-empty contents`() {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            expect<Class<*>>(Span::class.java) { tab.contents!!.javaClass }
        }
    }
    @Nested inner class `TabSheet-getTab()` {
        @Test fun `simple test`() {
            val ts = TabSheet()
            val s = Span("it works!")
            val tab = ts.add(Tab("foo"), s)
            expect(tab) { ts.getTab(s) }
        }
    }
    @Nested inner class findTabContaining {
        @Test fun `empty tabsheet`() {
            val ts = TabSheet()
            expect(null) { ts.findTabContaining(Span("bar")) }
        }

        @Test fun `simple test`() {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            expect(tab) { ts.findTabContaining(tab.contents!!) }
        }

        @Test fun `complex test`() {
            val nested = Button()
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), VerticalLayout(HorizontalLayout(nested)))
            expect(tab) { ts.findTabContaining(nested) }
        }
    }

    @Nested inner class `Tab-index` {
        @Test fun `in Tabs`() {
            val tabs = Tabs()
            val tab = Tab("foo")
            val tab2 = Tab("bar")
            tabs.add(tab, tab2)
            expect(0) { tab.index }
            expect(1) { tab2.index }
        }
        @Nested inner class `in TabSheet`() {
            @Test fun `0 for 1st tab`() {
                val ts = TabSheet()
                val tab = ts.add(Tab("foo"), Span("it works!"))
                expect(0) { tab.index }
            }
            @Test fun `two tabs`() {
                val ts = TabSheet()
                val tab = ts.add(Tab("foo"), Span("it works!"))
                val tab2 = ts.add(Tab("bar"), Span("it works 2!"))
                expect(0) { tab.index }
                expect(1) { tab2.index }
            }
        }
    }
}
