package com.github.mvysny.kaributools.testapp

import com.github.mvysny.vaadinboot.VaadinBoot
import com.vaadin.flow.component.dependency.StyleSheet
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.theme.lumo.Lumo

@StyleSheet(Lumo.STYLESHEET)
class AppShell : AppShellConfigurator

fun main() {
    VaadinBoot().run()
}
