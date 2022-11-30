package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.flow.component.menubar.MenuBar

@DynaTestDsl
fun DynaNodeGroup.menuBarUtilsTests() {
    test("smoke") {
        MenuBar().close()
    }
}
