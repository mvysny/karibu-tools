dependencies {
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin14_next_version"]}") {
        // Webjars are only needed when running in Vaadin 13 compatibility mode
        listOf("com.vaadin.webjar", "org.webjars.bowergithub.insites",
            "org.webjars.bowergithub.polymer", "org.webjars.bowergithub.polymerelements",
            "org.webjars.bowergithub.vaadin", "org.webjars.bowergithub.webcomponents")
            .forEach { exclude(group = it) }
    }
    testImplementation(project(":testsuite:vaadin14")) {
        exclude(group = "com.vaadin")
    }
}
