package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10._text
import com.vaadin.flow.component.button.Button
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.badgeTests() {
    test("smoke") {
        Badge()
        Badge("Foo").addThemeVariants(BadgeVariant.PRIMARY, BadgeVariant.ERROR, BadgeVariant.SMALL, BadgeVariant.PILL)
    }
}
