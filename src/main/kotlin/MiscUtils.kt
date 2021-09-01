package com.github.mvysny.kaributools

import java.beans.Introspector
import java.beans.PropertyDescriptor
import java.lang.reflect.Method
import kotlin.reflect.KProperty1

internal fun String.containsWhitespace(): Boolean = any { it.isWhitespace() }
private val regexWhitespace = Regex("\\s+")
internal fun String.splitByWhitespaces(): List<String> = split(regexWhitespace).filterNot { it.isBlank() }

/**
 * Returns the getter method for given property name; fails if there is no such getter.
 */
public fun Class<*>.getGetter(propertyName: String): Method {
    val descriptors: Array<out PropertyDescriptor> = Introspector.getBeanInfo(this).propertyDescriptors
    val descriptor: PropertyDescriptor? = descriptors.firstOrNull { it.name == propertyName }
    requireNotNull(descriptor) { "No such field '$propertyName' in $this; available properties: ${descriptors.joinToString { it.name }}" }
    val getter: Method = requireNotNull(descriptor.readMethod) { "The $this.$propertyName property does not have a getter: $descriptor" }
    return getter
}

/**
 * Returns a [Comparator] which compares beans of type [T] by values of a property
 * identified by given [propertyName].
 */
public fun <T> Class<T>.getPropertyComparator(propertyName: String): Comparator<T> {
    val getter: Method = getGetter(propertyName)
    return compareBy { if (it == null) null else getter.invoke(it) as Comparable<*> }
}

/**
 * Returns a [Comparator] which compares beans of type [T] by values of given property.
 */
public inline val <reified T> KProperty1<T, *>.comparator: Comparator<T>
    get() = T::class.java.getPropertyComparator(name)
