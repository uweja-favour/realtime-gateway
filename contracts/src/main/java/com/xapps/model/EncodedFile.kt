package com.github.uwejafavour.studentapplication__.core.common.file.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class EncodedFile {

    abstract val bytes: ByteArray

    @Serializable
    @SerialName("text")
    data class Text(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("pdf")
    data class Pdf(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("jpeg")
    data class Jpeg(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("png")
    data class Png(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("gif")
    data class Gif(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("bmp")
    data class Bmp(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("webp")
    data class Webp(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("docx")
    data class Docx(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("doc")
    data class Doc(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("xlsx")
    data class Xlsx(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("xls")
    data class Xls(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("pptx")
    data class Pptx(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("ppt")
    data class Ppt(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    @Serializable
    @SerialName("binary")
    data class Binary(
        override val bytes: ByteArray
    ) : EncodedFile() {

        override fun equals(other: Any?): Boolean =
            super.equals(other)

        override fun hashCode(): Int =
            super.hashCode()

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this::class != other?.let { it::class }) return false
        if (!super.equals(other)) return false

        other as Text

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}