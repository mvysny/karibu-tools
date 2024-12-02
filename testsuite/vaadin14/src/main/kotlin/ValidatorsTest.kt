package com.github.mvysny.kaributools

import com.vaadin.flow.data.binder.ValidationResult
import com.vaadin.flow.data.binder.Validator
import com.vaadin.flow.data.binder.ValueContext
import com.vaadin.flow.data.validator.EmailValidator
import com.vaadin.flow.data.validator.StringLengthValidator
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractValidatorsTests() {
    @Nested inner class isValid {
        @Test fun EmailValidator() {
            expect(true) { EmailValidator("").isValid("foo@bar.baz") }
            expect(false) { EmailValidator("").isValid("") }
            // null usually passes all validators since it's caught by Binder.asRequired()
            expect(true) { EmailValidator("").isValid(null) }
            expect(false) { EmailValidator("").isValid("a") }
        }
        @Test fun StringLengthValidator() {
            expect(true) { StringLengthValidator("", 1, null).isValid("a") }
            expect(false) { StringLengthValidator("", 1, null).isValid("") }
            expect(true) { StringLengthValidator("", 1, null).isValid(null) }
        }
        @Test fun RangeValidator() {
            expect(true) { rangeValidatorOf("", 1, null).isValid(1) }
            expect(false) { rangeValidatorOf("", 1, null).isValid(0) }
            expect(true) { rangeValidatorOf("", 1, null).isValid(null) }
        }
        @Test fun DummyValidator() {
            expect(true) { DummyValidator.isValid(null) }
            expect(true) { DummyValidator.isValid("") }
        }
    }
}

private object DummyValidator: Validator<String> {
    override fun apply(value: String?, context: ValueContext): ValidationResult = ValidationResult.ok()
}
