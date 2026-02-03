package com.github.mvysny.kaributools.v24

import AbstractAllTests21
import AbstractAllTests23
import com.github.mvysny.kaributools.*
import com.vaadin.flow.component.HasPlaceholder
import com.vaadin.flow.component.html.Div
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
        expect(null) { VaadinVersion.hilla }
    }

    @Test fun `vaadin version 2`() {
        val gradleProps: TomlTable = File("../../gradle/libs.versions.toml").parseToml()
        val expectedVaadinVersion: String = gradleProps["versions.vaadin24next"] as String
        expect(expectedVaadinVersion) { VaadinVersion.get.toString() }
    }

    @Nested
    inner class AllTests : AbstractAllTests()
    @Nested
    inner class AllTests21 : AbstractAllTests21()
    @Nested
    inner class AllTests23 : AbstractAllTests23()

    @Nested inner class HasPlaceholderTests {
        inner class MyComponent: Div(), HasPlaceholder {
            var myplaceholder: String? = null
            override fun getPlaceholder(): String? = myplaceholder
            override fun setPlaceholder(placeholder: String?) {
                this.myplaceholder = placeholder
            }
        }
        @Test fun `placeholder property uses the new HasPlaceholder interface introduced in Vaadin 24-3-0-alpha6`() {
            val c = MyComponent()
            expect(null) { c.placeholder }
            c.placeholder = "foo"
            expect("foo") { c.myplaceholder }
            expect("foo") { c.placeholder }
            c.myplaceholder = "bar"
            expect("bar") { c.placeholder }
            c.placeholder = null
            expect(null) { c.myplaceholder }
            expect(null) { c.placeholder }
        }
    }
}
