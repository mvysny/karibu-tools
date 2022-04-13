dependencies {
    api(project.rootProject)
    // for testing purposes
    api("com.github.mvysny.dynatest:dynatest:${properties["dynatest_version"]}")
    api("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")
    api("com.github.mvysny.kaributesting:karibu-testing-v10:${properties["karibu_testing_version"]}") {
        exclude(module = "karibu-tools")
    }

    api("com.vaadin:vaadin-core:${properties["vaadin14_version"]}") {
        // Webjars are only needed when running in Vaadin 13 compatibility mode
        listOf("com.vaadin.webjar", "org.webjars.bowergithub.insites",
            "org.webjars.bowergithub.polymer", "org.webjars.bowergithub.polymerelements",
            "org.webjars.bowergithub.vaadin", "org.webjars.bowergithub.webcomponents")
            .forEach { exclude(group = it) }
    }
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
}
