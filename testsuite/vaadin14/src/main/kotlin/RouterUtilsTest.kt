package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.*
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.AnchorTarget
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractRouterUtilsTests {
    @Nested inner class queryParameters {
        @Test fun get() {
            expect(null) { QueryParameters.empty()["foo"] }
            expect("bar") { QueryParameters("foo=bar")["foo"] }
        }
        @Test fun `get fails with multiple parameters`() {
            expectThrows(IllegalStateException::class, "Multiple values present for foo: [bar, baz]") {
                QueryParameters("foo=bar&foo=baz")["foo"]
            }
        }
        @Test fun getValues() {
            expectList() { QueryParameters.empty().getValues("foo") }
            expectList("bar") { QueryParameters("foo=bar")
                .getValues("foo") }
            expectList("bar", "baz") { QueryParameters("foo=bar&foo=baz")
                .getValues("foo") }
        }
        @Test fun isEmpty() {
            expect(true) { QueryParameters.empty().isEmpty }
            expect(false) { QueryParameters("foo=bar").isEmpty }
        }
        @Test fun isNotEmpty() {
            expect(false) { QueryParameters.empty().isNotEmpty }
            expect(true) { QueryParameters("foo=bar").isNotEmpty }
        }
    }

    @Nested inner class `navigation tests` {
        @BeforeEach fun fakeVaadin() {
            MockVaadin.setup(Routes().apply { routes.addAll(listOf(
                TestingView::class.java,
                TestingParametrizedView::class.java,
                RootView::class.java,
                TestingParametrizedView2::class.java,
                TestingView2::class.java
            )) })
            _expectNone<TestingView>()
            _expectNone<TestingParametrizedView>()
        }
        @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

        @Nested inner class navigateToView {
            @Test fun reified() {
                navigateTo<TestingView>()
                _expectOne<TestingView>()
            }
            @Test fun `reified doesn't work on parametrized views`() {
                expectThrows(IllegalArgumentException::class, "requires a parameter") {
                    // it fails somewhere deeply in Vaadin Flow
                    navigateTo<TestingParametrizedView>()
                }
                _expectNone<TestingParametrizedView>()
            }
            @Test fun `class`() {
                navigateTo(TestingView::class)
                _expectOne<TestingView>()
            }
            @Test fun parametrized() {
                navigateTo(TestingParametrizedView::class, 1L)
                _expectOne<TestingParametrizedView>()
            }
            @Test fun `String url`() {
                navigateTo("testingp/1")
                _expectOne<TestingParametrizedView>()
                expect(1L) { _get<TestingParametrizedView>().param }
            }
            @Test fun `String url honors navigation trigger`() {
                lateinit var trigger: NavigationTrigger
                UI.getCurrent().addBeforeEnterListener { e -> trigger = e.trigger }
                navigateTo("testingp/1", NavigationTrigger.PAGE_LOAD)
                expect(NavigationTrigger.PAGE_LOAD) { trigger }
            }
        }

        @Nested inner class routerLinks {
            @Test fun `no target`() {
                expect("") { RouterLink().href }
            }
            @Test fun `simple target`() {
                expect("testing") { RouterLink( "foo", TestingView::class.java).href }
            }
            @Test fun `parametrized target with missing param`() {
                expectThrows(IllegalArgumentException::class, "requires a parameter") {
                    RouterLink("foo", TestingParametrizedView::class.java)
                }
            }
            @Test fun `parametrized target`() {
                expect("testingp/1") { RouterLink("foo", TestingParametrizedView::class.java, 1L).href }
            }
            @Test fun navigateTo() {
                RouterLink("foo", TestingView::class.java).navigateTo()
                _expectOne<TestingView>()
            }
        }

        @Nested inner class getRouteUrl() {
            @Test fun `no target`() {
                expect("") { getRouteUrl(RootView::class) }
            }
            @Test fun `no target + query parameters`() {
                expect("?foo=bar") { getRouteUrl(RootView::class, "foo=bar") }
            }
            @Test fun `simple target`() {
                expect("testing") { getRouteUrl(TestingView::class) }
            }
            @Test fun `simple target + query parameters`() {
                expect("testing?foo=bar") { getRouteUrl(TestingView::class, "foo=bar") }
            }
            @Test fun `parametrized target with missing param`() {
                expectThrows(IllegalArgumentException::class, "requires a parameter") {
                    getRouteUrl(TestingParametrizedView::class)
                }
            }
        }

        @Nested inner class routeClass {
            @Test fun `navigating to root view`() {
                navigateTo<TestingView>()
                var called = false
                UI.getCurrent().addAfterNavigationListener {
                    called = true
                    expect(RootView::class.java) { it.routeClass }
                }
                navigateTo<RootView>()
                expect(true) { called }
            }
            @Test fun `navigating to TestingView`() {
                var called = false
                UI.getCurrent().addAfterNavigationListener {
                    called = true
                    expect(TestingView::class.java) { it.routeClass }
                }
                navigateTo<TestingView>()
                expect(true) { called }
            }
            @Test fun `navigating to TestingView2`() {
                var called = false
                UI.getCurrent().addAfterNavigationListener {
                    called = true
                    expect(TestingView::class.java) { it.routeClass }
                }
                navigateTo<TestingView>()
                expect(true) { called }
            }
            @Test fun `navigating to TestingParametrizedView`() {
                var called = false
                UI.getCurrent().addAfterNavigationListener {
                    called = true
                    expect(TestingParametrizedView::class.java) { it.routeClass }
                }
                navigateTo(TestingParametrizedView::class, 25L)
                expect(true) { called }
            }
            @Test fun `navigating to TestingParametrizedView2`() {
                var called = false
                UI.getCurrent().addAfterNavigationListener {
                    called = true
                    expect(TestingParametrizedView2::class.java) { it.routeClass }
                }
                navigateTo(TestingParametrizedView2::class, 25L)
                expect(true) { called }
            }
            @Test fun location() {
                expect(RootView::class.java) { Location("").getRouteClass() }
                expect(TestingView::class.java) { Location("testing").getRouteClass() }
                expect(TestingView2::class.java) { Location("testing/foo/bar").getRouteClass() }
                expect(TestingParametrizedView::class.java) { Location("testingp/20").getRouteClass() }
                expect(TestingParametrizedView2::class.java) { Location("testingp/with/subpath/20").getRouteClass() }
                expect(null) { Location("nonexisting").getRouteClass() }
                expect(TestingView::class.java) { Location("testing?foo=bar").getRouteClass() }
            }
        }

        @Test fun `UI-currentViewLocation`() {
            var expectedLocation = if (VaadinVersion.get.major > 14) "" else "."
            var expectedLocationBefore = expectedLocation
            UI.getCurrent().addBeforeLeaveListener {
                expect(expectedLocationBefore) { UI.getCurrent().currentViewLocation.pathWithQueryParameters }
            }
            UI.getCurrent().addBeforeEnterListener {
                expect(expectedLocationBefore) { UI.getCurrent().currentViewLocation.pathWithQueryParameters }
            }
            UI.getCurrent().addAfterNavigationListener {
                expect(expectedLocation) { UI.getCurrent().currentViewLocation.pathWithQueryParameters }
            }
            expect(expectedLocation) { UI.getCurrent().currentViewLocation.pathWithQueryParameters }

            expectedLocation = "testing"
            navigateTo<TestingView>()
            expect("testing") { UI.getCurrent().currentViewLocation.pathWithQueryParameters }
            expectedLocationBefore = "testing"
        }
    }
    @Test fun `RouterLink-target`() {
        val rl = RouterLink()
        rl.target = AnchorTarget.DEFAULT
        expect(AnchorTarget.DEFAULT) { rl.target }
        rl.setOpenInNewTab()
        expect(AnchorTarget.BLANK) { rl.target }
        rl.target = AnchorTarget.DEFAULT
        expect(AnchorTarget.DEFAULT) { rl.target }
    }
    @Test fun `Anchor-target_`() {
        val rl = Anchor()
        expect(AnchorTarget.DEFAULT) { rl.target_ }
        rl.target_ = AnchorTarget.DEFAULT
        expect(AnchorTarget.DEFAULT) { rl.target_ }
        rl.setOpenInNewTab()
        expect(AnchorTarget.BLANK) { rl.target_ }
        rl.target_ = AnchorTarget.DEFAULT
        expect(AnchorTarget.DEFAULT) { rl.target_ }
    }
}

class AppLayout : VerticalLayout(), RouterLayout
@Route("")
class RootView : VerticalLayout()
@Route("testing", layout = AppLayout::class)
class TestingView : VerticalLayout()
@Route("testing/foo/bar")
class TestingView2 : VerticalLayout()
@Route("testingp")
class TestingParametrizedView : VerticalLayout(), HasUrlParameter<Long> {
    var param: Long? = null
    override fun setParameter(event: BeforeEvent, parameter: Long?) {
        param = parameter!!
    }
}
@Route("testingp/with/subpath")
class TestingParametrizedView2 : VerticalLayout(), HasUrlParameter<Long> {
    override fun setParameter(event: BeforeEvent, parameter: Long?) {
        parameter!!
    }
}

