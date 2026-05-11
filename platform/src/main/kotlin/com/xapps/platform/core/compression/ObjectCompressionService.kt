@file:OptIn(ExperimentalSerializationApi::class)
package com.xapps.platform.core.compression

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

class ObjectCompressionService(
    private val protoBuf: ProtoBuf = ProtoBuf
) {

    fun <T> compress(
        serializer: KSerializer<T>,
        data: T
    ): ByteArray {
        val serialized = protoBuf.encodeToByteArray(serializer, data)

        return ByteArrayOutputStream().use { byteStream ->
            GZIPOutputStream(byteStream).use { gzip ->
                gzip.write(serialized)
            }
            byteStream.toByteArray()
        }
    }

    fun <T> compress(
        serializer: KSerializer<List<T>>,
        data: List<T>
    ): ByteArray {
        val serialized = protoBuf.encodeToByteArray(serializer, data)

        return ByteArrayOutputStream().use { byteStream ->
            GZIPOutputStream(byteStream).use { gzip ->
                gzip.write(serialized)
            }
            byteStream.toByteArray()
        }
    }

    fun <T> decompress(
        serializer: KSerializer<T>,
        compressedData: ByteArray
    ): T {
        val decompressedBytes = ByteArrayInputStream(compressedData).use { input ->
            GZIPInputStream(input).use { gzip ->
                gzip.readBytes()
            }
        }

        return protoBuf.decodeFromByteArray(serializer, decompressedBytes)
    }
}

fun <T> ObjectCompressionService.compressList(
    elementSerializer: KSerializer<T>,
    data: List<T>
): ByteArray = compress(ListSerializer(elementSerializer), data)

fun <T> ObjectCompressionService.decompressList(
    elementSerializer: KSerializer<T>,
    compressedData: ByteArray
): List<T> = decompress(ListSerializer(elementSerializer), compressedData)