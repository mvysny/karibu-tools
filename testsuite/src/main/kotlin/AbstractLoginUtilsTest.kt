package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import kotlin.test.expect

fun DynaNodeGroup.abstractLoginUtilsTests() {
    beforeEach { MockVaadin.setup() }
    afterEach { MockVaadin.tearDown() }

    test("smoke") {
        val form = LoginForm()
        form.setErrorMessage("title", "msg")
        expect(true) { form.isError }
        expect("title") { form.i18n!!.errorMessage.title }
        expect("msg") { form.i18n!!.errorMessage.message }
    }

    test("setting title+message preserves other values") {
        val form = LoginForm()
        form.i18n = LoginI18n().apply {
            header = LoginI18n.Header().apply {
                title = "foo title"
            }
        }
        form.setErrorMessage("title", "msg")
        expect("foo title") { form.i18n!!.header.title }
        expect("title") { form.i18n!!.errorMessage.title }
        expect("msg") { form.i18n!!.errorMessage.message }
    }
}
