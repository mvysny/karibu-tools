package com.github.mvysny.kaributools.v23

import allTests21
import allTests23
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.jvmVersion
import com.github.mvysny.kaributools.VaadinVersion
import com.github.mvysny.kaributools.allTests
import com.github.mvysny.kaributools.parseToml
import org.tomlj.TomlTable
import java.io.File
import kotlin.test.expect

class AllTests : DynaTest({
    // Vaadin 23+ requires JDK 11+
    if (jvmVersion >= 11) {
        test("vaadin version") {
            expect(23) { VaadinVersion.get.major }
            VaadinVersion.flow // smoke test that the call doesn't fail
        }

        test("hilla version") {
            expect(null) { VaadinVersion.hilla }
        }

        test("vaadin version 2") {
            val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
            val expectedVaadinVersion: String = gradleProps["versions.vaadin23"] as String
            expect(expectedVaadinVersion) { VaadinVersion.get.toString().replace('-', '.') }
        }

        group("vaadin14") {
            allTests()
        }
        group("vaadin21+") {
            allTests21()
        }
        group("vaadin23+") {
            allTests23()
        }
    }
})
