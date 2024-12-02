package com.github.mvysny.kaributools

import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.html.Div
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractComboBoxTests {
    @Test fun textAlign() {
        val cb = ComboBox<String>()
        expect(ComboBoxAlign.Left) { cb.textAlign }
        cb.textAlign = ComboBoxAlign.Center
        expect(ComboBoxAlign.Center) { cb.textAlign }
        cb.textAlign = ComboBoxAlign.Right
        expect(ComboBoxAlign.Right) { cb.textAlign }
    }
    @Test fun small() {
        val cb = ComboBox<String>()
        expect(false) { cb.isSmall }
        cb.isSmall = true
        expect(true) { cb.isSmall }
        cb.isSmall = false
        expect(false) { cb.isSmall }
    }
    @Test fun isHelperAboveField() {
        val cb = ComboBox<String>()
        expect(false) { cb.isSmall }
        cb.isSmall = true
        expect(true) { cb.isSmall }
        cb.isSmall = false
        expect(false) { cb.isSmall }
    }
    @Test fun variants() {
        val cb = ComboBox<String>()
        cb.addThemeVariants(ComboBoxVariant.AlignCenter, ComboBoxVariant.Small)
        expect(ComboBoxAlign.Center) { cb.textAlign }
        expect(true) { cb.isSmall }
        cb.removeThemeVariants(ComboBoxVariant.AlignCenter, ComboBoxVariant.Small)
        expect(ComboBoxAlign.Left) { cb.textAlign }
        expect(false) { cb.isSmall }
    }
    @Test fun dropdownWidth() {
        val cb = ComboBox<String>()
        expect(null) { cb.dropdownWidth }
        cb.dropdownWidth = "100px"
        expect("100px") { cb.dropdownWidth }
        cb.dropdownWidth = null
        expect(null) { cb.dropdownWidth }
    }
    @Nested inner class prefixComponent {
        @Test fun `empty by default`() {
            val cb = ComboBox<String>()
            expect(null) { cb.prefixComponent }
        }
        @Test fun setting() {
            val cb = ComboBox<String>()
            val div = Div()
            cb.prefixComponent = div
            expect(div) { cb.prefixComponent }
        }
        @Test fun replacing() {
            val cb = ComboBox<String>()
            val div = Div()
            cb.prefixComponent = Div()
            cb.prefixComponent = div
            expect(div) { cb.prefixComponent }
        }
        @Test fun clearing() {
            val cb = ComboBox<String>()
            cb.prefixComponent = Div()
            cb.prefixComponent = null
            expect(null) { cb.prefixComponent }
        }
    }
}
