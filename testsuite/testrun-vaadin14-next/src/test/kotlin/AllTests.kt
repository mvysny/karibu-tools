package com.github.mvysny.kaributools.v14

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.VaadinVersion
import com.github.mvysny.kaributools.allTests
import com.github.mvysny.kaributools.parseToml
import org.tomlj.TomlTable
import java.io.File
import kotlin.test.expect

class AllTests : DynaTest({
    test("vaadin version") {
        expect(14) { VaadinVersion.get.major }
        VaadinVersion.flow // smoke test that the call doesn't fail
    }

    test("hilla version") {
        expect(null) { VaadinVersion.hilla }
    }

    test("vaadin version 2") {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedVaadinVersion: String = gradleProps["versions.vaadin14next"] as String
        expect(expectedVaadinVersion) { VaadinVersion.get.toString().replace('-', '.') }
    }

    allTests()
})
