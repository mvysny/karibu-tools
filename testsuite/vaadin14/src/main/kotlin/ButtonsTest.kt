package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.expectList
import com.vaadin.flow.component.button.Button
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractButtonsTests() {
    @Test fun setPrimary() {
        val b = Button()
        expectList() { b.themeNames.toList() }
        b.setPrimary()
        expectList("primary") { b.themeNames.toList() }
    }

    @Test fun `button has 'text' but no 'label'`() {
        expect("") { Button().label }
        expect("") { Button("Foo").label }
        expect("Foo") { Button("Foo").text }
    }
}
