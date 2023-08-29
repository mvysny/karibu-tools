package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.menubar.MenuBar

@DynaTestDsl
fun DynaNodeGroup.menuBarUtilsTests() {
    test("smoke") {
        MenuBar().close()
        MenuBar().addIconItem(VaadinIcon.ABACUS.create()).subMenu.addIconItem(VaadinIcon.MENU.create())
        MenuBar().addIconItem(VaadinIcon.ABACUS.create(), "Foo").subMenu.addIconItem(VaadinIcon.MENU.create(), "Bar")
    }
}
