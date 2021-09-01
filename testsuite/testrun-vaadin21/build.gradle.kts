dependencies {
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin21_version"]}")
    testImplementation(project(":testsuite")) {
        exclude(group = "com.vaadin")
    }
}
