kotlin {
    explicitApi()
}

dependencies {
    api(project(":karibu-tools"))
    // Vaadin
    compileOnly("com.vaadin:vaadin-core:${properties["vaadin23_version"]}")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")
}

val configureMavenCentral = ext["configureMavenCentral"] as (artifactId: String) -> Unit
configureMavenCentral("karibu-tools-23")
