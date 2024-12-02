dependencies {
    api(project(":karibu-tools"))
    // for testing purposes
    api(libs.slf4j.simple)
    api(libs.toml4j)
    api(libs.junit5)
    api(kotlin("test"))
}
