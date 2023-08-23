package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.flow.data.validator.EmailValidator
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.validatorsTests() {
    test("isValid") {
        expect(true) { EmailValidator("").isValid("foo@bar.baz") }
        expect(false) { EmailValidator("").isValid("") }
        // null usually passes all validators since it's caught by Binder.asRequired()
        expect(true) { EmailValidator("").isValid(null) }
        expect(false) { EmailValidator("").isValid("a") }
    }
}
