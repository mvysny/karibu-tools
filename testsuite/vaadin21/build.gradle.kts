dependencies {
    api(project(":testsuite:testbase"))
    api("com.github.mvysny.kaributesting:karibu-testing-v10:${properties["karibu_testing_version"]}") {
        exclude(module = "karibu-tools")
    }

    api("com.vaadin:vaadin-core:${properties["vaadin22_version"]}") {
        exclude(module = "fusion-endpoint") // exclude fusion: it brings tons of dependencies (including swagger)
    }
}
