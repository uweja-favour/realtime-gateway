package realtime_gateway.infrastructure.serialization.http_message_reader_and_writer

import kotlinx.serialization.json.Json
import org.springframework.core.ResolvableType
import org.springframework.http.MediaType
import org.springframework.http.ReactiveHttpInputMessage
import org.springframework.http.codec.HttpMessageReader
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.lang.Nullable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import realtime_gateway.infrastructure.serialization.ReflectiveSerializerResolver
import java.nio.charset.StandardCharsets

class KotlinxSerializationJsonReader(
    private val json: Json,
    private val serializerResolver: ReflectiveSerializerResolver
) : HttpMessageReader<Any> {

    private val supportedMediaTypes = listOf(MediaType.APPLICATION_JSON)

    override fun getReadableMediaTypes(): List<MediaType> = supportedMediaTypes

    override fun canRead(elementType: ResolvableType, @Nullable mediaType: MediaType?): Boolean {
        return mediaType == null || supportedMediaTypes.any { it.isCompatibleWith(mediaType) }
    }

    override fun read(
        elementType: ResolvableType,
        message: ReactiveHttpInputMessage,
        hints: Map<String?, Any?>
    ): Flux<Any> {
        return readMono(elementType, message, hints).flux()
    }

    override fun read(
        actualType: ResolvableType,
        elementType: ResolvableType,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        hints: Map<String?, Any?>
    ): Flux<Any> {
        return read(elementType, request, hints)
    }

    override fun readMono(
        elementType: ResolvableType,
        message: ReactiveHttpInputMessage,
        hints: Map<String?, Any?>
    ): Mono<Any> {
        return message.body
            .reduce { buffer1, buffer2 -> buffer1.write(buffer2) } // accumulate DataBuffer
            .flatMap { buffer ->
                Mono.fromCallable {
                    val bytes = ByteArray(buffer.readableByteCount())
                    buffer.read(bytes)
                    val clazz = elementType.resolve()
                        ?: throw IllegalArgumentException("Cannot resolve type: $elementType")
                    val serializer = serializerResolver.resolve(elementType)
                    json.decodeFromString(serializer, bytes.toString(StandardCharsets.UTF_8))
                }.subscribeOn(Schedulers.boundedElastic())
            }
    }
}