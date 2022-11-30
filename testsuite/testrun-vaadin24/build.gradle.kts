dependencies {
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin24_version"]}")
    testImplementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
    testImplementation(project(":testsuite:vaadin14")) {
        exclude(group = "com.vaadin")
    }
    testImplementation(project(":testsuite:vaadin21")) {
        exclude(group = "com.vaadin")
    }
}
