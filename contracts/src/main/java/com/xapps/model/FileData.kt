package com.xapps.model

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.dto.FileUploadDTO
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class FileData(
    val id: String = UUID.randomUUID().toString().take(36),
    val fileName: String,
    val encodedFile: EncodedFile
)

enum class FileType(val mime: String) {

    TEXT("text/plain"),

    PDF("application/pdf"),

    JPEG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif"),
    BMP("image/bmp"),
    WEBP("image/webp"),

    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    DOC("application/msword"),

    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XLS("application/vnd.ms-excel"),

    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    PPT("application/vnd.ms-powerpoint"),

    BINARY("*/*");


    companion object {
        private val byMime = entries.associateBy { it.mime }

        fun fileTypeOrNull(mime: String): FileType? =
            byMime[mime]
    }
}


fun EncodedFile.fileType(): FileType {
    return when (this) {

        is EncodedFile.Text -> FileType.TEXT
        is EncodedFile.Pdf -> FileType.PDF

        is EncodedFile.Jpeg -> FileType.JPEG
        is EncodedFile.Png -> FileType.PNG
        is EncodedFile.Gif -> FileType.GIF
        is EncodedFile.Bmp -> FileType.BMP
        is EncodedFile.Webp -> FileType.WEBP

        is EncodedFile.Docx -> FileType.DOCX
        is EncodedFile.Doc -> FileType.DOC

        is EncodedFile.Xlsx -> FileType.XLSX
        is EncodedFile.Xls -> FileType.XLS

        is EncodedFile.Pptx -> FileType.PPTX
        is EncodedFile.Ppt -> FileType.PPT

        is EncodedFile.Binary -> FileType.BINARY
    }
}

fun FileData.fileType(): FileType = encodedFile.fileType()
fun FileUploadDTO.fileType(): FileType = encodedFile.fileType()

fun FileData.mime(): String = fileType().mime
fun FileUploadDTO.mime(): String = fileType().mime

fun FileData.extension(): String = fileType().extension()
fun FileUploadDTO.extension(): String = fileType().extension()

fun EncodedFile.toFileType(): FileType = when(this) {
    is EncodedFile.Binary -> FileType.BINARY
    is EncodedFile.Bmp -> FileType.BMP
    is EncodedFile.Doc -> FileType.DOC
    is EncodedFile.Docx -> FileType.DOCX
    is EncodedFile.Gif -> FileType.GIF
    is EncodedFile.Jpeg -> FileType.JPEG
    is EncodedFile.Pdf -> FileType.PDF
    is EncodedFile.Png -> FileType.PNG
    is EncodedFile.Ppt -> FileType.PPT
    is EncodedFile.Pptx -> FileType.PPTX
    is EncodedFile.Text -> FileType.TEXT
    is EncodedFile.Webp -> FileType.WEBP
    is EncodedFile.Xls -> FileType.XLS
    is EncodedFile.Xlsx -> FileType.XLSX
}

fun FileType.toEncodedFile(bytes: ByteArray) = when(this) {
    FileType.BINARY -> EncodedFile.Binary(bytes)
    FileType.BMP -> EncodedFile.Bmp(bytes)
    FileType.DOC -> EncodedFile.Doc(bytes)
    FileType.DOCX -> EncodedFile.Docx(bytes)
    FileType.GIF -> EncodedFile.Gif(bytes)
    FileType.JPEG -> EncodedFile.Jpeg(bytes)
    FileType.PNG -> EncodedFile.Png(bytes)
    FileType.PDF -> EncodedFile.Pdf(bytes)
    FileType.PPT -> EncodedFile.Ppt(bytes)
    FileType.PPTX -> EncodedFile.Pptx(bytes)
    FileType.TEXT -> EncodedFile.Text(bytes)
    FileType.WEBP -> EncodedFile.Webp(bytes)
    FileType.XLS -> EncodedFile.Xls(bytes)
    FileType.XLSX -> EncodedFile.Xlsx(bytes)
}

fun FileType.extension(): String = when(this) {

    FileType.TEXT -> "txt"
    FileType.PDF -> "pdf"

    FileType.JPEG -> "jpg"
    FileType.PNG -> "png"
    FileType.GIF -> "gif"
    FileType.BMP -> "bmp"
    FileType.WEBP -> "webp"

    FileType.DOCX -> "docx"
    FileType.DOC -> "doc"

    FileType.XLSX -> "xlsx"
    FileType.XLS -> "xls"

    FileType.PPTX -> "pptx"
    FileType.PPT -> "ppt"

    FileType.BINARY -> "bin"
}

fun FileData.toFileUploadDto(): FileUploadDTO {
    return FileUploadDTO(
        fileName = fileName,
        encodedFile = encodedFile
    )
}

