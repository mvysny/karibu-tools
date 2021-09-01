package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.expectThrows
import java.io.Serializable
import java.time.LocalDate
import kotlin.test.expect

class MiscUtilsTest : DynaTest({
    test("getter") {
        expect("getFullName") { Person::class.java.getGetter("fullName").name }
        expect("getAlive") { Person::class.java.getGetter("alive").name }
        expectThrows(IllegalArgumentException::class, "No such field 'foo' in class com.github.mvysny.kaributools.Person; available properties: alive, class, dateOfBirth, fullName") {
            Person::class.java.getGetter("foo")
        }
    }
})

private data class Person(var fullName: String? = null, var dateOfBirth: LocalDate? = null, var alive: Boolean = false) : Serializable
