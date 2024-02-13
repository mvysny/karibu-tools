package com.github.mvysny.kaributools.v22

import allTests21
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.VaadinVersion
import com.github.mvysny.kaributools.allTests
import com.github.mvysny.kaributools.parseToml
import org.tomlj.TomlTable
import java.io.File
import kotlin.test.expect

class AllTests : DynaTest({
    test("vaadin version") {
        expect(22) { VaadinVersion.get.major }
        VaadinVersion.flow // smoke test that the call doesn't fail
    }

    test("hilla version") {
        expect(null) { VaadinVersion.hilla }
    }

    test("vaadin version 2") {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedVaadinVersion: String = gradleProps["versions.vaadin22"] as String
        expect(expectedVaadinVersion) { VaadinVersion.get.toString().replace('-', '.') }
    }

    group("vaadin14") {
        allTests()
    }
    group("vaadin21+") {
        allTests21()
    }
})
