package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.getRenderedItems
import com.vaadin.flow.component.listbox.ListBox
import com.vaadin.flow.component.listbox.ListBoxBase
import com.vaadin.flow.component.listbox.MultiSelectListBox

fun DynaNodeGroup.listBoxTests() {
    group("ListBox") {
        test("setItemLabelGenerator()") {
            val lb = ListBox<String>()
            lb.setItems2("1", "2", "3")
            lb.setItemLabelGenerator { "item $it" }
            expectList("item 1", "item 2", "item 3") { lb.getRenderedItems() }
        }
    }
    group("MultiSelectListBox") {
        test("setItemLabelGenerator()") {
            val lb = MultiSelectListBox<String>()
            lb.setItems2("1", "2", "3")
            lb.setItemLabelGenerator { "item $it" }
            expectList("item 1", "item 2", "item 3") { lb.getRenderedItems() }
        }
    }
}

fun <T> ListBoxBase<*, T, *>.setItems2(vararg items: T) {
    // workaround for java.lang.NoSuchMethodError: 'void com.vaadin.flow.component.listbox.MultiSelectListBox.setItems(java.util.Collection)'
    // the setItems() method has been moved in Vaadin 22+, from HasItems to HasListDataView
    setDataProvider(ListDataProvider2(items.toList()))
}
