package com.github.mvysny.kaributools

import com.vaadin.flow.component.html.Span

/**
 * Populates its contents with given html snippet. The advantage over [com.vaadin.flow.component.Html]
 * is that any html is accepted - it doesn't have to be wrapped in a single root element.
 *
 * Note that it is the developer's responsibility to sanitize and remove any
 * dangerous parts of the HTML before sending it to the user through this
 * component. Passing raw input data to the user will possibly lead to
 * cross-site scripting attacks.
 *
 * This component does not expand the HTML fragment into a server side DOM tree
 * so you cannot traverse or modify the HTML on the server. The root element can
 * be accessed through [element] and the inner HTML through
 * [innerHTML].
 * @param innerHTML the HTML snippet to populate the span with.
 */
public class HtmlSpan(innerHTML: String = "") : Span() {
    /**
     * Sets the inner html. Removes any children added via [add].
     */
    public var innerHTML: String
        get() = element.getProperty("innerHTML", "")
        set(value) {
            removeAll()
            element.setProperty("innerHTML", value)
        }

    init {
        this.innerHTML = innerHTML
    }
}
