package com.github.mvysny.kaributools

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import java.io.Serializable

/**
 * Represents an icon name and a collection from which the icon came.
 * @property collection the icon collection name. `vaadin` collection is provided by
 * default and you can find all icons here: https://vaadin.com/components/vaadin-icons .
 * TODO how to add more collections?
 * @property name the icon name, e.g. "abacus"
 */
public data class IconName(val collection: String, val name: String) : Serializable {
    init {
        require(collection.isNotBlank()) { "$collection: collection is blank" }
        require(name.isNotBlank()) { "$name: name is blank" }
    }

    /**
     * Creates a new component for this icon - [Icon].
     */
    @Suppress("DEPRECATION")
    public fun createComponent(): Component = when {
        isVaadinIcon -> Icon(name)
        else -> Icon(collection, name) // even though this is marked as Deprecated in Vaadin 14, this is the preferred way in Vaadin 23+; IronIcon is removed in Vaadin 24.
    }

    /**
     * Checks whether this icon represents a [VaadinIcon]. Use [asVaadinIcon] to obtain
     * the original icon.
     */
    val isVaadinIcon: Boolean get() = collection == "vaadin"

    /**
     * If this icon represents a [VaadinIcon], return the appropriate [VaadinIcon]
     * constant, else return null.
     */
    public fun asVaadinIcon(): VaadinIcon? {
        if (!isVaadinIcon) {
            return null
        }
        val enumName: String = name.uppercase().replace('-', '_')
        return VaadinIcon.valueOf(enumName)
    }

    /**
     * Returns this icon name as a string in the format `collection:name`.
     */
    override fun toString(): String = "$collection:$name"

    public companion object {
        /**
         * Gets the icon name from given [vaadinIcon].
         */
        @JvmStatic
        public fun of(vaadinIcon: VaadinIcon): IconName =
            IconName("vaadin", vaadinIcon.name.lowercase().replace('_', '-'))

        /**
         * Parses the [toString] string representation. Returns null if the [iconName] is not in the expected format.
         * @param iconName string representation in the form of `collection:name`.
         */
        @JvmStatic
        public fun fromString(iconName: String): IconName? {
            val iconPair: List<String> = iconName.split(':')
            return if (iconPair.size == 2) IconName(iconPair[0], iconPair[1]) else null
        }

        @JvmStatic
        public fun fromComponent(icon: Icon): IconName? {
            val iconName: String? = icon.element.getAttribute("icon")
            return iconName?.let { fromString(it) }
        }
    }
}

/**
 * Returns the icon name and collection from the [Icon] component. Returns null
 * if no icon is set.
 */
public var Icon.iconName: IconName?
    get() = IconName.fromComponent(this)
    set(value) {
        element.setOrRemoveAttribute("icon", value?.toString())
    }

public fun Icon.setIcon(icon: VaadinIcon?) {
    iconName = if (icon == null) null else IconName.of(icon)
}

/**
 * Returns the [IconName] of this Vaadin icon.
 */
public val VaadinIcon.iconName: IconName get() = IconName.of(this)
