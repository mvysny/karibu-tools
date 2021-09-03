package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._click
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.notification.Notification
import kotlin.test.expect

fun DynaNodeGroup.notificationsTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("text") {
        expect("postponing") { Notification.show("postponing").getText() }
        expect("") { Notification.show("").getText() }
    }

    test("no text with child components") {
        expect("") { Notification(Span("foo")).getText() }
    }

    test("with close button") {
        val notification = Notification.show("Close").addCloseButton()
        expect(true) { notification.isOpened }
        _expectOne<Span> { text = "Close" }
        _get<Button>()._click()
        expect(false) { notification.isOpened }
    }
}
