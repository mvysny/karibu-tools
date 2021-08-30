package com.github.mvysny.kaributools

import com.vaadin.flow.component.dependency.NpmPackage
import com.vaadin.flow.server.Version
import com.vaadin.shrinkwrap.VaadinCoreShrinkWrap

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
        private val VERSION_REGEX = Regex("(\\d+)\\.(\\d+)\\.(\\d+)([-.](.*))?")

        /**
         * Parses the [version] string. Accepts the following formats:
         * * `major.minor.bugfix[-prerelease]`
         * * `major.minor.bugfix[.prerelease]`
         *
         * Always able to parse the output of
         * [SemanticVersion.toString].
         */
        public fun fromString(version: String): SemanticVersion {
            val match: MatchResult = requireNotNull(VERSION_REGEX.matchEntire(version)) {
                "The version must be in the form of major.minor.bugfix but is $version"
            }
            return SemanticVersion(
                match.groupValues[1].toInt(),
                match.groupValues[2].toInt(),
                match.groupValues[3].toInt(),
                match.groupValues[5].takeIf { it.isNotBlank() }
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
            Version.getRevision()
        )
    }

    /**
     * Returns a full Vaadin version.
     */
    public val get: SemanticVersion by lazy(LazyThreadSafetyMode.PUBLICATION) {
        // for Vaadin 14+ the version can be detected from the VaadinCoreShrinkWrap class.
        // This doesn't work for Vaadin 13 or lower, but nevermind - we only support Vaadin 14+ anyway.
        val annotation: NpmPackage = checkNotNull(
            VaadinCoreShrinkWrap::class.java.getAnnotation(NpmPackage::class.java),
            { "This version of Karibu-Testing only supports Vaadin 14 and higher" })
        val version: String = annotation.version
        SemanticVersion.fromString(version)
    }
}
