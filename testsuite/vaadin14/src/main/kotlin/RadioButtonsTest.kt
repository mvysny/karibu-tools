package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.getItemLabels
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.data.provider.DataProvider

@DynaTestDsl
fun DynaNodeGroup.radioButtonsTests() {
    test("setItemLabelGenerator") {
        val b = RadioButtonGroup<Int>()
        b.setItems2(listOf(1, 2, 3))
        b.setItemLabelGenerator { "Item #$it" }
        expectList("Item #1", "Item #2", "Item #3") { b.getItemLabels() }
    }
}

fun <T> RadioButtonGroup<T>.setItems2(items: Collection<T>) {
    // this way it's also compatible with Vaadin 18.
    if (VaadinVersion.get.isAtLeast(24)) {
        javaClass.getMethod("setItems", DataProvider::class.java)
            .invoke(this, ListDataProvider2(items.toList()))
    } else {
        dataProvider = ListDataProvider2(items)
    }
}
