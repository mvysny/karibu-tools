package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin

fun DynaNodeGroup.browserTimeZoneTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        BrowserTimeZone.fetch()
        BrowserTimeZone.get
        BrowserTimeZone.extendedClientDetails
        BrowserTimeZone.currentDateTime
    }
}
