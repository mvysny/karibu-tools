package com.github.mvysny.kaributools.v14stable

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.VaadinMeta
import com.github.mvysny.kaributools.VaadinVersion
import com.github.mvysny.kaributools.allTests
import com.github.mvysny.kaributools.loadAsProperties
import java.io.File
import java.util.*
import kotlin.test.expect

class AllTests : DynaTest({
    test("vaadin version") {
        expect(14) { VaadinMeta.version }
    }

    test("vaadin version 2") {
        val gradleProps: Properties = File("../../gradle.properties").loadAsProperties()
        val expectedVaadinVersion: String = gradleProps["vaadin14_version"] as String
        expect(expectedVaadinVersion) { VaadinVersion.get.toString() }
    }

    allTests()
})
