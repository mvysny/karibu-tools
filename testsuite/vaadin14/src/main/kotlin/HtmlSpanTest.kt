package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.UI
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractHtmlSpanTests {
    @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    @Test fun smoke() {
        UI.getCurrent().add(HtmlSpan())
        UI.getCurrent().add(HtmlSpan("foo"))
        UI.getCurrent().add(HtmlSpan("foo<p>bar</p>baz"))
    }

    @Test fun innerhtml() {
        val s = HtmlSpan()
        s.innerHTML = "foo"
        expect("foo") { s.innerHTML }
        s.innerHTML = "foo<p>bar</p>baz"
        expect("foo<p>bar</p>baz") { s.innerHTML }
    }
}
