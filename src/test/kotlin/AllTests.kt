package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest

fun DynaNodeGroup.allTests() {
    group("Browser Time Zone") {
        browserTimeZoneTests()
    }
    group("Component Utils") {
        componentUtilsTests()
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
}

class AllTest : DynaTest({
    allTests()
})
