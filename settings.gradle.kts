rootProject.name = "karibu-tools-parent"

include(
    "karibu-tools",
    "karibu-tools-23",
    "testsuite:testbase",
    "testsuite:vaadin14",
    "testsuite:vaadin21",
    "testsuite:vaadin23",
    "testsuite:testrun-vaadin14-stable",
    "testsuite:testrun-vaadin14-next",
    "testsuite:testrun-vaadin22",
    "testsuite:testrun-vaadin23",
    "testsuite:testrun-vaadin24",
    "testsuite:testrun-vaadin24next",
    "testsuite:testrun-vaadin25",
    "testsuite:testrun-vaadin25next",
    "testsuite:testrun-hilla",
    "testsuite:testrun-hilla-prev",
    "testsuite:testrun-hilla1",
    "testsuite:testrun-vaadin-hilla-hybrid",
)
// the demo app runs Vaadin 25, whose Gradle plugin needs Java 21+; the JDK 17 CI job leaves it out
if (JavaVersion.current() >= JavaVersion.VERSION_21) {
    include("testapp")
}
