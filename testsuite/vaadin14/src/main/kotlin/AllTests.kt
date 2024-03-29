package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import org.tomlj.Toml
import org.tomlj.TomlParseResult
import java.io.File
import java.util.*

@DynaTestDsl
fun DynaNodeGroup.allTests() {
    group("Browser Time Zone") {
        browserTimeZoneTests()
    }
    group("Component Utils") {
        componentUtilsTests()
    }
    group("Buttons") {
        buttonsTests()
    }
    group("Data Provider Utils") {
        dataProviderUtilsTests()
    }
    group("Depth First Tree Iterator") {
        depthFirstTreeIteratorTests()
    }
    group("Dialog Utils") {
        dialogUtilsTests()
    }
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
