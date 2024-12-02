package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.data.provider.ListDataProvider
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractAllTests {
    @Nested class BrowserTimeZoneTests {
        @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
        @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

        @Test fun smoke() {
            BrowserTimeZone.fetch()
            BrowserTimeZone.get
            BrowserTimeZone.extendedClientDetails
            BrowserTimeZone.currentDateTime
        }
    }
    @Nested inner class ComponentUtilsTests : AbstractComponentUtilsTests()
    @Nested inner class ButtonsTests : AbstractButtonsTests()
    @Nested inner class DataProviderUtilsTests {
        @Test fun fetchAll() {
            val list = (0..10000).toList()
            expect(list) { ListDataProvider(list).fetchAll() }
        }
    }
    @Nested inner class depthFirstTreeIteratorTests : AbstractDepthFirstTreeIteratorTests()
    @Nested inner class DialogUtilsTests : AbstractDialogUtilsTests()
}

@DynaTestDsl
fun DynaNodeGroup.allTests() {
    group("Element Utils") {
        elementUtilsTests()
    }
    group("Grid Utils") {
        gridUtilsTests()
    }
    group("Icon Utils") {
        iconUtilsTests()
    }
    group("Menu Bar Utils") {
        menuBarUtilsTests()
    }
    group("Router Utils") {
        routerUtilsTests()
    }
    group("Shortcuts") {
        shortcutsTests()
    }
    group("Text Field Utils") {
        textFieldUtilsTests()
    }
    group("Renderers") {
        renderersTests()
    }
    group("Notifications") {
        notificationsTests()
    }
    group("Upload") {
        uploadTests()
    }
    group("AbstractLogin") {
        abstractLoginUtilsTests()
    }
    group("radio button") {
        radioButtonsTests()
    }
    group("HtmlSpan") {
        htmlSpanTests()
    }
    group("Combobox") {
        comboboxesTests()
    }
    group("LabelWrapper") {
        labelWrapperTests()
    }
    group("Select") {
        selectsTests()
    }
    group("ListBox") {
        listBoxTests()
    }
    group("Badge") {
        badgeTests()
    }
    group("Checkbox") {
        checkboxesTests()
    }
    group("Validators") {
        validatorsTests()
    }
}
