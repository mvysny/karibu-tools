import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// A Vaadin Boot demo of the karibu-tools utilities: `./gradlew :testapp:run`, then http://localhost:8080
plugins {
    alias(libs.plugins.vaadin25)
    application
}

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget = JvmTarget.JVM_21
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(project(":karibu-tools-23"))
    implementation(libs.vaadin.v25.core)
    if (!vaadin.effective.productionMode.get()) {
        implementation(libs.vaadin.v25.dev)
    }
    implementation(libs.vaadin.boot)
    implementation(libs.slf4j.simple)

    testImplementation(libs.karibu.testing.v25) {
        exclude(module = "karibu-tools")
    }
    testImplementation(libs.junit5)
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "com.github.mvysny.kaributools.testapp.MainKt"
}
