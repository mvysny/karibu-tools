package com.github.mvysny.kaributools

import org.tomlj.Toml
import org.tomlj.TomlParseResult
import java.io.File

fun TomlParseResult.checkNoErrors(): TomlParseResult = apply {
    check(!hasErrors()) { "TOML parsing failed: ${errors()}" }
}
fun File.parseToml(): TomlParseResult = Toml.parse(absoluteFile.toPath()).checkNoErrors()
