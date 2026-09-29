package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.MockVaadin
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.test.expect

abstract class AbstractBrowserTimeZoneTests {
    @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
    @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }

    // Karibu's fake browser reports this zone
    private val helsinki: ZoneId = ZoneId.of("Europe/Helsinki")

    private fun fetchFromBrowser() {
        BrowserTimeZone.fetch()
        MockVaadin.clientRoundtrip()  // Karibu 2+ answers asynchronously, like a browser
    }

    @Test fun `UTC until fetched`() {
        expect(null) { BrowserTimeZone.extendedClientDetails }
        expect(ZoneOffset.UTC) { BrowserTimeZone.get }
    }

    @Test fun fetch() {
        fetchFromBrowser()
        expect(helsinki) { BrowserTimeZone.extendedClientDetails!!.timeZone }
        expect(helsinki) { BrowserTimeZone.get }
    }

    @Test fun `fetch keeps the stored details`() {
        fetchFromBrowser()
        val details = BrowserTimeZone.extendedClientDetails
        fetchFromBrowser()
        expect(details) { BrowserTimeZone.extendedClientDetails }
    }

    @Test fun toLocalDateTime() {
        fetchFromBrowser()
        expect(LocalDateTime.of(2024, 1, 1, 2, 0)) { BrowserTimeZone.toLocalDateTime(Instant.parse("2024-01-01T00:00:00Z")) }
        expect(LocalDateTime.of(2024, 7, 1, 3, 0)) { BrowserTimeZone.toLocalDateTime(Instant.parse("2024-07-01T00:00:00Z")) }
    }

    @Test fun currentDateTime() {
        fetchFromBrowser()
        val now = LocalDateTime.now(helsinki)
        expect(true) { Duration.between(now, BrowserTimeZone.currentDateTime).abs() < Duration.ofMinutes(1) }
        expect(true) { Duration.between(now, BrowserTimeZone.extendedClientDetails!!.currentDateTime).abs() < Duration.ofMinutes(1) }
    }
}
