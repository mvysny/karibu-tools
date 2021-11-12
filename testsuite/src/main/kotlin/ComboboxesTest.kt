package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.vaadin.flow.component.combobox.ComboBox
import kotlin.test.expect

fun DynaNodeGroup.comboboxesTests() {
    test("textAlign") {
        val cb = ComboBox<String>()
        expect(ComboBoxAlign.Left) { cb.textAlign }
        cb.textAlign = ComboBoxAlign.Center
        expect(ComboBoxAlign.Center) { cb.textAlign }
        cb.textAlign = ComboBoxAlign.Right
        expect(ComboBoxAlign.Right) { cb.textAlign }
    }
    test("small") {
        val cb = ComboBox<String>()
        expect(false) { cb.isSmall }
        cb.isSmall = true
        expect(true) { cb.isSmall }
        cb.isSmall = false
        expect(false) { cb.isSmall }
    }
    test("isHelperAboveField") {
        val cb = ComboBox<String>()
        expect(false) { cb.isSmall }
        cb.isSmall = true
        expect(true) { cb.isSmall }
        cb.isSmall = false
        expect(false) { cb.isSmall }
    }
    test("variants") {
        val cb = ComboBox<String>()
        cb.addThemeVariants(ComboBoxVariant.AlignCenter, ComboBoxVariant.Small)
        expect(ComboBoxAlign.Center) { cb.textAlign }
        expect(true) { cb.isSmall }
        cb.removeThemeVariants(ComboBoxVariant.AlignCenter, ComboBoxVariant.Small)
        expect(ComboBoxAlign.Left) { cb.textAlign }
        expect(false) { cb.isSmall }
    }
    test("dropdownWidth") {
        val cb = ComboBox<String>()
        expect(null) { cb.dropdownWidth }
        cb.dropdownWidth = "100px"
        expect("100px") { cb.dropdownWidth }
        cb.dropdownWidth = null
        expect(null) { cb.dropdownWidth }
    }
}
