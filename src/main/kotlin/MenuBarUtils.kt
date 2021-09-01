package com.github.mvysny.kaributools

import com.vaadin.flow.component.menubar.MenuBar

/**
 * Closes the submenu popup. Does nothing if the popup button is already closed.
 */
public fun MenuBar.close() {
    // workaround for https://github.com/vaadin/vaadin-menu-bar/issues/102
    element.executeJs("this._subMenu.close()")
}
