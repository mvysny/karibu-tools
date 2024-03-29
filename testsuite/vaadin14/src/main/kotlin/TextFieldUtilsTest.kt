package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.textfield.BigDecimalField
import com.vaadin.flow.component.textfield.EmailField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField

@DynaTestDsl
fun DynaNodeGroup.textFieldUtilsTests() {
    group("TextField") {
        test("smoke") {
            TextField().selectAll()
            TextField().selectNone()
            TextField().setCursorLocation(3)
        }
    }

    group("TextArea") {
        test("smoke") {
            TextArea().selectAll()
            TextArea().selectNone()
            TextArea().setCursorLocation(3)
        }
    }

    group("EmailField") {
        test("smoke") {
            EmailField().selectAll()
            EmailField().selectNone()
            EmailField().setCursorLocation(3)
        }
    }
}
