package com.github.mvysny.kaributools

import com.vaadin.flow.server.InitialPageSettings
import org.jsoup.nodes.Element

/**
 * Appends a `<meta name="foo" content="baz">` element to the html head. Useful
 * for [com.vaadin.flow.server.BootstrapListener] as a replacement for
 * [com.vaadin.flow.server.PageConfigurator]'s [InitialPageSettings.addMetaTag].
 */
public fun Element.addMetaTag(name: String, content: String): Element =
    appendElement("meta").attr("name", name).attr("content", content)
