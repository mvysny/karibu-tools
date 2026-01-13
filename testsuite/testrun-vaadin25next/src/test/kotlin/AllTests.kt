package com.github.mvysny.kaributools.v24prev

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
        expect(25) { VaadinVersion.get.major }
        VaadinVersion.flow // smoke test that the call doesn't fail
    }

    @Test fun `hilla version`() {
        expect(null) { VaadinVersion.hilla }
    }

    @Test fun `vaadin version 2`() {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedVaadinVersion: String = gradleProps["versions.vaadin25next"] as String
        expect(expectedVaadinVersion) { VaadinVersion.get.toString() }
    }

    @Nested
    inner class AllTests : AbstractAllTests()
    @Nested
    inner class AllTests21 : AbstractAllTests21()
    @Nested
    inner class AllTests23 : AbstractAllTests23()
}
