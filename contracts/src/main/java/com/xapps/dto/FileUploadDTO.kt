package com.xapps.dto

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import kotlinx.serialization.*

@Serializable
data class FileUploadDTO(
    val fileName: String,
    val encodedFile: EncodedFile
)