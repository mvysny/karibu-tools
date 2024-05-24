package com.github.mvysny.kaributools

import com.vaadin.flow.component.dependency.NpmPackage
import com.vaadin.flow.server.Version
import com.vaadin.flow.server.frontend.FrontendVersion
import com.vaadin.shrinkwrap.VaadinCoreShrinkWrap
import java.util.Optional
import java.util.Properties

/**
 * See https://semver.org/ for more details.
 */
public data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val bugfix: Int,
    val prerelease: String? = null
) : Comparable<SemanticVersion> {

    init {
        if (prerelease != null) {
            require(prerelease.isNotBlank()) { "$prerelease is blank" }
            require(!prerelease.startsWith('-')) { "$prerelease starts with a dash" }
        }
    }

    override fun compareTo(other: SemanticVersion): Int {
        return compareValuesBy(this, other,
            { it.major },
            { it.minor },
            { it.bugfix },
            // use the unicode character \ufffd when prerelease is null. That places the final version after any -beta versions or such.
            { it.prerelease ?: "\ufffd" })
    }

    /**
     * Formats the version in the following format: `major.minor.bugfix[-prerelease]`.
     */
    override fun toString(): String = "$major.$minor.$bugfix${if (prerelease != null) "-$prerelease" else ""}"

    public fun isExactly(major: Int, minor: Int): Boolean = this.major == major && this.minor == minor
    public fun isExactly(major: Int): Boolean = this.major == major
    @JvmOverloads // to keep binary compatibility
    public fun isAtLeast(major: Int, minor: Int, bugfix: Int = 0): Boolean = this >= SemanticVersion(major, minor, bugfix)
    public fun isAtLeast(major: Int): Boolean = this.major >= major
    public fun isAtMost(major: Int): Boolean = this.major <= major
    @JvmOverloads
    public fun isAtMost(major: Int, minor: Int, bugfix: Int = Int.MAX_VALUE): Boolean = this <= SemanticVersion(major, minor, bugfix)

    public companion object {

        /**
         * Parses the [version] string.
         *
         * Always able to parse the output of
         * [SemanticVersion.toString].
         */
        public fun fromString(version: String): SemanticVersion {
            val frontendVersion = FrontendVersion(version)
            return SemanticVersion(
                frontendVersion.majorVersion,
                frontendVersion.minorVersion,
                frontendVersion.revision,
                frontendVersion.buildIdentifier.takeIf { it.isNotEmpty() }
            )
        }
    }
}

public object VaadinVersion {
    /**
     * Vaadin Flow `flow-server.jar` version: for example 1.2.0 for Vaadin 12
     */
    public val flow: SemanticVersion by lazy(LazyThreadSafetyMode.PUBLICATION) {
        SemanticVersion(
            Version.getMajorVersion(),
            Version.getMinorVersion(),
            Version.getRevision(),
            Version.getBuildIdentifier().takeIf { it.isNotBlank() }
        )
    }

    /**
     * Calls `Platform.getVaadinVersion(): Optional<String>` function which is available since Vaadin 23+.
     *
     * Unfortunately this is not a reliable way, because of https://github.com/vaadin/flow/issues/17017
     */
    private fun platformGetVaadinVersion(): SemanticVersion? {
        val platformClass: Class<*>? = try {
            Class.forName("com.vaadin.flow.server.Platform")
        } catch (ex: ClassNotFoundException) { null }
        if (platformClass != null) {
            @Suppress("UNCHECKED_CAST")
            val vaadinVer: Optional<String> = platformClass.getDeclaredMethod("getVaadinVersion").invoke(null) as Optional<String>
            if (vaadinVer.isPresent) {
                return SemanticVersion.fromString(vaadinVer.get())
            }
        }
        return null
    }

    private fun getVaadinVersionFromPomProperties(): SemanticVersion? {
        val input = Thread.currentThread().contextClassLoader.getResourceAsStream("META-INF/maven/com.vaadin/vaadin-core/pom.properties") ?: return null
        val props = input.use { input2 -> Properties().apply { load(input2) } }
        val version: Any = props["version"] ?: return null
        return SemanticVersion.fromString(version.toString())
    }

    /**
     * Returns a full Vaadin version. Returns null if Vaadin is not on classpath (e.g. for pure Hilla apps).
     */
    public val vaadin: SemanticVersion? by lazy(LazyThreadSafetyMode.PUBLICATION) {
        getVaadinVersionFromPomProperties()
    }

    /**
     * Returns a full Vaadin version. Fails if Vaadin is not on the classpath.
     */
    public val get: SemanticVersion by lazy(LazyThreadSafetyMode.PUBLICATION) {
        // For Vaadin 14+ the version can be detected from the VaadinCoreShrinkWrap class.
        // This doesn't work for Vaadin 13 or lower, but nevermind - we only support Vaadin 14+ anyway.

        // For Vaadin 23 the way to obtain the version is different - VaadinCoreShrinkWrap no longer exists.

        // There's the `Platform.getVaadinVersion(): Optional<String>` function which we can use for Vaadin 23+.
        // See https://github.com/mvysny/karibu-tools/issues/4 for more info.
        // Unfortunately that's not a reliable way, because of https://github.com/vaadin/flow/issues/17017
        // val version = platformGetVaadinVersion()
        val version24 = getVaadinVersionFromPomProperties()
        if (version24 != null) {
            return@lazy version24
        }

        // no luck.
        // Starting from Vaadin 23, Flow & Vaadin share the same version - we can use that.
        val flowVersion = flow
        if (flowVersion.major >= 23) {
            return@lazy flowVersion
        }

        // fallback for Vaadin 14..22
        val annotation: NpmPackage = checkNotNull(VaadinCoreShrinkWrap::class.java.getAnnotation(NpmPackage::class.java)) {
            "Only Vaadin 14 and higher is supported"
        }
        val version: String = annotation.version
        SemanticVersion.fromString(version)
    }

    /**
     * The [Hilla](https://hilla.dev/) version, if on classpath. null if hilla is not on the classpath.
     */
    public val hilla: SemanticVersion? by lazy(LazyThreadSafetyMode.PUBLICATION) {
        val input = Thread.currentThread().contextClassLoader.getResourceAsStream("META-INF/maven/dev.hilla/hilla/pom.properties") ?: return@lazy null
        val props = input.use { input2 -> Properties().apply { load(input2) } }
        val version: Any = props["version"] ?: return@lazy null
        SemanticVersion.fromString(version.toString())
    }
}
