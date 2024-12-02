package com.github.mvysny.kaributools

import elemental.json.JsonObject
import elemental.json.impl.JreJsonFactory
import org.junit.jupiter.api.assertThrows
import java.io.Serializable
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.expect

class MiscUtilsTest {
    @Test fun getter() {
        expect("getFullName") { Person::class.java.getGetter("fullName").name }
        expect("getAlive") { Person::class.java.getGetter("alive").name }
        val ex = assertThrows<IllegalArgumentException> {
            Person::class.java.getGetter("foo")
        }
        expect("No such field 'foo' in class com.github.mvysny.kaributools.Person; available properties: alive, class, dateOfBirth, fullName") { ex.message }
    }

    @Test fun isNull() {
        expect(true) { (null as JsonObject?).isNull }
        expect(true) { JreJsonFactory().createNull().isNull }
        expect(false) { JreJsonFactory().create("foo").isNull }
        expect(false) { JreJsonFactory().create(false).isNull }
        expect(false) { JreJsonFactory().createArray().isNull }
        expect(false) { JreJsonFactory().createObject().isNull }
    }
}

private data class Person(var fullName: String? = null, var dateOfBirth: LocalDate? = null, var alive: Boolean = false) : Serializable
