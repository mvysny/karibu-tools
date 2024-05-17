package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.iconUtilsTests() {
    test("smoke") {
        expect(IconName("foo", "bar")) {
            @Suppress("DEPRECATION")
            Icon("foo", "bar").iconName
        }
    }

    test("vaadin icon") {
        expect(VaadinIcon.HOURGLASS_END) { IconName.of(VaadinIcon.HOURGLASS_END).asVaadinIcon() }
        expect(VaadinIcon.ABACUS) { IconName.of(VaadinIcon.ABACUS).asVaadinIcon() }
        expect(VaadinIcon.LIST_OL) {
            VaadinIcon.LIST_OL.create().iconName!!.asVaadinIcon()
        }
    }

    test("serverClick()") {
        val icon = Icon()
        var clicked = 0
        icon.addClickListener { clicked++ }
        icon.serverClick()
        expect(1) { clicked }
    }

    group("icon") {
        test("changing icon") {
            val icon = VaadinIcon.ABACUS.create()
            icon.iconName = IconName.of(VaadinIcon.VAADIN_H)
            expect(VaadinIcon.VAADIN_H) { icon.iconName!!.asVaadinIcon() }
        }

        test("clearing icon") {
            val icon = Icon(VaadinIcon.ABACUS)
            icon.iconName = null
            expect(null) { icon.iconName }
            expect(null) { icon.element.getAttribute("icon") }
        }

        test("vaadin-h icon by default") {
            val icon = Icon()
            if (VaadinVersion.get.major == 24 && VaadinVersion.get.minor >= 4) {
                expect(null) { icon.iconName }
            } else {
                expect(IconName.of(VaadinIcon.VAADIN_H)) { icon.iconName }
            }
        }
    }
}
