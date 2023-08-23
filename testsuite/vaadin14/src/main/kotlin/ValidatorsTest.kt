package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.flow.data.binder.ValidationResult
import com.vaadin.flow.data.binder.Validator
import com.vaadin.flow.data.binder.ValueContext
import com.vaadin.flow.data.validator.EmailValidator
import com.vaadin.flow.data.validator.RangeValidator
import com.vaadin.flow.data.validator.StringLengthValidator
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.validatorsTests() {
    group("isValid") {
        test("EmailValidator") {
            expect(true) { EmailValidator("").isValid("foo@bar.baz") }
            expect(false) { EmailValidator("").isValid("") }
            // null usually passes all validators since it's caught by Binder.asRequired()
            expect(true) { EmailValidator("").isValid(null) }
            expect(false) { EmailValidator("").isValid("a") }
        }
        test("StringLengthValidator") {
            expect(true) { StringLengthValidator("", 1, null).isValid("a") }
            expect(false) { StringLengthValidator("", 1, null).isValid("") }
            expect(true) { StringLengthValidator("", 1, null).isValid(null) }
        }
        test("RangeValidator") {
            expect(true) { rangeValidatorOf("", 1, null).isValid(1) }
            expect(false) { rangeValidatorOf("", 1, null).isValid(0) }
            expect(true) { rangeValidatorOf("", 1, null).isValid(null) }
        }
        test("DummyValidator") {
            expect(true) { DummyValidator.isValid(null) }
            expect(true) { DummyValidator.isValid("") }
        }
    }
}

private object DummyValidator: Validator<String> {
    override fun apply(value: String?, context: ValueContext): ValidationResult = ValidationResult.ok()
}
