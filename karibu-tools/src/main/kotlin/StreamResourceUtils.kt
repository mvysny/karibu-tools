package com.github.mvysny.kaributools

import com.vaadin.flow.server.InputStreamFactory
import com.vaadin.flow.server.StreamResource
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.Charset

/**
 * Creates a [StreamResource], downloading file with given [name] and with contents
 * provided by [streamProvider].
 */
public fun createStreamResource(name: String, contentType: MimeType? = null, streamProvider: () -> InputStream): StreamResource =
    StreamResource(
        name,
        InputStreamFactory { streamProvider() }
    ).apply {
        if (contentType != null) setContentType(contentType.toString())
    }

/**
 * Returns a [StreamResource] reading contents of this byte array.
 *
 * @param contentType [MimeType]
 * @return [StreamResource] which reads the contents of this [ByteArray]
 *
 * Example of usage:
 * ```kotlin
 * image(imageBytes.toStreamResource(imageName, MimeType.JPEG), imageName)
 * ```
 */
public fun ByteArray.toStreamResource(name: String, contentType: MimeType? = null): StreamResource =
    createStreamResource(name, contentType) { inputStream() }

/**
 * Returns a [toStreamResource] which reads contents of given file.
 *
 * @param contentType [MimeType]
 * @param bufferSize See [BufferedInputStream] size parameter
 * @return [StreamResource] which reads the contents of this [File]
 *
 * Example of usage:
 * ```kotlin
 * image(File("foo.jpeg").toStreamResource("image/jpeg"), MimeType.JPEG)
 * ```
 */
public fun File.toStreamResource(name: String? = null, contentType: MimeType? = null, bufferSize: Int = 8192) =
    createStreamResource(name ?: this.name, contentType) {
        BufferedInputStream(FileInputStream(this), bufferSize)
    }

/**
 * Returns a [toStreamResource] which reads contents of this String.
 *
 * @param name the file name
 * @param contentType [MimeType]
 * @param charset the charset to use, defaults to [Charsets.UTF_8].
 * @return [StreamResource] which reads the contents of this [String]
 *
 * Example of usage:
 * ```kotlin
 * "foo".toStreamResource("foo-name", MimeType.TEXT_PLAIN)
 * ```
 */
public fun String.toStreamResource(name: String, contentType: MimeType? = null, charset: Charset = Charsets.UTF_8) =
    createStreamResource(name, contentType) { byteInputStream(charset) }
