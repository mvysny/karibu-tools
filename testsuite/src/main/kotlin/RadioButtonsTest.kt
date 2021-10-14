package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.radiobutton.RadioButtonGroup

fun DynaNodeGroup.radioButtonsTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("setItemLabelGenerator") {
        val b = RadioButtonGroup<Int>()
        b.setItems(1, 2, 3)
        b.setItemLabelGenerator { "Item #$it" }
        // uncomment once Karibu-Testing 1.3.5 is integrated
//        expectList("Item #1", "Item #2", "Item #3") { b.getItemLabels() }
    }
}
