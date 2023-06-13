package com.github.mvysny.kaributools.v22

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.*
import loadAsProperties
import java.io.File
import java.util.*
import kotlin.test.expect

class AllTests : DynaTest({
    test("hilla version") {
        val gradleProps: Properties = File("../../gradle.properties").loadAsProperties()
        val expectedHillaVersion: String = gradleProps["hilla_version"] as String
        expect(expectedHillaVersion) { VaadinVersion.hilla.toString() }
    }
})
