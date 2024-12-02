package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.Key.KEY_C
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.menubar.MenuBar
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
    @Nested inner class ElementUtilsTests : AbstractElementUtilsTests()
    @Nested inner class GridUtilsTests : AbstractGridUtilsTests()
    @Nested inner class IconUtilsTests : AbstractIconUtilsTests()
    @Nested inner class MenuBarUtilsTests {
        @Test fun smoke() {
            MenuBar().close()
            MenuBar().addIconItem(VaadinIcon.ABACUS.create()).subMenu.addIconItem(VaadinIcon.MENU.create())
            MenuBar().addIconItem(VaadinIcon.ABACUS.create(), "Foo").subMenu.addIconItem(VaadinIcon.MENU.create(), "Bar")
        }
    }
    @Nested inner class RouterUtilsTests : AbstractRouterUtilsTests()
    @Nested inner class ShortcutsTests {
        @Test fun smoke() {
            Button().addClickShortcut(Alt + Ctrl + KEY_C)
            Button().addFocusShortcut(Alt + Ctrl + KEY_C)
            Button().addShortcut(Alt + Ctrl + KEY_C) { println("Foo") }
        }
    }
}

@DynaTestDsl
fun DynaNodeGroup.allTests() {
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
