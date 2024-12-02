package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Span
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractLabelWrapperTests() {
    @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    @Test fun smoke() {
        val labelWrapper = LabelWrapper("hello")
        UI.getCurrent().add(labelWrapper)
        expect("hello") { labelWrapper.label }
    }

    @Test fun children() {
        LabelWrapper("Interwebz").add(Span("Foo"), Span("Bar"))
    }
}
