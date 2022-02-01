package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.button.Button

fun DynaNodeGroup.buttonsTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("setPrimary") {
        val b = Button()
        expectList() { b.themeNames.toList() }
        b.setPrimary()
        expectList("primary") { b.themeNames.toList() }
    }
}
