import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    testImplementation("com.vaadin:vaadin-core:${properties["vaadin24_prev_version"]}")
    testImplementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
    testImplementation(project(":testsuite:vaadin14")) {
        exclude(group = "com.vaadin")
        exclude(group = "com.github.mvysny.kaributesting")
    }
    testImplementation(project(":testsuite:vaadin21")) {
        exclude(group = "com.vaadin")
        exclude(group = "com.github.mvysny.kaributesting")
    }
    testImplementation("com.github.mvysny.kaributesting:karibu-testing-v24:2.0.2") {
        exclude(module = "karibu-tools")
    }
}

// Vaadin 24 requires Java 17 or higher
tasks.test { onlyIf { JavaVersion.current() >= JavaVersion.VERSION_17 } }