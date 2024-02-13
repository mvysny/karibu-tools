dependencies {
    api(project(":testsuite:testbase"))
    api(project(":karibu-tools-23"))
    api(libs.karibu.testing.v10) {
        exclude(module = "karibu-tools")
    }

    api(libs.vaadin.v23.core)
}
