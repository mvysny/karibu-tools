package com.github.mvysny.kaributools

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.data.renderer.TemplateRenderer
import com.vaadin.flow.function.SerializableSupplier
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractRenderersTests {
    @Nested inner class valueProvider() {
        @Test fun smoke() {
            LocalDateRenderer<Person> { it.dateOfBirth }.valueProvider
        }
    }
    @Nested inner class `Renderer-template` {
        @Test fun templateRenderer() {
            assumeTrue(VaadinVersion.get.major < 24)
            expect("foobar") { TemplateRenderer.of<Person>("foobar").template }
        }
        @Test fun `component renderer`() {
            expect("") { ComponentRenderer<Button, Person>(SerializableSupplier { Button("Foo") }).template }
        }
    }
}
