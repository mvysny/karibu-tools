package com.github.mvysny.kaributools

import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractIconUtilsTests {
    @Test fun smoke() {
        expect(IconName("foo", "bar")) {
            @Suppress("DEPRECATION")
            Icon("foo", "bar").iconName
        }
    }

    @Test fun `vaadin icon`() {
        expect(VaadinIcon.HOURGLASS_END) { IconName.of(VaadinIcon.HOURGLASS_END).asVaadinIcon() }
        expect(VaadinIcon.ABACUS) { IconName.of(VaadinIcon.ABACUS).asVaadinIcon() }
        expect(VaadinIcon.LIST_OL) {
            VaadinIcon.LIST_OL.create().iconName!!.asVaadinIcon()
        }
        expect(VaadinIcon.LIST_OL) { VaadinIcon.LIST_OL.iconName.asVaadinIcon() }
    }

    @Test fun serverClick() {
        val icon = Icon()
        var clicked = 0
        icon.addClickListener { clicked++ }
        icon.serverClick()
        expect(1) { clicked }
    }

    @Test fun createComponent() {
        expect(IconName.of(VaadinIcon.ABACUS)) { (IconName.of(VaadinIcon.ABACUS).createComponent() as Icon).iconName }
        expect(IconName("lumo", "plus")) { (IconName("lumo", "plus").createComponent() as Icon).iconName }
    }

    @Test fun `asVaadinIcon of a non-vaadin icon`() {
        expect(null) { IconName("lumo", "plus").asVaadinIcon() }
    }

    @Nested inner class icon {
        @Test fun `changing icon`() {
            val icon = VaadinIcon.ABACUS.create()
            icon.iconName = IconName.of(VaadinIcon.VAADIN_H)
            expect(VaadinIcon.VAADIN_H) { icon.iconName!!.asVaadinIcon() }
        }

        @Test fun `clearing icon`() {
            val icon = Icon(VaadinIcon.ABACUS)
            icon.iconName = null
            expect(null) { icon.iconName }
            expect(null) { icon.element.getAttribute("icon") }
        }

        @Test fun setIcon() {
            val icon = Icon()
            icon.setIcon(VaadinIcon.ABACUS)
            expect(VaadinIcon.ABACUS) { icon.iconName!!.asVaadinIcon() }
            icon.setIcon(null)
            expect(null) { icon.iconName }
        }

        @Test fun `vaadin-h icon by default`() {
            val icon = Icon()
            if (VaadinVersion.get.isAtLeast(24, 4)) {
                expect(null) { icon.iconName }
            } else {
                expect(IconName.of(VaadinIcon.VAADIN_H)) { icon.iconName }
            }
        }
    }
}
