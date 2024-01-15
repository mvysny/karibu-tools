dependencies {
    api(project(":testsuite:testbase"))
    api(project(":karibu-tools-23"))
    api("com.github.mvysny.kaributesting:karibu-testing-v10:${properties["karibu_testing_version"]}") {
        exclude(module = "karibu-tools")
    }

    api("com.vaadin:vaadin-core:${properties["vaadin23_version"]}")
}
