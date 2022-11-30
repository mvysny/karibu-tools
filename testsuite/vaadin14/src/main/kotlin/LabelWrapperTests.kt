package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Span
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.labelWrapperTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        val labelWrapper = LabelWrapper("hello")
        UI.getCurrent().add(labelWrapper)
        expect("hello") { labelWrapper.label }
    }

    test("children") {
        LabelWrapper("Interwebz").add(Span("Foo"), Span("Bar"))
    }
}
