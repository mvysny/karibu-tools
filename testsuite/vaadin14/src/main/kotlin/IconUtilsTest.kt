package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.IronIcon
import com.vaadin.flow.component.icon.VaadinIcon
import kotlin.test.expect

fun DynaNodeGroup.iconUtilsTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

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
        }
    }

    group("iron icon") {
        test("changing icon") {
            val icon = IronIcon("foo", "bar")
            icon.iconName = IconName.of(VaadinIcon.VAADIN_H)
            expect(VaadinIcon.VAADIN_H) { icon.iconName!!.asVaadinIcon() }
        }

        test("clearing icon") {
            val icon = IronIcon("foo", "bar")
            icon.iconName = null
            expect(null) { icon.iconName }
        }
    }
}
