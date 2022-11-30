package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._text
import com.vaadin.flow.component.button.Button
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.buttonsTests() {
    test("setPrimary") {
        val b = Button()
        expectList() { b.themeNames.toList() }
        b.setPrimary()
        expectList("primary") { b.themeNames.toList() }
    }

    test("button has 'text' but no 'label'") {
        expect("") { Button().label }
        expect("") { Button("Foo").label }
        expect("Foo") { Button("Foo").text }
    }
}
