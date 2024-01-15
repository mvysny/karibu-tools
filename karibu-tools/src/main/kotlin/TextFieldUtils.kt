package com.github.mvysny.kaributools

import com.vaadin.flow.component.AbstractSinglePropertyField
import com.vaadin.flow.component.textfield.GeneratedVaadinTextArea
import com.vaadin.flow.component.textfield.GeneratedVaadinTextField

/**
 * Selects all text in this text field. The selection is not really visible in
 * the browser unless the field is focused.
 */
public fun AbstractSinglePropertyField<*, String>.selectAll() {
    element.executeJs("this.inputElement.select()");
}

/**
 * Clears the selection in the text field and moves the cursor to the end of
 * the text. There may be no effect if the field is unfocused - the browser
 * generally selects all when the field gains focus.
 */
public fun <R: AbstractSinglePropertyField<R, String>> R.selectNone() {
    setCursorLocation(value?.length ?: 0)
}

/**
 * Moves the cursor within the text field. Has the side-effect of clearing the selection.
 *
 * There may be no effect if the field is unfocused - the browser
 * generally selects all when the field gains focus.
 */
public fun AbstractSinglePropertyField<*, String>.setCursorLocation(cursorLocation: Int) {
    select(cursorLocation until cursorLocation)
}

/**
 * Selects given characters in this text field. The selection is not really visible in
 * the browser unless the field is focused.
 */
public fun AbstractSinglePropertyField<*, String>.select(selection: IntRange) {
    require(selection.first >= 0)
    require(selection.last >= -1)
    element.executeJs("this.inputElement.setSelectionRange(${selection.first}, ${selection.last + 1})")
}
