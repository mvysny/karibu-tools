package com.github.mvysny.kaributools.hybrid

import AbstractAllTests21
import AbstractAllTests23
import com.github.mvysny.kaributools.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.tomlj.TomlTable
import java.io.File
import kotlin.test.expect

class AllTests {
    @Test fun `vaadin version`() {
        expect(24) { VaadinVersion.get.major }
        VaadinVersion.flow // smoke test that the call doesn't fail
    }

    @Test fun `hilla version`() {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedHillaVersion: String = gradleProps["versions.hilla2next"] as String
        expect(expectedHillaVersion) { VaadinVersion.hilla.toString().replace('-', '.') }
    }

    @Test fun `vaadin version 2`() {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedVaadinVersion: String = gradleProps["versions.vaadin24next"] as String
        expect(expectedVaadinVersion) { VaadinVersion.get.toString().replace('-', '.') }
    }

    @Nested inner class AllTests : AbstractAllTests()
    @Nested inner class allTests21 : AbstractAllTests21()
    @Nested inner class allTests23 : AbstractAllTests23()
}
