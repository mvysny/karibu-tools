package com.github.mvysny.kaributools

/**
 * NOTE: data class instead of enum class to support producing [MimeType] from arbitrary [contentSubType]
 */
public data class MimeType(val mimeContentType: ContentType, val contentSubType: String = "*") {

    @Suppress("unused")
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

    @Suppress("unused")
    public companion object {

        public val PLAIN: MimeType = MimeType(ContentType.TEXT, "plain")
        public val HTML: MimeType = MimeType(ContentType.TEXT, "html")
        public val XLS: MimeType = MimeType(ContentType.APPLICATION, "vnd.ms-excel")
        public val XLSX: MimeType = MimeType(ContentType.APPLICATION, "vnd.openxmlformats-officedocument.spreadsheetml.sheet")

        public val IMAGE: MimeType = MimeType(ContentType.IMAGE)
        public val JPEG: MimeType = MimeType(ContentType.IMAGE, "jpeg")

        public val AUDIO: MimeType = MimeType(ContentType.AUDIO)
        public val MP3: MimeType = MimeType(ContentType.AUDIO, "mpeg")

        public val VIDEO: MimeType = MimeType(ContentType.VIDEO)
        public val AVI: MimeType = MimeType(ContentType.VIDEO, "x-msvideo")
        public val MPEG: MimeType = MimeType(ContentType.VIDEO, "mpeg")
        public val MP4: MimeType = MimeType(ContentType.VIDEO, "mp4")

    }

    override fun toString(): String = "$mimeContentType/$contentSubType"

}