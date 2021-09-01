package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._expectNone
import com.github.mvysny.kaributesting.v10._expectOne
import com.vaadin.flow.component.dialog.Dialog

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
}
