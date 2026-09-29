kotlin {
    explicitApi()
}

dependencies {
    api(project(":karibu-tools"))
    // Vaadin
    compileOnly(libs.vaadin.v23.core)
    compileOnly(libs.javax.servletapi)
}

@Suppress("UNCHECKED_CAST")
val configureMavenCentral = ext["configureMavenCentral"] as (artifactId: String) -> Unit
configureMavenCentral("karibu-tools-23")
