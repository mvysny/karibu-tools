package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.select.Select
import kotlin.test.expect

fun DynaNodeGroup.selectsTests() {
    test("textAlign") {
        val cb = Select<String>()
        expect(ComboBoxAlign.Left) { cb.textAlign }
        cb.textAlign = ComboBoxAlign.Center
        expect(ComboBoxAlign.Center) { cb.textAlign }
        cb.textAlign = ComboBoxAlign.Right
        expect(ComboBoxAlign.Right) { cb.textAlign }
    }
    test("small") {
        val cb = Select<String>()
        expect(false) { cb.isSmall }
        cb.isSmall = true
        expect(true) { cb.isSmall }
        cb.isSmall = false
        expect(false) { cb.isSmall }
    }
    test("isHelperAboveField") {
        val cb = Select<String>()
        expect(false) { cb.isSmall }
        cb.isSmall = true
        expect(true) { cb.isSmall }
        cb.isSmall = false
        expect(false) { cb.isSmall }
    }
    test("variants") {
        val cb = Select<String>()
        cb.addThemeVariants(SelectVariant.AlignCenter, SelectVariant.Small)
        expect(ComboBoxAlign.Center) { cb.textAlign }
        expect(true) { cb.isSmall }
        cb.removeThemeVariants(SelectVariant.AlignCenter, SelectVariant.Small)
        expect(ComboBoxAlign.Left) { cb.textAlign }
        expect(false) { cb.isSmall }
    }
    group("prefixComponent") {
        test("empty by default") {
            val cb = Select<String>()
            expect(null) { cb.prefixComponent }
        }
        test("setting") {
            val cb = Select<String>()
            val div = Div()
            cb.prefixComponent = div
            expect(div) { cb.prefixComponent }
        }
        test("replacing") {
            val cb = Select<String>()
            val div = Div()
            cb.prefixComponent = Div()
            cb.prefixComponent = div
            expect(div) { cb.prefixComponent }
        }
        test("clearing") {
            val cb = Select<String>()
            cb.prefixComponent = Div()
            cb.prefixComponent = null
            expect(null) { cb.prefixComponent }
        }
    }
}
