package com.github.mvysny.kaributools.v14

import com.github.mvysny.kaributools.AbstractAllTests
import com.github.mvysny.kaributools.VaadinVersion
import com.github.mvysny.kaributools.parseToml
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.tomlj.TomlTable
import java.io.File
import kotlin.test.expect

class AllTests {
    @Test fun `vaadin version`() {
        expect(14) { VaadinVersion.get.major }
        VaadinVersion.flow // smoke test that the call doesn't fail
    }

    @Test fun `hilla version`() {
        expect(null) { VaadinVersion.hilla }
    }

    @Test fun `vaadin version 2`() {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedVaadinVersion: String = gradleProps["versions.vaadin14next"] as String
        expect(expectedVaadinVersion) { VaadinVersion.get.toString().replace('-', '.') }
    }

    @Nested inner class AllTests : AbstractAllTests()
}
