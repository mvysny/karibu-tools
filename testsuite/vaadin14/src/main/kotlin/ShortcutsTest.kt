package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._fireShortcut
import com.github.mvysny.kaributesting.v10.fireShortcut
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.Key.KEY_C
import com.vaadin.flow.component.KeyModifier
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.PendingJavaScriptResult
import com.vaadin.flow.component.textfield.TextField
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractShortcutsTests {
    @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    @Test fun `building shortcuts`() {
        expect(KeyShortcut(KEY_C)) { KEY_C.shortcut }
        expect(KeyShortcut(KEY_C, setOf(ModifierKey.Ctrl))) { Ctrl + KEY_C }
        expect(KeyShortcut(KEY_C, setOf(ModifierKey.Ctrl, ModifierKey.Alt))) { Ctrl + Alt + KEY_C }
        expect(listOf(KeyModifier.CONTROL, KeyModifier.ALT_GRAPH)) { (Ctrl + AltGr + KEY_C).vaadinModifiers.toList() }
    }

    @Test fun addClickShortcut() {
        val button = Button()
        UI.getCurrent().add(button)
        var clicks = 0
        button.addClickListener { clicks++ }
        button.addClickShortcut(Ctrl + KEY_C)
        fireShortcut(KEY_C)
        expect(0) { clicks }
        fireShortcut(KEY_C, KeyModifier.CONTROL)
        expect(1) { clicks }
    }

    @Test fun `addClickShortcut onlyWhenFocused`() {
        val button = Button()
        UI.getCurrent().add(button)
        var clicks = 0
        button.addClickListener { clicks++ }
        button.addClickShortcut(Ctrl + KEY_C, onlyWhenFocused = true)
        fireShortcut(KEY_C, KeyModifier.CONTROL)
        expect(0) { clicks }
        button._fireShortcut(KEY_C, KeyModifier.CONTROL)
        expect(1) { clicks }
    }

    @Test fun addShortcut() {
        val layout = VerticalLayout()
        UI.getCurrent().add(layout)
        var calls = 0
        layout.addShortcut(Alt + KEY_C) { calls++ }
        fireShortcut(KEY_C, KeyModifier.ALT)
        expect(0) { calls }
        layout._fireShortcut(KEY_C, KeyModifier.ALT)
        expect(1) { calls }
    }

    @Test fun onEnter() {
        val tf = TextField()
        UI.getCurrent().add(tf)
        var calls = 0
        tf.onEnter { calls++ }
        tf._fireShortcut(Key.ENTER)
        // the block waits until the browser flushes the field value
        expect(0) { calls }
        UI.getCurrent().internals.stateTree.runExecutionsBeforeClientResponse()
        val flush = UI.getCurrent().internals.dumpPendingJavaScriptInvocations()
            .single { it.invocation.expression.contains("_onChange") }
        flush.completeWithNull()
        expect(1) { calls }
    }
}

/**
 * Completes this invocation as if the browser returned a JSON `null`. Reflection: `complete()`
 * takes an elemental `JsonValue` up to Vaadin 24.4, a Jackson `JsonNode` since.
 */
private fun PendingJavaScriptResult.completeWithNull() {
    val complete = javaClass.methods.first { it.name == "complete" && it.parameterCount == 1 }
    val jsonType: Class<*> = complete.parameterTypes[0]
    val jsonNull: Any = if (jsonType.name == "elemental.json.JsonValue") {
        Class.forName("elemental.json.Json").getMethod("createNull").invoke(null)
    } else {
        Class.forName("${jsonType.packageName}.node.NullNode").getMethod("getInstance").invoke(null)
    }
    complete.invoke(this, jsonNull)
}
