dependencies {
    api(project(":testsuite:testbase"))
    api(libs.karibu.testing.v10) {
        exclude(module = "karibu-tools")
    }

    api(libs.vaadin.v14.core) {
        // Webjars are only needed when running in Vaadin 13 compatibility mode
        listOf("com.vaadin.webjar", "org.webjars.bowergithub.insites",
            "org.webjars.bowergithub.polymer", "org.webjars.bowergithub.polymerelements",
            "org.webjars.bowergithub.vaadin", "org.webjars.bowergithub.webcomponents")
            .forEach { exclude(group = it) }
    }
}
