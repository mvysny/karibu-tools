package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.upload.Upload
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractUploadTests() {
    @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    @Nested inner class isEnabled {
        @Test fun `enabled by default`() {
            val upload = Upload()
            upload.maxFiles = 1
            expect(true) { upload.isEnabled }
        }

        @Test fun `disable sets max files to 0`() {
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

    @Nested inner class clear {
        @Test fun smoke() {
            Upload().clear()
        }
    }

    @Test fun buttonCaption() {
        expect(null) { Upload().buttonCaption }
        val u = Upload()
        u.buttonCaption = "foo"
        expect("foo") { u.buttonCaption }
    }
}
