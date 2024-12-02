package com.github.mvysny.kaributools

import com.vaadin.flow.component.login.AbstractLogin
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginOverlay
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractLoginUtilsTests {
    @Nested inner class loginForm : AbstractLoginTests({ LoginForm() })
    @Nested inner class loginOverlay : AbstractLoginTests({ LoginOverlay() })
}

abstract class AbstractLoginTests(val provider: () -> AbstractLogin) {
    @Test fun smoke() {
        val form = provider()
        form.setErrorMessage("title", "msg")
        expect(true) { form.isError }
        expect("title") { form.i18n!!.errorMessage.title }
        expect("msg") { form.i18n!!.errorMessage.message }
    }

    @Test fun `null i18n`() {
        // a bit of a corner-case since LoginForm sets the LoginI18n.createDefault()
        // and there is no point in setting null i18n...
        val form = provider()
        form.i18n = null
        expect(null) { form.i18n }
        form.setErrorMessage("title", "msg")
        expect(true) { form.isError }
        expect("title") { form.i18n!!.errorMessage.title }
        expect("msg") { form.i18n!!.errorMessage.message }
    }

    @Test fun `setting title+message preserves other values`() {
        val form = provider()
        form.i18n = LoginI18n().apply {
            header = LoginI18n.Header().apply {
                title = "foo title"
            }
        }
        form.setErrorMessage("title", "msg")
        // check that the header/title is preserved
        expect("foo title") { form.i18n!!.header.title }
        // check that the new error message has been applied.
        expect("title") { form.i18n!!.errorMessage.title }
        expect("msg") { form.i18n!!.errorMessage.message }
    }
}
