package com.github.mvysny.kaributools

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

class JsoupUtilsTest {
    private lateinit var html: Document
    @BeforeEach fun createHtmlDocument() {
        html = Jsoup.parse("<html><head></head><body></body></html>")
        html.outputSettings().prettyPrint(false)
    }

    @Test fun addMetaTag() {
        html.head().addMetaTag("foo", "bar")
        expect("""<html><head><meta name="foo" content="bar"></head><body></body></html>""") { html.toString() }
    }
}
