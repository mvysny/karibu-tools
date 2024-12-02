package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.dialog.Dialog
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractDialogUtilsTests() {
    @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    @Test fun `no dialogs initially`() {
        expectList() { getAllDialogs() }
    }

    @Test fun `simple dialog`() {
        val dialog = Dialog()
        dialog.open()
        _expectOne<Dialog>()
        expectList(dialog) { getAllDialogs() }
    }

    @Test fun `closed dialog won't show up in getAllDialogs()`() {
        val dialog = Dialog()
        dialog.open()
        _expectOne<Dialog>()
        dialog.close()
        _expectNone<Dialog>()
        expectList() { getAllDialogs() }
    }

    @Nested inner class center {
        @Test fun smoke() {
            val dialog = Dialog()
            dialog.center()
            dialog.width = "250px"
            dialog.height = "250px"
            dialog.center()
        }
    }

    @Nested inner class requestClose {
        @Test fun `no close action event`() {
            val dialog = Dialog()
            dialog.open()
            dialog.requestClose()
            _expectNone<Dialog>()
            expect(false) { dialog.isOpened }
        }

        @Test fun `with close action event`() {
            val dialog = Dialog()
            dialog.open()
            var listenerCalled = false
            dialog.addDialogCloseActionListener { listenerCalled = true }

            // the dialog should stay open (unless explicitly closed from the listener)
            dialog.requestClose()
            _expectOne<Dialog>()
            expect(true) { dialog.isOpened }
            expect(true) { listenerCalled }
        }
    }
}
