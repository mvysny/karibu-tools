package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.expectList
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.dom.Element
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.streams.toList
import kotlin.test.expect

abstract class AbstractElementUtilsTests() {
    @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    @Test fun setOrRemoveAttribute() {
        val t = Div().element
        expect(null) { t.getAttribute("foo") }
        t.setOrRemoveAttribute("foo", "bar")
        expect("bar") { t.getAttribute("foo") }
        t.setOrRemoveAttribute("foo", null)
        expect(null) { t.getAttribute("foo") }
    }

    @Nested inner class `toggle class name` {
        @Test fun add() {
            val t = Div()
            t.classNames.toggle("test")
            expect(setOf("test")) { t.classNames }
        }
        @Test fun remove() {
            val t = Div()
            t.classNames.add("test")
            t.classNames.toggle("test")
            expect(setOf<String>()) { t.classNames }
        }
    }

    @Test fun insertBefore() {
        val l = Div().element
        val first: Element = Span("first").element
        l.appendChild(first)
        val second: Element = Span("second").element
        l.insertBefore(second, first)
        expect("second, first") { l.children.toList().joinToString { it.text } }
        l.insertBefore(Span("third").element, first)
        expect("second, third, first") { l.children.toList().joinToString { it.text } }
    }

    @Test fun textRecursively2() {
        expect("foo") { Span("foo").element.textRecursively2 }
        expect("foobarbaz") {
            val div = Div()
            div.add(Span("foo"), Text("bar"), Paragraph("baz"))
            div.element.textRecursively2
        }
        expect("foo") { Element("div").apply { setProperty("innerHTML", "foo") }.textRecursively2 }
    }

    @Nested inner class getVirtualChildren {
        @Test fun `initially empty`() {
            expectList() { Div().element.getVirtualChildren() }
            expectList() { Span().element.getVirtualChildren() }
            expectList() {
                val b = Button()
                UI.getCurrent().add(b)
                b.element.getVirtualChildren()
            }
        }
        @Test fun `add virtual child`() {
            val span = Span().element
            val parent = Div()
            parent.element.appendVirtualChild(span)
            expectList(span) { parent.element.getVirtualChildren() }
        }
    }

    @Test fun getChildrenInSlot() {
        expectList() { TextField().element.getChildrenInSlot("prefix") }
        val div = Div()
        expectList(div.element) { TextField().apply { prefixComponent = div } .element.getChildrenInSlot("prefix") }
    }

    @Test fun clearSlot() {
        val tf = TextField()
        tf.prefixComponent = Div()
        tf.element.clearSlot("prefix")
        expectList() { tf.element.getChildrenInSlot("prefix") }
        expect(null) { tf.prefixComponent }
    }
}
