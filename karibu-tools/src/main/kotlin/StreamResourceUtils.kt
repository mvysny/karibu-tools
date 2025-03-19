package com.github.mvysny.kaributools

import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.Charset

/**
 * [createStreamResource] shorthand for convenience
 * See also comments on [StreamResource] constructor
 */
public fun createStreamResource(name: String, getStream: () -> InputStream): StreamResource =
    StreamResource(
        name,
        InputStreamFactory {
            return@InputStreamFactory getStream()
        }
    )

/**
 * [toStreamResource] shorthand for convenience
 *
 * @param contentType See comments on [StreamResource.setContentType], e.g. mime type like "image/jpeg"
 *
 * Example of usage:
 * ```kotlin
 * image(imageBytes.toStreamResource(imageName, "image/jpeg"), imageName)
 * ```
 */
public fun ByteArray.toStreamResource(name: String, contentType: String? = null) = run {
    createStreamResource(name) { ByteArrayInputStream(this) }.apply {
        if (contentType != null) setContentType(contentType)
    }
}

/**
 * [toStreamResource] shorthand for convenience
 *
 * @param contentType See comments on [StreamResource.setContentType], e.g. mime type like "image/jpeg"
 *
 * Example of usage:
 * ```kotlin
 * image(File("foo.jpeg").toStreamResource("image/jpeg"), "foo.jpeg")
 * ```
 */
public fun File.toStreamResource(name: String? = null, contentType: String? = null) = run {
    createStreamResource(name ?: this.name) { FileInputStream(this) }.apply {
        if (contentType != null) setContentType(contentType)
    }
}

/**
 * [toStreamResource] shorthand for convenience
 *
 * @param contentType See comments on [StreamResource.setContentType], e.g. mime type like "text/plain"
 *
 * Example of usage:
 * ```kotlin
 * "foo".toStreamResource("foo-name", "text/plain")
 * ```
 */
public fun String.toStreamResource(name: String, contentType: String? = null, charset: Charset = Charsets.UTF_8) = run {
    createStreamResource(name) { this.byteInputStream(charset) }.apply {
        if (contentType != null) setContentType(contentType)
    }
}
