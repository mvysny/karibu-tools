package com.github.mvysny.kaributools.v22

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
        expect(22) { VaadinVersion.get.major }
        VaadinVersion.flow // smoke test that the call doesn't fail
    }

    test("vaadin version 2") {
        val gradleProps: Properties = File("../../gradle.properties").loadAsProperties()
        val expectedVaadinVersion: String = gradleProps["vaadin22_version"] as String
        expect(expectedVaadinVersion) { VaadinVersion.get.toString().replace('-', '.') }
    }

    allTests()
})
