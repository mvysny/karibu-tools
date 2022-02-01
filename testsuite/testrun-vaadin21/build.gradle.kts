dependencies {
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin21_version"]}")
    testImplementation(project(":testsuite:vaadin14")) {
        exclude(group = "com.vaadin")
    }
    testImplementation(project(":testsuite:vaadin21")) {
        exclude(group = "com.vaadin")
    }
}
