package com.github.mvysny.kaributools.testapp

import com.github.mvysny.kaributools.setOpenInNewTab
import com.github.mvysny.kaributools.target
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.AnchorTarget
import com.vaadin.flow.component.html.Paragraph
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouteConfiguration
import com.vaadin.flow.router.RouterLink

/**
 * Demoes `RouterLink.target` and `setOpenInNewTab()`. Each link leads to [LinkDestinationView], which
 * shows the UI id it was rendered in: the id of this tab means the router navigated in place, a new id
 * means the browser loaded the page anew.
 */
@Route("", layout = MainLayout::class)
@PageTitle("RouterLink target")
class RouterLinkTargetView : VerticalLayout() {
    init {
        add(Paragraph("This tab's UI id: ${UI.getCurrent().uiId}").apply { setId("uiId") })
        add(RouterLink("No target", LinkDestinationView::class.java).apply { setId("noTarget") })
        add(RouterLink("target=_self", LinkDestinationView::class.java).apply {
            setId("self")
            target = AnchorTarget.SELF
        })
        add(RouterLink("setOpenInNewTab()", LinkDestinationView::class.java).apply {
            setId("newTab")
            setOpenInNewTab()
        })
        // the same route through an Anchor, for comparison
        add(Anchor(RouteConfiguration.forSessionScope().getUrl(LinkDestinationView::class.java), "Anchor.setOpenInNewTab()").apply {
            setId("anchorNewTab")
            setOpenInNewTab()
        })
    }
}

@Route("link-destination", layout = MainLayout::class)
@PageTitle("Link destination")
class LinkDestinationView : VerticalLayout() {
    init {
        add(Paragraph("Rendered in UI id: ${UI.getCurrent().uiId}").apply { setId("uiId") })
        add(RouterLink("Back", RouterLinkTargetView::class.java))
    }
}
