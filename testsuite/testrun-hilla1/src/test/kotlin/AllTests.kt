package com.github.mvysny.kaributools.hilla1

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.*
import com.vaadin.flow.server.Platform
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
        val expectedHillaVersion: String = gradleProps["versions.hilla1"] as String
        expect(expectedHillaVersion) { VaadinVersion.hilla.toString() }
        // Platform.getVaadinVersion() returns Hilla version if Hilla is on the classpath. Implementation detail?
        // See https://github.com/vaadin/hilla/issues/1022
        expect(Platform.getVaadinVersion().orElse(null)) { VaadinVersion.hilla.toString() }
    }
})
