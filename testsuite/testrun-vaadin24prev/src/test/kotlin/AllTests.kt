package com.github.mvysny.kaributools.v24prev

import allTests21
import allTests23
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.*
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
        expect(null) { VaadinVersion.hilla }
    }

    test("vaadin version 2") {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedVaadinVersion: String = gradleProps["versions.vaadin24"] as String
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
})
