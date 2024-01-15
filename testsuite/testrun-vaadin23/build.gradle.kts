dependencies {
    api(project(":karibu-tools-23"))
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin23_version"]}")
    testImplementation(project(":testsuite:vaadin14")) {
        exclude(group = "com.vaadin")
    }
    testImplementation(project(":testsuite:vaadin21")) {
        exclude(group = "com.vaadin")
    }
}

// Vaadin 23 requires Java 11 or higher
tasks.test { onlyIf { JavaVersion.current() >= JavaVersion.VERSION_11 } }
