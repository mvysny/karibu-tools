package com.github.mvysny.kaributools.v22

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.*
import com.vaadin.flow.server.Platform
import loadAsProperties
import java.io.File
import java.util.*
import kotlin.test.expect

class AllTests : DynaTest({
    test("hilla version") {
        val gradleProps: Properties = File("../../gradle.properties").loadAsProperties()
        val expectedHillaVersion: String = gradleProps["hilla_prev_version"] as String
        expect(expectedHillaVersion) { VaadinVersion.hilla.toString() }
        // Platform.getVaadinVersion() returns Hilla version if Hilla is on the classpath. Implementation detail?
        // See https://github.com/vaadin/hilla/issues/1022
        expect(Platform.getVaadinVersion().orElse(null)) { VaadinVersion.hilla.toString() }
    }
})
