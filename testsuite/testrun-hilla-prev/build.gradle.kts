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
    testImplementation(project(":testsuite:testbase"))
    testImplementation(libs.hilla.v2)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Vaadin 24 requires Java 17 or higher
tasks.test { onlyIf { JavaVersion.current() >= JavaVersion.VERSION_17 } }
