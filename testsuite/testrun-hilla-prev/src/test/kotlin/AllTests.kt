package com.github.mvysny.kaributools.hillaprev

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.*
import org.tomlj.TomlTable
import java.io.File
import kotlin.test.expect

class AllTests : DynaTest({
    test("vaadin version") {
        expect(null) { VaadinVersion.vaadin }
        VaadinVersion.flow // smoke test that the call doesn't fail
    }

    test("hilla version") {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedHillaVersion: String = gradleProps["versions.hilla2"] as String
        expect(expectedHillaVersion) { VaadinVersion.hilla.toString() }
    }
})
