dependencies {
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin23_version"]}")
    testImplementation(project(":testsuite")) {
        exclude(group = "com.vaadin")
    }
}
