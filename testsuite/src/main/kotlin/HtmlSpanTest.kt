package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import kotlin.test.expect

fun DynaNodeGroup.htmlSpanTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        UI.getCurrent().add(HtmlSpan())
        UI.getCurrent().add(HtmlSpan("foo"))
        UI.getCurrent().add(HtmlSpan("foo<p>bar</p>baz"))
    }

    test("innerhtml") {
        val s = HtmlSpan()
        s.innerHTML = "foo"
        expect("foo") { s.innerHTML }
        s.innerHTML = "foo<p>bar</p>baz"
        expect("foo<p>bar</p>baz") { s.innerHTML }
    }
}
