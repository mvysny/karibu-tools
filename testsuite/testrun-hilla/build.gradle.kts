import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    testImplementation(project(":testsuite:testbase"))
    testImplementation(libs.hilla.v2next)
}

// Vaadin 24 requires Java 17 or higher
tasks.test { onlyIf { JavaVersion.current() >= JavaVersion.VERSION_17 } }
