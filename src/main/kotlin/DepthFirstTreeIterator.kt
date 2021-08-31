package com.github.mvysny.kaributools

import com.vaadin.flow.component.Component
import java.util.*
import kotlin.streams.toList

/**
 * Walks the child tree, depth-first: first the node, then its descendants,
 * then its next sibling.
 */
public class DepthFirstTreeIterator<out T>(root: T, private val children: (T) -> List<T>) : Iterator<T> {
    private val queue: Deque<T> = LinkedList(listOf(root))
    override fun hasNext(): Boolean = !queue.isEmpty()
    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        val result: T = queue.pop()
        children(result).asReversed().forEach { queue.push(it) }
        return result
    }
}

/**
 * Walks the component child tree, depth-first: first the component, then its descendants,
 * then its next sibling.
 */
public fun Component.walk(): Iterable<Component> = Iterable {
    DepthFirstTreeIterator(this) { component: Component -> component.children.toList() }
}
