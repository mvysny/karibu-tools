package com.github.mvysny.kaributools.v22

import allTests21
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.*
import java.io.File
import java.util.*
import kotlin.test.expect

class AllTests : DynaTest({
    test("vaadin version") {
        expect(24) { VaadinVersion.get.major }
        VaadinVersion.flow // smoke test that the call doesn't fail
    }

    test("vaadin version 2") {
        val gradleProps: Properties = File("../../gradle.properties").loadAsProperties()
        val expectedVaadinVersion: String = gradleProps["vaadin24_version"] as String
        expect(expectedVaadinVersion) { VaadinVersion.get.toString().replace('-', '.') }
    }

    group("vaadin14") {
        allTests()
    }
    group("vaadin21+") {
        allTests21()
    }
})
