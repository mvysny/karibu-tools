package com.github.mvysny.kaributools

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.page.ExtendedClientDetails
import com.vaadin.flow.server.VaadinSession
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * The time zone as reported by the browser. Use [com.vaadin.flow.component.page.Page.retrieveExtendedClientDetails]
 * to get [ExtendedClientDetails].
 */
public val ExtendedClientDetails.timeZone: ZoneId
    get() = if (!timeZoneId.isNullOrBlank()) {
        // take into account zone ID. This is important for historical dates, to properly compute date with daylight savings.
        ZoneId.of(timeZoneId)
    } else {
        // fallback to time zone offset
        ZoneOffset.ofTotalSeconds(timezoneOffset / 1000)
    }

/**
 * Returns the current date and time at browser's current time zone.
 */
public val ExtendedClientDetails.currentDateTime: LocalDateTime
    get() = LocalDateTime.now(timeZone)

/**
 * Provides the time-zone information from the browser. Don't forget to call [fetch] first!
 */
public object BrowserTimeZone {
    /**
     * Retrieves [extendedClientDetails] from the browser and populates [get].
     * Does nothing if the [extendedClientDetails] has already been populated.
     *
     * Stores the [extendedClientDetails] into the session.
     */
    public fun fetch() {
        if (extendedClientDetails == null) {
            UI.getCurrent().page.retrieveExtendedClientDetails { extendedClientDetails = it }
        }
    }

    /**
     * The time zone as reported by the browser. You need to populate the [extendedClientDetails] first (by calling [fetch]), otherwise the
     * UTC Time zone is going to be returned!
     *
     * This operation is instant since the time zone is stored in the session.
     *
     * Tips:
     * * To convert [Instant] to [LocalDateTime], `LocalDate` or `LocalTime`, call [toLocalDateTime].
     */
    public val get: ZoneId
        get() = extendedClientDetails?.timeZone ?: ZoneOffset.UTC

    /**
     * Utility function which converts given [instant] to this browser's [LocalDateTime].
     */
    public fun toLocalDateTime(instant: Instant): LocalDateTime = instant.atZone(get).toLocalDateTime()

    /**
     * Returns the current [ExtendedClientDetails], which is stored in the current session.
     * You need to populate this field first, by using [fetch],
     * otherwise this will return null.
     */
    public var extendedClientDetails: ExtendedClientDetails?
        get() = VaadinSession.getCurrent().getAttribute(ExtendedClientDetails::class.java)
        set(value) {
            VaadinSession.getCurrent().setAttribute(ExtendedClientDetails::class.java, value)
        }

    /**
     * Returns the current date and time at browser's current time zone.
     */
    public val currentDateTime: LocalDateTime
        get() = LocalDateTime.now(get)
}
