package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.dialog.Dialog
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.dialogUtilsTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("no dialogs initially") {
        expectList() { getAllDialogs() }
    }

    test("simple dialog") {
        val dialog = Dialog()
        dialog.open()
        _expectOne<Dialog>()
        expectList(dialog) { getAllDialogs() }
    }

    test("closed dialog won't show up in getAllDialogs()") {
        val dialog = Dialog()
        dialog.open()
        _expectOne<Dialog>()
        dialog.close()
        _expectNone<Dialog>()
        expectList() { getAllDialogs() }
    }

    group("center") {
        test("smoke") {
            val dialog = Dialog()
            dialog.center()
            dialog.width = "250px"
            dialog.height = "250px"
            dialog.center()
        }
    }

    group("requestClose()") {
        test("no close action event") {
            val dialog = Dialog()
            dialog.open()
            dialog.requestClose()
            _expectNone<Dialog>()
            expect(false) { dialog.isOpened }
        }

        test("with close action event") {
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
