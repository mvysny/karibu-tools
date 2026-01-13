import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget = JvmTarget.JVM_17
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    api(project(":karibu-tools-23"))
    testImplementation(libs.vaadin.v24.core)
    testImplementation(libs.jakarta.servletapi)
    testImplementation(project(":testsuite:vaadin14")) {
        exclude(group = "com.vaadin")
        exclude(group = "com.github.mvysny.kaributesting")
    }
    testImplementation(project(":testsuite:vaadin21")) {
        exclude(group = "com.vaadin")
        exclude(group = "com.github.mvysny.kaributesting")
    }
    testImplementation(project(":testsuite:vaadin23")) {
        exclude(group = "com.vaadin")
    }
    testImplementation(libs.karibu.testing.v24) {
        exclude(module = "karibu-tools")
    }
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Vaadin 24 requires Java 17 or higher
tasks.test { onlyIf { JavaVersion.current() >= JavaVersion.VERSION_17 } }
