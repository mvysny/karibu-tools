dependencies {
    api(project(":testsuite:testbase"))
    api(libs.karibu.testing.v10) {
        exclude(module = "karibu-tools")
    }

    api(libs.vaadin.v22.core) {
        exclude(module = "fusion-endpoint") // exclude fusion: it brings tons of dependencies (including swagger)
    }
}
