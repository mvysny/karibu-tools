package com.github.mvysny.kaributools

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.dom.Element
import java.lang.reflect.Field

private val _Notification_templateElement: Field by lazy(LazyThreadSafetyMode.PUBLICATION) {
    val f: Field = Notification::class.java.getDeclaredField("templateElement")
    f.isAccessible = true
    f
}

public val Notification._templateElement: Element
    get() = _Notification_templateElement.get(this) as Element

/**
 * Returns the notification text.
 *
 * Vote for https://github.com/vaadin/web-components/issues/2446
 */
public fun Notification.getText(): String {
    if (hasChildren) {
        // adding components to the notification clears the notification text
        return ""
    }
    val e: Element = _templateElement
    return e.getProperty("innerHTML") ?: ""
}

/**
 * Adds a close button to the notification.
 *
 * Vote for https://github.com/vaadin/web-components/issues/438
 */
public fun Notification.addCloseButton(): Notification {
    if (getText().isNotBlank()) {
        // adding components clears the text; preserve the original text in a Span.
        add(Span(getText()))
    }
    val closeButton = Button(Icon(VaadinIcon.CLOSE))
    closeButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY)
    closeButton.addClickListener { close() }
    add(closeButton)
    return this
}
