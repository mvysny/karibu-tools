dependencies {
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin23_version"]}")
    testImplementation(project(":testsuite:vaadin14")) {
        exclude(group = "com.vaadin")
    }
    testImplementation(project(":testsuite:vaadin21")) {
        exclude(group = "com.vaadin")
    }
}
