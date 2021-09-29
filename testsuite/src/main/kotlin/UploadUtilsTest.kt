package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.upload.Upload
import kotlin.test.expect

fun DynaNodeGroup.uploadTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("enabled by default") {
        val upload = Upload()
        upload.maxFiles = 1
        expect(true) { upload.isEnabled }
    }

    test("disable sets max files to 0") {
        val upload = Upload()
        upload.isEnabled = false
        expect(false) { upload.isEnabled }
        expect(0) { upload.maxFiles }
        upload.isEnabled = true
        expect(true) { upload.isEnabled }
        expect(1) { upload.maxFiles }
        upload.isEnabled = false
        expect(false) { upload.isEnabled }
        expect(0) { upload.maxFiles }
    }
}
