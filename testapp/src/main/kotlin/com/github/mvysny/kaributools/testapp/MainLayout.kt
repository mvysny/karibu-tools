package com.github.mvysny.kaributools.testapp

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem

/**
 * The app shell: one [SideNav] item per demo route.
 */
class MainLayout : AppLayout() {
    init {
        addToNavbar(DrawerToggle(), H1("Karibu-Tools demo").apply {
            style.set("font-size", "var(--lumo-font-size-l)").set("margin", "0")
        })
        addToDrawer(SideNav().apply {
            addItem(SideNavItem("RouterLink target", RouterLinkTargetView::class.java))
        })
    }
}
