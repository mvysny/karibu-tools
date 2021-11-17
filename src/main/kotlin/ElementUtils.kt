package com.github.mvysny.kaributools

import com.vaadin.flow.dom.ClassList
import com.vaadin.flow.dom.Element
import com.vaadin.flow.dom.ElementUtil
import com.vaadin.flow.internal.StateNode
import com.vaadin.flow.internal.nodefeature.VirtualChildrenList
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import kotlin.streams.toList

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

/**
 * Returns all virtual child elements added via [Element.appendVirtualChild].
 */
public fun Element.getVirtualChildren(): List<Element> {
    if (node.hasFeature(VirtualChildrenList::class.java)) {
        val virtualChildrenList: VirtualChildrenList? =
            node.getFeatureIfInitialized(VirtualChildrenList::class.java)
                .orElse(null)
        if (virtualChildrenList != null) {
            return virtualChildrenList.iterator().asSequence().map { it.element } .toList()
        }
    }
    return listOf()
}

/**
 * Gets the element mapped to the given state node.
 */
public val StateNode.element: Element get() = Element.get(this)

/**
 * Returns child elements with the `slot` attribute set to given [slotName].
 */
public fun Element.getChildrenInSlot(slotName: String): List<Element> =
    children.filter { child -> child.getAttribute("slot") == slotName } .toList()

/**
 * Removes all child elements from given slot, leaving it empty.
 */
public fun Element.clearSlot(slotName: String) {
    require(slotName.isNotBlank())
    getChildrenInSlot(slotName).forEach { it.removeFromParent() }
}
