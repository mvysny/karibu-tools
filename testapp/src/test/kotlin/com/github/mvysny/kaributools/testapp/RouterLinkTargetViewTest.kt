package com.github.mvysny.kaributools.testapp

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributesting.v10.Routes
import com.github.mvysny.kaributesting.v10._click
import com.github.mvysny.kaributesting.v10._expectOne
import com.github.mvysny.kaributesting.v10._get
import com.github.mvysny.kaributools.target
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.AnchorTarget
import com.vaadin.flow.router.RouterLink
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

class RouterLinkTargetViewTest {
    private val routes = Routes().autoDiscoverViews("com.github.mvysny.kaributools.testapp")

    @BeforeEach fun setupVaadin() { MockVaadin.setup(routes) }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    @Test fun smoke() {
        _expectOne<RouterLinkTargetView>()
    }

    @Test fun targets() {
        expect(AnchorTarget.DEFAULT.value) { _get<RouterLink> { id = "noTarget" }.target.value }
        expect(null) { _get<RouterLink> { id = "noTarget" }.element.getAttribute("target") }
        expect("_self") { _get<RouterLink> { id = "self" }.element.getAttribute("target") }
        expect("_blank") { _get<RouterLink> { id = "newTab" }.element.getAttribute("target") }
        expect("_blank") { _get<Anchor> { id = "anchorNewTab" }.element.getAttribute("target") }
        expect("link-destination") { _get<Anchor> { id = "anchorNewTab" }.href }
    }

    @Test fun linksLeadToDestination() {
        _get<RouterLink> { id = "self" }._click()
        _expectOne<LinkDestinationView>()
    }
}
