package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaTest
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.test.expect

class JsoupUtilsTest : DynaTest({
    lateinit var html: Document
    beforeEach {
        html = Jsoup.parse("<html><head></head><body></body></html>")
        html.outputSettings().prettyPrint(false)
    }

    test("addMetaTag") {
        html.head().addMetaTag("foo", "bar")
        expect("""<html><head><meta name="foo" content="bar"></head><body></body></html>""") { html.toString() }
    }
})
