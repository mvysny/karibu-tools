dependencies {
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin22_version"]}")
    testImplementation(project(":testsuite")) {
        exclude(group = "com.vaadin")
    }
}
