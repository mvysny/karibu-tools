dependencies {
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin20_version"]}")
    testImplementation(project(":testsuite")) {
        exclude(group = "com.vaadin")
    }
}
