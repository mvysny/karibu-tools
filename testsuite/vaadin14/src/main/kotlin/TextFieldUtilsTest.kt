package com.github.mvysny.kaributools

import com.vaadin.flow.component.textfield.EmailField
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

abstract class AbstractTextFieldUtilsTests() {
    @Nested inner class textField {
    @Test fun smoke() {
            TextField().selectAll()
            TextField().selectNone()
            TextField().setCursorLocation(3)
        }
    }

    @Nested inner class textArea {
        @Test fun smoke() {
            TextArea().selectAll()
            TextArea().selectNone()
            TextArea().setCursorLocation(3)
        }
    }

    @Nested inner class emailField {
        @Test fun smoke() {
            EmailField().selectAll()
            EmailField().selectNone()
            EmailField().setCursorLocation(3)
        }
    }
}
