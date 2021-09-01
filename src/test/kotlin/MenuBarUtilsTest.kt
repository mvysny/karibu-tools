package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.component.menubar.MenuBar

class MenuBarUtilsTest : DynaTest({
    test("smoke") {
        MenuBar().close()
    }
})
