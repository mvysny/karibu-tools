dependencies {
    api(project.rootProject)
    // for testing purposes
    api("com.github.mvysny.dynatest:dynatest:${properties["dynatest_version"]}")
    api("org.slf4j:slf4j-simple:${properties["slf4j_version"]}")
}
