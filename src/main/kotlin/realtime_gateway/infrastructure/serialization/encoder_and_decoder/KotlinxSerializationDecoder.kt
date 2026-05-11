package realtime_gateway.infrastructure.serialization.encoder_and_decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.reactivestreams.Publisher
import org.springframework.core.ResolvableType
import org.springframework.core.codec.AbstractDecoder
import org.springframework.core.codec.DecodingException
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.util.MimeType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import realtime_gateway.infrastructure.serialization.ReflectiveSerializerResolver
import java.io.ByteArrayInputStream

/**
 * Non-blocking Kotlinx Serialization Decoder for WebFlux.
 */
class KotlinxSerializationDecoder(
    private val json: Json,
    private val resolver: ReflectiveSerializerResolver
) : AbstractDecoder<Any>(
    MediaType.APPLICATION_JSON,
    MediaType("application", "*+json")
) {

    override fun canDecode(elementType: ResolvableType, mimeType: MimeType?): Boolean =
        runCatching { resolver.resolve(elementType) }.isSuccess

    @OptIn(ExperimentalSerializationApi::class)
    override fun decodeToMono(
        input: Publisher<DataBuffer>,
        elementType: ResolvableType,
        mimeType: MimeType?,
        hints: Map<String, Any>?
    ): Mono<Any> {
        val serializer: KSerializer<Any> =
            runCatching {
                resolver.resolve(elementType)
            }.getOrElse {
                return Mono.error(DecodingException(it.message ?: "Cannot resolve serializer", it))
            } as KSerializer<Any>

        return Flux.from(input)
            .flatMap { buffer ->
                val bytes = ByteArray(buffer.readableByteCount())
                buffer.read(bytes)
                DataBufferUtils.release(buffer)
                Mono.just(bytes)
            }
            .collectList()
            .map { chunks ->
                val allBytes = chunks.fold(ByteArray(0)) { acc, b -> acc + b }
                json.decodeFromStream(serializer, ByteArrayInputStream(allBytes))
            }
    }

    override fun decode(
        input: Publisher<DataBuffer>,
        elementType: ResolvableType,
        mimeType: MimeType?,
        hints: Map<String, Any>?
    ): Flux<Any> =
        decodeToMono(input, elementType, mimeType, hints).flux()

    override fun decode(
        dataBuffer: DataBuffer,
        targetType: ResolvableType,
        mimeType: MimeType?,
        hints: Map<String, Any>?
    ): Any {
        val bytes = ByteArray(dataBuffer.readableByteCount())
        dataBuffer.read(bytes)
        DataBufferUtils.release(dataBuffer)

        val serializer: KSerializer<Any> =
            runCatching {
                resolver.resolve(targetType)
            }.getOrElse {
                throw DecodingException(it.message ?: "Cannot resolve serializer", it)
            } as KSerializer<Any>

        return json.decodeFromStream(serializer, ByteArrayInputStream(bytes))
    }
}
