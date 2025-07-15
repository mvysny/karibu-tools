package com.github.mvysny.kaributools

import com.vaadin.flow.component.formlayout.FormLayout
import org.junit.jupiter.api.Test

abstract class AbstractFormLayoutTests {
    @Test fun addRowBreakSmokeTest() {
        FormLayout().addRowBreak()
    }
}
