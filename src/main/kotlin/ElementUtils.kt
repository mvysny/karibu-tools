package com.github.mvysny.kaributools

import com.vaadin.flow.dom.ClassList
import com.vaadin.flow.dom.Element
import com.vaadin.flow.dom.ElementUtil
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

/**
 * Either calls [Element.setAttribute] (if the [value] is not null), or
 * [Element.removeAttribute] (if the [value] is null).
 * @param attribute the name of the attribute.
 */
public fun Element.setOrRemoveAttribute(attribute: String, value: String?) {
    if (value == null) {
        removeAttribute(attribute)
    } else {
        setAttribute(attribute, value)
    }
}

/**
 * Toggles [className] - removes it if it was there, or adds it if it wasn't there.
 * @param className the class name to toggle, cannot contain spaces.
 */
public fun ClassList.toggle(className: String) {
    require(!className.containsWhitespace()) { "'$className' cannot contain whitespace" }
    set(className, !contains(className))
}

/**
 * Inserts [newNode] as a child, right before an [existingNode].
 * A counterpart for JavaScript DOM `Node.insertBefore()`.
 */
public fun Element.insertBefore(newNode: Element, existingNode: Element) {
    val parent: Element = requireNotNull(existingNode.parent) { "$existingNode has no parent element" }
    require(parent == this) { "$existingNode is not nested in $this" }
    insertChild(indexOfChild(existingNode), newNode)
}

/**
 * This function actually works, as opposed to [Element.getTextRecursively].
 */
public val Element.textRecursively2: String
    get() {
        // remove when this is fixed: https://github.com/vaadin/flow/issues/3668
        val node = ElementUtil.toJsoup(Document(""), this)
        return node.textRecursively
    }

public val Node.textRecursively: String
    get() = when (this) {
        is TextNode -> this.text()
        else -> childNodes().joinToString(separator = "", transform = { it.textRecursively })
    }
