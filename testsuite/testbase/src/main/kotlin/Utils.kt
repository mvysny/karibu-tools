package com.github.mvysny.kaributools

import org.tomlj.Toml
import org.tomlj.TomlParseResult
import java.io.File
import kotlin.test.expect

fun TomlParseResult.checkNoErrors(): TomlParseResult = apply {
    check(!hasErrors()) { "TOML parsing failed: ${errors()}" }
}
fun File.parseToml(): TomlParseResult = Toml.parse(absoluteFile.toPath()).checkNoErrors()

/**
 * Expects that [actual] list of objects matches [expected] list of objects. Fails otherwise.
 */
fun <T> expectList(vararg expected: T, actual: ()->List<T>) {
    expect(expected.toList(), actual)
}
