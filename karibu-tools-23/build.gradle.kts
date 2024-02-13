kotlin {
    explicitApi()
}

dependencies {
    api(project(":karibu-tools"))
    // Vaadin
    compileOnly(libs.vaadin.v23.core)
    compileOnly(libs.javax.servletapi)
}

val configureMavenCentral = ext["configureMavenCentral"] as (artifactId: String) -> Unit
configureMavenCentral("karibu-tools-23")
