import com.github.mvysny.kaributools.MimeType
import kotlin.test.Test
import kotlin.test.expect

class MimeTypeTest {
    @Test fun parse() {
        expect(MimeType.AUDIO_MP3) { MimeType.of("audio/mpeg") }
        expect(MimeType.AUDIO) { MimeType.of("audio/*") }
        expect(MimeType.VIDEO_MPEG) { MimeType.of("video/mpeg") }
    }
    @Test fun testToString() {
        expect("audio/mpeg") { MimeType.AUDIO_MP3.toString() }
        expect("audio/*") { MimeType.AUDIO.toString() }
        expect("video/mpeg") { MimeType.VIDEO_MPEG.toString() }
    }
}