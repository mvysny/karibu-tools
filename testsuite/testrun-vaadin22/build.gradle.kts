dependencies {
    testImplementation(libs.vaadin.v22.core) {
        exclude(module = "fusion-endpoint") // exclude fusion: it brings tons of dependencies (including swagger)
    }
    testImplementation(project(":testsuite:vaadin14")) {
        exclude(group = "com.vaadin")
    }
    testImplementation(project(":testsuite:vaadin21")) {
        exclude(group = "com.vaadin")
    }
}
