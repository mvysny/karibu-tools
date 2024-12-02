package com.github.mvysny.kaributools

import kotlin.test.Test
import kotlin.test.expect

private val String.v: SemanticVersion get() = SemanticVersion.fromString(this)

class SemanticVersionTest {
    @Test fun testtoString() {
        expect("1.2.3") { SemanticVersion(1, 2, 3).toString() }
        expect("1.2.3-beta2") { SemanticVersion(1, 2, 3, "beta2").toString() }
        expect("14.2.3-SNAPSHOT") { SemanticVersion(14, 2, 3, "SNAPSHOT").toString() }
    }
    @Test fun fromString() {
        expect(SemanticVersion(1, 2, 3)) { "1.2.3".v }
        expect("1.2.3-beta2") { "1.2.3-beta2".v.toString() }
        expect("1.2.3-beta2") { "1.2.3.beta2".v.toString() }
        expect("1.2.3-SNAPSHOT") { "1.2.3-SNAPSHOT".v.toString() }
        expect("1.2.0-SNAPSHOT") { "1.2-SNAPSHOT".v.toString() }
    }
    @Test fun `compare to`() {
        expect(true) { "14.2.28".v < "14.3.0".v }
        expect(true) { "14.3.0".v < "15.0.0".v }
        expect(true) { "16.0.2".v < "17.0.0".v }
        expect(true) { "14.3.0-beta2".v < "14.3.0".v }
        expect(true) { "1.2-SNAPSHOT".v < "1.2".v }
        expect(true) { "1.2-SNAPSHOT".v < "1.2.0".v }
        expect(true) { "1.2.0-SNAPSHOT".v < "1.2.0".v }
    }
    @Test fun `is at least major,minor`() {
        expect(true) { "14.3.0".v.isAtLeast(14, 2) }
        expect(true) { "14.3.0".v.isAtLeast(14, 3) }
        expect(true) { "14.3.0".v.isAtLeast(13, 0) }
        expect(false) { "14.3.0".v.isAtLeast(14, 4) }
        expect(true) { "14.3.1".v.isAtLeast(14, 2) }
        expect(true) { "14.3.1".v.isAtLeast(14, 3) }
        expect(true) { "14.3.0-alpha1".v.isAtLeast(14, 2) }
        expect(false) { "14.3.0-alpha1".v.isAtLeast(14, 3) }
        expect(false) { "14.3.0-alpha1".v.isAtLeast(14, 4) }
    }
    @Test fun `is at least major,minor,bugfix`() {
        expect(true) { "14.3.0".v.isAtLeast(14, 2, 0) }
        expect(true) { "14.3.0".v.isAtLeast(14, 2, 25) }
        expect(true) { "14.3.0".v.isAtLeast(14, 3, 0) }
        expect(false) { "14.3.0".v.isAtLeast(14, 3, 1) }
        expect(false) { "14.3.0".v.isAtLeast(14, 4, 0) }
        expect(true) { "14.3.1".v.isAtLeast(14, 2, 0) }
        expect(true) { "14.3.1".v.isAtLeast(14, 3, 0) }
        expect(true) { "14.3.1".v.isAtLeast(14, 3, 1) }
        expect(false) { "14.3.1".v.isAtLeast(14, 3, 2) }
        expect(true) { "14.3.0-alpha1".v.isAtLeast(14, 2, 0) }
        // yup: 14.3.0-alpha1 is not yet 14.3.0
        expect(false) { "14.3.0-alpha1".v.isAtLeast(14, 3, 0) }
        expect(false) { "14.3.0-alpha1".v.isAtLeast(14, 3, 1) }
        expect(false) { "14.3.0-alpha1".v.isAtLeast(14, 4, 0) }
    }
    @Test fun `is at least major`() {
        expect(true) { "14.3.0".v.isAtLeast(14) }
        expect(true) { "14.3.0".v.isAtLeast(13) }
        expect(false) { "14.3.0".v.isAtLeast(15) }
        expect(true) { "14.3.1".v.isAtLeast(14) }
        expect(true) { "14.3.1".v.isAtLeast(13) }
        expect(true) { "14.3.0-alpha1".v.isAtLeast(14) }
        expect(true) { "14.3.0-alpha1".v.isAtLeast(13) }
    }
    @Test fun `is exactly major`() {
        expect(true) { "14.3.0".v.isExactly(14) }
        expect(false) { "14.3.0".v.isExactly(13) }
        expect(false) { "14.3.0".v.isExactly(15) }
        expect(true) { "14.3.1".v.isExactly(14) }
        expect(false) { "14.3.1".v.isExactly(13) }
        expect(true) { "14.3.0-alpha1".v.isExactly(14) }
        expect(false) { "14.3.0-alpha1".v.isExactly(13) }
    }
    @Test fun `is exactly major,minor`() {
        expect(true) { "14.3.0".v.isExactly(14, 3) }
        expect(false) { "14.3.0".v.isExactly(14, 2) }
        expect(false) { "14.3.0".v.isExactly(15, 0) }
        expect(true) { "14.3.1".v.isExactly(14, 3) }
        expect(false) { "14.3.1".v.isExactly(14, 2) }
        expect(true) { "14.3.0-alpha1".v.isExactly(14, 3) }
        expect(false) { "14.3.0-alpha1".v.isExactly(14, 2) }
    }
    @Test fun `is at most major`() {
        expect(true) { "14.3.0".v.isAtMost(14) }
        expect(false) { "14.3.0".v.isAtMost(13) }
        expect(true) { "14.3.0".v.isAtMost(15) }
        expect(true) { "14.3.1".v.isAtMost(14) }
        expect(false) { "14.3.1".v.isAtMost(13) }
        expect(true) { "14.3.0-alpha1".v.isAtMost(14) }
        expect(false) { "14.3.0-alpha1".v.isAtMost(13) }
    }
    @Test fun `is at most major,minor`() {
        expect(true) { "14.3.0".v.isAtMost(14, 3) }
        expect(false) { "14.3.0".v.isAtMost(14, 2) }
        expect(false) { "14.3.0".v.isAtMost(13, 5) }
        expect(false) { "14.3.0".v.isAtMost(13, 2) }
        expect(true) { "14.3.0".v.isAtMost(15, 0) }
        expect(false) { "14.3.1".v.isAtMost(14, 0) }
        expect(true) { "14.3.1".v.isAtMost(14, 3, 1) }
        expect(false) { "14.3.1".v.isAtMost(14, 3, 0) }
        expect(true) { "14.3.1".v.isAtMost(14, 3) }
        expect(true) { "14.3.0-alpha1".v.isAtMost(14, 3) }
        expect(false) { "14.3.0-alpha1".v.isAtMost(13, 0) }
        expect(true) { "14.3.0-alpha1".v.isAtMost(14, 3, 1) }
        expect(false) { "14.3.1-alpha1".v.isAtMost(14, 3, 0) }
        expect(true) { "14.3.1-alpha1".v.isAtMost(14, 3) }
        expect(true) { "14.3.1-alpha1".v.isAtMost(14, 3, 1) }
    }
}
