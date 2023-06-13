import java.io.File
import java.io.InputStream
import java.io.Reader
import java.util.*

fun File.loadAsProperties(): Properties = absoluteFile.reader().loadAsProperties()
fun InputStream.loadAsProperties(): Properties = use { stream -> Properties().apply { load(stream.buffered()) } }
fun Reader.loadAsProperties(): Properties = use { stream -> Properties().apply { load(stream.buffered()) } }
