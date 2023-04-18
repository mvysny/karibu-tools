package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.checkboxesTests() {
    test("serverClick()") {
        val cb = Checkbox()
        var clicked = 0
        cb.addClickListener { clicked++ }
        cb.serverClick()
        expect(1) { clicked }
    }
}
