package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import java.io.File
import java.util.*

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
}

fun File.loadAsProperties(): Properties {
    val p = Properties()
    absoluteFile.reader().use { p.load(it.buffered()) }
    return p
}
