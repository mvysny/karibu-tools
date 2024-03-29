package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.data.renderer.TemplateRenderer
import com.vaadin.flow.function.SerializableSupplier
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.renderersTests() {
    group("valueProvider") {
        test("smoke") {
            LocalDateRenderer<Person> { it.dateOfBirth }.valueProvider
        }
    }
    group("Renderer.template") {
        if (VaadinVersion.get.major < 24) {
            test("template renderer") {
                expect("foobar") { TemplateRenderer.of<Person>("foobar").template }
            }
        }
        test("component renderer") {
            expect("") { ComponentRenderer<Button, Person>(SerializableSupplier { Button("Foo") }).template }
        }
    }
}
