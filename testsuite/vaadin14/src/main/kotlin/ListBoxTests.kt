package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.getRenderedItems
import com.vaadin.flow.component.listbox.ListBox
import com.vaadin.flow.component.listbox.ListBoxBase
import com.vaadin.flow.component.listbox.MultiSelectListBox
import com.vaadin.flow.data.provider.DataProvider
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

abstract class AbstractListBoxTests() {
    @Nested inner class listBox {
        @Test fun setItemLabelGenerator() {
            val lb = ListBox<String>()
            lb.setItems2("1", "2", "3")
            lb.setItemLabelGenerator { "item $it" }
            expectList("item 1", "item 2", "item 3") { lb.getRenderedItems() }
        }
    }
    @Nested inner class multiSelectListBox {
        @Test fun setItemLabelGenerator() {
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
    if (VaadinVersion.get.isAtLeast(24)) {
        javaClass.getMethod("setItems", DataProvider::class.java).invoke(this, ListDataProvider2(items.toList()))
    } else {
        setDataProvider(ListDataProvider2(items.toList()))
    }
}
