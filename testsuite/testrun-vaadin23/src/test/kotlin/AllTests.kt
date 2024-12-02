package com.github.mvysny.kaributools.v23

import AbstractAllTests21
import AbstractAllTests23
import com.github.mvysny.kaributesting.v10.jvmVersion
import com.github.mvysny.kaributools.AbstractAllTests
import com.github.mvysny.kaributools.VaadinVersion
import com.github.mvysny.kaributools.parseToml
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.tomlj.TomlTable
import java.io.File
import kotlin.test.expect

class AllTests {
    // Vaadin 23+ requires JDK 11+
    @BeforeEach
    fun assumeJDK11() {
        assumeTrue(jvmVersion >= 11)
    }

    @Test
    fun `vaadin version`() {
        expect(23) { VaadinVersion.get.major }
        VaadinVersion.flow // smoke test that the call doesn't fail
    }

    @Test
    fun `hilla version`() {
        expect(null) { VaadinVersion.hilla }
    }

    @Test
    fun `vaadin version 2`() {
        val gradleProps: TomlTable =
            File("../../gradle/libs.versions.toml").parseToml()
        val expectedVaadinVersion: String =
            gradleProps["versions.vaadin23"] as String
        expect(expectedVaadinVersion) {
            VaadinVersion.get.toString().replace('-', '.')
        }
    }

    @Nested
    inner class AllTests : AbstractAllTests()
    @Nested
    inner class AllTests21 : AbstractAllTests21()
    @Nested
    inner class AllTests23 : AbstractAllTests23()
}
