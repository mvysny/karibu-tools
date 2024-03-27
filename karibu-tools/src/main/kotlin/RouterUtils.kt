package com.github.mvysny.kaributools

import com.vaadin.flow.component.Component
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.AnchorTarget
import com.vaadin.flow.component.html.AnchorTargetValue
import com.vaadin.flow.router.*
import com.vaadin.flow.server.VaadinService
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.reflect.KClass

/**
 * Navigates to given route: `navigateTo<AdminRoute>()`
 */
public inline fun <reified T: Component> navigateTo() {
    navigateTo(T::class)
}

/**
 * Navigates to given view: `navigateTo(AdminRoute::class)`
 */
public fun navigateTo(route: KClass<out Component>) {
    // can't use this: the signature of this function changed in Vaadin 24.1.2+ and the bytecode compiled for older Vaadins isn't portable.
//    UI.getCurrent().navigate(route.java)

    val m = UI::class.java.getMethod("navigate", Class::class.java)
    try {
        m.invoke(UI.getCurrent(), route.java)
    } catch (e: InvocationTargetException) {
        throw e.cause!!
    }
}

/**
 * Navigates to given route with parameters: `navigateTo(DocumentRoute::class, 25L)`.
 * @param param typically a non-null parameter, but may be null in case of route's optional parameter.
 */
public fun <C, T> navigateTo(route: KClass<out T>, param: C?) where T: Component, T: HasUrlParameter<C> {
    // don't use this fun with reified C - when there is a parameter T, that would require the user to write something like this:
    // navigateToView<Long, EditArticleView>(article.id!!)   // note the Long

    // can't use this: the signature of this function changed in Vaadin 24.1.2+ and the bytecode compiled for older Vaadins isn't portable.
//    UI.getCurrent().navigate(route.java, param)

    val m = UI::class.java.getMethod("navigate", Class::class.java, Object::class.java)
    try {
        m.invoke(UI.getCurrent(), route.java, param)
    } catch (e: InvocationTargetException) {
        throw e.cause!!
    }
}

/**
 * Returns [RouteConfiguration] for this router.
 */
public val Router.configuration: RouteConfiguration get() = RouteConfiguration.forRegistry(registry)

/**
 * Calculates a URL to given [route], with given [routeParameters] and
 * [queryParameters]. Useful in combination with [navigateTo].
 */
public fun getRouteUrl(
    route: KClass<out Component>,
    routeParameters: RouteParameters = RouteParameters.empty(),
    queryParameters: QueryParameters = QueryParameters.empty()
): String {
    val ui = UI.getCurrent()!!
    val configuration: RouteConfiguration = ui.internals.router.configuration
    var url = configuration.getUrl(route.java, routeParameters)
    if (queryParameters.isNotEmpty) {
        url += "?" + queryParameters.queryString
    }
    return url
}

/**
 * Calculates a URL to given [route], with given [queryParameters]. Useful in combination with [navigateTo].
 *
 * Examples:
 *
 * * `navigateTo(getRouteUrl(AdminRoute::class, "lang=en"))` will construct something like `admin?lang=en`.
 */
public fun getRouteUrl(
    route: KClass<out Component>,
    queryParameters: String
): String {
    var url = getRouteUrl(route)
    if (queryParameters.isNotBlank()) {
        url += "?" + queryParameters.trim()
    }
    return url
}

/**
 * Navigates to any kind of link within the current UI, including optional query parameters:
 *
 * * `""` (empty string) - the root view.
 * * `foo/bar` - navigates to a view
 * * `foo/25` - navigates to a view with parameters
 * * `foo/25?token=bar` - any view with parameters and query parameters
 * * `?token=foo` - the root view with query parameters
 *
 * Tip: Use [getRouteUrl] to construct the [location] string.
 */
public fun navigateTo(location: String, trigger: NavigationTrigger = NavigationTrigger.UI_NAVIGATE) {
    val ui = UI.getCurrent()!!
    ui.internals.router.navigate(ui, Location(location), trigger)
}

/**
 * Navigates to the target of this link; this only works for links within this app.
 */
public fun RouterLink.navigateTo(trigger: NavigationTrigger = NavigationTrigger.UI_NAVIGATE) {
    navigateTo(href, trigger)
}

/**
 * Returns [UI.getRouter]/[VaadinService.router], whichever returns a non-null value.
 */
private fun getRouter(): Router {
    @Suppress("DEPRECATION")
    var router: Router? = UI.getCurrent()?.router
    if (router == null) {
        router = VaadinService.getCurrent().router
    }
    if (router == null) {
        throw IllegalStateException("Implicit router instance is not available. Pass in the Router instance explicitly.")
    }
    return router
}

/**
 * Set the [navigationTarget] for this link.
 */
public fun RouterLink.setRoute(navigationTarget: KClass<out Component>) {
    setRoute(getRouter(), navigationTarget.java)
}

/**
 * Set the [navigationTarget] for this link.
 * @param parameter url parameter for navigation target
 * @param T url parameter type
 * @param C navigation target type
 */
public fun <T, C> RouterLink.setRoute(navigationTarget: KClass<out C>, parameter: T) where C: Component, C: HasUrlParameter<T> {
    setRoute(getRouter(), navigationTarget.java, parameter)
}

/**
 * Returns the navigated-to route class.
 */
public val AfterNavigationEvent.routeClass: Class<out Component> get() =
    (activeChain.first() as Component).javaClass

/**
 * Finds a view mapped to this location.
 * @param router router to use, defaults to [UI.getRouter]/[VaadinService.router].
 */
public fun Location.getRouteClass(router: Router = getRouter()): Class<out Component>? {
    val navigationTarget: Optional<NavigationState> =
        router.resolveNavigationTarget("/$path", queryParameters.parameters.mapValues { it.value.toTypedArray() })
    return navigationTarget.orElse(null)?.navigationTarget
}

/**
 * Returns the singleton value associated with given [parameterName].
 * Returns null if there is no such parameter.
 * @throws IllegalStateException if the parameter has 2 or more values.
 */
public operator fun QueryParameters.get(parameterName: String): String? {
    val value = getValues(parameterName)
    return when {
        value.isEmpty() -> null
        value.size == 1 -> value.first()
        else -> throw IllegalStateException("Multiple values present for $parameterName: $value")
    }
}

/**
 * Returns the values associated with given [parameterName]. Returns an empty list
 * if there is no such parameter.
 */
public fun QueryParameters.getValues(parameterName: String): List<String> =
    parameters[parameterName] ?: listOf()

/**
 * Parses given query as a QueryParameters.
 * @param query the query string e.g. `foo=bar&quak=foo`; the parameters may repeat.
 */
public fun QueryParameters(query: String): QueryParameters = when {
    query.isBlank() -> QueryParameters.empty()
    else -> Location("?${query.trim('?')}").queryParameters
}

/**
 * Checks whether there are any query parameters.
 */
public val QueryParameters.isEmpty: Boolean get() = parameters.isEmpty()

/**
 * Checks whether there are no query parameters.
 */
public inline val QueryParameters.isNotEmpty: Boolean get() = !isEmpty

/**
 * Configures this [Anchor] to open the target in new tab/window.
 *
 * Effectively sets [AnchorTarget.BLANK].
 */
public fun Anchor.setOpenInNewTab() {
    setTarget(AnchorTarget.BLANK)
}

/**
 * Sets the target of this anchor ([RouterLink] is technically also an Anchor element).
 */
public var RouterLink.target: AnchorTargetValue
    get() = element.getAttribute("target")
        ?.let { AnchorTargetValue.forString(it) } ?: AnchorTarget.DEFAULT
    set(value) {
        element.setOrRemoveAttributeIfNullOrEmpty("target", value.value)
    }

/**
 * Configures this [RouterLink] to open the target in new tab/window.
 *
 * Effectively sets [AnchorTarget.BLANK].
 */
public fun RouterLink.setOpenInNewTab() {
    target = AnchorTarget.BLANK
}

/**
 * Sets the target of this anchor ([RouterLink] is technically also an Anchor element).
 */
public var Anchor.target_: AnchorTargetValue
    get() = targetValue
    set(value) {
        setTarget(value)
    }
