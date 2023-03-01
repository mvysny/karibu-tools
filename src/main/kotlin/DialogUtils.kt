package com.github.mvysny.kaributools

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.dialog.Dialog.DialogCloseActionEvent
import kotlin.streams.toList

/**
 * Returns all dialogs opened in the current UI. There may be closed dialogs
 * since they are cleaned up lately by Vaadin.
 */
public fun getAllDialogs(): List<Dialog> = UI.getCurrent().getAllDialogs()

/**
 * Returns all dialogs opened in the given UI. There may be closed dialogs
 * since they are cleaned up lately by Vaadin.
 */
public fun UI.getAllDialogs(): List<Dialog> = children.toList().filterIsInstance<Dialog>()

/**
 * Re-centers the dialog on-screen after the width/height have been changed.
 *
 * The dialog is centered by default; there's no need to call this on every dialog construction.
 */
public fun Dialog.center() {
    // https://github.com/vaadin/vaadin-dialog/issues/220
    val styles: MutableList<String> = mutableListOf<String>()
    if (width != null) {
        styles.add("width: $width")
    }
    if (height != null) {
        styles.add("height: $height")
    }
    element.executeJs("this.$.overlay.$.overlay.style = '${styles.joinToString(";")}';")
}

/**
 * Requests that the dialog is closed. Simply calls [Dialog.close] unless the dialog has [Dialog.addDialogCloseActionListener].
 * If it does, then those listeners are called instead; the dialog is not closed.
 */
public fun Dialog.requestClose(fromClient: Boolean = true) {
    if (hasListener(DialogCloseActionEvent::class.java)) {
        fireEvent(DialogCloseActionEvent(this, fromClient))
    } else {
        close()
    }
}
