package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._click
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.notification.Notification
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractNotificationsTests {
    @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    @Test fun text() {
        expect("postponing") { Notification.show("postponing").getText() }
        expect("") { Notification.show("").getText() }
    }

    @Test fun `no text with child components`() {
        expect("") { Notification(Span("foo")).getText() }
    }

    @Test fun `with close button`() {
        val notification = Notification.show("Close").addCloseButton()
        expect(true) { notification.isOpened }
        _expectOne<Span> { text = "Close" }
        _get<Button>()._click()
        expect(false) { notification.isOpened }
    }
}
