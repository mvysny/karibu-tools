dependencies {
    api(project(":karibu-tools-23"))
    testImplementation(libs.vaadin.v23.core)
    testImplementation(project(":testsuite:vaadin14")) {
        exclude(group = "com.vaadin")
    }
    testImplementation(project(":testsuite:vaadin21")) {
        exclude(group = "com.vaadin")
    }
    testImplementation(project(":testsuite:vaadin23")) {
        exclude(group = "com.vaadin")
    }
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Vaadin 23 requires Java 11 or higher
tasks.test { onlyIf { JavaVersion.current() >= JavaVersion.VERSION_11 } }
