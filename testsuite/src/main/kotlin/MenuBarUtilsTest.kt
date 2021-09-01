package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.menubar.MenuBar

fun DynaNodeGroup.menuBarUtilsTests() {
    test("smoke") {
        MenuBar().close()
    }
}
