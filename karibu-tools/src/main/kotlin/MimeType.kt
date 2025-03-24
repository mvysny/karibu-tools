package com.github.mvysny.kaributools

/**
 * Represents the MIME type.
 */
public data class MimeType(val mimeContentType: ContentType, val contentSubType: String = "*") {

    public enum class ContentType(public val value: String) {
        APPLICATION("application"),
        AUDIO("audio"),
        EXAMPLE("example"),
        FONT("font"),
        IMAGE("image"),
        MESSAGE("message"),
        MODEL("model"),
        MULTIPART("multipart"),
        TEXT("text"),
        VIDEO("video"),
        CHEMICAL("chemical"),
        FLV_APPLICATION("flv-application"),
        INODE("inode"),
        WWW("www"),
        X_CONFERENCE("x-conference"),
        X_CONTENT("x-content"),
        X_DIRECTORY("x-directory"),
        X_EPOC("x-epoc"),
        X_WORLD("x-world"),
        ZZ_APPLICATION("zz-application"),
        STAR("*");
    }

    public companion object {

        public val TEXT_PLAIN: MimeType = MimeType(ContentType.TEXT, "plain")
        public val TEXT_HTML: MimeType = MimeType(ContentType.TEXT, "html")
        public val XLS: MimeType = MimeType(ContentType.APPLICATION, "vnd.ms-excel")
        public val XLSX: MimeType = MimeType(ContentType.APPLICATION, "vnd.openxmlformats-officedocument.spreadsheetml.sheet")

        public val IMAGE: MimeType = MimeType(ContentType.IMAGE)
        public val IMAGE_JPEG: MimeType = MimeType(ContentType.IMAGE, "jpeg")

        public val AUDIO: MimeType = MimeType(ContentType.AUDIO)
        public val AUDIO_MP3: MimeType = MimeType(ContentType.AUDIO, "mpeg")

        public val VIDEO: MimeType = MimeType(ContentType.VIDEO)
        public val VIDEO_AVI: MimeType = MimeType(ContentType.VIDEO, "x-msvideo")
        public val VIDEO_MPEG: MimeType = MimeType(ContentType.VIDEO, "mpeg")
        public val VIDEO_MP4: MimeType = MimeType(ContentType.VIDEO, "mp4")

        public fun of(mimeType: String): MimeType {
            val parts = mimeType.split('/')
            require(parts.size == 2) { "Invalid mimeType: $mimeType" }
            val contentType = parts[0]
            val ct = ContentType.entries.firstOrNull { it.value == contentType }
            requireNotNull(ct) { "Invalid mimeType: $contentType" }
            return MimeType(ct, parts[1])
        }
    }

    override fun toString(): String = "$mimeContentType/$contentSubType"
}