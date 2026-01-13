package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._text
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Input
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.textfield.BigDecimalField
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.streams.toList
import kotlin.test.expect

@Suppress("DEPRECATION", "USELESS_CAST")
abstract class AbstractComponentUtilsTests() {
    @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    @Nested inner class removeFromParent {
        @Test fun `component with no parent`() {
            val t = Text("foo")
            t.removeFromParent()
            expect(null) { t.parent.orElse(null) }
        }
        @Test fun `nested component`() {
            val fl = FlexLayout().apply { add(Button("foo")) }
            val label = fl.getComponentAt(0)
            expect(fl) { label.parent.get() }
            label.removeFromParent()
            expect(null) { label.parent.orElse(null) }
            expect(0) { fl.componentCount }
        }
        @Test fun reattach() {
            val fl = FlexLayout().apply { add(Button("foo")) }
            val label = fl.getComponentAt(0)
            label.removeFromParent()
            fl.add(label)
            expect(fl) { label.parent.orElse(null) }
            expect(1) { fl.componentCount }
        }
    }

    @Test fun serverClick() {
        val b = Button()
        var clicked = 0
        b.addClickListener { clicked++ }
        b.serverClick()
        expect(1) { clicked }
    }

    @Test fun tooltip() {
        val b = Button()
        expect(null) { b.tooltip }
        b.tooltip = ""
        expect("") { b.tooltip }
        b.tooltip = "foo"
        expect("foo") { b.tooltip }
        b.tooltip = null
        expect(null) { b.tooltip }
    }

    @Test fun ariaLabel() {
        val b = Button()
        expect(null) { b.ariaLabel }
        b.ariaLabel = ""
        expect("") { b.ariaLabel }
        b.ariaLabel = "foo"
        expect("foo") { b.ariaLabel }
        b.ariaLabel = null
        expect(null) { b.ariaLabel }
    }

    @Test fun `addContextMenuListener smoke`() {
        Button().addContextMenuListener({})
    }

    @Nested inner class findAncestor {
        @Test fun `null on no parent`() {
            expect(null) { Button().findAncestor { false } }
        }
        @Test fun `null on no acceptance`() {
            val button = Button()
            UI.getCurrent().add(button)
            expect(null) { button.findAncestor { false } }
        }
        @Test fun `finds UI`() {
            val button = Button()
            UI.getCurrent().add(button)
            expect(UI.getCurrent()) { button.findAncestor { it is UI } }
        }
        @Test fun `doesn't find self`() {
            val button = Button()
            UI.getCurrent().add(button)
            expect(UI.getCurrent()) { button.findAncestor { true } }
        }
    }

    @Nested inner class findAncestorOrSelf {
        @Test fun `null on no parent`() {
            expect(null) { Button().findAncestorOrSelf { false } }
        }
        @Test fun `null on no acceptance`() {
            val button = Button()
            UI.getCurrent().add(button)
            expect(null) { button.findAncestorOrSelf { false } }
        }
        @Test fun `finds self`() {
            val button = Button()
            UI.getCurrent().add(button)
            expect(button) { button.findAncestorOrSelf { true } }
        }
    }

    @Test fun isNestedIn() {
        expect(false) { Button().isNestedIn(UI.getCurrent()) }
        val button = Button()
        UI.getCurrent().add(button)
        expect(true) { button.isNestedIn(UI.getCurrent()) }
    }

    @Test fun isAttached() {
        expect(true) { UI.getCurrent().isAttached() }
        expect(false) { Button("foo").isAttached() }
        expect(true) {
            val button = Button()
            UI.getCurrent().add(button)
            button.isAttached()
        }
        UI.getCurrent().close()
        expect(true) { UI.getCurrent().isAttached() }
    }

    @Test fun insertBefore() {
        val l = HorizontalLayout()
        val first = Span("first")
        l.addComponentAsFirst(first)
        val second = Span("second")
        l.insertBefore(second, first)
        expect("second, first") { l.children.toList().map { it._text } .joinToString() }
        l.insertBefore(Span("third"), first)
        expect("second, third, first") { l.children.toList().map { it._text } .joinToString() }
    }

    @Test fun hasChildren() {
        val l = HorizontalLayout()
        expect(false) { l.hasChildren }
        l.addComponentAsFirst(Span("first"))
        expect(true) { l.hasChildren }
        l.removeAll()
        expect(false) { l.hasChildren }
    }

    @Test fun isNotEmpty() {
        val l = HorizontalLayout()
        expect(false) { l.isNotEmpty }
        l.addComponentAsFirst(Span("first"))
        expect(true) { l.isNotEmpty }
        l.removeAll()
        expect(false) { l.isNotEmpty }
    }

    @Test fun isEmpty() {
        val l = HorizontalLayout()
        expect(true) { l.isEmpty }
        l.addComponentAsFirst(Span("first"))
        expect(false) { l.isEmpty }
        l.removeAll()
        expect(true) { l.isEmpty }
    }

    @Nested inner class classnames2 {
        @Test fun addClassNames2() {
            val div = Div().apply { addClassNames2("foo  bar    baz") }
            expect(true) {
                div.classNames.containsAll(listOf("foo", "bar", "baz"))
            }
        }
        @Test fun `addClassNames2(vararg)`() {
            val div = Div().apply { addClassNames2("foo  bar    baz", "  one  two") }
            expect(true) {
                div.classNames.containsAll(listOf("foo", "bar", "baz", "one", "two"))
            }
        }
        @Test fun setClassNames2() {
            val div = Div().apply { addClassNames2("foo  bar    baz", "  one  two") }
            div.setClassNames2("  three four  ")
            expect(true) {
                div.classNames.containsAll(listOf("three", "four"))
            }
        }
        @Test fun `setClassNames2(vararg)`() {
            val div = Div().apply { addClassNames2("foo  bar    baz", "  one  two") }
            div.setClassNames2("  three ", "four  ")
            expect(true) {
                div.classNames.containsAll(listOf("three", "four"))
            }
        }
        @Test fun removeClassNames2() {
            val div = Div().apply { addClassNames2("foo  bar    baz", "  one  two") }
            div.removeClassNames2("  bar baz  ")
            expect(true) {
                div.classNames.containsAll(listOf("foo", "one", "two"))
            }
        }
        @Test fun `removeClassNames2(vararg)`() {
            val div = Div().apply { addClassNames2("foo  bar    baz", "  one  two") }
            div.removeClassNames2("  bar ", "baz  ")
            expect(true) {
                div.classNames.containsAll(listOf("foo", "one", "two"))
            }
        }
    }

    @Test fun placeholder() {
        var c: Component = TextField().apply { placeholder = "foo" }
        expect("foo") { (c as Component).placeholder }
        (c as Component).placeholder = ""
        expect("") { (c as Component).placeholder }
        c = TextArea().apply { placeholder = "foo" }
        expect("foo") { (c as Component).placeholder }
        (c as Component).placeholder = ""
        expect("") { (c as Component).placeholder }
        c = Button() // doesn't support placeholder
        expect(null) { (c as Component).placeholder }
        val ex = assertThrows<IllegalStateException> {
            (c as Component).placeholder = "foo"
        }
        expect("Button doesn't support setting placeholder") { ex.message }
        c = Input() // test incoming incompatible change in getPlaceholder() from Optional<String> to String
        expect("") { (c as Component).placeholder ?: "" }
        (c as Component).placeholder = "foo"
        expect("foo") { (c as Component).placeholder }

        c = NumberField("", "foo")
        expect("foo") { (c as Component).placeholder }
        (c as Component).placeholder = ""
        expect("") { (c as Component).placeholder }

        c = BigDecimalField("", "foo")
        expect("foo") { (c as Component).placeholder }
        (c as Component).placeholder = ""
        expect("") { (c as Component).placeholder }

        c = IntegerField("", "foo")
        expect("foo") { (c as Component).placeholder }
        (c as Component).placeholder = ""
        expect("") { (c as Component).placeholder }
    }

    @Nested inner class label {
        @Test fun testTextField() {
            val c: Component = TextField()
            expect("") { c.label }
            c.label = "foo"
            expect("foo") { c.label }
            c.label = ""
            expect("") { c.label }
        }
        @Test fun testCheckbox() {
            val c: Component = Checkbox()
            expect("") { c.label }
            c.label = "foo"
            expect("foo") { c.label }
            expect("foo") { (c as Checkbox).label }
            c.label = ""
            expect("") { c.label }
            expect("") { (c as Checkbox).label }
        }
        @Test fun testTab() {
            val c: Component = Tab()
            expect("") { c.label }
            c.label = "foo"
            expect("foo") { c.label }
            expect("foo") { (c as Tab).label }
            c.label = ""
            expect("") { c.label }
            expect("") { (c as Tab).label }
        }
    }

    @Test fun caption() {
        var c: Component = Button("foo")
        expect("foo") { c.caption }
        c.caption = ""
        expect("") { c.caption }
        c = Checkbox().apply { caption = "foo" }
        expect("foo") { c.caption }
        c.caption = ""
        expect("") { c.caption }
        expect("") { FormLayout.FormItem().label }
        val fl = FormLayout()
        c = fl.addFormItem(Button(), "foo")
        expect("foo") { c.caption }
    }

    @Test fun `Button-caption`() {
        val c = Button("foo")
        expect("foo") { c.caption }
        c.caption = ""
        expect("") { c.caption }
    }
}
