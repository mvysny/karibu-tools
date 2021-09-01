package com.github.mvysny.kaributools

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.dialog.Dialog
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
 * Centers the dialog on-screen.
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
