import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.*
import com.github.mvysny.kaributools.v23.*
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.TabSheet
import com.vaadin.flow.component.tabs.Tabs
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.tabSheetTest() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    group("tabCount") {
        test("zero when empty") {
            expect(0) { TabSheet().tabCount }
        }
        test("adding 1 tab") {
            val ts = TabSheet()
            ts.add(Tab("foo"), Span("it works!"))
            expect(1) { ts.tabCount }
        }
        test("two tabs") {
            val ts = TabSheet()
            ts.add(Tab("foo"), Span("it works!"))
            ts.add(Tab("bar"), Span("it works 2!"))
            expect(2) { ts.tabCount }
        }
        test("Removing last tab brings count to 0") {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            ts.remove(tab)
            expect(0) { ts.tabCount }
        }
        test("Removing all tabs brings count to 0") {
            val ts = TabSheet()
            ts.add(Tab("foo"), Span("it works!"))
            ts.add(Tab("bar"), Span("it works 2!"))
            ts.removeAll()
            expect(0) { ts.tabCount }
        }
    }

    group("tabs") {
        test("empty when no tabs") {
            expectList() { TabSheet().tabs }
        }
        test("adding 1 tab") {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            expectList(tab) { ts.tabs }
        }
        test("two tabs") {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            val tab2 = ts.add(Tab("bar"), Span("it works 2!"))
            expectList(tab, tab2) { ts.tabs }
        }
        test("Removing last tab clears the tab list") {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            ts.remove(tab)
            expectList() { ts.tabs }
        }
        test("Removing all tabs clears the tab list") {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            val tab2 = ts.add(Tab("bar"), Span("it works 2!"))
            ts.removeAll()
            expectList() { ts.tabs }
        }
    }

    test("owner") {
        val tabs = Tabs()
        val tab2 = Tab("foo")
        tabs.add(tab2)
        expect(tabs) { tab2.owner }

        val ts = TabSheet()
        val tab = ts.add(Tab("foo"), Span("it works!"))
        expect(ts._get<Tabs>()) { tab.owner }
    }

    test("ownerTabSheet") {
        val ts = TabSheet()
        val tab = ts.add(Tab("foo"), Span("it works!"))
        expect(ts) { tab.ownerTabSheet }
    }

    group("Tab.contents") {
        test("non-empty contents") {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            expect<Class<*>>(Span::class.java) { tab.contents!!.javaClass }
        }
    }
    group("TabSheet.getTab()") {
        test("simple test") {
            val ts = TabSheet()
            val s = Span("it works!")
            val tab = ts.add(Tab("foo"), s)
            expect(tab) { ts.getTab(s) }
        }
    }
    group("findTabContaining") {
        test("empty tabsheet") {
            val ts = TabSheet()
            expect(null) { ts.findTabContaining(Span("bar")) }
        }

        test("simple test") {
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), Span("it works!"))
            expect(tab) { ts.findTabContaining(tab.contents!!) }
        }

        test("complex test") {
            val nested = Button()
            val ts = TabSheet()
            val tab = ts.add(Tab("foo"), VerticalLayout(HorizontalLayout(nested)))
            expect(tab) { ts.findTabContaining(nested) }
        }
    }

    group("Tab.index") {
        test("in Tabs") {
            val tabs = Tabs()
            val tab = Tab("foo")
            val tab2 = Tab("bar")
            tabs.add(tab, tab2)
            expect(0) { tab.index }
            expect(1) { tab2.index }
        }
        group("in TabSheet") {
            test("0 for 1st tab") {
                val ts = TabSheet()
                val tab = ts.add(Tab("foo"), Span("it works!"))
                expect(0) { tab.index }
            }
            test("two tabs") {
                val ts = TabSheet()
                val tab = ts.add(Tab("foo"), Span("it works!"))
                val tab2 = ts.add(Tab("bar"), Span("it works 2!"))
                expect(0) { tab.index }
                expect(1) { tab2.index }
            }
        }
    }
}
