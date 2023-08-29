package com.github.mvysny.kaributools

import com.vaadin.flow.component.Text
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.contextmenu.SubMenu
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.menubar.MenuBarVariant

/**
 * Closes the submenu popup. Does nothing if the popup button is already closed.
 */
public fun MenuBar.close() {
    // workaround for https://github.com/vaadin/vaadin-menu-bar/issues/102
    element.executeJs("this._subMenu.close()")
}

/**
 * Add an item with an icon.
 *
 * Taken from https://vaadin.com/docs/latest/components/menu-bar#icons
 */
public fun MenuBar.addIconItem(icon: Icon, label: String = "", ariaLabel: String = label): MenuItem {
    // not portable across multiple Vaadin versions
//    addThemeVariants(MenuBarVariant.LUMO_ICON)
    if (label.isBlank()) {
        // icon-only item. By default menu bar adds too much padding around the icon; reduce the padding by adding the icon theme variant.
        themeNames.add(MenuBarVariant.LUMO_ICON.variantName)
    }

    val item: MenuItem = addItem(icon)
    if (ariaLabel.isNotBlank()) {
        item.ariaLabel = ariaLabel
    }
    if (label.isNotBlank()) {
        item.add(Text(label))
    }
    return item
}

/**
 * Add an item with an icon.
 *
 * Taken from https://vaadin.com/docs/latest/components/menu-bar#icons
 */
public fun SubMenu.addIconItem(icon: Icon, label: String = "", ariaLabel: String = label): MenuItem {
    val item: MenuItem = addItem(icon)
    icon.style["width"] = "var(--lumo-icon-size-s)"
    icon.style["height"] = "var(--lumo-icon-size-s)"
    icon.style["marginRight"] = "var(--lumo-space-s)"
    if (ariaLabel.isNotBlank()) {
        item.ariaLabel = ariaLabel
    }
    if (label.isNotBlank()) {
        item.add(Text(label))
    }
    return item
}
