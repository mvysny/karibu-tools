package com.github.mvysny.kaributools

import com.vaadin.flow.component.login.AbstractLogin
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.internal.JsonSerializer
import elemental.json.JsonValue

/**
 * WARNING: In order to apply new values you have to write the object back.
 */
public var AbstractLogin.i18n: LoginI18n?
    get() {
        val json = element.getPropertyRaw("i18n") as JsonValue?
        return when {
            json.isNull -> null
            else -> JsonSerializer.toObject(LoginI18n::class.java, json)
        }
    }
    set(value) {
        setI18n(value)
    }

/**
 * Shows an error message and sets [AbstractLogin.setError] to true.
 *
 * Vote for [issue #1525](https://github.com/vaadin/flow-components/issues/1525)
 */
public fun AbstractLogin.setErrorMessage(title: String?, message: String?) {
    var loginI18n = i18n
    if (loginI18n == null) {
        loginI18n = LoginI18n.createDefault()!!
    }
    if (loginI18n.errorMessage == null) {
        loginI18n.errorMessage = LoginI18n.ErrorMessage()
    }
    loginI18n.errorMessage.title = title
    loginI18n.errorMessage.message = message
    i18n = loginI18n
    isError = true
}
