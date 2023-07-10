package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.*
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.kaributesting.v10.*
import com.vaadin.flow.component.UI
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.routerUtilsTests() {
    group("QueryParameters") {
        test("get") {
            expect(null) { QueryParameters.empty()["foo"] }
            expect("bar") { QueryParameters("foo=bar")["foo"] }
        }
        test("get fails with multiple parameters") {
            expectThrows(IllegalStateException::class, "Multiple values present for foo: [bar, baz]") {
                QueryParameters("foo=bar&foo=baz")["foo"]
            }
        }
        test("getValues") {
            expectList() { QueryParameters.empty().getValues("foo") }
            expectList("bar") { QueryParameters("foo=bar")
                .getValues("foo") }
            expectList("bar", "baz") { QueryParameters("foo=bar&foo=baz")
                .getValues("foo") }
        }
        test("isEmpty") {
            expect(true) { QueryParameters.empty().isEmpty }
            expect(false) { QueryParameters("foo=bar").isEmpty }
        }
        test("isNotEmpty") {
            expect(false) { QueryParameters.empty().isNotEmpty }
            expect(true) { QueryParameters("foo=bar").isNotEmpty }
        }
    }

    group("navigation tests") {
        beforeEach {
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
        afterEach { MockVaadin.tearDown() }

        group("navigateToView") {
            test("reified") {
                navigateTo<TestingView>()
                _get<TestingView>()
            }
            test("reified doesn't work on parametrized views") {
                expectThrows(IllegalArgumentException::class, "requires a parameter") {
                    // it fails somewhere deeply in Vaadin Flow
                    navigateTo<TestingParametrizedView>()
                }
                _expectNone<TestingParametrizedView>()
            }
            test("class") {
                navigateTo(TestingView::class)
                _get<TestingView>()
            }
            test("parametrized") {
                navigateTo(TestingParametrizedView::class, 1L)
                _get<TestingParametrizedView>()
            }
        }

        group("routerLinks") {
            test("no target") {
                expect("") { RouterLink().href }
            }
            test("simple target") {
                expect("testing") { RouterLink( "foo", TestingView::class.java).href }
            }
            test("parametrized target with missing param") {
                expectThrows(IllegalArgumentException::class, "requires a parameter") {
                    RouterLink("foo", TestingParametrizedView::class.java)
                }
            }
            test("parametrized target") {
                expect("testingp/1") { RouterLink("foo", TestingParametrizedView::class.java, 1L).href }
            }
            test("navigateTo") {
                RouterLink("foo", TestingView::class.java).navigateTo()
                _expectOne<TestingView>()
            }
        }

        group("getRouteUrl") {
            test("no target") {
                expect("") { getRouteUrl(RootView::class) }
            }
            test("no target + query parameters") {
                expect("?foo=bar") { getRouteUrl(RootView::class, "foo=bar") }
            }
            test("simple target") {
                expect("testing") { getRouteUrl(TestingView::class) }
            }
            test("simple target + query parameters") {
                expect("testing?foo=bar") { getRouteUrl(TestingView::class, "foo=bar") }
            }
            test("parametrized target with missing param") {
                expectThrows(IllegalArgumentException::class, "requires a parameter") {
                    getRouteUrl(TestingParametrizedView::class)
                }
            }
        }

        group("routeClass") {
            test("navigating to root view") {
                navigateTo<TestingView>()
                var called = false
                UI.getCurrent().addAfterNavigationListener {
                    called = true
                    expect(RootView::class.java) { it.routeClass }
                }
                navigateTo<RootView>()
                expect(true) { called }
            }
            test("navigating to TestingView") {
                var called = false
                UI.getCurrent().addAfterNavigationListener {
                    called = true
                    expect(TestingView::class.java) { it.routeClass }
                }
                navigateTo<TestingView>()
                expect(true) { called }
            }
            test("navigating to TestingView2") {
                var called = false
                UI.getCurrent().addAfterNavigationListener {
                    called = true
                    expect(TestingView::class.java) { it.routeClass }
                }
                navigateTo<TestingView>()
                expect(true) { called }
            }
            test("navigating to TestingParametrizedView") {
                var called = false
                UI.getCurrent().addAfterNavigationListener {
                    called = true
                    expect(TestingParametrizedView::class.java) { it.routeClass }
                }
                navigateTo(TestingParametrizedView::class, 25L)
                expect(true) { called }
            }
            test("navigating to TestingParametrizedView2") {
                var called = false
                UI.getCurrent().addAfterNavigationListener {
                    called = true
                    expect(TestingParametrizedView2::class.java) { it.routeClass }
                }
                navigateTo(TestingParametrizedView2::class, 25L)
                expect(true) { called }
            }
            test("location") {
                expect(RootView::class.java) { Location("").getRouteClass() }
                expect(TestingView::class.java) { Location("testing").getRouteClass() }
                expect(TestingView2::class.java) { Location("testing/foo/bar").getRouteClass() }
                expect(TestingParametrizedView::class.java) { Location("testingp/20").getRouteClass() }
                expect(TestingParametrizedView2::class.java) { Location("testingp/with/subpath/20").getRouteClass() }
                expect(null) { Location("nonexisting").getRouteClass() }
                expect(TestingView::class.java) { Location("testing?foo=bar").getRouteClass() }
            }
        }

        test("UI.currentViewLocation") {
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
    override fun setParameter(event: BeforeEvent, parameter: Long?) {
        parameter!!
    }
}
@Route("testingp/with/subpath")
class TestingParametrizedView2 : VerticalLayout(), HasUrlParameter<Long> {
    override fun setParameter(event: BeforeEvent, parameter: Long?) {
        parameter!!
    }
}

