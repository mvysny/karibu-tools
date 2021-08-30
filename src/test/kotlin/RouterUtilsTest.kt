package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectList
import com.github.mvysny.dynatest.expectThrows
import com.vaadin.flow.router.QueryParameters
import kotlin.test.expect

class RouterUtilsTest : DynaTest({
    group("QueryParameters") {
        test("get") {
            expect(null) { QueryParameters.empty()["foo"] }
            expect("bar") { QueryParameters("foo=bar")["foo"] }
        }
        test("get fails with multiple parameters") {
            expectThrows(IllegalStateException::class, "Multiple values present for foo: [bar, baz]") {
                QueryParameters("foo=bar&foo=baz")["foo"]
            }
        }
        test("getValues") {
            expectList() { QueryParameters.empty().getValues("foo") }
            expectList("bar") { QueryParameters("foo=bar")
                .getValues("foo") }
            expectList("bar", "baz") { QueryParameters("foo=bar&foo=baz")
                .getValues("foo") }
        }
    }
})
