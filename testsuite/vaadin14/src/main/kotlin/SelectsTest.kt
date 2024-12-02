package com.github.mvysny.kaributools

import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.select.Select
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractSelectsTests {
    @Test fun textAlign() {
        val cb = Select<String>()
        expect(ComboBoxAlign.Left) { cb.textAlign }
        cb.textAlign = ComboBoxAlign.Center
        expect(ComboBoxAlign.Center) { cb.textAlign }
        cb.textAlign = ComboBoxAlign.Right
        expect(ComboBoxAlign.Right) { cb.textAlign }
    }
    @Test fun small() {
        val cb = Select<String>()
        expect(false) { cb.isSmall }
        cb.isSmall = true
        expect(true) { cb.isSmall }
        cb.isSmall = false
        expect(false) { cb.isSmall }
    }
    @Test fun isHelperAboveField() {
        val cb = Select<String>()
        expect(false) { cb.isSmall }
        cb.isSmall = true
        expect(true) { cb.isSmall }
        cb.isSmall = false
        expect(false) { cb.isSmall }
    }
    @Test fun variants() {
        val cb = Select<String>()
        cb.addThemeVariants(SelectVariant.AlignCenter, SelectVariant.Small)
        expect(ComboBoxAlign.Center) { cb.textAlign }
        expect(true) { cb.isSmall }
        cb.removeThemeVariants(SelectVariant.AlignCenter, SelectVariant.Small)
        expect(ComboBoxAlign.Left) { cb.textAlign }
        expect(false) { cb.isSmall }
    }
    @Nested inner class prefixComponent {
        @Test fun `empty by default`() {
            val cb = Select<String>()
            expect(null) { cb.prefixComponent }
        }
        @Test fun setting() {
            val cb = Select<String>()
            val div = Div()
            cb.prefixComponent = div
            expect(div) { cb.prefixComponent }
        }
        @Test fun replacing() {
            val cb = Select<String>()
            val div = Div()
            cb.prefixComponent = Div()
            cb.prefixComponent = div
            expect(div) { cb.prefixComponent }
        }
        @Test fun clearing() {
            val cb = Select<String>()
            cb.prefixComponent = Div()
            cb.prefixComponent = null
            expect(null) { cb.prefixComponent }
        }
    }
}
