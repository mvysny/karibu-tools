package com.github.mvysny.kaributools

import com.github.mvysny.kaributesting.v10.MockVaadin
import com.vaadin.flow.server.StreamResource
import com.vaadin.flow.server.VaadinSession
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.expect

fun StreamResource.toByteArray(): ByteArray = ByteArrayOutputStream().apply { writer.accept(this,
    VaadinSession.getCurrent()) } .toByteArray()
fun StreamResource.readAsString(): String = toByteArray().toString(Charsets.UTF_8)

abstract class AbstractStreamResourceUtilsTests {
    @BeforeEach fun fakeVaadin() {
        MockVaadin.setup()
    }
    @AfterEach fun cleanup() {
        MockVaadin.tearDown()
    }

    @Nested inner class createStreamResourceTests {
        @Test fun name() {
            val r = createStreamResource("foo.txt") { byteArrayOf().inputStream() }
            expect("foo.txt") { r.name }
        }
        @Test fun emptyContents() {
            val r = createStreamResource("foo.txt") { byteArrayOf().inputStream() }
            expect("") { r.readAsString() }
        }
        @Test fun nonEmptyContents() {
            val r = createStreamResource("foo.txt") { "Hello".toByteArray().inputStream() }
            expect("Hello") { r.readAsString() }
        }
    }

    @Nested inner class fileToStreamResource {
        @TempDir private lateinit var tempDir: Path
        private lateinit var tempFile: File
        @BeforeEach fun initialize() {
            tempFile = Files.createFile(tempDir.resolve("foo.txt")).toFile()
            tempFile.writeText("")
        }
        @Test fun name() {
            var r = tempFile.toStreamResource()
            expect("foo.txt") { r.name }
            r = tempFile.toStreamResource("different.name")
            expect("different.name") { r.name }
        }
        @Test fun emptyContents() {
            val r = tempFile.toStreamResource()
            expect("") { r.readAsString() }
        }
        @Test fun nonEmptyContents() {
            tempFile.writeText("Hello")
            val r = tempFile.toStreamResource()
            expect("Hello") { r.readAsString() }
        }
    }

    @Nested inner class stringToStreamResource {
        @Test fun name() {
            val r = "".toStreamResource("foo.txt")
            expect("foo.txt") { r.name }
        }
        @Test fun emptyContents() {
            val r = "".toStreamResource("foo.txt")
            expect("") { r.readAsString() }
        }
        @Test fun nonEmptyContents() {
            val r = "Hello".toStreamResource("foo.txt")
            expect("Hello") { r.readAsString() }
        }
    }
}