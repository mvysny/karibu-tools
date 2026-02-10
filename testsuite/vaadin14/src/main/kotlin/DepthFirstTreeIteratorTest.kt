package com.github.mvysny.kaributools

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractDepthFirstTreeIteratorTests() {
    @Test fun DepthFirstTreeIterator() {
        val i = DepthFirstTreeIterator("0") { if (it.length > 2) listOf() else listOf("${it}0", "${it}1", "${it}2")}
        expectList("0", "00", "000", "001", "002", "01", "010", "011", "012", "02", "020", "021", "022") { i.asSequence().toList() }
    }

    @Test fun walk() {
        val expected = mutableListOf<Component>()
        val root = VerticalLayout().apply {
            expected.add(this)
            add(TextField("Foo").apply { expected.add(this) })
            add(HorizontalLayout().apply {
                expected.add(this)
                add(Span().apply { expected.add(this) })
            })
            add(VerticalLayout().apply { expected.add(this) })
        }
        expect(expected) { root.walk().toList() }
        expect(root) { root.walk().toList()[0] }
    }
}
