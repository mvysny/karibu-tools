package com.github.mvysny.kaributools.v24

import allTests21
import allTests23
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.*
import com.vaadin.flow.component.HasPlaceholder
import com.vaadin.flow.component.html.Div
import org.tomlj.TomlTable
import java.io.File
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
        val expectedVaadinVersion: String = gradleProps["versions.vaadin24next"] as String
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
    group("HasPlaceholder") {
        class MyComponent: Div(), HasPlaceholder {
            var myplaceholder: String? = null
            override fun getPlaceholder(): String? = myplaceholder
            override fun setPlaceholder(placeholder: String?) {
                this.myplaceholder = placeholder
            }
        }
        test("placeholder property uses the new HasPlaceholder interface introduced in Vaadin 24.3.0.alpha6") {
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
})
