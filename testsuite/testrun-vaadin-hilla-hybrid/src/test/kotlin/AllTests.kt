package com.github.mvysny.kaributools.hybrid

import allTests21
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.*
import com.vaadin.flow.server.Platform
import org.tomlj.TomlTable
import java.io.File
import java.util.*
import kotlin.test.expect

class AllTests : DynaTest({
    test("vaadin version") {
        expect(24) { VaadinVersion.get.major }
        VaadinVersion.flow // smoke test that the call doesn't fail
    }

    test("hilla version") {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedHillaVersion: String = gradleProps["versions.hilla2next"] as String
        expect(expectedHillaVersion) { VaadinVersion.hilla.toString().replace('-', '.') }
    }

    test("vaadin version 2") {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedVaadinVersion: String = gradleProps["versions.vaadin24next"] as String
        expect(expectedVaadinVersion) { VaadinVersion.get.toString().replace('-', '.') }
    }

    group("vaadin14") {
        allTests()
    }
    group("vaadin21+") {
        allTests21()
    }
})
