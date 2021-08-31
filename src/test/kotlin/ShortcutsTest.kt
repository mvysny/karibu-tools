package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributools.ModifierKey.Alt
import com.github.mvysny.kaributools.ModifierKey.Ctrl
import com.vaadin.flow.component.Key.KEY_C
import com.vaadin.flow.component.button.Button

class ShortcutsTest : DynaTest({
    test("smoke") {
        Button().addClickShortcut(Alt + Ctrl + KEY_C)
        Button().addFocusShortcut(Alt + Ctrl + KEY_C)
        Button().addShortcut(Alt + Ctrl + KEY_C) { println("Foo") }
    }
})
