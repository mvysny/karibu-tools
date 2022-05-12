package com.github.mvysny.kaributools

import com.vaadin.flow.component.dependency.NpmPackage
import com.vaadin.flow.server.Version
import com.vaadin.flow.server.frontend.FrontendVersion
import com.vaadin.shrinkwrap.VaadinCoreShrinkWrap
import java.util.Optional

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
    public fun isAtLeast(major: Int, minor: Int): Boolean = this >= SemanticVersion(major, minor, 0, "\u0001")
    public fun isAtLeast(major: Int): Boolean = this.major >= major

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
     * Returns a full Vaadin version.
     */
    public val get: SemanticVersion by lazy(LazyThreadSafetyMode.PUBLICATION) {
        // For Vaadin 14+ the version can be detected from the VaadinCoreShrinkWrap class.
        // This doesn't work for Vaadin 13 or lower, but nevermind - we only support Vaadin 14+ anyway.

        // For Vaadin 23 the way to obtain the version is different - VaadinCoreShrinkWrap no longer exists.
        // There's the `Platform.getVaadinVersion(): Optional<String>` function which we can use for Vaadin 23+.
        // See https://github.com/mvysny/karibu-tools/issues/4 for more info.
        val platformClass: Class<*>? = try {
            Class.forName("com.vaadin.flow.server.Platform")
        } catch (ex: ClassNotFoundException) { null }
        if (platformClass != null) {
            val vaadinVer: Optional<String> = platformClass.getDeclaredMethod("getVaadinVersion").invoke(null) as Optional<String>
            if (vaadinVer.isPresent) {
                return@lazy SemanticVersion.fromString(vaadinVer.get())
            }
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
}
