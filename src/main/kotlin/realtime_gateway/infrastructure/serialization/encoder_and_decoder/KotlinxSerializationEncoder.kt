package realtime_gateway.infrastructure.serialization.encoder_and_decoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.reactivestreams.Publisher
import org.springframework.core.ResolvableType
import org.springframework.core.codec.AbstractEncoder
import org.springframework.core.codec.EncodingException
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.MediaType
import org.springframework.util.MimeType
import reactor.core.publisher.Flux
import realtime_gateway.infrastructure.serialization.ReflectiveSerializerResolver
import java.io.ByteArrayOutputStream

/**
 * Non-blocking Kotlinx Serialization Encoder for WebFlux.
 */
class KotlinxSerializationEncoder(
    private val json: Json,
    private val resolver: ReflectiveSerializerResolver
) : AbstractEncoder<Any>(
    MediaType.APPLICATION_JSON,
    MediaType("application", "*+json")
) {

    override fun canEncode(elementType: ResolvableType, mimeType: MimeType?): Boolean =
        runCatching { resolver.resolve(elementType) }.isSuccess

    @OptIn(ExperimentalSerializationApi::class)
    override fun encode(
        input: Publisher<out Any>,
        bufferFactory: DataBufferFactory,
        elementType: ResolvableType,
        mimeType: MimeType?,
        hints: Map<String, Any>?
    ): Flux<DataBuffer> =
        Flux.from(input)
            .map { value ->
                val serializer: KSerializer<Any> =
                    runCatching {
                        resolver.resolve(ResolvableType.forInstance(value)) as KSerializer<Any?>
                    }.getOrElse {
                        throw EncodingException(it.message ?: "No serializer found", it)
                    } as KSerializer<Any>

                val out = ByteArrayOutputStream()
                json.encodeToStream(serializer, value, out)
                val buffer = bufferFactory.allocateBuffer(out.size())
                buffer.write(out.toByteArray())
                buffer
            }
}
