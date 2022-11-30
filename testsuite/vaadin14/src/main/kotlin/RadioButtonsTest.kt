package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.getItemLabels
import com.vaadin.flow.component.radiobutton.RadioButtonGroup

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
    dataProvider = ListDataProvider2(items)
}
