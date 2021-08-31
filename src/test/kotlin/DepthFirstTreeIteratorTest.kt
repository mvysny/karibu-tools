package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import kotlin.test.expect

class DepthFirstTreeIteratorTest : DynaTest({
    test("DepthFirstTreeIterator") {
        val i = DepthFirstTreeIterator("0") { if (it.length > 2) listOf() else listOf("${it}0", "${it}1", "${it}2")}
        expectList("0", "00", "000", "001", "002", "01", "010", "011", "012", "02", "020", "021", "022") { i.asSequence().toList() }
    }

    test("walk") {
        val expected = mutableListOf<Component>()
        val root = VerticalLayout().apply {
            expected.add(this)
            add(Button("Foo").apply { expected.add(this) })
            add(HorizontalLayout().apply {
                expected.add(this)
                add(Label().apply { expected.add(this) })
            })
            add(VerticalLayout().apply { expected.add(this) })
        }
        expect(expected) { root.walk().toList() }
        expect(root) { root.walk().toList()[0] }
    }
})
